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
public class Cd {
    private int cd_id;
    private HashMap<String, Faixa> faixas = new HashMap<>();
    
    public HashMap<String, Cd> listaCds(int albun_id){
        HashMap<String, Cd> cds = new HashMap<>();
        String SQL = "";
        SQL += "SELECT " 
                + "cd_id "
                + "FROM "
                + "cds "
                + "WHERE "                
                + "albun_id = " + albun_id;
                
                
        ResultSet rscds = DbUtils.Lista(SQL);
        try{
            if(!rscds.isClosed()){
                while (rscds.next()) { 
                    Cd cd = new Cd();
                    cd.setId(Integer.parseInt(rscds.getString("cd_id")));
                    cds.put(String.valueOf(cd.getcdId()), cd);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            if (rscds != null) { 
                try {
                    rscds.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Cd.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return cds;
    }
    public void setFaixas(HashMap<String, Faixa> newFaixas){
        faixas = newFaixas;
    }
    public HashMap<String, Faixa> GetFaixas(){
        return faixas;
    }
    public void setId(int cdId){
        cd_id = cdId;
    }
    public int getcdId(){
        return cd_id;
    }
    
}
