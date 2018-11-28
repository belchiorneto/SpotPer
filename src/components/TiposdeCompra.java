/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
