package classes;

public class Node {

    public String id;
    public int pos;
    public boolean available;
    public Node next;

    public Node(String id, int pos){
        this.id = id;
        this.pos = pos;
        available = false;
        next = null;
    }

    public Node(int pos){
        this.pos = pos;
        id = null;
        available = true;
        next = null;
    }

    public String toString(){
        return "#" + pos + ", available: " + available;
    }
}
