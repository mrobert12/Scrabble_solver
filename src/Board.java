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
                    if(i > 0 && getUp(i,j).getTile() == null){
                        if(!anchors.contains(getUp(i,j))){
                            anchors.add(getUp(i,j));
                        }
                    }
                    if(i < 6 && getDown(i,j).getTile() == null){
                        if(!anchors.contains(getDown(i,j))) {
                            anchors.add(getDown(i,j));
                        }
                    }
                    if(j > 0 && getLeft(i,j).getTile() == null){
                        if(!anchors.contains(getLeft(i,j))) {
                            anchors.add(getLeft(i,j));
                        }
                    }
                    if(j < 6 && getRight(i,j).getTile() == null){
                        if(!anchors.contains(getRight(i,j))) {
                            anchors.add(getRight(i,j));
                        }
                    }
                }
            }
        }
    }
    public void printAnchors(){
        for (Space anchor : anchors) {
            System.out.println(anchor.getRow() + " " + anchor.getCol());
        }
    }
    public Boolean emptySpace(Space space){
        return space.getTile() == null;
    }
    public void addSpace(Space space,int row,int col){
        spaces[row][col] = space;
    }

    public void addAnchor(Space space){
        anchors.add(space);
    }
    public ArrayList<Space> getAnchors(){
        return anchors;
    }
    public Space getSpace(int row,int col){
        return spaces[row][col];
    }
    public int getSize(){
        return boardSize;
    }

    public void printBoard(){
        for(int i = 0; i < boardSize;i++){
            for(int j = 0; j< boardSize;j++){
                Space space = spaces[i][j];
                if(j > 0){
                    System.out.print(" ");
                }
                if(space.getTile() != null){
                    System.out.print(" " + space.getTile().getLetter());
                }
                else if(space.getTileMult() == 1 && space.getWordMult() == 1){
                    System.out.print("..");
                }
                else if(space.getTileMult() != 1){
                    System.out.print("." + space.getTileMult());
                }
                else if(space.getWordMult() != 1){
                    System.out.print(space.getWordMult() + ".");
                }
            }
            System.out.println();
        }
    }

    public Space getLeft(int row, int col){
        return spaces[row][col - 1];
    }
    public Space getRight(int row, int col){
        return spaces[row][col + 1];
    }
    public Space getUp(int row, int col){
        return spaces[row - 1][col];
    }
    public Space getDown(int row, int col){
        return spaces[row + 1][col];
    }


}
