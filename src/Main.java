import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();
        File dictionary = new File(args[0]);
        int[] values = new int[26];
        setValues(values);
        readDictionary(dictionary,trie);
        Scanner stdin = new Scanner(System.in);
        String fileIn = stdin.nextLine();
        String fileOut = stdin.nextLine();
        File outfile = new File(fileOut);
        FileWriter output = null;

        try{
            output = new FileWriter(outfile,false);
            output.close();
            output = new FileWriter(outfile,true);
        }
        catch(IOException e){
            System.out.println("Filewriter error");
            e.printStackTrace();
        }
        boardFromFile(values,trie,output,fileIn);
    }

    public static void readDictionary(File dictionary,Trie trie){
        Scanner fileRead = null;
        try {
            fileRead = new Scanner(dictionary);
        }
        catch(FileNotFoundException e){
            System.out.println("File Read Error in: readDictionary");
            System.exit(1);
        }
        while(fileRead.hasNextLine()){
            String word = fileRead.nextLine();
            trie.insert(word);
        }
    }

    public static void setValues(int[] values){
        File tileValues = new File("Resources\\ScrabbleTiles");
        Scanner scan = null;
        try {
            scan = new Scanner(tileValues);
        }
        catch(FileNotFoundException e){
            System.out.println("File Read Error in: setValues");
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

    public static void boardFromFile(int[] values,Trie trie,
                                     FileWriter output,String fileIn) {
        File file = new File(fileIn);
        Scanner input = null;
        try {
            input = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File Read Error in: boardFromFile");
            System.exit(1);
        }
        while (input.hasNextLine()) {
            Computer player = new Computer(trie,values,output);
            int boardSize = Integer.parseInt(input.nextLine());
            Board board = new Board(boardSize);
            for (int i = 0; i < boardSize; i++) {
                String line = input.nextLine();
                String[] spaces = line.trim().split("\\s+");
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
                    }
                    else if (first == '.') {
                        space = new Space(second - '0', 1, i, j
                                , null);
                        board.addSpace(space, i, j);
                    }
                    else if (first - '0' >= 0 && first - '0' <= 9) {
                        space = new Space(1, first - '0', i, j
                                , null);
                        board.addSpace(space, i, j);
                    }
                    else if (first >= 'a' && first <= 'z') {
                        Tile tile = new Tile(first, values[first - 'a']);
                        space = new Space(1, 1, i, j, tile);
                        board.addSpace(space, i, j);
                    }
                    else if (first >= 'A' && first <= 'Z') {
                        Tile tile = new Tile(first, 0);
                        space = new Space(1, 1, i, j, tile);
                        board.addSpace(space, i, j);
                    }
                }
            }
            String hand = input.nextLine();
            Tile tile;
            for (int x = 0; x < hand.length(); x++) {
                char letter = hand.charAt(x);
                if (letter == '*') {
                    tile = new Tile(letter, 0);
                }
                else {
                    tile = new Tile(letter, values[letter - 'a']);
                }
                player.addTile(tile);
            }
            try {
                output.write("InputBoard:\n");
                board.printBoard(output);
                board.setAnchors();
                player.solver(board);
                output.write("\n");
            }
            catch(IOException e){
                System.out.println("File error");
                e.printStackTrace();
            }
        }
        try {
            output.flush();
            output.close();
        }
        catch(IOException e){
            System.out.println("File close error");
            e.printStackTrace();
        }
    }
}
