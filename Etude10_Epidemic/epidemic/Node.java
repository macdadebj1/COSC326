package epidemic;

public class Node{

    Node left;
    Node right;
    Node above;
    Node below;
    NodeState state;

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