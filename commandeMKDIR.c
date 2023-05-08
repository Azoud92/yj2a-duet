#include "liste.h"
#include <ctype.h>

///Commande mkdir
noeud* commande_mkdir(noeud* l,char* string){
    if(strlen(string)>99){                 ///vérifier si une chaine est de longueur moins que 100
        perror("Très longue chaîne");
        exit(EXIT_FAILURE);
    }

    for(int i=0;i< strlen(string);++i){   ///vérifier si tout les caractères sont alpha-numerique
        if(isalnum(*(string+i))==0) {
            perror("Format incorrecte");
            exit(EXIT_FAILURE);
        }
    }
    noeud* newNoeud= creationNoeudDossier(l,string);
    if(l->fils==NULL){
        liste_noeud* ln= malloc(sizeof(liste_noeud));
        ln->no=newNoeud;
        ln->succ=NULL;
        l->fils=ln;
    }else {
        noeud *current = l->fils->no;
        liste_noeud *currentListe = l->fils->succ;
        while (currentListe != NULL) {                  ///vérifier si quelqu'un porte le même nom
            if (strcmp(current->nom, string) == 0) {
                perror("Nom existe déjà");
                exit(EXIT_FAILURE);
            }
            current = currentListe->no;
            currentListe = currentListe->succ;

        }
        if (strcmp(current->nom, string) == 0) {
            perror("Nom existe déjà");
            exit(EXIT_FAILURE);
        }
        liste_noeud* ln= malloc(sizeof(liste_noeud));
        ln->no=newNoeud;
        ln->succ=NULL;

        currentListe = l->fils;
        while(currentListe->succ!=NULL){
            currentListe=currentListe->succ;
        }
        currentListe->succ=ln;
    }
    return newNoeud;
}

///Creation du racine
noeud* creationRacine(){
    noeud* newRacine= malloc(sizeof(noeud));
    newRacine->est_dossier=true;
    for(int i=0;i<100;++i) newRacine->nom[i]=' ';
    newRacine->pere=newRacine;
    newRacine->racine=newRacine;
    newRacine->fils=NULL;
    return newRacine;
}


///Creation d'un noeud Dossier
noeud* creationNoeudDossier(noeud* n,char* name){
    noeud* newNoeud= malloc(sizeof(noeud));
    newNoeud->est_dossier=true;
    for(int i=0;i<100;++i) newNoeud->nom[i]=*(name+i);
    newNoeud->pere=n;
    newNoeud->racine=noeudracine;
    newNoeud->fils=NULL;
    return newNoeud;
}



