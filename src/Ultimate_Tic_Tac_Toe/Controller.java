package Ultimate_Tic_Tac_Toe;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;

/**
 *
 *
 */
public class Controller {

    State board = new State(9);

    public void gameOn() {
        System.out.println(board);

        /*for (int i = 0; i < board.getDomainsWinners().size(); i++) {
            System.out.println(i + "," + board.getDomainsWinners().get(i));
        }*/
        while (true) {
            System.out.println("************************USER************************");
            getUserMove();
            System.out.println(board);
            if (board.isWon()) {
                System.out.println("User Wins");
                break;
            }

            System.out.println("**********************Computer**********************");
            getComputerMove();
            System.out.println(board);
            if (board.isWon()) {
                System.out.println("Computer Wins");
                break;
            }

        }

    }

    private void getUserMove() {
        Scanner s = new Scanner(System.in);
        int x, y;
        while (true) {
            int row;
            while (true) {
                System.out.print("Enter row: ");
                row = s.nextInt();
                System.out.println();
                if ((row > 0) && (row < board.getWidth() + 1)) {
                    break;
                }
            }
            int col;
            while (true) {
                System.out.print("Enter column: ");
                col = s.nextInt();
                System.out.println();
                if ((col > 0) && (col < board.getWidth() + 1)) {
                    break;
                }
            }

            x = row - 1;
            y = col - 1;
            Pair<Integer, Integer> t;
            int dnum = 0;

            for (int di = 0; di < 9; di++) {
                t = board.getDomains().get(di);
                if (x >= t.getKey() && x <= t.getKey() + 2 && y >= t.getValue() && y <= t.getValue() + 2) {
                    dnum = di;
                }

            }

            if (board.getDomainsWinners().get(dnum) == 'N') {

                int lr = board.getLast_move_r();
                int lc = board.getLast_move_c();

                if (lr == -1) {
                    break;
                }

                dnum = board.getCellNum()[lr][lc];
                t = board.getDomains().get(dnum);
                if (board.getDomainsWinners().get(dnum) != 'N') {
                    break;
                }
                if (x >= t.getKey() && x <= t.getKey() + 2 && y >= t.getValue() && y <= t.getValue() + 2) {
                    break;
                }
            }
        }

        board.play(x, y, 'X');
    }

    public static void main(String[] args) {
        Controller g = new Controller();
        g.gameOn();
    }

    private void getComputerMove() {

        int x = 0, y = 0;
        List<State> next = board.getAllPossibleMoves('O');
        int best = (int) -1e9;
        int depthLimit = 4;
        for (State son : next) {
            Pair<Integer, State> cur = maxMove(son, depthLimit);
            if (cur.getKey() > best) {
                best = cur.getKey();
                Pair<Integer, Integer> t = getCoords(board, son);
                x = t.getKey();
                y = t.getValue();
            }
        }
        board.play(x, y, 'O');

        // Alpha-Beta
        /*
        int alpha = (int) -1e9;
        int beta = (int) 1e9;
        for (State son : next) {
            Pair<Integer, State> sonPart = maxMoveAlphaBeta(son, alpha, beta, depthLimit);
            if (best < sonPart.getKey()) {
                best = sonPart.getKey();
                board = son;
            }
        }
         */
    }

    private Pair<Integer, Integer> getCoords(State b1, State b2) {
        for (int i = 0; i < b1.getWidth(); i++) {
            for (int j = 0; j < b1.getWidth(); j++) {
                if (b1.getBoard()[i][j] != b2.getBoard()[i][j]) {
                    return new Pair<Integer, Integer>(i, j);
                }
            }
        }
        return new Pair<Integer, Integer>(0, 0);
    }

    private Pair<Integer, State> maxMove(State b, int depthLimit) {

        if (b.isWon() || depthLimit == 0) {
            Pair<Integer, State> best = new Pair<Integer, State>(b.eval(), b);
            return best;
        } else {
            Pair<Integer, State> best = new Pair<Integer, State>(b.eval(), b);
            List<State> next = b.getAllPossibleMoves('X');
            for (State son : next) {
                Pair<Integer, State> sonPart = minMove(son, depthLimit - 1);
                if (sonPart.getKey() > best.getKey()) {
                    best = sonPart;
                }
            }
            return best;
        }

    }

    private Pair<Integer, State> minMove(State b, int depthLimit) {

        if (b.isWon() || depthLimit == 0) {
            Pair<Integer, State> best = new Pair<Integer, State>(b.eval(), b);
            return best;
        } else {
            Pair<Integer, State> best = new Pair<Integer, State>(b.eval(), b);
            List<State> next = b.getAllPossibleMoves('X');
            for (State son : next) {

                Pair<Integer, State> sonPart = maxMove(son, depthLimit - 1);
                if (sonPart.getKey() < best.getKey()) {
                    best = sonPart;
                }

            }
            return best;
        }
    }

    /*
    private Pair<Integer, State> maxMoveAlphaBeta(State b, int alpha, int beta, int depthLimit) {


        if (b.isWon()  || depthLimit == 0) {
            Pair<Integer, State> best = new Pair<Integer, State>(b.eval(), b);
            return best;
        } else {
            Pair<Integer, State> best = new Pair<Integer, State>(b.eval(), b);
            List<State> next = b.getAllPossibleMoves();
            for (State son : next) {

                Pair<Integer, State> sonPart = minMoveAlphaBeta(son, alpha, beta, depthLimit - 1);
                if (alpha < sonPart.getKey()) {
                    alpha = sonPart.getKey();
                    best = sonPart;
                }
                if (alpha >= beta) {
                    return best;
                }

            }
            return best;
        }

    }
     */
 /*
    private Pair<Integer, State> minMoveAlphaBeta(State b, int alpha, int beta, int depthLimit) {


        if (b.isWon()  || depthLimit == 0) {
            Pair<Integer, State> best = new Pair<Integer, State>(b.eval(), b);
            return best;
        } else {
            Pair<Integer, State> best = new Pair<Integer, State>(b.eval(), b);
            List<State> next = b.getAllPossibleMoves();
            for (State son : next) {

                Pair<Integer, State> sonPart = maxMoveAlphaBeta(son, alpha, beta, depthLimit - 1);
                if (beta > sonPart.getKey()) {
                    beta = sonPart.getKey();
                    best = sonPart;
                }
                if (alpha >= beta) {
                    return best;
                }

            }
            return best;
        }

    }
     */
}
