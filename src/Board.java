public class Board {
    int boardSize;
    Space[][] spaces;
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

    }
    public void setAnchors(){

    }
}
