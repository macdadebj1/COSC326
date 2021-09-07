package epidemic;

public class Node{

    protected Node left;
    protected Node right;
    protected Node above;
    protected Node below;
    protected NodeState state;

    public Node(NodeState s){
        this.state = s;
    }
    public Node(){
        this.state = NodeState.VULNERABLE;
    }

    public String toString(){
        if(state == NodeState.VULNERABLE) return ".";
        else if(state == NodeState.SICK) return "S";
        else if(state == NodeState.IMMUNE) return "I";
        else return"!";
    }
}