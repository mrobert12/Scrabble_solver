import java.util.ArrayList;

public class Board {
    int boardSize;
    Space[][] spaces;
    ArrayList<Space> anchors = new ArrayList<>();
    public Board(int boardSize){
        this.boardSize = boardSize;
        spaces = new Space[boardSize][boardSize];
        for(int i = 0; i < boardSize;i++){
            for(int j = 0; j < boardSize;j++){
                spaces[i][j] = null;
            }
        }
    }
    public void setAnchors(){
        for(int i = 0;i < boardSize;i++){
            for(int j = 0; j < boardSize;j++){
                if(spaces[i][j].getTile() != null){
                    if(i != 0 && spaces[i - 1][j].getTile() == null){
                        if(!anchors.contains(spaces[i - 1][j])) {
                            anchors.add(spaces[i - 1][j]);
                        }
                    }
                    if(i != 6 && spaces[i + 1][j].getTile() == null){
                        if(!anchors.contains(spaces[i + 1][j])) {
                            anchors.add(spaces[i + 1][j]);
                        }
                    }
                    if(j != 0 && spaces[i][j - 1].getTile() == null){
                        if(!anchors.contains(spaces[i][j - 1])) {
                            anchors.add(spaces[i][j - 1]);
                        }
                    }
                    if(j != 6 && spaces[i][j + 1].getTile() == null){
                        if(!anchors.contains(spaces[i][j + 1])) {
                            anchors.add(spaces[i][j + 1]);
                        }
                    }
                }
            }
        }
    }
    public void printAnchors(){
        for(int i = 0; i < anchors.size();i++){
            System.out.println(anchors.get(i).getxCoordinate() + " " + anchors.get(i).getyCoordinate());
        }
    }

    public void addSpace(Space space,int x,int y){
        spaces[x][y] = space;
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
    public int getSize(){
        return boardSize;
    }
}
