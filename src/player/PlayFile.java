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
package player;

// fonte: https://stackoverflow.com/questions/25171205/playing-sound-in-java

import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


public class PlayFile{
    private Media media;
    private MediaPlayer player;
    private MediaView view;
    private Pane nPane;
    
    public void play(){
        player.setVolume(1);
        player.play();
    }
    public void stop(){
        player.pause();
    }
    public double getDuration(){
        return player.getTotalDuration().toSeconds();
    }
    public MediaPlayer getClip(){
        return player;
    }
    public void setClip(String path){
        JFXPanel fxPanel = new JFXPanel(); // gambiarra, num apague não!
        media = new Media(path);
        player = new MediaPlayer(media);
        view = new MediaView(player);
        nPane = new Pane();
        nPane.getChildren().add(view);
    }
}