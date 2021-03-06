#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#define SIZE 7

int answers[SIZE] = {};
//answers[row] = col;
int count = 0;

int length(int array[]){
    int i=0;
    while(array[i] != -1) i++;
    return i;
}

int getRand(int mod){
    return random() % mod;
}

void printArray(int array[]){
    int i;
    for(i=0; i<SIZE-1; i++) printf("(%i,%i),",i,array[i]);
    printf("(%i,%i)",SIZE-1,array[SIZE-1]);
    printf("\n");
}

int isValid(int array[], int curRow, int curCol){
    int i;
    if(length(array)){
        for(i=0;i<length(array);i++) if(i==curRow || array[i]==curCol || abs(i-curRow)==abs(array[i]-curCol)) return 0;
        return 1;
    } else return 1;
    return 0;
}

int getWeight(int array[]){
    int weight = 0;
    int queen;
    for(queen=0;queen<SIZE;queen++){    //for each queen
        int nextqueen;
        for(nextqueen=queen+1;nextqueen<SIZE;nextqueen++){        //for each of the other queens (nextqueen = queen to avoid counting pairs twice)
            if(array[queen] == array[nextqueen] || abs(queen-nextqueen)==abs(array[queen]-array[nextqueen])){   //if conflict
                weight++;
            }
        }
    }
    return weight;
}

void hillClimbing(int array[]){
    int weight = getWeight(array);
    if (weight == 0){
        printf("\n\nsolution: ");
        printArray(array);
        printf("count: %i\n",count);
        exit(0);
    } else {
        int nextrow[] = {-1};
        int nextcol[] = {-1};
        int nextweight = weight;
        int queen;
        for(queen=0;queen<SIZE;queen++){                            //for each queen/row            -|
            int origcol = array[queen];                             //save the original column       |
            int validcol;                                           //                               |--- searching the whole board
            for(validcol = 0; validcol<SIZE;validcol++){            //for each valid column          |
                if(validcol != origcol){                            //not including the current one -|
                    array[queen] = validcol;                        //place the queen in the next column
                    int newweight = getWeight(array);               //get the weight of the modified board
                    if(newweight < nextweight){                    //if it's a better move
                        int i;
                        for(i=0;i<length(nextrow);i++){            //clear the boards
                            nextrow[i] = (int)NULL;
                            nextcol[i] = (int)NULL;
                        }
                        nextrow[0] = queen;
                        nextrow[1] = -1;                //end of array char
                        nextcol[0] = validcol;
                        nextcol[1] = -1;
                        nextweight = newweight;
                        count++;
                        hillClimbing(array);
                    } else if (newweight == nextweight){
                        int len = length(nextrow);
                        nextrow[len] = queen;
                        nextrow[len+1] = -1;
                        nextcol[len] = validcol;
                        nextcol[len+1] = -1;
                    }

                }
            }
            array[queen] = origcol;
        }
        //once we're done searching the board
        if (nextcol[0] != -1 && nextrow[0] != -1){          //if we've found a better move
            int i;
            for(i=0;i<length(nextrow);i++){                 //for each nextmove that will yield a better weight
                array[nextrow[i]] = nextcol[i];             //make it
                count++;                                    //increase the count
                printf("count: %i\n",count);
                hillClimbing(array);                        //recurse
                count--;                                    //decrease the count
            }
        } else {
            printf("local max reached. retrying\n");
            int i;
            for(i=0;i<SIZE;i++) answers[i] = getRand(SIZE);
            printArray(answers);
            hillClimbing(answers);
        }
    }
}

int main(int argc, const char * argv[]){
    srandom((unsigned int)time(NULL));  //seed random
    int i;
    /**
     * Generates all queens on same row
     */
    for (i=0;i<SIZE;i++) answers[i] = 0;

     /**
      * Generates all queens on the diagonal
      */
//    for(i=0;i<SIZE;i++) answers[i] = i;

    /**
     * Generates all according to the mixed up scenario
     */
//    answers[0] = 0;
//    answers[1] = 6;
//    answers[2] = 1;
//    answers[3] = 5;
//    answers[4] = 2;
//    answers[5] = 4;
//    answers[6] = 3;

    printf("running hill climbing with array: ");
    printArray(answers);
    hillClimbing(answers);
    return 0;
}