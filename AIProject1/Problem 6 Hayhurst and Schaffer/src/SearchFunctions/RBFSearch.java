package Problem6.src.SearchFunctions;

import Problem6.src.Puzzle.SearchNode;
import Problem6.src.Puzzle.State;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
/**
 * Created by John Wesley Hayhurst & Austin Schaffer
 */
public class RBFSearch {
    private static int maxQueueSize = 0;
    private static int expansions = 0;

    public static void search(int[] board)
    {
        SearchNode root = new SearchNode(new State(board));
        Queue<SearchNode> queue = new LinkedList<SearchNode>();
        queue.add(root);

        recursiveBFS(queue);
    }

    public static boolean checkForRepeating(SearchNode pCheckNode)
    {
        boolean retVal = false;
        SearchNode checkNode = pCheckNode;
        while (pCheckNode.getParent() != null && !retVal)
        {
            if (pCheckNode.getParent().getCurrentState().equals(checkNode.getCurrentState()))
            {
                retVal = true;
            }
            pCheckNode = pCheckNode.getParent();

        }
        return retVal;
    }

    public static void recursiveBFS(Queue<SearchNode> queue)
    {


        if (queue.isEmpty()) return;

        if (queue.size() >= maxQueueSize)
        {
            maxQueueSize = queue.size();
        }

        SearchNode tempNode = queue.poll();

        if (!tempNode.getCurrentState().isGoal())
        {
            ArrayList<State> tempSuccessors = tempNode.getCurrentState().generateSuccessors();

            for (State states : tempSuccessors)
            {
                SearchNode childNode = new SearchNode(states,tempNode,tempNode.getCost() + states.getCost(),0);
                if (!checkForRepeating(childNode))
                {
                    queue.add(childNode);
                }
            }
            expansions++;
        }
        else
        {
            System.out.println("The Cost to find the solution was: " + tempNode.getCost());
            System.out.println("The Total number of expansions were: " + expansions);
            System.out.println("The maximum queue size was: " + maxQueueSize);
            return;
        }
        recursiveBFS(queue);
    }


}
