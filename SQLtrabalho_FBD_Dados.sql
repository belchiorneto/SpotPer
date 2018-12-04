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
	incluiremos alguns dados no banco de dados para poder executar as ações solicitadas no BDSpotPer
	==============================================================================================
*/
USE BDSpotPer
GO

insert into paises  (pais_id,	nome) 
			 values (1, 'Italia')
insert into cidades (cidade_id, nome, pais_id)
			 values (1, 'Veneza', 1)
insert into periodosmusicais(periodomusical_id, descr, atividade_inicio, atividade_fim)
					  values(1, 'Renascentista', '1450', '1600')
insert into periodosmusicais(periodomusical_id, descr, atividade_inicio, atividade_fim)
				 	 values (2, 'Barroco', '1600', '1750')
insert into periodosmusicais(periodomusical_id, descr, atividade_inicio, atividade_fim)
				     values (3, 'Clássico', '1750', '1810')
insert into periodosmusicais(periodomusical_id, descr, atividade_inicio, atividade_fim)
					 values (4, 'Romântico', '1810', '1900')
insert into periodosmusicais(periodomusical_id, descr, atividade_inicio, atividade_fim)
					 values (5, 'Moderno', '1900', '')

insert into compositores(compositor_id, dt_nascimento, dt_morte, nome, cidade_id, periodomusical_id)
				 values (1, '1678-03-04', '1741-07-28', 'Antonio Lucio Vivaldi', 1, 2)
insert into tipos_composicoes(tipo_composicao_id, descr)
					  values (1, 'Concerto')
insert into tipos_composicoes(tipo_composicao_id, descr)
					  values (2, 'Partitura')

insert into composicoes(composicao_id , tipo_composicao_id, descr)
			    values (1, 1, 'Concerto nº 1 em mi maior, "La primavera", RV 269')
insert into composicoes(composicao_id , tipo_composicao_id, descr)
				values (2, 1, 'Concerto nº 2 em sol menor, "L''estate", RV 315')
insert into composicoes(composicao_id , tipo_composicao_id, descr)
			    values (3, 1, 'Concerto nº 3 em fá maior, "L''autunno", RV 293')
insert into composicoes(composicao_id , tipo_composicao_id, descr)
				values (4, 1, 'Concerto nº 4 em fá menor, "L''inverno", RV 297')
insert into composicoes(composicao_id , tipo_composicao_id, descr)
				values (5, 1, 'Concerto nº 5 em mi maior, "La tempesta di mare", RV 253')


insert into tipos_interpretes(tipo_interprete_id, descr)
					  values (1, 'maestro')

insert into interpretes(interprete_id, nome, tipo_interprete_id)
				values (1, 'Gabriel Pavel', 1)

insert into tipos_gravacao values (1, 'ADD')
insert into tipos_gravacao values (2, 'DDD')

insert into gravadoras values (1, 'Advent Chamber Orchestra & Choir', null, null)
insert into gravadoras values (2, 'Sony', null, null)
insert into gravadoras values (3, 'Universal', null, null)

insert into tipos_compra values (1, 'fisica')
insert into tipos_compra values (2, 'download')

insert into albuns(albun_id, pr_compra, dt_compra, dt_gravacao, descr, tipo_compra_id, gravadora_id)
		   values (1, 5000, '2018-06-06 08:00:00', '1768-10-12', 'O melhor da música clássica', 1, 1)
insert into albuns(albun_id, pr_compra, dt_compra, dt_gravacao, descr, tipo_compra_id, gravadora_id)
		   values (2, 5000, '2018-06-06 08:00:00', '1768-10-12', 'O melhor da música clássica 2', 2, 3)
insert into albuns(albun_id, pr_compra, dt_compra, dt_gravacao, descr, tipo_compra_id, gravadora_id)
		   values (3, 5000, '2018-06-06 08:00:00', '1768-10-12', 'O melhor da música clássica 3', 2, 3)
		   

insert into faixas(faixa_id, duracao, descr, tipo_gravacao_id, albun_id, tipo_composicao_id)
	       values (1, '00:05:20', 'Credo in unum Deum, do Credo em mi menor (RV 591) para coro e orquestra', 1, 1, 1)
insert into faixas(faixa_id, duracao, descr, tipo_gravacao_id, albun_id, tipo_composicao_id)
	       values (2, '00:04:20', 'Allegro - Adagio e spiccato - Allegro, do Concerto para dois violinos em ré menor, Op. 3 No. 11', 1, 1, 1)
insert into faixas(faixa_id, duracao, descr, tipo_gravacao_id, albun_id, tipo_composicao_id)
		   values (3, '00:08:30', 'Allegro do concerto para violino Primavera, das Quatro Estações', 1, 1, 1)

insert into faixas(faixa_id, duracao, descr, tipo_gravacao_id, albun_id, tipo_composicao_id)
			values(4, '00:05:20', 'Credo in unum Deum, do Credo em mi menor (RV 591) para coro e orquestra', 1, 2, 1)
insert into faixas(faixa_id, duracao, descr, tipo_gravacao_id, albun_id, tipo_composicao_id)
		    values(5, '00:04:20', 'Allegro - Adagio e spiccato - Allegro, do Concerto para dois violinos em ré menor, Op. 3 No. 11', 1, 2, 1)
insert into faixas(faixa_id, duracao, descr, tipo_gravacao_id, albun_id, tipo_composicao_id)
			values(6, '00:08:30', 'Allegro do concerto para violino Primavera, das Quatro Estações', 1, 2, 1)

insert into faixas(faixa_id, duracao, descr, tipo_gravacao_id, albun_id, tipo_composicao_id)
			values(7, '00:05:20', 'Credo in unum Deum, do Credo em mi menor (RV 591) para coro e orquestra', 2, 3, 1)
insert into faixas(faixa_id, duracao, descr, tipo_gravacao_id, albun_id, tipo_composicao_id)
			values(8, '00:04:20', 'Allegro - Adagio e spiccato - Allegro, do Concerto para dois violinos em ré menor, Op. 3 No. 11', 2, 3, 1)
insert into faixas(faixa_id, duracao, descr, tipo_gravacao_id, albun_id, tipo_composicao_id)
			values(9, '00:08:30', 'Allegro do concerto para violino Primavera, das Quatro Estações', 2, 3, 1)

insert into faixas_interpretes (faixa_id, interprete_id) values (1, 1);
insert into faixas_interpretes (faixa_id, interprete_id) values (2, 1);
insert into faixas_interpretes (faixa_id, interprete_id) values (3, 1);
insert into faixas_interpretes (faixa_id, interprete_id) values (4, 1);
insert into faixas_interpretes (faixa_id, interprete_id) values (5, 1);
insert into faixas_interpretes (faixa_id, interprete_id) values (6, 1);
insert into faixas_interpretes (faixa_id, interprete_id) values (7, 1);
insert into faixas_interpretes (faixa_id, interprete_id) values (8, 1);
insert into faixas_interpretes (faixa_id, interprete_id) values (9, 1);

insert into faixas_composicoes(faixa_id, composicao_id) values (1, 1);
insert into faixas_composicoes(faixa_id, composicao_id) values (2, 1);
insert into faixas_composicoes(faixa_id, composicao_id) values (3, 1);
insert into faixas_composicoes(faixa_id, composicao_id) values (4, 2);
insert into faixas_composicoes(faixa_id, composicao_id) values (5, 2);
insert into faixas_composicoes(faixa_id, composicao_id) values (6, 1);
insert into faixas_composicoes(faixa_id, composicao_id) values (7, 2);
insert into faixas_composicoes(faixa_id, composicao_id) values (8, 1);
insert into faixas_composicoes(faixa_id, composicao_id) values (9, 1);

insert into faixas_compositores(faixa_id, compositor_id) values (1, 1);
insert into faixas_compositores(faixa_id, compositor_id) values (2, 1);
insert into faixas_compositores(faixa_id, compositor_id) values (3, 1);
insert into faixas_compositores(faixa_id, compositor_id) values (4, 1);
insert into faixas_compositores(faixa_id, compositor_id) values (5, 1);
insert into faixas_compositores(faixa_id, compositor_id) values (6, 1);
insert into faixas_compositores(faixa_id, compositor_id) values (7, 1);
insert into faixas_compositores(faixa_id, compositor_id) values (8, 1);
insert into faixas_compositores(faixa_id, compositor_id) values (9, 1);

insert into playlists values (1, 'Clássicas', '17-10-2018 09:15:00', null, null)
insert into playlists values (2, 'Clássicas anteriores a 1600', '17-10-2018 09:15:00', null, null)
insert into playlists values (3, 'Clássicas Modernas', '17-10-2018 09:15:00', null, null)

insert into playlists_faixas values (1, 1, null, null)
insert into playlists_faixas values (1, 2, null, null)
insert into playlists_faixas values (1, 3, null, null)

insert into playlists_faixas values (2, 3, null, null)
insert into playlists_faixas values (2, 2, null, null)
insert into playlists_faixas values (2, 4, null, null)

insert into playlists_faixas values (3, 4, null, null)
insert into playlists_faixas values (3, 5, null, null)
insert into playlists_faixas values (3, 6, null, null)

insert into playlists_faixas values (3, 7, null, null)
insert into playlists_faixas values (3, 8, null, null)
insert into playlists_faixas values (3, 9, null, null)
GO

/*
Fim das inclusões de dados no banco de dados
========================================================================================================
*/