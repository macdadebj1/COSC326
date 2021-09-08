package epidemic;

import java.util.ArrayList;

class Universe{

    protected ArrayList<ArrayList<Node>> nodes = new ArrayList<>();

    public Universe(){
    }

    public Universe(Universe u){
        if(epidemic.debug) System.out.println("In universe copy constructor");
        if(epidemic.debug) System.out.println("nodes.size: " +u.nodes.size());
        if(epidemic.debug) System.out.println("nodes.get(0).size(): "+u.nodes.get(0).size());
        for(int i = 0; i < u.nodes.size();i++){
            nodes.add(new ArrayList<Node>());
            for(int j = 0; j < u.nodes.get(i).size();j++){
                //if(epidemic.debug) System.out.println("copied!");
                nodes.get(i).add(new Node(u.nodes.get(i).get(j)));
            }
        }
    }
}