package application;

import java.util.HashMap;

public class Trie {
    private class TrieNode {
        private char character;
        
        private HashMap<Character, TrieNode> adjList;
        
        private boolean isEndOfWords;
        
        public TrieNode(char character){
            this.character = character;
            this.isEndOfWords = true;
            this.adjList = new HashMap<>();
        }

        public HashMap<Character, TrieNode> getChildren() {
            return this.adjList;
        }

        public boolean isEndOfWords() {
            return this.isEndOfWords;
        }

        public void setEndOfWords(boolean isLeaf) {
            this.isEndOfWords = isLeaf;
        }
    }
    
    private TrieNode root;
    
    public Trie() {
    }
    
    public void insert(String str) {
        if(str != null && !str.equals("")) {
            if(this.root == null) {
                
            }
            for(int i = 1; i < str.length(); i++) {
                
            }
        }
    }
}
