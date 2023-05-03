#include "liste.h"
#include <ctype.h>

///Commande mkdir
void commande_mkdir(noeud* l,char* string){
    if(strlen(string)>99){                 ///vérifier si une chaine est de longueur moins que 100
        perror("Très longue chaîne");
        exit(EXIT_FAILURE);
    }
    for(int i=0;i< strlen(string);++i){   ///vérifier si tout les caractères sont alpha-numerique
        if(isalnum(*(string+i))==0){}
        perror("Format incorrecte");
        exit(EXIT_FAILURE);
    }
    if(l->fils==NULL){
        noeud* newNoeud= creationNoeud(l,string);
        l->fils->no=newNoeud;
    }else{
        noeud* current=l->fils->no;
        liste_noeud* currentListe=l->fils->succ;
        while(currentListe!=NULL){                  ///vérifier si quelqu'un porte le même nom
            if(strcmp(current->nom,string)==0){
                perror("Nom existe déjà");
                exit(EXIT_FAILURE);
            }
            current=currentListe->no;
            currentListe=currentListe->succ;
        }
        if(strcmp(current->nom,string)==0){
            perror("Nom existe déjà");
            exit(EXIT_FAILURE);
        }
        noeud* newNoeud= creationNoeud(l,string);
        currentListe->no=newNoeud;
    }

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


///Creation d'un noeud
noeud* creationNoeud(noeud* n,char* name){
    noeud* newNoeud= malloc(sizeof(noeud));
    newNoeud->est_dossier=true;
    for(int i=0;i<100;++i) newNoeud->nom[i]=*(name+i);
    newNoeud->pere=n;
    newNoeud->racine=noeudracine;
    newNoeud->fils=NULL;
    return newNoeud;
}



