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
import components.Faixa;
import db.DbConn;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JScrollPane;
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
    private final JButton ButtonVoltar;
    private final JPanel PainelBotoes;
    private final JPanel PainelCentral;
    private final JPanel PainelMaisTocadas;
  
    
    public SpotPer() {
        super("SpotPer");
        DbConn.OpenConnection();
        printComponents componentes = new printComponents();
        printWebComponents webComponentes = new printWebComponents();
        PainelBotoes = new JPanel(new GridBagLayout());
        PainelBotoes.setPreferredSize(new Dimension(1000, 110));
        PainelMaisTocadas = new JPanel(new GridBagLayout());
        PainelMaisTocadas.setPreferredSize(new Dimension(200, 700));
        PainelCentral = new JPanel(new GridBagLayout());
        PainelCentral.setLayout(new BoxLayout(PainelCentral, BoxLayout.PAGE_AXIS));
        

        JScrollPane PainelCentralScrool = new JScrollPane(PainelCentral);
        PainelCentralScrool.setViewportView(PainelCentral);
        
        ButtonAlbuns = new JButton();
        ButtonAlbuns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/spotper/icon_albuns.png")));
        ButtonCompositores = new JButton();
        ButtonCompositores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/spotper/icon_compositores.png")));
        ButtonPlaylist = new JButton();
        ButtonPlaylist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/spotper/icon_playlist.png")));
        ButtonWeb = new JButton();
        ButtonWeb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/spotper/icon_web.png")));
        ButtonVoltar = new JButton();
        ButtonVoltar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/spotper/icon_voltar.png")));
 
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
        constraints.gridx = 4;
        PainelBotoes.add(ButtonVoltar, constraints);
        add(PainelBotoes, BorderLayout.NORTH);
        add(PainelMaisTocadas, BorderLayout.WEST);
        add(PainelCentralScrool, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // EDIT
        
        // mostra lista de albuns
        componentes.printAlbuns(PainelCentral, new Albun().listaAlbuns());
        ButtonWeb.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent event){
                PainelCentral.removeAll();
                SearchFiles searchfiles = new SearchFiles();
                searchfiles.setConteudoExtraido("https://www.mfiles.co.uk/classical-mp3.htm");
                webComponentes.printCompositores(PainelCentral, searchfiles);
                PainelCentral.revalidate();
                PainelCentral.repaint();
            }
        });
        ButtonAlbuns.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent event){
                PainelCentral.removeAll();
                componentes.printAlbuns(PainelCentral, new Albun().listaAlbuns());
                
                PainelCentral.revalidate();
                PainelCentral.repaint();
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
