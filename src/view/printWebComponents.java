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
package view;
import components.Albun;
import components.Compositor;
import components.Faixa;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import javafx.scene.media.MediaPlayer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import player.PlayFile;
import tools.SearchFiles;

/**
 *
 * @author iranilda
 */
public class printWebComponents {
    Border blackline = BorderFactory.createLineBorder(Color.black);
    PlayFile playfile = new PlayFile();
    
    public void printCompositores(JPanel painelPai, SearchFiles searchfiles){
        HashMap<String, String> compositores = searchfiles.listaCompositores("h2");
        painelPai.setBorder(blackline);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
       // constraints.fill = GridBagConstraints.VERTICAL;
       // constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        
        for(String compositor : compositores.values()){
            
            try{
                JPanel pnlCompositores = new JPanel(new GridBagLayout());
                JLabel compNome = new JLabel();
                compNome.setText(compositor);
                compNome.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent event){
                        printComposicoes(pnlCompositores, searchfiles, compositor);
                    }
                });
                pnlCompositores.add(compNome, constraints);
                painelPai.add(pnlCompositores, constraints);
            }catch(NullPointerException e){
                e.printStackTrace();
            }
        
        }
    }
    public void printComposicoes(JPanel painelPai, SearchFiles searchfiles, String compositor){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        HashMap<String, Faixa> faixas = searchfiles.listaComposicoes("h2", "li", compositor);
        Albun albun = new Albun();
        albun.setNewId();
        albun.setDescr(compositor.split("\\(")[0]);
        String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        albun.setDt_compra(dataAtual);
        albun.setDt_gravacao(dataAtual);
        albun.setTipo_compra_id(1);
        albun.setGravadora_id(1);
        albun.setFaixas(faixas);
        Compositor comp = new Compositor();
        comp.setNewId();
        comp.setNome(compositor);
        
        int count = 1;
        for(Faixa faixa: albun.getFaixas().values()){
            JLabel nomeFaixa = new JLabel();
            nomeFaixa.setText(faixa.getDescr());
            nomeFaixa.setForeground(Color.blue);
            nomeFaixa.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent event){
                    
                }
            });
            JButton playButton = new JButton();
            playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/play.png")));
            // test to see if a file exists
            String nomedaFaixa = faixa.getDescr().replaceAll("\\u0020", "%20");
            nomedaFaixa = nomedaFaixa.replaceAll("\"", "");
            String path = "file:///C:/FBD/" + nomedaFaixa + ".mp3";
            playButton.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent event){
                    if(playfile.getClip() != null){
                        if(playfile.getClip().getStatus() == MediaPlayer.Status.PLAYING){
                            playfile.stop();
                        }else{
                            playfile.setClip(path);
                            playfile.play();
                            faixa.countPlay();
                        }
                    }else{
                        playfile.setClip(path);
                        playfile.play();
                        faixa.countPlay();
                    }
                    
                }
            });
            JButton downloadButton = new JButton();
            downloadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon_download.png")));
            downloadButton.setVisible(false);
            downloadButton.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent event){
                    /*
                    este site fornece arquivos de musica clássica gratuitos em mp3
                    https://www.mfiles.co.uk/
                    */
                    String urlfile = "https://www.mfiles.co.uk/"+ faixa.getUrlDownload();
                    try {
                        String nomedaFaixa = faixa.getDescr();
                        nomedaFaixa = nomedaFaixa.replaceAll("\"", "");
                        new tools.FileManager().download(urlfile, "C:/FBD/" + nomedaFaixa + ".mp3");
                        downloadButton.setVisible(false);
                        playButton.setVisible(true);
                        nomedaFaixa = nomedaFaixa.replaceAll("\\u0020", "%20");
                        String path = "file:///C:/FBD/" + nomedaFaixa + ".mp3";
                        playfile.setClip(path);
                        faixa.setTipoGravacaoID(1);
                        //faixa.setDuracao(String.valueOf(playfile.getDuration()));
                        System.out.println("Duraçao da faixa: " + playfile.getDuration());
                        // inclui faixa no db
                        if(!albun.exist()){
                            albun.addToDb();                        
                        }
                        if(!comp.exist()){
                            comp.addToDb();                        
                        }
                        // inclui faixa no bd
                        faixa.addToDB(albun.getAlbunid());
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                }
            });
            
            File file = new File("C:/FBD/" + faixa.getDescr() + ".mp3");
            if(!file.exists()){
                playButton.setVisible(false);
                downloadButton.setVisible(true);
            }
            
            constraints.gridy = count;
            constraints.gridx = 0;
            painelPai.add(nomeFaixa, constraints);
            constraints.gridx = 1;
            painelPai.add(downloadButton, constraints);
            constraints.gridx = 2;
            painelPai.add(playButton, constraints);
            count++;
        }
        painelPai.revalidate();
        painelPai.repaint();
    }
}
