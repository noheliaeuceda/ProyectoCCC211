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

    public int add(Record record) {
        Node temp;
        Node newNode;
        if (record == null) {
            newNode = new Node(size);
            removed++;
        } else {
            newNode = new Node(record.getPK(), size);
        }

        if (init && first != null && first.available){
            last.next = newNode;
            last = newNode;
            size++;
        } else if (isEmpty()) {
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
                    temp.id = record.getPK();
                    temp.available = false;
                    removed--;
                    return temp.pos;
                }
                temp = temp.next;
            }
        }
        return -1;
    }

    public void remove(Record record){
        Node temp;
        if (!isEmpty()) {
            temp = first;
            while (temp != null){
                if (!temp.available && temp.id.equals(record.getPK())){
                    temp.available = true;
                    removed++;
                    break;
                }
                temp = temp.next;
            }
        }
    }

    public void print(){
        StringBuilder result = new StringBuilder("Avail List:\n");
        Node temp = first;
        while (temp != null) {
            result.append(temp.toString());
            result.append('\n');
            temp = temp.next;
        }
        System.out.println(result.toString());
    }

}
