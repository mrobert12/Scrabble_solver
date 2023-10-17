import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/* Michel Robert
* Mrobert12@unm.edu
* Solver drives the program creating the dictionary and setting tile values and
* reading in the board state*/
public class Solver {
    public static void main(String[] args) {
        Trie trie = new Trie();
        File dictionary = new File(args[0]);
        int[] values = new int[26];
        setValues(values);
        readDictionary(dictionary,trie);
        readBoard(values,trie);
    }
    /* readDictionary reads the command line argument file and inserts every
    * word into the trie*/
    public static void readDictionary(File dictionary,Trie trie){
        Scanner fileRead = null;
        try {
            fileRead = new Scanner(dictionary);
        }
        catch(FileNotFoundException e){
            System.out.println("File Read Error Dictionary");
            System.exit(1);
        }
        while(fileRead.hasNextLine()){
            String word = fileRead.nextLine();
            trie.insert(word);
        }
    }
    /* setValues reads the tile settings file and sets the score values for
    * each letter*/
    public static void setValues(int[] values){
        File tileValues = new File("Resources/ScrabbleTiles");
        Scanner scan = null;
        try {
            scan = new Scanner(tileValues);
        }
        catch(FileNotFoundException e){
            System.out.println("File Read Error Set Values");
            System.exit(1);
        }
        while(scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] lines = line.split("\\s+");
            char letter = lines[0].charAt(0);
            if(letter == '*'){
                continue;
            }
            int score = Integer.parseInt(lines[1]);
            values[letter - 'a'] = score;
        }
    }
    /* boardFromFile reads the board from system.in will be read from a file
    * given when running the jar it will loop over multiple boards fed into the
    * file. it sets up the board state for solving*/
    public static void readBoard(int[] values,Trie trie) {
        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            Computer player = new Computer(trie,values);
            int boardSize = Integer.parseInt(input.nextLine());
            Board board = new Board(boardSize);
            for (int i = 0; i < boardSize; i++) {
                String line = input.nextLine();
                //spits the line up by each space and trims away the spaces
                String[] spaces = line.trim().split("\\s+");
                /* loop over each space read in and creates spaces depending on
                * what was read in. adds tiles to the spaces if necessary, if
                * not necessary it sets the tile value to null, and then it
                * adds the space to the board*/
                for (int j = 0; j < spaces.length; j++) {
                    char first = spaces[j].charAt(0);
                    char second = ' ';
                    if (spaces[j].length() > 1) {
                        second = spaces[j].charAt(1);
                    }
                    Space space;
                    if (first == '.' && second == '.') {
                        space = new Space(1, 1, i, j, null);
                        board.addSpace(space, i, j);
                    } else if (first == '.') {
                        space = new Space(second - '0', 1, i, j
                                , null);
                        board.addSpace(space, i, j);
                    } else if (first - '0' >= 0 && first - '0' <= 9) {
                        space = new Space(1, first - '0', i, j
                                , null);
                        board.addSpace(space, i, j);
                    } else if (first >= 'a' && first <= 'z') {
                        Tile tile = new Tile(first, values[first - 'a']);
                        space = new Space(1, 1, i, j, tile);
                        board.addSpace(space, i, j);
                    } else if (first >= 'A' && first <= 'Z') {
                        Tile tile = new Tile(first, 0);
                        space = new Space(1, 1, i, j, tile);
                        board.addSpace(space, i, j);
                    }
                }
            }
            /* read in the hand values for the board*/
            String hand = input.nextLine();
            Tile tile;
            for (int x = 0; x < hand.length(); x++) {
                char letter = hand.charAt(x);
                if (letter == '*') {
                    tile = new Tile(letter, 0);
                } else {
                    tile = new Tile(letter, values[letter - 'a']);
                }
                player.addTile(tile);
            }
            /* after the whole board and hand have been read in the board is
            * printed out and set anchor spaces then solve the board.*/
            System.out.println("Input Board:");
            board.printBoard();
            board.setAnchors();
            player.solver(board);
            System.out.println();
        }
    }
}
