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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Belchior
 */
public class Albun {
    private int albun_id;
    private float pr_compra;
    private String dt_compra, dt_gravacao, tipo_compra, descr;
    private int tipo_compra_id, gravadora_id;
    private HashMap<String, Faixa> faixas = new HashMap<>();
    
    public Albun(){
        
    }
    public void setNewId(){
        int id = 1; 
        String SQL = "";
        SQL += "SELECT " 
                + "MAX(albun_id) as albun_id "
                + "FROM "
                + "albuns";
        ResultSet rs = DbUtils.Lista(SQL);
        try{
            if(rs.next()) {
                id = Integer.parseInt(rs.getString("albun_id")) + 1;
                System.out.println(id);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        albun_id = id;
    }
    public void addToDb(){
        String campos = "albun_id, pr_compra, dt_compra, dt_gravacao, descr, tipo_compra_id, gravadora_id";
        String dados = getAlbunid() + "," 
                + getPrCompra() + ","
                + getDtCompra() + ","
                + getDtGravacao() + ","
                + "'" + getDescr() + "',"
                + getTipoCompraId() + ","
                + getGravadoraId();
        String tabela = "albuns";
        DbUtils.Insert(campos, dados, tabela);
    }
    public boolean exist(){
        String SQL = "";
        SQL += "SELECT " 
                + "albun_id "
                + "FROM "
                + "albuns "
                + "WHERE "
                + "albun_id = " + getAlbunid();
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
    public HashMap<String, Albun> listaAlbuns(){
        HashMap<String, Albun> albuns = new HashMap<>();
        
        String SQL = "";
        SQL += "SELECT " 
                + "a.albun_id, "
                + "a.dt_compra, "
                + "a.descr, "
                + "a.dt_gravacao, "
                + "t.descr as tipo_compra, "
                + "g.nome, "
                + "g.endereco, "
                + "g.website "
                + "FROM "
                + "albuns a, "
                + "tipos_compra t, "
                + "gravadoras g "
                + "WHERE "
                + "a.tipo_compra_id = t.tipo_id AND "
                + "a.gravadora_id = g.gravadora_id;";
                
                
        ResultSet rs = DbUtils.Lista(SQL);
        try{
            while (rs.next()) { 
                Albun albun = new Albun();
                albun.albun_id = Integer.parseInt(rs.getString("albun_id"));
                albun.descr = rs.getString("descr");
                albun.dt_compra = rs.getString("dt_compra");
                albun.dt_gravacao = rs.getString("dt_gravacao");
                albun.tipo_compra = rs.getString("tipo_compra");
                albuns.put(String.valueOf(albun.albun_id), albun);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return albuns;
    }
    public Albun buscaAlbun(String termoBusca){
        Albun albun = new Albun();
        String SQL = "";
        SQL += "SELECT " 
                + "* "
                + "FROM "
                + "albuns "
                + "WHERE "
                + "descr like '%"+ termoBusca +"%';";
        ResultSet rs = DbUtils.Lista(SQL);
        try{
            if(rs.next()) {
                albun.albun_id = Integer.parseInt(rs.getString("albun_id"));
                albun.descr = rs.getString("descr");
                albun.dt_compra = rs.getString("dt_compra");
                albun.dt_gravacao = rs.getString("dt_gravacao");
                albun.tipo_compra_id = Integer.parseInt(rs.getString("tipo_compra_id"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return albun;
    }
    public void updateAlbun(){
        String SQL = "";
        SQL += "UPDATE albuns " 
                + "SET "
                + "descr = '" + getDescr() + "' , "
                + "pr_compra =  '" + getPrCompra() + "' , "
                + "dt_compra =  '" + getDtCompra() + "' , "
                + "dt_gravacao =  '" + getDtGravacao() + "' "
                + "WHERE "
                + "albun_id = " + getAlbunid();
        DbUtils.update(SQL);
    }
    public void setDescr(String descricao){
        descr = descricao;
    }
    public String getDescr(){
        return descr;
    }
    public void setAlbunid(int novoAlbun_id){
        albun_id = novoAlbun_id;
    }
    public int getAlbunid(){
        return albun_id;
    }
    public void setPr_compra(float novoPr_compra){
        pr_compra = novoPr_compra;
    }
    public Float getPrCompra(){
        return pr_compra;
    }
    public void setDt_compra(String novaDt_compra){
        dt_compra = novaDt_compra;
    }
    public String getDtCompra(){
        return dt_compra;
    }
    public void setDt_gravacao(String novaDt_gravacao){
        dt_gravacao = novaDt_gravacao;
    }
    public String getDtGravacao(){
        return dt_gravacao;
    }
    public void setTipo_compra_id(int novoTipo_compra_id){
        tipo_compra_id = novoTipo_compra_id;
    }
    public int getTipoCompraId(){
        return tipo_compra_id;
    }
    public void setGravadora_id(int novaGravadora_id){
        gravadora_id = novaGravadora_id;
    }
    public int getGravadoraId(){
        return gravadora_id;
    }
    public void setFaixas(HashMap<String, Faixa> newFaixas){
        faixas = newFaixas;
    }
    public HashMap<String, Faixa> getFaixas(){
        return faixas;
    }
    
}
