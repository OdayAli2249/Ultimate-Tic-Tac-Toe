package Ultimate_Tic_Tac_Toe;

import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;

/**
 *
 *
 */
public class State {

    private int width;

    public int getLast_move_r() {
        return last_move_r;
    }

    public int getLast_move_c() {
        return last_move_c;
    }

    public void setLast_move_r(int last_move_r) {
        this.last_move_r = last_move_r;
    }

    public void setLast_move_c(int last_move_c) {
        this.last_move_c = last_move_c;
    }

    public int[][] getCellNum() {
        return cellNum;
    }

    public List<Pair<Integer, Integer>> getDomains() {
        return domains;
    }

    public void setDomains(List<Pair<Integer, Integer>> domains) {
        this.domains = domains;
    }

    public void setCellNum(int[][] cellNum) {
        this.cellNum = cellNum;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public char[][] getBoard() {
        return board;
    }

    private char[][] board;
    private List< Pair<Integer, Integer>> domains = new LinkedList<>();
    private List< Character> domainsWinners = new LinkedList<>();
    private int[][] cellNum;

    public void setDomainsWinners(List<Character> domainsWinners) {
        this.domainsWinners = domainsWinners;
    }

    public List<Character> getDomainsWinners() {
        return domainsWinners;
    }

    private int last_move_r = -1;
    private int last_move_c = -1;

    public State(int width) {
        this.width = width;
        board = new char[width][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = ' ';
            }
        }

        Pair<Integer, Integer> t;
        for (int i = 1; i <= 7; i += 3) {
            for (int j = 1; j <= 7; j += 3) {
                t = new Pair<Integer, Integer>(i - 1, j - 1);
                domains.add(t);
                domainsWinners.add('N');
            }
        }

        cellNum = new int[12][12];
        for (int c = 0; c < 9; c++) {
            t = domains.get(c);
            int num = 0;
            for (int i = t.getKey(); i <= t.getKey() + 2; i++) {

                for (int j = t.getValue(); j <= t.getValue() + 2; j++) {
                    cellNum[i][j] = num;
                    num++;
                }
            }

        }

    }

    public State(State state) {

        this.width = state.width;
        board = new char[width][width];

        for (int i = 0; i < width; i++) {
            System.arraycopy(state.board[i], 0, board[i], 0, width);
        }

        Pair<Integer, Integer> t;
        for (int i = 1; i <= 7; i += 3) {
            for (int j = 1; j <= 7; j += 3) {
                t = new Pair<Integer, Integer>(i - 1, j - 1);
                domains.add(t);

            }
        }
        for (int i = 0; i < state.getDomainsWinners().size(); i++) {
            this.domainsWinners.add(state.getDomainsWinners().get(i));
        }

        cellNum = new int[12][12];
        for (int c = 0; c < 9; c++) {
            t = domains.get(c);
            int num = 0;
            for (int i = t.getKey(); i <= t.getKey() + 2; i++) {

                for (int j = t.getValue(); j <= t.getValue() + 2; j++) {
                    cellNum[i][j] = num;
                    num++;
                }
            }

        }

    }

    public int getWidth() {
        return this.width;
    }

    public void play(int x, int y, char ch) {
        board[x][y] = ch;
        this.last_move_r = x;
        this.last_move_c = y;

        Pair<Integer, Integer> t;
        int dnum = 0;
        for (int di = 0; di < 9; di++) {
            t = domains.get(di);
            if (x >= t.getKey() && x <= t.getKey() + 2 && y >= t.getValue() && y <= t.getValue() + 2) {
                dnum = di;
                break;
            }

        }

        this.isClosed(dnum);
    }

    public List<State> getAllPossibleMoves(char ch) {

        List<State> nextBoards = new LinkedList<>();

        Pair<Integer, Integer> t = this.domains.get(this.cellNum[this.last_move_r][this.last_move_c]);

        for (int i = t.getKey(); i <= t.getKey() + 2; i++) {

            for (int j = t.getValue(); j <= t.getValue() + 2; j++) {
                if (board[i][j] == ' ') {
                    State nextBoard = new State(this);
                    nextBoard.play(i, j, ch);
                    nextBoards.add(nextBoard);
                }
            }
        }

        return nextBoards;
    }

    public int eval() {


        /*    your code here
        write a function to evaluate the state for a computer
        this fucntion should return a low value if the last move results in loss
        
        and an evalutation for the state that estimates the "goodness" of the state
        (the heigher value the better the state)
        
        for this week only: make the function return a low value if it results in
        loss and 0 otherwise (since you can't evalute a win case unless it happens)
        
         */
        //check rows
        int cnto = 0, cntx = 0;
        for (int i = 0; i < 9; i += 3) {
            boolean x = true, o = true;
            for (int j = i; j <= i + 2; j++) {
                if (domainsWinners.get(j) == 'X') {
                    o = false;
                }
                if (domainsWinners.get(j) == 'O') {
                    x = false;
                }

            }
            if (x) {
                cntx++;
            }
            if (o) {
                cnto++;
            }

        }

        //check columns
        for (int i = 0; i < 3; i++) {
            boolean x = true, o = true;
            for (int j = i; j <= i + 6; j += 3) {
                if (domainsWinners.get(j) == 'X') {
                    o = false;
                }
                if (domainsWinners.get(j) == 'O') {
                    x = false;
                }
            }
            if (x) {
                cntx++;
            }
            if (o) {
                cnto++;
            }
        }
        // check diag 1
        boolean x = true, o = true;
        for (int i = 0; i <= 8; i += 4) {
            if (domainsWinners.get(i) == 'X') {
                o = false;
            }
            if (domainsWinners.get(i) == 'O') {
                x = false;
            }
        }
        if (x) {
            cntx++;
        }
        if (o) {
            cnto++;
        }

        // check diag 2
        x = o = true;
        for (int i = 2; i <= 6; i += 2) {
            if (domainsWinners.get(i) == 'X') {
                o = false;
            }
            if (domainsWinners.get(i) == 'O') {
                x = false;
            }
        }
        if (x) {
            cntx++;
        }
        if (o) {
            cnto++;
        }

        return cnto - cntx;
    }

    public void isClosed(int domainInd) {
        Pair<Integer, Integer> t = domains.get(domainInd);
        int row = t.getKey();
        int col = t.getValue();

        ////check rows
        for (int i = row; i <= row + 2; i++) {
            //check row
            List<Character> collected_row = new LinkedList<>();
            for (int j = col; j <= col + 2; j++) {
                collected_row.add(board[i][j]);
            }
            if (all_X(collected_row)) {
                domainsWinners.set(domainInd, 'X');
               // System.out.println("X..done");
                return;
            }
            if (all_O(collected_row)) {
                domainsWinners.set(domainInd, 'O');
               // System.out.println("O..done");
                return;
            }
        }

        //check columns
        for (int j = col; j <= col + 2; j++) {
            //check col
            List<Character> collected_col = new LinkedList<>();
            for (int i = row; i <= row + 2; i++) {
                collected_col.add(board[i][j]);
            }
            if (all_X(collected_col)) {
                domainsWinners.set(domainInd, 'X');
                //  System.out.println("X..done");
                return;
            }
            if (all_O(collected_col)) {
                domainsWinners.set(domainInd, 'O');
                  //System.out.println("O..done");
                return;
            }
        }

        // check diag 1
        if (last_move_r == last_move_c) {
            List<Character> collected_d1 = new LinkedList<>();

            for (int i = row; i <= row + 2; i++) {
                collected_d1.add(board[i][i]);
            }
            if (all_X(collected_d1)) {
                domainsWinners.set(domainInd, 'X');
                // System.out.println("X..done");
                return;
            }
            if (all_O(collected_d1)) {
                domainsWinners.set(domainInd, 'O');
                 // System.out.println("O..done");
                return;
            }
        }

        // check diag 2
        if (last_move_r == (width - (last_move_c + 1))) {
            List<Character> collected_d2 = new LinkedList<>();
            for (int i = row; i <= row + 2; i++) {
                collected_d2.add(board[i][width - (i + 1)]);
            }
            if (all_X(collected_d2)) {
                domainsWinners.set(domainInd, 'X');
                 //System.out.println("X..done");
                return;
            }
            if (all_O(collected_d2)) {
                domainsWinners.set(domainInd, 'O');
                // System.out.println("O..done");
                return;
            }
        }
        return;
    }

    public boolean isWon() {

        //check rows
        for (int i = 0; i < 9; i += 3) {
            boolean x = true, o = true;
            for (int j = i; j <= i + 2; j++) {
                if (domainsWinners.get(j) == 'X') {
                    o = false;
                }
                if (domainsWinners.get(j) == 'O') {
                    x = false;
                }
                if (domainsWinners.get(j) == 'N') {
                    o = false;
                    x = false;
                    
                }
            }
            if (x || o) {
                return true;
            }
        }

        //check columns
        for (int i = 0; i < 3; i++) {
            boolean x = true, o = true;
            for (int j = i; j <= i + 6; j += 3) {
                if (domainsWinners.get(j) == 'X') {
                    o = false;
                }
                if (domainsWinners.get(j) == 'O') {
                    x = false;
                }
                if (domainsWinners.get(j) == 'N') {
                    o = false;
                    x = false;
                }
            }
            if (x || o) {
                return true;
            }
        }

        // check diag 1
        boolean x = true, o = true;
        for (int i = 0; i <= 8; i += 4) {
            if (domainsWinners.get(i) == 'X') {
                o = false;
            }
            if (domainsWinners.get(i) == 'O') {
                x = false;
            }
            if (domainsWinners.get(i) == 'N') {
                o = false;
                x = false;
            }
        }
        if (x || o) {
            return true;
        }

        // check diag 2
        x = o = true;
        for (int i = 2; i <= 6; i += 2) {
            if (domainsWinners.get(i) == 'X') {
                o = false;
            }
            if (domainsWinners.get(i) == 'O') {
                x = false;
            }
            if (domainsWinners.get(i) == 'N') {
                o = false;
                x = false;
            }
        }
        if (x || o) {
            return true;
        }

        return false;
    }

    private boolean all_X(List<Character> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != 'X') {
                return false;
            }
        }
        return true;
    }

    private boolean all_O(List<Character> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != 'O') {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("   | ");
        for (int i = 0; i < width; i++) {
            sb.append(i + 1);
            if ((i + 1) % 3 == 0 && i != 0 && i != 8) {
                sb.append(" |     | ");
            } else {
                sb.append(" | ");
            }
        }
        sb.delete(sb.length() - 1, sb.length() - 1);
        sb.append("\n");
        sb.append("   ");
        for (int i = 0; i < width + 40; i++) {
            sb.append(".");
        }
        sb.delete(sb.length() - 1, sb.length() - 1);
        sb.append("\n");
        for (int i = 0; i < width; i++) {

            sb.append(i + 1);
            sb.append(": | ");

            for (int j = 0; j < width; j++) {
                sb.append(board[i][j]);
                if ((j + 1) % 3 == 0 && j != 0 && j != 8) {
                    sb.append(" |     | ");
                } else {
                    sb.append(" | ");
                }

            }
            if ((i + 1) % 3 == 0) {
                sb.append("\n");
            }
            sb.delete(sb.length() - 1, sb.length() - 1);
            sb.append('\n');
        }
        return sb.toString();
    }

    // public static void main(String[] args) {
//        State board = new State(3);
//        board.play(1, 1);
//        board.play(0, 1);
//        board.play(2, 2);
//        System.out.println(board);
//        boolean val = board.isLoseOnLastMove();
//        System.out.println(val);
//
//        List<State> next = board.allNextMoves();
//        int i = 1;
//        for (State b : next) {
//            System.out.println(i + ": (" + b.eval() + ")");
//            System.out.println(b);
//            val = b.isLoseOnLastMove();
//            System.out.println(val);
//            i++;
//        }
    //}
}
