import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class Main {

    static ObjectMapper mapper = new ObjectMapper();
    static File fileObj = new File("C:\\Users\\jrmad\\Documents\\Java_Projects\\AlphaO\\src\\main\\java\\costs_data.json");
    static Map<String, Double> costs;

    static {
        try {
            costs = mapper.readValue(fileObj, new TypeReference<Map<String, Double>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static int lastMove = -1;


    static int depth = 9;


    static int emptySpaces = 77;


    static int timelimit = 10;
    static double timeBuffer = 0.09;

    static long count = 0;
    static long evalcount = 0;
    static long terminalcount = 0;


    static int[][] position = new int[9][9];
    static int[] globalBoard = new int[9];

    static boolean firstMove = true;

    static long start = 0;

    final static String name = "AlphaO";

    public Main() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\jrmad\\Documents\\Java_Projects\\AlphaO\\src\\main\\java\\first_four_moves");
        while (!file.isFile())
            file = new File("C:\\Users\\jrmad\\Documents\\Java_Projects\\AlphaO\\src\\main\\java\\first_four_moves");
        BufferedReader f = new BufferedReader(new FileReader(file));
        String line = f.readLine();
        while (line != null) {
            String[] lineArr = line.split(" ");
            position[Integer.parseInt(lineArr[1])][Integer.parseInt(lineArr[2])] = lineArr[0].equals(name) ? -1 : 1;
            lastMove = Integer.parseInt(lineArr[2]);
            line = f.readLine();
        }
        f.close();


        File end = new File("C:\\Users\\jrmad\\Documents\\Java_Projects\\AlphaO\\src\\main\\java\\end_game");
        while (!end.isFile()) {
            end = new File("C:\\Users\\jrmad\\Documents\\Java_Projects\\AlphaO\\src\\main\\java\\end_game");
            File go = new File("C:\\Users\\jrmad\\Documents\\Java_Projects\\AlphaO\\src\\main\\java\\" + name + ".go");
            if (go.isFile()){
                start = System.currentTimeMillis();
                doTurn();
            }
        }


    }


    public static void doTurn() throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(new File("C:\\Users\\jrmad\\Documents\\Java_Projects\\AlphaO\\src\\main\\java\\move_file")));
        String line = file.readLine();
        if (line != null) {
            String[] f = line.split(" ");
            if (!f[0].equals(name)) {
                emptySpaces -= 1;
                lastMove = Integer.parseInt(f[2]);
                position[Integer.parseInt(f[1])][Integer.parseInt(f[2])] = 1;
//                System.out.println("Thinking");
                count = 0;
                evalcount = 0;
                terminalcount = 0;
                Move move = miniMax(position, lastMove, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
                System.out.println("lastmove: " + lastMove);
                System.out.println(Arrays.toString(move.move));
//                System.out.println((System.currentTimeMillis()-start)/(double)1000);
//                System.out.println(evalcount);
//                System.out.println(count);
                System.out.println(terminalcount);
                makeMove(move.move);
                System.out.println((System.currentTimeMillis() - start) / (double) 1000);
                position[move.move[0]][move.move[1]] = -1;
                emptySpaces -= 1;
                if (emptySpaces <= 20) {
                    depth += 1;
                }
            }
        } else {
            System.out.println("I'm blue!");
            System.out.println("lastmove: " + lastMove);
            Move move = miniMax(position, lastMove, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
            System.out.println(Arrays.toString(move.move));
            System.out.println((System.currentTimeMillis() - start) / (double) 1000);
            makeMove(move.move);
            position[move.move[0]][move.move[1]] = -1;
            emptySpaces -= 1;
            if (emptySpaces <= 20) {
                depth += 1;
            }
        }
        file.close();
    }

    static double evaluatePosition(int[][] position, int currentBoard) {
        double eval = 0;
        int[] mainBd = new int[9];
        double[] evalMultiplier = new double[]{1.4, 1, 1.4, 1, 1.75, 1, 1.4, 1, 1.4};
        for (int i = 0; i < 9; i++) {
            eval += evaluate(position[i]) * 1.5 * evalMultiplier[i];
            if (i == currentBoard) eval += evaluate(position[i]) * evalMultiplier[i];
            int tmpEval = checkWinCondition(position[i]);
            eval -= tmpEval * evalMultiplier[i];
            mainBd[i] = tmpEval;
        }
        eval -= checkWinCondition(mainBd) * 5000;
        eval += evaluate(mainBd) * 150;
        return eval;
    }

    static void makeMove(int[] move) throws IOException {
        FileWriter f = new FileWriter("C:\\Users\\jrmad\\Documents\\Java_Projects\\AlphaO\\src\\main\\java\\move_file");
        f.write(name + " " + move[0] + " " + move[1]);
        f.close();
    }


    static double evaluate(int[] square) {
        evalcount++;
        return costs.get(Arrays.toString(square));
    }


    static int checkWinCondition(int[] square) {
        int a = 1;
        if (square[0] + square[1] + square[2] == a * 3
                || square[3] + square[4] + square[5] == a * 3
                || square[6] + square[
                7] + square[8] == a * 3
                || square[0] + square[3] + square[6] == a * 3
                || square[1] + square[4] + square[
                7] == a * 3 ||
                square[2] + square[5] + square[8] == a * 3
                || square[0] + square[4] + square[8] == a * 3
                || square[2] +
                square[4] + square[6] == a * 3)
            return a;


        a = -1;
        if (square[0] + square[1] + square[2] == a * 3
                || square[3] + square[4] + square[5] == a * 3
                || square[6] + square[
                7] + square[8] == a * 3
                || square[0] + square[3] + square[6] == a * 3
                || square[1] + square[4] + square[
                7] == a * 3 ||
                square[2] + square[5] + square[8] == a * 3
                || square[0] + square[4] + square[8] == a * 3
                || square[2] +
                square[4] + square[6] == a * 3)
            return a;

        return 0;
    }

    static class Move {

        int[][] position;
        int[] move;
        double cost;

        public Move(int[][] position, int[] move) {
            this.position = deepCopy(position);
            this.move = move;
            this.cost = evaluatePosition(this.position, move[0]);
        }

        Move copy() {
            return new Move(deepCopy(this.position), Arrays.copyOf(this.move, this.move.length));
        }

    }

    static int[][] deepCopy(int[][] original) {
        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    static Move[] getPossibleMoves(int[][] position, int boardToPlay, boolean maximizingPlayer) {
        int player = -1;
        if (maximizingPlayer) player = 1;
        ArrayList<Move> nextPossibleMoves = new ArrayList<>();

        if (checkWinCondition(position[boardToPlay]) == 0 && Arrays.stream(position[boardToPlay]).anyMatch(i -> i == 0)) {
            for (int i = 0; i < 9; i++) {
                if (position[boardToPlay][i] == 0) {
                    int[][] tmp = deepCopy(position);
                    tmp[boardToPlay][i] = player;
                    Move move = new Move(tmp, new int[]{boardToPlay, i});
                    nextPossibleMoves.add(move);
                }
            }
        } else {
            for (int i = 0; i < 9; i++) {
                if (checkWinCondition(position[i]) == 0)
                    for (int j = 0; j < 9; j++) {
                        if (position[i][j] == 0) {
                            int[][] tmp = deepCopy(position);
                            tmp[i][j] = player;
                            Move move = new Move(tmp, new int[]{i, j});
                            nextPossibleMoves.add(move);
                        }
                    }
            }

        }
        Comparator<Move> comparator = (o1, o2) -> {
            if (o1.cost < o2.cost) return -1;
            if (o1.cost > o2.cost) return 1;
            return 0;
        };
        nextPossibleMoves.sort(comparator);
        Move[] ret = nextPossibleMoves.toArray(new Move[0]);
        return ret;
    }

    static Move miniMax(int[][] position, int boardToPlay, int depth, double alpha, double beta, boolean minimizing) {
        count++;
        Move tmpMove = new Move(position, new int[]{-1, -1});
        if (depth == 0) terminalcount++;
        if ((System.currentTimeMillis() - start) >= (timelimit - timeBuffer) * 1000 || depth == 0) return tmpMove;
        if (minimizing) {
            Move[] nextMovePossibilities = getPossibleMoves(position, boardToPlay, true);
            if (nextMovePossibilities.length == 0) return new Move(position, new int[]{-1, -1});
            Move minEval = nextMovePossibilities[0];
            minEval.cost = Double.POSITIVE_INFINITY;
            for (Move child : nextMovePossibilities) {
                Move eval = miniMax(child.position, child.move[1], depth - 1, alpha, beta, false);
                if (eval.cost < minEval.cost) {
                    minEval = eval.copy();
                    minEval.move = Arrays.copyOf(child.move, child.move.length);
                }
                beta = Math.min(beta, eval.cost);
                if (beta <= alpha) break;
            }
            return minEval;
        } else {
            Move[] nextMovePossibilities = getPossibleMoves(position, boardToPlay, false);
            if (nextMovePossibilities.length == 0) return new Move(position, new int[]{-1, -1});
            Move maxEval = nextMovePossibilities[0];
            maxEval.cost = Double.NEGATIVE_INFINITY;
            for (Move child : nextMovePossibilities) {
                Move eval = miniMax(child.position, child.move[1], depth - 1, alpha, beta, true);
                if (eval.cost > maxEval.cost) {
                    maxEval = eval.copy();
                    maxEval.move = Arrays.copyOf(child.move, child.move.length);
                }
                alpha = Math.max(alpha, eval.cost);
                if (beta <= alpha) break;
            }
            return maxEval;
        }
    }
}
