import java.util.Scanner;

public class woofFinder{

    public static void main(String args[]){
        Scanner scan = new Scanner(System.in);

        while(scan.hasNext()){
            String line = scan.nextLine();
            if(woof(line)) System.out.println("woof");
            else System.out.println("not woof");
        }

    }
    /**
     * Contains all the over arching checks to see if something is a woof...
     *
     * */
    private static boolean woof(String str){
        if(str.length() == 1) return validateWoof(str.charAt(0));
        else if(str.charAt(0) == 'N' && str.length() > 1){
            return validateWoof(str.charAt(1));
        }else if(validateLeader(str.charAt(0)) &&
                validateWoof(str.charAt(1)) && validateWoof(str.charAt(2))){
            return true;
        }
        return false;
    }
    /**
     * Checks whether an inputted char is a woof or not.
     * */
    private static boolean validateWoof(char c){
        switch(c){
            case 'p':
            case 'q':
            case 'r':
            case 's':
                return true;
            default:
                return false;

        }
    }
    /**
     * Checks whether an inputted char is a leader or not.
     * */
    private static boolean validateLeader(char c){
        switch(c){
            case 'C':
            case 'A':
            case 'K':
            case 'E':
                return true;
            default:
                return false;
        }
    }

}