package com.company;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class LinkNodes {

    public static void main(String args[]) {
        Node root = createTree();
        transform(root);
        root.print();
        root.leftChild.print();
        root.leftChild.leftChild.print();
        root.leftChild.leftChild.leftChild.print();
    }

    private static void transform(final Node root) {
        Queue<Node> queue1 = new ArrayBlockingQueue<Node>(100);
        Queue<Node> queue2 = new ArrayBlockingQueue<Node>(100);
        queue1.add(root);

        while (!queue1.isEmpty()) {
            Node curr = queue1.remove();
            queue2.add(curr);
            if (curr != null && curr.hasChildren()) {
                queue1.add(curr.leftChild);
                queue1.add(curr.rightChild);
            }

        }

        Node start = queue2.remove();
        Node prev = start;
        while (!queue2.isEmpty()) {
            Node curr = queue2.remove();
            if (start.leftChild != curr) {
                if (prev != null ) {
                    prev.rightNode = curr;
                }
                prev = curr;
            } else {
                start = curr;
                prev = start;
            }
        }
    }

    private static Node createTree() {
        Node h = new Node('H', null, null);
        Node i = new Node('I', null, null);
        Node j = new Node('J', null, null);
        Node k = new Node('K', null, null);
        Node l = new Node('L', null, null);
        Node m = new Node('M', null, null);
        Node n = new Node('N', null, null);
        Node o = new Node('O', null, null);
        Node d = new Node('D', h, i);
        Node e = new Node('E', j, k);
        Node f = new Node('F', l, m);
        Node g = new Node('G', n, o);

        Node b = new Node('B', d, e);
        Node c = new Node('C', f, g);

        Node a = new Node('A', b, c);

        return a;
        // create a 4 level tree
        //          A
       //    B                 C
   //     D       E          F     G
  //  H     I  J    K      L   M  N   O
    }
}

class Node {

    public Node leftChild;
    public Node rightChild;
    public Node rightNode;
    public char value;
    public boolean visited;

    public Node(char value, Node leftChild, Node rightChild) {
        this.value = value;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public void print() {
        System.out.print(" " + this.value);
        if (this.rightNode != null) {
            System.out.print(" -> ");
            this.rightNode.print();
        } else {
            System.out.println();
        }
    }

    public boolean hasChildren() {
        if (rightChild != null && leftChild != null) {
            return true;
        }
        return false;
    }
}
