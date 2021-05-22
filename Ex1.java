import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class Ex1 {
	/**
	 * Take a given txt file an turn its data into a list
	 * @param FileName
	 * @return
	 */
	public static List<String> readFromFile(String FileName){
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(FileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public static void init(String input){
		Integer[][] startingBoard, goalBoard;
		int N,M;
		long sTime, eTime;
		boolean withTime = false,withOpen = false;
		String searchAlgorithm = "";

		List<String> file = readFromFile(input);

		//Get search algorithm from input.txt LINE #1
		searchAlgorithm = file.get(0);

		//Get the "with time" or "no time" LINE #2
		if (file.get(1).equals("with time"))
			withTime = true;

		//Get the "with open" or "no open" LINE #3
		if (file.get(2).equals("with open"))
			withOpen = true;


		//Get cols and rows (the size of the matrix) LINE #4
		String[] temp = file.get(3).split("x");
		N = Integer.parseInt(temp[0]);
		M = Integer.parseInt(temp[1]);


		// Turn the input.txt given matrix into an Array of strings LINE #5
		String[][] strStartingBoard = new String[N][M];
		int j = 4;
		for (int i = 0; i < N; i++) {
			strStartingBoard[i] = file.get(j).split(",");
			j++;
		}
        /*
         Prints the string board from the input.txt
         for (int i = 0 ; i< strStartingBoard.length;i++){
            System.out.println(Arrays.toString(Arrays.stream(strStartingBoard[i]).toArray()));
        }
         */

        /*
         Convert the String Array into an Array of Integers while avoiding the '_' char
         and replace it with a zero
         */
		startingBoard = new Integer[N][M];
		for (int i = 0; i < N; i++) {
			for (int k = 0; k < M; k++) {
				if (strStartingBoard[i][k].equals("_")) {
					startingBoard[i][k] = null; // value 0 for the empty place ("_")
					continue;
				}
				startingBoard[i][k] = Integer.parseInt(strStartingBoard[i][k]);
			}

		}
        /* Prints the int board
        for (int i = 0;i<N;i++){
            for (int k = 0; k<M; k++){
                System.out.print(startingBoard[i][k] + " ");
            }
            System.out.println();
        }
         */


		//----------------GOAL MATRIX----------------
		// Turn the input.txt given matrix into an Array of strings LINE #5
		String[][] strGoalBoard = new String[N][M];
		goalBoard = new Integer[N][M];
		int goalLine = 0;
		for (int i = 0; i < file.size(); i++) {
			if (file.get(i).equals("Goal state:"))
				goalLine = i;
		}
		j = goalLine+1;
		for (int i = 0; i < N; i++) {
			strGoalBoard[i] = file.get(j).split(",");
			j++;
		}

        /*
         //Prints the string board from the input.txt
         for (int i = 0 ; i< strStartingBoard.length;i++){
            System.out.println(Arrays.toString(Arrays.stream(strStartingBoard[i]).toArray()));
        }
        */

        /*
         Convert the String Array into an Array of Integers while avoiding the '_' char
         and replace it with a zero
         */
		for (int i = 0; i < N; i++) {
			for (int k = 0; k < M; k++) {
				if (strGoalBoard[i][k].equals("_")) {
					goalBoard[i][k] = null; // value 0 for the empty place ("_")
					continue;
				}
				goalBoard[i][k] = Integer.parseInt(strGoalBoard[i][k]);
			}

		}
		//calling the right function: BFS,DFIDF,A*,IDA*, DFBnB
		String ans = "";

		if(searchAlgorithm.equals("BFS")) {
			BFS algo = new BFS(startingBoard, goalBoard, withOpen);
			sTime = System.nanoTime(); // start time
			ans = algo.solve(); //gets the goal puzzle with his results
			eTime = System.nanoTime(); //end time
			if (withTime) //if we get "with time" so print how long time the algo takes
			{
				double ansTime = (double) (eTime - sTime) / 1_000_000_000;
				ans += "\n" + (new DecimalFormat("##.###").format(ansTime) + " seconds");
			}
		}
		if(searchAlgorithm.equals("DFID")) {
			DFID algo = new DFID(startingBoard, goalBoard, withOpen);
			sTime = System.nanoTime(); // start time
			ans = algo.solve(); //gets the goal puzzle with his results
			eTime = System.nanoTime(); //end time
			if (withTime) //if we get "with time" so print how long time the algo takes
			{
				double ansTime = (double) (eTime - sTime) / 1_000_000_000;
				ans += "\n" + (new DecimalFormat("##.###").format(ansTime) + " seconds");
			}
		}
		if(searchAlgorithm.equals("DFBnB")) {
			DFBnB algo = new DFBnB(startingBoard, goalBoard, withOpen);
			sTime = System.nanoTime(); // start time
			ans = algo.solve(); //gets the goal puzzle with his results
			eTime = System.nanoTime(); //end time
			if (withTime) //if we get "with time" so print how long time the algo takes
			{
				double ansTime = (double) (eTime - sTime) / 1_000_000_000;
				ans += "\n" + (new DecimalFormat("##.###").format(ansTime) + " seconds");
			}
		}
		if(searchAlgorithm.equals("A*")) {
			AStar algo = new AStar(startingBoard, goalBoard, withOpen);
			sTime = System.nanoTime(); // start time
			ans = algo.solve(); //gets the goal puzzle with his results
			eTime = System.nanoTime(); //end time
			if (withTime) //if we get "with time" so print how long time the algo takes
			{
				double ansTime = (double) (eTime - sTime) / 1_000_000_000;
				ans += "\n" + (new DecimalFormat("##.###").format(ansTime) + " seconds");
			}
		}
		if(searchAlgorithm.equals("IDA*")) {
			IDAStar algo = new IDAStar(startingBoard, goalBoard, withOpen);
			sTime = System.nanoTime(); // start time
			ans = algo.solve(); //gets the goal puzzle with his results
			eTime = System.nanoTime(); //end time
			if (withTime) //if we get "with time" so print how long time the algo takes
			{
				double ansTime = (double) (eTime - sTime) / 1_000_000_000;
				ans += "\n" + (new DecimalFormat("##.###").format(ansTime) + " seconds");
			}
		}
		try {
			FileWriter myWriter = new FileWriter("output.txt");
			myWriter.write(ans);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Ex1.init("input.txt");
	}
}









