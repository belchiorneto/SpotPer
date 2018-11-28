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
import components.Cd;
import components.Faixa;
import player.PlayFile;
import tools.SearchFiles;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.media.MediaPlayer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;


/**
 *
 * @author Belchior
 */
public class printComponents {
    Border blackline = BorderFactory.createLineBorder(Color.black);
    PlayFile playfile = new PlayFile();
    
    // Aplica as alterações no painel principal adicionando a lista de albuns
    
    public void printAlbuns(JPanel painelPai, HashMap<String, Albun> albuns){
        //painelPai.removeAll();
        painelPai.setBorder(blackline);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        
        for(Albun albun : albuns.values()){
            try{
                JPanel pnlAlbun = new JPanel(new GridBagLayout());
                JLabel albunDescr = new JLabel();
                albunDescr.setText(albun.getDescr());
                albunDescr.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent event){
                        if(albun.getCds().isEmpty()){
                            HashMap<String, Cd> cds = new Cd().listaCds(albun.getAlbunid());
                            albun.setCds(cds);
                        }
                        printCds(pnlAlbun, albun.getCds());
                    }
                });
                pnlAlbun.add(albunDescr, constraints);
                painelPai.add(pnlAlbun, constraints);
            }catch(NullPointerException e){
                e.printStackTrace();
            }
        
        }
        
    }
    // função para incluir na tela a lista de cds referente a cada album 
    public void printCds(JPanel painelPai, HashMap<String, Cd> cds){
      //  painelPai.removeAll();
        painelPai.setBorder(blackline);
        GridBagConstraints constraints = new GridBagConstraints();
        //constraints.anchor = GridBagConstraints.WEST;
        //constraints.insets = new Insets(10, 10, 10, 10);
        int numcd = 1;
        constraints.gridy = 1;
        for(Cd cd : cds.values()){
            JPanel pnlCds = new JPanel(new GridBagLayout());
            JButton cdButton = new JButton();
            cdButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/spotper/icon_cd.png")));
            cdButton.setText(String.valueOf("CD Nº " + numcd));
            cdButton.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent event){
                    if(cd.GetFaixas().isEmpty()){
                        HashMap<String, Faixa> faixas = new Faixa().listaFaixas(String.valueOf(cd.getcdId()));
                        cd.setFaixas(faixas);
                    }
                    printFaixas((JPanel)pnlCds.getParent(), cd.GetFaixas(), cds.size());
                }
            });
            constraints.gridx = numcd - 1;
            pnlCds.add(cdButton, constraints);
            painelPai.add(pnlCds, constraints);
            painelPai.revalidate();
            painelPai.repaint();
            numcd++;
        }
    }
    // função para incluir na tela a lista de faixas referente a cada cd
    public void printFaixas(JPanel painelPai, HashMap<String, Faixa> faixas, int qtcds){
     //   painelPai.removeAll();
        painelPai.setBorder(blackline);
        
        int countfaixas = 1;
        for(Faixa faixa: faixas.values()){
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.WEST;
            JPanel pnlFaixas = new JPanel(new GridBagLayout());
            JLabel faixaDescr = new JLabel();
            faixaDescr.setText(faixa.getDescr());
            JCheckBox  cb = new JCheckBox ();
            /*
            cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> ov,
                Boolean old_val, Boolean new_val) {
                    if(cb.isSelected()){

                    }
                }
            });
            */
            JButton playButton = new JButton();
            playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/spotper/play.png")));
            String path = "file:///C:/FBD/" + faixa.getDescr().replaceAll("\\u0020", "%20") + ".mp3";
            playButton.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent event){
                    if(playfile.getClip() != null){
                        if(playfile.getClip().getStatus() == MediaPlayer.Status.PLAYING){
                            playfile.stop();
                        }else{
                            playfile.setClip(path);
                            playfile.play();
                        }
                    }else{
                        playfile.setClip(path);
                        playfile.play();
                    }
                    
                }
            });
            
            constraints.gridx = 0;
            pnlFaixas.add(faixaDescr, constraints);
            constraints.gridx = 1;
            pnlFaixas.add(cb, constraints);
            constraints.gridx = 2;
            pnlFaixas.add(playButton, constraints);
            constraints.gridwidth = qtcds;
            constraints.gridx = 0;
            painelPai.add(pnlFaixas, constraints);
            painelPai.revalidate();
            painelPai.repaint();
            countfaixas++;
        }
    }
}
