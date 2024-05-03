package sudokuV5;

import java.util.Random;

/**
 * The Sudoku number puzzle to be solved
 */
public class Puzzle {
   // All variables have package access
   // The numbers on the puzzle
   int[][] numbers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
   // The clues - isGiven (no need to guess) or need to guess
   boolean[][] isGiven = new boolean[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
   boolean[][] isNotBlank = new boolean[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];

   
   
   private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
   // Constructor
   public Puzzle() {
      super();
   }

// Generate a new puzzle given the number of cells to be guessed, which can be used
   // to control the difficulty level.
   // This method shall set (or update) the arrays numbers and isGiven
   public void newPuzzle(int cellsToGuess, String diffLevel) {
	  
      
      generateSudoku(numbers);
      
      int[][]softcodedNumbers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
      
      for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
          for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
        	 if( numbers[row][col] != 0) {
        		 softcodedNumbers[row][col] = numbers[row][col];
        	 }
          }
       }
      
      //set all to not blank;
      for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
          for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
       	   isNotBlank[row][col] = true;
          }
      }
      
      generateblanks(isNotBlank, diffLevel);
      
      // Set isGiven array based on the filled cells
      for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
         for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {        	
            if (numbers[row][col] != 0) {
            	isGiven[row][col] = isNotBlank[row][col];
            }
         }
      }
   }

   // Generate a new Sudoku puzzle
   private static boolean generateSudoku(int[][] grid) {
       Random rand = new Random();
       for (int row = 0; row < 9; row++) {
           for (int col = 0; col < 9; col++) {
               if (grid[row][col] == 0) { // If cell is empty
                   // Try numbers 1 to 9 randomly
                   for (int num : getRandomNumbers()) {
                       if (isValidNumber(grid, row, col, num)) {
                           grid[row][col] = num; // Assign the number
                           if (generateSudoku(grid)) { // Recursively fill next cell
                               return true;
                           }
                           grid[row][col] = 0; // Backtrack
                       }
                   }
                   return false; // No valid number found for this cell
               }
           }
       }
       return true; // All cells filled
   }
   
   //Generate random blanks
   private static boolean generateblanks(boolean[][] grid, String diffLevel) {
	   Random rand = new Random();
	   int numBlanks = 0;
	   
	 //converting from non-static to instance to read in a static
	   if (diffLevel.equals("Easy")) {
	       numBlanks = rand.nextInt(5);
	       while (numBlanks == 0) {
	            numBlanks = rand.nextInt(5);
	       }
	   } else if (diffLevel.equals("Normal")) {
	       numBlanks = rand.nextInt(10);
	       while (numBlanks <= 5) {
	            numBlanks = rand.nextInt(10);
	       }
	   } else if (diffLevel.equals("Hard")) {
	       numBlanks = rand.nextInt(15);
	       while (numBlanks <= 10) {
	            numBlanks = rand.nextInt(15);
	       }
	   }
	   //randomise the number of blanks & ensure that num is not 0
	    

	   
	   
	   //randomise the row and col
	   for (int empty = numBlanks; empty > 0; empty--) {
		   int row = rand.nextInt(SudokuConstants.GRID_SIZE);
		   int col = rand.nextInt(SudokuConstants.GRID_SIZE);
		   //check if is false already.
		   while (!grid[row][col]) {
			   row = rand.nextInt(SudokuConstants.GRID_SIZE);
			   col = rand.nextInt(SudokuConstants.GRID_SIZE);
		   }
			   grid[row][col] = false;
	   }
	   
	   return true; //when blanks is 0 and is done;
   }
   
   // Check if a number is valid for the current cell
   private static boolean isValidNumber(int[][] grid, int row, int col, int num) {
      // Check row
      for (int i = 0; i < SudokuConstants.GRID_SIZE; i++) {
         if (grid[row][i] == num) {
            return false;
         }
      }
      // Check column
      for (int i = 0; i < SudokuConstants.GRID_SIZE; i++) {
         if (grid[i][col] == num) {
            return false;
         }
      }
      // Check 3x3 subgrid
      int startRow = row - row % 3;
      int startCol = col - col % 3;
      for (int i = startRow; i < startRow + 3; i++) {
         for (int j = startCol; j < startCol + 3; j++) {
            if (grid[i][j] == num) {
                return false;
            }
         }
      }
      return true; // Number is valid
   }
   private static int[] getRandomNumbers() {
       int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
       Random rand = new Random();
       for (int i = numbers.length - 1; i > 0; i--) {
           int index = rand.nextInt(i + 1);
           // Swap numbers[index] and numbers[i]
           int temp = numbers[index];
           numbers[index] = numbers[i];
           numbers[i] = temp;
       }
       return numbers;
   }
}





