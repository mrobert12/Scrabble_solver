import java.util.HashMap;

public class Trie {
    public void insert(String word){
        TrieNode node = new TrieNode();
        HashMap<Character,TrieNode> children = node.getChildren();
        char ch;

        for(int i = 0; i < word.length();i++){
            ch = word.charAt(i);

        }
    }
}
