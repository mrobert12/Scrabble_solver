

public class Trie {
    TrieNode root = new TrieNode();

    public void insert(String word){
        TrieNode node = root;
        char ch;
        for(int i = 0; i < word.length();i++){
            ch = word.charAt(i);
            int index = ch - 'a';
            if(node.getChild(index) == null){
                node.addChildren(index);
            }
            node = node.getChild(index);
        }
        node.setEndOfWord();
    }
}
