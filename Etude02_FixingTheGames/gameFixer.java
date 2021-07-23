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
    private static int[][] collapsedGameArray;
    private static int[][] expandedGameArray;
    private static boolean[] hasHadBye;


    public static void main(String[] args){
        initialSetup(args);

    }

    private static void shiftDown(int indexToShift){


    }

    private static void initialSetup(String[] args){
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
                        System.out.println(line);
                        while(lineReader.hasNext()){
                            playersOnThisLine++;
                            lineReader.next();
                        }
                        if(numberOfPlayers == 0){
                            numberOfPlayers = playersOnThisLine;
                            //System.out.println("Set number of players equal to players on the line!");
                        } else if(numberOfPlayers != playersOnThisLine){
                            System.out.println("Bad Format");
                            break;

                        }
                    }
                    System.out.println("Number of games (lines): " + numberOfGames);
                    System.out.println("Number of players (columns): " + numberOfPlayers);

                    numberOfGames = numberOfGames + 1;
                    collapsedGameArray = new int[numberOfGames-1][numberOfPlayers];
                    expandedGameArray = new int[numberOfGames][numberOfPlayers];
                    hasHadBye = new boolean[numberOfPlayers];

                    scan = new Scanner(file);

                    for(int i = 0; i < numberOfPlayers; i++){
                        for(int j = 0; j < numberOfGames; j++){
                            if(scan.hasNext()) {
                                collapsedGameArray[i][j] = Integer.parseInt(scan.next());
                            }
                        }
                    }
                   printArray(collapsedGameArray);

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

    private static void printArray(int[][] array){
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[i].length;j++){
                System.out.print(array[i][j] + " ");
            }
            System.out.println("");
        }

    }



}
