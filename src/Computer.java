import java.util.ArrayList;
import java.util.HashMap;

public class Computer {
    ArrayList<Tile> hand = new ArrayList<>();
    ArrayList<Character> charHand = new ArrayList<>();
    Board board;
    Trie trie;
    int [] values;
    int highScore;
    Board highBoard;
    String highWord;
    public void addTile(Tile tile){
        hand.add(tile);
    }

    public Computer(Trie trie,int[] values){
        this.trie = trie;
        this.values = values;
    }

    public void solver(Board board){
        this.board = board;
        handToCharArray();
        ArrayList<Space> anchors = board.getAnchors();
        allWords("",trie.getRoot());
        for(Space space : anchors){
            int row = space.getRow();
            int col = space.getCol();

        }
    }

    public void allWords(String partialWord,TrieNode node){
        HashMap<Character,TrieNode> children = node.getChildren();
        if(node.isEndOfWord()){
            System.out.println(partialWord);
        }
        children.forEach((key,value) ->{
            if(charHand.contains(key)){
                charHand.remove(key);
                allWords(partialWord+key,children.get(key));
                charHand.add(key);
            }
        });
    }

    public void handToCharArray(){
        for (Tile tile : hand) {
            charHand.add(tile.getLetter());
        }
    }
}
