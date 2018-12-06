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
import components.PlayList;
import db.DbConn;
import player.PlayFile;
import tools.SearchFiles;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.media.MediaPlayer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.border.Border;


/**
 *
 * @author Belchior
 */
public class printComponents {
    Border blackline = BorderFactory.createLineBorder(Color.black);
    PlayFile playfile = new PlayFile();
    // cria uma playlist para eventualmente adicionar faixas e salvar
    PlayList playlist = new PlayList();
    JFrame framePlayList = new JFrame("SALVAR PLAY LIST");
    JPanel pnlListaFaixas = new JPanel(); 
    JButton botaoSalvaPlayList = new JButton();
    

    // Aplica as alterações no painel principal adicionando a lista de albuns
    
    public void printAlbuns(JPanel painelPai, HashMap<String, Albun> albuns){
        //painelPai.removeAll();
        //painelPai.setBorder(blackline);
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
                albunDescr.setFont(new Font("Serif", Font.BOLD, 12));
                albunDescr.setForeground(Color.BLUE);
                albunDescr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                albunDescr.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent event){
                        if(albun.getFaixas().isEmpty()){
                            HashMap<String, Faixa> faixas = new Faixa().listaFaixas(String.valueOf(albun.getAlbunid()));
                            albun.setFaixas(faixas);
                        }
                        printFaixas(pnlAlbun, albun.getFaixas());
                    }
                });
                pnlAlbun.add(albunDescr, constraints);
                painelPai.add(pnlAlbun, constraints);
            }catch(NullPointerException e){
                e.printStackTrace();
            }
        
        }
        botaoSalvaPlayList.setVisible(false);
        botaoSalvaPlayList.setText("Salvar PlayList");
        botaoSalvaPlayList.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent event){
                showFormPlayList();
            }
        });
        painelPai.add(botaoSalvaPlayList, constraints);
    }
    
    // Aplica as alterações no painel principal adicionando a lista de compositores
    
    public void printCompositores(JPanel painelPai, HashMap<String, Compositor> compositores){
        painelPai.setBorder(blackline);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        
        for(Compositor compositor : compositores.values()){
            try{
                JPanel pnlCompositores = new JPanel(new GridBagLayout());
                JLabel compositorDescr = new JLabel();
                compositorDescr.setText(compositor.getNome());
                compositorDescr.setFont(new Font("Serif", Font.BOLD, 12));
                compositorDescr.setForeground(Color.BLUE);
                compositorDescr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                System.out.println("Compositores: " + compositor.getNome());
                compositorDescr.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent event){
                    }
                });
                pnlCompositores.add(compositorDescr, constraints);
                painelPai.add(pnlCompositores, constraints);
            }catch(NullPointerException e){
                e.printStackTrace();
            }
        
        }
        
    }
    // Aplica as alterações no painel principal adicionando a lista de playlists
    
    public void printPlayLists(JPanel painelPai, HashMap<String, PlayList> playlists){
        //painelPai.setBorder(blackline);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        
        for(PlayList playlist : playlists.values()){
            try{
                JPanel pnlPlaylists = new JPanel(new GridBagLayout());
                JLabel compositorDescr = new JLabel();
                compositorDescr.setText(playlist.getNome());
                compositorDescr.setFont(new Font("Serif", Font.BOLD, 12));
                compositorDescr.setForeground(Color.BLUE);
                compositorDescr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                compositorDescr.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent event){
                        
                        if(playlist.getFaixas().isEmpty()){
                            playlist.setFaixas();
                        }
                        printFaixas(pnlPlaylists, playlist.getFaixas());
                    }
                });
                pnlPlaylists.add(compositorDescr, constraints);
                painelPai.add(pnlPlaylists, constraints);
            }catch(NullPointerException e){
                e.printStackTrace();
            }
        
        }
        
    }
    
    // função para incluir na tela a lista de faixas referente a cada cd
    public void printFaixas(JPanel painelPai, HashMap<String, Faixa> faixas){
        
        //painelPai.setBorder(blackline);
        
        for(Faixa faixa: faixas.values()){
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.NORTH;
            JPanel pnlFaixas = new JPanel(new GridBagLayout());
            JLabel faixaDescr = new JLabel();
            faixaDescr.setText(faixa.getDescr());
            JCheckBox cb = new JCheckBox ();
            cb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (cb.isSelected()) {
                        playlist.addFaixa(faixa.getFaixaId(), faixa);
                        //showFormPlayList(painelPai, playlist);
                        botaoSalvaPlayList.setVisible(true);
                    } else {
                        playlist.removeFaixa(faixa.getFaixaId());
                        System.out.println(playlist.getFaixas().size());
                        if(playlist.getFaixas().size() == 1){
                            botaoSalvaPlayList.setVisible(false);
                        }
                    }
                }
            });
           
            JButton playButton = new JButton();
            playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/play.png")));
            String path = "file:///C:/FBD/" + faixa.getDescr().replaceAll("\\u0020", "%20") + ".mp3";
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
            
            constraints.gridx = 0;
            pnlFaixas.add(faixaDescr, constraints);
            constraints.gridx = 1;
            pnlFaixas.add(cb, constraints);
            constraints.gridx = 2;
            pnlFaixas.add(playButton, constraints);
            constraints.gridx = 0;
            constraints.anchor = GridBagConstraints.PAGE_START;
            painelPai.add(pnlFaixas, constraints);
            painelPai.revalidate();
            painelPai.repaint();
        }
    }
    public void showFormPlayList(){
        pnlListaFaixas.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        framePlayList.setSize(500, 700); 
        Popup popUpPlaylist; 
        PopupFactory pf = new PopupFactory(); 
        
        pnlListaFaixas.setBackground(Color.WHITE); 
        int count = 1;
        for(Faixa faixa: playlist.getFaixas().values()){
            constraints.gridy = count;
            JLabel titulo = new JLabel(faixa.getDescr()); 
            pnlListaFaixas.add(titulo, constraints); 
            count++;
        }
        
        popUpPlaylist = pf.getPopup(framePlayList, pnlListaFaixas, 180, 100); 
        JLabel fieldNome = new JLabel("Nome da playlist:"); 
        JTextField tfNomePlayList = new JTextField(20);
        pnlListaFaixas.add(fieldNome);
        pnlListaFaixas.add(tfNomePlayList);
        JButton buttonSalvar = new JButton("SALVAR"); 
        JButton buttonFechar = new JButton("FECHAR"); 
        buttonFechar.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent event){
                framePlayList.setVisible(false); 
            }
        });
        buttonSalvar.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent event){
                playlist.setPlaylistNewId();
                playlist.setNome(tfNomePlayList.getText());
                playlist.salvaPlaylist();
                framePlayList.setVisible(false); 
            }
        });
        constraints.gridy = 1;
        constraints.gridx = 0;
        pnlListaFaixas.add(buttonSalvar, constraints);
        constraints.gridx = 1;
        pnlListaFaixas.add(buttonFechar, constraints);
        pnlListaFaixas.revalidate();
        pnlListaFaixas.repaint();
        framePlayList.add(pnlListaFaixas); 
        framePlayList.setVisible(true); 
        
    }
    public void formEditAlbun(JPanel painelPai, Albun albun){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        painelPai.removeAll();
        JLabel lblIdAlbun = new JLabel("Id do Albun:"); 
        JTextField jtfIdAlbun = new JTextField(20);
        jtfIdAlbun.setText(String.valueOf(albun.getAlbunid()));
        
        JLabel lblNomeAlbun = new JLabel("Nome do Albun:"); 
        JTextField jtfNomeAlbun = new JTextField(20);
        jtfNomeAlbun.setText(albun.getDescr());
        
        JLabel lblPrCompra = new JLabel("Preço de Compra:"); 
        JTextField jtfPrCompra = new JTextField(20);
        jtfPrCompra.setText(String.valueOf(albun.getPrCompra()));
        
        JLabel lbDtCompra = new JLabel("Data da compra:"); 
        JTextField jtfDtCompra = new JTextField(20);
        jtfDtCompra.setText(albun.getDtCompra());
        
        JLabel lbDtGravacao = new JLabel("Data da Gravação:"); 
        JTextField jtfDtGravacao = new JTextField(20);
        jtfDtGravacao.setText(albun.getDtGravacao());
        
        JButton salvaAlteracao = new JButton("Salvar");
        salvaAlteracao.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent event){
                albun.setAlbunid(Integer.parseInt(jtfIdAlbun.getText()));
                albun.setDescr(jtfNomeAlbun.getText());
                albun.setPr_compra(Float.parseFloat(jtfPrCompra.getText()));
                albun.setDt_compra(jtfDtCompra.getText());
                albun.setDt_gravacao(jtfDtGravacao.getText());
                albun.updateAlbun();
            }
        });
       
        constraints.gridy = 1;
        painelPai.add(lblNomeAlbun, constraints);
        painelPai.add(jtfNomeAlbun, constraints);
        constraints.gridy = 2;
        painelPai.add(lblPrCompra, constraints);
        painelPai.add(jtfPrCompra, constraints);
        constraints.gridy = 3;
        painelPai.add(lbDtCompra, constraints);
        painelPai.add(jtfDtCompra, constraints);
        constraints.gridy = 4;
        painelPai.add(lbDtGravacao, constraints);
        painelPai.add(jtfDtGravacao, constraints);
        constraints.gridy = 5;
        painelPai.add(salvaAlteracao, constraints);
        painelPai.revalidate();
        painelPai.repaint();
    }
}
