import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by jakehayhurst on 11/9/15.
 */
public class FullJointProbTablePlayer2 extends NannonPlayer{

    private int [][][][][][][][][] winning_board_next_with_move_effect_with_home_knowledge_states = new int[3][3][3][3][3][3][12][4][4];
    private int [][][][][][][][][] losing_board_next_with_move_effect_with_home_knowledge_states = new int[3][3][3][3][3][3][12][4][4];
    private  int number_of_wins = 1;
    private int number_of_losses = 1;


    public FullJointProbTablePlayer2(NannonGameBoard gameBoard)
    {
        super(gameBoard);
        initialize();
    }

    /**
     * inits the two arrays lists to have 1 in each category. This insures that there is not a zero probability in each state
     */
    private void initialize() {
        for (int [][][][][][][][] step_two : winning_board_next_with_move_effect_with_home_knowledge_states)
            for (int [][][][][][][] step_three : step_two)
                for (int [][][][][][] step_four : step_three)
                    for (int [][][][][] step_five : step_four)
                        for (int [][][][] step_six : step_five)
                            for(int [][][] step_seven : step_six)
                                for (int[][] step_eight : step_seven)
                                    for (int [] step_nine : step_eight)
                                        Arrays.fill(step_nine,1);

        for (int [][][][][][][][] step_two : losing_board_next_with_move_effect_with_home_knowledge_states)
            for (int [][][][][][][] step_three : step_two)
                for (int [][][][][][] step_four : step_three)
                    for (int [][][][][] step_five : step_four)
                        for (int [][][][] step_six : step_five)
                            for(int [][][] step_seven : step_six)
                                for (int[][] step_eight : step_seven)
                                    for (int [] step_nine : step_eight)
                                        Arrays.fill(step_nine,1);
    }

    @Override
    public String getPlayerName() {
        return "Full Joint Probability Nannon Player";
    }

    /**
     * chooses the move based on the ratio of the probabilities of the states given a win over the states given a loss
     * @param boardConfiguration the current board configuration
     * @param legalMoves list of legal moves the player has to choose from
     * @return the chosen mve
     */
    @SuppressWarnings("unused")
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

                double next_state_and_move_effect_given_win_prob =  (double) winning_board_next_with_move_effect_with_home_knowledge_states[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]][effect][resultingBoard[1]][resultingBoard[2]] /(double)number_of_wins;
                double win_prob = (double)number_of_wins / ((double)number_of_wins + (double)number_of_losses);

                double next_state_and_move_effect_given_loss_prob = (double) losing_board_next_with_move_effect_with_home_knowledge_states[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]][effect][resultingBoard[1]][resultingBoard[2]] / (double)number_of_losses;
                double loss_prob = (double)number_of_losses / ((double)number_of_wins + (double)number_of_losses);

                double ratio_of_success=    ( next_state_and_move_effect_given_win_prob * win_prob) /
                                            ( next_state_and_move_effect_given_loss_prob * loss_prob);

                if (ratio_of_success > highest_ratio) {
                    highest_ratio = ratio_of_success;
                    high_ratio_move = move;
                }
            }
            return high_ratio_move;
        }

        return null;
    }

    /**
     * updates the full joint probability table with the statistics of winning and losing
     * @param didIwinThisGame if the player won the game
     * @param allBoardConfigurationsThisGameForPlayer all board configurations for this game for the player
     * @param allCountsOfPossibleMovesForPlayer all possible moves for the game of the player
     * @param allMovesThisGameForPlayer all the moves for the game of the player
     */
    @Override
    public void updateStatistics(boolean didIwinThisGame, List<int[]> allBoardConfigurationsThisGameForPlayer,
                                 List<Integer> allCountsOfPossibleMovesForPlayer, List<List<Integer>> allMovesThisGameForPlayer) {

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
                //updates winning probability count here
                winning_board_next_with_move_effect_with_home_knowledge_states[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]][effect][resultingBoard[1]][resultingBoard[2]]++;
            }
            else
            {
                //updates winning probability count here
                losing_board_next_with_move_effect_with_home_knowledge_states[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]][effect][resultingBoard[1]][resultingBoard[2]]++;
            }
        }

        if (didIwinThisGame)
        {
            number_of_wins++;
        }
        else {
            number_of_losses++;
        }
    }


    @Override
    public void reportLearnedModel() {
        Utils.println("The Full Joint Probability Table Implementation of Nannon");
    }
}
