#include "liste.h"

///Affichage du nom
void affiche(char nom[100]){
    for(int i=0;i< strlen(nom);++i){
        if(*(nom+i)!=' ') {
            printf("%c", *(nom + i));
        }
    }
    printf(" ");
}

///Commande ls
void commande_ls(noeud* l){
    if(l->fils!=NULL){
        noeud* current=l->fils->no;
        liste_noeud* currentListe=l->fils->succ;
        while(currentListe!=NULL){
            affiche(current->nom);
            printf(" ");
            current=currentListe->no;
            currentListe=currentListe->succ;
        }
        affiche(current->nom);
        printf("\n");
    }else{
        printf(" ");
    }

}






