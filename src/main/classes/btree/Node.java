/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.classes.btree;

/**
 *
 * @author euced
 */
public class Node {

    private int size;
    private Comparable[] elems;
    private Node[] childs;
    private FileIndex indice;

    public Node(int k, Comparable elem, Node left, Node right, FileIndex indice) {
        elems = new Comparable[k];
        childs = new Node[k + 1];
        this.size = 1;
        this.elems[0] = elem;
        this.childs[0] = left;
        this.childs[1] = right;
        this.indice = indice;
    }

    public Node(int k, Comparable[] elems, Node[] childs, int l, int r, FileIndex indice) {
        this.elems = new Comparable[k];
        this.childs = new Node[k + 1];
        this.size = 0;
        for (int j = l; j < r; j++) {
            this.elems[this.size] = elems[j];
            this.childs[this.size] = childs[j];
            this.size++;
        }
        this.childs[this.size] = childs[r];
        this.indice = indice;
    }

    public FileIndex getIndice() {
        return indice;
    }

    public void setIndice(FileIndex indice) {
        this.indice = indice;
    }
    

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Comparable[] getElems() {
        return elems;
    }

    public void setElems(Comparable[] elems) {
        this.elems = elems;
    }

    public Node[] getChilds() {
        return childs;
    }

    public void setChilds(Node[] childs) {
        this.childs = childs;
    }
    

    public boolean isLeaf() {
        return (childs[0] == null);
    }

    public int searchInNode(Comparable node) {
        int l = 0, r = size - 1;
        while (l <= r) {
            int m = (l + r) / 2;
            int comp = node.compareTo(elems[m]);
            if (comp == 0) {
                return m;
            } else if (comp < 0) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return l;
    }

    public void insertInNode(Comparable elem, Node leftChild, Node rightChild, int ins, FileIndex ind) {
        for (int i = size; i > ins; i--) {
            elems[i] = elems[i - 1];
            childs[i + 1] = childs[i];
        }
        size++;
        elems[ins] = elem;
        childs[ins] = leftChild;
        childs[ins + 1] = rightChild;
        indice = ind;
    }

    public void nodeLeft(Node that, Comparable elem) {
        System.arraycopy(this.elems, 0, this.elems, that.size + 1, this.size);
        System.arraycopy(this.childs, 0, this.childs, that.size + 1, this.size + 1);
        System.arraycopy(that.elems, 0, this.elems, 0, that.size);
        this.elems[that.size] = elem;
        System.arraycopy(that.childs, 0, this.childs, 0, that.size + 1);
        this.size += that.size + 1;
    }

    public void nodeRight(Comparable elem, Node that) {
        this.elems[this.size] = elem;
        System.arraycopy(that.elems, 0, this.elems, this.size + 1, that.size);
        System.arraycopy(that.childs, 0, this.childs, this.size + 1, that.size + 1);
        this.size += that.size + 1;
    }

    public void removeFromNode(int elemPos, int childPos) {
        // Remove from this node the element at position elemPos, and the child 
        // at position childPos.
        for (int i = elemPos; i < size; i++) {
            elems[i] = elems[i + 1];
        }
        if (!isLeaf()) {
            for (int i = childPos; i < size; i++) {
                childs[i] = childs[i + 1];
            }
        }
        size--;
    }

    @Override
    public String toString() {
        return "Node{" + "indice=" + indice + '}';
    }
    

}
