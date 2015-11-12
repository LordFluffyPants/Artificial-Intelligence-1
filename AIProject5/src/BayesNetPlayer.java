import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jakehayhurst on 11/9/15.
 */
public class BayesNetPlayer extends NannonPlayer{

    private int [][][][][][][][] winning_next_state_with_home_states = new int[3][3][3][3][3][3][4][4];
    private int [] winning_move_effect = new int[12];
    private int win_count = 1;

    private int [][][][][][][][] losing_next_state_with_home_states = new int[3][3][3][3][3][3][4][4];
    private int [] losing_move_effect = new int[12];
    private int lose_count = 1;

    public BayesNetPlayer(NannonGameBoard game_board)
    {
        super(game_board);
        initialize();
    }

    private void initialize()
    {
        //init winning states
        for (int [][][][][][][] step_one : winning_next_state_with_home_states)
            for (int [][][][][][] step_two : step_one)
                for (int [][][][][] step_three: step_two )
                    for (int [][][][] step_four : step_three)
                        for (int [][][] step_five : step_four)
                            for (int [][] step_six : step_five)
                                for (int [] step_seven : step_six)
                                 Arrays.fill(step_seven,1);
        Arrays.fill(winning_move_effect,1);

        //init losing states
        for (int [][][][][][][] step_one : losing_next_state_with_home_states)
            for (int [][][][][][] step_two : step_one)
                for (int [][][][][] step_three: step_two )
                    for (int [][][][] step_four : step_three)
                        for (int [][][] step_five : step_four)
                            for (int [][] step_six : step_five)
                                for (int [] step_seven : step_six)
                                    Arrays.fill(step_seven,1);
        Arrays.fill(losing_move_effect,1);

    }

    @Override
    public String getPlayerName() {
        return "Bayes Net Nannon Player";
    }

    @Override
    public List<Integer> chooseMove(int[] boardConfiguration, List<List<Integer>> legalMoves) {
        double highest_ratio = -1;

        if (legalMoves != null) {
            List<Integer> high_ratio_move = new ArrayList<Integer>();
            for (List<Integer> move : legalMoves) { // <----- be sure to drop the "false &&" !

                int from = move.get(0) ;  // Convert below to an internal count-from-zero system.
                int to = move.get(1) ;
                int effect = move.get(2);  // See ManageMoveEffects.java for the possible values that can appear here.
                from = (from == NannonGameBoard.movingFromHOME ? 0 : from);
                to   = (to   == NannonGameBoard.movingToSAFETY ? 0 : to);

                int[] resultingBoard = gameBoard.getNextBoardConfiguration(boardConfiguration, move);

                //calculate states given win probabilities here
                double prob_next_state_and_home_state_given_win = (double) winning_next_state_with_home_states[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]][resultingBoard[1]][resultingBoard[2]] / (double) win_count;
                double prob_move_effect_given_win = (double) winning_move_effect[effect] / (double) win_count;
                double prob_win = (double) win_count / ((double)win_count + (double)lose_count);

                //calculate states given loss probabilities here
                double prob_next_state_and_home_state_given_loss = (double) losing_next_state_with_home_states[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]][resultingBoard[1]][resultingBoard[2]] / (double) lose_count;
                double prob_move_effect_given_loss = (double) losing_move_effect[effect] / (double) lose_count;
                double prob_lose = (double) lose_count / ((double)lose_count + (double)win_count);

                double ratio_of_success = ((prob_next_state_and_home_state_given_win  * prob_move_effect_given_win * prob_win)
                                            / (prob_next_state_and_home_state_given_loss  * prob_move_effect_given_loss * prob_lose));

                //calculate the ratio of winning vs losing


                if (ratio_of_success > highest_ratio) {
                    highest_ratio = ratio_of_success;
                    high_ratio_move = move;
                }
            }
            return high_ratio_move;
        }

        return null;
    }

    @Override
    public void updateStatistics(boolean didIwinThisGame, List<int[]> allBoardConfigurationsThisGameForPlayer,
                                 List<Integer> allCountsOfPossibleMovesForPlayer, List<List<Integer>> allMovesThisGameForPlayer) {

        if (didIwinThisGame)
            win_count++;
        else
            lose_count++;


        int numberOfMyMovesThisGame = allBoardConfigurationsThisGameForPlayer.size();

        for (int myMove = 0; myMove < numberOfMyMovesThisGame; myMove++) {
            int[] currentBoard = allBoardConfigurationsThisGameForPlayer.get(myMove);
            int numberPossibleMoves = allCountsOfPossibleMovesForPlayer.get(myMove);
            List<Integer> moveChosen = allMovesThisGameForPlayer.get(myMove);
            int[] resultingBoard = (numberPossibleMoves < 1 ? currentBoard // No move possible, so board is unchanged.
                    : gameBoard.getNextBoardConfiguration(currentBoard, moveChosen));

            if (numberPossibleMoves < 1) {
                continue;
            }
            int from = moveChosen.get(0) ;  // Convert below to an internal count-from-zero system.
            int to = moveChosen.get(1) ;
            int effect = moveChosen.get(2);
            from = (from == NannonGameBoard.movingFromHOME ? 0 : from);
            to   = (to   == NannonGameBoard.movingToSAFETY ? 0 : to);

            if (didIwinThisGame)
            {
                //updates winning probability counts here
                winning_next_state_with_home_states[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]][resultingBoard[1]][resultingBoard[2]]++;
                winning_move_effect[effect]++;
            }
            else
            {
                //updates losing probability counts here
                losing_next_state_with_home_states[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]][resultingBoard[1]][resultingBoard[2]]++;
                losing_move_effect[effect]++;
            }
        }

    }

    @Override
    public void reportLearnedModel() {
        Utils.println("The Bayes Net Implementation of Nannon");
    }
}
