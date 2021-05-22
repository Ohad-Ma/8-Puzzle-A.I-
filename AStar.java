import java.util.*;

/**
 * This class represents A* class, I didn't find it on the given pdf files so I went here:
 * https://www.geeksforgeeks.org/a-search-algorithm/
 *
 * // A* Search Algorithm
 * 1.  Initialize the open list
 * 2.  Initialize the closed list
 *     put the starting node on the open
 *     list (you can leave its f at zero)
 *
 * 3.  while the open list is not empty
 *     a) find the node with the least f on
 *        the open list, call it "q"
 *
 *     b) pop q off the open list
 *
 *     c) generate q's 8 successors and set their
 *        parents to q
 *
 *     d) for each successor
 *         i) if successor is the goal, stop search
 *           successor.g = q.g + distance between
 *                               successor and q
 *           successor.h = distance from goal to
 *           successor (This can be done using many
 *           ways, we will discuss three heuristics-
 *           Manhattan, Diagonal and Euclidean
 *           Heuristics)
 *
 *           successor.f = successor.g + successor.h
 *
 *         ii) if a node with the same position as
 *             successor is in the OPEN list which has a
 *            lower f than successor, skip this successor
 *
 *         iii) if a node with the same position as
 *             successor  is in the CLOSED list which has
 *             a lower f than successor, skip this successor
 *             otherwise, add  the node to the open list
 *      end (for loop)
 *
 *     e) push q on the closed list
 *     end (while loop)
 */
public class AStar {
    private Board currentGame;
    private Integer[][] goalPuzzle;
    private Boolean withOpen;
    private String res;
    private int vertices;
    private Queue<Collection<Board>> frontiers;
    private HashMap<String, Board> open;
    private HashMap<String, Board> close;
    private int mDist;
    private List<Board> blockSuccessors;

    public AStar(Integer[][] startPuzzle,Integer[][] goalPuzzle, boolean withOpen){
        this.currentGame = new Board(startPuzzle);
        this.goalPuzzle  = new Integer[goalPuzzle.length][goalPuzzle[0].length];
        for(int i=0;i<goalPuzzle.length;i++) {
            System.arraycopy(goalPuzzle[i], 0, this.goalPuzzle[i], 0, goalPuzzle[i].length);
        };
        this.res = "";
        this.withOpen = withOpen;
        this.open = new HashMap<>();
        this.close = new HashMap<>();
        this.vertices = 1;
    }

    public String solve() {
        Board boardPriorityQ = new Board();
        PriorityQueue<Board> boardPriorityQueue = new PriorityQueue<>(5, boardPriorityQ);
        Board currentPuzzle = new Board(currentGame.getCurrentPuzzle());
        currentPuzzle.addToOverallCost(0 , heuristicFunc(currentPuzzle.getCurrentPuzzle(), goalPuzzle));
        boardPriorityQueue.add(currentPuzzle);
        open.put(currentPuzzle.toString(), currentPuzzle);


        while (!boardPriorityQueue.isEmpty()) {
            int cost;
            Board board,brd;

            if(this.withOpen) { // if input.txt contains "with open"
                this.frontiers = new LinkedList<>();
                frontiers.add(open.values());
                System.out.println(frontiers);
            }
            board= boardPriorityQueue.poll();

            assert board != null;
            open.remove(board.toString());

            if(equals(goalPuzzle,board.getCurrentPuzzle())) {
                this.res = Board.getPath(board,currentGame);
                this.res += "\nNum: "+ vertices;
                cost = Board.getTotalCost(board, currentGame);
                this.res += "\nCost: "+cost;
                return res;

            }
            close.put(board.toString(), board);
            blockSuccessors = Board.move(board, close);
            for (Board idx : blockSuccessors) {
                this.vertices++;
                idx.setParentBlock(board);
                idx.setFlag(true);
                if(!close.containsKey(idx.toString()) && !boardPriorityQueue.contains(idx) ) {
                    idx.addToOverallCost(board.getOverallCost(),heuristicFunc(idx.getCurrentPuzzle(),goalPuzzle));
                    open.put(idx.toString(),idx);
                    boardPriorityQueue.add(idx);
                }
                else if(boardPriorityQueue.contains(idx)) { // go to priority q and remove higher
                    brd = open.remove(idx.toString());
                    if(brd.getOverallCost()>idx.getOverallCost()) {
                        open.put(idx.toString(), idx);
                        boardPriorityQueue.remove(brd);
                        boardPriorityQueue.add(idx);
                    }
                }
            }
        }

        String res="no path";
        res+="\nNum: "+ vertices;
        return res;
    }

    private int heuristicFunc(Integer [][] currentPuzzle, Integer[][] goalState) {
        int rows = currentPuzzle.length;
        int cols = currentPuzzle[0].length;
        this.mDist = 0;
        for (int c = 0; c < rows; c++) {
            for (int d = 0; d < cols; d++) {
                for (int a = 0; a < rows; a++) {
                    for (int b = 0; b < cols; b++) {
                        if ((currentPuzzle[a][b]==(goalState[c][d])) && currentPuzzle[a][b] != null) {
                            this.mDist += Math.abs(a-c)+Math.abs(b-d);
                        }
                    }

                }
            }
        }
        return 3*this.mDist;
    }

    private boolean equals(Integer[][] state, Integer[][] goalMatrix) {
        int rows = state.length;
        int cols = state[0].length;
        for(int i=0; i<rows ;i++) {
            for(int j=0;j<cols;j++) {
                if(state[i][j]!=(goalMatrix[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }
}
