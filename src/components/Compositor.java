/*
 * UFC - Universidade Federal do Ceará
 * FDB - Fundamentos de Bancos de Dados
 * Professor: ANGELO RONCALLI ALENCAR BRAYNER
 * Equipe:
 *  Everson Magalhaes Cavalcante
 *  Belchior Dameao de Araújo Neto
 *  Este script faz parte do projeto BDSpotPer
 *  trabalho prático necessário como parte da nota 
 *  para a cadeira de Fundamentos de Bancos de Dados 2018.2

 */
package components;

import java.sql.Array;

/**
 *
 * @author Belchior
 */
public class Compositor {
    int compositor_id;
    String dt_morte;
    String dt_nascimento;
    String nome;
    String Cidade;
    String Pais;
    Composicao[] composicoes;
    public void setNome(String Nome){
        nome = Nome;
    }
}
