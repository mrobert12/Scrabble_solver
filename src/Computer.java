import java.util.ArrayList;
import java.util.HashMap;

public class Computer {
    ArrayList<Tile> hand = new ArrayList<>();
    ArrayList<Character> charHand = new ArrayList<>();
    Board board;
    Trie trie;
    int [] values;

    public void addTile(Tile tile){
        hand.add(tile);
    }

    public void Solver(Board board,Trie trie,int[] values){
        this.board = board;
        this.trie = trie;
        this.values = values;
        TrieNode root = trie.getRoot();
        ArrayList<Space> anchors = board.getAnchors();
        for(int i = 0; i < anchors.size();i++){
            StringBuilder partialWord = new StringBuilder("");
            int row = anchors.get(i).getRow();
            int col = anchors.get(i).getCol();
            String prefix = leftPrefix(partialWord,row,col);
            TrieNode node = trie.getNode(prefix);
            allWords(prefix,node);
        }
    }

    public void allWords(String partialWord, TrieNode node){
        HashMap<Character,TrieNode> children = node.getChildren();
        if(node.isEndOfWord()){
            System.out.println(partialWord);
        }
        children.forEach((key,value) ->{
            if(charHand.contains(key)){
                charHand.remove(key);
                allWords(partialWord + key,children.get(key));
                charHand.add(key);
            }
        });
    }

    public String leftPrefix(StringBuilder partialWord,int row ,int col){
        if(col == 0){
            return partialWord.toString();
        }
        else if(!board.emptySpace(board.getLeft(row,col))){
            partialWord.insert(0,board.getLeft(row,col).getTile().getLetter());
            leftPrefix(partialWord,row,col - 1);
        }
        return partialWord.toString();
    }

    public void handToCharArray(){
        for (Tile tile : hand) {
            charHand.add(tile.getLetter());
        }
    }
}
