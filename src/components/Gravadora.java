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

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Belchior
 */
public class Gravadora {
    int id;
    String nome, endereco, website;
    String[][] camposEditaveis = {
        {"id_gravadora", "Número da gravadora:"},
        {"nome", "Nome da gravadora:"},
        {"endereco", "Endereco da gravadora:"},
        {"website", "Website da gravadora:"},
    };
    public Gravadora(int novoid, String novoNome){
        id = novoid;
        nome = novoNome;
    }
    public void setId(int newId){
        id = newId;
    }
    public int getId(){
        return id;
    }
    public void setNome(String newNome){
        nome = newNome;
    }
    public String getNome(){
        return nome;
    }
    public void setEnderco(String novoEndereco){
        endereco = novoEndereco;
    }
    public String getEndereco(){
        return endereco;
    }
    public void setWebsite(String novoWebsite){
        website = novoWebsite;
    }
    public String getWebsite(){
        return website;
    }
    
    public void showLabels(GridPane g){
        for(int i = 0; i < camposEditaveis.length; i++){
            g.add(new Label(camposEditaveis[i][1]), 0, i + 1);
            g.add(new TextField(), 1, i + 1);
        }
    }
}
