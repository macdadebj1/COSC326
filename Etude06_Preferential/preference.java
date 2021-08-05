import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class preference{

    private static Map<String, Integer> ballot = new HashMap<>();

    private static ArrayList<Voter> voterList= new ArrayList<>();

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
        if(debug) System.out.println(ballot);
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
            if (ballot.containsKey(name)) {
                ballot.put(name, ballot.get(name) + 1);
            } else {
                ballot.put(name, 1);
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


}
