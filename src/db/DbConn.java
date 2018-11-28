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
