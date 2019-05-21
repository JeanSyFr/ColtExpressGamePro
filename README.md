# ColtExpressGamePro

Ce projet vous a comme objective  construire une version électronique et un peu simplifiée du jeu Colt Express.


## Aperçu des règles du jeu

Le jeu se déroule à bord d’un train, composé d’une locomotive et d’un certain nombre de wagons. Les
joueurs incarnent des bandits qui ont sauté à bord pour détrousser les passagers. Objectif : récupérer le
plus de butin possible, chacun pour soi. Il s’agit d’un jeu de programmation, dans lequel on alterne entre
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

On a reussi a repondre a tous les besoin de projet et de fonctionalite demande.
Ce jeu peut se jouer a 3 personne, en alternant entre les deux phases principales : *Panification et Action*\
On est quand meme alle plus loin et ajuote quelques details vu la liberte donne par l'enonce. \
On surtout essaye d'implementer ce qu'on a fait en cours avec M. Balabonski 

## Architecture de projt

### Train

Notre Class train est en effait le model principale de jeu. C'est un list dounlement chaine **DLL** , *Iterable*  et **Observable**.

Il consite d'une DLL de wagon commencent par la *locomotive* suive par *firstwagon* qui son de type Train.Wagon. Il contient aussi un *marshall* et une liste des *joueurs* qui sont les bandirs.

Il contient un ensemble des constantes qui seront utile pour l'initialisations et







