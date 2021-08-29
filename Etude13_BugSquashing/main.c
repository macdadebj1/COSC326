#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

struct S{
    char* firstName;
    char* lastName; // inconsistent * placement.
    int phone;
    char* emailAddress;
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

int findFirstName(struct S** ss, char* s){
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

int findLastName(struct S** ss, char* s){ //named incorrectly, is redeclaring ffn, should be find last name
    while(++i < count) {
        if (ss[i]->lastName == s)
            return 1;
    }
    return 0;
}

void sem(struct S** ss){
    for(i = 0; i < count; i++){
        for (j = 0; j < count; j++){
            if(ss[i]->emailAddress > ss[j]->emailAddress) { //typo
                struct S *s = ss[i];
                ss[j] = ss[i]; //will just reasign ss[j] twice... not what we want...
                ss[j] = s;
            }
        }
    }
}

int findEmail(struct S** ss,char* s){
    while(++i < count){
        if(ss[i]->emailAddress == s)
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

int findPhoneNumber(struct S** ss, int s){
    while(++i < count){
        if(ss[i]->phone == s) //changed so is comparing correctly
            return 1;
    }
    return 0;
}

int main(int argc, char** argv){ //inconsistent ** placement...
    int i;
    int count = 0;
    char buffer[10]; //incorrect declaration..? maybe?

    struct S** ss = (struct S**) malloc (100*sizeof(struct S**));
    struct S* s = malloc(sizeof(*s));

    printf("Number of arguments recieved: %d\n",argc);
    if(argc != 2){
        printf("Incorrect number of arguments supplied! Got %d\n",argc);
    }
    FILE* f = fopen(argv[1],"r"); // no error checking
    if(f == NULL){
        printf("\n the file %s could not be opened!",argv[1]);
        exit(1);
    }else{
        printf("File isn't null!");
    }

    for(i = 0; i < 50; i++){
        s->firstName = (char*) malloc(80 * sizeof(s->firstName[0]));
        s->lastName = (char*) malloc(80 * sizeof(s->firstName[0])); //May go out of bounds...
        s->emailAddress = (char*) malloc(80 * sizeof(s->firstName[0])); //May go out of bounds...

        //fscanf is depreciated... use fscanf_s instead...
        fscanf(f, "%79s %79s %d %79s", s->firstName,s->lastName, &s->phone,s->emailAddress);

        ss[count] = s;
        count+=1;

        printf("\nin for loop!");
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
                    printf("found it? %d\n",findEmail(ss,val));
                    break;
                case 2:
                    printf("looking for firstname %s\n",val);
                    sfn(ss);
                    printf("found it? %d\n",findFirstName(ss,val));
                    break;
                case 3:
                    printf("looking for lastname %s\n",val);
                    sln(ss);
                    printf("found it? %d\n",findLastName(ss,val));
                    break;
                case 4:
                    printf("looking for email %s\n",val); //should be looking for ph number.
                    sph(ss);
                    printf("found it? %d\n",findPhoneNumber(ss,atoi(val)));
                default:
                    break;
            }
        }

    }

}
