import java.util.*;

public class Board implements Comparator<Board> {
    private Integer[][] currentPuzzle;
    private Board parentBlock; // parent board
    private String path; // a solution to the given puzzle
    private boolean flag; // used in BFS and A* algorithms, if node has reached -> flag up, else down
    private int blockCost, overallCost; // certain block cost and overall cost which will be add to the total

    /**
     *  ---------Initiate Constructor-----------
     */
    public Board(Integer[][] currentPuzzle) {
        int rows = currentPuzzle.length;
        int cols = currentPuzzle[0].length;
    	this.currentPuzzle = new Integer[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(currentPuzzle[i], 0, this.currentPuzzle[i], 0, currentPuzzle[i].length);
    	}
        this.flag = false;
        this.blockCost = 0;
        this.overallCost = 0;
        this.path = "";
    }

    /**
     *  ---------Default Constructor-----------
     */
    public Board() {
        this.flag = false;
        this.blockCost = 0;
        this.overallCost = 0;
        this.path = "";
    }

    /**
     * Check whether the puzzle is solved
     * @param currentPuzzle
     * @param goalMatt
     * @return boolean
     */
    private static boolean isSolved(Integer[][] currentPuzzle, Integer[][] goalMatt) {
        int rows = currentPuzzle.length;
        int cols = currentPuzzle[0].length;
        for(int i = 0; i < rows ; i++)
        {
            for(int j = 0; j < cols ; j++)
            {
                if(currentPuzzle[i][j] != goalMatt[i][j])
                    return false;
            }
        }
        return true;
    }

    /**
     * Put the values in a string matrix
     * @param state
     * @return
     */
    public static String openList(Integer [][] state) {
            StringBuilder ans = new StringBuilder();
            for (Integer[] puzzleValues : state) {
                for (int j = 0; j < state[0].length; j++) {
                    ans.append(puzzleValues[j]).append(" ");
                }
                ans.append("  ");
            }
            return ans.toString();
        }


    /**
     * A massive operators function which swaps values by given a direction (up,down,right,left, up as pair...) and as well
     * indexes - x1,y1 for two indexes manipulation x1,y1,x2,y2 for four index manipulation
     * @param oneBlock
     * @param direction
     * @param currentPuzzle
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public static void movements(boolean oneBlock, String direction, Integer[][] currentPuzzle, int x1, int y1, int x2, int y2 ){
        int temp;
        if (oneBlock) {
             {
                if (direction.equals("up")) {
                    temp = currentPuzzle[x1+1][y1];
                    currentPuzzle[x1+1][y1] = null;
                    currentPuzzle[x1][y1] = temp;
                }
                else if(direction.equals("left")) {
                    temp = currentPuzzle[x1][y1+1];
                    currentPuzzle[x1][y1+1] = null;
                    currentPuzzle[x1][y1] = temp;
                }
                else if(direction.equals("right")) {
                    temp = currentPuzzle[x1][y1-1];
                    currentPuzzle[x1][y1-1] = null;
                    currentPuzzle[x1][y1] = temp;
                }
                else if(direction.equals("down")) {
                    temp = currentPuzzle[x1-1][y1];
                    currentPuzzle[x1-1][y1] = null;
                    currentPuzzle[x1][y1] = temp;
                }
            }
        }else {
                if (direction.equals("pair up")) {
                    temp = currentPuzzle[x1 + 1][y1];
                    currentPuzzle[x1][y1] = temp;
                    currentPuzzle[x2][y2] = currentPuzzle[x2 + 1][y2];
                    currentPuzzle[x1 + 1][y1] = null;
                    currentPuzzle[x2 + 1][y2] = null;
                }
                else if(direction.equals("pair left")) {
                    temp = currentPuzzle[x1][y1 + 1];
                    currentPuzzle[x1][y1] = temp;
                    currentPuzzle[x2][y2] = currentPuzzle[x2][y2 + 1];
                    currentPuzzle[x1][y1 + 1] = null;
                    currentPuzzle[x2][y2 + 1] = null;
                }
                else if(direction.equals("pair right")) {
                    temp = currentPuzzle[x1][y1 - 1];
                    currentPuzzle[x1][y1] = temp;
                    currentPuzzle[x2][y2] = currentPuzzle[x2][y2 - 1];
                    currentPuzzle[x1][y1 - 1] = null;
                    currentPuzzle[x2][y2 - 1] = null;
                }
                else if(direction.equals("pair down")) {
                    temp = currentPuzzle[x1 - 1][y1];
                    currentPuzzle[x1][y1] = temp;
                    currentPuzzle[x2][y2] = currentPuzzle[x2 - 1][y2];
                    currentPuzzle[x1 - 1][y1] = null;
                    currentPuzzle[x2 - 1][y2] = null;
                }

        }
    }

    /**
     * Add the movement to the returned path
     * @param manipulatedPuzzle
     * @param currentPuzzle
     * @param listOfPuzzles
     * @param cost
     * @param direction
     * @param i
     * @param j
     * @param k
     */
    public static void addMovement(Integer[][] manipulatedPuzzle, Board currentPuzzle, List<Board> listOfPuzzles, int cost,String direction, int i, int j, int k){
        Board t = new Board(manipulatedPuzzle);
        String move = "";
        t.setBlockCost(cost);
        if (k == 1)
            move = t.getCurrentPuzzle()[i][j].toString();
        else if (k == 2)
            move = t.getCurrentPuzzle()[i][j].toString()+"&"+t.getCurrentPuzzle()[i+1][j].toString();
        else if (k == 3)
            move = t.getCurrentPuzzle()[i][j].toString()+"&"+t.getCurrentPuzzle()[i][j+1].toString();
        move+=direction;
        t.setPath(move);
        t.setOverallCost(currentPuzzle.getOverallCost()+t.getBlockCost());
        listOfPuzzles.add(t);
    }

    /**
     * Manipulate the puzzle by swapping blocks - without any hashmap, used in BFS, DFBnB, IDA* algorithms
     * @param currentPuzzle
     * @return List<Board>
     */
    public static List<Board> move(Board currentPuzzle) {
        int rows = currentPuzzle.getCurrentPuzzle().length;
        int cols = currentPuzzle.getCurrentPuzzle()[0].length;
        String path = currentPuzzle.getPath();
        String pathToBeAdded;
        List<Board> listOfPuzzles = new ArrayList<>();
        Integer [][] manipulatedPuzzle = new Integer[rows][cols];
        deepCopyMatt(manipulatedPuzzle, currentPuzzle);
        for (int i = 0; i<rows; i++) {
            for (int j = 0; j<cols; j++) {
                if (currentPuzzle.getCurrentPuzzle()[i][j] == null) {

                    // special condition - two blocks vertical (left)
                    if(i+1<rows && currentPuzzle.getCurrentPuzzle()[i+1][j] == null && j+1<cols) {
                        // move two blocks left
                        movements(false, "pair left",manipulatedPuzzle,i,j,i+1,j);
                        pathToBeAdded = path;
                        if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+ "&"+ manipulatedPuzzle[i+1][j]+"R")) {
                            addMovement(manipulatedPuzzle, currentPuzzle, listOfPuzzles, 6, "L", i, j,2);
                        }
                        // move two blocks right
                        movements(false,"pair right",manipulatedPuzzle,i,j+1,i+1,j+1);
                    }

                    // the special condition with the cost of 7 - two blocks horizontal (up)
                    if(j+1<cols && currentPuzzle.getCurrentPuzzle()[i][j+1]==0 && i+1<rows) {
                        // move two block up
                        movements(false, "pair up", manipulatedPuzzle,i,j,i,j+1);
                        pathToBeAdded = path;
                        if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+ "&"+ manipulatedPuzzle[i][j+1]+"D")) {

                            addMovement(manipulatedPuzzle,currentPuzzle,listOfPuzzles,7,"U",i,j,3);
                        }
                        // move two block down
                        movements(false,"pair down", manipulatedPuzzle,i+1,j,i+1,j+1);
                    }

                    // the special condition with the cost of 6 - two blocks vertical (right)
                    if(i+1<rows && currentPuzzle.getCurrentPuzzle()[i+1][j]==0 && j>0) {
                        movements(false, "pair right",manipulatedPuzzle,i,j,i+1,j);
                        pathToBeAdded = path;
                        if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+ "&"+ manipulatedPuzzle[i+1][j]+"L")) {
                            // the special condition with the cost of 6 - two blocks vertical
                            addMovement(manipulatedPuzzle,currentPuzzle,listOfPuzzles,6,"R",i,j,2);
                        }
                        movements(false,"pair left",manipulatedPuzzle,i,j-1,i+1,j-1);
                    }


                    // the special condition with the cost of 7 - two blocks horizontal (down)
                    if(j+1<cols && currentPuzzle.getCurrentPuzzle()[i][j+1]==0 && i>0) {
                        movements(false, "pair down", manipulatedPuzzle,i,j,i,j+1);
                        pathToBeAdded = path;
                        if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+"&"+manipulatedPuzzle[i][j+1]+"U")) {
                            addMovement(manipulatedPuzzle,currentPuzzle,listOfPuzzles,7,"D",i,j,3);
                        }
                        movements(false, "pair up",manipulatedPuzzle,i-1,j,i-1,j+1);
                    }




                    // single block left
                    if(j+1<cols && currentPuzzle.getCurrentPuzzle()[i][j+1] != null) {
                        movements(true,"left", manipulatedPuzzle, i, j,0,0);
                        pathToBeAdded = path;
                        if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+"R")) {
                            addMovement(manipulatedPuzzle,currentPuzzle,listOfPuzzles,5,"L",i,j,1);
                        }
                        movements(true,"right", manipulatedPuzzle, i, j+1,0,0);
                    }

                    // single block up
                    if(i+1<rows && currentPuzzle.getCurrentPuzzle()[i+1][j] != null) {
                        movements(true,"up" ,manipulatedPuzzle,i,j,0,0);
                        pathToBeAdded = path;
                        if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+"D")) {
                            addMovement(manipulatedPuzzle,currentPuzzle,listOfPuzzles,5,"U",i,j,1);
                        }
                        movements(true,"down",manipulatedPuzzle,i+1,j,0,0);
                    }


                    // single block right
                    if(j>0 && currentPuzzle.getCurrentPuzzle()[i][j-1] != null) {
                        movements(true,"right",manipulatedPuzzle,i,j,0,0);
                        pathToBeAdded = path;
                        if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+"L")) {
                            addMovement(manipulatedPuzzle, currentPuzzle, listOfPuzzles, 5,"R",i,j,1);
                        }
                        movements(true,"left",manipulatedPuzzle,i,j-1,0,0);
                    }

                    // single block down
                    if(i>0 && currentPuzzle.getCurrentPuzzle()[i-1][j] != null) {
                        movements(true,"down",manipulatedPuzzle,i,j,0,0);
                        pathToBeAdded = path;
                        if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+"U")) {
                            addMovement(manipulatedPuzzle, currentPuzzle, listOfPuzzles, 5,"D",i,j,1);
                        }
                        movements(true,"up", manipulatedPuzzle,i-1,j,0,0);
                    }
                }
            }
        }
        return listOfPuzzles;
    }

    /**
     * Deep copy matrix
     * @param manipulatedPuzzle
     * @param currentPuzzle
     */
    private static void deepCopyMatt(Integer[][] manipulatedPuzzle, Board currentPuzzle){
        int rows = currentPuzzle.getCurrentPuzzle().length;
        int cols = currentPuzzle.getCurrentPuzzle()[0].length;
        for(int i = 0; i<rows; i++) {
            for(int j = 0; j<cols; j++) {
                manipulatedPuzzle[i][j] = currentPuzzle.getCurrentPuzzle()[i][j];
            }
        }
    }

    /**
     * Manipulate the puzzle by swapping blocks - with hashmap, used in DFID & A* as learnt
     * @param currentPuzzle
     * @return List<Board>
     */
    public static List<Board> move(Board currentPuzzle, HashMap<String, Board> mapOfBoard) {
        int rows = currentPuzzle.getCurrentPuzzle().length;
        int cols = currentPuzzle.getCurrentPuzzle()[0].length;
        String path = currentPuzzle.getPath();
        String pathToBeAdded;
        List<Board> listOfPuzzles = new ArrayList<>();
        Integer [][] manipulatedPuzzle =new Integer [rows][cols];
        deepCopyMatt(manipulatedPuzzle, currentPuzzle);
        for (int i = 0; i<rows; i++) {
            for (int j = 0; j<cols; j++) {
                if (currentPuzzle.getCurrentPuzzle()[i][j] == null) {

                    // move pair left
                    if(i+1<rows && currentPuzzle.getCurrentPuzzle()[i+1][j] == null && j+1<cols) {
                        movements(false, "pair left",manipulatedPuzzle,i,j,i+1,j);
                        if(!mapOfBoard.containsKey(openList(manipulatedPuzzle))) {
                            pathToBeAdded = path;
                            if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+ "&"+ manipulatedPuzzle[i+1][j]+"R")) {
                                addMovement(manipulatedPuzzle, currentPuzzle, listOfPuzzles, 6, "L", i, j, 2);
                            }
                        }
                        movements(false, "pair right", manipulatedPuzzle,i,j+1,i+1,j+1);

                    }

                    // move pair up
                    if(j+1<cols && currentPuzzle.getCurrentPuzzle()[i][j+1] == null && i+1<rows) {
                        movements(false, "pair up",manipulatedPuzzle,i,j,i,j+1);
                        if(!mapOfBoard.containsKey(openList(manipulatedPuzzle))) {
                            pathToBeAdded = path;
                            if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+ "&"+ manipulatedPuzzle[i][j+1]+"D")) {
                                addMovement(manipulatedPuzzle, currentPuzzle, listOfPuzzles, 7, "U", i, j, 3);
                            }
                        }
                        movements(false,"pair down",manipulatedPuzzle,i+1,j,i+1,j+1);
                    }


                    // move pair right
                    if(i+1<rows && currentPuzzle.getCurrentPuzzle()[i+1][j] == null && j>0) {
                        movements(false, "pair right",manipulatedPuzzle,i,j,i+1,j);
                        if(!mapOfBoard.containsKey(openList(manipulatedPuzzle))) {
                            pathToBeAdded = path;
                            if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+ "&"+ manipulatedPuzzle[i+1][j]+"L")) {
                                addMovement(manipulatedPuzzle,currentPuzzle,listOfPuzzles,6,"R",i,j,2);
                            }
                        }
                        movements(false, "pair left",manipulatedPuzzle,i,j-1,i+1,j-1);
                    }

                    // move pair down
                    if(j+1<cols && currentPuzzle.getCurrentPuzzle()[i][j+1] == null && i>0) {
                        movements(false, "pair down",manipulatedPuzzle,i,j,i,j+1);
                        if(!mapOfBoard.containsKey(openList(manipulatedPuzzle))) {
                            pathToBeAdded = path;
                            if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+ "&"+ manipulatedPuzzle[i][j+1]+"U")) {
                                addMovement(manipulatedPuzzle, currentPuzzle, listOfPuzzles, 7, "D",i,j,3);
                            }
                        }
                        movements(false, "pair up",manipulatedPuzzle,i-1,j,i-1,j+1);

                    }

                    // one block left
                    if(j+1<cols && currentPuzzle.getCurrentPuzzle()[i][j+1] != null) {
                        movements(true,"left",manipulatedPuzzle,i,j,0,0);
                        if(!mapOfBoard.containsKey(openList(manipulatedPuzzle))) {
                            pathToBeAdded = path;
                            if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+"R")) {
                                addMovement(manipulatedPuzzle, currentPuzzle, listOfPuzzles, 5, "L",i,j,1);
                            }
                        }
                        movements(true,"right",manipulatedPuzzle,i,j+1,0,0);
                    }

                    // one block up
                    if(i+1<rows && currentPuzzle.getCurrentPuzzle()[i+1][j] != null) {
                        movements(true,"up", manipulatedPuzzle,i,j,0,0);

                        if(!mapOfBoard.containsKey(openList(manipulatedPuzzle))) {
                            pathToBeAdded = path;
                            if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+"D")) {
                                addMovement(manipulatedPuzzle,currentPuzzle,listOfPuzzles,5,"U",i,j,1);
                            }
                        }
                        movements(true,"down",manipulatedPuzzle,i+1,j,0,0);
                    }

                    // one block right
                    if(j>0 && currentPuzzle.getCurrentPuzzle()[i][j-1] != null) {
                        movements(true,"right",manipulatedPuzzle,i,j,0,0);
                        if(!mapOfBoard.containsKey(openList(manipulatedPuzzle))) {// couldn't find the specific key
                            pathToBeAdded = path;
                            if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+"L")) {
                                addMovement(manipulatedPuzzle, currentPuzzle, listOfPuzzles, 5, "R",i,j,1);
                            }
                        }
                        movements(true,"left", manipulatedPuzzle,i,j-1,0,0);
                    }

                    // one block down
                    if(i>0 && currentPuzzle.getCurrentPuzzle()[i-1][j] != null) {
                        movements(true,"down",manipulatedPuzzle,i,j,0,0);
                        if(!mapOfBoard.containsKey(openList(manipulatedPuzzle))) {
                            pathToBeAdded = path;
                            if(!pathToBeAdded.equals(manipulatedPuzzle[i][j]+"U")) {
                                addMovement(manipulatedPuzzle, currentPuzzle, listOfPuzzles, 5, "D",i,j,1);
                            }
                        }
                        movements(true,"up", manipulatedPuzzle,i-1,j,0,0);
                    }

                }
            }
        }
        return listOfPuzzles;

    }


    /**
     *  ---------Getters setters-----------
     */
        public Integer[][] getCurrentPuzzle() {
            return currentPuzzle;
        }

        public boolean flag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public static int getTotalCost(Board goalPuzzle, Board currentPuzzle) {
            int totalCost=0;
            while (!isSolved(goalPuzzle.getCurrentPuzzle(),currentPuzzle.getCurrentPuzzle())) {
                totalCost+=goalPuzzle.getBlockCost();
                goalPuzzle=goalPuzzle.getParentBlock();
            }
            return totalCost;
        }

        public int getOverallCost() {
            return overallCost;
        }

        public void setOverallCost(int overallCost) {
            this.overallCost = overallCost;
        }

        public void addToOverallCost( int costToBeAdded, int blockCost) {
            this.overallCost = blockCost + costToBeAdded;
        }

        public void setPath(String move) { this.path = this.path + move; }

        public String getPath() {
            return this.path;
        }

        public static String getPath(Board goal, Board root) {
            Stack<Board> solutionStack = new Stack<>();
            StringBuilder path= new StringBuilder();
            solutionStack.push(goal);
            while (!isSolved(goal.getCurrentPuzzle(),root.getCurrentPuzzle())) {
                solutionStack.push(goal.getParentBlock());
                goal=goal.getParentBlock();
            }
            while(!solutionStack.isEmpty()) {
                Board curr1=solutionStack.pop();
                path.append(curr1.getPath());
                path.append("-");
            }

            path = new StringBuilder(path.substring(1,path.length()-1));
            return path.toString();

        }

        public void setBlockCost(int blockCost) {
            this.blockCost = blockCost;
        }

        public int getBlockCost() {
            return blockCost;
        }

        public void setParentBlock(Board parentBlock) {
            this.parentBlock = parentBlock;
        }

        public Board getParentBlock() {
        return parentBlock;
    }


        @Override
        public int compare(Board A, Board B) {
            if (A.getOverallCost() > B.getOverallCost())
                return 1;
            else if (A.getOverallCost() < B.getOverallCost())
                return -1;

            return 0;
        }

        @Override
        public String toString()
        {
            StringBuilder str = new StringBuilder("\n");
            for (Integer[] puzzleValues : this.currentPuzzle) {
                for (int j = 0; j < this.currentPuzzle[0].length; j++) {
                    if (puzzleValues[j] == null)
                        str.append("0 ");
                    else
                        str.append(puzzleValues[j]).append(" ");
                }
                str.append("\n");
            }
            return str.toString();
        }
}



