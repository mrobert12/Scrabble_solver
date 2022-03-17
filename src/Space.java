public class Space {
    Tile tile;
    private final int tileMult;
    private final int wordMult;
    private final int row;
    private final int col;
    public Space(int tileMult,int wordMult,int row,int col
            ,Tile tile){
        this.tile = tile;
        this.tileMult = tileMult;
        this.wordMult = wordMult;
        this.row = row;
        this.col = col;
    }
    public void setTile(Tile tile){
        this.tile = tile;
    }
    public Tile getTile(){
        return tile;
    }

    public int getTileMult() {
        return tileMult;
    }

    public int getWordMult() {
        return wordMult;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
