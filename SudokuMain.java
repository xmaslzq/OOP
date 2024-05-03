package sudokuV5;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
// Add any other imports you need below this line
import javax.swing.SwingUtilities;

/**
 * The main Sudoku program.
 */
public class SudokuMain extends JFrame {
   private static final long serialVersionUID = 1L;  // to prevent serial warning

   // Private variables
   GameBoardPanel board = new GameBoardPanel(); // Assuming GameBoardPanel is a custom JPanel
   JButton btnNewGame = new JButton("New Game");
   JButton btnNewGameEasy = new JButton("Easy Mode");
   JButton btnNewGameNormal = new JButton("Normal Mode");
   JButton btnNewGameHard = new JButton("Hard Mode");
   
   private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
   private Puzzle puzzle = new Puzzle();
   
   //to reference later for randomise of blanks
   public String diffLevel = "Easy";
   
   public String getDiffLevel() {
	    return diffLevel;
   }
   
   public void setDiffLevel(String level) {
	    this.diffLevel = level;
   }
   
   // Constructor
   public SudokuMain() {
      Container cp = getContentPane();
      cp.setLayout(new BorderLayout());

      cp.add(board, BorderLayout.CENTER); // Add the game board to the center

      // Add a button to the south to re-start the game via board.newGame()
      cp.add(btnNewGame, BorderLayout.SOUTH);
      
      cp.add(btnNewGameEasy, BorderLayout.NORTH);
	  cp.add(btnNewGameNormal, BorderLayout.EAST);
	  cp.add(btnNewGameHard, BorderLayout.WEST);
	  
      btnNewGame.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
        	  board.newGame(diffLevel);
        	  SoundEffect.NEW_GAME.play();
        	  SoundEffect.BGMUSIC.play();
        	  
          }
      });
      
      btnNewGameEasy.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
    		  diffLevel = "Easy";
    		  board.newGame(diffLevel);
    		  SoundEffect.NEW_GAME.play();
    		  SoundEffect.BGMUSIC.play();
		  };
	  });
	  
	  btnNewGameNormal.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
    		  diffLevel = "Normal";
    		  board.newGame(diffLevel);
    		  SoundEffect.NEW_GAME.play();
    		  SoundEffect.BGMUSIC.play();
		  }
	  });
	  
	  btnNewGameHard.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
    		  diffLevel = "Hard";
    		  board.newGame(diffLevel);
    		  SoundEffect.NEW_GAME.play();
    		  SoundEffect.BGMUSIC.play();
		  };
	  });
	  board.newGame(diffLevel);
	  SoundEffect.NEW_GAME.play();
	  SoundEffect.BGMUSIC.play();
	  SoundEffect.BGMUSIC.loop();
	  
	  ImageIcon game= new ImageIcon("./sudoku/Sudoku.png");  
	  SwingUtilities.invokeLater(new Runnable() {
		  public void run() {
			  JOptionPane.showMessageDialog(SudokuMain.this,
              "ARE YOU READY FOR THIS", 
              "SUDOKU", 
              JOptionPane.INFORMATION_MESSAGE,
              game
              );
     	 }
      });
      pack();     // Pack the UI components, instead of using setSize()
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window closing
      setTitle("Sudoku");
      setVisible(true);   // Make the frame visible
   }

    /** The entry main() method */
    public static void main(String[] args) {
        // Run GUI codes in the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //new SwingTemplate(); //for testing
                new SudokuMain();    // Let the constructor do the job                   
            }
        });
    }
}
