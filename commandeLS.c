#include "liste.h"

///Affichage du nom
void affiche(char nom[100]){
    int lastIndice;
    for(int i=99;i>=0;--i){
        if(*(nom+i)!=' ') lastIndice=i;
    }
    for(int i=0;i<=lastIndice;++i){
        printf("%c",*(nom+i));
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






