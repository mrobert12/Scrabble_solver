import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

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
        if(anchors.size() == 0){
            anchors.add(board.getSpace(board.boardSize/2,board.boardSize/2));
        }
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
                extendRight(prefix,node,row,col,row,col);
            }
            else{
                int limit = 0;
                int left = board.getLeft(col);
                while(board.emptySpace(row,left)
                        && !anchors.contains(board.getSpace(row,left))){
                    limit++;
                    left = board.getLeft(left);
                }
                leftPart("",trie.getRoot(),row,col,limit,row,col);
            }
            if(board.isFilled(board.getUp(row),col)){
                int up = board.getUp(row);
                String prefix = "";
                while(board.isFilled(up,col)) {
                    prefix = board.getTileLetter(up,col) + prefix;
                    up--;
                }
                TrieNode node = trie.getNode(prefix);
                extendDown(prefix,node,row,col,row,col);
            }
            else{
                int limit = 0;
                int up = board.getUp(row);
                while(board.emptySpace(up,col)
                        && !anchors.contains(board.getSpace(up,col))){
                    limit++;
                    up = board.getUp(up);
                }
                upPart("",trie.getRoot(),row,col,limit,row,col);
            }
        }
        if(highBoard != null) {
            System.out.println("Solution " + highWord + " has " + highScore
                    + " points");
            System.out.println("Solution Board: ");
            highBoard.printBoard();
        }
        else{
            System.out.println("No solution found");
        }
    }

    public void legalMoveAcross(String word,int row,int col,int anchorRow,
                                int anchorCol){
        Board temp = new Board(board.boardSize);
        temp.copyBoard(board.spaces);
        ArrayList<Space> playedSpaces = new ArrayList<>();
        int playPos = col;
        int wordLength = word.length() - 1;
        int score;
        int wordScore = 0;
        int wordMult = 1;
        char ch;
        int tilesPlayed = 0;
        while(wordLength >= 0) {
            ch = word.charAt(wordLength);
            if(board.emptySpace(row,playPos)) {
                String up = "";
                String down = "";
                if(board.isFilled(board.getUp(row),playPos)) {
                    up = getLettersUp(row, playPos);
                }
                if(board.isFilled(board.getDown(row),playPos)) {
                    down = getLettersDown(row, playPos);
                }
                String upDownWord = up + Character.toLowerCase(ch) + down;
                upDownWord = upDownWord.toLowerCase(Locale.ROOT);
                if(trie.search(upDownWord) || upDownWord.length() == 1) {
                    if(Character.isUpperCase(ch)){
                        Tile blank = new Tile(ch,0);
                        temp.playTile(row,playPos,blank);
                        Space space = board.getSpace(row,playPos);
                        wordMult *= space.getWordMult();
                        playedSpaces.add(space);
                        tilesPlayed++;
                    }
                    else {
                        for (Tile tile : hand) {
                            if (tile.getLetter() == ch) {
                                temp.playTile(row, playPos, tile);
                                Space space = board.getSpace(row, playPos);
                                playedSpaces.add(space);
                                wordMult *= space.getWordMult();
                                wordScore += values[ch - 'a']
                                        * space.getTileMult();
                                tilesPlayed++;
                                break;
                            }
                        }
                    }
                }
                else{return;}
            }
            else{
                wordScore += values[ch - 'a'];
            }
            playPos = board.getLeft(playPos);
            wordLength--;
        }
        wordScore *= wordMult;
        if(temp.isFilled(anchorRow,anchorCol)) {
            score = Score(temp,playedSpaces,tilesPlayed == 7,true,
                    wordScore);
            if (score > highScore) {
            highScore = score;
            highBoard = temp;
            highWord = word;
            }
        }
    }

    public void leftPart(String partialWord,TrieNode node,int row,int col
            ,int limit,int anchorRow,int anchorCol){
        HashMap<Character,TrieNode> children = node.getChildren();
        extendRight(partialWord,node,row,col,anchorRow,anchorCol);
        if(limit > 0) {
            children.forEach((key, value) -> {
                for(int i = 0; i < charHand.size();i++){
                    if(charHand.get(i) == '*'){
                        charHand.remove(i);
                        leftPart(
                             partialWord + Character.toUpperCase(key),
                                children.get(key),row,col,limit -1,
                                anchorRow,anchorCol);
                        charHand.add('*');
                    }
                    else if(charHand.get(i) == key){
                        charHand.remove(key);
                        leftPart(partialWord + key, children.get(key),
                                row, col,limit - 1,anchorRow,anchorCol);
                        charHand.add(key);
                    }
                }
            });
        }
    }

    public void extendRight(String partialWord,TrieNode node,int row, int col,
                            int anchorRow,int anchorCol){
        HashMap<Character,TrieNode> children = node.getChildren();
        if(!board.isFilled(row,col) && node.isEndOfWord()){
            legalMoveAcross(partialWord,row,board.getLeft(col),anchorRow
                    ,anchorCol);
        }
        if(board.inBounds(row,col)) {
            if(board.emptySpace(row,col)) {
                children.forEach((key, value) -> {
                    for(int i = 0; i < charHand.size();i++){
                        if(charHand.get(i) == '*'){
                            charHand.remove(i);
                            extendRight(
                             partialWord + Character.toUpperCase(key),
                                    children.get(key),row,board.getRight(col),
                                    anchorRow,anchorCol);
                            charHand.add('*');
                        }
                        else if(charHand.get(i) == key){
                            charHand.remove(key);
                            extendRight(partialWord + key,
                                    children.get(key), row, board.getRight(col),
                                    anchorRow,anchorCol);
                            charHand.add(key);
                        }
                    }
                });
            }
            else{
                char tileLetter = board.getTileLetter(row,col);
                if(children.containsKey(tileLetter)){
                    extendRight(partialWord + tileLetter,
                            children.get(tileLetter),row,board.getRight(col),
                            anchorRow,anchorCol);
                }
            }
        }
    }

    public void legalMoveDown(String word,int row,int col,int anchorRow,
                              int anchorCol){
        Board temp = new Board(board.boardSize);
        temp.copyBoard(board.spaces);
        ArrayList<Space> playedSpaces = new ArrayList<>();
        int playPos = row;
        int wordLength = word.length() - 1;
        int wordScore = 0;
        int wordMult = 1;
        int score;
        char ch;
        int tilesPlayed = 0;
        while(wordLength >= 0) {
            ch = word.charAt(wordLength);
            if(board.emptySpace(playPos,col)) {
                String left = "";
                String right = "";
                if(board.isFilled(playPos,board.getLeft(col))) {
                    left = getLettersLeft(playPos, col);
                }
                if(board.isFilled(playPos,board.getRight(col))) {
                    right = getLettersRight(playPos, col);
                }
                String acrossWord = left + Character.toLowerCase(ch) + right;
                acrossWord = acrossWord.toLowerCase(Locale.ROOT);
                if(trie.search(acrossWord) || acrossWord.length() == 1) {
                    if(Character.isUpperCase(ch)){
                        Tile blank = new Tile(ch,0);
                        temp.playTile(playPos,col,blank);
                        Space space = board.getSpace(playPos,col);
                        wordMult *= space.getWordMult();
                        playedSpaces.add(space);
                        tilesPlayed++;
                    }
                    else {
                        for (Tile tile : hand) {
                            if (tile.getLetter() == ch) {
                                temp.playTile(playPos, col, tile);
                                Space space = board.getSpace(playPos, col);
                                playedSpaces.add(space);
                                wordMult *= space.getWordMult();
                                wordScore += values[ch - 'a'] * space.getTileMult();
                                tilesPlayed++;
                                break;
                            }
                        }
                    }
                }
                else{return;}
            }
            else{
                if(!Character.isUpperCase(ch)) {
                    wordScore += values[ch - 'a'];
                }
            }
            playPos = board.getUp(playPos);
            wordLength--;
        }
        wordScore *= wordMult;
        if(temp.isFilled(anchorRow,anchorCol)) {
            score = Score(temp, playedSpaces, tilesPlayed == 7,
                    false, wordScore);
            if (score > highScore) {
                highScore = score;
                highBoard = temp;
                highWord = word;
            }
        }
    }

    public void upPart(String partialWord,TrieNode node,int row,int col
            ,int limit,int anchorRow,int anchorCol){
        HashMap<Character,TrieNode> children = node.getChildren();
        extendDown(partialWord,node,row,col,anchorRow,anchorCol);
        if(limit > 0) {
            children.forEach((key, value) -> {
                for(int i = 0; i < charHand.size();i++){
                    if(charHand.get(i) == '*'){
                        charHand.remove(i);
                        upPart(partialWord + Character.toUpperCase(key),
                                children.get(key),row,col,limit -1,
                                anchorRow,anchorCol);
                        charHand.add('*');
                    }
                    else if(charHand.get(i) == key){
                        charHand.remove(key);
                        upPart(partialWord + key, children.get(key)
                                , row, col, limit - 1,anchorRow,anchorCol);
                        charHand.add(key);
                    }
                }
            });
        }
    }

    public void extendDown(String partialWord,TrieNode node,int row, int col,
                           int anchorRow,int anchorCol){
        HashMap<Character,TrieNode> children = node.getChildren();
        if(!board.isFilled(row,col) && node.isEndOfWord()){
            legalMoveDown(partialWord,board.getUp(row),col,anchorRow,anchorCol);
        }
        if(board.inBounds(row,col)) {
            if(board.emptySpace(row,col)) {
                children.forEach((key, value) -> {
                    for(int i = 0; i < charHand.size();i++){
                        if(charHand.get(i) == '*'){
                            charHand.remove(i);
                            extendDown(
                             partialWord + Character.toUpperCase(key),
                                    children.get(key),board.getDown(row),col,
                                    anchorRow,anchorCol);
                            charHand.add('*');
                        }
                        else if(charHand.get(i) == key){
                            charHand.remove(key);
                            extendDown(partialWord + key,
                                    children.get(key), board.getDown(row), col,
                                    anchorRow,anchorCol);
                            charHand.add(key);
                        }
                    }
                });
            }
            else{
                char tileLetter = board.getTileLetter(row,col);
                if(children.containsKey(tileLetter)){
                    extendDown(partialWord + tileLetter,
                            children.get(tileLetter),board.getDown(row),col,
                            anchorRow,anchorCol);
                }
            }
        }
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
    public int Score(Board temp, ArrayList<Space> playedSpaces,Boolean bonus
            ,Boolean across,int wordScore){
        int score = 0;
        int row;
        int col;
        for(Space space: playedSpaces){
            row = space.getRow();
            col = space.getCol();
            Space boardSpace = temp.getSpace(row,col);
            Tile tile = boardSpace.getTile();
            int tileMult = boardSpace.getTileMult();
            int sideWordScore = 0;
            if(across){
                String up;
                String down;
                if(temp.isFilled(temp.getUp(row),col)) {
                    up = getLettersUp(row, col);
                    for(int i = 0; i < up.length();i++){
                        char upChar = up.charAt(i);
                        if(!Character.isUpperCase(upChar)){
                            sideWordScore += values[upChar - 'a'];
                        }
                    }
                }
                if(temp.isFilled(temp.getDown(row),col)) {
                    down = getLettersDown(row, col);
                    for(int i = 0; i < down.length();i++){
                        char downChar = down.charAt(i);
                        if(!Character.isUpperCase(downChar)){
                            sideWordScore += values[downChar - 'a'];
                        }
                    }
                }
            }
            else{
                String left;
                String right;
                if(temp.isFilled(row,temp.getLeft(col))) {
                    left = getLettersLeft(row, col);
                    for(int i = 0; i < left.length();i++){
                        char leftChar = left.charAt(i);
                        if(!Character.isUpperCase(leftChar)){
                            sideWordScore += values[leftChar - 'a'];
                        }
                    }
                }
                if(temp.isFilled(row,temp.getRight(col))) {
                    right = getLettersRight(row, col);
                    for(int i = 0; i < right.length();i++){
                        char rightChar = right.charAt(i);
                        if(!Character.isUpperCase(rightChar)){
                            sideWordScore+= values[rightChar - 'a'];
                        }
                    }
                }
            }
            if(sideWordScore != 0) {
                if(Character.isLowerCase(tile.getLetter())) {
                    sideWordScore += (values[tile.getLetter() - 'a'] * tileMult);
                }
                sideWordScore *= boardSpace.getWordMult();
                score += sideWordScore;
            }
        }
        score += wordScore;

        if(bonus){
            score += 50;
        }
        return score;
    }
}
