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

/**
 *
 * @author Belchior
 */
public class TiposdeCompra {
    int tipo_id;
    String descricao;
   
    public void setTipo_id(int novoTipo_id){
        tipo_id = novoTipo_id;
    }
    public int getTipo_id(){
        return tipo_id;
    }
    public void setDescricao(String novaDescricao){
        descricao = novaDescricao;
    }
    public String getDescricao(){
        return descricao;
    }
}
