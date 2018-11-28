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

CREATE DATABASE	BDSpotPer
ON
	PRIMARY
	(
		NAME = 'BDSpotPer',
		FILENAME = 'C:\FBD\BDSpotPer.mdf',
		SIZE = 10120KB,
		FILEGROWTH = 10%
	),
	FILEGROUP BDSpotPer_fg01
	(
		NAME = 'BDSpotPer_001',
		FILENAME = 'C:\FBD\BDSpotPer_001.ndf',
		SIZE = 2048KB,
		FILEGROWTH = 20%
	),
	(
		NAME ='BDSpotPer_002',
		FILENAME = 'C:\FBD\BDSpotPer_002.ndf',
		SIZE = 2024KB,
		FILEGROWTH = 10%
	),
	FILEGROUP BDSpotPer_fg02
	(
		NAME = 'BDSpotPer_003',
		FILENAME = 'C:\FBD\BDSpotPer_003.ndf',
		SIZE = 2048KB,
		FILEGROWTH = 20%
	)
	LOG ON
	(
		NAME = 'BDSpotPer_log',
		FILENAME = 'C:\FBD\BDSpotPer_log.ldf',
		SIZE = 1024KB,
		FILEGROWTH = 10%
	)
GO
USE BDSpotPer
GO

/*
Tabela "albuns", conterá informações dos albuns armazenados no banco de dados
campos:
- albun_id (identificador único)
- pr_compra (armazena o preço de compra do album)
- dt_compra (armazena a data da compra do album)
- dt_gravacao (armazena a data em que o album foi gravado)
- tipo_compra_id (armazena o id do tipo de compra presente na tabela "tipos_compra")
- gravadora_id (armazena o identificado da gravadora presente na tabela "gravadoras")
*/
CREATE TABLE albuns
	(
		albun_id TINYINT NOT NULL,
		pr_compra DECIMAL(8,2),
		dt_compra DATETIME,
		dt_gravacao CHAR(10),
		descr varchar(255),
		tipo_compra_id TINYINT NOT NULL,
		gravadora_id TINYINT NOT NULL,
		CONSTRAINT pk_albun PRIMARY KEY (albun_id) 
		-- adicionaremos as chaves estrangeiras depois de criada a tabela "tipo_compra" e "gravadora"
	) ON BDSpotPer_fg01

/*
Tabela "tipos_compra", conterá os tipos de compra efetuados na aquisição dos albuns
campos:
- tipo_id (identificador único de cada tipo de compra)
- descr(uma descrição do tipo de compra)
*/
CREATE TABLE tipos_compra 
	(
		tipo_id TINYINT NOT NULL,
		descr VARCHAR(20),
		CONSTRAINT pk_tipo PRIMARY KEY (tipo_id)
	) ON BDSpotPer_fg01

/*
Tabela "gravadoras", conterá informações referentes às gravadoras
campos:
- gravadora_id (identificador único de cada gravadora)
- nome (o nome da gravadora)
- endereco (o endereço da gravadora, a principio, somente uma linha de texto de 255 caracteres)
- website (o endereço para o site da gravadora na web)
*/

CREATE TABLE gravadoras 
	(
		gravadora_id TINYINT NOT NULL,
		nome VARCHAR(40),
		endereco VARCHAR(255),
		website VARCHAR(255),
		CONSTRAINT pk_gravadora PRIMARY KEY (gravadora_id)
	) ON BDSpotPer_fg01

/*
agora que as tabelas "tipos_compra" e "gravadoras" existem, podemos adicionar as FKs na tabela "albuns" 
*/
ALTER TABLE albuns
	ADD CONSTRAINT fk_gravadora_id
	FOREIGN KEY  (gravadora_id) REFERENCES gravadoras(gravadora_id);
ALTER TABLE albuns
	ADD CONSTRAINT fk_tipo_compra_id
	FOREIGN KEY  (tipo_compra_id) REFERENCES tipos_compra(tipo_id);

/*
Tabela "cds", conterá informações dos cds armazenados no banco de dados
campos:
- cd_id (identificador único de cada cd)
- albun_id (armazena o identificador do albun presente na tabela "albuns")
*/

CREATE TABLE cds
	(
		cd_id TINYINT NOT NULL,
		albun_id TINYINT NOT NULL,
		CONSTRAINT pk_cd_id 
		PRIMARY KEY (cd_id),
		
		CONSTRAINT fk_albun_id 
		FOREIGN KEY (albun_id)
		REFERENCES albuns(albun_id)
	) ON BDSpotPer_fg01

/*
Tabela "faixas", conterá as informações referentes a cada faixa de música
campos:
- faixa_id (identificador único de cada faixa)
- duracao (a duração no formato DATETIME de cada faixa)
- descr(uma descrição da faixa)
- tipo_gravacao_id (o identificados do tipo de gravação presente na tabela "tipos_gravacao")
- cd_id (o identificador do cd a qual esta faixa pertence, armazenado na tabela "cds")
*/

CREATE TABLE faixas
	(
		faixa_id TINYINT NOT NULL,
		duracao DATETIME,
		descr VARCHAR(255),
		tipo_gravacao_id TINYINT NOT NULL,
		cd_id TINYINT NOT NULL,
		
		CONSTRAINT pk_faixa_id
		PRIMARY KEY (faixa_id),
		CONSTRAINT fk_cd_id
		FOREIGN KEY( cd_id ) REFERENCES cds ( cd_id )
		-- adcicionaremos a fk tipo_gravacao_id depois de criada a tabela "tipos_gravacoes"
	) ON BDSpotPer_fg02


/*
Tabela "tipos_gravacoes", conterá os tipos de gravacoes de cada faixa
campos:
- tipo_gravacao_id (identificador único de cada tipo)
- descr(uma descrição do tipo de gravacao)
*/

CREATE TABLE tipos_gravacao
	(
		tipo_gravacao_id TINYINT NOT NULL,
		descr VARCHAR(60),
		CONSTRAINT pk_tipo_gravacao_id
		PRIMARY KEY (tipo_gravacao_id)
	) ON BDSpotPer_fg01

ALTER TABLE faixas
	ADD CONSTRAINT fk_tipo_gravacao_id
	FOREIGN KEY (tipo_gravacao_id) 
	REFERENCES tipos_gravacao(tipo_gravacao_id);
/*
Tabela "telefones", conterá os números de telefones e descrição do mesmo
campos:
- telefone_id (identificador único de cada telefone)
- descr(uma descrição do tipo de linha (residencial, BDSpotPer, celular etc))
- numero (o número do telefone)
- gravadora_id (o identificador da gravadora a qual o telefone é vinculado)
*/

CREATE TABLE telefones
	(
		telefone_id TINYINT NOT NULL,
		descr VARCHAR(20),
		numero VARCHAR(20),
		gravadora_id TINYINT NOT NULL,
		CONSTRAINT pk_telefone_id
		PRIMARY KEY (telefone_id),

		CONSTRAINT fk_gravadora_telefone_id
		FOREIGN KEY (gravadora_id) 
		REFERENCES gravadoras(gravadora_id)
	) ON BDSpotPer_fg01


/*
Tabela "playlists", conterá as informações referentes a cada playlist no sistema
campos:
- playlist_id (identificador único de cada playlist)
- nome (armazena um nome para a playlist)
- dt_criacao (armazena a data em que a playlist foi criada)
*/
CREATE TABLE playlists
	(
		playlist_id TINYINT NOT NULL,
		nome VARCHAR(20),
		dt_criacao DATETIME,
		CONSTRAINT pk_playlist_id
		PRIMARY KEY (playlist_id),
	) ON BDSpotPer_fg02
/*
Tabela "playlists_faixas", necessária para implementar a relação entre playlists e faixas
campos:
- playlist_id (identificador da playlist presente na tabela "playlists")
- faixa_id (identificados da faixa presente na tabela "faixas")
*/
CREATE TABLE playlists_faixas
	(
		playlist_id TINYINT NOT NULL,
		faixa_id TINYINT NOT NULL,
		qt_plays INT,
		dt_ultimo_play DATETIME,
		CONSTRAINT fk_playlist_faixa_id
		FOREIGN KEY (playlist_id) 
		REFERENCES playlists(playlist_id),
		CONSTRAINT fk_faixa_playlist_id
		FOREIGN KEY (faixa_id) 
		REFERENCES faixas(faixa_id)
	) ON BDSpotPer_fg02
/*
Tabela "periodosmusicais", armazena as informações de periodos musicais
campos:
- periodomusical_id (identificador único do periodo musical)
- descr(uma descricao do periodo musical)
- atividade_inicio (armazena a data de inicio da atividade daquele periodo musical) 
- atividade_fim (armazena a data em que acabou aquele periodo musical)
*/
CREATE TABLE periodosmusicais
	(
		periodomusical_id TINYINT NOT NULL,
		descr VARCHAR(65),
		atividade_inicio char(4),
		atividade_fim char(4),
		CONSTRAINT pk_periodomusical_id
		PRIMARY KEY (periodomusical_id),
	) ON BDSpotPer_fg01
/*
Tabela "paises", armazena as informações de cada pais
campos:
- pais_id (identificador único do pais)
- nome (nome do pais)
*/
CREATE TABLE paises
	(
		pais_id TINYINT NOT NULL,
		nome VARCHAR(65),
		CONSTRAINT pk_pais_id
		PRIMARY KEY (pais_id),
	) ON BDSpotPer_fg01
/*
Tabela "cidades", armazena as informações de cada cidade
campos:
- cidade_id (identificador único da cidade)
- nome (nome da cidade)
- pais_id (armazena o identificador do pais presente na tabela "paises")

*/
CREATE TABLE cidades
	(
		cidade_id TINYINT NOT NULL,
		nome VARCHAR(65),
		pais_id TINYINT NOT NULL,
		CONSTRAINT cidade_id
		PRIMARY KEY (cidade_id),
		CONSTRAINT fk_pais_id
		FOREIGN KEY (pais_id) 
		REFERENCES paises(pais_id)
	) ON BDSpotPer_fg01

/*
Tabela "compositores", armazena as informações de cada compositor
campos:
- compositor_id (identificador único do compositor)
- dt_morte (armazena a data em que o compositor morreu, ou nulo caso ainda esteja vivo)
- dt_nascimento ((armazena a data em que o compositor nasceu)
- nome (o nome do compositor) 
- cidade_id (armazena o identificador da cidade presente na tabela "cidades")
- periodomusical_id (armazena o identificador do periodo musical presente na tabela "periodosmusicais")
*/
CREATE TABLE compositores
	(
		compositor_id TINYINT NOT NULL,
		dt_morte CHAR(10),
		dt_nascimento CHAR(10),
		nome VARCHAR(65),
		cidade_id TINYINT NOT NULL,
		periodomusical_id TINYINT NOT NULL,
		CONSTRAINT pk_compositor_id
		PRIMARY KEY (compositor_id),
		CONSTRAINT fk_cidade_id
		FOREIGN KEY (cidade_id) 
		REFERENCES cidades(cidade_id),
		CONSTRAINT fk_periodomusical_id
		FOREIGN KEY (periodomusical_id) 
		REFERENCES periodosmusicais(periodomusical_id)
	) ON BDSpotPer_fg01
/*
Tabela "tipos_composicoes", armazenará as informações dos tipos de composição
campos:
- tipo_composicao_id (identificador único do tipo da composição)
- descr(uma descrição do tipo de composição)
*/

CREATE TABLE tipos_composicoes
	(
		tipo_composicao_id TINYINT NOT NULL,
		descr VARCHAR(65)
		CONSTRAINT pk_tipo_composicao_id
		PRIMARY KEY (tipo_composicao_id)
	) ON BDSpotPer_fg01
/*
Tabela "composicoes", armazenará as informações de cada composição
campos:
- composicao_id (identificador único da composição)
- compositor_id (identificados do compositor presente na tabela "compositores")
- tipo_composicao_id (identificados do tipo de composição presente na tabela "tipos_composicoes")
- descr(uma descrição de cada composição)
*/
CREATE TABLE composicoes
	(
		composicao_id TINYINT NOT NULL,
		compositor_id TINYINT NOT NULL,
		tipo_composicao_id TINYINT NOT NULL,
		descr VARCHAR(65),
		CONSTRAINT pk_composicao_id
		PRIMARY KEY (composicao_id),
		CONSTRAINT fk_compositor_id
		FOREIGN KEY (compositor_id) 
		REFERENCES compositores(compositor_id),
		CONSTRAINT fk_tipo_composicao_id
		FOREIGN KEY (tipo_composicao_id) 
		REFERENCES tipos_composicoes(tipo_composicao_id)
	) ON BDSpotPer_fg01

/*
Tabela "tipos_interpretes", armazena os tipos de interpretes
campos:
- tipo_interprete_id (identificador único do tipo de interprete)
- descr(descrição do tipo de interprete)
*/
CREATE TABLE tipos_interpretes
	(
		tipo_interprete_id TINYINT NOT NULL,
		descr VARCHAR(65),
		CONSTRAINT pk_tipo_interprete_id
		PRIMARY KEY (tipo_interprete_id)
	) ON BDSpotPer_fg01
/*
Tabela "interpretes", armazenará as informações de cada interprete
campos:
- interprete_id (identificador único do interprete)
- nome (o nome do interprete)
- tipo_interprete_id (identificados do tipo de interprete presente na tabela "tipos_interpretes")
*/
CREATE TABLE interpretes
	(
		interprete_id TINYINT NOT NULL,
		nome VARCHAR(65),
		tipo_interprete_id TINYINT NOT NULL,
		CONSTRAINT pk_interprete_id
		PRIMARY KEY (interprete_id),
		CONSTRAINT fk_tipo_interprete_id
		FOREIGN KEY (tipo_interprete_id) 
		REFERENCES tipos_interpretes(tipo_interprete_id)
	) ON BDSpotPer_fg01
/*
Tabela "faixas_interpretes_comp", necessária para fazer a ligação entre as tabelas faixas, interpretes e composições
campos:
- interprete_id (identificador do interprete presente na tabela "interpretes")
- faixa_id (identificados da faixa presente na tabela "faixas")
- composicao_id (identificados da composição presente na tabela "composições")
*/
CREATE TABLE faixas_interpretes_comp
	(
		interprete_id TINYINT NOT NULL,
		faixa_id TINYINT NOT NULL,
		composicao_id TINYINT NOT NULL,
		CONSTRAINT fk_interprete_id
		FOREIGN KEY (interprete_id) 
		REFERENCES interpretes(interprete_id),
		CONSTRAINT fk_faixa_id
		FOREIGN KEY (faixa_id) 
		REFERENCES faixas(faixa_id),
		CONSTRAINT fk_composicao_id
		FOREIGN KEY (composicao_id) 
		REFERENCES composicoes(composicao_id)
	) ON BDSpotPer_fg01
/*
Tabela "interpretes_composicoes", necessária para fazer a ligação entre as tabelas interpretes e composições
campos:
- interprete_id (identificador do interprete presente na tabela "interpretes")
- composicao_id (identificados da composição presente na tabela "composições")
*/
CREATE TABLE interpretes_composicoes
	(
		interprete_id TINYINT NOT NULL,
		composicao_id TINYINT NOT NULL,
		CONSTRAINT fk_interpretes_composicoes_id
		FOREIGN KEY (interprete_id) 
		REFERENCES interpretes(interprete_id),
		CONSTRAINT fk_composicoes_interpretes_id
		FOREIGN KEY (composicao_id) 
		REFERENCES composicoes(composicao_id)
	) ON BDSpotPer_fg01

GO

/*
	incluiremos alguns dados no banco de dados para poder executar as ações solicitadas no BDSpotPer
	==============================================================================================
*/

insert into paises values (1, 'Italia')
insert into cidades values (1, 'Veneza', 1)
insert into periodosmusicais values (1, 'Renascentista', '1450', '1600')
insert into periodosmusicais values (2, 'Barroco', '1600', '1750')
insert into periodosmusicais values (3, 'Clássico', '1750', '1810')
insert into periodosmusicais values (4, 'Romântico', '1810', '1900')
insert into periodosmusicais values (5, 'Moderno', '1900', '')

insert into compositores values (1, '1678-03-04', '1741-07-28', 'Antonio Lucio Vivaldi', 1, 2)
insert into tipos_composicoes values (1, 'Conserto')

insert into composicoes values (1, 1, 1, 'Concerto nº 1 em mi maior, "La primavera", RV 269')
insert into composicoes values (2, 1, 1, 'Concerto nº 2 em sol menor, "L''estate", RV 315')
insert into composicoes values (3, 1, 1, 'Concerto nº 3 em fá maior, "L''autunno", RV 293')
insert into composicoes values (4, 1, 1, 'Concerto nº 4 em fá menor, "L''inverno", RV 297')
insert into composicoes values (5, 1, 1, 'Concerto nº 5 em mi maior, "La tempesta di mare", RV 253')


insert into tipos_interpretes values (1, 'maestro')

insert into interpretes values (1, 'Gabriel Pavel', 1)

insert into tipos_gravacao values (1, 'ADD')
insert into tipos_gravacao values (2, 'DDD')

insert into gravadoras values (1, 'Advent Chamber Orchestra & Choir', null, null)

insert into tipos_compra values (1, 'fisica')
insert into tipos_compra values (2, 'download')

insert into albuns values (1, 5000, '2018-06-06 08:00:00', '1768-10-12', 'O melhor da música clássica', 1, 1)
insert into albuns values (2, 5000, '2018-06-06 08:00:00', '1768-10-12', 'O melhor da música clássica 2', 2, 1)
insert into albuns values (3, 5000, '2018-06-06 08:00:00', '1768-10-12', 'O melhor da música clássica 3', 2, 1)


insert into cds values (1, 1)
insert into cds values (2, 1)
insert into cds values (3, 1)


insert into faixas values (1, '00:05:20', 'Credo in unum Deum, do Credo em mi menor (RV 591) para coro e orquestra', 1, 1)
insert into faixas values (2, '00:04:20', 'Allegro - Adagio e spiccato - Allegro, do Concerto para dois violinos em ré menor, Op. 3 No. 11', 1, 1)
insert into faixas values (3, '00:08:30', 'Allegro do concerto para violino Primavera, das Quatro Estações', 1, 1)

insert into faixas values (4, '00:05:20', 'Credo in unum Deum, do Credo em mi menor (RV 591) para coro e orquestra', 1, 2)
insert into faixas values (5, '00:04:20', 'Allegro - Adagio e spiccato - Allegro, do Concerto para dois violinos em ré menor, Op. 3 No. 11', 1, 2)
insert into faixas values (6, '00:08:30', 'Allegro do concerto para violino Primavera, das Quatro Estações', 1, 2)

insert into faixas values (7, '00:05:20', 'Credo in unum Deum, do Credo em mi menor (RV 591) para coro e orquestra', 2, 3)
insert into faixas values (8, '00:04:20', 'Allegro - Adagio e spiccato - Allegro, do Concerto para dois violinos em ré menor, Op. 3 No. 11', 2, 3)
insert into faixas values (9, '00:08:30', 'Allegro do concerto para violino Primavera, das Quatro Estações', 2, 3)

insert into faixas_interpretes_comp values (1, 1, 1);
insert into faixas_interpretes_comp values (1, 2, 2);
insert into faixas_interpretes_comp values (1, 3, 3);

insert into playlists values (1, 'Clássicas', '17-10-2018 09:15:00')

insert into playlists_faixas values (1, 1, null, null)
insert into playlists_faixas values (1, 2, null, null)
insert into playlists_faixas values (1, 3, null, null)
GO

/*
Fim das inclusões de dados no banco de dados
========================================================================================================
*/
/*
	ITEM 6 DA PARTE 2 DO BDSpotPer, IMPLEMENTAR FUNÇÃO QUE TENHA COMO ENTRADA NOME OU PARTE DO NOME 
	DO COMPOSITOR E COMO SAIDA ALBUNS E OBRAS DESTE COMPOSITOR
	==============================================================================================
*/
CREATE FUNCTION BuscaCompositor(@Nome varchar)
RETURNS TABLE
AS
RETURN (SELECT 
		c.nome as 'Compositor',
		a.descr as 'Albun',
		co.descr as 'Obra composta'
		FROM  albuns a,
			  compositores c,
			  composicoes co,
			  faixas_interpretes_comp fic,
			  faixas f,
			  cds cd
        WHERE a.albun_id = cd.albun_id AND
			  f.cd_id = cd.cd_id AND
			  fic.faixa_id = f.faixa_id AND
			  fic.composicao_id = co.composicao_id AND
			  co.compositor_id = c.compositor_id AND
			  c.nome like '%' + @Nome + '%')
/*
===================================================================================================
*/

