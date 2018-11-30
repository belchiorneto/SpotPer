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
package principal;

import components.Albun;
import components.Compositor;
import components.Faixa;
import components.PlayList;
import db.DbConn;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import tools.SearchFiles;
import view.printComponents;
import view.printWebComponents;

/**
 *
 * @author Belchior
 */
public class SpotPer extends JFrame {
    private final JButton ButtonAlbuns;
    private final JButton ButtonCompositores;
    private final JButton ButtonPlaylist;
    private final JButton ButtonWeb;
    private final JPanel PainelBotoes;
    private final JPanel PainelBusca;
    private final JPanel PainelCentral;
    private final JPanel PainelMaisTocadas;
  
    
    public SpotPer() {
        super("SpotPer");
        DbConn connection = new DbConn();
        printComponents componentes = new printComponents();
        printWebComponents webComponentes = new printWebComponents();
        PainelBotoes = new JPanel(new GridBagLayout());
        PainelBotoes.setPreferredSize(new Dimension(1000, 140));
        PainelBusca = new JPanel(new GridBagLayout());
        PainelBusca.setPreferredSize(new Dimension(1000, 30));
        PainelMaisTocadas = new JPanel(new GridBagLayout());
        PainelMaisTocadas.setPreferredSize(new Dimension(200, 700));
        PainelCentral = new JPanel(new GridBagLayout());
        //PainelCentral.setLayout(new BoxLayout(PainelCentral, BoxLayout.PAGE_AXIS));
        

        JScrollPane PainelCentralScrool = new JScrollPane(PainelCentral);
        PainelCentralScrool.setViewportView(PainelCentral);
        
        ButtonAlbuns = new JButton();
        ButtonAlbuns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon_albuns.png")));
        ButtonAlbuns.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ButtonCompositores = new JButton();
        ButtonCompositores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon_compositores.png")));
        ButtonCompositores.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ButtonPlaylist = new JButton();
        ButtonPlaylist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon_playlist.png")));
        ButtonPlaylist.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ButtonWeb = new JButton();
        ButtonWeb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon_web.png")));
        ButtonWeb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
        PainelCentral.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "SpotPer"));
        
        PainelMaisTocadas.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Mais Tocadas"));
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        PainelBotoes.add(ButtonAlbuns, constraints);
        constraints.gridx = 1;
        PainelBotoes.add(ButtonCompositores, constraints);
        constraints.gridx = 2;
        PainelBotoes.add(ButtonPlaylist, constraints);
        constraints.gridx = 3;
        PainelBotoes.add(ButtonWeb, constraints);
        JLabel lblBuscaAlbun = new JLabel("Buscar Albun: ");
        JTextField jtfBuscaAlbun = new JTextField(20);
        jtfBuscaAlbun.setColumns(10);
        JButton btBuscaAlbun = new JButton("Buscar");
        btBuscaAlbun.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent event){
                Albun albun = new Albun().buscaAlbun(jtfBuscaAlbun.getText());
                JLabel lblNaoEncontrado = new JLabel("Nada encontrado ...");
                lblNaoEncontrado.setFont(new Font("Serif", Font.BOLD, 14));
                lblNaoEncontrado.setForeground(Color.RED);
                if(albun.getAlbunid() < 1){
                    PainelCentral.removeAll();
                    PainelCentral.add(lblNaoEncontrado);
                    PainelCentral.revalidate();
                    PainelCentral.repaint();
                }else{
                    componentes.formEditAlbun(PainelCentral, albun);
                }
            }
        });
        constraints.gridx = 4;
        PainelBotoes.add(lblBuscaAlbun, constraints);
        constraints.gridx = 5;
        PainelBotoes.add(jtfBuscaAlbun, constraints);
        constraints.gridx = 6;
        PainelBotoes.add(btBuscaAlbun, constraints);
        add(PainelBotoes, BorderLayout.NORTH);
        
        add(PainelMaisTocadas, BorderLayout.WEST);
        add(PainelCentralScrool, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // EDIT
        if(!connection.OpenConnection()){
            // connexão inválida
            JLabel lblConnError = new JLabel("Erro de conexão com o banco de dados");
            lblConnError.setFont(new Font("Serif", Font.BOLD, 14));
            lblConnError.setForeground(Color.RED);
            PainelCentral.add(lblConnError);
        }else{
            // mostra lista de albuns
            componentes.printAlbuns(PainelCentral, new Albun().listaAlbuns());
        }
        
        ButtonAlbuns.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent event){
                PainelCentral.removeAll();
                componentes.printAlbuns(PainelCentral, new Albun().listaAlbuns());
                PainelCentral.revalidate();
                PainelCentral.repaint();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonAlbuns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon_albuns_h.png")));
              
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonAlbuns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon_albuns.png")));
              
            }
        });
        
        ButtonCompositores.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent event){
                PainelCentral.removeAll();
                componentes.printCompositores(PainelCentral, new Compositor().listaCompositores());
                PainelCentral.revalidate();
                PainelCentral.repaint();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonCompositores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon_compositores_h.png")));
              
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonCompositores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon_compositores.png")));
              
            }
        });
        ButtonPlaylist.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent event){
                PainelCentral.removeAll();
                componentes.printPlayLists(PainelCentral, new PlayList().lista());
                
                PainelCentral.revalidate();
                PainelCentral.repaint();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonPlaylist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon_playlist_h.png")));
              
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonPlaylist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon_playlist.png")));
              
            }
        });
        ButtonWeb.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent event){
                PainelCentral.removeAll();
                SearchFiles searchfiles = new SearchFiles();
                searchfiles.setConteudoExtraido("https://www.mfiles.co.uk/classical-mp3.htm");
                webComponentes.printCompositores(PainelCentral, searchfiles);
                PainelCentral.revalidate();
                PainelCentral.repaint();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonWeb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon_web_h.png")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonWeb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/principal/icon_web.png")));
            }
        });
        pack();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
         // set look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SpotPer().setVisible(true);
            }
        });
    }
}
