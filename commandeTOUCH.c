#include "liste.h"
#include <ctype.h>

void commande_touch(noeud *n,char* nom){
    if(strlen(nom)>99){
        perror("Nom de fichier trop long");
        exit(EXIT_FAILURE);
    }
    for(int i=0;i< strlen(nom);++i){   ///vérifier si tout les caractères sont alpha-numerique
        if(isalnum(*(nom+i))==0){}
        perror("Format incorrecte");
        exit(EXIT_FAILURE);
    }
    liste_noeud* ln=n->fils;
    while(ln->succ!=NULL){
        if(strcmp(nom,ln->no->nom)!=0){
            perror("Il existe déjà un nombre de fichier ou dossier ayant ce nom");
            exit(EXIT_FAILURE);
        }
        ln=ln->succ;
    }
    noeud* fichier= creationNoeudFichier(n,nom);
    liste_noeud* f={NULL,fichier};
    ln->succ=f;
}
noeud* creationNoeudFichier(noeud* n,char* nom){
    noeud* fichier= malloc(sizeof(noeud));
    for(int i=0;i<100;++i) fichier->nom[i]=*(nom+i);
    fichier->pere=n;
    fichier->racine=noeudracine;
    fichier->fils=NULL;
    return fichier;
}
