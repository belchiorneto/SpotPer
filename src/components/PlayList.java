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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Belchior
 */
public class PlayList {
    int playlist_id;
    String nome;
    String dt_criacao;
    int tempo_exec; // (em segundos)
    int numero_tocadas;
    private HashMap<String, Faixa> faixas = new HashMap<>();
    
    public void setNome(String newnome){
        nome = newnome;
    }
    public String getNome(){
        return nome;
    }
    public void setDtCriacao(String novaData){
        dt_criacao = novaData;
    }
    public String getDtCriacao(){
        return dt_criacao;
    }
    public void setTempoExec(){
        int tempoTotal = 0;
        for(Faixa faixa: getFaixas().values()){
            tempoTotal += Integer.parseInt(faixa.getDuracao());
        }
        tempo_exec = tempoTotal;
    }
    public int getTempoExec(){
        return tempo_exec;
    }
    
    public void setPlaylistId(int id){
        playlist_id = id;
    }
    public void setPlaylistNewId(){ // para o caso de novas playlists
        int id = 1; 
        String SQL = "";
        SQL += "SELECT " 
                + "MAX(playlist_id) as playlist_id "
                + "FROM "
                + "playlists";
        ResultSet rs = DbUtils.Lista(SQL);
        try{
            if(rs.next()) {
                id = Integer.parseInt(rs.getString("playlist_id")) + 1;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        playlist_id = id;
    }
    public int getPlaylistId(){
        return playlist_id;
    }
    
    public void setFaixas(){
        HashMap<String, Faixa> newfaixas = new HashMap<>();
        String SQL = "";
        SQL += "SELECT " 
                + "f.*, pl.* "
                + "FROM faixas f, playlists_faixas pl "
                + "WHERE f.faixa_id = pl.faixa_id AND "
                + "pl.playlist_id = " + getPlaylistId();
                
        ResultSet rs = DbUtils.Lista(SQL);
        try{
            while (rs.next()) { 
                Faixa faixa = new Faixa();
                faixa.setFaixaId(Integer.parseInt(rs.getString("faixa_id")));
                faixa.setDuracao(rs.getString("duracao"));
                faixa.setDescr(rs.getString("descr"));
                newfaixas.put(String.valueOf(faixa.getFaixaId()), faixa);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        faixas = newfaixas;
    }
    public HashMap<String,Faixa> getFaixas(){
        return faixas;
    }
    public void addFaixa(int id_faixa, Faixa faixa){
        faixas.put(String.valueOf(id_faixa), faixa);
    }
    public void removeFaixa(int id_faixa){
        faixas.remove(id_faixa);
        
    }
    public HashMap<String, PlayList> lista(){
        HashMap<String, PlayList> playlists = new HashMap<>();
        String SQL = "";
        SQL += "SELECT " 
                + "* "
                + "FROM "
                + "playlists ";
                
        ResultSet rs = DbUtils.Lista(SQL);
        try{
            while (rs.next()) { 
                PlayList playlist = new PlayList();
                playlist.setPlaylistId(Integer.parseInt(rs.getString("playlist_id")));
                playlist.setDtCriacao(rs.getString("dt_criacao"));
                playlist.setNome(rs.getString("nome"));
                playlists.put(String.valueOf(playlist.getPlaylistId()), playlist);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return playlists;
    }
    public void salvaPlaylist(){
        // prepara SQl para incluir uma nova playlist
        String campos = "playlist_id, nome, dt_criacao";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String values = playlist_id + " , '" + nome + "' , '" + dateFormat.format(date) + "'";
        DbUtils.Insert(campos, values, "playlists"); // inserindo nova playlist
        for(Faixa faixa : faixas.values()){
            // inserindo as faixas desta playlist
            String campos_faixa = "playlist_id, faixa_id, qt_plays, dt_ultimo_play";
            String values_faixa = playlist_id + ", " + faixa.getFaixaId() + ", null, null";
            DbUtils.Insert(campos_faixa, values_faixa, "playlists_faixas"); // inserindo nova playlist
        }
        
    }
}
