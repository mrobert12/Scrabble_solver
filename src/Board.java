public class Board {
    int boardSize;
    Tile[][] tiles;
    public Board(int boardSize){
        this.boardSize = boardSize;
        tiles = new Tile[boardSize][boardSize];
        for(int i = 0; i < boardSize;i++){
            for(int j = 0; j < boardSize;j++){
                tiles[i][j] = null;
            }
        }
    }
    public void addSpace(Space space,int x,int y){

    }
    public void setAnchors(){

    }
}
