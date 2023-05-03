#ifndef PROJET_LISTE_H
#define PROJET_LISTE_H

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define noeudracine creationRacine()
#define noeudcurseur creationRacine()

struct noeud;
struct liste_noeud;
struct noeud{
    bool est_dossier;
    char nom[100];
    struct noeud *pere;
    struct noeud *racine;
    struct liste_noeud *fils;
};
struct liste_noeud{
    struct noeud *no;
    struct liste_noeud *succ;
};
typedef struct noeud noeud;
typedef struct liste_noeud liste_noeud;

///ls
extern void affiche(char[100]);
extern void commande_ls(noeud* );


///mkdir
extern void commande_mkdir(noeud* ,char*);
extern noeud* creationRacine();
extern noeud* creationNoeud(noeud*,char[100]);




#endif //PROJET_LISTE_H
