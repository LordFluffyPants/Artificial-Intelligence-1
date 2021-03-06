package Problem6.src.Puzzle;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Created by John Wesley Hayhurst & Austin Schaffer
 */
public class State {
    private final int[] GOAL = {1,2,3,4,5,6,0,7,8};
    private final int cost = 1;
    private int[] currentBoard;

    private int manhattanDistance = 0;

    public State(int[] board)
    {
        currentBoard = board;
        setManhattanDistance();
//        setFaultyManhattanDistance();
    }

    public int getCost()
    {
        return cost;
    }

    public boolean isGoal()
    {
        for (int i = 0; i < GOAL.length; i++)
        {
            if (currentBoard[i] != GOAL[i]) return false;
        }
        return true;
    }

    public int getEmpty()
    {
        int emptySpace = -1;
        for (int i = 0; i < currentBoard.length; i++)
        {
            if (currentBoard[i] == 0) return i;
        }
        return emptySpace;
    }

    private int[] copyBoard()
    {
        int[] copy = new int[9];
        for (int i = 0; i < copy.length; i++)
        {
            copy[i] = currentBoard[i];
        }
        return copy;
    }

    public void swap(int holePosition, int swapPosition, ArrayList<State> successor)
    {
        int[] temp = copyBoard();
        int swapValue = temp[swapPosition];
        temp[swapPosition] = 0;
        temp[holePosition] = swapValue;
        successor.add(new State(temp));
    }

    public ArrayList<State> generateSuccessors()
    {
        ArrayList<State> successors = new ArrayList<State>();
        int hole = getEmpty();
        //Move Up
        if (hole != 0 && hole != 1 && hole != 2 )
        {
            swap(hole,hole-3,successors);
        }
        //Move Down
        if (hole != 6 && hole != 7 && hole != 8)
        {
            swap(hole, hole+3, successors);
        }
        //Move Left
        if (hole != 0 && hole != 3 && hole != 6)
        {
            swap(hole, hole-1, successors);
        }
        //Move Right
        if (hole != 2 && hole != 5 && hole != 8)
        {
            swap(hole,hole+1, successors);
        }
        return successors;
    }

    public int getManhattanDistance()
    {
        return manhattanDistance;
    }

    public void setManhattanDistance()
    {
        int index = -1;

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                index++;
                int value = (currentBoard[index] -1);

                if (!((value == 7 && index == 8) ||(value == 6 && index == 7)))
                {
                    if (value != -1)
                    {
                        int horizontal = value % 3;
                        int vertical = value /3;

                        manhattanDistance += Math.abs(vertical - y) + Math.abs(horizontal -x);
                    }
                }

            }
        }
    }

    public void setFaultyManhattanDistance()
    {
        int index = -1;

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                index++;
                int value = (currentBoard[index] -1);
                if (Math.round(Math.random()) == 1)
                {
                    System.out.println("Added extra value to random manhattan");
                    manhattanDistance += 1;
                }
                if (!((value == 7 && index == 8) ||(value == 6 && index == 7)))
                {
                    if (value != -1)
                    {
                        int horizontal = value % 3;
                        int vertical = value /3;

                        manhattanDistance += Math.abs(vertical - y) + Math.abs(horizontal -x);
                    }
                }

            }
        }
    }

    public int[] getCurrentBoard()
    {
        return currentBoard;
    }

    public boolean equals(State state)
    {
        if (Arrays.equals(currentBoard, state.getCurrentBoard()))
        {
            return true;
        }
        else
            return false;

    }

}
