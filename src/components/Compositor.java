/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
