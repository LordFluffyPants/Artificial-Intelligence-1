import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jakehayhurst on 11/9/15.
 */
public class NaiveBayesNetPlayer extends NannonPlayer {

    private int [] winning_my_home_state = new int[4];
    private int [] winning_opponent_home_state = new int[4];
    private int [] winning_move_effects = new int[12];
    private int [][][][][][] winning_next_board_state = new int[3][3][3][3][3][3];
    private int win_count = 1;

    private int [] losing_my_home_state = new int[4];
    private int [] losing_opponent_home_state = new int[4];
    private int [] losing_move_effects = new int[12];
    private int [][][][][][] losing_next_board_state = new int[3][3][3][3][3][3];
    private int loss_count = 1;

    public NaiveBayesNetPlayer(NannonGameBoard gameBoard)
    {
        super(gameBoard);
        initialize();
    }

    private void initialize()
    {
        //inits the winning probability counts
        Arrays.fill(winning_my_home_state, 1);
        Arrays.fill(winning_opponent_home_state, 1);
        Arrays.fill(winning_move_effects,1);
        for(int [][][][][] step_one : winning_next_board_state)
            for(int [][][][] step_two : step_one)
                for(int [][][] step_three : step_two)
                    for(int [][] step_four : step_three)
                        for(int [] step_five : step_four)
                            Arrays.fill(step_five,1);

        //inits the losing probability counts
        Arrays.fill(losing_my_home_state,1);
        Arrays.fill(losing_opponent_home_state,1);
        Arrays.fill(losing_move_effects,1);
        for(int [][][][][] step_one : losing_next_board_state)
            for(int [][][][] step_two : step_one)
                for(int [][][] step_three : step_two)
                    for(int [][] step_four : step_three)
                        for(int [] step_five : step_four)
                            Arrays.fill(step_five,1);

    }

    @Override
    public String getPlayerName() {
        return "Naive Bayes Net Nannon Player assumes full independence";
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
                double prob_my_home_state_given_win = (double) winning_my_home_state[resultingBoard[1]] / (double) win_count;
                double prob_opponent_home_state_given_win = (double) winning_opponent_home_state[resultingBoard[2]] / (double) win_count;
                double prob_move_effects_given_win = (double) winning_move_effects[effect] / (double) win_count;
                double prob_next_board_state_given_win = (double) winning_next_board_state[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]] / (double) win_count;
                double win_prob = (double) win_count /  ((double) win_count + (double) loss_count);

                //calculate states given loss probabilities here
                double prob_my_home_state_given_loss = (double) losing_my_home_state[resultingBoard[1]] / (double) loss_count;
                double prob_opponent_home_state_given_loss = (double) losing_opponent_home_state[resultingBoard[2]] / (double) loss_count;
                double prob_move_effects_given_loss = (double) losing_move_effects[effect] / (double) loss_count;
                double prob_next_board_state_given_loss = (double) losing_next_board_state[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]] / (double) loss_count;
                double loss_prob  = (double) loss_count / ((double) win_count + (double) loss_count);

                double ratio_of_success = ((prob_my_home_state_given_win * prob_opponent_home_state_given_win * prob_move_effects_given_win * prob_next_board_state_given_win * win_prob)
                                            / (prob_my_home_state_given_loss * prob_opponent_home_state_given_loss * prob_move_effects_given_loss * prob_next_board_state_given_loss * loss_prob));

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
        {
            win_count++;
        }
        else
        {
            loss_count++;
        }

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
                winning_my_home_state[resultingBoard[1]]++;
                winning_opponent_home_state[resultingBoard[2]]++;
                winning_move_effects[effect]++;
                winning_next_board_state[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]]++;
            }
            else
            {
                //updates losing probability counts here
                losing_my_home_state[resultingBoard[1]]++;
                losing_opponent_home_state[resultingBoard[2]]++;
                losing_move_effects[effect]++;
                losing_next_board_state[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]]++;
            }
        }

    }

    @Override
    public void reportLearnedModel() {
        Utils.println("The Naive Bayes Net Implementation of Nannon");
    }
}
