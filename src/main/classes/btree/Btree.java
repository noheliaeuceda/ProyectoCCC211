/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.classes.btree;

import java.util.ArrayList;

public class Btree {

    private Node root;
    private int order;
    ArrayList ls = new ArrayList();

    public Btree(int k) {
        root = null;
        order = k;
    }

    public Node search(Comparable node) {
        if (root == null) {
            return null;
        }
        Node temp = root;
        for (;;) {
            int pos = temp.searchInNode(node);
            if (node.equals(temp.getElems()[pos])) {
                return temp;
            } else if (temp.isLeaf()) {
                return null;
            } else {
                temp = temp.getChilds()[pos];
            }
        }
    }

    public void insert(Comparable elem, FileIndex ind) {
        if (root == null) {
            root = new Node(order, elem, null, null, ind);
            return;
        }
        LinkedStack fathers = new LinkedStack();
        Node temp = root;
        for (;;) {
            int tempPos = temp.searchInNode(elem);
            if (elem.equals(temp.getElems()[tempPos])) {
                return;
            } else if (temp.isLeaf()) {
                ls.add(ind);
                temp.insertInNode(elem, null, null, tempPos, ind);
                if (temp.getSize() == order) {
                    splitNode(temp, fathers);
                }
                return;
            } else {
                fathers.push(new Integer(tempPos));
                fathers.push(temp);
                temp = temp.getChilds()[tempPos];
            }
        }
    }

    public FileIndex searchNode(Comparable elem) {
        if (root == null) {
            return null;
        }
        Node temp = root;
        for (;;) {
            int pos = temp.searchInNode(elem);
            if (elem.equals(temp.getElems()[pos])) {
                for (Object l : ls) {
                    if (elem.equals((((FileIndex) l).getId()))) {
                        return ((FileIndex) l);
                    }
                }
            } else if (temp.isLeaf()) {
                return null;
            } else {
                temp = temp.getChilds()[pos];
            }
        }
    }

    private void splitNode(Node node, LinkedStack fathers) {
        int medPos = node.getSize() / 2;
        Comparable med = node.getElems()[medPos];
        FileIndex nodeMed = searchNode(med);
        Node leftSib = new Node(order, node.getElems(), node.getChilds(), 0, medPos, nodeMed);
        Node rightSib = new Node(order, node.getElems(), node.getChilds(), medPos + 1, node.getSize(), nodeMed);
        if (node == root) {
            root = new Node(order, med, leftSib, rightSib, node.getIndice());
        } else {
            Node parent = (Node) fathers.pop();
            int parentIns = ((Integer) fathers.pop()).intValue();
            FileIndex medNode = searchNode(med);
            parent.insertInNode(med, leftSib, rightSib, parentIns, medNode);
            if (parent.getSize() == order) {
                splitNode(parent, fathers);
            }
        }
    }

    public void delete(Comparable elem) {
        // Delete element elem from this B-tree.
        if (root == null) {
            return;
        }
        LinkedStack ancestors = new LinkedStack();
        Node curr = root;
        int currPos;
        for (;;) {
            currPos = curr.searchInNode(elem);
            if (elem.equals(curr.getElems()[currPos])) {
                break;
            } else if (curr.isLeaf()) {
                return;
            } else {
                ancestors.push(new Integer(currPos));
                ancestors.push(curr);
                curr = curr.getChilds()[currPos];
            }
        }
        if (curr.isLeaf()) {
            curr.removeFromNode(currPos, currPos);
            if (underflowed(curr)) {
                restock(curr, ancestors);
            }
        } else {
            Node leftmostNode = findLeftmostNode(curr.getChilds()[currPos + 1], ancestors);
            Comparable nextElem = leftmostNode.getElems()[0];
            leftmostNode.removeFromNode(0, 0);
            Node temp = root;
            System.out.println(temp);
            curr.getElems()[currPos] = nextElem;
            if (underflowed(leftmostNode)) {
                restock(leftmostNode, ancestors);
            }
        }
    }

    /*

    public void delete(Comparable elem) {
        if (root == null) {
            return;
        }
        LinkedStack fathers = new LinkedStack();
        Node temp = root;
        int tempPos;
        for (;;) {
            tempPos = temp.searchInNode(elem);
            if (elem.equals(temp.getElems()[tempPos])) {
                break;
            } else if (temp.isLeaf()) {
                return;
            } else {
                fathers.push(new Integer(tempPos));
                fathers.push(temp);
                System.out.println(fathers+" ???");
                temp = temp.getChilds()[tempPos];
            }
        }
        if (temp.isLeaf()) {
            temp.removeFromNode(tempPos, tempPos);
            
            if (underflowed(temp)) {
                restock(temp, fathers);
            }
        } else {
            Node leftmostNode = findLeftmostNode(temp.getChilds()[tempPos + 1], fathers);
            Comparable nextElem = leftmostNode.getElems()[0];
            leftmostNode.removeFromNode(0, 0);
            temp.getElems()[tempPos] = nextElem;
            if (underflowed(leftmostNode)) {
                restock(leftmostNode, fathers);
            }
        }
    }*/
    private void restock(Node node, LinkedStack fathers) {
        if (node == root) {  // node.size == 0
            root = node.getChilds()[0];
            return;
        }
        Node parent = (Node) fathers.pop();
        int childPos = 0;
        while (parent.getChilds()[childPos] != node && childPos < 5) {
            childPos++;
        }
        int sibMinSize = (order - 1) / 2;
        if (childPos > 0&&parent.getChilds()[childPos - 1] != null  && parent.getChilds()[childPos - 1].getSize() > sibMinSize) {
            Node sib = parent.getChilds()[childPos - 1];
            Comparable parentElem = parent.getElems()[childPos - 1];
            Comparable spareElem = sib.getElems()[sib.getSize() - 1];
            Node spareChild = sib.getChilds()[sib.getSize()];
            sib.removeFromNode(sib.getSize() - 1, sib.getSize());
            FileIndex medNode = searchNode(parentElem);
            node.insertInNode(parentElem, spareChild, node.getChilds()[0], 0, medNode);
            parent.getElems()[childPos - 1] = spareElem;
        } else if (parent.getChilds()[childPos - 1] != null && childPos < parent.getSize() && parent.getChilds()[childPos + 1].getSize() > sibMinSize) {
            Node sib = parent.getChilds()[childPos + 1];
            Comparable parentElem = parent.getElems()[childPos];
            Comparable spareElem = sib.getElems()[0];
            Node spareChild = sib.getChilds()[0];
            sib.removeFromNode(0, 0);
            FileIndex medNode = searchNode(parentElem);
            node.insertInNode(parentElem, node.getChilds()[node.getSize()], spareChild, node.getSize(), medNode);
            parent.getElems()[childPos] = spareElem;
        } else if (childPos > 0 && parent.getChilds()[childPos - 1] != null) {
            Node sib = parent.getChilds()[childPos - 1];
            Comparable parentElem = parent.getElems()[childPos - 1];
            node.nodeLeft(sib, parentElem);
            parent.removeFromNode(childPos - 1, childPos - 1);
            if (underflowed(parent)) {
                restock(parent, fathers);
            }
        } else {  // childPos < parent.size
            System.out.println(childPos);
            while (childPos <= 5) {
                try {
                    Node sib = parent.getChilds()[childPos + 1];
                    Comparable parentElem = parent.getElems()[childPos];
                    node.nodeRight(parentElem, sib);
                    parent.removeFromNode(childPos, childPos + 1);
                    if (underflowed(parent)) {
                        restock(parent, fathers);
                    }

                } catch (Exception e) {
                }
                childPos--;
            }

        }
    }

    private void removeFromNode(Node node, int elemPos, int childPos) {
        for (int i = elemPos; i < node.getSize(); i++) {
            node.getElems()[i] = node.getElems()[i + 1];
        }
        if (!node.isLeaf()) {
            for (int i = childPos; i < node.getSize(); i++) {
                node.getChilds()[i] = node.getChilds()[i + 1];
            }
        }
        node.setSize(node.getSize() - 1);;
    }

    private Node findLeftmostNode(Node top, LinkedStack fathers) {
        Node temp = top;
        while (!temp.isLeaf()) {
            fathers.push(new Integer(0));
            fathers.push(temp);
            temp = temp.getChilds()[0];
        }
        return temp;
    }

    private boolean underflowed(Node node) {
        int minSize = (node == root ? 1 : (order - 1) / 2);
        return (node.getSize() < minSize);
    }

    public void print() {
        printSubtree(root, "");
    }

    private static void printSubtree(Node top, String indent) {
        if (top == null) {
            System.out.println(indent + "");
        } else {
            System.out.println(indent + "-->");
            boolean isLeaf = top.isLeaf();
            String childIndent = indent + "    ";
            for (int i = 0; i < top.getSize(); i++) {
                if (!isLeaf) {
                    printSubtree(top.getChilds()[i], childIndent);
                }
                System.out.println(childIndent + top.getElems()[i]);
            }
            if (!isLeaf) {
                printSubtree(top.getChilds()[top.getSize()], childIndent);
            }
        }
    }

}
