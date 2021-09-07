import java.util.Scanner;
import java.util.ArrayList;

class epidemic{

    protected static boolean debug = false;
    private static ArrayList<Universe> UniverseList = new ArrayList<>();

    public static void main(String[] args){
        if(args.length >0){
            if(args[0].charAt(0) == 'd' || args[0].charAt(0) == 'D'){
                System.out.println("Enabled Debugging!");
                debug = true;
            }
        }

        readData();

    }

    private static void readData(){
        Scanner Scan = new Scanner(System.in);
        String line;
        while(Scan.hasNextLine()){
            line = Scan.nextLine();
            if(line.equals("") || UniverseList.size() == 0){
                UniverseList.add(new Universe());
                if(debug) System.out.println("new Universe!");
            }
            //line.chars().forEach(ch->{if(debug) System.out.print((char)ch);});
            for(int i = 0; i < line.length();i++){

            }
        }
    }

    private static void addNodeToUniverse(char c){
        if(c != '.' && c != 'I' && c != 'S'){
            System.out.println("There is an unrecognized character: "+c);
        }

    }

}