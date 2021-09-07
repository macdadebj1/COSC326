package epidemic;

import java.util.Scanner;
import java.util.ArrayList;

class epidemic{

    protected static boolean debug = false;
    private static ArrayList<Universe> UniverseList = new ArrayList<>();
    private static Universe currentUniverse;

    public static void main(String[] args){
        if(args.length >0){
            if(args[0].charAt(0) == 'd' || args[0].charAt(0) == 'D'){
                System.out.println("Enabled Debugging!");
                debug = true;
            }
        }

        readData();
        printUniverseList();

    }

    private static void readData(){
        Scanner Scan = new Scanner(System.in);
        String line;
        NodeState state = NodeState.UNINITIALIZED;
        while(Scan.hasNextLine()){
            line = Scan.nextLine();
            if(line.equals("") || UniverseList.size() == 0){
                currentUniverse = new Universe();
                UniverseList.add(currentUniverse);
                if(debug) System.out.println("==============================");
                if(debug) System.out.println("new Universe!");
            }
            line.chars().forEach(ch->{if(debug) System.out.print((char)ch);});
            if(debug) System.out.println("");
            for(int i = 0; i < line.length();i++){
                if(i == 0){
                    currentUniverse.nodes.add(new ArrayList<Node>());
                }
                if(line.charAt(i) == '.') state = NodeState.VULNERABLE;
                else if(line.charAt(i) == 'I') state = NodeState.IMMUNE;
                else if(line.charAt(i) == 'S') state = NodeState.SICK;
                else System.out.println("ERROR! Char not recognized!");
                currentUniverse.nodes.get(currentUniverse.nodes.size()-1).add(new Node(state));
            }
        }
    }

    private static void linkNodes(Universe u){
        for(int i = 0; i < u.nodes.size();i++){

        }
    }

    private static void printUniverse(Universe u){
        for(int i = 0; i < u.nodes.size();i++){
            for(int j = 0; j < u.nodes.get(i).size();j++){
                System.out.print(u.nodes.get(i).get(j));
            }
            System.out.println("");
        }
    }


    private static void printUniverseList(){
        for(int i = 0; i < UniverseList.size();i++){
            printUniverse(UniverseList.get(i));
        }
    }

    /*private static void addNodeToUniverse(char c){
        if(c != '.' && c != 'I' && c != 'S'){
            System.out.println("There is an unrecognized character: "+c);
        }

    }*/

}