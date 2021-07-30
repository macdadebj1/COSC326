import java.util.Scanner;

public class woofFinder{
    private static boolean enableDebugging = false;
    public static void main(String args[]){
        if(args.length > 0){
            if(args[0].charAt(0) == 'd' || args[0].charAt(0) == 'D') {
                enableDebugging = true;
            }
        }
        if(enableDebugging) System.out.println("Debugging is enabled!");
        Scanner scan = new Scanner(System.in);

        while(scan.hasNext()){
            String line = scan.nextLine();
            if(woof(line)) System.out.println("woof");
            else System.out.println("not woof");
        }

    }
    /**
     * Contains all the over arching checks to see if something is a woof...
     *     KNqKEEsrCrsEAKqssCsq
     * */
    private static boolean woof(String str){
        boolean inwoof = false;
        if(validateWoof(str.charAt(0))) {
            if(enableDebugging) System.out.println("Single char: " +str);
            if(str.length() == 1) return true;
            //else if(validateWoof(str.charAt(0)) && validateWoof(str.charAt(1))) return false;
            else if (inwoof) {
                if(enableDebugging) System.out.println("In woof: "+inwoof);
                inwoof = false;
                return woof(str.substring(1));
            }
        }else if(str.charAt(0) == 'N' && str.length() > 1){
            if(enableDebugging) System.out.println("N --> substring:" +str.substring(1));
            return woof(str.substring(1));
        }else if(validateLeader(str.charAt(0))){
            if(enableDebugging) System.out.println("Leader + str:" + str);
            inwoof = true;
            return woof(str.substring(1)) && woof(str.substring(2));
            //return true;
        }else{
            if(enableDebugging) System.out.println("Fell through if statements in woof!");
            validateWoof(str.charAt(0));
        }
        return false;
    }
    /**
     * Checks whether an inputted char is a woof or not.
     * */
    private static boolean validateWoof(char c){
        boolean returnType;

        switch(c){
            case 'p':
            case 'q':
            case 'r':
            case 's':
                returnType = true;
                break;
            default:
                returnType = false;
                break;

        }
        if(enableDebugging) System.out.println("validate woof: "+c + " "+returnType);
        return returnType;
    }
    /**
     * Checks whether an inputted char is a leader or not.
     * */
    private static boolean validateLeader(char c){
        boolean returnType;
        switch(c){
            case 'C':
            case 'A':
            case 'K':
            case 'E':
                returnType = true;
                break;
            default:
                returnType = false;
                break;
        }
        if(enableDebugging) System.out.println("validate leader: "+c + " " +returnType);
        return returnType;
    }

}