import java.util.HashMap;

public class TrieNode {
    private Boolean endOfWord = false;
    HashMap<Character,TrieNode> children = new HashMap<>();

    public void setEndOfWord(){
        endOfWord = true;
    }

    public Boolean isEndOfWord(){
        return endOfWord;
    }

    public void addChildren(HashMap<Character,TrieNode> children){
        this.children = children;
    }

    public HashMap<Character,TrieNode> getChildren(){
        return children;
    }

}
