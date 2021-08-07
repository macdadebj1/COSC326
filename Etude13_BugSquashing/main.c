#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

struct S{
    char *firstName;
    char* lastName; // inconsistent * placement.
    int phone;
    char* emialAddress; //bad spelling
};

static int i, j;
static int count;

void sfn(struct S** ss){
    for(i = 0; i < count; i++)
        for(j = 0; j <count; j++)
            if(ss[i]->firstName > ss[j]->firstName)
                ss[i] = ss[j]; //will cause a bug... just copying back and forward...
    ss[j] = ss[i];
}

int ffn(struct S** ss, char* s){
    while(++i < count)
        if(ss[i]->firstName == s)  //Add in curly braces, make reading easier...
            return 1;
    return 0;
}

void sln(struct S** ss){
    for(i = 0; i < count; i++)
        for(j = 0; j < count; j++)
            if(ss[i]->lastName > ss[j]->lastName)
                ss[i] = ss[j]; //will cause a bug... just copying back and forward...
    ss[j] = ss[i];
}

int ffn(struct S** ss, char* s){
    while(++i < count) {
        if (ss[i]->lastName == s)
            return 1;
    }
    return 0;
}

void sem(struct S** ss){
    for(i = 0; i < count; i++){
        for (j = 0; j < count; j++){
            if(ss[i]->emialAddress > ss[j]->emialAddress) { //typo
                struct S *s = ss[i];
                ss[j] = ss[i]; //will just reasign ss[j] twice... not what we want...
                ss[j] = s;
            }
        }
    }
}

int fem(struct S** ss,char* s){
    while(++i < count){
        if(ss[i]->emialAddress == s)
            return 1;
    }
    return 0;
}

void sph(struct S** ss){
    for(; i<count;i++){
        for(; j<count;j++){
            if(ss[i]->phone > ss[j]->phone){
                struct S *s = ss[i]; //this is more like how we want to do it...
                ss[i] = ss[j];
                ss[j] = s;
            }
        }
    }
}

int fph(struct S** ss, int s){
    while(++i < count){
        if(ss[i]->phone == ss)
            return 1;
    }
    return 0;
}

int main(int argc, char ** argv){ //inconsistent ** placement...
    int i;
    int count = 0;
    char buffer[10]; //incorrect declaration..? maybe?

    struct S** ss = (struct S**) malloc (100*sizeof(struct S**));
    struct S* s = malloc(sizeof(*s));

    FILE *f = fopen(argv[1],"r");

    for(i = 0; i < 50; i++){
        s->firstName = (char*) malloc(80 * sizeof(s->firstName[0]));
        s->lastName = (char*) malloc(80 * sizeof(s->firstName[0])); //May go out of bounds...
        s->emialAddress = (char*) malloc(80 * sizeof(s->firstName[0])); //May go out of bounds...

        //fscanf is depreciated... use fscanf_s instead...
        fscanf(f, "%s %s %d %s", &s->firstName,&s->lastName, &s->phone,&s->emialAddress);

        ss[count] = s;
        count+=1;
        {
            int command = 10;
            while(command != 0){ //may be a dangerous loop...
                char* val = malloc(100*sizeof(val[0]));
                gets(buffer); //gets is INSECURE!
                command = atoi(buffer);
                gets(buffer);
                strcpy(val,buffer);
                switch(command){
                    case 1:
                        printf("looking for email %s\n",val);
                        sem(ss);
                        printf("found it? %d\n",fem(ss,val));
                        break;
                    case 2:
                        printf("looking for firstname %s\n",val);
                        sfn(ss);
                        printf("found it? %d\n",ffn(ss,val));
                        break;
                    case 3:
                        printf("looking for lastname %s\n",val);
                        sln(ss);
                        printf("found it? %d\n",fln(ss,val));
                        break;
                    case 4:
                        printf("looking for email %s\n",val); //should be looking for ph number.
                        sph(ss)
                        printf("found it? %d\n",fph(ss,atoi(val)));
                    default:
                        break;
                }
            }
        }

    }

}
