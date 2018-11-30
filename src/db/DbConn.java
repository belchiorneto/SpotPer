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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.FileManager;
/**
 *
 * @author Belchior
 */
public class DbConn {
   public static Connection con;
   public static Statement stmt;
   // variaveis de conexão
   private String server = "localhost";
   private String porta = "1433";
   private String bd = "BDSpotPer";
   private String user = "SpotPer";
   private String pass = "fbd2018";
   private String connectionUrl = "";
   private boolean useWindowsUser = true; // setar em false para usar autenticação do SQL Server
   
   public boolean OpenConnection(){
       boolean retorno = false;
       if(useWindowsUser){
            connectionUrl = "jdbc:sqlserver://"+server+":"+porta+";databasename="+bd+";integratedsecurity=true";
        }else{
            connectionUrl = "jdbc:sqlserver://"+server+":"+porta+";databaseName="+bd+";user="+user+";password="+pass;
        }
       try {
           con = DriverManager.getConnection(connectionUrl);   
           stmt = con.createStatement();
           retorno = true;
       } catch (SQLException ex) {
           Logger.getLogger(DbConn.class.getName()).log(Level.SEVERE, null, ex);
           retorno = false;
       }
       return retorno;
   }
   
   public static Statement getStatment(){
       return stmt;
   }
}
