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
        //printUniverseList();
        for(int i = 0; i <UniverseList.size();i++){
            if(debug) System.out.println("Size: " +UniverseList.get(i).nodes.size());
            printUniverse(findSteadyState(UniverseList.get(i)));
        }

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

    private static Universe findSteadyState(Universe u){
        if(debug) System.out.println("In find steady state!");
        Universe localUniverse = new Universe(u);
        boolean foundSolution = false;
        if(debug) System.out.println("Before:");
        if(debug) printUniverse(localUniverse);
        int recursiveDepth = 0;
        int numberOfAlterationsThisPass;
        while(!foundSolution) {
            if(debug) System.out.println("Doing another loop!");
            numberOfAlterationsThisPass = 0;
            //if(recursiveDepth >= 10) System.exit(0);
            for (int i = 0; i < localUniverse.nodes.size(); i++) {
                for (int j = 0; j < localUniverse.nodes.get(i).size(); j++) {
                    if(localUniverse.nodes.get(i).get(j).state == NodeState.VULNERABLE){
                        if(countSickNeighbours(i,j,localUniverse) >=2){
                            localUniverse.nodes.get(i).get(j).makeSick();
                            if(debug) System.out.println("This Node is now Sick!");
                            //foundSolution = true;
                            numberOfAlterationsThisPass += 1;
                        }
                    }
                }
            }
            recursiveDepth++;
            if(debug) System.out.println("Alterations this pass: "+numberOfAlterationsThisPass);
            if(numberOfAlterationsThisPass == 0){
                foundSolution = true;
            }
        }
        if(debug) System.out.println("After:");
        return localUniverse;
    }

    private static int countSickNeighbours(int x, int y, Universe u){
        int xSize = u.nodes.size();
        int ySize = u.nodes.get(0).size();
        int count = 0;
        if(debug) System.out.println("Counting sick neighbours! x: "+x+" y:"+y);
        if(x<xSize-1){
            if(u.nodes.get(x+1).get(y).state == NodeState.SICK){
                count += 1;
                if(debug) System.out.println("Sick below!");
            }
        }
        if(x > 0){
            if(u.nodes.get(x-1).get(y).state == NodeState.SICK){
                count += 1;
                if(debug) System.out.println("Sick above!");
            }
        }
        if(y<ySize-1){
            if(u.nodes.get(x).get(y+1).state == NodeState.SICK){
                count += 1;
                if(debug) System.out.println("Sick to the right!");
            }
        }
        if(y > 0){
            if(u.nodes.get(x).get(y-1).state == NodeState.SICK){
                count += 1;
                if(debug) System.out.println("Sick to the left!");
            }
        }
        if(debug) System.out.println("Returning a count of: " +count);
        return count;

    }
/*
    private static void linkNodes(Universe u){
        for(int i = 0; i < u.nodes.size();i++){
            for(int j = 0; j < u.nodes.get(i).size();j++){

            }
        }
    }
*/
    private static void printUniverse(Universe u){
        if(debug) System.out.println("In print universe!");
        for(int i = 0; i < u.nodes.size();i++){
            for(int j = 0; j < u.nodes.get(i).size();j++){
                System.out.print(u.nodes.get(i).get(j));
            }
            System.out.println("");
        }
        System.out.println("");
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