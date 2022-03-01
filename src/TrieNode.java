
public class TrieNode {
    private Boolean endOfWord = false;
    TrieNode[] children = new TrieNode[26];
    public void setEndOfWord(){
        endOfWord = true;
    }

    public Boolean isEndOfWord(){
        return endOfWord;
    }

    public void addChildren(int index){
        children[index] = new TrieNode();
    }

    public TrieNode[] getChildren(int index){
        return children;
    }
    public TrieNode getChild(int index){
        return children[index];
    }
}
