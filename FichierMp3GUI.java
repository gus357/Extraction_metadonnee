package mimeMp3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.stream.XMLStreamException;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 ** Interface Graphique servant à l'analyse de fichier.
 * <br>Le bouton Ouvrir permet d'ajouter des fichiers à analyser.
 * <br>Le bouton Analyser permet l'analyse des fichiers ouverts.
 * <br>Le bouton Reset permet de réinitialiser la liste de fichier ouvert.
 * <br>Le bouton Quitter permet de fermer le programme.
 * @author Gulsum et Emma
 */
@SuppressWarnings("serial")
public class FichierMp3GUI extends JFrame{
	private static JTextArea jtfEtat;
	private JFileChooser jfc;
	private LinkedList<File> listeFichier;
	private  FichierMp3 mp3;
	private ArrayList<FichierMp3> listeMp3=new ArrayList<FichierMp3>();
	private JPanel l1;
	
	/**
	 * Creation de l'interface graphique
	 * @throws IOException : probleme d'autorisation
	 */
	public FichierMp3GUI () throws IOException {
		super("Analyse de fichier Mp3");
		this.getContentPane().setLayout(new GridBagLayout());
		
		jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		mp3 = new FichierMp3();
		listeFichier = new LinkedList<File>();
		jtfEtat = new JTextArea();

		
		l1 = new JPanel();
		l1.setLayout(new GridLayout(0,1,30,10));
		
		JPanel pan1 = new JPanel();
		BufferedImage img1 = ImageIO.read(new File("images/ouvrir.png"));
		ImageIcon ouvrir = new ImageIcon(img1);
		JButton jbOuvrir = new JButton(ouvrir);
		pan1.setPreferredSize(new Dimension(50,65));
		jbOuvrir.setToolTipText("Ouvre un fichier");
		//jbOuvrir.setPreferredSize(new Dimension(50,50));
		jbOuvrir.addActionListener(new ActionOuvrir());
		jbOuvrir.setBackground(Color.WHITE);
		pan1.add(jbOuvrir);
		l1.add(pan1);

		JPanel pan2 = new JPanel();
		BufferedImage img2 = ImageIO.read(new File("images/test.jfif"));
		ImageIcon test = new ImageIcon(img2);
		JButton jbAnalyse = new JButton(test);
		pan2.setPreferredSize(new Dimension(50,65));
		jbAnalyse.setToolTipText("Analyse le fichier pour déterminer son type");
		jbAnalyse.addActionListener(new ActionAnalyse());
		jbAnalyse.setBackground(Color.WHITE);
		pan2.add(jbAnalyse);
		l1.add(pan2);
		
		JPanel pan3 = new JPanel();
		BufferedImage img3 = ImageIO.read(new File("images/reset.jfif"));
		ImageIcon reset = new ImageIcon(img3);
		JButton jbReset = new JButton(reset);
		pan3.setPreferredSize(new Dimension(50,65));
		jbReset.setToolTipText("Vide la liste de tous ses fichiers, reinitialise l'affichage");
		jbReset.addActionListener(new ActionReset());
		jbReset.setBackground(Color.WHITE);
		pan3.add(jbReset);
		l1.add(pan3);
		
		JPanel pan4 = new JPanel();
		BufferedImage img4 = ImageIO.read(new File("images/quitter.jfif"));
		ImageIcon quitter = new ImageIcon(img4);
		JButton jbQuitter = new JButton(quitter);
		pan4.setPreferredSize(new Dimension(50,65));
		jbQuitter.setToolTipText("Sortir du programme");
		jbQuitter.addActionListener(new ActionQuitter());
		jbQuitter.setBackground(Color.WHITE);
		pan4.add(jbQuitter);
		l1.add(pan4);
		
		jtfEtat.append("Init.\n");
        jtfEtat.setMargin(new Insets(5,5,5,5));
		jtfEtat.setEditable(false);
		JScrollPane l2 = new JScrollPane(jtfEtat);
		l2.setAutoscrolls(true);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 40;
		c.ipady = 20;
		this.getContentPane().add(l1,c);
		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 1000;
		c.ipady = 200;
		c.gridwidth = 3;
		this.getContentPane().add(l2,c);

		JMenuBar menuBar = new JMenuBar();
		JMenu fichier = new JMenu("Fichier");
		JMenuItem help = new JMenuItem("Aide");
		help.addActionListener(new ActionAide());
		JMenuItem ouvre = new JMenuItem("Ouvrir");
		ouvre.addActionListener(new ActionOuvrir());
		JMenuItem analyse = new JMenuItem("Analyse");
		analyse.addActionListener(new ActionAnalyse());
		JMenuItem res = new JMenuItem("Reset");
		res.addActionListener(new ActionReset());
		JMenuItem quit = new JMenuItem("Quitter");
		quit.addActionListener(new ActionQuitter());
		JMenu aide = new JMenu("?");
		aide.addActionListener(new ActionAide());
		aide.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jtfEtat.append("Ce programme permet d'analyser des fichiers et des dossiers. Utilisez: "
						+ "\n\t Cliquer sur le bouton 'ouvrir' pour choisir le fichier (ou dossier) à analyser,"
						+ "\n\t Cliquer sur le bouton 'test' pour analyser les documents ouverts"
						+ "\n\t Cliquer sur le bouton 'reset' pour réinitialiser le JTextArea,"
						+ "\n\t Cliquer sur le bouton 'quitter' pour quitter la JFrame.");
			}
		});
		fichier.add(help);
		fichier.add(ouvre);
		fichier.add(analyse);
		fichier.add(res);
		fichier.add(quit);
		menuBar.add(fichier);
		menuBar.add(aide);
		this.setJMenuBar(menuBar);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(100, 100);
		this.setSize(1200, 400);
		//this.pack();
		this.setVisible(true);
	}
	
	/**
	 * Bouton d'ouverture de fichier
	 */
	private class ActionOuvrir implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int returnVal = jfc.showOpenDialog(FichierMp3GUI.this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    		try {
						Ouverture(jfc.getSelectedFile());
					} catch (UnsupportedTagException | InvalidDataException | IOException | XMLStreamException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    }
		    else {
		    	jtfEtat.append("Erreur lors de l'ouverture du fichier.\n");
		    }
		}
	}
	
	/**
	 * Rajoute à une LinkedList<File> un fichier ou cherche dans un dossier l'existence d'autre fichier et dossier.
	 * @param f un fichier ou un dossier à rajouter dans une liste.
	 * @return une liste de fichier.
	 * @throws XMLStreamException : probleme de traitement
	 * @throws IOException : probleme d'autorisation
	 * @throws InvalidDataException : probleme de donnée
	 * @throws UnsupportedTagException : probleme de tag
	 */
	private LinkedList<File> Ouverture (File f) throws UnsupportedTagException, InvalidDataException, IOException, XMLStreamException  {
		if (f.isDirectory()) {
			mp3.listeDossier(f,listeMp3);
			File[] listF = f.listFiles();
			for (int i = 0 ; i < listF.length ; i++) {
				Ouverture(listF[i]);
			}
		}
		else if (f.isFile() ) {
			if (!listeFichier.contains(f) ) {
				if (Long.signum(f.length()) > 0) {
					if (listeFichier.add(f)) {
						jtfEtat.append("Ouverture de "+f+".\n");
					}else {
						jtfEtat.append("Erreur lors de l'ouverture du fichier.\n");
					}
				}else {
					jtfEtat.append("Impossible d'ouvrir "+f+" . Le fichier est vide.\n");
				}
			}else { 
				jtfEtat.append("Fichier déjà ouvert.\n");
			}
		}
		return listeFichier;
	}
	
	/**
	 * Bouton d'Analyse
	 */
	private class ActionAnalyse implements ActionListener {
		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent e) {
			if (!listeFichier.isEmpty()) {
				jtfEtat.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n"
							 + "\tDébut de l'analyse des fichiers.\n"
							 + "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
				for (Iterator<File> it = listeFichier.iterator(); it.hasNext(); ) {
					File doc = it.next();
						try {
							jtfEtat.append("Analyse du fichier "+doc.getPath()+"\n"+mp3.analyseFichier(doc)+"\n\n");
						} catch (UnsupportedTagException | InvalidDataException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (XMLStreamException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}
			}
			else {
				jtfEtat.append("Aucun fichier.\n");
			}
		}
	}
	
	/**
	 * Bouton Reinitialisation
	 */
	private class ActionReset implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!listeFichier.isEmpty()) {
				listeFichier.clear();
			}
			jtfEtat.setText("Init.\n");
		}
	}
	
	/**
	 * Bouton pour quitter le programme
	 */
	private  class ActionQuitter implements ActionListener {
		@SuppressWarnings({ "unused", "static-access" })
		public void actionPerformed(ActionEvent e) {
			Integer choix = JOptionPane.showConfirmDialog(null, "Voulez-vous quitter ?", "Fermeture...", JOptionPane.YES_NO_OPTION);
			if(choix== JOptionPane.YES_OPTION) {
				mp3.writeXML("GUIPlaylist.xspf","playlist", listeMp3);
			}
			System.exit(0);
		}
	}
	
	/**
	 * Bouton d'aide dans le JMenuBar 
	 *
	 */
	private static class ActionAide implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			jtfEtat.setText("Ce programme permet d'analyser des fichiers et des dossiers. Utilisez: "
					+ "\n\t Cliquer sur le bouton 'ouvrir' pour choisir le fichier (ou dossier) à analyser,"
					+ "\n\t Cliquer sur le bouton 'test' pour analyser les documents ouverts"
					+ "\n\t Cliquer sur le bouton 'reset' pour réinitialiser le JTextArea,"
					+ "\n\t Cliquer sur le bouton 'quitter' pour quitter la JFrame.");
		}
	}

	/**
	 * @param args : argument passer en parametre 
	 * @throws IOException : probleme d'autorisation
	 */
	public static void main(String[]args) throws IOException {
		new FichierMp3GUI();
	}
}
