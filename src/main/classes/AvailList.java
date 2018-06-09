package main.classes;

import java.io.RandomAccessFile;

public class AvailList {

    public Node first;
    public Node last;
    public int size;

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

    public AvailList(int pos, int fieldLength, String filename) {
        first = last = null;
        size = 0;
        if (pos != -1)
            load(filename, pos, fieldLength);
    }

    public void load(String filename, int pos, int fieldLength) {
        try {
            RandomAccessFile raFile = new RandomAccessFile(filename, "r");
            int nextPos = pos;
            while (nextPos != -1) {
                add(nextPos);
                raFile.seek(0);
                raFile.seek(fieldLength * nextPos);
                nextPos = Integer.valueOf(raFile.readUTF().trim());
            }
            raFile.close();
        } catch (Exception e) { }
    }

    public boolean isEmpty(){
        return first == null;
    }

    public void add(int pos) {
        if (isEmpty()) {
            first = last = new Node(pos);
            size = 1;
        } else {
            last.next = new Node(pos);
            size++;
        }
    }

    public int remove() {
        // only the first element of the list can be removed
        int head = first.pos;
        if (size == 1) {
            first = last = null;
        } else {
            Node save = first;
            first = first.next;
            save.next = null;
        }
        size--;
        return head;
    }

    public Node at(int pos) {
        // get a node from the list at a given position
        Node temp = first;
        if (pos >= 0 && pos < size)
            for (int i = 0; i < size; i++)
                if (i == pos)
                    return temp;
                else
                    temp = temp.next;
        return null;
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
