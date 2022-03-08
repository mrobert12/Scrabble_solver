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
        ArrayList<Space> anchors = board.getAnchors();
        for(int i = 0;i< anchors.size();i++){
            Space anchor = anchors.get(i);
            LeftPart(anchor);
        }
    }
    public void LeftPart(Space anchor){
        int x = anchor.getxCoordinate();
        int y = anchor.getyCoordinate();
        if(board.getSpace(x,y).getTile() == null){

        }
    }
}
