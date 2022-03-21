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
                TrieNode node = trie.getNode(prefix);
                extendRight(prefix,node,row,col);
            }
            else{
                int limit = 0;
                int left = board.getLeft(col);
                while(board.emptySpace(row,left)
                        && !anchors.contains(board.getSpace(row,left))){
                    limit++;
                    left = board.getLeft(left);
                }
                leftPart("",trie.getRoot(),row,col,limit);
            }
            if(board.isFilled(board.getUp(row),col)){
                int up = board.getUp(row);
                String prefix = "";
                while(board.isFilled(up,col)) {
                    prefix = board.getTileLetter(up,col) + prefix;
                    up--;
                }
                TrieNode node = trie.getNode(prefix);
                extendDown(prefix,node,row,col);
            }
            else{
                int limit = 0;
                int up = board.getUp(row);
                while(board.emptySpace(up,col)
                        && !anchors.contains(board.getSpace(up,col))){
                    limit++;
                    up = board.getLeft(up);
                }
                upPart("",trie.getRoot(),row,col,limit);
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

    public void legalMoveAcross(String word,int row,int col){
        Board temp = new Board(board.boardSize);
        temp.copyBoard(board.spaces);
        int playPos = col;
        int wordLength = word.length() - 1;
        int score = 0;
        char ch;
        while(wordLength >= 0) {
            ch = word.charAt(wordLength);
            if(board.emptySpace(row,playPos)) {
                String up = "";
                String down = "";
                if(board.isFilled(board.getUp(row),playPos)) {
                    up = getLettersUp(row, playPos);
                    for(int i = 0; i < up.length();i++){
                        char upChar = up.charAt(i);
                        score += values[upChar - 'a'];
                    }
                }
                if(board.isFilled(board.getDown(row),playPos)) {
                    down = getLettersDown(row, playPos);
                    for(int i = 0; i < down.length();i++){
                        char downChar = down.charAt(i);
                        score += values[downChar - 'a'];
                    }
                }
                String upDownWord = up + ch + down;
                if(trie.search(upDownWord) || upDownWord.length() == 1) {
                    for (Tile tile : hand) {
                        if (tile.getLetter() == ch) {
                            temp.playTile(row, playPos, tile);
                            score += score(row, playPos, ch, true);
                            break;
                        }
                    }
                }
                else{return;}
            }
            else if (board.isFilled(row, playPos)) {
                score += score(row, playPos, ch, false);
            }
            playPos = board.getLeft(playPos);
            wordLength--;
        }
        if (score >= highScore) {
            highScore = score;
            highBoard = temp;
            highWord = word;
        }
    }

    public void leftPart(String partialWord,TrieNode node,int row,int col
            ,int limit){
        HashMap<Character,TrieNode> children = node.getChildren();
        extendRight(partialWord,node,row,col);
        if(limit > 0) {
            children.forEach((key, value) -> {
                if (charHand.contains(key)) {
                    charHand.remove(key);
                    leftPart(partialWord + key, children.get(key), row, col
                            , limit - 1);
                    charHand.add(key);
                }
            });
        }
    }

    public void extendRight(String partialWord,TrieNode node,int row, int col){
        HashMap<Character,TrieNode> children = node.getChildren();
        if(!board.isFilled(row,col) && node.isEndOfWord()){
            legalMoveAcross(partialWord,row,board.getLeft(col));
        }
        if(board.inBounds(row,col)) {
            if(board.emptySpace(row,col)) {
                children.forEach((key, value) -> {
                    if (charHand.contains(key)) {
                        charHand.remove(key);
                        extendRight(partialWord + key, children.get(key)
                                , row, board.getRight(col));
                        charHand.add(key);
                    }
                });
            }
            else{
                char tileLetter = board.getTileLetter(row,col);
                if(children.containsKey(tileLetter)){
                    extendRight(partialWord + tileLetter,
                            children.get(tileLetter),row,board.getRight(col));
                }
            }
        }
    }

    public void legalMoveDown(String word,int row,int col){
        Board temp = new Board(board.boardSize);
        temp.copyBoard(board.spaces);
        int playPos = row;
        int wordLength = word.length() - 1;
        int score = 0;
        char ch;
        while(wordLength >= 0) {
            ch = word.charAt(wordLength);
            if(board.emptySpace(playPos,col)) {
                String left = "";
                String right = "";
                if(board.isFilled(playPos,board.getLeft(col))) {
                    left = getLettersLeft(playPos, col);
                    for(int i = 0; i < left.length();i++){
                        char leftChar = left.charAt(i);
                        score += values[leftChar - 'a'];
                    }
                }
                if(board.isFilled(playPos,board.getRight(col))) {
                    right = getLettersRight(playPos, col);
                    for(int i = 0; i < right.length();i++){
                        char rightChar = right.charAt(i);
                        score += values[rightChar - 'a'];
                    }
                }
                String acrossWord = left + ch + right;
                if(trie.search(acrossWord) || acrossWord.length() == 1) {
                    for (Tile tile : hand) {
                        if (tile.getLetter() == ch) {
                            temp.playTile(playPos, col, tile);
                            score += score(playPos, col, ch, true);
                            break;
                        }
                    }
                }
                else{return;}
            }
            else if (board.isFilled(playPos, col)) {
                score += score(playPos, col, ch, false);
            }
            playPos = board.getUp(playPos);
            wordLength--;
        }
        if (score >= highScore) {
            highScore = score;
            highBoard = temp;
            highWord = word;
        }
        /*System.out.println(word);
        System.out.println(score);
        temp.printBoard();
        System.out.println();*/
    }

    public void upPart(String partialWord,TrieNode node,int row,int col
            ,int limit){
        HashMap<Character,TrieNode> children = node.getChildren();
        extendDown(partialWord,node,row,col);
        if(limit > 0) {
            children.forEach((key, value) -> {
                if (charHand.contains(key)) {
                    charHand.remove(key);
                    upPart(partialWord + key, children.get(key), row, col
                            , limit - 1);
                    charHand.add(key);
                }
            });
        }
    }

    public void extendDown(String partialWord,TrieNode node,int row, int col){
        HashMap<Character,TrieNode> children = node.getChildren();
        if(!board.isFilled(row,col) && node.isEndOfWord()){
            legalMoveDown(partialWord,board.getUp(row),col);
        }
        if(board.inBounds(row,col)) {
            if(board.emptySpace(row,col)) {
                children.forEach((key, value) -> {
                    if (charHand.contains(key)) {
                        charHand.remove(key);
                        extendDown(partialWord + key,
                                children.get(key), board.getDown(row), col);
                        charHand.add(key);
                    }
                });
            }
            else{
                char tileLetter = board.getTileLetter(row,col);
                if(children.containsKey(tileLetter)){
                    extendDown(partialWord + tileLetter,
                            children.get(tileLetter),board.getDown(row),col);
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
    public String getLettersLeft(int row,int col){
        int left = board.getLeft(col);
        String lettersLeft = "";
        while(board.isFilled(row,left)){
            lettersLeft = board.getTileLetter(row,left) + lettersLeft;
            left = board.getLeft(left);
        }
        return lettersLeft;
    }
    public String getLettersRight(int row,int col){
        int right = board.getRight(col);
        String lettersRight = "";
        while(board.isFilled(row,right)){
            lettersRight = board.getTileLetter(row,right) + lettersRight;
            right = board.getRight(right);
        }
        return lettersRight;
    }

    public String getLettersUp(int row,int col){
        int up = board.getUp(row);
        String lettersUp = "";
        while(board.isFilled(up,col)){
            lettersUp = board.getTileLetter(up,col) + lettersUp;
            up = board.getUp(up);
        }
        return lettersUp;
    }

    public String getLettersDown(int row,int col){
        int down = board.getDown(row);
        String lettersDown = "";
        while(board.isFilled(down,col)){
            lettersDown = board.getTileLetter(down,col) + lettersDown;
            down = board.getDown(down);
        }
        return lettersDown;
    }

    public void handToCharArray(){
        for (Tile tile : hand) {
            charHand.add(tile.getLetter());
        }
    }
}
