import java.util.HashMap;
/* Michel Robert
* TrieNode class for creating nodes of our trie*/
public class TrieNode {
    private Boolean endOfWord = false;
    HashMap<Character,TrieNode> children = new HashMap<>();
    //sets the current node to be the end of a word
    public void setEndOfWord(){
        endOfWord = true;
    }
    //checks if a node is the end of a word
    public Boolean isEndOfWord(){
        return endOfWord;
    }
    /*method for getting the hashmap containing any valid next letters in the
     * trie*/
    public HashMap<Character,TrieNode> getChildren(){
        return children;
    }

}
