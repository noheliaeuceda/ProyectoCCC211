package classes;

public class Node {

    public int pos;
    public Node next;

    public Node(int pos){
        this.pos = pos;
        next = null;
    }

    public String toString(){
        return "#" + pos;
    }
}
