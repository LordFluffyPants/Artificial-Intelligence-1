import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by jakehayhurst on 11/9/15.
 */
public class FullJointProbTablePlayer2 extends NannonPlayer{
    private int [][][][][][] winning_board_states = new int[3][3][3][3][3][3];
    private int [][][][][][] losing_board_states = new int[3][3][3][3][3][3];
    private int [][][][][][][] winning_board_next_states = new int[3][3][3][3][3][3][12];
    private int [][][][][][][] losing_board_next_states = new int[3][3][3][3][3][3][12];
    private int [][] winning_moves = new int[7][7];
    private int [][] losing_moves = new int[7][7];
    private  int number_of_wins = 1;
    private int number_of_losses = 1;


    public FullJointProbTablePlayer2(NannonGameBoard gameBoard)
    {
        super(gameBoard);
        initialize();
    }

    private void initialize() {
        for (int [][][][][][] step_two : winning_board_next_states)
            for (int [][][][][] step_three : step_two)
                for (int [][][][] step_four : step_three)
                    for (int [][][] step_five : step_four)
                        for (int [][] step_six : step_five)
                            for(int [] step_seven : step_six)
                                Arrays.fill(step_seven,1);

        for (int [][][][][][] step_two : losing_board_next_states)
            for (int [][][][][] step_three : step_two)
                for (int [][][][] step_four : step_three)
                    for (int [][][] step_five : step_four)
                        for (int [][] step_six : step_five)
                            for (int [] step_seven : step_six)
                                Arrays.fill(step_seven,1);

        for (int [][][][][] step_two : winning_board_states)
            for (int [][][][] step_three : step_two)
                for (int [][][] step_four : step_three)
                    for (int [][] step_five : step_four)
                        for (int [] step_six : step_five)
                            Arrays.fill(step_six,1);

        for (int [][][][][] step_two : losing_board_states)
            for (int [][][][] step_three : step_two)
                for (int [][][] step_four : step_three)
                    for (int [][] step_five : step_four)
                        for (int [] step_six : step_five)
                            Arrays.fill(step_six,1);
        for (int [] steps : winning_moves)
        {
            Arrays.fill(steps,1);
        }
        for (int [] steps : losing_moves)
        {
            Arrays.fill(steps,1);
        }
    }

    @Override
    public String getPlayerName() {
        return "Full Joint Probability Nannon Player";
    }

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

                double current_state_given_win = (double) winning_board_states[boardConfiguration[7]][boardConfiguration[8]][boardConfiguration[9]][boardConfiguration[10]][boardConfiguration[11]][boardConfiguration[12]] /(double)number_of_wins;
                double next_state_and_move_effect_given_win_prob =  (double) winning_board_next_states[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]][effect] /(double)number_of_wins;
                double move_prob_given_win = (double)winning_moves[from][to] / (double)number_of_wins;
                double win_prob = (double)number_of_wins / ((double)number_of_wins + (double)number_of_losses);

                double current_state_given_loss = (double) losing_board_states[boardConfiguration[7]][boardConfiguration[8]][boardConfiguration[9]][boardConfiguration[10]][boardConfiguration[11]][boardConfiguration[12]] /(double)number_of_losses;
                double next_state_and_move_effect_given_loss_prob = (double) losing_board_next_states[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]][effect] / (double)number_of_losses;
                double move_prob_given_loss = (double)losing_moves[from][to] / (double)number_of_losses;
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

            // DO SOMETHING HERE.  See chooseMove() for an explanation of what is stored in currentBoard and resultingBoard.

            if (didIwinThisGame)
            {
                winning_board_states[currentBoard[7]][currentBoard[8]][currentBoard[9]][currentBoard[10]][currentBoard[11]][currentBoard[12]]++;
                winning_board_next_states[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]][effect]++;
                winning_moves[from][to]++;
            }
            else
            {
                losing_board_states[currentBoard[7]][currentBoard[8]][currentBoard[9]][currentBoard[10]][currentBoard[11]][currentBoard[12]]++;
                losing_board_next_states[resultingBoard[7]][resultingBoard[8]][resultingBoard[9]][resultingBoard[10]][resultingBoard[11]][resultingBoard[12]][effect]++;
                losing_moves[from][to]++;
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
