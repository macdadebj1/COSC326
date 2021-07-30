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


    public static void main(String[] args){
        gameFixer f = new gameFixer();
        f.initialSetup(args);
        //printArray(gameArray);
        f.gameLoop(0,gameArray,-1, hasHadBye);
        System.out.println();
        //shiftDown(0,2);
        //shiftDown(2, 1);


    }

    private void gameLoop(int gameIndex, int[][] localGame,int playerIndex,boolean[] localBye){
        int[][] localGameArray = localGame;
        boolean[] localHasHadBye = localBye;
        boolean[] localScores = new boolean[numberOfPlayers];
        int indexOne = -1, indexTwo = -1;
        int byecount =0;
        //int repeatedScoreIndex
        System.out.println("Game index: " +gameIndex);

        for(int i = 0; i < numberOfPlayers; i++){
            if(localHasHadBye[i] == true) byecount++;
            else if(byecount == numberOfPlayers){
                System.out.println("This is as far as I can get! Everyone has had a bye!");
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
                System.out.println("PlayerScore: "+playerScore+" At game: "+gameIndex+" at player: "+i);
                if(!(localScores[playerScore])){ // if we have not seen a player with that score in this game.
                    localScores[playerScore] = true; // we have now seen a player with this score in this game.
                    System.out.println("We have not seen a player with this score, in this game! "+playerScore);
                }else if(localScores[playerScore]){ //if we have already seen a player with this score...
                    indexTwo = i;
                    System.out.println("We have already seen a player with this score, in this game! " + playerScore);
                    for(int j = 0; j < localGameArray[gameIndex].length;j++){
                        System.out.println(localGameArray[gameIndex][j]+" score at J, indextwo = " +indexTwo);
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
        if(gameIndex < numberOfGames){
            if(indexOne > -1 && indexTwo > -1){
                System.out.println("Index one: " + indexOne + ". Index two: "+indexTwo);
                if(!localHasHadBye[indexOne]) new gameFixer().gameLoop(gameIndex+1, localGameArray,indexOne,localHasHadBye);
                if(!localHasHadBye[indexTwo]) new gameFixer().gameLoop(gameIndex+1, localGameArray, indexTwo,localHasHadBye);
            }
        } else{
            System.out.println("Can't Go further... this is what I got:");
            printArray(localGameArray);
        }
        
    }

    private void shiftDown(int playerToShift, int gametoShiftTo,int[][] localArray){

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

    private void initialSetup(String[] args){
        String fileName;
        Scanner scan, lineReader;
        File file;
        //Pattern pattern = Pattern.compile("[0-9]");

        if(args[0] != null) {
            try {
                file = new File(args[0]);
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

    private void printArray(int[][] array){
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
