#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

//&i is the memory address i occupies

struct Person{
    char* firstName;
    char* lastName;
    char* phone;
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

void deepCopyStruct(struct Person* s, struct Person* p){
    //struct Person* s = malloc(sizeof *s);
    //struct Person* s;
    /*s->firstName = malloc(sizeof(p->firstName)+1);
    s->lastName = malloc(sizeof(p->lastName)+1);
    s->emailAddress = malloc(sizeof(s->emailAddress)+1);
    s->phone = malloc(sizeof(s->phone)+1);*/
    s->firstName = malloc(80 * sizeof(s->firstName[0]));
    s->lastName = malloc(80 * sizeof(s->firstName[0]));
    s->emailAddress = malloc(80 * sizeof(s->firstName[0]));
    s->phone = malloc(15* sizeof(s->phone[0]));
    strcpy(s->firstName,p->firstName);
    strcpy(s->lastName,p->lastName);
    strcpy(s->emailAddress,p->emailAddress);
    strcpy(s->phone,p->phone);
}

void sortFirstName(struct Person** pp, int count){
    printf("in sort firstname!\n");
    printf("count: %d\n",count);
    for(i = 0; i < count; i++){
        for(j = 0; j < count; j++){
            printf("i:%s, j:%s\n",pp[i]->firstName,pp[j]->firstName);
            if(strcmp(pp[i]->firstName , pp[j]->firstName) > 0){ //bad string comparison
                printf("swapping!\n");
                struct Person *temp = pp[i];
                pp[i] = pp[j];
                pp[j] = temp;
            }
        }
    }                
}

int findFirstName(struct Person** ss, char* s, int count){
    printf("%d < %d... i < count\n", i, count);
    for(i = 0; i < count; i++){
        if(!strcmp(ss[i]->firstName,s)){ //can probably just do if(!strcmp)
            printf("Found it!\n");
            return 1;
        }  
        printf("Couldn't find it! %s != %s\n",s,ss[i]->firstName); 
    } 
    return 0;
}

void sortLastName(struct Person** ss, int count){
    printf("in sort lastName!\n");
    for(i = 0; i < count; i++){
        for(j = 0; j < count; j++){
            if(strcmp(ss[i]->lastName , ss[j]->lastName) > 0){
                struct Person *s = ss[i]; //is this copying the value or the memory location?
                ss[i] = ss[j]; 
                ss[j] = s;
            }   
        }
    }             
}

int findLastName(struct Person** ss, char* s, int count){ //named incorrectly, is redeclaring ffn, should be find last name
    for(i = 0; i < count; i++){
        if (!strcmp(ss[i]->lastName,s)) return 1;
    }
    return 0;
}

void sortEmail(struct Person** ss, int count){
    printf("in sort Email!\n");
    for(i = 0; i < count; i++){
        for (j = 0; j < count; j++){
            if(strcmp(ss[i]->emailAddress , ss[j]->emailAddress) > 0) {
                struct Person *s = ss[i];
                ss[i] = ss[j];
                ss[j] = s;
            }
        }
    }
}

int findEmail(struct Person** ss, char* s, int count){
    for(i = 0; i < count; i++){
        if(!strcmp(ss[i]->emailAddress,s)) return 1;
    }
    return 0;
}

void sortPhoneNumber(struct Person** ss, int count){
    printf("in SortPhoneNumber!\n");
    for(i = 0; i < count; i++){
        for(j = 0; j < count; j++){
            if(strcmp(ss[i]->phone , ss[j]->phone) > 0){
                struct Person *s = ss[i];
                ss[i] = ss[j];
                ss[j] = s;
            }
        }
    }
}

int findPhoneNumber(struct Person** ss, char* s, int count){
    for(i = 0; i < count; i++){
        if(!strcmp(ss[i]->phone,s)) return 1;
    }
    return 0;
}


int main(int argc, char** argv){
    int i;
    int count = 0;
    char buffer[10];
    char strBuffer[80];


    struct Person** ss = (struct Person**) malloc (100*sizeof(struct Person**)); //maybe too large for an initial assignment? maybe assign small and reassign as needed?
    struct Person* s = malloc(sizeof(*s));
    s->firstName = malloc(80 * sizeof(s->firstName[0]));
    s->lastName = malloc(80 * sizeof(s->firstName[0]));
    s->emailAddress = malloc(80 * sizeof(s->firstName[0]));
    s->phone = malloc(15* sizeof(s->phone[0]));
    char* val = malloc(100*sizeof(val[0]));

    printf("Number of arguments recieved: %d\n",argc);
    if(argc != 2){
        printf("Incorrect number of arguments supplied! Got %d\n",argc);
    }
    FILE* f = fopen(argv[1],"r");
    if(f == NULL){
        printf("the file %s could not be opened!\n",argv[1]);
        exit(1);
    }else{
        printf("File isn't null!\n");
    }

    for(i = 0; i < 50; i++){
        

        //fscanf is depreciated... use fscanf_s instead...
        if(fscanf(f, "%79s %79s %14s %79s", s->firstName,s->lastName, s->phone, s->emailAddress) != 4){
            printf("No more data to read!\n");
            /*free(s->firstName);
            free(s->lastName);
            free(s->emailAddress);
            free(s->phone);
            free(s);
            free(ss);
            exit(0);*/
            break;
        }
        printf("%s %s %s %s\n",s->firstName,s->lastName, s->phone, s->emailAddress);

        ss[count] = malloc(sizeof(*s));
        deepCopyStruct(ss[count],s);
        count+=1;

        char *ptr;

        printf("in for loop %d! count = %d\n",i,count);
        int command = 10;
        //int foundResult = 0;
        while(command != 0){ //may be a dangerous loop...
            
            fgets(buffer,sizeof buffer,stdin);
            command = atoi(buffer);
            if(command != 0){
                fgets(strBuffer,sizeof strBuffer,stdin);
                fixfgetsInput(strBuffer,sizeof strBuffer);
                strcpy(val,strBuffer);
                switch(command){
                    case 1:
                        printf("looking for email %s\n",val);
                        sortEmail(ss, count);
                        printf("found it? %d\n",findEmail(ss,val,count));
                        break;
                    case 2:
                        printf("looking for firstname %s\n",val);
                        sortFirstName(ss, count);
                        printf("found it? %d\n",findFirstName(ss,val,count));
                        break;
                    case 3:
                        printf("looking for lastname %s\n",val);
                        sortLastName(ss, count);
                        printf("found it? %d\n",findLastName(ss,val,count));
                        break;
                    case 4:
                        printf("looking for phone number %s\n",val);
                        sortPhoneNumber(ss, count);
                        printf("found it? %d\n",findPhoneNumber(ss,val,count));
                        break;
                    default:
                        printf("Please enter a valid option!\n");
                        break;
                }
            }
            
        }

    }
    printf("Stopped reading, reached the 50 person limit!\n");
    for(i = 0; i < count; i++){
        printf("Freeing!\n");
        free(ss[i]->firstName);
        free(ss[i]->lastName);
        free(ss[i]->emailAddress);
        free(ss[i]->phone);
        free(ss[i]);
    }
    free(ss);
    free(s->firstName);
    free(s->lastName);
    free(s->emailAddress);
    free(s->phone);
    free(s);
    free(val);
    return 0;

}
