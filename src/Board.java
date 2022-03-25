import java.util.ArrayList;
/* Michel Robert
* Board class for holding the board state of the game when its created it is
* given a size. all boards are square boards are held as 2d arrays of spaces.*/
public class Board{
    int boardSize;
    Space[][] spaces;
    ArrayList<Space> anchors = new ArrayList<>();
    public Board(int boardSize){
        this.boardSize = boardSize;
        spaces = new Space[boardSize][boardSize];
        //set every space to null
        for(int i = 0; i < boardSize;i++){
            for(int j = 0; j < boardSize;j++){
                spaces[i][j] = null;
            }
        }
    }

    /* loop over the entire board looking for spaces with tiles on them. when
    * a filled spaces is found we look up down left and right of the space if
    * a position is empty then we add it to our array list of anchors */
    public void setAnchors(){
        for(int i = 0;i < boardSize;i++){
            for(int j = 0; j < boardSize;j++){
                if(isFilled(i,j)){
                    if(emptySpace(i,getLeft(j))){
                        if(!anchors.contains(getSpace(i,getLeft(j)))){
                            anchors.add(getSpace(i,getLeft(j)));
                        }
                    }
                    if(emptySpace(i,getRight(j))){
                        if(!anchors.contains(getSpace(i,getRight(j)))){
                            anchors.add(getSpace(i,getRight(j)));
                        }
                    }
                    if(emptySpace(getUp(i),j)){
                        if(!anchors.contains(getSpace(getUp(i),j))){
                            anchors.add(getSpace(getUp(i),j));
                        }
                    }
                    if(emptySpace(getDown(i),j)){
                        if(!anchors.contains(getSpace(getDown(i),j))){
                            anchors.add(getSpace(getDown(i),j));
                        }
                    }
                }
            }
        }
    }

    /* checks if the position is inBounds and returns true if the space doesn't
    * have a tile on it*/
    public Boolean emptySpace(int row, int col){
        return inBounds(row,col) && this.getSpace(row,col).getTile() == null;
    }
    /* checks if the position is inBounds and returns true if the space does
     * have a tile on it*/
    public Boolean isFilled(int row, int col){
        return inBounds(row,col) && this.getSpace(row,col).getTile() != null;
    }

    //add a space to the board
    public void addSpace(Space space,int row,int col){
        spaces[row][col] = space;
    }
    //method to get the arraylist of anchors
    public ArrayList<Space> getAnchors(){
        return anchors;
    }
    //method for getting a space on the board
    public Space getSpace(int row,int col){
        return spaces[row][col];
    }
    //method for getting the size of the board
    public int getSize(){
        return boardSize;
    }
    //method for playing a tile on a space on the board.
    public void playTile(int row,int col,Tile tile){
        Space space = this.spaces[row][col];
        space.setTile(tile);
    }
    //method that prints out the board at any given state
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
    /* the next four methods are to make moving around the board more readable*/
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

    /*method for copying the board array for creating temp boards to check added
    words*/
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
    //method for testing if a location is within the bounds of the board
    public Boolean inBounds(int row, int col){
        return row>= 0 && row < boardSize && col >= 0 && col < boardSize;
    }
    //method for getting the letter given a space location
    public Character getTileLetter(int row, int col){
        return getSpace(row,col).getTile().getLetter();
    }
}
