/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    private String tipo_gravacao;
    private String url_download;
    private Interprete[] interpretes = new Interprete[5];
    private Compositor compositor;
    
    public HashMap<String, Faixa> listaFaixas(String cdId){
        HashMap<String, Faixa> faixas = new HashMap<>();
        String SQL = "";
        SQL += "SELECT " 
                + "f.faixa_id, "
                + "f.descr, "
                + "f.duracao, "
                + "c.nome "
                + "FROM "
                + "faixas f, "
                + "faixas_interpretes_comp fc, "
                + "composicoes cp, "
                + "compositores c "                
                + "WHERE "
                + "fc.faixa_id = f.faixa_id AND "
                + "fc.composicao_id = cp.composicao_id AND "
                + "cp.compositor_id = c.compositor_id AND "
                + "f.cd_id = " + cdId;
        ResultSet rs = DbUtils.Lista(SQL);
        try{
            while (rs.next()) { 
                Faixa faixa = new Faixa();
                faixa.setFaixaId(Integer.parseInt(rs.getString("faixa_id")));
                faixa.setDuracao(rs.getString("duracao"));
                faixa.setDescr(rs.getString("descr"));
                Compositor compositor = new Compositor();
                compositor.setNome(rs.getString("nome"));
                faixa.setCompositor(compositor);
                faixas.put(String.valueOf(faixa.getFaixaId()), faixa);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
        // you should release your resources here
            if (rs != null) { 
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cd.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    return faixas;
    }
    public void setFaixaId(int faixaId){
        faixa_id = faixaId;
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
    public void setTipoGravacao(String tipogravacao){
        tipo_gravacao = tipogravacao;
    }
    public String getTipoGravacao(){
        return tipo_gravacao;
    }
    public void setCompositor(Compositor comp){
        compositor = comp;
    }
    public Compositor getCompositor(){
        return compositor;
    }
  
}
