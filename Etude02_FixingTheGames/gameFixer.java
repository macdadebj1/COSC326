import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
//import java.util.regex.Pattern;
/**
 * Input Data constraints:
 * array height and width must be equal to number of players p.
 * Number of players in any round must be p-1, with one "_" to represent the player missing a game.
 * 
 * 
 * 
 * 
 */


public class gameFixer {

    private static int numberOfPlayers = 0;
    private static int numberOfGames = 0;
    private static int[][] gameArray;
    private static boolean[] hasHadBye;
    private static boolean debug = false;
    private static ArrayList<int[][]> viableGames = new ArrayList<int[][]>();
    private static ArrayList<String> thisGame = new ArrayList<String>();
    private static int numberOfPossibleSolutions = 0;


    public static void main(String[] args){
        if(args.length >= 1){
            if(args[0].charAt(0) == 'd' || args[0].charAt(0) =='D'){
                debug = true;
                System.out.println("Debugging enabled!");
            }
        }
        initialSetup();
        gameLoop(0,gameArray,-1, hasHadBye);
        if(viableGames.size() == 0){
            System.out.println("Inconsistent results");
            System.exit(1);
        } else if(viableGames.size() == 1){
            printArray(viableGames.get(0));
            System.out.println("Different results: 1");
            System.exit(0);
        }
        numberOfPossibleSolutions = viableGames.size();
        if(debug) System.out.println("We got " + numberOfPossibleSolutions + " possible games (may have duplicates)");

        checkForDuplicateSolutions();
        System.out.println("Different results: "+numberOfPossibleSolutions);

    }

    /**
     * Main loop that recursively tries to expand the games array.
     * */
    private static void gameLoop(int gameI, int[][] localGame,int playerI,boolean[] localBye){
        int[][] localGameArray = localGame;
        boolean[] localHasHadBye = localBye;
        boolean[] localScores = new boolean[numberOfPlayers];
        int gameIndex = gameI;
        int playerIndex = playerI;
        int indexOne = -1, indexTwo = -1;
        int byecount =0;
        //int repeatedScoreIndex
        if(debug) System.out.println("Game index: " +gameIndex);

        /*for(int i = 0; i < numberOfPlayers; i++){
            if(localHasHadBye[i] == true) byecount++;
            if(byecount == numberOfPlayers){
                System.out.println("======== This is as far as I can get! Everyone has had a bye! ==========");
                printArray(localGameArray);
                return;
            }
        }*/

        if(playerIndex > -1) {
            shiftDown(playerIndex, gameIndex-1,localGameArray);
            localHasHadBye[playerIndex] =true;
        }
        for(int i = 0; i < localGameArray[gameIndex].length; i++){
            if(true/*!localHasHadBye[i]*/){ //if the current player we are on hasn't had a bye.
                int playerScore = localGameArray[gameIndex][i]; //Store the score of the current player we are on.
                if(debug) System.out.println("PlayerScore: "+playerScore+" At game: "+gameIndex+" at player: "+i);
                if(!(localScores[playerScore])){ // if we have not seen a player with that score in this game.
                    localScores[playerScore] = true; // we have now seen a player with this score in this game.
                    if(debug) System.out.println("We have not seen a player with this score, in this game! "+playerScore);
                }else if(localScores[playerScore]){ //if we have already seen a player with this score...
                    indexTwo = i;
                    if(debug) System.out.println("We have already seen a player with this score, in this game! " + playerScore);
                    for(int j = 0; j < localGameArray[gameIndex].length;j++){
                        if(debug) System.out.println(localGameArray[gameIndex][j]+" score at J, indextwo = " +indexTwo);
                        if(localGameArray[gameIndex][j] == localGameArray[gameIndex][indexTwo]){
                            indexOne = j; //Bad way of getting indices of both players with the same score...
                            //System.out.println(indexOne+ " ; "+indexTwo);
                            break;
                            
                        }
                        
                    }
                    break;
                }
            }
        }
        //printArray(localGameArray);
        if(gameIndex < numberOfGames-1){
            if(indexOne > -1 && indexTwo > -1){
                if(debug) System.out.println("Index one: " + indexOne + ". Index two: "+indexTwo);
                if(!localHasHadBye[indexOne]) {
                    if(debug) System.out.println("Branching to index 1!");
                    gameLoop(gameIndex + 1, cloneGame(localGameArray), indexOne, cloneBoolArray(localHasHadBye));
                }
                if(!localHasHadBye[indexTwo]){
                    if(debug) System.out.println("Branching to index 2!");
                    gameLoop(gameIndex+1, cloneGame(localGameArray), indexTwo,cloneBoolArray(localHasHadBye));
                }
                else{
                    if(debug) System.out.println("Fell out of if statements when trying to branch!");
                }
            }
        } else{
            if(debug) System.out.println("=======Can't Go further... this is what I got:=======");
            boolean gameOkay = true;
            for(int i = 0; i<localGameArray.length; i++){
                gameOkay = checkGame(localGameArray[i]);
                if(gameOkay == false) break;


            }
            if(gameOkay){
                /**ADD to list of okay games!*/
                if(debug) System.out.println("Found an okay game!");
                viableGames.add(localGameArray);
                if(debug) printArray(localGameArray);
            }else if(!gameOkay){
                if(debug) System.out.println("Found a bad game!");
            }
        }
        
    }

    /**
     * shifts the scores of one player down from a starting game index.
     * */
    private static void shiftDown(int playerToShift, int gametoShiftTo,int[][] localArray){

        if(gametoShiftTo < numberOfGames){
            if(debug) System.out.println("Shifting down player: " +playerToShift+", in game: "+gametoShiftTo);
            for(int i = numberOfGames-2; i>= gametoShiftTo; i--){
                localArray[i+1][playerToShift] = localArray[i][playerToShift];
                if(i == gametoShiftTo){
                    localArray[i][playerToShift] = 0;
                }
                if(debug) System.out.println("Shift down! " +i);
            }
            if(debug) printArray(localArray);
        }

    }

    /**
     * Handles reading the data from stdin and storing in a 2d array.
     * */
    private static void initialSetup(){
        //String fileName;
        Scanner scan, lineReader;
        //File file;
        //Pattern pattern = Pattern.compile("[0-9]");


        try {
            //file = new File(filename);
                scan = new Scanner(System.in);
                while(scan.hasNextLine()){
                    numberOfGames++;
                    String line = scan.nextLine();
                    thisGame.add(line);
                    int playersOnThisLine = 0;
                    lineReader = new Scanner(line);
                        //System.out.println(line);
                    while(lineReader.hasNext()){
                        playersOnThisLine++;
                        lineReader.next();
                    }
                    if(numberOfPlayers == 0){
                        numberOfPlayers = playersOnThisLine;
                        //System.out.println("Set number of players equal to players on the line!");
                    } else if(numberOfPlayers != playersOnThisLine){
                        System.out.println("Bad Format");
                        System.exit(1);

                    }
                }
                if(debug)System.out.println("Number of games (rows): " + numberOfGames);
                if(debug)System.out.println("Number of players (columns): " + numberOfPlayers);

                numberOfGames = numberOfGames + 1;
                //collapsedGameArray = new int[numberOfGames-1][numberOfPlayers];
                gameArray = new int[numberOfGames][numberOfPlayers];
                hasHadBye = new boolean[numberOfPlayers];
                for(int i = 0; i < hasHadBye.length;i++){
                    hasHadBye[i] = false;
                }

                //scan = new Scanner(System.in);
                scan.reset();

                for(int i = 0; i < numberOfGames-1; i++){
                    scan = new Scanner(thisGame.get(i));
                    for(int j = 0; j < numberOfPlayers; j++){
                        if(scan.hasNext()) {
                            try {
                                int value = Integer.parseInt(scan.next());
                                if(value > 0) gameArray[i][j] = value;
                                else throw new NumberFormatException();
                            }catch(NumberFormatException e){
                                System.out.println("Bad values");
                                System.exit(1);
                            }
                        }
                    }
                }
                if(debug)printArray(gameArray);

        } catch (Exception e){
            System.out.println("Got Exception when trying to read STDIN and open file! "+e);
        }


    }

    /**
     * Helper method to clone a 2d int array.
     * */
    private static int[][] cloneGame(int[][] ingame){
        int[][] outArray = new int[ingame.length][ingame[0].length];
        for(int i = 0; i <outArray.length;i++){
            for(int j = 0; j <outArray[i].length;j++){
                outArray[i][j]=ingame[i][j];
            }
        }
        return outArray;
    }

    /**
     * Helper method to clone an array.
     * */
    private static boolean[] cloneBoolArray(boolean[] inArray){
        boolean[] outArray = new boolean[inArray.length];
        for(int i = 0; i < outArray.length;i++){
            outArray[i] = inArray[i];
        }
        return outArray;
    }

    private static int[] cloneIntArray(int[] inArray){
        int[] outArray = new int[inArray.length];
        for(int i = 0; i < outArray.length;i++){
            outArray[i] = inArray[i];
        }
        return outArray;
    }

    /**
     * Goes through a given game and makes sure it meets the criteria to be expanded
     * Criteria are: no duplicate scores and there must be a single bye.
     * */
    private static boolean checkGame(int[] game){
        if(debug)System.out.println("In checkGame!");
        boolean hadBye =false;
        boolean[] hasScoreArray = new boolean[numberOfPlayers];
        for(int i = 0; i < game.length;i++){
            if(hasScoreArray[game[i]]) {
                if(debug)System.out.println("CheckGame is going to return false!");
                return false;
            }
            else hasScoreArray[game[i]] = true;
            if(game[i]==0) {
                if(!hadBye) hadBye = true;
                else if(hadBye){
                    if(debug) System.out.println("Check game has found a game with multiple byes!");
                    return false;
                }
            }
        }
        if(debug) System.out.println("CheckGame is going to return " + hadBye);
        return hadBye;

    }

    /**
     * will check if two games are the same as each other, if so, it will remove one from the total number of unique games.
     * */
    private static void checkForDuplicateSolutions(){
        for(int i = 0; i < viableGames.size(); i++){
            if(debug) System.out.println("=========This, sorted:===========");
            printArray(viableGames.get(i));
            viableGames.set(i,sortGame(viableGames.get(i)));
            if(debug) System.out.println("=========Becomes:===========");
            if(debug) printArray(viableGames.get(i));

        }
        for(int i = 0; i < viableGames.size()-1;i++){
            if(compareGames(viableGames.get(i),viableGames.get(i+1)) == true){
                if(debug) System.out.println("Found a game that was the same!");
                numberOfPossibleSolutions -=1;
            }else{
                if(debug) System.out.println("Found two different games!");
            }
        }

    }

    /**
     * returns whether or not two games are exactly the same.
     * */
    private static boolean compareGames(int [][] game1,int[][] game2){
        for(int i = 0; i < game1.length; i++){
            for(int j = 0; j < game1[i].length;j++){
                if(game1[i][j] != game2[i][j]) return false;
            }
        }
        return true;
    }

    /**
     * This is a bubble sort implementation that I used from stackoverflow
     * sorts rows based on the content of the columns.
     *
     * Taken from here:
     * https://stackoverflow.com/a/20861638
     * */
    private static int[][] sortGame(int[][] game){
        int[] temp;
        int length = game.length;
        for(int k = length-1; k >=0;k--){         // Copied lines...
            for(int i = 0; i < length; i++){      //
                for(int j = i; j<length;j++){     //
                    if(game[i][k] < game[j][k]){  //
                        temp = game[i];
                        game[i] = game[j];
                        game[j] = temp;
                    }
                }
            }
        }
        return game;
    }

    /**Not currently used... but works :)*/
    private static int[][] swapRows(int[] row1, int[] row2){
        int [] tempRow = cloneIntArray(row1);
        row1 = cloneIntArray(row2);
        row2 = cloneIntArray(tempRow);
        return new int[][]{row1,row2};
    }

    private static void printArray(int[][] array){
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[i].length;j++){
                int itemAt = array[i][j];
                if(itemAt == 0){
                    System.out.print("_ ");
                }else{
                    System.out.print(itemAt + " ");
                }
                
            }
            System.out.println("");
        }
        System.out.println();

    }



}
