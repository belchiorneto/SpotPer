/*==============================================================================================
BDSpotPer para disciplina de Fundamento de Banco de Dados
Professor: ANGELO RONCALLI ALENCAR BRAYNER
Equipe: 
	Belchior Dameao de Araújo Neto - 384343
	Everson Magalhaes Cavalcante

Resumo do BDSpotPer:
Projetar banco de dados para um aplicativo de gerenciamento de músicas semelhante ao Spotfy (SpotPer)
Este documento faz parte do projeto e contém as informações e scripts necessários para criação do banco de dados
em ambiente "SQL Server" ou "PostgreSQL"
================================================================================================*/


USE BDSpotPer
GO

/*
 3ª A) Um album com faixas de musicas do periodo barroco só pode ser adquirido caso o tipo de gravação
 seja DDD
 Estratégia: criação de um trigger que irá verificar, no momento da inclusão/update de faixas
 1 - se o periodo é barroco
 2 - se o tipo de gravação é DDD
 
 */

/*
 3ª B) um Album não pode ter mais que 64 faixas (musicas)
 Estratégia: Criação de um trigger na tabela faixas pra contar a quantidade de faixas que já existem 
 para o id do albun que está sendo inserido
 */

CREATE TRIGGER albumLimite_trigger
ON dbo.faixas
FOR INSERT,UPDATE
AS
BEGIN
    DECLARE
    @ALBUN_ID SMALLINT,
	@QTDE_FAIXAS SMALLINT,
	@str_invalido char(40)
	
	SELECT @ALBUN_ID = albun_id FROM INSERTED
	SELECT @QTDE_FAIXAS = COUNT(*) 
	FROM dbo.faixas
	WHERE albun_id = @ALBUN_ID
	set @str_invalido='Quantidade de faixas: '+cast(@QTDE_FAIXAS as varchar(2))+
      ' inválida'
	IF @QTDE_FAIXAS > 63
	BEGIN
		raiserror(@str_invalido,16,1)
		ROLLBACK TRANSACTION
	END
END
GO

/*
 3ª C) No caso de remoção de um album, todas as faixas devem ser removidas
 Estratégia: Foi incluso no script de criação das tabelas específicas, 
 a clausula ON DELETE CASCADE E ON UPDATE CASCADE, no momento de criação das chaves estrangeiras

 */

/*
4ª A) faixa deve possuir indice primario sobre o atributo codigo do album
removemos as chaves estrangeiras afetadas, criamos o indice e depois recriamos as chaves estrangeiras
*/
ALTER TABLE faixas_composicoes
DROP CONSTRAINT pk_faixa_composicao_id
ALTER TABLE faixas_compositores
DROP CONSTRAINT pk_faixa_compositor_id
ALTER TABLE faixas_interpretes
DROP CONSTRAINT pk_faixa_interpret_id
ALTER TABLE playlists_faixas
DROP CONSTRAINT pk_playlist_faixa_id
-- removendo a chave primaria
ALTER TABLE faixas
DROP CONSTRAINT pk_faixa_id
/*
agora conseguimos criar o indice
=================================
*/
CREATE CLUSTERED INDEX I_faixa
	ON faixas (albun_id)
	WITH (FILLFACTOR = 100);  
/*
precisamos recriar as chaves removidas
=================================
*/

ALTER TABLE faixas
ADD CONSTRAINT pk_faixa_id
PRIMARY KEY (faixa_id, albun_id)

ALTER TABLE faixas_composicoes
ADD CONSTRAINT pk_faixa_composicao_id
PRIMARY KEY (faixa_id, composicao_id) 

ALTER TABLE faixas_compositores
ADD CONSTRAINT pk_faixas_compositores_id
PRIMARY KEY (faixa_id, compositor_id) 


ALTER TABLE faixas_interpretes
ADD CONSTRAINT pk_faixas_interpretes_id
PRIMARY KEY (faixa_id, interprete_id) 


ALTER TABLE playlists_faixas
ADD CONSTRAINT pk_faixa_playlist_id
PRIMARY KEY (playlist_id, faixa_id) 

GO
--4ª B) faixa deve possuir indice secundario sobre o atributo composicao
CREATE NONCLUSTERED INDEX I2_faixa
	ON faixas (tipo_composicao_id)
	WITH (FILLFACTOR = 100);  
GO

/*
5ª Criar Visão materializada com nome da playlist e quantidade de albuns
Alterações necessárias para que se possa incluir index em uma view, segundo documentação em:
https://docs.microsoft.com/en-us/sql/relational-databases/views/create-indexed-views?view=sql-server-2017
*/
SET NUMERIC_ROUNDABORT OFF;  
SET ANSI_PADDING, ANSI_WARNINGS, CONCAT_NULL_YIELDS_NULL, ARITHABORT,  
    QUOTED_IDENTIFIER, ANSI_NULLS ON;  
GO  

IF OBJECT_ID ('V_album', 'view') IS NOT NULL  
DROP VIEW V_album ; 
GO


CREATE VIEW V_album 
WITH schemabinding
AS
	SELECT 
		p.playlist_id, p.nome AS 'Nome da PlayList', COUNT_BIG(*) as 'Quantidade de Albuns' 
	FROM 
		dbo.playlists p, dbo.playlists_faixas p2, dbo.faixas f
	WHERE 
		p2.faixa_id = f.faixa_id and 
		p2.playlist_id = p.playlist_id
	GROUP BY p.nome, p.playlist_id

GO

--Criando um index para a View.  
CREATE UNIQUE CLUSTERED INDEX inx_V_album  
    ON V_album (playlist_id);  
GO  

-- select * from V_album -- TESTE

/*
	6ª) IMPLEMENTAR FUNÇÃO QUE TENHA COMO ENTRADA NOME OU PARTE DO NOME 
	DO COMPOSITOR E COMO SAIDA ALBUNS E OBRAS DESTE COMPOSITOR
	==============================================================================================
*/
CREATE FUNCTION BuscaCompositor(@Nome varchar)
RETURNS TABLE
AS
RETURN (select  distinct(c.nome) as 'Compositor',
		a.descr as 'Albun',
		co.descr as 'Obra composta'
		FROM  albuns a,
			  compositores c,
			  composicoes co,
			  faixas_composicoes fc,
			  faixas_compositores fcom,
			  faixas f
        WHERE a.albun_id = f.albun_id AND
			  f.faixa_id = fc.faixa_id AND
			  f.faixa_id = fcom.faixa_id AND
			  fcom.compositor_id = c.compositor_id AND
			  fc.composicao_id = co.composicao_id AND
			  c.nome like '%' + @Nome + '%')
GO
--select * from BuscaCompositor('vivaldi'); -- teste


/*
 Questão 9, Item a: Listar albuns com preço de compra maior que a média de compra dos preços de todos os albuns
*/
SELECT descr
FROM albuns
WHERE pr_compra > 
			(
				SELECT AVG(pr_compra) 
				FROM albuns
			)
			
GO
/*
 Questão 9, Item b: Listar nome da gravadora com maior número de playlists que possuem pelo menos uma faixa
 composta por um compositor
*/

CREATE FUNCTION ListaGravadoras(@nomeCompositor varchar(50))
RETURNS TABLE
AS
RETURN (SELECT count(p.playlist_id) as 'qt_plays', g.nome as 'nome_gravadora'
			FROM 
				gravadoras g, 
				albuns a, 
				faixas f, 
				faixas_compositores fc,
				compositores c,
				playlists p, 
				playlists_faixas pf
			WHERE a.albun_id = f.albun_id AND
				f.faixa_id = pf.faixa_id AND
				fc.faixa_id = f.faixa_id AND
				fc.compositor_id = c.compositor_id AND
				pf.playlist_id = p.playlist_id AND
				c.nome like '%' + @nomeCompositor + '%'
			GROUP BY p.playlist_id, g.nome, a.descr)
GO
SELECT DISTINCT(nome_gravadora) as 'Gravadora', MAX(qt_plays) OVER (PARTITION BY nome_gravadora) as 'playlists'
FROM ListaGravadoras('Vivaldi')
GROUP BY nome_gravadora, qt_plays
GO

/*
 Questão 9, Item C: Listar nome do compositor com maior numero de faixas nas playlists existentes
*/

CREATE FUNCTION ListaCompositor()
RETURNS TABLE
AS
RETURN (SELECT count(f.faixa_id) as 'qt_faixas', c.nome as 'nome_compositor'
			FROM 
				faixas f, 
				faixas_compositores fc,
				compositores c,
				playlists p, 
				playlists_faixas pf
			WHERE 
				f.faixa_id = pf.faixa_id AND
				fc.faixa_id = f.faixa_id AND
				fc.compositor_id = c.compositor_id AND
				pf.playlist_id = p.playlist_id
			GROUP BY p.playlist_id, c.nome)
GO

SELECT DISTINCT(nome_compositor) as 'Compositor', MAX(qt_faixas) OVER (PARTITION BY nome_compositor) as 'Faixas'
FROM ListaCompositor()
GROUP BY nome_compositor, qt_faixas
/*
 Questão 9, Item D
*/
/*
SELECT nome_playlist
	FROM playlist p
		inner join playlist pf
			ON pf.playlist_id=p.playlist_id
		inner join faixas f
			ON f.faixa_id=pf.faixa_id
		inner join faixas_composicoes fc
			ON fc.faixa_id=f.faixa_id
		inner join composicoes c
			ON fc.composicao_id= c.composicao_id
		inner join compositores co
			ON co.compositor_id = f.com
		inner join periodosmusicais pm
			ON pm.periodomusical_id = 
WHERE c.descr = 'Concerto' and pm.descr = 'Barroco'
GO
*/

/*
===================================================================================================
OUTRAS RESTRIÇÕES
ITEM (i)(c)
data de compra do album deve ser de um ano posterior a data 01-01-2000 00:00:00
*/
ALTER TABLE albuns ADD CONSTRAINT CK_dt_compra
CHECK (dt_compra >= '01-01-2000 00:00:00')
GO
/*
ITEM (i)(E)
Considerando tipo compra 1= ADD e 2=DDD
*/

CREATE TRIGGER media_pr_compra_trigger
ON dbo.albuns
FOR INSERT,UPDATE
AS
BEGIN
    DECLARE
    @TIPO_COMPRA SMALLINT,
	@PRECO_ALBUM DECIMAL(8,2),
	@MEDIA_PRECO_ALBUM DECIMAL(8,2),
	@str_invalido char(50),
	@cod SMALLINT
	
	SELECT @cod=albun_id FROM INSERTED 
	SELECT @PRECO_ALBUM = pr_compra FROM INSERTED
	SELECT @MEDIA_PRECO_ALBUM = AVG(pr_compra) FROM ALBUNS WHERE albun_id <> @cod
	SELECT @TIPO_COMPRA=tipo_compra_id FROM INSERTED
	set @str_invalido='Valor do album: '+cast(@PRECO_ALBUM as varchar(10))+
      ' maior que 3*Media= ' +cast(3*@MEDIA_PRECO_ALBUM as varchar(10))+ ' '
	IF (@TIPO_COMPRA=2) AND (@PRECO_ALBUM) > 3*(@MEDIA_PRECO_ALBUM)
	BEGIN
		raiserror(@str_invalido,16,1)
		ROLLBACK TRANSACTION
	END
END

GO

/*
Conferir tempo total de uma playlist. Na clausula where, modificar o id
*/

declare @T int
 
set @T = (SELECT sum(DATEPART(SECOND, duracao) + 
                     (DATEPART(MINUTE, duracao)* 60)+ 
                     (DATEPART(HOUR, duracao))*3600) 
          FROM faixas f inner join playlists_faixas pf on pf.playlist_id=f.albun_id where pf.playlist_id=1)
 select @T
SELECT CONVERT(varchar, DATEADD(ms, @T * 1000, 0), 114) 

/*Conferir tempo total de um album. 
Essa funcao pode retornar também o tempo de uma playlist, copiando exatamente o codigo anterior na funcao abaixo.
Na clausula where, modificar o id*/
/*
GO
CREATE FUNCTION tempo_album(@album_id SMALLINT)
returns time
as
BEGIN
	declare 
	@T int,
	@tempo_total time
 
	set @T = (SELECT sum(DATEPART(SECOND, duracao) + 
						 (DATEPART(MINUTE, duracao)* 60)+ 
						 (DATEPART(HOUR, duracao))*3600) 
			  FROM faixas f inner join albuns a on a.albun_id=f.faixa_id where a.albun_id=@albun_id)
	set @tempo_total = SELECT CONVERT(varchar, DATEADD(ms, @T * 1000, 0), 114) 
return @tempo_total
END
*/