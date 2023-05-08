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
extern noeud* commande_mkdir(noeud* ,char*);
extern noeud* creationRacine();
extern noeud* creationNoeudDossier(noeud*,char[100]);

///pwd
extern void commande_pwd();
extern int profondeurNoeud();

///touch
extern void commande_touch(noeud*,char*);
extern noeud* creationNoeudFichier(noeud* ,char*);

///print
extern void print_dossier(noeud*);


///remove
extern void commande_remove(noeud*);
extern void remove_fichier(noeud* );
extern void remove_dossier(noeud*);





#endif //PROJET_LISTE_H
