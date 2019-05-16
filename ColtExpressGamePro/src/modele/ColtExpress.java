package modele;

import java.awt.EventQueue;

import vue.CEVue;


public class ColtExpress {
	public static void main(String[] args){
		EventQueue.invokeLater(() -> {
			/** Voici le contenu qui nous int�resse. */
			Train modele = new Train();
			Personne b1 = new Bandit(modele, "Bandito");
			Personne b2 = new Bandit(modele, "Zorro");
			Personne b3 = new Bandit(modele, "Garcia");
			Personne mars = new Marshall(modele, "Marshall");
			CEVue vue = new CEVue(modele);
		    });


	}

}
