public class Candidate implements Comparable<Candidate>{

    public int votes;
    public String name;

    public Candidate(String name, int votes){
        this.name = name;
        this.votes = votes;
    }

    /**
     * Copy constructor.
     * */
    public Candidate(Candidate c){
        this.name = c.name;
        this.votes = c.votes;
    }

    public String toString(){
        //return String.format("%-11s %s",name,votes);
        return String.format("%s  %s",name,votes);
    }

    /**
     * Custom comparitor, used by collections to sort the candidate ArrayList based on number of votes.
     * */
    @Override
    public int compareTo(Candidate c){
        if(c.votes > this.votes){
            return 1;
        }
        else if(c.votes < this.votes){
            return -1;
        }
        return 0;
    }

}