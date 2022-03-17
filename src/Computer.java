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

    public void Solver(Board board,Trie trie,int[] values){
        this.board = board;
        this.trie = trie;
        this.values = values;
        TrieNode root = trie.getRoot();
        ArrayList<Space> anchors = board.getAnchors();
        System.out.println(charHand);
        for (Space anchor : anchors) {
            StringBuilder partialWord = new StringBuilder();
            int row = anchor.getRow();
            int col = anchor.getCol();
            String prefix = leftPrefix(partialWord, row, col);
            int prefixLength = prefix.length();
            TrieNode node = trie.getNode(prefix);
            allWords(prefix, node, row, col, prefixLength);
        }
        System.out.println("Solution " + highWord + " has " + highScore + " points");
        highBoard.printBoard();
    }

    public void playOnBoard(String word,int row, int col,int prefixLength){
        Board temp = new Board(board.boardSize);
        temp.copyBoard(board.spaces);
        int score = 0;
        char ch;
        for(int i = 0; i < word.length();i++){
            if(i < prefixLength){
                ch = word.charAt(i);
                score += score(row,col,ch,false);
                continue;
            }
            ch = word.charAt(i);
            int index = 0;
            for(int j = 0;j < hand.size();j++){
                if(hand.get(j).getLetter() == ch){
                    index = j;
                }
            }
            score += score(row,col,ch,true);
            if(temp.emptySpace(temp.getSpace(row,col))) {
                temp.playTile(row, col, hand.get(index));
            }
            else {
                return;
            }
            col++;
        }
        if(score > highScore){
            highScore = score;
            highBoard = temp;
            highWord = word;
        }
    }

    public void allWords(String partialWord, TrieNode node,int row,int col
            ,int prefixLength){
        HashMap<Character,TrieNode> children = node.getChildren();
        int wordLength = board.boardSize - col;
        if(partialWord.length() > wordLength){
            return;
        }
        if(node.isEndOfWord()){
            System.out.println(partialWord);
            playOnBoard(partialWord,row,col,prefixLength);
        }
        children.forEach((key,value) ->{
            if(charHand.contains(key)){
                charHand.remove(key);
                allWords(partialWord + key,children.get(key),row,col
                        ,prefixLength);
                charHand.add(key);
            }
        });

    }

    public String leftPrefix(StringBuilder partialWord,int row ,int col){
        if(col == 0){
            return partialWord.toString();
        }
        else if(!board.emptySpace(board.getLeft(row,col))){
            partialWord.insert(0,board.getLeft(row,col).getTile().getLetter());
            leftPrefix(partialWord,row,col - 1);
        }
        return partialWord.toString();
    }

    public void handToCharArray(){
        for (Tile tile : hand) {
            charHand.add(tile.getLetter());
        }
    }
    public void printHand(){
        for (Tile tile : hand) {
            System.out.print(tile.getLetter());
        }
    }
    public int score(int row,int col,char ch,Boolean spaceBonus){
        int score;
        Space space = board.getSpace(row,col);
        int tMult = space.getTileMult();
        int wMult = space.getWordMult();
        if(spaceBonus) {
            score = values[ch - 'a'] * tMult * wMult;
        }
        else{
            score = values[ch -'a'];
        }
        return score;
    }
}
