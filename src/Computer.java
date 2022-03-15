import java.util.ArrayList;

public class Computer {
    ArrayList<Tile> hand = new ArrayList<>();
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
        for(int i = 0;i< anchors.size();i++){
            Space anchor = anchors.get(i);
            LeftPart(anchor,root,anchor.getxCoordinate()
                    ,anchor.getyCoordinate());
        }
    }

    public void LeftPart(Space anchor,TrieNode node,int x ,int y){

        Tile middle = anchor.getTile();



    }
}
