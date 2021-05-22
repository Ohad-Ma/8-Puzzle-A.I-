# A.I 8 Puzzle game

## Background
This JAVA project is part of an A.I course - Introduction to artificial intelligence and Operations Research.
 
 We were given an **enhanced 8 puzzle game** - NxM (N rows, M cols) with unique operations and were told to solve it with an option of displaying the **time** it took to run, and an **open list** which will display the lists.
  
  In order to solve it there will be a usage of various search algorithms:
 * A* 
 * DFID - Depth First Iterative Deepening.
 * BFS - Breadth-First-Search.
 * DFBnB - Depth First Branch and Bound.
 * IDA* - Iterative deepening A*.
 
 ####Options
 
 There is an additional editable `input.txt` file in the project which contains (in the correct order) :
 - The name of the desirable algorithm from the list above. (1)
 - An option to display the time it took for the specific algorithm to finish (2) -
      * `"with time"` - for displaying the time or `"no time"`.
 - An option to display on the screen an open list each algorithm stored (3) -
      * `"with open"` - display the list.
      * `"no open"` - do not display the list.
 - A desirable N rows and M columns in the format `NxM` (4) .  
 - The puzzle itself (5).
 - The solved puzzle which the puzzle supposed to turn to when the specific algorithm finishes it job (6).
 
 ####input.txt Example
 
      BFS 
      with time
      no open
      3x4
      1,2,3,4
      5,6,11,7
      9,10,8,_
      Goal state:
      1,2,3,4
      5,6,7,8
      9,10,11,_
      
  
  As the format above shows there are multiple lines each lines has a role in this project (which described above).
  
  The other methods of this game is being able to solve a puzzle which looks like: 
  
      1,_,4
      3,5,6
      2,_,7
  
 ####Special conditions
 - For a single block movement (which close to the empty block) there is a cost of 5 for each moves
 - If the board contains **TWO** empty blocks then the nearby blocks will be able to move parallel horizontal or vertical, two vertical blocks movements **costs 6** 
 and two horizontal blocks movements **costs 7**.
 
 For example, by given:
  
 ![puzzle](https://imgur.com/LRDNIhB.png)
  
  
In order to reach the final solution; 6 cans be move left and 7 & 8 cans be move up together.
Therefore, the cost of the "sliding" above will be 5 (single block) + 7 (two blocks up) which equals to 12.
 
 
 
 #####Finally:
 
 For any other suggestions/improvements feel free to contact me or just drop an issue
 
 
 
 
 
