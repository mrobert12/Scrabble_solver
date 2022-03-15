import java.util.HashMap;

public class Trie {
    TrieNode root = new TrieNode();

    public void insert(String word){
        HashMap<Character,TrieNode> children = root.getChildren();
        TrieNode node = root;
        char ch;
        for(int i = 0; i < word.length();i++){
            ch = word.charAt(i);
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

    public TrieNode getRoot(){
        return root;
    }
}
