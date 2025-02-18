import sac.*;
import sac.graph.AStar;
import sac.graph.GraphSearchAlgorithm;
import sac.graph.GraphState;
import sac.graph.GraphStateImpl;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

class slidingPuzzle extends GraphStateImpl {
    private byte [][] board;
    private int emptyCol;
    private int emptyRow;
    private int n;

    public slidingPuzzle(int n) {
        this.board = new byte[n][n];
        this.n = n;
        byte tmp = 0;
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j] = tmp;
                tmp++;
            }
        }
        this.emptyRow = 0;
        this.emptyCol = 0;
    }

    public slidingPuzzle(slidingPuzzle sp) {
        board = new byte[sp.n][sp.n];
        n = sp.n;
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                board[i][j] = sp.board[i][j];
            }
        }
        emptyRow = sp.emptyRow;
        emptyCol = sp.emptyCol;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(byte[]r:board){
            for(int i=0; i<r.length; i++){
                str.append(r[i] + " ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    @Override
    public List<GraphState> generateChildren() {
        List<GraphState> lst = new ArrayList<>();
        if (emptyRow != 0) {
            slidingPuzzle sp = new slidingPuzzle(this);
            sp.board[emptyRow][emptyCol] = sp.board[emptyRow-1][emptyCol];
            sp.board[emptyRow-1][emptyCol] = 0;
            sp.emptyRow -= 1;
            sp.setMoveName("D");
            lst.add(sp);
        }

        if (emptyRow != n-1) {
            slidingPuzzle sp = new slidingPuzzle(this);
            sp.board[emptyRow][emptyCol] = sp.board[emptyRow+1][emptyCol];
            sp.board[emptyRow+1][emptyCol] = 0;
            sp.emptyRow += 1;
            sp.setMoveName("U");
            lst.add(sp);
        }

        if (emptyCol != 0) {
            slidingPuzzle sp = new slidingPuzzle(this);
            sp.board[emptyRow][emptyCol] = sp.board[emptyRow][emptyCol-1];
            sp.board[emptyRow][emptyCol-1] = 0;
            sp.emptyCol -= 1;
            sp.setMoveName("R");
            lst.add(sp);
        }

        if (emptyCol != n-1) {
            slidingPuzzle sp = new slidingPuzzle(this);
            sp.board[emptyRow][emptyCol] = sp.board[emptyRow][emptyCol+1];
            sp.board[emptyRow][emptyCol+1] = 0;
            sp.emptyCol += 1;
            sp.setMoveName("L");
            lst.add(sp);
        }

        return lst;
    }

    @Override
    public boolean isSolution() {
        byte tmp = 0;
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(board[i][j] != tmp){
                    return false;
                }
                tmp++;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public int getMisplacedTilesValue() {
        int mis = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int c = board[i][j];
                if (i != c / n || j != c % n) {
                    if(c != 0){
                        mis++;
                    }
                }
            }
        }
        return mis;
    }

    public int getManhattanDistance() {
        int man = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int c = board[i][j];
                if (i != c / n || j != c % n) {
                    if(c != 0) {
                        man += Math.abs(i - c / n) + Math.abs(j - c % n);
                    }
                }
            }
        }
        return man;
    }

    public GraphState shuffle(int n) {
        Random rand = new Random();
        GraphState st = this;
        for (int i=0; i<n; i++){
            List<GraphState> children = st.generateChildren();
            st = children.get(rand.nextInt(children.size()));
        }
        return st;
    }


}

class HeuristicMisplacedTiles extends StateFunction {
    @Override
    public double calculate(State s) {
        if (s instanceof slidingPuzzle) {
            return ((slidingPuzzle) s).getMisplacedTilesValue();
        }
        else{
            return Double.NaN;
        }
    }
}

class HeuristicManhattan extends StateFunction {
    @Override
    public double calculate(State s) {
        if (s instanceof slidingPuzzle) {
            return ((slidingPuzzle) s).getManhattanDistance();
        }
        else{
            return Double.NaN;
        }
    }
}

public class Main {
    public static void expand (int n, GraphState gs) {
        if (n<=0){
            System.out.println(gs);
        }
        else {
            for(GraphState gst : gs.generateChildren()){
                expand(n-1, gst);
            }
        }
    }

    public static void main(String[] args) {
        /////////////////////
        int puzzleSize = 4;
        int problemsNumber = 10;
        int shuffleMoves = 30;
        /////////////////////


        StateFunction[] heuristics = {new HeuristicMisplacedTiles(), new HeuristicManhattan()};
        slidingPuzzle sp = new slidingPuzzle(puzzleSize);
        double[] pathLength = {0, 0};
        double[] closedStates = {0, 0};
        double[] openStates = {0, 0};
        double[] durationTime = {0, 0};
        GraphSearchAlgorithm alg = new AStar();
        for (int i = 0; i < problemsNumber; i++) {
            GraphState gs = sp.shuffle(shuffleMoves);
            alg.setInitial(gs);
            //System.out.println("Sliding puzzle to solve: \n" + gs);
            for (StateFunction h : heuristics) {
                slidingPuzzle.setHFunction(h);
                alg.execute();
                slidingPuzzle sol = (slidingPuzzle) alg.getSolutions().get(0);

                if(h instanceof HeuristicManhattan) {
                    pathLength[0] += sol.getPath().size();
                    closedStates[0] += alg.getClosedStatesCount();
                    openStates[0] += alg.getOpenSet().size();
                    durationTime[0] += alg.getDurationTime();
                }
                else{
                    pathLength[1] += sol.getPath().size();
                    closedStates[1] += alg.getClosedStatesCount();
                    openStates[1] += alg.getOpenSet().size();
                    durationTime[1] += alg.getDurationTime();
                }
            }
        }

        System.out.println("---------------------------------");
        System.out.println("n = " + puzzleSize + ", problemsNumber = " + problemsNumber + ", shuffleMoves = " + shuffleMoves);
        System.out.println("---------------------------------");
        System.out.println("Heuristic Manhattan: ");
        System.out.println("Average path length: " + pathLength[0]/problemsNumber);
        System.out.println("Average closed states: " + closedStates[0]/problemsNumber);
        System.out.println("Average open states: " + openStates[0]/problemsNumber);
        System.out.println("Average duration time: " + durationTime[0]/problemsNumber + " ms");
        System.out.println("---------------------------------");
        System.out.println("Heuristic Misplaced Tiles: ");
        System.out.println("Average path length: " + pathLength[1]/problemsNumber);
        System.out.println("Average closed states: " + closedStates[1]/problemsNumber);
        System.out.println("Average open states: " + openStates[1]/problemsNumber);
        System.out.println("Average duration time: " + durationTime[1]/problemsNumber + " ms");
        System.out.println("---------------------------------");
    }
}