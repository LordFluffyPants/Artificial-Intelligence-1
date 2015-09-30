
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>

#define e 2.718281828459045 //double
#define SIZE 7

int answers[SIZE] = {};
//answers[row] = column;

int getRandOne(){
    return (random()%1000)/1000;
}

void printArray(int array[]){
    int i;
    for(i=0; i<SIZE-1; i++) printf("(%i,%i),",i,array[i]);
    printf("(%i,%i)",SIZE-1,array[SIZE-1]);
    printf("\n");
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

void simulatedAnnealing(){
    int temp = 4000;
    int sch = .99;
    int k;
    int dw;
    int count = 0;
    int successor[SIZE];

    for(k=0;k<30000;k++){  //roughly even distribution of "took to long" and solution with this k value 170000

        temp *= sch;
        int i;
        for(i=0;i<SIZE;i++)
            successor[i] = answers[i];

        count++;
        successor[random()%SIZE] = random()%SIZE; //generating new successor
        dw = getWeight(successor) - getWeight(answers);

        if(dw > 0 || getRandOne() < pow(e,-dw*temp)){

            for(i=0;i<SIZE;i++)
                answers[i] = successor[i];
        }
        if(getWeight(answers) == 0){
            printf("solution: ");
            printArray(answers);
            printf("%i",count);
            exit(0);
        }


    }
    printf("took too long");
}

int main(int argc, const char * argv[])
{
    srandom((unsigned int)time(NULL));
    int i;
    /**
     * Generates the array with all queens in the same row
     */
//    for(i=0;i<SIZE;i++) answers[i] = 0;

    /**
     * Generates the array with all queens in the diagonal
     */
//    for(i=0;i<SIZE;i++) answers[i] = i;

    /**
     * Generates the array with all queens mixed up
     */
    answers[0] = 0;
    answers[1] = 6;
    answers[2] = 1;
    answers[3] = 5;
    answers[4] = 2;
    answers[5] = 4;
    answers[6] = 3;

    printf("Starting with array: ");
    printArray(answers);
    simulatedAnnealing();
    return 0;
}