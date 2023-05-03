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
    noeud *actuelle=noeudcurseur;
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

}