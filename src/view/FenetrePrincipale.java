package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.Direction;
import controller.Partie;

public class FenetrePrincipale extends JFrame {
	
	private Partie partie;
	//private ChoixCouleur choixCouleur;
	//private ChoixImage choixImage;
	private Regles regles;
	private ChoixCouleur choixCouleur;
	private JButton[] cases = new JButton[16];
	private Color couleur = Color.BLUE;
	JPanel panneauDeJeu;
	JMenuItem itemNouvellePartie;
	JMenuItem itemArreterPartie;
	JMenuItem itemCoupSuivantAide;

	public FenetrePrincipale() {
		super("Taquin");
		try { // Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
		    e.getMessage();
		}
		partie = new Partie();
		choixCouleur = new ChoixCouleur(this, "Choix couleur");
		//choixImage = new ChoixImage("Choix image");
		regles = new Regles(this, "Règles");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(contPane());
		setJMenuBar(menuBar(this));
		pack();
		setVisible(true);
	}
	
	public JPanel contPane() {
		JPanel contPane = new JPanel();
		contPane.setBackground(Color.WHITE);
		contPane.setLayout(new BorderLayout());
		contPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		panneauDeJeu = new JPanel(new GridLayout(4,4));
		panneauDeJeu.setPreferredSize(new Dimension(400, 400));
		panneauDeJeu.setBackground(Color.WHITE);


		for(int i = 0; i < 16; i++) {

			JButton bouton = new JButton();
			bouton.setFont(new Font("Arial", Font.PLAIN, 20));
			bouton.setBackground(couleur);
			bouton.setOpaque(true);
			bouton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			cases[i] = bouton;
			panneauDeJeu.add(cases[i]);
		}
		
		updateCases(partie.getGrille().getMatrice());
		
		contPane.add(panneauDeJeu, BorderLayout.CENTER);
		return contPane;
	}
	
	public JMenuBar menuBar(FenetrePrincipale parent) {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menuPartie = new JMenu("Partie");
		JMenu menuParametre = new JMenu("Paramètre");
		JMenu menuAide = new JMenu("Aide");
		
		menuBar.add(menuPartie);
		menuBar.add(menuParametre);
		menuBar.add(menuAide);
		
		itemNouvellePartie = new JMenuItem("Nouvelle ..");
		itemArreterPartie = new JMenuItem("Arrêter ..");
		
		itemArreterPartie.setEnabled(false);
		
		itemNouvellePartie.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				partie.nouvelle();
				panneauDeJeu.removeAll();
				int v = 0;
				int w = 0;
				for(int i = 0; i < 16; i++) {
					
					if(i%4 ==0 && i!= 0){
						v = 0;
						w ++;
					}
					JButton bouton = new JButton();
					bouton.setFont(new Font("Arial", Font.PLAIN, 20));
					bouton.addActionListener(new EcouteurCase(v, w, partie, parent));
					bouton.setBackground(couleur);
					bouton.setOpaque(true);
					bouton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					cases[i] = bouton;
					panneauDeJeu.add(cases[i]);
					v++;
				}
				
				updateCases(partie.getGrille().getMatrice());
				validate();
				itemArreterPartie.setEnabled(true);
				itemNouvellePartie.setEnabled(false);
				itemCoupSuivantAide.setEnabled(true);
	        }
			
		});
		
		itemArreterPartie.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				partie.arreter();
				panneauDeJeu.removeAll();

				for(int i = 0; i < 16; i++) {

					JButton bouton = new JButton();
					bouton.setFont(new Font("Arial", Font.PLAIN, 20));
					bouton.setBackground(couleur);
					bouton.setOpaque(true);
					bouton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					cases[i] = bouton;
					panneauDeJeu.add(cases[i]);
				}
				
				updateCases(partie.getGrille().getMatrice());
				validate();
				itemArreterPartie.setEnabled(false);
				itemNouvellePartie.setEnabled(true);
				itemCoupSuivantAide.setEnabled(false);
	        }
			
		});
		
		menuPartie.add(itemNouvellePartie);
		menuPartie.add(itemArreterPartie);
		
		JMenuItem itemCouleurParametre = new JMenuItem("Couleur");
		JMenuItem itemImageParametre = new JMenuItem("Image");
		itemImageParametre.setEnabled(false);
		
		itemCouleurParametre.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				//choixCouleur.setVisible(true);
				couleur = JColorChooser.showDialog(null, "couleur du fond", couleur);
				updateTabCases();
	        }
		});
		
		itemImageParametre.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				//choixImage.setVisible(true);
	        }
		});
		
		menuParametre.add(itemCouleurParametre);
		menuParametre.add(itemImageParametre);
		
		JMenuItem itemRegleAide = new JMenuItem("Règles");
		itemCoupSuivantAide = new JMenuItem("Coup Suivant");
		itemCoupSuivantAide.setEnabled(false);
		JMenuItem itemSolutionAide = new JMenuItem("Solution");
		itemSolutionAide.setEnabled(false);
		
		itemRegleAide.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				regles.setVisible(true);
	        }
		});
		
		itemCoupSuivantAide.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				aideCoupSuivant();
	        }
		});
		
		itemSolutionAide.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				solution();
	        }
		});
		
		menuAide.add(itemRegleAide);
		menuAide.add(itemCoupSuivantAide);
		menuAide.add(itemSolutionAide);
		
		return menuBar;
	}
	
	private void aideCoupSuivant(){
		Direction prochainCoup = partie.getGrille().getPileCoups().getResolution();
		partie.getGrille().getPileCoups().depiler();
		if(prochainCoup == Direction.BAS){
			partie.deplacer(Direction.HAUT);
		}
		if(prochainCoup == Direction.HAUT){
			partie.deplacer(Direction.BAS);
		}
		if(prochainCoup == Direction.DROITE){
			partie.deplacer(Direction.GAUCHE);
		}
		if(prochainCoup == Direction.GAUCHE){
			partie.deplacer(Direction.DROITE);
		}
		updateTabCases();
		fin();
	}
	
	private void solution() {
		
	}
	
	private void updateCases(int[][] matrice) {
		int cpt = 0;
		for(int i = 0; i < matrice.length; i++){
			for(int j = 0; j < matrice[i].length; j++){
				if(matrice[i][j] == 0){
					cases[cpt].setEnabled(false);
				} else {
					cases[cpt].setText(""+matrice[i][j]);
				}
				cpt++;
			}
		}
	}

	public void updateTabCases(){
		panneauDeJeu.removeAll();
		int v = 0;
		int w = 0;
		for(int i = 0; i < 16; i++) {
			
			if(i%4 ==0 && i!= 0){
				v = 0;
				w ++;
			}
			JButton bouton = new JButton();
			bouton.setFont(new Font("Arial", Font.PLAIN, 20));
			bouton.addActionListener(new EcouteurCase(v, w, partie, this));
			bouton.setBackground(couleur);
			bouton.setOpaque(true);
			bouton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			cases[i] = bouton;
			panneauDeJeu.add(cases[i]);
			v++;
		}
		
		updateCases(partie.getGrille().getMatrice());
		validate();
	}

	public void fin(){
		int[][] matrice = partie.getGrille().getMatrice();
		int cpt = 1;
		boolean gagner = true;
		
		for(int i = 0; i < matrice.length; i++){
			for(int j = 0; j < matrice[i].length; j++){
				if(matrice[i][j] != cpt && matrice[i][j] > 0){
					gagner = false;
					break;
				} 
				cpt++;
			}
		}
		if(gagner){
			JOptionPane.showMessageDialog(this, "Vous avez gagner !", "BRAVO !!!", JOptionPane.PLAIN_MESSAGE,null);
			partie.arreter();
			panneauDeJeu.removeAll();

			for(int i = 0; i < 16; i++) {

				JButton bouton = new JButton();
				bouton.setFont(new Font("Arial", Font.PLAIN, 20));
				bouton.setBackground(couleur);
				bouton.setOpaque(true);
				bouton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				cases[i] = bouton;
				panneauDeJeu.add(cases[i]);
			}
			
			updateCases(partie.getGrille().getMatrice());
			validate();
			itemArreterPartie.setEnabled(false);
			itemNouvellePartie.setEnabled(true);
		}
	}
	
	public static void main(String[] args) {
		FenetrePrincipale fenetre = new FenetrePrincipale();
		/*
		Partie partie = new Partie();
		System.out.println(partie.getGrille());
		
		partie.nouvelle();
		System.out.println(partie.getGrille());
		
		try {
			partie.deplacer(Direction.DROITE);
			System.out.println(partie.getGrille());
		} catch(ArrayIndexOutOfBoundsException e) {}
		
		partie.arreter();
		System.out.println(partie.getGrille());*/
	}
}
