public class Trie {

    public class TrieNode{
        TrieNode[] nodes = new TrieNode[26];
        Boolean endOfWord;

        TrieNode(){
            for(int i = 0;i < 26;i++){
                endOfWord = false;
                nodes[i] = null;
            }
        }
    }
    public void insert(String word){
        TrieNode node = new TrieNode();
        char ch;
        int alphabetLocation;
        for(int i = 0; i < word.length();i++){
            ch = word.charAt(i);
            alphabetLocation = ch - 'a';
            if(node.nodes[alphabetLocation] == null){

            }

        }
    }
}
