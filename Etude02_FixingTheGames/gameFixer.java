import java.util.Scanner;
import java.io.File;
import java.util.regex.Pattern;
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
    private static int[] collapsedGameArray;
    private static int[] expandedGameArray;


    public static void main(String[] args){
        String fileName;
        Scanner scan, lineReader;
        File file;
        Pattern pattern = Pattern.compile("[0-9]");

        if(args[0] != null) {
            try {
                file = new File(args[0]);
                if(file.exists() && file.canRead()) {
                    scan = new Scanner(file);
                    while(scan.hasNextLine()){
                        numberOfGames++;
                        String line = scan.nextLine();
                        //lineReader = new Scanner(line);
                        System.out.println(line);
                        if(numberOfPlayers > 0){
                            while(scan.hasNext()){
                                numberOfPlayers++;
                            }
                        }
                    }
                    scan.reset();
                    System.out.println("Number of games (lines): " + numberOfGames);

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
}
