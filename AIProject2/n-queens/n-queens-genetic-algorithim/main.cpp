
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <iostream>
#define SIZE 7
#define POP 7

int answers[SIZE] = {0,0,0,0,0,0,0};


int getRand(int mod){
    if (mod==0) return 0;
    else return random()%mod;
}

void printArray(int array[]){
    int i;
    for(i=0; i<SIZE-1; i++) printf("(%i,%i),",i,array[i]);
    printf("(%i,%i)",SIZE-1,array[SIZE-1]);
    printf("\n");
}

int getWeight(int array[]){
    int weight = 28;
    int queen;
    for(queen=0;queen<SIZE;queen++){    //for each queen
        int nextqueen;
        for(nextqueen=queen+1; nextqueen<SIZE; nextqueen++){        //for each of the other queens (nextqueen = queen to avoid counting pairs twice)
            if(array[queen] == array[nextqueen] || abs(queen-nextqueen)==abs(array[queen]-array[nextqueen])){   //if conflict
                weight--;
            }
        }
    }
    return weight;
}

void geneticAlgorithm(){
    int population[POP][SIZE];
    int children[POP][SIZE];
    int weightProb[224] = {};
    int wpl = 0; //weightProb[] length
    float mutProb = 0.2; //higher prob yields faster times. works decently anyways.
    int done = 0;
    int i;
    for(i=0;i<POP;i++)
        for(int j=0;j<SIZE;j++)
            population[i][j] = answers[j];

    int count = 0;
    printf("Starting with array: ");
    printArray(population[0]);
    while(done == 0){
        for(i=0;i<POP;i++){
            if(getWeight(children[i]) == 28){
                printf("solution: ");
                printArray(children[i]);
                printf("%i",count);
                done = 1;
            }
        }

        for(i=0;i<wpl;i++)
            weightProb[i] = (int)NULL; //clear weightprob

        wpl=0;

        //weighted probability distribution
        for(i=0;i<POP;i++){
            int w = getWeight(population[i]);
            for(int j=0;j<w;j++){
                weightProb[wpl] = i; //fill array with member number w times
                wpl++;
            }
        }

        //reproduce
        for(i=0;i<POP;i+=2){
            int par1 = weightProb[getRand(wpl)];
            int par2 = weightProb[getRand(wpl)];
            int split = getRand(SIZE);
            //crossover
            for(int j=0;j<split;j++){
                children[i][j] = population[par1][j];
                children[i+1][j] = population[par2][j];
            }
            for(int j=split;j<SIZE;j++){
                children[i][j] = population[par2][j];
                children[i+1][j] = population[par1][j];
            }
            //mutation
            if(getRand(1000000)<=mutProb*1000000){
                int child=getRand(2);
                if(child == 0) children[i][getRand(SIZE)] = getRand(SIZE);
                else children[i+1][getRand(SIZE)] = getRand(SIZE);
            }
            count++;
        }

        for(i=0;i<POP;i++)
            for(int j=0;j<SIZE;j++)
                population[i][j] = children[i][j];
        wpl = 0;

    }

}

int main(int argc, const char * argv[]){
    srandom((unsigned int)time(NULL));  //seed random
    int i;
    /**
     * Generates all queens on same row
     */
//    for (i=0;i<SIZE;i++) answers[i] = 0;

    /**
     * Generates all queens on the diagonal
     */
//    for(i=0;i<SIZE;i++) answers[i] = i;

    /**
     * Generates all according to the mixed up scenario
     */
    answers[0] = 0;
    answers[1] = 6;
    answers[2] = 1;
    answers[3] = 5;
    answers[4] = 2;
    answers[5] = 4;
    answers[6] = 3;
    geneticAlgorithm();
    return 0;
}
