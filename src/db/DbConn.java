/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Belchior
 */
public class DbConn {
   public static Connection con;
   public static Statement stmt;
   // variaveis de conexão
   public static String server = "localhost";
   public static String porta = "1433";
   public static String bd = "BDSpotPer";
   private static String connectionUrl = "jdbc:sqlserver://"+server+":"+porta+";databasename="+bd+";integratedsecurity=true";
   public static void OpenConnection(){
       try {
           con = DriverManager.getConnection(connectionUrl);   
           stmt = con.createStatement();
       } catch (SQLException ex) {
           Logger.getLogger(DbConn.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
   
   public static Statement getStatment(){
       return stmt;
   }
}
