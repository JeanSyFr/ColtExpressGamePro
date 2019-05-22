package jouer;

import java.awt.EventQueue;

import modele.Train;
import vue.CEVue;


public class ColtExpress {
	public static void main(String[] args){
		EventQueue.invokeLater(() -> {
			Train modele = new Train();
			CEVue vue = new CEVue(modele);
		    });
	}

}
