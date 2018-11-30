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
public class Faixa {
    private int faixa_id;
    private String duracao;
    private String descr;
    private int tipoGravacaoId;
    private String url_download;
    private Interprete[] interpretes = new Interprete[5];
    private Compositor compositor;
    
    public HashMap<String, Faixa> listaFaixas(String albunId){
        HashMap<String, Faixa> faixas = new HashMap<>();
        String SQL = "";
        SQL += "SELECT " 
                + "DISTINCT(f.faixa_id), "
                + "f.descr, "
                + "f.duracao "
                + "FROM "
                + "faixas f "
                + "WHERE "
                + "f.albun_id = " + albunId;
        ResultSet rs = DbUtils.Lista(SQL);
        try{
            while (rs.next()) { 
                Faixa faixa = new Faixa();
                faixa.setFaixaId(Integer.parseInt(rs.getString("faixa_id")));
                faixa.setDuracao(rs.getString("duracao"));
                faixa.setDescr(rs.getString("descr"));
                faixas.put(String.valueOf(faixa.getFaixaId()), faixa);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    return faixas;
    }
    public void setNewFaixaId(){
        int id = 0; 
        String SQL = "";
        SQL += "SELECT " 
                + "MAX(faixa_id) as faixa_id "
                + "FROM "
                + "faixas";
        ResultSet rs = DbUtils.Lista(SQL);
        try{
            if(rs.next()) {
                id = Integer.parseInt(rs.getString("faixa_id")) + 1;
                System.out.println(id);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        faixa_id = id;
    }
    public void addToDB(int albun_id){
        String campos = "faixa_id, duracao, descr, tipo_gravacao_id, albun_id";
        String dados = getFaixaId() + "," 
                + getDuracao() + ","
                + "'" + getDescr() + "',"
                + getTipoGravacaoId() + ","
                + albun_id;
        String tabela = "faixas";
        DbUtils.Insert(campos, dados, tabela);
    }
    public void setFaixaId(int faixaId){
        faixa_id = faixaId;
    }
    public int getFaixaId(){
        return faixa_id;
    }
    public void setDuracao(String durac){
        duracao = durac;
    }
    public String getDuracao(){
        return duracao;
    }
    public void setDescr(String descricao){
        descr = descricao;
    }
    public String getDescr(){
        return descr;
    }
    public void setUrlDownload(String urldownload){
        url_download = urldownload;
    }
    public String getUrlDownload(){
        return url_download;
    }
    public void setTipoGravacaoID(int tipogravacaoid){
        tipoGravacaoId = tipogravacaoid;
    }
    public int getTipoGravacaoId(){
        return tipoGravacaoId;
    }
    public void setCompositor(Compositor comp){
        compositor = comp;
    }
    public Compositor getCompositor(){
        return compositor;
    }
  
}
