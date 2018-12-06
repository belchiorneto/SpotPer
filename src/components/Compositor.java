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

import db.DbUtils;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author Belchior
 */
public class Compositor {
    private int compositor_id;
    private String nome;
    private String dt_morte;
    private String dt_nascimento;
    private int cidadeId;
    private int periodoMusicalId;
    
    Composicao[] composicoes;
    public void setNewId(){
        int id = 0; 
        String SQL = "";
        SQL += "SELECT " 
                + "MAX(compositor_id) as compositor_id "
                + "FROM "
                + "compositores";
        ResultSet rs = DbUtils.Lista(SQL);
        try{
            if(rs.next()) {
                id = Integer.parseInt(rs.getString("compositor_id")) + 1;
                System.out.println(id);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        compositor_id = id;
    }
    public void setId(int newId){
        compositor_id = newId;
    }
    public int getId(){
        return compositor_id;
    }
    public void setcidadeId(int newcidadeId){
        cidadeId = newcidadeId;
    }
    public int getcidadeId(){
        return cidadeId;
    }
    public void setperiodoMusicalId(int newperiodoMusicalId){
        periodoMusicalId = newperiodoMusicalId;
    }
    public int getperiodoMusicalId(){
        return periodoMusicalId;
    }
    public void setNome(String Nome){
        nome = Nome;
    }
    public String getNome(){
        return nome;
    }
    public void setDtMorte(String dtMorte){
        dt_morte = dtMorte;
    }
    public String getDtMorte(){
        return dt_morte;
    }
    public void setDtNascimento(String dtNascimento){
        dt_nascimento = dtNascimento;
    }
    public String getDtNascimento(){
        return dt_nascimento;
    }
    public boolean exist(){
        String SQL = "";
        SQL += "SELECT " 
                + "compositor_id "
                + "FROM "
                + "compositores "
                + "WHERE "
                + "compositor_id = " + getId();
        ResultSet rs = DbUtils.Lista(SQL);
        try{
            if(rs.next()) {
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public void addToDb(){
        String campos = "compositor_id, dt_morte, dt_nascimento, nome, cidade_id, periodomusical_id";
        String dados = getId() + ", null, null ," 
                +"'"+ getNome() + "',"
                + "'1', "
                + "'1'";
        String tabela = "compositores";
        DbUtils.Insert(campos, dados, tabela);
    }
    public HashMap<String, Compositor> listaCompositores(){
        HashMap<String, Compositor> compositores = new HashMap<>();
        
        String SQL = "";
        SQL += "SELECT " 
                + " * "
                + "FROM "
                + "compositores";

        ResultSet rs = DbUtils.Lista(SQL);
        try{
            while (rs.next()) { 
                Compositor compositor = new Compositor();
                compositor.setId(Integer.parseInt(rs.getString("compositor_id")));
                compositor.setNome(rs.getString("nome"));
                compositor.setDtMorte(rs.getString("dt_morte"));
                compositor.setDtNascimento(rs.getString("dt_nascimento"));
                compositor.setDtMorte(rs.getString("dt_morte"));
                compositor.setcidadeId(Integer.parseInt(rs.getString("cidade_id")));
                compositor.setperiodoMusicalId(Integer.parseInt(rs.getString("periodomusical_id")));
                compositores.put(String.valueOf(compositor.getId()), compositor);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return compositores;
    }
}
