# ColtExpressGamePro

Ce projet vous a comme objectif de construire une version simplifiée du jeu Colt Express.


## Aperçu des règles du jeu

Le jeu se déroule à bord d’un train, composé d’une locomotive et d’un certain nombre de wagons. Les
joueurs incarnent des bandits qui ont sauté à bord pour détrousser les passagers. Objectif : récupérer le
plus de butins possible, chacun pour soi. Il s’agit d’un jeu dans lequel on alterne entre
deux phases :\
**Planification** : chaque joueur décide secrètement un certain nombres d’actions, que son personnage
va effectuer dans l’ordre.
**Action** : on effectue toutes les actions numéro 1, puis toutes les numéro 2, et ce jusqu’au bout.
Les bandits peuvent se trouver dans les wagons ou la locomotive, et pour chacun de ces éléments soit à
l’intérieur soit sur le toit. Dans cet énoncé, par abus de langage on désignera par « wagon » un élément
quelconque du train, qui peut être la locomotive. Les actions possibles pour les bandits sont :
* Se déplacer d’un wagon en avant ou en arrière, en restant au même étage.
* Aller à l’intérieur ou grimper sur le toit de leur wagon actuel.
* Braquer un voyageur pour récupérer du butin (ou simplement récupérer un butin qui a été
abandonné là).
* Tirer sur un autre bandit proche pour lui faire lâcher son butin.
Les butins récupérables à bord du train sont :
* Des bourses valant entre 0 et 500$, auprès des passagers, à l’intérieur des wagons.
* Des bijoux valant 500$, auprès des passagers, à l’intérieur des wagons.
* Un magot valant 1000$, à l’intérieur de la locomotive, sous la garde du Marshall. 

Un Marshall est présent à bord du train et peut se déplacer entre la locomotive et les wagons, en restant
toujours à l’intérieur. Il tire sur tous les bandits qui se trouvent à la même position que lui et les force à
se retrancher sur le toit.

## Parties de sujets traites

On a reussi a implémenter toutes les fonctionalités demandées.
Ce jeu peut se jouer à 3 personnes, en alternant entre les deux phases principales : *Panification et Action*\
Nous avons ajouté quelques details vu la liberte donne par l'enonce. \
On a surtout essaye d'implementer ce que nous avons fait en cours avec [M. Balabonski](https://www.lri.fr/~blsk/POGL/) (Classe interne, LambdaExpression, Interface fonctionelle, Iterors, Observers, ForEach ...etc)
Chaque classe de notre modele a redéfinit la fonction toString() qui affiche l'etat detaillé de chaque instance. Cela a permit de tester le modele avant faire les tests junit et l'affichage.

## Architecture de projt

Le projet est organisée selon une architecture Modèle-Vue-Contrôleur (MVC Design Pattern). Le but est la séparation des deux parties suivantes :
- Le coeur de l'application, appelé le modèle, où est fait l'essentiel du travail.
- L'interface utilisateur, appelée la vue, qui à la fois montre des choses à l'utilisateur et lui fournit des moyens d'interagir.

Le modèle est indépendant des autres modules. Il ne se sert ni de la vue ni du contrôleur, il peut cependant leur envoyer des messages3. 
Il y a deux liens entre la vue et le modèle: premièrement la vue lit l'etat du modèle et deuxièmement reçoit des messages provenant du modèle. \
La vue est dépendante du modèle. Elle interroge celui-ci pour en afficher une représentation.\
Le contrôleur dépend de la vue et du modèle : la vue comporte des éléments visuels que l'utilisateur peut actionner. Le contrôleur répond aux actions effectuées sur la vue et modifie l'etat du modèle. \
Plus des detailles sur [MVC wikipidia](https://fr.wikipedia.org/wiki/Mod%C3%A8le-vue-contr%C3%B4leur)

## Travail en groupe

Ce projet a ete realise par GARDILLE Arnaud et ARBACHE Jean.
**Jean ARBACHE:** Conception de la Modele (package modele)
**Arnaud GARDILLE:** Utilisateur de modele et concepteur de Vue et de Controleur (package vue).

# Modele


### Train

Notre Class train représente le model principale de jeu. C'est une liste dounlement chainée  **DLL** , *Iterable*  et *Observable*.

Il consite d'une DLL de wagons commencent par la *locomotive* suive par *firstwagon* qui son de type Train.Wagon. Il contient aussi un *marshall* et une liste des *joueurs*, qui sont les bandits.\
Il contient l'ensemble des constantes qui seront utile pour l'initialisations, les invariants et les tests.\
Le train est notre modele prncipale. La classe train contient certaines methodes en visibiite *public* pour que l'affichage et le controleur puissent l'utiliser, conformément au Design Patern **MVC**.\

> *Rdv dans le codes*   dans **Gestion de jeu** pour regarder en detailles ces fonctions
```java
    // **************************************************
    // Gestion de jeu
    // **************************************************
```

> Vous pouvez regarder la deroulement du jeu en excutant la fonction main de ce classe et en regardant la console.
```java
    public static void main(String args[]) {
		Train t = new Train();
		print(t);
		//Cette fonction creer des actions deja defini a l'avance 
		t.actionsPreDefini();
		print("\n\nAfter action 1\n\n " + t);
		t.excuteTour();
		print("\n\nAfter action 2\n\n " + t);
		t.excuteTour();
		print("\n\nAfter action 3\n\n " + t);
		t.excuteTour();
		print("\n\nAfter action 4\n\n " + t);
		t.excuteTour();
		print("\n\nAfter action 5\n\n " + t);
		t.excuteTour();
		print("\n\nafter all actions \n\n" + t);
	}
```

#### Train.Wagon

Wagon est une classe interne de train. Cela permet l'acces aux attributs et aux methodes depuis ces deux classes sans casser l'encapsulation.\
Cette classe est aussi une sous-classe de classe abstrait *Possesseur*, carecterise les elements qui peuvent contenir des burins decrite plus bas, car dans les wagons on trouve les butins. 
Comme chaque wagon est un element de la DLL Train, il a accesibilte au wagon *suivant* et *precedent*.\
Il possède un ensemble '*HashSet*' des pointeurs vers les bandits dans ce wagon. Le choix de HashSet est justifiée car l'ordre n'a pas d'importance.
Chaque wagon a un ordre d'apparition dans le train comme attribut, mais il peut etre egalement calculé depuis le train. Cela est tres utile pour les tests; on peut verifier si les deux valeurs correspondent bien.
On sait que dans notre jeu il y a un *marshal* qui se déplace dans les wagons. Soit le marshal est dans ce wagon soit non. Pour cela on a choisit de mettre un *boolean* qui sera = true si le marshal est dans ce wagon.\
Vu que Wagon est une classe interne de Train, notre modele, elle a une certain responsabilité dans la gestion du deroulement du jeu. Elle est la responsable pincipale pour le deplacement des personnages et pour gerer les tires.

> *Rdv dans le codes*   dans **Utilities functions for Personne** et **Utilities functions for ActionList class**

```java
        // **************************************************
        // Utilities functions for Personne
        // The Wagon class is responsible for the deplacement of Personne
        // @see Bandit::executeAction() Marshal::executeAction()
        // **************************************************
```
```java
        // **************************************************
        // Utilities functions for ActionList class
        // The Wagon class is responsible for managing the procedure of shooting, stealing 
        // @see Bandit::executeAction() Marshal::executeAction()
        // **************************************************
```



### ForEach

Cette classe utilise les notions d'interface fonctionelles est de lambda expression vue en cour avec [M.Balabonski](https://www.lri.fr/~blsk/POGL/LambdaExpressions.java).
```java


/** Passer une méthode en paramètre. 
 *  On cherche à définir une méthode [forEach] attendant deux paramètres : 
 * - une collection d'objets de type [T], implémentant [Iterable<T>] 
 * - une méthode [void f(T elt)] à appliquer à chaque élément de la collection 
 * 
 * 
 * Comme on ne peut pas passer de méthode en paramètre à une autre méthode, 
 * on peut à la place utiliser un objet possédant la méthode [f] souhaitée.
 * 
 *  Nous définissons ci-dessous une interface fonctionelle [FProvider<T>] qui caractérise les classes fournissant une méthode [f] avec la bonne signature. 
 *  */

/**
 * 
 * @author arbache
 *
 * @param <T>
 */
class ForEach<T> {
	public static <T> void forEach(Iterable<T> c, FProvider<T> f) {
		Boolean b = new Boolean(true);
		for(T e: c) f.f(e);
	}
	
	interface FProvider <T>{
		void f (T elt);
	}
}

```
Cela etait tres utile pour parcourir les wagons, bandits et les butins est realiser des tests.\
> Exemple

```java
/** 
	    * This method is used to find check all the possible variants of the model 
	    * @return boolean This returns true if all the variants are correct, false if one failed
	    */
	protected boolean checkInvariants() {
		return ForEachPredicat.forEach(this, w->this.checkWagonOrdre(w)) && 
				ForEachPredicat.forEach(this.joueurs, b -> this.checkBanditPlacement(b)) &&
				ForEachPredicat.forEach(this, w -> this.checkButinPlacement(w));
				
	}

```

```java
@Test
	public void testInitialPlace() {
		assert ForEachPredicat.forEach(t.getBandits(), b -> t.getLastWagon().bandits.contains(b)) : "The initial place of bandits is flase";
	}

```


### TrainTest

C'est une classe de test pour le modèle. Il s'agit bien à la fois d'un test unitaire pour le train et un test d'integralité pour le modèle.

### Possesseur

C'est une classe abstraite qui represente les elements de notre modele qui peuvent posseder des Butins. Elle a deux sous-classes Personne et Train.Wagon.\
Elle fonctionne comme la structure de donnée *HashSet* dont le nombre d'élements est limité. Ajouter des élements quand l'ensemble est rempli n'a pas d'effet. Enlever des elements se fait par un tirage aleatoire sur l'ensemble.\
les deux fonctions *Possesseur::popButin()* et  *Possesseur::addButin()* sont utilsées pendant l'initialisation dans la calsse *Train* et  Action.Tirer et Action.Braquer dans la classe *Train.Wagon* lors du deroulement du jeu.\
Au debut pour cette classe on a utilise la structure de donne *Stack* (FILO). Le but etait de faire tomber le dernier butin recuperer par les bandits quand on tire. Cela a ete change a un *HashSet* qui corresspond bien a l'enonce du projet.

### Personne

Une personne est un possesseur aussi. Son etat est determine par son *nom*, le *wagon* où il est , l'ensemble des *actions* qu'il peut prendre sont de type Personne.ActionList et le modele Train.\
La fonction **mettrePersonneBonWagon**  utilise les notions de polymorphisme et l'interpretation dynamique de java vu en cours avec [M. Balabonski](https://www.lri.fr/~blsk/POGL/) pour mettre la personnage en bon endroit. 
Cette fonction est implémentée dans les sous-classe *Bandit* et *Marshal*.

```java
	//this method will be used in contructor and we will redefine it in each sub-class 
	//according to polymorphisme, the method applied in the contructor are the good one
	/**
	 * This method will be used in contructor and we will redefine it in each sub-class ,
	 * according to polymorphisme and dynamic interpretation of Java, the method applied in the constructor are the one corresponding with the right class
	 * 
	 * @param t the train (the model)
	 * @param p the personne that we should put in the right place
	 * @return Train.Wagon the right wagon where we have to put the personne
	 */
	abstract Train.Wagon mettrePersonneBonWagon(Train t, Personne p); // return the wagon where p should be
	
```



#### Personne.ActionList

L'objectif de cette classe interne est de gérer les actions sans mettre trop de code dans les autre classes.\
ActionList fonctionne exactement comme la structure de donnée *Queue* (FIFO). L'action à executer (out) est la premier action qu'on a ajouté. Cette structure de données repond exactement a notre besoin.\
On a essayé d'utiliser la structure de file donnee par java, mais cela foctionnait mal.

### Marshall et Bandit

Deux sous-classe de personne. Elle implemente les fonctions d'une facon differente chaque une en fonction de son role dans le jeu.




# Vue et Controleur

La classe CEVue s’occupe de l’affichage et du contrôleur relatifs à la classe Train.

Les attributs numAction et numBandit permettent au sous classe de savoir de quelle action et de quel bandit on est en train de s’occuper, tant pendant la planification que pendant l’exécution.

J’ai choisis de placer 4 JPanel au sein de ma JFrame:

- vueSac : décrit les butins possédés par chaque bandit, la somme de leurs valeurs, ainsi que le nombre de balles qui lui restent. Instancie la classe VueSac qui hérite de JPanel.
- vueCommande : Ensemble de JBouton qui permettent au banditCourrant de planifier ses actions, puis d’effectuer les action planifiées. Instancie la classe VueCommande qui hérite de JPanel, et qui implémente l’interface KeyListener afin de pouvoir réarige aux touches du clavier.
- vueTrain : affichage du train, c’est à dire de la locomotive et des wagons, ainsi que des personnages et des butins. 
    Instancie la classe VueTrain qui hérite de JPanel, et qui implémente l’interface Observer, afin de réagir aux notifications envoyées par le modèle.
- Tableau: un JTable qui représente l’objet dataTableau. Permet l’affichage des action planifiées pour chaque joueur.

---


La classe CEVue joue a la fois le rôle de la vue et du contrôleur au sein de l’implémentation du desing pattern modèle-vue-contrôleur. 

J’ai implémenté le design patern observable-observer pour récupérer les informations du train.
Le train, qui représente le modèle, étend la classe Observable. Il hérite donc des méthodes addObserver et notifyObservers. 
La classe CEVue implémente Observer, et notamment la méthode update en lui disant de tout redessiner. Dans son constructeur, CEVue s’ajoute comme observer du train. Ce dernier va appeler notifyObserver pour déclencher une mise à jour de l’affichage.

La classe interne vueCommande se charge d’ajouter des actions au banditCourrant lorsque l’on clique sur un bouton ou que l’on appuis sur le clavier. Cela permet au contrôleur VueCommande d’agir sur le modèle Train.
 
---

Concernant l’envoi d’action aux bandits, j’avais commencé par coder une classe abstraite Bouton qui représentait l’action a exécuter lors du cliquage d’un bouton. Je lui faisais hériter une classe pour chaque bouton. Mais je me suis rendu compte qu’elles ne différaient que d’une méthode.
Après avoir renommé la classe Bouton en SendAction, je l’ai dérivé en autant de classe anonyme que besoin au sein du constructeur de VueCommande. J’ai mis en commentaire les anciennes classes héritées de bouton.
Ainsi, mon code plus lisible et plus concis.

Au sein de la classe vueCommande, J’ai mis tous les boutons et leurs actions associées dans un HashSet afin de pouvoir les parcourir plus facilement. Cela est utilisé dans la fonction majBoutons(). 






