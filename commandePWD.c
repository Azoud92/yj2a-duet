#include "liste.h"
#include <ctype.h>

int profondeurNoeud(){
    noeud *actuelle=noeudcurseur;
    int profondeur=0;
    while(actuelle->pere!=actuelle->racine){
        ++profondeur;
        actuelle=actuelle->pere;
    }
    return profondeur;
}
//Commande PWD
void commande_pwd(){
    char* string[profondeurNoeud(noeudcurseur)];
    // On créer un nouveau noeud car pwd ne change pas la position du noeud actuelle
    noeud *actuelle=n;
    int i=0;
    while(actuelle->pere!=actuelle->racine){
        string[i]=actuelle->nom;
        i++;
        actuelle=actuelle->pere;
    }
    for(int j=i-1;j>=0;--j){
        printf("/%s", string[j]);
    }
}
int main(){
    noeud *racine=creationRacine();
    noeud *n1= creationNoeud(racine,"n1");
    noeud *n2= creationNoeud(n1,"n2");
    noeud *n3= creationNoeud(n2,"n3");
    noeud *n4= creationNoeud(n2,"n4");
}