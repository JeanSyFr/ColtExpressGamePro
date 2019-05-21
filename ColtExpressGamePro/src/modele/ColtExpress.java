package modele;

import java.awt.EventQueue;

import vue.CEVue;


public class ColtExpress {
	public static void main(String[] args){
		EventQueue.invokeLater(() -> {
			/** Voici le contenu qui nous int�resse. */
			Train modele = new Train();
			CEVue vue = new CEVue(modele);
		    });
	}

}
