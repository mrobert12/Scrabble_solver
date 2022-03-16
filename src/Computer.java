import java.util.ArrayList;
import java.util.HashMap;

public class Computer {
    ArrayList<Tile> hand = new ArrayList<>();
    ArrayList<Character> charHand = new ArrayList<>();
    Board board;
    Trie trie;
    int [] values;
    String highWord;


    public void addTile(Tile tile){
        hand.add(tile);
    }

    public void Solver(Board board,Trie trie,int[] values){
        this.board = board;
        this.trie = trie;
        this.values = values;
        TrieNode root = trie.getRoot();
        ArrayList<Space> anchors = board.getAnchors();
        for(int i = 0;i< anchors.size();i++){
            Space anchor = anchors.get(i);
            int x = anchor.getxCoordinate() + 1;
            int y = anchor.getyCoordinate();
            String right = "";
            while(x < board.getSize()){
                if(board.getSpace(x,y).getTile() == null) {
                    break;
                }
                else{
                    right += board.getSpace(x,y).getTile().getLetter();
                }
                x++;
            }
            LeftPart(anchor,root, anchor.getxCoordinate(),
                    anchor.getyCoordinate(),right);
        }
    }

    public void allWords(String partialWord, TrieNode node){
        HashMap<Character,TrieNode> children = node.getChildren();
        if(node.isEndOfWord()){
            System.out.println("Found Word: " + partialWord);
        }
        children.forEach((key,value) ->{
            if(charHand.contains(key)){
                charHand.remove(key);
                allWords(partialWord + key,children.get(key));
                charHand.add(key);
            }
        });
    }
    public void LeftPart(Space anchor,TrieNode node,int x ,int y,String right){

    }
    public void handToCharArray(){
        for(int i = 0; i < hand.size();i++){
            charHand.add(hand.get(i).getLetter());
        }
    }
}
