package com.udacity;

import java.util.Arrays;

/**
 * Created by udacity 2016
 * The Main class containing game logic and backend 2D array
 */
public class Game {

    private char turn; // who's turn is it, 'x' or 'o' ? x always starts
    private boolean twoPlayer; // true if this is a 2 player game, false if AI playing
    private char [][] grid; // a 2D array of chars representing the game grid
    private int freeSpots; // counts the number of empty spots remaining on the board (starts from 9  and counts down)
    private static GameUI gui;

    /**
     * Create a new single player game
     *
     */
    public Game() {
        newGame(false);
    }

    /**
     * Create a new game by clearing the 2D grid and restarting the freeSpots counter and setting the turn to x
     * @param twoPlayer: true if this is a 2 player game, false if playing against the computer
     */
    public void newGame(boolean twoPlayer){
        //sets a game to one or two player
        this.twoPlayer = twoPlayer;

        // initialize all chars in 3x3 game grid to '-'
        grid = new char[3][3];
        //fill all empty slots with -
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                grid[i][j] = '-';
            }
        }
        //start with 9 free spots and decrement by one every time a spot is taken
        freeSpots = 9;
        //x always starts
        turn = 'x';
    }


    /**
     * Gets the char value at that particular position in the grid array
     * @param i the x index of the 2D array grid
     * @param j the y index of the 2D array grid
     * @return the char value at the position (i,j):
     *          'x' if x has played here
     *          'o' if o has played here
     *          '-' if no one has played here
     *          '!' if i or j is out of bounds
     */
    public char gridAt(int i, int j){
        if(i>=3||j>=3||i<0||j<0)
            return '!';
        return grid[i][j];
    }

    /**
     * Places current player's char at position (i,j)
     * Uses the variable turn to decide what char to use
     * @param i the x index of the 2D array grid
     * @param j the y index of the 2D array grid
     * @return boolean: true if play was successful, false if invalid play
     */
    public boolean playAt(int i, int j){
        //check for index boundries
        if(i>=3||j>=3||i<0||j<0)
            return false;
        //check if this position is available
        if(grid[i][j] != '-'){
            return false; //bail out if not available
        }
        //update grid with new play based on who's turn it is
        grid[i][j] = turn;
        //update free spots
        freeSpots--;
        return true;
    }


    /**
     * Override
     * @return string format for 2D array values
     */
    public String toString(){
        return Arrays.deepToString(this.grid);
    }

    /**
     * Performs the winner chack and displayes a message if game is over
     * @return true if game is over to start a new game
     */
    public boolean doChecks() {
        //check if there's a winner or tie ?
        String winnerMessage = checkGameWinner(grid);
        if (!winnerMessage.equals("None")) {
            gui.gameOver(winnerMessage);
            newGame(false);
            return true;
        }
        return false;
    }

    /**
     * Allows computer to play in a single player game or switch turns for 2 player game
     */
    public void nextTurn(){
        //check if single player game, then let computer play turn
        if(!twoPlayer){
            if(freeSpots == 0){
                return ; //bail out if no more free spots
            }
            int ai_i, ai_j;
            do {
                //randomly pick a position (ai_i,ai_j)
                ai_i = (int) (Math.random() * 3);
                ai_j = (int) (Math.random() * 3);
            }while(grid[ai_i][ai_j] != '-'); //keep trying if this spot was taken
            //update grid with new play, computer is always o
            grid[ai_i][ai_j] = 'o';
            //update free spots
            freeSpots--;
        }
        else{
            //change turns
            if(turn == 'x'){
                turn = 'o';
            }
            else{
                turn = 'x';
            }
        }
        return;
    }


    /**
     * Checks if the game has ended either because a player has won, or if the game has ended as a tie.
     * If game hasn't ended the return string has to be "None",
     * If the game ends as tie, the return string has to be "Tie",
     * If the game ends because there's a winner, it should return "X wins" or "O wins" accordingly
     * @param grid 2D array of characters representing the game board
     * @return String indicating the outcome of the game: "X wins" or "O wins" or "Tie" or "None"
     */
    public String checkGameWinner(char [][]grid){
        String result = "None";
        
        //Scenario if there are 3 'X' or 3 'O' in one line (row)
        int xInLines = 0;
        int oInLines = 0;
        for (int j = 0;j<=2;j++) {
            for (int i = 0; i <= 2; i++) {
                if (grid[i][j] == 'x') {
                    xInLines++;
                } else if (grid[i][j] == 'o') {
                   oInLines++;
                }
            }
            if (xInLines != 3) {
                xInLines = 0;
            }
            if (oInLines != 3) {
                oInLines =0;
            }
            if (xInLines == 3) {
                return "X wins";
            } else if (oInLines == 3){
                return "O wins";
            }
        }

        //Scenario if there are 3 'X' or 3 'O' in one column
        int xInColumns = 0;
        int oInColumns = 0;
        for (int i = 0; i<=2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (grid[i][j] == 'x') {
                    xInColumns++;
                } else if (grid[i][j] == 'o') {
                    oInColumns++;
                }
            }
            if (xInColumns != 3) {
                xInColumns = 0;
            }
            if (oInColumns != 3) {
                oInColumns = 0;
            }

            if (xInColumns == 3) {
                return "X wins";
            } else if (oInColumns == 3) {
                return "O wins";
            }
        }

        //Scenario if there are 3 'X' or 3 'O' in diagonal from left upper corner to right lower corner
        int leftToRightDiagonalWithX = 0;
        int leftToRightDiagonalWithO = 0;

        for (int i = 0; i<=2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (grid[i][j] == 'x') {
                    if (i == j) {
                        leftToRightDiagonalWithX += 1;
                    }
                } else if (grid[i][j] == 'o') {
                    if (i == j) {
                        leftToRightDiagonalWithO += 1;
                    }
                }
            }
        }
        if (leftToRightDiagonalWithX == 3) {
           return "X wins";
        } else if (leftToRightDiagonalWithO == 3) {
            return "O wins";
        }

        //Scenario if there are 3 'X' or 3 'O' in diagonal from right upper corner to left lower corner
        int rightToLeftDiagonalWithX = 0;
        int rightToLeftDiagonalWithO = 0;
        int trackedI = 0;
        int trackedJ = 0;
        for (int i = 2; i>=0; i--) {
            for (int j = 0; j <= 2; j++) {
                if (grid[i][j] == 'x') {
                    if (i == 2 && j == 0) {
                        rightToLeftDiagonalWithX++;
                        trackedI = i;
                        trackedJ = j;
                    }
                    if ((trackedI - i == 1) && (j == trackedJ + 1)) {
                        rightToLeftDiagonalWithX++;
                        trackedI = i;
                        trackedJ = j;
                    }
                } else if (grid[i][j] == 'o') {
                    if (i == 2 && j == 0) {
                        rightToLeftDiagonalWithO++;
                        trackedI = i;
                        trackedJ = j;
                    }
                    if ((trackedI - i == 1) && (j == trackedJ + 1)) {
                        rightToLeftDiagonalWithO++;
                        trackedI = i;
                        trackedJ = j;
                    }
                }
            }
        }
        if (rightToLeftDiagonalWithX == 3) {
            return "X wins";
        } else if (rightToLeftDiagonalWithO == 3) {
            return "O wins";
        }

        // Alternative version of both diagonals
        int axisX1 = 0, axisX2 = 0;
        int axisO1 = 0, axisO2 = 0;
        for (int i = 0; i <= 2; i++) {
            if (grid[i][i] == 'x') {
                axisX1++;
            }
            if (grid[i][2-i] == 'x') {
                axisX2++;
            }
            if (grid[i][i] == 'o') {
                axisO1++;
            }
            if (grid[i][2-i] == 'o') {
                axisO2++;
            }
        }

        if (axisX1 == 3 || axisX2 == 3) {
            return "X wins";
        }
        if (axisO1 == 3 || axisO2 == 3) {
            return "O wins";
        }

        // "Tie" (no winner) scenario
        if (freeSpots == 0) {
            return "Tie";
        }
            return result;
    }

    /**
     * Main function
     * @param args command line arguments
     */
    public static void main(String args[]){
        Game game = new Game();
        gui = new GameUI(game);
    }

}
