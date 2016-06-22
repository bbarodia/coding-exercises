package com.uber;

// Input: String word, List<String> glossary
// Output: % match
// 'catdog', ['dog', 'frog', 'cat']    =>    100%
// 'cardog', ['fog', 'card', 'cat']    =>    50%

import java.util.ArrayList;
import java.util.List;

public class WordMatch {

    public static void main(String args[]) {

        String word = "cardog";
        List<String> glossaryItems = new ArrayList<String>();
        glossaryItems.add("dog");
        glossaryItems.add("cat");
        Trie glossaryItemsTrie = new Trie(glossaryItems);
        System.out.println("Word match is " + glossaryItemsTrie.getMatch(word) + "%");
    }
}

class Trie {

    Node root;

    public Trie(List<String> glossary) {
        root = new Node('/');
        for (String word : glossary) {
            Node start = root;
            for (int i = 0; i < word.length(); i++) {
                char value = word.charAt(i);
                if (start.containsValue(value)) {
                    continue;
                } else {
                    start.addChild(value);
                }
                start = start.getNode(value);
            }
        }
    }

    public int searchWordAtRoot(String word) {
        Node root = this.root;
        for (int index = 0; index < word.length(); index++) {
            System.out.println("Checking " + word + " index " + index);
            if (root.containsValue(word.charAt(index))) {
                root = root.getNode(word.charAt(index));
            } else if (root.isLeaf()) {
                return index;
            } else {
                return -1;
            }
        }
        if (root.isLeaf()) {
            return word.length();
        }
        return -1;
    }

    public float getMatch(String word) {
        int unMatchedCharCount = 0;
        for (int index = 0; index < word.length(); ) {
            int newIndex = 0;
            newIndex = searchWordAtRoot(word.substring(index, word.length()));
            if (newIndex < 0) {
                index++;
                unMatchedCharCount++;
            } else {
                index = index + newIndex;
            }
            System.out.println("Unmatched char count" + unMatchedCharCount);
        }
        return 100 - ((float)unMatchedCharCount / word.length()) * 100;
    }
}

class Node {

    char value;
    List<Node> children;
    boolean isLeaf = true;

    Node(char value) {
        this.value = value;
        this.isLeaf = true;
        this.children = new ArrayList<Node>();
    }

    public void addChild(char value) {
        Node newChild = new Node(value);
        this.isLeaf = false;
        this.children.add(newChild);
    }

    public boolean containsValue(char value) {
        for (Node child : children) {
            if (child.value == value) {
                return true;
            }
        }
        return false;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public Node getNode(char value) {
        for (Node child : children) {
            if (child.value == value) {
                return child;
            }
        }
        return null;
    }
}
