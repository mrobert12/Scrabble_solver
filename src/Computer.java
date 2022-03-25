import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
/* Michel Robert
* Computer class is where the board is solved for the highest score word.
* it takes in the dictionary and the values of the tiles when created. when
* the solver method is called it takes in the board. then it will solve for
* valid words and determine the score for the word and if it is the highscore
* it saves the board state the word and the score.*/
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
    /* solver will loop over the anchor points(the empty spaces next to tiles
    * that are already on the board). it will check for prefixes either up or
    * to the left of the anchor point. after it has looped over every anchor
    * point it will print out the highest point solution found*/
    public void solver(Board board){
        this.board = board;
        handToCharArray();
        System.out.print("Tray: ");
        for (Character character : charHand) {
            System.out.print(character);
        }
        System.out.println();
        ArrayList<Space> anchors = board.getAnchors();
        /* if the board is empty set the center square to the only anchor point*/
        if(anchors.size() == 0){
            anchors.add(board.getSpace(board.boardSize/2,board.boardSize/2));
        }
        /* looping over the anchor spaces*/
        for(Space space : anchors){
            int row = space.getRow();
            int col = space.getCol();
            /* if the position to the left of the anchor is filled it will
            * loop to the left until it finds an empty square and build a prefix
            * from the letters on the filled spaces*/
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
            /* if the left position isn't filled it will find the lenght that it
            * can make the prefix to the left of the anchor point and will then
            * call leftPart where it will try to create a prefix before the
            * anchor point*/
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
            /* if the position abovethe anchor is filled it will
             * loop upwards until it finds an empty square and build a prefix
             * from the letters on the filled spaces*/
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
            /* if the position above isn't filled it will find the lenght that
             * it can make the prefix above the anchor point and will
             * then call upPart where it will try to create a prefix above
             * the anchor point*/
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
        /* printing the found solution and its board if one is found else it
        * will print no solution*/
        if(highBoard != null) {
            System.out.println("Solution " + highWord + " has " + highScore
                    + " points");
            System.out.println("Solution Board:");
            highBoard.printBoard();
        }
        else{
            System.out.println("No solution found");
        }
    }

    /* legalMoveAcross creates temp boards for determining scores of a word
    * that is trying to be played. its starting point is the end of the word
    * so, it will loop backwards over the word checking the board for either
    * empty spaces or filled spaces and will play the letter if empty and
    * skip the letter if it is already on the board. while it is placing a new
    * tile on the board it checks above and below the position to see if adding
    * the letter will create a word with those pre and postfixes as well.
    * It keeps score of the word that is being played at this time, and after
    * it has looped over the whole word it calls score to calculate score of
    * any side words. before calculating score and determining if it is a new
    * high score it checks to be sure that the play has covered the anchor
    * square as I was running into cases where it was just creating the prefix
    * which would be a word and wasn't covering the anchor square. when a high
    * score is found the board, word, and score are saved.*/
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
        //loop over the word backwards
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
                /* checks to see that words created from prefix above and the
                 * posfix below the position are actual words or that both are
                 * empty so only the Character to play is present*/
                if(trie.search(upDownWord) || upDownWord.length() == 1) {
                    /* if the tile to be played is Blank add no score*/
                    if(Character.isUpperCase(ch)){
                        Tile blank = new Tile(ch,0);
                        temp.playTile(row,playPos,blank);
                        Space space = board.getSpace(row,playPos);
                        //update the word multiplier for the space
                        wordMult *= space.getWordMult();
                        playedSpaces.add(space);
                        tilesPlayed++;
                    }
                    else {
                        //loop over hand to find the letter in our hand
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
                /* if the word created from the pre and postfixes isn't a word
                 * stop trying to build the word on the board*/
                else{return;}
            }
            /* if the letter is already on the board just add the value of the
             * letter */
            else{
                wordScore += values[ch - 'a'];
            }
            /* move left on the board and decrement the position to get in the
            * word*/
            playPos = board.getLeft(playPos);
            wordLength--;
        }
        //multiply the wordscore found by the word multiplier
        wordScore *= wordMult;
        /* if the anchor square is filled calculate the score and deterimine if
        * it is a high score*/
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

    /* leftPart is for finding prefixes if the space(s) to the left of the
    * anchor. every time it finds another part of a prefix it sends that to
    * extend right to create the rest of the word. it works recursively when
    * it finds a letter to add on it adds it and decriments the limit and gets
    * the next trie node and recurses.*/
    public void leftPart(String partialWord,TrieNode node,int row,int col
            ,int limit,int anchorRow,int anchorCol){
        HashMap<Character,TrieNode> children = node.getChildren();
        extendRight(partialWord,node,row,col,anchorRow,anchorCol);
        if(limit > 0) {
            /* loop over the letters that can be connected to the prefix
             * already there*/
            children.forEach((key, value) -> {
                /* loop over our hand if the hand location is a Blank(*) we
                * recurse setting the blank as an uppercase version of the next
                * child letter in the map.*/
                for(int i = 0; i < charHand.size();i++){
                    if(charHand.get(i) == '*'){
                        charHand.remove(i);
                        leftPart(
                             partialWord + Character.toUpperCase(key),
                                children.get(key),row,col,limit -1,
                                anchorRow,anchorCol);
                        charHand.add('*');
                    }
                    /* if the next letter in our hand isn't a blank and is the
                    * same as the map letter and recurses*/
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

    /* extendRight will build words recursively until it finds a word that is
    * in our dictionary then it will call legalMove to determine if it is an
    * acceptable play. it works similarly to the leftPart method where it
    * loops over all the possible children for the given node checking for blank
    * tiles in the hand. One difference is that ever recursive call moves the
    * column to the right when a new letter is found so when it gets to a point
    * where it finds a word it checks that the current space it is on, which is
    * one to the right of the end of the word isn't filled before it tries to
    * play the word.*/
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
    /* legalMoveDown works exactly the same as legalMoveAcross except it is for
    * words being played vertically*/
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
    /* upPart works the same as leftPart except it is for up and down words*/
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
    /* extendDown works the same as extendRight except it is fore up and down
    * words*/
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
    /* getLettersLeft loops left on the board while the space has a tile on it
    * to find prefixes already on the board*/
    public String getLettersLeft(int row,int col){
        int left = board.getLeft(col);
        String lettersLeft = "";
        while(board.isFilled(row,left)){
            lettersLeft = board.getTileLetter(row,left) + lettersLeft;
            left = board.getLeft(left);
        }
        return lettersLeft;
    }
    /* getLettersRight loops right on the board while a space has letters on it
    * to get postfixes that are already on the board*/
    public String getLettersRight(int row,int col){
        int right = board.getRight(col);
        String lettersRight = "";
        while(board.isFilled(row,right)){
            lettersRight = board.getTileLetter(row,right) + lettersRight;
            right = board.getRight(right);
        }
        return lettersRight;
    }
    /* getLettersUp loops upward on the board while the space has a tile on it
     * to find prefixes already on the board*/
    public String getLettersUp(int row,int col){
        int up = board.getUp(row);
        String lettersUp = "";
        while(board.isFilled(up,col)){
            lettersUp = board.getTileLetter(up,col) + lettersUp;
            up = board.getUp(up);
        }
        return lettersUp;
    }
    /* getLettersDown loops down on the board while a space has letters on it
     * to get postfixes that are already on the board*/
    public String getLettersDown(int row,int col){
        int down = board.getDown(row);
        String lettersDown = "";
        while(board.isFilled(down,col)){
            lettersDown = board.getTileLetter(down,col) + lettersDown;
            down = board.getDown(down);
        }
        return lettersDown;
    }
    /* this creats a character array for easier checking of what is in our hand*/
    public void handToCharArray(){
        for (Tile tile : hand) {
            charHand.add(tile.getLetter());
        }
    }
    /* Score finds the score of all words adjacent to the new word being played
    * on the board*/
    public int Score(Board temp, ArrayList<Space> playedSpaces,Boolean bonus
            ,Boolean across,int wordScore){
        int score = 0;
        int row;
        int col;
        //loop over every spcae where a new tile was played
        for(Space space: playedSpaces){
            row = space.getRow();
            col = space.getCol();
            Space boardSpace = temp.getSpace(row,col);
            Tile tile = boardSpace.getTile();
            int tileMult = boardSpace.getTileMult();
            int sideWordScore = 0;
            //determine if the play was across or up down
            if(across){
                String up;
                String down;
                /*check if the space above a played tile is filled. if it is,
                 * get the prefix above.*/
                if(temp.isFilled(temp.getUp(row),col)) {
                    up = getLettersUp(row, col);
                    /* loop over the prefix and get its score*/
                    for(int i = 0; i < up.length();i++){
                        char upChar = up.charAt(i);
                        if(!Character.isUpperCase(upChar)){
                            sideWordScore += values[upChar - 'a'];
                        }
                    }
                }
                /* check if the space belove a played tile is filled. if it is,
                * get the postfix below*/
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
            //for up down moves
            else{
                String left;
                String right;
                /* check if the space left of a played tile is filled. if it is,
                * get the prefix to the left*/
                if(temp.isFilled(row,temp.getLeft(col))) {
                    left = getLettersLeft(row, col);
                    for(int i = 0; i < left.length();i++){
                        char leftChar = left.charAt(i);
                        if(!Character.isUpperCase(leftChar)){
                            sideWordScore += values[leftChar - 'a'];
                        }
                    }
                }
                /* check if the space right of a played tile is filled. if it
                 * is, get the postfix to the left*/
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
            /* if there were some adjacent letters we add the letter that was
            * played on the board as well as multiplying by any tile multiplier
            * and word multiplier*/
            if(sideWordScore != 0) {
                if(Character.isLowerCase(tile.getLetter())) {
                    sideWordScore += (values[tile.getLetter() - 'a'] * tileMult);
                }
                sideWordScore *= boardSpace.getWordMult();
                score += sideWordScore;
            }
        }
        score += wordScore;
        //if 7 tiles were played add 50 points to the score
        if(bonus){
            score += 50;
        }
        return score;
    }
}
