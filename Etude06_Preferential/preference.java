import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

public class preference{

    //private static Map<String, Integer> ballot = new HashMap<>();

    private static ArrayList<Voter> voterList= new ArrayList<>();
    private static ArrayList<Candidate> candidateArrayList = new ArrayList<>();

    private static boolean debug = false;

    public static void main(String args[]){

        if(args.length >0){
            if(args[0].charAt(0) =='d' || args[0].charAt(0) =='D'){
                System.out.println("Enabled Debugging!");
                debug = true;
            }
        }
        readVoterInfo();
        if(debug) printBallot();
        readBallotInfo();
        if(debug) printCurrentRound();
        if(debug) System.out.println("================");
        sortCurrentRound();
        if(debug) printCurrentRound();
    }

    private static boolean doElection(){
        if(candidateArrayList.get(0).votes >= voterList.size()/2) return true;
        else if(candidateArrayList.get(0).votes == candidateArrayList.get(1).votes){
            //Tie...
        }else{

        }
    }

    private static void readVoterInfo(){
        if(debug) System.out.println("Reading Ballot Info!");
        Scanner scan = new Scanner(System.in);

        while(scan.hasNextLine()){
            Voter v = new Voter();
            String line = scan.nextLine();
            Scanner lineReader = new Scanner(line);
            while(lineReader.hasNext()){
                v.voteList.add(lineReader.next());
            }
            voterList.add(v);
        }

    }

    private static void readBallotInfo(){
        for(int i = 0; i < voterList.size();i++) {
            String name = voterList.get(i).voteList.get(0);
            int index = findCandidate(name);
            if (index > -1) {
                candidateArrayList.get(index).votes+=1;
                //ballot.put(name, ballot.get(name) + 1);
            } else {
                candidateArrayList.add(new Candidate(name,1));
            }
        }
    }

    private static void printBallot(){
        for(int i = 0; i < voterList.size();i++){
            ArrayList<String> votes = voterList.get(i).voteList;
            for(int j = 0;j < votes.size();j++){
                System.out.print(votes.get(j)+ " ");
            }
            System.out.println();
        }
    }

    private static int findCandidate(String name){
        for(int i = 0; i < candidateArrayList.size();i++){
            if(candidateArrayList.get(i).name.equals(name)){
                //if(debug) System.out.println("Found a matching candidate at index "+i);
                return i;
            }
        }
        return -1;
    }

    private static void printCurrentRound(){
        for(int i = 0; i < candidateArrayList.size();i++){
            System.out.println(candidateArrayList.get(i));
        }
    }

    private static void sortCurrentRound(){
        Collections.sort(candidateArrayList);
        /*
        for(int i = 0; i < candidateArrayList.size();i++){
            for(int j = 0; j <candidateArrayList.size();j++){
                if(candidateArrayList.get(i).votes < candidateArrayList.get(i).votes){
                    Candidate temp = copyCandidate(candidateArrayList.get(i));
                    candidateArrayList.set(i,copyCandidate(candidateArrayList.get(j)));
                    candidateArrayList.set(j,copyCandidate(temp));
                }
            }
        }*/
    }

    private static Candidate copyCandidate(Candidate c){
        return new Candidate(c);
    }


}
