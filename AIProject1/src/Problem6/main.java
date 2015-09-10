package Problem6;

import Problem6.src.Data.ImportManager;
import Problem6.src.SearchFunctions.AStarSearch;
import Problem6.src.SearchFunctions.BFSearch;
import Problem6.src.SearchFunctions.DFSearch;
import Problem6.src.SearchFunctions.RBFSearch;

/**
 * Created by jakehayhurst on 9/6/15.
 */
public class main {

    public static void main(String [] args)
    {
        int [] board = new int[9];
        for (int i = 0; i < ImportManager.getInstance().getMap().size(); i ++)
        {
            board[i] = ImportManager.getInstance().getMap().get(i);
        }

        System.out.println("BFSearch: ");
        BFSearch.Search(board);
        System.out.println("\nDFSearch: ");
        DFSearch.search(board);
        System.out.println("\nA* Search: ");
        AStarSearch.search(board);
        System.out.println("\nRecursive BFSearch: ");
        RBFSearch.search(board);
    }
}
