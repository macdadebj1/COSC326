import java.util.Scanner;
import java.util.HashMap;
//import java.util.Map
import java.util.ArrayList;
import java.util.Collections;


public class preference{

    //List of all voters, stored as Voter objects.
    private static ArrayList<Voter> voterList= new ArrayList<>();

    //Stores the index of each candidate in a slightly less intensive to access way...
    private static HashMap<String, Integer> candidateLocation = new HashMap<>();

    //the  current state of the election.
    private static ArrayList<Candidate> currentCandidateArrayList = new ArrayList<>();

    //Stores all past rounds.
    private static ArrayList<ArrayList<Candidate>> gameHistory = new ArrayList<>();

    //Used mainly to stop infinite recursion...
    private static int recursiveDepth = 0;

    //Global debug flag.
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
        if(debug) System.out.println("================");
        readBallotInfo();
        if(debug) printCurrentRound();
        if(debug) System.out.println("================");
        sortCurrentRound();
        //if(debug) printCurrentRound();
        doElection();
        if(debug) System.out.println(gameHistory.size());
    }

    private static boolean doElection(){
        recursiveDepth++;
        if(debug) System.out.println("==========");
        if(recursiveDepth >10) System.exit(1);
        System.out.println("Round " +recursiveDepth);
        printCurrentRound();
        if(currentCandidateArrayList.get(0).votes > voterList.size()/2){
            System.out.println("Winner: "+ currentCandidateArrayList.get(0).name);
            return true;
        }else{
            int votesAtLastIndex = currentCandidateArrayList.get(currentCandidateArrayList.size()-1).votes;
            ArrayList<String> candidatesWithDuplicateVotes = new ArrayList<>();
            for(int i = currentCandidateArrayList.size()-1; i >=0; i--){
                if(currentCandidateArrayList.get(i).votes == votesAtLastIndex){
                    candidatesWithDuplicateVotes.add(currentCandidateArrayList.get(i).name);
                }else break;
            }
            if(candidatesWithDuplicateVotes.size() == 1){ //no Tie!
                Candidate c = currentCandidateArrayList.get(currentCandidateArrayList.size()-1);
                redistributeVotes(c);
            }else{ //tie... :(
                 TIEBREAKER(candidatesWithDuplicateVotes);
            }


        }
        return false;
    }

     private static void TIEBREAKER(ArrayList<String> nameArrayList){

        for(int i = gameHistory.size()-1; i <= 0 ; i--){
            for(int i = gameHistory.get(i).size()-1;i <=0;i++){

            }
        }
     }

     /**
      * Redistributes the votes of the passed in candidate.
      * @param c the candidate whos votes we are going to be dividing out.
      * */
    private static void redistributeVotes(Candidate c){
        for(int i = 0; i <voterList.size();i++){
            Voter v = voterList.get(i);
            if(debug) System.out.println("searching for: "+c.name);
            if(debug) System.out.println("Searching voter: "+i+". They voted for: "+v.voteList.get(v.voteIndex));
            if(v.voteList.get(v.voteIndex).equals(c.name)){
                v.voteIndex += 1;
                c.votes--;
                if(v.voteList.size() > v.voteIndex) { //If they still have another next-best choice...
                    String tempCandidate = v.voteList.get(v.voteIndex);
                    if (debug) System.out.println("added one vote to: " + tempCandidate);
                    addVote(tempCandidate, 1);
                }else{ //If they don't have another next-best choice...
                    voterList.remove(i); //TODO we are removing the voter from the voting pool if they don't have another option... may not want to do this..?
                    if(debug) System.out.println("A Voter didn't have another option to choose, so they didn't vote...");
                }
            }else{
                if(debug) System.out.println("didn't match!");
            }
        }
        if(c.votes==0){
            if(debug) System.out.println("Removing a candidate from the pool!");
            System.out.println("Eliminated: "+c.name+"\n");
            currentCandidateArrayList.remove(currentCandidateArrayList.size()-1);
            saveRound();
            doElection();
        } else{
            if(debug) System.out.println("Error removing candidate from pool!, they still have votes assigned to them!");
        }
    }

    /**
     * addVote adds a number of votes to a candidate.
     *
     * @param name the name of the candidate to give the votes to.
     * @param numberOfVotes the number of votes to give the candidate.
     * */
    private static void addVote(String name,int numberOfVotes){
        if(debug) System.out.println("In adding vote!");
        if(candidateLocation.containsKey(name)){
            if(debug) System.out.println("in adding vote if statement, candidateLocation contains Key!");
            int index = candidateLocation.get(name);
            currentCandidateArrayList.get(index).votes+=numberOfVotes;
        }else{
            if(debug) System.out.println("index is null in addVote! this means the hashmap doesn't have an index available for candidate: "+name);
        }
    }

    /**
     * Saves the current round in the gameHistory ArrayList.
     * */
    private static void saveRound(){
        gameHistory.add(cloneArrayList(currentCandidateArrayList));
    }

    /**
     * Helper method to do a deep copy of an ArrayList.
     * */
    private static ArrayList<Candidate> cloneArrayList(ArrayList<Candidate> AL){
        ArrayList<Candidate> tempArrayList = new ArrayList<Candidate>();
        for(Candidate c:AL){
            tempArrayList.add(c);
        }
        return tempArrayList;
    }

    /**
     * Reads voter info from stdin and writes each voter to the voteList ArrayList
     * */
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

    /**
     * Reloads the candidate index hashmap with the latest information.
     * */
    private static void updateHashMap(){
        candidateLocation.clear();
        if(debug) System.out.println("Updating HashMap!");
        for(int i = 0; i < currentCandidateArrayList.size(); i++){
            candidateLocation.put(currentCandidateArrayList.get(i).name,i);
        }
    }

    /**
     * Converts the voter array into an actual election. Adds candidates
     * */
    private static void readBallotInfo(){
        for(int i = 0; i < voterList.size();i++) {
            String name = voterList.get(i).voteList.get(0);
            int index = findCandidate(name);
            if (index > -1) {
                currentCandidateArrayList.get(index).votes+=1;
                //ballot.put(name, ballot.get(name) + 1);
            } else {
                currentCandidateArrayList.add(new Candidate(name,1));
            }
        }
    }

    /**
     * Prints all votes.
     * */
    private static void printBallot(){
        for(int i = 0; i < voterList.size();i++){
            ArrayList<String> votes = voterList.get(i).voteList;
            for(int j = 0;j < votes.size();j++){
                System.out.print(votes.get(j)+ " ");
            }
            System.out.println();
        }
    }

    /**
     * finds the index of the current passed candidate.
     * */
    private static int findCandidate(String name){
        for(int i = 0; i < currentCandidateArrayList.size(); i++){
            if(currentCandidateArrayList.get(i).name.equals(name)){
                //if(debug) System.out.println("Found a matching candidate at index "+i);
                return i;
            }
        }
        return -1;
        //if(candidateLocation.containsKey(name)) return candidateLocation.get(name);
        //else return -1;
    }

    /**
     * Prints the current round.
     * */
    private static void printCurrentRound(){
        for(int i = 0; i < currentCandidateArrayList.size(); i++){
            System.out.println(currentCandidateArrayList.get(i));
        }
    }

    /**
     * uses collections to sort the candidate list based on number of votes.
     * */
    private static void sortCurrentRound(){
        Collections.sort(currentCandidateArrayList);
        if(debug) System.out.println("Sorting Round!");
        updateHashMap();
    }

}
