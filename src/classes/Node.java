package classes;

public class Node {

    public Person content;
    public int pos;
    public boolean available;
    public Node next;

    public Node(Person content, int pos){
        this.content = content;
        this.pos = pos;
        available = false;
        next = null;
    }

    public Node(int pos){
        this.pos = pos;
        content = null;
        available = true;
        next = null;
    }
}
