import java.util.ArrayList;

public class Board {
    int boardSize;
    Space[][] spaces;
    ArrayList<Space> anchors;
    public Board(int boardSize){
        this.boardSize = boardSize;
        spaces = new Space[boardSize][boardSize];
        for(int i = 0; i < boardSize;i++){
            for(int j = 0; j < boardSize;j++){
                spaces[i][j] = null;
            }
        }
    }
    public void addSpace(Space space,int x,int y){
        spaces[x][y] = space;
    }
    public void firstAnchors(ArrayList<Space> anchors){
        this.anchors = anchors;
    }
    public void addAnchor(Space space){
        anchors.add(space);
    }
    public ArrayList<Space> getAnchors(){
        return anchors;
    }
    public Space getSpace(int x,int y){
        return spaces[x][y];
    }
}
