import java.util.HashMap;
/* Michel Robert
Trie is our dictionary structure*/
public class Trie {
    TrieNode root = new TrieNode();
    /* insert takes a word and loops over the word and traverses the trie
    * if the trie has nodes that correspond to the letter then it continues
    * through the word if there is no node it creates a new one and places it on
    * the trie when it gets to the end of the word it sets that nodes endOfWord
    * Boolean to true*/
    public void insert(String word){
        HashMap<Character,TrieNode> children = root.getChildren();
        TrieNode node = root;
        char ch;
        for(int i = 0; i < word.length();i++){
            ch = Character.toLowerCase((word.charAt(i)));
            if(children.containsKey(ch)){
                node = children.get(ch);
            }
            else{
                node = new TrieNode();
                children.put(ch,node);
            }
            children = node.getChildren();
        }
        node.setEndOfWord();
    }
    /* method for searching the trie for dictionary words returns true only if
    * the node at the end of the word has an end of word value of true.*/
    public Boolean search(String word){
        HashMap<Character,TrieNode> children = root.getChildren();
        TrieNode node = root;
        char ch;
        for(int i = 0; i < word.length();i++){
            ch = word.charAt(i);
            if(children.containsKey(ch)){
                node = children.get(ch);
                children = node.getChildren();
            }
            else{
                return false;
            }
        }
        return node.isEndOfWord();
    }
    /* returns a trieNode after traversing the trie given a prefix*/
    public TrieNode getNode(String prefix){
        HashMap<Character,TrieNode> children = root.getChildren();
        TrieNode node = root;
        char ch;
        for(int i = 0; i < prefix.length();i++){
            ch = prefix.charAt(i);
            if(children.containsKey(ch)){
                node = children.get(ch);
                children = node.getChildren();
            }
        }
        return node;
    }
    /* method for getting the root of our trie*/
    public TrieNode getRoot(){
        return root;
    }
}
