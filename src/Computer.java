import java.util.ArrayList;
import java.util.HashMap;

public class Computer {
    ArrayList<Tile> hand = new ArrayList<>();
    ArrayList<Character> charHand = new ArrayList<>();
    Board board;
    Trie trie;
    int [] values;
    int highScore;
    Board highBoard;
    String highWord;
    public void addTile(Tile tile){
        hand.add(tile);
    }

    public Computer(Trie trie,int[] values){
        this.trie = trie;
        this.values = values;
    }

    public void solver(Board board){
        this.board = board;
        handToCharArray();
        ArrayList<Space> anchors = board.getAnchors();
        for(Space space : anchors){
            int row = space.getRow();
            int col = space.getCol();
            if(board.isFilled(row,board.getLeft(col))){
                int left = board.getLeft(col);
                String prefix = "";
                while(board.isFilled(row,left)) {
                    prefix = board.getTileLetter(row,left) + prefix;
                    left--;
                }
                TrieNode node = trie.getNode(prefix);
                extendRight(prefix,node,row,col);
            }
        }
    }

    public void legalMove(String word,int row,int col){
        System.out.println("Found word: " + word);
        Board temp = new Board(board.boardSize);
        temp.copyBoard(board.spaces);
        int playPos = col;
        int wordLength = word.length() - 1;
        for(int i = 0; i < word.length();i++){
            char ch;
            while(wordLength >= 0) {
                System.out.println(wordLength);
                ch = word.charAt(wordLength);
                System.out.println(ch);
                for (Tile tile : hand) {
                    if (tile.getLetter() == ch) {
                        temp.playTile(row,playPos,tile);
                        break;
                    }
                }
                playPos = board.getLeft(playPos);
                wordLength--;
            }
        }
        temp.printBoard();

    }
    /*public void allWords(String partialWord,TrieNode node){
        HashMap<Character,TrieNode> children = node.getChildren();
        if(node.isEndOfWord()){
            legalMove(partialWord);
        }
        children.forEach((key,value) ->{
            if(charHand.contains(key)){
                charHand.remove(key);
                allWords(partialWord+key,children.get(key));
                charHand.add(key);
            }
        });
    }*/

    public void extendRight(String partialWord,TrieNode node,int row, int col){
        HashMap<Character,TrieNode> children = node.getChildren();
        if(node.isEndOfWord()){
            legalMove(partialWord,row,board.getLeft(col));
        }
        if(board.inBounds(row,col)) {
            children.forEach((key, value) -> {
                if (charHand.contains(key)) {
                    charHand.remove(key);
                    extendRight(partialWord + key, children.get(key)
                            , row, board.getRight(col));
                    charHand.add(key);
                }
            });
        }
    }

    public void handToCharArray(){
        for (Tile tile : hand) {
            charHand.add(tile.getLetter());
        }
    }
}
