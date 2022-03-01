public class Space {
    private boolean anchorPoint;
    Tile tile;
    private int tileMult;
    private int wordMult;
    private int xCoordinate;
    private int yCoordinate;
    public Space(int tileMult,int wordMult,int xCoordinate,int yCoordinate){
        this.tile = null;
        this.tileMult = tileMult;
        this.wordMult = wordMult;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public void setAnchorPoint(boolean anchorPoint) {
        this.anchorPoint = anchorPoint;
    }

    public Boolean getAnchorPoint(){
        return anchorPoint;
    }
    public void setTile(Tile tile){
        this.tile = tile;
    }
    public Tile getTile(){
        return tile;
    }
}
