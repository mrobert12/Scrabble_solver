import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();
        File dictionary = new File(args[0]);
        Scanner input = new Scanner(System.in);
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
}
