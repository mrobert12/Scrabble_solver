import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();
        File dictionary = new File(args[0]);
        readDictionary(dictionary,trie);
        Board board = readBoard();
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
    public static Board readBoard(){
        Scanner input = new Scanner(System.in);
        int boardSize = input.nextInt();
        Board board = new Board(boardSize);
        ArrayList<Space> anchors = new ArrayList<>();
        for(int i = 0;i < boardSize;i++){
            String line = input.nextLine();
            String[] spaces = line.split(" ");
            for(int j = 0;j < spaces.length;j++){
                char first = spaces[j].charAt(0);
                char second;
                if(spaces[j].length() > 1){
                    second = spaces[j].charAt(1);
                }
                Space space;
                if(first =='.'){
                     space = new Space(1,1,j,i,null);
                    board.addSpace(space,j,i);
                }
                else if(first - '0' >= 0 && first - '0' <= 9){
                    space = new Space (first - '0',1,j,i
                            ,null);
                    board.addSpace(space,j,i);
                }
                else if(first >= 'a' && first <= 'z'){
                    Tile tile = new Tile(first,false);
                    space = new Space(1,1,j,i,tile);
                    board.addSpace(space,j,i);
                    anchors.add(space);
                }
                else if(first >= 'A' && first <= 'Z'){
                    Tile tile = new Tile(Character.toLowerCase(first)
                            ,true);
                    space = new Space(1,1,j,i,tile);
                    board.addSpace(space,j,i);
                    anchors.add(space);
                }
            }
        }

        return board;
    }
}
