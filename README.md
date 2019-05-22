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
quelconque du train, qui peut être la locomotive. Les actions possibles pour les bandits sont :\
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

### Train

Notre Class train représente le model principale de jeu. C'est une liste dounlement chainée  **DLL** , *Iterable*  et *Observable*.

Il consite d'une DLL de wagons commencent par la *locomotive* suive par *firstwagon* qui son de type Train.Wagon. Il contient aussi un *marshall* et une liste des *joueurs*, qui sont les bandits.\
Il contient l'ensemble des constantes qui seront utile pour l'initialisations, les invariants et les tests.\
Le train est notre modele prncipale. La classe train contient certaines methodes en visibiite *public* pour que l'affichage et le controleur puissent l'utiliser, conformément au Design Patern **MVC**.\

> *Rdv dans le codes*   dans **Gestion de jeu**
```java
    // **************************************************
    // Gestion de jeu
    // **************************************************
```

#### Train.Wagon

Wagon est une classe interne de train. Cela permet d'acceder aux attributs et aux methodes sans casser l'encapsulation.\
Cette classe est aussi une sous-classe de classe abstrait *Possesseur*, decrite plus bas, car dans les wagons on trouve les butins. 
Comme chaque wagon est un element de la DLL Train, il a accesibilte au wagon *suivant* et *precedent*.\
Il possède un ensemble '*HashSet*' des pointeurs vers les bandits dans ce wagon. Le choix de HashSet est justifiée car l'ordre n'a pas d'importance.
Chaque wagon a un ordre d'apparition dans le train comme attribut, mais il peut etre egalement etre calculé depuis le train. Cela est tres utile pour les tests; on peut verifier si les deux valeurs correspondent.
On sait que dans notre jeu il y a un *marshal* qui se déplace dans les wagons. Soit le marshal est dans ce wagon soit non. Pour cela on a choisit de mettre un *boolean* qui sera = true si le marshal est dans ce wagon.\
Vu que Wagon est une classe interne de Train, notre modele, elle a une certain responsabilité dans la gestion du deroulement du jeu. Elle est la responsable pinsipale pour le deplacement des personnages et pour les tires.

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
Elle fonctionne comme la structure de donnée *HashSet* dont le nombre d'élements est limité. Ajouter des élements quand c'est rempli n'a pas d'effet. Enlever des elements se fait par un tirage aleatoire sur l'ensemble.\
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








