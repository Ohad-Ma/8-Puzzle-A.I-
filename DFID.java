import java.util.*;
/**
 * This class represents a Recursive DFID algorithm
 */
public class DFID {
    private int vertices;
    private Queue<Collection<Board>> frontiers;
    private Board currentPuzzle;
    private Integer[][] goalPuzzle;
    private String result;
    private Boolean withOpen;

    public DFID(Integer[][] startPuzzle, Integer[][] goalPuzzle, Boolean withOpen){
        this.currentPuzzle = new Board(startPuzzle);
        this.goalPuzzle  = new Integer[goalPuzzle.length][goalPuzzle[0].length];
        for(int i=0;i<goalPuzzle.length;i++) {
            System.arraycopy(goalPuzzle[i], 0, this.goalPuzzle[i], 0, goalPuzzle[i].length);
        }
        this.vertices = 1;
        this.result = "";
        this.withOpen = withOpen;
    }

    public String solve() {
        int maxVal = Integer.MAX_VALUE;
        String res = "";
        for (int depth = 0; depth < maxVal; depth++) { //For depth= 1 to inf
            HashMap<String, Board> makeHashTable = new HashMap<>(); //H  make_hash_table
            result = limited_dfs(currentPuzzle, goalPuzzle, depth, makeHashTable); //result  Limited_DFS start,Goals,depth,H)
            if (withOpen) { // if input.txt contains "with open"
                frontiers = new LinkedList<>();
                frontiers.add(makeHashTable.values());
                System.out.println(frontiers);
            }
            if (!result.equals("cutoff")) { //If result ≠ cutoff then return result
                return result;
            }
        }
        res = "no path";
        res += "\nNum:" + vertices;
        return res;
    }

    public String limited_dfs(Board n, Integer[][] goalState, int limit, HashMap<String, Board> H) {
        String result = "";
        List<Board> blockSuccessors;
        int cost;
        boolean isCutoff;
        if (equals(goalState, n.getCurrentPuzzle())) { //If goal(n) then return path(n) //use the back pointers or the recursion tail
            result = Board.getPath(n, currentPuzzle);
            result += "\nNum: " + vertices;
            cost = Board.getTotalCost(n, currentPuzzle);
            result += "\nCost: " + cost;
            return result;
        }
        else if (limit == 0) //Else if limit = 0 then return cutoff
            return "cutoff";
        else {
            H.put(n.toString(), n); //H.insert (n)
            isCutoff = false; //isCutoff  false
            blockSuccessors = Board.move(n, H);
            for (Board idx : blockSuccessors) { //For each allowed operator on n
                vertices++;
                idx.setParentBlock(n); //g  operator(n)
                result = limited_dfs(idx, goalState, limit - 1, H);
                if (result.equals("cutoff")) //If result = cutoff
                    isCutoff = true; //isCutoff  true
                else if (!result.equals("fail")) //Else if result ≠ fail
                    return result; //return result
            }


            H.remove(n.toString()); //H.remove ( n) - the memory for n should be also released
            if (isCutoff) {
                return "cutoff";
            } else {
                return "fail";
            }
        }

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
