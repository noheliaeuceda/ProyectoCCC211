package classes;

public class AvailList {

    public boolean init;
    public Node first;
    public Node last;
    public int removed;
    public int size;

    public AvailList(){
        init = true;
        first = null;
        last = null;
        removed = 0;
        size = 0;
    }

    public boolean isEmpty(){
        return first == null || (first == last && first.available);
    }

    public int add(Person person) {
        Node temp;
        Node newNode;
        if (person == null) {
            newNode = new Node(size);
            removed++;
        } else {
            newNode = new Node(person, size);
        }

        if (isEmpty()) {
            first = last = newNode;
            size++;
        } else if (removed == 0 || init) {
            last.next = newNode;
            last = newNode;
            size++;
        } else {
            temp = first;
            while (temp != null){
                if (temp.available){
                    temp.content = person;
                    temp.available = false;
                    removed--;
                    return temp.pos;
                }
                temp = temp.next;
            }
        }
        return -1;
    }

    public void remove(Person person){
        Node temp;
        if (!isEmpty()) {
            temp = first;
            while (temp != null){
                if (!temp.available && temp.content == person){
                    temp.available = true;
                    removed++;
                    break;
                }
                temp = temp.next;
            }
        }
    }

}
