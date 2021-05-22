import java.util.*;

/**
 * This class represents BFS algorithm
 */
public class BFS {
    private HashMap<String, Board> openList = new HashMap<>();//open list
    private Queue<Collection<Board>> frontiers = new LinkedList<>(); // used for "with open" option
    private HashMap<String, Board> closedList = new HashMap<>();//close list
    private Queue<Board> q = new LinkedList<>();
    private List<Board> blockSucessors;
    private Integer[][] goalPuzzle;
    private int numOfNodes;
    private boolean withOpen;
    private String res = "";
    private Board currentPuzzle;

    public BFS(Integer[][] startPuzzle, Integer[][] goalPuzzle, Boolean withOpen){
        this.currentPuzzle = new Board(startPuzzle);
        this.goalPuzzle  = new Integer[goalPuzzle.length][goalPuzzle[0].length];
        for(int i=0;i<goalPuzzle.length;i++) {
            System.arraycopy(goalPuzzle[i], 0, this.goalPuzzle[i], 0, goalPuzzle[i].length);
        }
        this.numOfNodes = 1;
        this.withOpen = withOpen;
    }

    public String solve() { // L  make_queue (start) and make_hash_table
        this.q.add(currentPuzzle);
        Board board;
        int cost;
        while (!q.isEmpty()) { // While L not empty loop
            board = q.poll(); // n  L.remove_front
            if(withOpen) { // if input.txt contains "with open"
                frontiers.add(openList.values());
                System.out.println(frontiers);
            }
            closedList.put(board.toString(), currentPuzzle);
            blockSucessors = Board.move(board);
            openList.remove(board.toString());
            for (Board idx : blockSucessors) { // For each allowed operator on n
                idx.setFlag(true);
                this.numOfNodes++;
                idx.setParentBlock(board); // g  operator(n)
                if(!closedList.containsKey(idx.toString()) &&
                        !openList.containsKey(idx.toString())) { //If g not in C and not in L
                    if(equals(idx.getCurrentPuzzle(),goalPuzzle)) { //If goal(g) return path(g)
                        res = Board.getPath(idx, currentPuzzle);
                        res += "\nNum: "+numOfNodes;
                        cost = Board.getTotalCost(idx, currentPuzzle);
                        res += "\nCost: "+cost;
                        return res;
                    }
                    q.add(idx); // L.insert (
                    openList.put(idx.toString(),idx);
                }

            }

        }
        res = "no path";
        res += "\nNum: "+numOfNodes;
        return res;
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
