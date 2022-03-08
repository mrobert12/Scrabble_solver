import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();
        File dictionary = new File("D:\\Documents\\UNM\\Spring2022\\CS351\\scrabble\\Resources\\animals");
        int[] values = new int[26];
        Computer player = new Computer();
        setValues(values);
        readDictionary(dictionary,trie);
        Board board = readBoard(values,player);
        player.Solver(board,trie,values);
    }

    public static void readDictionary(File dictionary,Trie trie){
        Scanner fileRead = null;
        try {
            fileRead = new Scanner(dictionary);
        }
        catch(FileNotFoundException e){
            System.out.println("File Read Error");
            System.exit(1);
        }
        while(fileRead.hasNextLine()){
            String word = fileRead.nextLine();
            trie.insert(word);
        }
    }

    public static Board readBoard(int[] values,Computer player){
        Scanner input = new Scanner(System.in);
        int boardSize = Integer.parseInt(input.nextLine());
        Board board = new Board(boardSize);
        ArrayList<Space> anchors = new ArrayList<>();
        for(int i = 0;i < boardSize;i++){
            String line = input.nextLine();
            String[] spaces = line.trim().split("\\s+");
            for(int j = 0;j < spaces.length;j++){
                char first = spaces[j].charAt(0);
                char second = ' ';
                if(spaces[j].length() > 1){
                    second = spaces[j].charAt(1);
                }
                Space space;
                if(first =='.' && second == '.'){
                     space = new Space(1,1,j,i,null);
                    board.addSpace(space,j,i);
                }
                else if(first == '.'){
                    space = new Space(1,second - '0',j,i
                            ,null);
                    board.addSpace(space,j,i);
                }
                else if(first - '0' >= 0 && first - '0' <= 9){
                    space = new Space (first - '0',1,j,i
                            ,null);
                    board.addSpace(space,j,i);
                }
                else if(first >= 'a' && first <= 'z'){
                    Tile tile = new Tile(first,values[first - 'a']);
                    space = new Space(1,1,j,i,tile);
                    board.addSpace(space,j,i);
                    anchors.add(space);
                }
                else if(first >= 'A' && first <= 'Z'){
                    Tile tile = new Tile(Character.toLowerCase(first)
                            ,0);
                    space = new Space(1,1,j,i,tile);
                    board.addSpace(space,j,i);
                    anchors.add(space);
                }
            }
        }
        String hand = input.nextLine();
        Tile tile;
        for(int x = 0;x < hand.length();x++){
            char letter = hand.charAt(x);
            if(letter == '*'){
                tile = new Tile(letter,0);
            }
            else{
                tile = new Tile(letter, values[letter - 'a']);
            }
            player.addTile(tile);
        }
        board.firstAnchors(anchors);
        return board;
    }

    public static void setValues(int[] values){
        File tileValues = new File("D:\\Documents\\UNM\\Spring2022\\CS351\\scrabble\\Resources\\Scrabble tiles");
        Scanner scan = null;
        try {
            scan = new Scanner(tileValues);
        }
        catch(FileNotFoundException e){
            System.out.println("File Read Error");
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
}
