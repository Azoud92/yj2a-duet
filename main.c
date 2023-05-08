#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include "liste.h"
#include <ctype.h>


int main(){
    int main(){
        char ch[250];
        FILE *flux=fopen("fichiertest.txt","r");
        if(flux==NULL){
            perror("Probleme ouverture de fichier");
            exit(EXIT_FAILURE);
        }
        while(fgets(ch,50,flux)!=NULL){
            int k=0;
            char *s[3];
            char *temp=malloc((strlen(ch)+1)*sizeof(char));
            assert(temp!=NULL); strcpy(temp,ch);
            char *lex=strtok(temp," ");
            while(lex!=NULL){
                for(int i=0;i< strlen(lex);i++){
                    if(*(lex+i)!='\r' && *(lex+i)!='\n'){
                        *(s[k]+i)=*(lex+i);
                    }
                }
                k++;
                lex=strtok(NULL," ");
            }
            if(strcmp("mkdir",s[0])==0){
                ///on appelle la fonction mkdir avec s[1]
            }

            ///on appelle la fonction ls

        }
        int r=fclose(flux);
        if(r!=0){ perror("Probleme fermeture de fichier");
        }

    }

}