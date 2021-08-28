package Preference;

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

    protected static int maxNameLength = 0;

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
        maxNameLength = getMaxNameLength();
        if(debug) printCurrentRound();
        if(debug) System.out.println("================");
        sortCurrentRound();
        //if(debug) printCurrentRound();
        doElection();
        if(debug) System.out.println(gameHistory.size());
    }

    /**
     * The main election loop, is done recursively
     * */
    private static boolean doElection(){
        Collections.sort(currentCandidateArrayList);
        updateHashMap();
        saveRound();
        //recursiveDepth++;
        if(debug) System.out.println("==========");
        //if(recursiveDepth >10) System.exit(1);
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
                 String result = TIEBREAKER(candidatesWithDuplicateVotes);
                 if(result == null){
                     System.out.println("Unbreakable tie");
                     System.exit(0);
                 }else{
                     if(debug) System.out.println("found a loser! "+result);
                     int index = candidateLocation.get(result);
                     if(debug) System.out.println("Bob's index is: " +index);
                     redistributeVotes(currentCandidateArrayList.get(index));
                 }
                 updateHashMap();
            }


        }
        return false;
    }

    /**
     * Attempts to break a tie.
     * @param nameArrayList the names of candidates who are tying.
     * */
     private static String TIEBREAKER(ArrayList<String> nameArrayList){
        if(debug) System.out.println("In TIEBREAKER!");
        if(debug) System.out.println("game history size: " +gameHistory.size());
        if(gameHistory.size() > 0){
            for(int i = gameHistory.size()-1; i >= 0 ; i--){
                ArrayList<Candidate> tempCandidateArray = cloneArrayList(gameHistory.get(i));
                if(debug) System.out.println("in first level");
                if(debug) System.out.println("This previous round has "+tempCandidateArray.size()+" candidates.");
                if(debug) System.out.println("name array list size: "+nameArrayList.size());
                if(debug) System.out.println("Looking at previous round: "+ i);
                Collections.sort(tempCandidateArray);
                Collections.reverse(tempCandidateArray);
                if(debug)printGivenRound(tempCandidateArray);
                if(debug) printArrayList(nameArrayList);
                for(int j = 0; j < tempCandidateArray.size(); j++) {
                    for (int k = 0; k < tempCandidateArray.size(); k++) {
                        if (contains(nameArrayList, tempCandidateArray.get(j).name) && contains(nameArrayList, tempCandidateArray.get(k).name) && j != k) {
                            if (debug) System.out.println(tempCandidateArray.get(j).name + " is here!");
                            if (debug) System.out.println(tempCandidateArray.get(k).name + " is also here!");
                            if (tempCandidateArray.get(j).votes < tempCandidateArray.get(k).votes) {
                                String name = tempCandidateArray.get(j).name;
                                if (debug)
                                    System.out.println(name + " is this rounds loser, with a score of: " + tempCandidateArray.get(j).votes);
                                if (debug)
                                    System.out.println("His opponent " + tempCandidateArray.get(k).name + " had " + tempCandidateArray.get(k).votes + " votes!");
                                return name;
                            } else {
                                if (debug) System.out.println("Didn't find a resolution this round! Will continue!");
                                break;
                            }
                        } else {
                            if (debug)
                                System.out.println(tempCandidateArray.get(j).name + " isn't who we are looking for, neither is "+tempCandidateArray.get(k).name);
                        }
                    }
                }
            }
        }
        else{
            if(debug) System.out.println("We have no history!!!"); // Need to sort when there is a tie and we have no history to look through!
        }
        return null;

     }

     /**
      * Redistributes the votes of the passed in candidate.
      * @param c the candidate whos votes we are going to be dividing out.
      * */
    private static void redistributeVotes(Candidate c){
        if(debug) System.out.println(c.name +" has " +c.votes+ " votes!");
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
                    if(!addVote(tempCandidate, 1)){
                        i-=1; // if their next candidate has been removed... we give them another go...
                    }
                }else{ //If they don't have another next-best choice...
                    voterList.remove(i); //TODO we are removing the voter from the voting pool if they don't have another option... may not want to do this..?
                    i-=1;
                    if(debug) System.out.println("A Voter didn't have another option to choose, so they didn't vote...");
                }
            }else{ //Theoretically will be called if someone's second choice is not longer active...
                if(debug) System.out.println("didn't match!");
            }
        }
        if(c.votes==0){
            if(debug) System.out.println("Removing a candidate from the pool!");
            System.out.println("Eliminated: "+c.name+"\n");
            currentCandidateArrayList.remove(currentCandidateArrayList.size()-1);

            doElection();
        } else{
            if(debug) System.out.println("Error removing candidate from pool!, they still have votes assigned to them!");
        }
    }

    private static int getMaxNameLength(){
        int mLength = 0;
        for(Candidate c:currentCandidateArrayList){
            if(c.name.length() > mLength) mLength = c.name.length();
        }
        return mLength;
    }

    private static boolean contains(ArrayList<String> a, String s){
        for(int i = 0; i < a.size();i++){

            if(a.get(i).equals(s)){
                if(debug) System.out.println("Contains: "+a.get(i));
                return true;
            }
        }
        return false;

    }

    /**
     * addVote adds a number of votes to a candidate.
     *
     * @param name the name of the candidate to give the votes to.
     * @param numberOfVotes the number of votes to give the candidate.
     * */
    private static boolean addVote(String name,int numberOfVotes){
        if(debug) System.out.println("In adding vote!");
        if(candidateLocation.containsKey(name)){
            if(debug) System.out.println("in adding vote if statement, candidateLocation contains Key!");
            int index = candidateLocation.get(name);
            currentCandidateArrayList.get(index).votes+=numberOfVotes;
            return true;
        }else{
            if(debug) System.out.println("index is null in addVote! this means the hashmap doesn't have an index available for candidate: "+name);
        }
        return false;
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
            tempArrayList.add(new Candidate(c));
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
     * Prints a custom round.
     * */
    private static void printGivenRound(ArrayList<Candidate> a){
        if(debug) System.out.println("!+!+!+!+!+!+!+!");
        if(debug) System.out.println("Printing Custom Round!");
        for(int i = 0; i < a.size(); i++){
            System.out.println(a.get(i));
        }
    }

    /**
     * Prints a string ArrayList.
     * */
    private static void printArrayList(ArrayList<String> a){
        if(debug) System.out.println("!+!+!+!+!+!+!+!");
        if(debug) System.out.println("Printing Custom ArrayList!");
        for(int i = 0; i < a.size(); i++){
            System.out.println(a.get(i));
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
