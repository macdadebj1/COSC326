public class Candidate implements Comparable<Candidate>{

    public int votes;
    public String name;

    public Candidate(String name, int votes){
        this.name = name;
        this.votes = votes;
    }

    public String toString(){
        return name +"\t"+votes;
    }

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