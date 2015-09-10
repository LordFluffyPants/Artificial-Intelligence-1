package Problem6.src.SearchFunctions;

import Problem6.src.Puzzle.SearchNode;
import Problem6.src.Puzzle.State;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by jakehayhurst on 9/8/15.
 */
public class AStarSearch
{
    public static void search(int[] board)
    {
        SearchNode root = new SearchNode(new State(board));
        Queue<SearchNode> queue = new LinkedList<SearchNode>();
        queue.add(root);
        preformAStarSearch(queue);
    }

    public static boolean checkRepeats(SearchNode node)
    {
        boolean retVal = false;
        SearchNode checkNode = node;
        while (node.getParent() != null && !retVal)
        {
            if (node.getParent().getCurrentState().equals(checkNode.getCurrentState()))
            {
                retVal = true;
            }
            node = node.getParent();
        }

        return retVal;

    }

    public static void preformAStarSearch(Queue<SearchNode> queue)
    {
        int expansions = 0;
        int maxQueueSize = 0;
        while (!queue.isEmpty())
        {
            if (queue.size() >= maxQueueSize)
            {
                maxQueueSize = queue.size();
            }

            SearchNode tempNode = queue.poll();
            if (!tempNode.getCurrentState().isGoal())
            {
                ArrayList<State> tempSuccessors = tempNode.getCurrentState().generateSuccessors();
                ArrayList<SearchNode> nodeSuccessors = new ArrayList<SearchNode>();

                for (State states : tempSuccessors)
                {
                    SearchNode checkedNode = new SearchNode(states,tempNode,tempNode.getCost() + states.getCost(), states.getManhattanDistance());

                    if (!checkRepeats(checkedNode))
                    {
                        nodeSuccessors.add(checkedNode);
                    }
                }

                if (nodeSuccessors.size() == 0) continue;

                SearchNode lowestNode = nodeSuccessors.get(0);

                for (int i = 0; i < nodeSuccessors.size(); i++)
                {
                    if (lowestNode.getfCost()> nodeSuccessors.get(i).getfCost())
                    {
                        lowestNode = nodeSuccessors.get(i);
                    }
                }

                double lowestValue = lowestNode.getfCost();

                for (int i = 0; i< nodeSuccessors.size(); i++)
                {
                    if (nodeSuccessors.get(i).getfCost() == lowestValue)
                    {
                        queue.add(nodeSuccessors.get(i));
                    }
                }

                expansions++;
            }
            else
            {
                System.out.println("The Cost to find the solution was: " + tempNode.getCost());
                System.out.println("The Total number of expansions were: " + expansions);
                System.out.println("The maximum queue size was: " + maxQueueSize);
                break;
            }
        }

    }
}
