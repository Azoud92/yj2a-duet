#include "liste.h"
#include <ctype.h>

void remove_fichier(noeud* n){
    liste_noeud* ln=n->pere->fils;
    if(ln->no==n&&ln->succ==NULL){
        n->pere->fils=NULL;
        free(n);
        return;
    }if(ln->no==n&&ln->succ!=NULL){
        n->pere->fils=ln->succ;
        free(n);
        return;
    }
    liste_noeud* previous=n->pere->fils;
    ln=ln->succ;
    while(ln->succ!=NULL){
        if(ln->no==n){
            previous->succ=ln->succ;
            free(n);
            return;
        }
        previous=previous->succ;
        ln=ln->succ;
    }
    previous->succ=NULL;
    free(n);
}
void remove_dossier(noeud* n){
    if(n->fils==NULL){
        remove_fichier(n);
    }else{
        liste_noeud* current = n->fils;
        while (current != NULL) {
            remove_dossier(current->no);
            liste_noeud* inter = current;
            current = current->succ;
            free(inter);
        }
        remove_fichier(n);
    }
}
void commande_remove(noeud* n){
    if(n->est_dossier) remove_dossier(n);
    if(!n->est_dossier) remove_fichier(n);
}
void test(noeud* n){
    if(n->fils->succ!=NULL) printf("1");

}

