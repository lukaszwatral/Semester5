import sac.StateFunction;
import sac.game.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void expand(GameState s, int d, int[] cnt){

        if(d == cnt.length){
            return;
        }
        cnt[d]++;
        for(GameState t : s.generateChildren()){
            expand(t, d+1, cnt);
        }
    }
    public static void expand(GameState s, int d){
        int[] cnt = new int[d];
        expand(s, 0, cnt);
        for(int i = 1; i < d; i++){
            System.out.println("Depth " + i + ": " + cnt[i]);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameState game = new Mill();
        GameSearchConfigurator configurator = new GameSearchConfigurator();
        configurator.setDepthLimit(3);
        GameSearchAlgorithm alg = new AlphaBetaPruning(game, configurator);
        Mill.setHFunction(new heuristic_value());
        String move;

        //0 - play game (Human starts)
        //1 - expand tree case 1
        //2 - expand tree case 2
        //3 - expand tree case 3
        //4 - play ending of the game
        //5 - play game (AI starts)

        int test_case = 0;




        switch(test_case) {
            case 0:
                while (!game.isWinTerminal() && !game.isNonWinTerminal()) {
                    List<GameState> children = game.generateChildren();
                    System.out.println("Current board:");
                    System.out.println(game);
                    System.out.println("Enter your move: ");
                    move = scanner.nextLine();
                    boolean validMove = false;
                    for (GameState child : children) {
                        if (move.equals(child.getMoveName())) {
                            game = child;
                            validMove = true;
                            break;
                        }
                    }
                    if (!validMove) {
                        System.out.println("Invalid move. Try again.");
                        continue;
                    }
                    if (game.isWinTerminal() || game.isNonWinTerminal()) {
                        break;
                    }
                    System.out.println("AI's turn:");
                    children = game.generateChildren();
                    alg.setInitial(game);
                    alg.execute();
                    move = alg.getFirstBestMove();
                    for (GameState child : children) {
                        if (move.equals(child.getMoveName())) {
                            game = child;
                            break;
                        }
                    }
                }
                if (game.isMaximizingTurnNow()) {
                    System.out.println("You win!");
                }
                else {
                    System.out.println("AI wins!");
                }
                break;
            case 1:
                expand(game, 6);
                break;
            case 2:
                ((Mill)game).board[0][1] = 'W';
                game.setMaximizingTurnNow(false);
                expand(game, 6);
                break;
            case 3:
                ((Mill)game).board[0][0] = 'W';
                ((Mill)game).board[0][3] = 'W';
                ((Mill)game).board[1][4] = 'W';
                ((Mill)game).board[1][5] = 'W';
                ((Mill)game).board[1][6] = 'W';
                ((Mill)game).board[2][3] = 'W';
                ((Mill)game).board[2][7] = 'W';
                ((Mill)game).board[0][1] = 'B';
                ((Mill)game).board[1][0] = 'B';
                ((Mill)game).board[0][5] = 'B';
                ((Mill)game).board[2][5] = 'B';
                ((Mill)game).whitePawns = 7;
                ((Mill)game).blackPawns = 4;
                ((Mill)game).pawnsToPlace = 0;
                expand(game, 6);
                break;
            case 4:
                ((Mill)game).board[0][0] = 'W';
                ((Mill)game).board[0][3] = 'W';
                ((Mill)game).board[1][4] = 'W';
                ((Mill)game).board[1][5] = 'W';
                ((Mill)game).board[1][6] = 'W';
                ((Mill)game).board[2][3] = 'W';
                ((Mill)game).board[2][7] = 'W';
                ((Mill)game).board[0][1] = 'B';
                ((Mill)game).board[1][0] = 'B';
                ((Mill)game).board[0][5] = 'B';
                ((Mill)game).board[2][5] = 'B';
                ((Mill)game).whitePawns = 7;
                ((Mill)game).blackPawns = 4;
                ((Mill)game).pawnsToPlace = 0;
                expand(game, 6);
                while (!game.isWinTerminal() && !game.isNonWinTerminal()) {
                    List<GameState> children = game.generateChildren();
                    System.out.println("Current board:");
                    System.out.println(game);
                    System.out.println("Enter your move: ");
                    move = scanner.nextLine();
                    boolean validMove = false;
                    for (GameState child : children) {
                        if (move.equals(child.getMoveName())) {
                            game = child;
                            validMove = true;
                            break;
                        }
                    }
                    if (!validMove) {
                        System.out.println("Invalid move. Try again.");
                        continue;
                    }
                    if (game.isWinTerminal() || game.isNonWinTerminal()) {
                        break;
                    }
                    System.out.println("AI's turn:");
                    children = game.generateChildren();
                    alg.setInitial(game);
                    alg.execute();
                    move = alg.getFirstBestMove();
                    for (GameState child : children) {
                        if (move.equals(child.getMoveName())) {
                            game = child;
                            break;
                        }
                    }
                }
                if (game.isMaximizingTurnNow()) {
                    System.out.println("You win!");
                }
                else {
                    System.out.println("AI wins!");
                }
                break;

            case 5:
                while (!game.isWinTerminal() && !game.isNonWinTerminal()) {
                    if (game.isMaximizingTurnNow()) {
                        System.out.println("AI's turn:");
                        List<GameState> children = game.generateChildren();
                        alg.setInitial(game);
                        alg.execute();
                        move = alg.getFirstBestMove();
                        for (GameState child : children) {
                            if (move.equals(child.getMoveName())) {
                                game = child;
                                break;
                            }
                        }
                    } else {
                        List<GameState> children = game.generateChildren();
                        System.out.println("Current board:");
                        System.out.println(game);
                        System.out.println("Enter your move: ");
                        move = scanner.nextLine();
                        boolean validMove = false;
                        for (GameState child : children) {
                            if (move.equals(child.getMoveName())) {
                                game = child;
                                validMove = true;
                                break;
                            }
                        }
                        if (!validMove) {
                            System.out.println("Invalid move. Try again.");
                            continue;
                        }
                    }
                }

                if (game.isMaximizingTurnNow()) {
                    System.out.println("You win!");
                } else {
                    System.out.println("AI wins!");
                }
            default:
                break;
        }
    }
}

