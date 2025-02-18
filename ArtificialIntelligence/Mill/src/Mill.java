import sac.State;
import sac.StateFunction;
import sac.game.GameState;
import sac.game.GameStateImpl;
import java.util.ArrayList;
import java.util.List;


public class Mill extends GameStateImpl{
    public char[][] board;
    public int whitePawns;
    public int blackPawns;
    public int pawnsToPlace;

    Mill() {
        this.board = new char[3][8];
        this.pawnsToPlace = 18;
        this.whitePawns = 0;
        this.blackPawns = 0;
        this.maximizingTurnNow = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = ' ';
            }
        }
    }

    public Mill (Mill mill) {
        this.board = new char[3][8];
        for (int r = 0; r < 3; r++) {
            System.arraycopy(mill.board[r], 0, this.board[r], 0, 8);
        }
        this.pawnsToPlace = mill.pawnsToPlace;
        this.whitePawns = mill.whitePawns;
        this.blackPawns = mill.blackPawns;
        this.maximizingTurnNow = mill.maximizingTurnNow;
    }

    @Override
    public String toString() {
        String str = "";
        str += board[0][6] + " - - - - - " + board[0][5] + " - - - - - " + board[0][4] + "\n";
        str += "|           |           |\n";
        str += "|   " + board[1][6] + " - - - " + board[1][5] + " - - - "  + board[1][4] + "   |\n";
        str += "|   |       |       |   |\n";
        str += "|   |   " + board[2][6] + " - " + board[2][5] + " - " + board[2][4] + "   |   |\n";
        str += "|   |   |       |   |   |\n";
        str += board[0][7] + " - " + board[1][7] + " - " + board[2][7] + "       " + board[2][3] + " - " + board[1][3] + " - " + board[0][3] + "\n";
        str += "|   |   |       |   |   |\n";
        str += "|   |   " + board[2][0] + " - " + board[2][1] + " - " + board[2][2] + "   |   |\n";
        str += "|   |       |       |   |\n";
        str += "|   " + board[1][0] + " - - - " + board[1][1] + " - - - " + board[1][2] + "   |\n";
        str += "|           |           |\n";
        str += board[0][0] + " - - - - - " + board[0][1] + " - - - - - " + board[0][2] + "\n";
        return str;

    }

    @Override
    public int hashCode() {
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                board.append(this.board[i][j]);
            }
        }
        return board.hashCode();
    }

    @Override
    public List<GameState> generateChildren() {
        List<GameState> children = new ArrayList<>();
        if (pawnsToPlace > 0) {
            children.addAll(firstPhase());
        }
        else if (maximizingTurnNow && whitePawns == 3 || !maximizingTurnNow && blackPawns == 3) {
            children.addAll(thirdPhase());
        }
        else if (maximizingTurnNow && whitePawns > 3 || !maximizingTurnNow && blackPawns > 3) {
                children.addAll(secondPhase());
            }
        return children;
    }

    private int mod(int a, int b) {
        return (a % b + b) % b;
    }

    public int[][] getNeighbours(int r, int c) {
        if (c%2 == 0){
            return new int[][]{{r, mod(c+1, 8)}, {r, mod(c-1, 8)}};
        }
        else {
            if(r == 0) {
                return new int[][]{{r, mod(c+1, 8)}, {r, mod(c-1, 8)}, {1, c}};
            }
            else if (r == 1) {
                return new int[][]{{r, mod(c+1, 8)}, {r, mod(c-1, 8)}, {0, c}, {2, c}};
            }
            else {
                return new int[][]{{r, mod(c+1, 8)}, {r, mod(c-1, 8)}, {1, c}};
            }

        }
    }

    public boolean isMill(int r, int c) {
        if (board[r][c] == ' ') return false;
        if (c % 2 == 0) {
            if (board[r][c] == board[r][mod(c + 1, 8)] && board[r][c] == board[r][mod(c + 2, 8)]) {
                return true;
            }
            if (board[r][c] == board[r][mod(c - 1, 8)] && board[r][c] == board[r][mod(c - 2, 8)]) {
                return true;
            }
        } else {
            if (board[r][c] == board[r][mod(c + 1, 8)] && board[r][c] == board[r][mod(c - 1, 8)]) {
                return true;
            }
            if (board[r][c] == board[mod(r + 1, 3)][c] && board[r][c] == board[mod(r + 2, 3)][c]) {
                return true;
            }
        }
        return false;
    }

    public List<GameState> solveMill() {
        List<GameState> lst = new ArrayList<>();
        char enemyPlayer;
        boolean isRemoved = false;
        if (maximizingTurnNow) {
            enemyPlayer = 'B';
        }
        else {
            enemyPlayer = 'W';
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == enemyPlayer && !isMill(i, j)) { //piece not in mill
                    Mill mill = new Mill(this);
                    mill.board[i][j] = ' ';
                    if (maximizingTurnNow) {
                        mill.blackPawns--;
                    } else {
                        mill.whitePawns--;
                    }
                    isRemoved = true;
                    mill.maximizingTurnNow = !this.maximizingTurnNow;
                    mill.setMoveName(i + " " + j);
                    lst.add(mill);
                }
            }
        }
        if (!isRemoved) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 8; j++){
                    if (board[i][j] == enemyPlayer) { //piece in mill
                        Mill mill = new Mill(this);
                        mill.board[i][j] = ' ';
                        if (maximizingTurnNow) {
                            mill.blackPawns--;
                        } else {
                            mill.whitePawns--;
                        }
                        mill.maximizingTurnNow = !this.maximizingTurnNow;
                        mill.setMoveName(i + " " + j);
                        lst.add(mill);
                    }
                }
            }
        }
        return lst;
    }


    public List<GameState> firstPhase() {
        List<GameState> lst = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == ' ') {
                    Mill mill = new Mill(this);
                    if(maximizingTurnNow) {
                        mill.board[i][j] = 'W';
                        mill.whitePawns++;
                    } else {
                        mill.board[i][j] = 'B';
                        mill.blackPawns++;
                    }
                    mill.pawnsToPlace--;
                    if(mill.isMill(i, j)) {
                        List<GameState> tmp = mill.solveMill();
                        for (GameState g : tmp) {
                            g.setMoveName(i + " " + j + " " + g.getMoveName());
                        }
                        lst.addAll(tmp);
                    }
                    else {
                        mill.setMoveName(i + " " + j);
                        mill.maximizingTurnNow = !mill.maximizingTurnNow;
                        lst.add(mill);
                    }
                }
            }
        }
        return lst;
    }



    public List<GameState> secondPhase() {
        List<GameState> lst = new ArrayList<>();
        int[][] neighbours = {};
        char currentPlayer;
        if (maximizingTurnNow) {
            assert (whitePawns > 3);
            currentPlayer = 'W';
        } else {
            assert(blackPawns > 3);
            currentPlayer = 'B';
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] == currentPlayer){
                    neighbours = getNeighbours(i, j);
                    for (int[] neighbour : neighbours){
                        if (board[neighbour[0]][neighbour[1]] == ' ') {
                            Mill mill = new Mill(this);
                            mill.board[i][j] = ' ';
                            mill.board[neighbour[0]][neighbour[1]] = currentPlayer;

                            if (mill.isMill(neighbour[0], neighbour[1])) {
                                List<GameState> tmp = mill.solveMill();
                                for (GameState g : tmp) {
                                    g.setMoveName(i + " " + j + " " + neighbour[0] + " " + neighbour[1] + " " + g.getMoveName());
                                }
                                lst.addAll(tmp);
                            }
                            else {
                                mill.maximizingTurnNow = !this.maximizingTurnNow;
                                mill.setMoveName(i + " " + j + " " + neighbour[0] + " " + neighbour[1]);
                                lst.add(mill);
                            }
                        }
                    }
                }
            }
        }
        return lst;
    }

    public List<GameState> thirdPhase() {
        List<GameState> lst = new ArrayList<>();
        char currentPlayer;
        if (maximizingTurnNow) {
            assert (whitePawns == 3);
            currentPlayer = 'W';
        } else {
            assert (blackPawns == 3);
            currentPlayer = 'B';
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] == currentPlayer){
                    for (int k = 0; k < 3; k++){
                        for (int l = 0; l < 8; l++) {
                            if (board[k][l] == ' ') {
                                Mill mill = new Mill(this);
                                mill.board[i][j] = ' ';
                                mill.board[k][l] = currentPlayer;
                                if (mill.isMill(k, l)) {
                                    List<GameState> tmp = mill.solveMill();
                                    for (GameState g : tmp) {
                                        g.setMoveName(i + " " + j + " " + k + " " + l + " " + g.getMoveName());
                                    }
                                    lst.addAll(tmp);
                                }
                                else {
                                    mill.maximizingTurnNow = !this.maximizingTurnNow;
                                    mill.setMoveName(i + " " + j + " " + k + " " + l);
                                    lst.add(mill);
                                }
                            }
                        }
                    }
                }
            }
        }
        return lst;
    }

    public String stringBoard () {
        StringBuilder sbB = new StringBuilder();
        StringBuilder sbW = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'B') {
                    sbB.append(i*8+j).append(" ");
                }
                if (board[i][j] == 'W') {
                    sbW.append(i*8+j).append(" ");
                }
            }
        }
        return sbW.toString() + "| " + sbB.toString() + "| ";
    }

    List<GameState> legalMoves(char player) {
        List<GameState> lst = new ArrayList<>();
        int[][] neighbours;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == player) {
                    neighbours = getNeighbours(i, j);
                    for (int[] neighbour : neighbours) {
                        if (board[neighbour[0]][neighbour[1]] == ' ') {
                            Mill mill = new Mill(this);
                            mill.board[i][j] = ' ';
                            mill.board[neighbour[0]][neighbour[1]] = player;
                            lst.add(mill);
                        }
                    }
                }
            }
        }
        return lst;
    }

    @Override
    public boolean isNonWinTerminal() {
        if (maximizingTurnNow && pawnsToPlace == 0) {
            if(legalMoves('W').isEmpty()) {
                return true;
            }
        }
        else if (!maximizingTurnNow && pawnsToPlace == 0) {
            if(legalMoves('B').isEmpty()) {
                return true;
            }
        }
        return false;
    }

}

class heuristic_value extends StateFunction {
    @Override
    public double calculate(State state) {
        Mill mill = (Mill) state;
        int whitePawns = mill.whitePawns;
        int blackPawns = mill.blackPawns;
        int pawnsToPlace = mill.pawnsToPlace;

        if (pawnsToPlace <=0) {

            if (blackPawns < 3) {
                return Double.POSITIVE_INFINITY;
            } else if (whitePawns < 3) {
                return Double.NEGATIVE_INFINITY;
            }
        }
        return whitePawns - blackPawns;
    }
}