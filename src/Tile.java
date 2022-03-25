/* Michel Robert
* Tile class for holding scrabble tiles when a tile is created it is given a
* character and the score value*/
public class Tile {
    private final char letter;
    private final int value;
    public Tile(char letter,int value){
        this.letter = letter;
        this.value = value;
    }
    //method for getting the letter on a tile
    public char getLetter() {
        return letter;
    }
    //method for getting the score value on a tile
    public int getValue() {
        return value;
    }

}
