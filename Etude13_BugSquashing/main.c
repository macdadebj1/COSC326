#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

//&i is the memory address i occupies

struct Person{
    char* firstName;
    char* lastName;
    long phone;
    char* emailAddress;
};

static int i, j;
static int count;

void fixfgetsInput(char *string,int length)
{
    int x;
    for(x=0;x<=length;x++)
    {
        if( string[x] == '\n')
        {
            string[x] = '\0';
            break;
        }
    }
}

void sortFirstName(struct Person** pp){
    for(i = 0; i < count; i++){
        for(j = 0; j <count; j++){
            if(pp[i]->firstName > pp[j]->firstName){
                struct Person *temp = pp[i];
                pp[i] = pp[j];
                pp[j] = temp;
            }
        }
    }                
}

int findFirstName(struct Person** ss, char* s, int localCount){
    printf("%d < %d... i < count\n",i,localCount);
    while(i < localCount){ //what if i is used else where? may not be 0...
        if(!strcmp(ss[i]->firstName,s)){ //can probably just do if(!strcmp)
            printf("Found it!\n");
            return 1;
        }  
        printf("Couldn't find it! %s != %s\n",s,ss[i]->firstName);  
        i++;
    }
    
    
    return 0;
}

void sortLastName(struct Person** ss){
    for(i = 0; i < count; i++){
        for(j = 0; j < count; j++){
            if(ss[i]->lastName > ss[j]->lastName){
                struct Person *s = ss[i]; //is this copying the value or the memory location?
                ss[i] = ss[j]; 
                ss[j] = s;
            }   
        }
    }             
}

int findLastName(struct Person** ss, char* s, int count){ //named incorrectly, is redeclaring ffn, should be find last name
    while(i < count) {
        if (!strcmp(ss[i]->lastName,s)) return 1;
        i++;
    }
    return 0;
}

void sortEmail(struct Person** ss){
    for(i = 0; i < count; i++){
        for (j = 0; j < count; j++){
            if(ss[i]->emailAddress > ss[j]->emailAddress) { //typo
                struct Person *s = ss[i];
                ss[i] = ss[j];
                ss[j] = s;
            }
        }
    }
}

int findEmail(struct Person** ss, char* s, int count){
    while(i < count){
        if(!strcmp(ss[i]->emailAddress,s)) return 1;
        i++;
    }
    return 0;
}

void sortPhoneNumber(struct Person** ss){
    for(; i<count;i++){
        for(; j<count;j++){
            if(ss[i]->phone > ss[j]->phone){
                struct Person *s = ss[i]; //Does this need to be struct Person **s?
                ss[i] = ss[j];
                ss[j] = s;
            }
        }
    }
}

int findPhoneNumber(struct Person** ss, long s, int count){
    while(i < count){
        if(!(ss[i]->phone - s)) return 1;
        i++;
    }
    return 0;
}

int main(int argc, char** argv){
    int i;
    int count = 0;
    char buffer[10];
    char strBuffer[80];


    struct Person** ss = (struct Person**) malloc (100*sizeof(struct Person**));
    struct Person* s = malloc(sizeof(*s));

    printf("Number of arguments recieved: %d\n",argc);
    if(argc != 2){
        printf("Incorrect number of arguments supplied! Got %d\n",argc);
    }
    FILE* f = fopen(argv[1],"r"); // no error checking
    if(f == NULL){
        printf("the file %s could not be opened!\n",argv[1]);
        exit(1);
    }else{
        printf("File isn't null!\n");
    }

    for(i = 0; i < 50; i++){
        s->firstName = (char*) malloc(80 * sizeof(s->firstName[0]));
        s->lastName = (char*) malloc(80 * sizeof(s->firstName[0])); //May go out of bounds...
        s->emailAddress = (char*) malloc(80 * sizeof(s->firstName[0])); //May go out of bounds...

        //fscanf is depreciated... use fscanf_s instead...
        fscanf(f, "%79s %79s %ld %79s", s->firstName,s->lastName, &s->phone, s->emailAddress);
        printf("%s %s %ld %s\n",s->firstName,s->lastName, s->phone,s->emailAddress);

        ss[count] = s; //should maybe = &s?
        count+=1;

        char *ptr;

        printf("in for loop %d! count = %d\n",i,count);
        int command = 10;
        //int foundResult = 0;
        while(command != 0){ //may be a dangerous loop...
            char* val = malloc(100*sizeof(val[0]));
            fgets(buffer,sizeof buffer,stdin);
             //gets is INSECURE!
            command = atoi(buffer);
            fgets(strBuffer,sizeof strBuffer,stdin);
            fixfgetsInput(strBuffer,sizeof strBuffer);
            strcpy(val,strBuffer);
            switch(command){
                case 1:
                    printf("looking for email %s\n",val);
                    sortEmail(ss);
                    printf("found it? %d\n",findEmail(ss,val,count));
                    break;
                case 2:
                    printf("looking for firstname %s\n",val);
                    sortFirstName(ss);
                    printf("found it? %d\n",findFirstName(ss,val,count));
                    break;
                case 3:
                    printf("looking for lastname %s\n",val);
                    sortLastName(ss);
                    printf("found it? %d\n",findLastName(ss,val,count));
                    break;
                case 4:
                    printf("looking for phone number %ld\n",strtol(val,&ptr,11));
                    sortPhoneNumber(ss);
                    printf("found it? %d\n",findPhoneNumber(ss,strtol(val,NULL,sizeof val),count));
                default:
                    printf("Please enter a valid option!\n");
                    break;
            }
        }

    }

}
