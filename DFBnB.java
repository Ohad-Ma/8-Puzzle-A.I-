import java.util.*;

/**
 * This class represents DFBnB algorithm
 */
public class DFBnB {
    private Board currentGame;
    private Integer[][] goalPuzzle;
    private Boolean withOpen;
    private String result;
    private Queue<Collection<Board>> frontiers;
    List<Board> blockSuccessors;
    private HashMap<String, Board> H;
    private Stack<Board> stackOfPuzzles;
    private int vertices;

    public DFBnB(Integer[][] startPuzzle,Integer[][] goalPuzzle, boolean withOpen){
        this.currentGame = new Board(startPuzzle);
        this.goalPuzzle = new Integer[goalPuzzle.length][goalPuzzle[0].length];
        for(int i=0;i<goalPuzzle.length;i++) {
            System.arraycopy(goalPuzzle[i], 0, this.goalPuzzle[i], 0, goalPuzzle[i].length);
        }
        this.result = "";
        this.withOpen = withOpen;
        this.frontiers = new LinkedList<>();
        this.H =new HashMap<>();
        this.stackOfPuzzles = new Stack<>();
        this.vertices = 1;
    }


    public String solve() {
        H.put(currentGame.toString(), currentGame);
        stackOfPuzzles.push(currentGame);
        Board n, priorityQ, gT;
        Board[] N;
        int maxVal_t=Integer.MAX_VALUE,cost;

        while(!stackOfPuzzles.isEmpty()) { // while L is not empty
            n = stackOfPuzzles.pop(); // L.remove_front()
            if(this.withOpen) { // if the given input.txt contains "with open"
                frontiers.add(H.values());
                System.out.println(frontiers);
            }
            if(n.flag()) // if n is marked as "out"
                H.remove(n.toString()); //H.remove(n)
            else {
                n.setFlag(true); //mark n as out ” and L.insert (
                stackOfPuzzles.push(n);
                blockSuccessors = Board.move(n);
                N = new Board[blockSuccessors.size()]; // initiate
                int i=0;
                for (Board idx : blockSuccessors) { // For each node g from N according to the order of N
                    this.vertices++;
                    idx.setParentBlock(n);
                    idx.addToOverallCost(heuristicFunction(idx.getCurrentPuzzle(), goalPuzzle),idx.getOverallCost());
                    N[i]=idx;
                    i++;
                }
                Arrays.asList(N);
                priorityQ = new Board();
                Arrays.sort(N,priorityQ);

                for (i=0;i<N.length;i++) {
                    if(N[i].getOverallCost()>=maxVal_t) {
                        N[i] = null; // apply all of the allowed operators
                        while(i<N.length) {
                            N[i] = null;
                            i++;
                        }
                    }
                    else if(N[i].flag() && H.containsKey(N[i].toString())) {
                        //remove g from N
                        N[i]=null;
                    }
                    else if(!N[i].flag() && H.containsKey(N[i].toString())) {
                        gT=H.get(N[i].toString());
                        if(gT.getOverallCost()<=N[i].getOverallCost()) {
                            //remove g from N
                            N[i]=null;

                        }
                        else { //remove g' from L and from H
                            stackOfPuzzles.remove(gT);
                            H.remove(gT.toString());
                        }
                    }
                    // if we reached here, f(g) < t
                    else if(N[i] != null && equals(N[i].getCurrentPuzzle(), goalPuzzle) ) {
                        maxVal_t = N[i].getOverallCost()+ heuristicFunction(N[i].getCurrentPuzzle(), goalPuzzle);
                        result= Board.getPath(N[i],currentGame);
                        result += "\nNum: "+ vertices;
                        cost = Board.getTotalCost(N[i], currentGame);
                        result += "\nCost: "+cost;

                        if(cost<maxVal_t) {
                            return result;
                        }

                        N[i]=null;
                        while(i<N.length) {
                            N[i]=null;
                            i++;
                        }
                    }
                }
                for(int j=N.length-1;j>=0;j--) { //insert N in a reverse order to L and H
                    if(N[j]!=null) {
                        stackOfPuzzles.push(N[j]);
                        H.put(N[j].toString(),N[j]);
                    }
                }
            }
        }
        String res="no path";
        res +="\nNum: ";
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









    /*
     * this function compute the
     * */

    private int heuristicFunction(Integer [][] currentState, Integer[][] goalState) {
        int manhattanDistance = 0;
        for (int c = 0; c < goalState.length; c++) {
            for (int d = 0; d < goalState[0].length; d++) {
                for (int a = 0; a < currentState.length; a++) {
                    for (int b = 0; b < currentState[0].length; b++) {
                        if (currentState[a][b]==(goalState[c][d]) && currentState[a][b]!=null) {
                            manhattanDistance+=Math.abs(a-c)+Math.abs(b-d);
                        }
                    }

                }
            }
        }
        return 3*manhattanDistance;



    }
}
