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
        System.out.print("Tray: ");
        for (Character character : charHand) {
            System.out.print(character);
        }
        System.out.println();
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
                int prefixLength = prefix.length();
                TrieNode node = trie.getNode(prefix);
                extendRight(prefix,node,row,col,prefixLength);
            }
        }
        if(highBoard != null) {
            System.out.println("Solution " + highWord + " has " + highScore + " points");
            System.out.println("Solution Board: ");
            highBoard.printBoard();
        }
        else{
            System.out.println("No solution found");
        }
    }

    public void legalMove(String word,int row,int col,int prefixLength){
        //System.out.println("Found word: " + word);
        Board temp = new Board(board.boardSize);
        temp.copyBoard(board.spaces);
        int playPos = col;
        int wordLength = word.length() - 1;
        int score = 0;
        char ch;
        while(wordLength >= 0) {
            ch = word.charAt(wordLength);
            for (Tile tile : hand) {
                if (tile.getLetter() == ch) {
                    temp.playTile(row,playPos,tile);
                    score += score(row,playPos,ch,true);
                    break;
                }
            }
            if(board.isFilled(row,playPos) && wordLength > prefixLength){
                score += score(row,playPos,ch,false);
            }
            if(wordLength < prefixLength){
                score += score(row,playPos,ch,false);
            }
            playPos = board.getLeft(playPos);
            wordLength--;
        }
        if (score >= highScore) {
            highScore = score;
            highBoard = temp;
            highWord = word;
        }
        temp.printBoard();
        System.out.println();
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

    public void extendRight(String partialWord,TrieNode node,int row, int col
            ,int prefixLength){
        HashMap<Character,TrieNode> children = node.getChildren();
        if(!board.isFilled(row,col) && node.isEndOfWord()){
            legalMove(partialWord,row,board.getLeft(col),prefixLength);
        }
        if(board.inBounds(row,col)) {
            if(board.emptySpace(row,col)) {
                children.forEach((key, value) -> {
                    if (charHand.contains(key)) {
                        charHand.remove(key);
                        extendRight(partialWord + key, children.get(key)
                                , row, board.getRight(col), prefixLength);
                        charHand.add(key);
                    }
                });
            }
            else{
                char tileLetter = board.getTileLetter(row,col);
                if(children.containsKey(tileLetter)){
                    extendRight(partialWord + tileLetter,
                            children.get(tileLetter),row,board.getRight(col)
                            ,prefixLength);
                }
            }
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

    public void handToCharArray(){
        for (Tile tile : hand) {
            charHand.add(tile.getLetter());
        }
    }
}
