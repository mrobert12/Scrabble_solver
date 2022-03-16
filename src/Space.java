public class Space {
    Tile tile;
    private final int tileMult;
    private final int wordMult;
    private final int xCoordinate;
    private final int yCoordinate;
    public Space(int tileMult,int wordMult,int xCoordinate,int yCoordinate
            ,Tile tile){
        this.tile = tile;
        this.tileMult = tileMult;
        this.wordMult = wordMult;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
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

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }
}
