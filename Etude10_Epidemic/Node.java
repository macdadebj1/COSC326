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
}