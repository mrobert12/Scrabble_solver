/* Michel Robert
* Space hold tiles and multipliers for the board*/
public class Space {
    Tile tile;
    private final int tileMult;
    private final int wordMult;
    private final int row;
    private final int col;
    /* When a space is created it takes a tile multiplier and a word multiplier
    * its position on the board and a tile*/
    public Space(int tileMult,int wordMult,int row,int col
            ,Tile tile){
        this.tile = tile;
        this.tileMult = tileMult;
        this.wordMult = wordMult;
        this.row = row;
        this.col = col;
    }
    // sets the Tile on a space
    public void setTile(Tile tile){
        this.tile = tile;
    }
    // gets the tile on a space
    public Tile getTile(){
        return tile;
    }
    //gets the tile multiplier of a space
    public int getTileMult() {
        return tileMult;
    }
    //gets the word multiplier for a space
    public int getWordMult() {
        return wordMult;
    }
    //gets the spaces row
    public int getRow() {
        return row;
    }
    //gets the spaces column
    public int getCol() {
        return col;
    }
}
