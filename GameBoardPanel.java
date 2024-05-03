package sudokuV5;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
   private static final long serialVersionUID = 1L; // to prevent serial warning

   // Define named constants for UI sizes
   public static final int CELL_SIZE = 60; // Cell width/height in pixels
   public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
   public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
   // Board width/height in pixels

   // Define properties
   /** The game board composes of 9x9 Cells (customized JTextFields) */
   private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
   /** It also contains a Puzzle with array numbers and isGiven */
   private Puzzle puzzle = new Puzzle();
   

   /** Constructor */
   public GameBoardPanel() {
      super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE)); // JPanel

      // Allocate the 2D array of Cell, and added into JPanel.
      for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
         for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
            cells[row][col] = new Cell(row, col);
            super.add(cells[row][col]); // JPanel
         }
      }

      // [TODO 3] Allocate a common listener as the ActionEvent listener for all the
      // Cells (JTextFields)
      CellInputListener listener = new CellInputListener();
      
      // [TODO 4] Adds this common listener to all editable cells
      for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
           for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
               if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);   // For all editable rows and cols
               }
           }
      }
      super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
      
      //Add KeyListener to each editable cell
      for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
          for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
              if (cells[row][col].isEditable()) {
            	  CellKeyListener keyListener = new CellKeyListener(row, col);
                  cells[row][col].addKeyListener(keyListener); // Add KeyListener to cell
                  //System.out.println("Recorded"); //debug
              }
          }
      }
      
   }
   
   //create similar to focus on left/right/up/down editable cell below
   public void focusFirstEditableCell() {
       for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
           for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
               if (cells[row][col].isEditable()) {
                   cells[row][col].requestFocusInWindow();
                   System.out.println("I found it!"); //debug
                   return; // Focus set, exit method
               }
           }
       }
   }
   /**
    * Generate a new puzzle; and reset the game board of cells based on the puzzle.
    * You can call this method to start a new game.
    */
   public void newGame(String diffLevel) {
      // Generate a new puzzle
      puzzle.newPuzzle(2, diffLevel);

      // Initialize all the 9x9 cells, based on the puzzle.
      for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
         for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
            cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
         }
      }
      
      SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              focusFirstEditableCell();
          }
      });
   }

   /**
    * Return true if the puzzle is solved
    * i.e., none of the cell have status of TO_GUESS or WRONG_GUESS
    */
   public boolean isSolved() {
      for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
         for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
            if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
               return false;
            }
         }
      }
      return true;
   }
  
   // [TODO 2] Define a Listener Inner Class for all the editable Cells
   private class CellInputListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         // Get a reference of the JTextField that triggers this action event
         Cell sourceCell = (Cell) e.getSource();

         // Retrieve the int entered
         int numberIn = Integer.parseInt(sourceCell.getText());
         // For debugging
         System.out.println("You entered " + numberIn);

         /*
          * [TODO 5] (later - after TODO 3 and 4)
          * Check the numberIn against sourceCell.number.
          * Update the cell status sourceCell.status,
          * and re-paint the cell via sourceCell.paint().
          */
         if (numberIn == sourceCell.number) {
            sourceCell.status = CellStatus.CORRECT_GUESS;
            SoundEffect.CORRECT_GUESS.play();
         } else {
            sourceCell.status = CellStatus.WRONG_GUESS;
            SoundEffect.WRONG_GUESS.play();
         }
            
         //this is for conflicting   
         int row = sourceCell.row;
         int col = sourceCell.col;
         
         
         // Check row
         for (int j = 0; j < SudokuConstants.GRID_SIZE; j++) {
         	if (j == col) {
         		continue;
         	}
         	Cell currentcell = cells[row][j];
         	final CellStatus[][] prevStatus = currentcell.getPrevStatus();
         	
         	if ( (cells[row][j].number == numberIn) && (prevStatus[row][j] != CellStatus.TO_GUESS) && cells[row][j].status != CellStatus.CORRECT_GUESS) {
                 cells[row][j].status = CellStatus.CONFLICTING;
                 //System.out.println("Cell at row " + row + ", col " + i + " is conflicting."); //debug
             } else if ((cells[row][j].number != numberIn) && cells[row][j].status != CellStatus.CORRECT_GUESS){
             	cells[row][j].status = prevStatus[row][j];
             	//System.out.println("Cell at row " + row + ", col " + i + "status is: " + prevStatus[i][j]); //debug
             } else if (sourceCell.status == CellStatus.CORRECT_GUESS && cells[row][j].status != CellStatus.CORRECT_GUESS) {
             	cells[row][j].status = prevStatus[row][j];
             }
         	if (cells[row][j].status == CellStatus.CORRECT_GUESS) {
         		prevStatus[row][j] = cells[row][j].status;
         	}
             cells[row][j].paint();
         }

         // Check column
         for (int i = 0; i < SudokuConstants.GRID_SIZE; i++) {
         	if (i == row) {
         		continue;
         	}
         	Cell currentcell = cells[i][col];
         	final CellStatus[][] prevStatus = currentcell.getPrevStatus();
         	
         	if (cells[i][col].number == numberIn && prevStatus[i][col] != CellStatus.TO_GUESS) {
                 cells[i][col].status = CellStatus.CONFLICTING;
                 //System.out.println("Cell at row " + i + ", col " + col + " is conflicting."); //debug
             } else if ((cells[i][col].number != numberIn) && cells[i][col].status != CellStatus.CORRECT_GUESS) {
             	cells[i][col].status = prevStatus[i][col];
             	//System.out.println("Cell at row " + i + ", col " + col + "status is: " + prevStatus[i][j]); //debug
             } else if (sourceCell.status == CellStatus.CORRECT_GUESS && cells[i][col].status != CellStatus.CORRECT_GUESS) {
             	cells[i][col].status = prevStatus[i][col];
             }
         	
         	if (cells[i][col].status == CellStatus.CORRECT_GUESS) {
         		prevStatus[i][col] = cells[i][col].status;
         	}
             cells[i][col].paint();
         }
         
         // Check 3x3 subgrid
         int startRow = row - row % 3;
         int startCol = col - col % 3;
         
         for (int i = startRow; i < startRow + 3; i++) {
             for (int j = startCol; j < startCol + 3; j++) {
             	if (i == row && j == col) {
             		continue;
             	}
             	Cell currentcell = cells[i][j];
             	final CellStatus[][] prevStatus = currentcell.getPrevStatus();
             	
             	
             	if (cells[i][j].number == numberIn && prevStatus[i][j] != CellStatus.TO_GUESS) {
                     cells[i][j].status = CellStatus.CONFLICTING;
                     //System.out.println("Cell at row " + i + ", col " + j + " is conflicting."); //debug
                 }
             	
             	if ((cells[i][j].number != numberIn) && cells[i][j].status != CellStatus.CORRECT_GUESS) {
                 	cells[i][j].status = prevStatus[i][j];
                 	//System.out.println("Cell at row " + i + ", col " + j + "status is: " + prevStatus[i][j]); //debug
                 } 
             	
             	if ((cells[i][j].number != numberIn) && sourceCell.status == CellStatus.CORRECT_GUESS && cells[i][j].status != CellStatus.CORRECT_GUESS) {
                 	cells[i][j].status = prevStatus[i][j];
                 }
             	
             	if (cells[i][j].status == CellStatus.CORRECT_GUESS) {
             		prevStatus[i][j] = cells[i][j].status;
             	}
             	cells[i][j].paint();
             }
         }

         sourceCell.paint(); // re-paint this cell based on its status

         /*
          * [TODO 6] (later)
          * Check if the player has solved the puzzle after this move,
          * by calling isSolved(). Put up a congratulation JOptionPane, if so.
          */
         if (GameBoardPanel.this.isSolved()) {
	         // Display a congratulatory message
        	 SoundEffect.CONGRATS.play();
        	 ImageIcon img= new ImageIcon("./sudoku/image.png"); 
	         SwingUtilities.invokeLater(new Runnable() {
	        	 public void run() {
	        		 JOptionPane.showMessageDialog(GameBoardPanel.this, 
	                 "Congratulations! You've solved the puzzle.", 
	                 "Puzzle Solved", 
	                 JOptionPane.INFORMATION_MESSAGE,
	                 img);
	        	 }
	         });
         }
      }
   }
   private class CellKeyListener extends KeyAdapter {
	    private int row;
	    private int col;

	    public CellKeyListener(int row, int col) {
	        this.row = row;
	        this.col = col;
	    }

	    @Override
	    public void keyPressed(KeyEvent e) {
	        int key = e.getKeyCode();
	        
	        if (cells[row][col].isEditable()) {
		        if (key == KeyEvent.VK_RIGHT) {
		        	//System.out.println("RIght pressed. Focusing next editable cell."); //debug
		            focusNextEditableCell(row, col);
		            //System.out.println("Next editable cell focused."); //debug
		        }
		        
		        if (key == KeyEvent.VK_LEFT) {
		        	//System.out.println("Left pressed. Focusing next editable cell."); //debug
		            focusPrevEditableCell(row, col);
		            //System.out.println("Next editable cell focused."); //debug
		        }
		        
		        if (key == KeyEvent.VK_UP) {
		        	//System.out.println("Up pressed. Focusing next editable cell."); //debug
		            focusUpEditableCell(row, col);
		            //System.out.println("Next editable cell focused."); //debug
		        }
		        
		        if (key == KeyEvent.VK_DOWN) {
		        	//System.out.println("Down pressed. Focusing next editable cell."); //debug
		            focusDownEditableCell(row, col);
		            //System.out.println("Next editable cell focused."); //debug
		        }
		    }
	    }
	    
	    
	    
	    //setting up/down/left/right arrow keys
	    private void focusNextEditableCell(int currentRow, int currentCol) {
	        int nextRow = currentRow;
	        int nextCol = currentCol;

	        // Find the next editable cell
	        do {
	            nextCol++;
	            if (nextCol == SudokuConstants.GRID_SIZE) {
	                nextCol = 0;
	                nextRow++;
	            }
	            if (nextRow == SudokuConstants.GRID_SIZE) {
	                nextRow = 0;
	            }
	        } while (nextRow < SudokuConstants.GRID_SIZE && !cells[nextRow][nextCol].isEditable());

	        // Focus on the next editable cell if it exists
	        if (nextRow < SudokuConstants.GRID_SIZE && cells[nextRow][nextCol].isEditable()) {
	            cells[nextRow][nextCol].requestFocusInWindow();
	        }
	    }
	    
	    private void focusPrevEditableCell(int currentRow, int currentCol) {

	        int prevRow = currentRow;
	        int prevCol = currentCol;

	        // Find the next editable cell
	        do {
	            prevCol--;
	            if (prevCol < 0) {
	                prevCol = SudokuConstants.GRID_SIZE - 1;
	                prevRow--;
	            }
	            if (prevRow < 0) {
	                prevRow = SudokuConstants.GRID_SIZE - 1;
	            }
	        } while (prevRow >= 0 && !cells[prevRow][prevCol].isEditable());

	        // Focus on the next editable cell if it exists
	        if (prevRow >= 0 && cells[prevRow][prevCol].isEditable()) {
	            cells[prevRow][prevCol].requestFocusInWindow();
	        }
	    }
	    
	    private void focusUpEditableCell(int currentRow, int currentCol) {

	        int upRow = currentRow;

	        // Find the next editable cell in same row
	        do {
	            upRow--;
	            if (upRow < 0) {
	                upRow = SudokuConstants.GRID_SIZE - 1;
	            }
	        } while (upRow >= 0 && !cells[upRow][currentCol].isEditable());

	        // Focus on the next editable cell if it exists
	        if (upRow >= 0 && cells[upRow][currentCol].isEditable()) {
	            cells[upRow][currentCol].requestFocusInWindow();
	        }
	    }
	    
	    private void focusDownEditableCell(int currentRow, int currentCol) {
	        int downRow = currentRow;

	        // Find the next editable cell in same row
	        do {
	            downRow++;
	            if (downRow == SudokuConstants.GRID_SIZE) {
	                downRow = 0;
	            }
	        } while (downRow >= 0 && !cells[downRow][currentCol].isEditable());

	        // Focus on the next editable cell if it exists
	        if (downRow >= 0 && cells[downRow][currentCol].isEditable()) {
	            cells[downRow][currentCol].requestFocusInWindow();
	        }
	    }
   }
}
