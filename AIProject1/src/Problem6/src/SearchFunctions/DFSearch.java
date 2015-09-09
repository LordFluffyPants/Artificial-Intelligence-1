package Problem6.src.SearchFunctions;

import Problem6.src.Puzzle.SearchNode;
import Problem6.src.Puzzle.State;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by jakehayhurst on 9/8/15.
 */
public class DFSearch {

    public static void search(int[] board)
    {
        SearchNode root = new SearchNode(new State(board));
        Stack<SearchNode> stack = new Stack<SearchNode>();
        stack.add(root);

        preformDFSearch(stack);
    }

    public static boolean checkRepeat(SearchNode node)
    {
        boolean retVal = false;
        SearchNode checkNode = node;

        while (checkNode.getParent() != null & !retVal)
        {
            if (node.getParent().getCurrentState().equals(checkNode.getCurrentState()))
            {
                retVal = true;
            }
            node = node.getParent();
        }
        return retVal;
    }

    public static void preformDFSearch(Stack<SearchNode> stack)
    {
        int expansions = 0;
        int maxQueueSize = 0;
        while (!stack.isEmpty())
        {
            if (stack.size() >= maxQueueSize)
            {
                maxQueueSize = stack.size();
            }
            
            SearchNode tempNode = stack.pop();

            if (!tempNode.getCurrentState().isGoal())
            {
                ArrayList<State> tempSuccessors = tempNode.getCurrentState().generateSuccessors();

                for (State states : tempSuccessors)
                {
                    SearchNode childNode = new SearchNode(states,tempNode,tempNode.getCost() + states.getCost(),0);

                    if (!checkRepeat(childNode))
                    {
                        stack.add(childNode);
                    }
                }
                expansions++;
            }
            else
            {
                System.out.println("The cost of the search was: "+ tempNode.getCost());
                System.out.println("The total number of expansions were: " + expansions);
                System.out.println("The maximum queue size was: " + maxQueueSize);
            }
        }
    }
}
