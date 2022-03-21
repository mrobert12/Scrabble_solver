import java.util.ArrayList;

public class Board{
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
                if(isFilled(i,j)){
                    if(emptySpace(i,getUp(j))){
                        if(!anchors.contains(getSpace(i,getUp(j)))){
                            anchors.add(getSpace(i,getUp(j)));
                        }
                    }
                    if(emptySpace(i,getDown(j))){
                        if(!anchors.contains(getSpace(i,getDown(j)))){
                            anchors.add(getSpace(i,getDown(j)));
                        }
                    }
                    if(emptySpace(getLeft(i),j)){
                        if(!anchors.contains(getSpace(getLeft(i),j))){
                            anchors.add(getSpace(getLeft(i),j));
                        }
                    }
                    if(emptySpace(getRight(i),j)){
                        if(!anchors.contains(getSpace(getRight(i),j))){
                            anchors.add(getSpace(getRight(i),j));
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

    public Boolean emptySpace(int row, int col){
        return inBounds(row,col) && this.getSpace(row,col).getTile() == null;
    }

    public Boolean isFilled(int row, int col){
        return inBounds(row,col) && this.getSpace(row,col).getTile() != null;
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

    public void playTile(int row,int col,Tile tile){
        Space space = this.spaces[row][col];
        space.setTile(tile);
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

    public int getLeft(int col){
        return col - 1;
    }

    public int getRight(int col){
        return col + 1;
    }

    public int getUp(int row){
        return row - 1;
    }

    public int getDown(int row){
        return row + 1;
    }

    public void copyBoard(Space[][] sp){
        for(int i = 0;i< boardSize;i++){
            for(int j = 0;j< boardSize;j++){
                int tmult = sp[i][j].getTileMult();
                int wmult = sp[i][j].getWordMult();
                Tile tile = sp[i][j].getTile();
                Space space = new Space(tmult,wmult,i,j,tile);
                this.addSpace(space,i,j);
            }
        }
    }

    public Boolean inBounds(int row, int col){
        return row>= 0 && row < boardSize && col >= 0 && col < boardSize;
    }
}
