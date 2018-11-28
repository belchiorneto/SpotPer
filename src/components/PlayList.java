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
    HashMap<Integer,Faixa> faixas;
    
    public PlayList(int num, String nome_playlist){
        playlist_id = num;
        nome = nome_playlist;
        faixas = new HashMap<>();
    }
    public int getPlaylistId(){
        return playlist_id;
    }
    
    public String getNome(){
        return nome;
    }
    public String getDtCriacao(){
        return dt_criacao;
    }
    public int getTempoExec(){
        return tempo_exec;
    }
    public HashMap<Integer,Faixa> getFaixas(){
        return faixas;
    }
    
    public void setPlaylistId(int id){
        playlist_id = id;
    }
    
    public void setNome(String Nome){
        nome = Nome;
    }
    public void setDtCriacao(String dtCriacao){
        dt_criacao = dtCriacao;
    }
    public void setTempoExec(int tempoExec){
        tempo_exec = tempoExec;
    }
    public void setFaixas(HashMap<Integer,Faixa> f){
        faixas = f;
    }
    public void addFaixa(int id_faixa, Faixa faixa){
        faixas.put(id_faixa, faixa);
    }
    public void removeFaixa(int id_faixa){
        faixas.remove(id_faixa);
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
