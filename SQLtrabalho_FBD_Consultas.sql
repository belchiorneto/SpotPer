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

/*
	ITEM 6 DA PARTE 2 DO BDSpotPer, IMPLEMENTAR FUNÇÃO QUE TENHA COMO ENTRADA NOME OU PARTE DO NOME 
	DO COMPOSITOR E COMO SAIDA ALBUNS E OBRAS DESTE COMPOSITOR
	==============================================================================================
*/
USE BDSpotPer
GO
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
/*
===================================================================================================
*/

 --select * from BuscaCompositor('vivaldi');

 /*
 3ª A) Um album com faixas de musicas do periodo barroco só pode ser adquirido caso o tipo de gravação
 seja DDD
 Estratégia: criação de um trigger que irá verificar, no momento da inclusão/update de faixas
 1 - se o periodo é barroco
 2 - se o tipo de gravação é DDD
 */
/*
CREATE TRIGGER TGR_RESTRICAO_BARROCO
ON faixas
FOR INSERT,UPDATE
AS
BEGIN
    DECLARE
    @PERIODO CHAR(7),
    @TIPO_GRAVACAO CHAR(3),
	@ALBUN_ID TINYINT
	
 
    SELECT @ALBUN_ID = id_albun FROM INSERTED
	SELECT @PERIODO = descr FROM dbo.periodosmusicais
    UPDATE CAIXA SET SALDO_FINAL = SALDO_FINAL + @VALOR
    WHERE DATA = @DATA
END
GO
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
    @ALBUN_ID TINYINT,
	@QTDE_FAIXAS TINYINT,
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


-- 4ª A) faixa deve possuir indice primario sobre o atributo codigo do album
-- removendo as chaves estrangeiras
ALTER TABLE faixas_composicoes
DROP CONSTRAINT fk_faixa_id
ALTER TABLE faixas_compositores
DROP CONSTRAINT fk_faixas_compositores_id
ALTER TABLE faixas_interpretes
DROP CONSTRAINT fk_faixas_interpretes_id
ALTER TABLE playlists_faixas
DROP CONSTRAINT fk_faixa_playlist_id
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
PRIMARY KEY (faixa_id)

ALTER TABLE faixas_composicoes
ADD CONSTRAINT fk_faixa_id
FOREIGN KEY (faixa_id) 
REFERENCES faixas(faixa_id)
ON DELETE CASCADE
ON UPDATE CASCADE

ALTER TABLE faixas_compositores
ADD CONSTRAINT fk_faixas_compositores_id
FOREIGN KEY (faixa_id) 
REFERENCES faixas(faixa_id)
ON DELETE CASCADE
ON UPDATE CASCADE

ALTER TABLE faixas_interpretes
ADD CONSTRAINT fk_faixas_interpretes_id
FOREIGN KEY (faixa_id) 
REFERENCES faixas(faixa_id)
ON DELETE CASCADE
ON UPDATE CASCADE

ALTER TABLE playlists_faixas
ADD CONSTRAINT fk_faixa_playlist_id
FOREIGN KEY (faixa_id) 
REFERENCES faixas(faixa_id)
ON DELETE CASCADE
ON UPDATE CASCADE

GO
--4ª B) faixa deve possuir indice secundario sobre o atributo composicao
CREATE NONCLUSTERED INDEX I2_faixa
	ON faixas (tipo_composicao_id)
	WITH (FILLFACTOR = 100);  
GO

--5ª visao

CREATE VIEW V_album 
WITH schemabinding
AS
	SELECT 
		p.nome AS 'Nome da PlayList', count(DISTINCT(f.albun_id)) as 'Quantidade de Albuns' 
	FROM 
		dbo.playlists p, dbo.playlists_faixas p2, dbo.faixas f
	WHERE 
		p2.faixa_id = f.faixa_id and 
		p2.playlist_id = p.playlist_id
	GROUP BY p.nome

GO
--select * from V_album

--ITEM (i)(c)
--data de compra do album deve ser de um ano posterior a data 01-01-2000 00:00:00
ALTER TABLE albuns ADD CONSTRAINT CK_dt_compra
CHECK (dt_compra >= '01-01-2000 00:00:00')
GO
--Considerando tipo compra 1= ADD e 2=DDD

CREATE TRIGGER media_pr_compra_trigger
ON dbo.albuns
FOR INSERT,UPDATE
AS
BEGIN
    DECLARE
    @TIPO_COMPRA TINYINT,
	@PRECO_ALBUM DECIMAL(8,2),
	@MEDIA_PRECO_ALBUM DECIMAL(8,2),
	@str_invalido char(50),
	@cod TINYINT
	
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
 Questão 9, Item a
*/
SELECT descr
FROM albuns
WHERE pr_compra > 
			(
				SELECT AVG(pr_compra) 
				FROM albuns
			)
			
/*
 Questão 9, Item b
*/
GO

SELECT g.nome
FROM gravadoras g inner join albuns a
	ON g.gravadora_id=a.gravadora_id
	inner join faixas f
	ON a.albun_id=f.albun_id
	inner join playlists_faixas pf
	ON pf.faixa_id=f.faixa_id
	inner join faixas_compositores fc
	ON f.faixa_id= fc.faixa_id 
	inner join compositores c
	ON fc.faixa_id = c.compositor_id
WHERE c.nome = 'Devorack'
GO

/*
 Questão 9, Item c
*/

CREATE FUNCTION ListaGravadoras()
RETURNS TABLE
AS
RETURN (SELECT count(p.playlist_id) as 'qt_plays', g.nome as 'nome_gravadora'
			FROM gravadoras g, albuns a, faixas f, playlists p, playlists_faixas pf
			WHERE a.albun_id = f.albun_id and
				f.faixa_id = pf.faixa_id and
				pf.playlist_id = p.playlist_id
			GROUP BY p.playlist_id, g.nome, a.descr)
GO

SELECT DISTINCT(nome_gravadora) as 'Gravadora', MAX(qt_plays) as 'playlists'
FROM ListaGravadoras()
GROUP BY nome_gravadora, qt_plays

GO

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
GO
CREATE FUNCTION tempo_album(@album_id tinyint)
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