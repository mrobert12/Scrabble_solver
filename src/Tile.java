import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Tile {
    private final char letter;
    private final int value;
    private final int[] values = new int[26];
    public Tile(char letter,Boolean blank){
        this.letter = letter;
        if(!blank) {
            this.value = values[letter - 'a'];
        }
        else this.value = 0;
    }

    public char getLetter() {
        return letter;
    }
    public int getValue() {
        return value;
    }
    public void setValues(){
        File tileValues = new File("TileValues");
        Scanner scan = null;
        try {
            scan = new Scanner(tileValues);
        }
        catch(FileNotFoundException e){
            System.out.println("File Read Error");
            System.exit(1);
        }
        while(scan.hasNextLine()){

        }
    }
}
