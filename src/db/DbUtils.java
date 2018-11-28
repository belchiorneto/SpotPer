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
package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import static db.DbConn.stmt;

/**
 *
 * @author Belchior
 */
public class DbUtils {
    public static void Insert(String campos, String data, String table){
       String SQL = "INSERT INTO " + table + " (" +campos+ ") VALUES (" + data + ");";
       System.out.println(SQL);
       try{
           stmt.executeUpdate(SQL);
       }catch (SQLException e) {
            e.printStackTrace();
       }
   }
    public static ResultSet Lista(String SQL){
       System.out.println(SQL);
       ResultSet rs = null;
       try{
            rs = stmt.executeQuery(SQL);
       }catch (SQLException e) {
            e.printStackTrace();
       }
       return rs;
   }
    
    public static boolean executeDBScripts(String aSQLScriptFilePath) throws IOException,SQLException {
        boolean isScriptExecuted = false;
        try {
            BufferedReader in = new BufferedReader(new FileReader(aSQLScriptFilePath));
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = in.readLine()) != null) {
            sb.append(str + "\n ");
        }
        in.close();
        stmt.executeUpdate(sb.toString());
        isScriptExecuted = true;
        } catch (Exception e) {
            System.err.println("Failed to Execute" + aSQLScriptFilePath +". The error is"+ e.getMessage());
        } 
        return isScriptExecuted;
        }
}
