import java.util.Scanner;
import java.io.File;
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
    //private static int[][] collapsedGameArray;
    private static int[][] gameArray;
    private static boolean[] hasHadBye;
    private static boolean debug = false;


    public static void main(String[] args){
        if(args.length >= 2){
            if(args[1].charAt(0) == 'd' || args[1].charAt(0) =='D'){
                debug = true;
                System.out.println("Debugging enabled!");
            }
        }
        initialSetup(args[0]);
        //printArray(gameArray);
        gameLoop(0,gameArray,-1, hasHadBye);
        //newGameLoop();
        System.out.println();
        //shiftDown(0,2);
        //shiftDown(2, 1);


    }

    /*private static void newGameLoop(){
        int[] currentGameScores = new int[numberOfPlayers];
        for(int i = 0; i < numberOfGames; i++){
            for(int j = 0; j <numberOfPlayers; j++){
                int playerScore = gameArray[i][j];
                if(currentGameScores[playerScore]==0) {
                    currentGameScores[playerScore] = j;
                    if (debug) System.out.println("We haven't seen this score in this game before! "+currentGameScores[playerScore]);
                }else if(currentGameScores[playerScore] != 0){
                    if (debug) System.out.println("We have seen this score in this game before! "+playerScore + " at index: "+currentGameScores[playerScore]);
                }
            }
        }

    }*/

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

        for(int i = 0; i < numberOfPlayers; i++){
            if(localHasHadBye[i] == true) byecount++;
            if(byecount == numberOfPlayers){
                System.out.println("======== This is as far as I can get! Everyone has had a bye! ==========");
                printArray(localGameArray);
                return;
            }
        }

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
            }
        } else{
            if(debug) System.out.println("=======Can't Go further... this is what I got:=======");
            boolean gameOkay = true;
            for(int i = 0; i<localGameArray.length; i++){
                gameOkay = checkGame(localGameArray[i]);

            }
            if(gameOkay){
                /**ADD to list of okay games!*/
                System.out.println("Found an okay game!");
                printArray(localGameArray);
            }
        }
        
    }

    private static void shiftDown(int playerToShift, int gametoShiftTo,int[][] localArray){

        if(gametoShiftTo < numberOfGames){
            System.out.println("Shifting down player: " +playerToShift+", in game: "+gametoShiftTo);
            for(int i = numberOfGames-2; i>= gametoShiftTo; i--){
                localArray[i+1][playerToShift] = localArray[i][playerToShift];
                if(i == gametoShiftTo){
                    localArray[i][playerToShift] = 0;
                }
                System.out.println("Shift down! " +i);
            }
            printArray(localArray);
        }

    }

    private static void initialSetup(String filename){
        //String fileName;
        Scanner scan, lineReader;
        File file;
        //Pattern pattern = Pattern.compile("[0-9]");

        if(filename != null) {
            try {
                file = new File(filename);
                if(file.exists() && file.canRead()) {
                    scan = new Scanner(file);
                    while(scan.hasNextLine()){
                        numberOfGames++;
                        String line = scan.nextLine();
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
                    System.out.println("Number of games (columns): " + numberOfGames);
                    System.out.println("Number of players (lines): " + numberOfPlayers);

                    numberOfGames = numberOfGames + 1;
                    //collapsedGameArray = new int[numberOfGames-1][numberOfPlayers];
                    gameArray = new int[numberOfGames][numberOfPlayers];
                    hasHadBye = new boolean[numberOfPlayers];
                    for(int i = 0; i < hasHadBye.length;i++){
                        hasHadBye[i] = false;
                    }

                    scan = new Scanner(file);

                    for(int i = 0; i < numberOfGames; i++){
                        for(int j = 0; j < numberOfPlayers; j++){
                            if(scan.hasNext()) {
                                gameArray[i][j] = Integer.parseInt(scan.next());
                            }
                        }
                    }
                   printArray(gameArray);

                }else{
                    System.out.println("file object is null in main! Please supply a valid filename!");
                }

            } catch (Exception e){
                System.out.println("Got exception when trying to read STDIN and open file! "+e);
            }



        }else{
            System.out.println("args[0] is null, please supply filename to read.");
        }

    }

    private static int[][] cloneGame(int[][] ingame){
        int[][] outArray = new int[ingame.length][ingame[0].length];
        for(int i = 0; i <outArray.length;i++){
            for(int j = 0; j <outArray[i].length;j++){
                outArray[i][j]=ingame[i][j];
            }
        }
        return outArray;
    }

    private static boolean[] cloneBoolArray(boolean[] inArray){
        boolean[] outArray = new boolean[inArray.length];
        for(int i = 0; i < outArray.length;i++){
            outArray[i] = inArray[i];
        }
        return outArray;
    }

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
            if(game[i]==0) hadBye = true;
        }
        if(debug) System.out.println("CheckGame is going to return " + hadBye);
        return hadBye;

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

    }



}
