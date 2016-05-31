/*Board.java*/

/**
 * Represents a Board configuration of a game of Connect Four
 * @author Taehyung (Kevin) Kim
 */

public class Board {

	public Piece [][] pieces;
	public int side; // keeps track of which player is in turn
	private int pos_x; // keeps track of current x position
	private int pos_y; // keeps track of current y position
	private int numRed; // counts the number of active Red pieces on the board
	private int numBlue; // counts the number of active Blue pieces on the board
	private boolean hasPlaced; // whether a piece has been moved in the player's turn
	public boolean inGame; // whether the game is in session or not
  /**
   * Constructs a new Board
   */
  public Board() {
	pieces = new Piece[7][6];
	pos_x = -1;
	pos_y = -1;
	side = 0;  
	numRed = 0;
	numBlue = 0;
	hasPlaced = false;
	inGame = true;
    }

  /**
   * gets the Piece at coordinates (x, y)
   */
  public Piece pieceAt(int x, int y) {
	  if (((x>6)||(y>5)||(y<0)||(x<0)) || (!(pieces[x][y] instanceof Piece))) {
		  return null;
	  }
	  return pieces[x][y];
  }

  /**
   * Places a Piece at coordinate (x, y)
   */
  public void place(int x, int y) {
	  if ( (x>6)||(y>5)||(y<0)||(x<0) ){
		return;
	} else {
		Piece p = new Piece (side, this);
		pieces[x][y] = p;
	    p.set_X(x);
	    p.set_Y(y);
	}
	hasPlaced = true;
  }

  // returns the number of Fire Pieces on the board
  public int returnRedCount() {
	  return numRed;
  }
  
  
  //return the board
  public Piece[][] returnBoard() {
	  return pieces;
  }
  
  // returns the number of Water Pieces on the board
  public int returnBlueCount() {
	  return numBlue;
  }

  // returns whether a piece has been placed this turn
  public boolean returnPlaced() {
	  return hasPlaced;
  }
  
  // returns whether the game is in session or not
  public boolean getInGame() {
	  return inGame;
  }
  
  // ends the Game
  public void endGame() {
	  inGame = false;
  }
  
  /**
   * Determines if a Piece can be placed at (x,y) 
   */
public boolean canPlace(int x, int y) {
	if (pieces[x][y] instanceof Piece){
		return false;
	}
	
		boolean filled = true;
		for (int k = 0; k < y; k++) {
			if (!(pieces[x][k] instanceof Piece)){
				filled = false;
			}
		}
			if (filled) {
			return true;
			}
		
	  return false;
  }

  /**
   * Determines if the turn can end
   */
  public boolean canEndTurn() {
    if (hasPlaced){
    	return true;
    }
    return false;
  }

  /**
   * Ends the current turn. Changes the player.
   */
  public void endTurn() {
	  if (canEndTurn()) {
	        side = 1 - side;
	  }
	  hasPlaced = false;
  }

  /**
   * Returns the winner of the game
   */
  public String winner() {
	  int a = 0;
	  int b = 0;
	  int c = 0;
	  int d = 0;
	  int max_streak = 0;
	  int side = -1;
    for (int x = 0; x < 7; x++) {
    	for (int y = 0; y < 6; y++) {
    		if (pieces[x][y] instanceof Piece) {
    			side = pieces[x][y].side();
    			a = piece_exists(x, y, x+1, y+1, side);
    			b = piece_exists(x, y, x, y+1, side);
    			c = piece_exists(x, y, x+1, y, side);
    			d = piece_exists(x, y, x-1, y+1, side);
    		}
    		int max_streak1 = Math.max(a,b);
    		int max_streak2 = Math.max(c,d);
    		int temp_streak = Math.max(max_streak1,  max_streak2);
    		if (temp_streak > max_streak) {
    			max_streak = temp_streak;
    		}
    		if (max_streak >= 3) {
    			return "" + side;
    		}
    	}
     }
     return null;
  }
  
  /**
   * Checks whether or not piece exists at given location
   */ 
  public int piece_exists(int prev_x, int prev_y, int x, int y, int side) {
	 if ( (x>6)||(y>5)||(y<0)||(x<0) ) {
		return 0;
	 } else if (pieces[x][y] instanceof Piece && pieces[x][y].side() == side) {
		 return 1 + piece_exists(x, y, x + (x-prev_x), y + (y-prev_y), side);
	 }
	 return 0;
  }
 
  /**
   * Draws the board in its current state
   */ 
  public void drawBoard() {
	  
	  for (int i = 0; i < pieces.length; i++) {
	      for (int j = 0; j < pieces[0].length; j++) {
	        
	    	  if (i == pos_x && j == pos_y && (pieces[pos_x][pos_y] != null)) {
	    		  StdDrawPlus.setPenColor(StdDrawPlus.WHITE);
          		  StdDrawPlus.filledSquare(i + .5, j + .5, .5);
	    	  } else if ((i + j) % 2 == 0) {
	    		  StdDrawPlus.setPenColor(StdDrawPlus.PINK);
	          } else {
	        	  StdDrawPlus.setPenColor(StdDrawPlus.YELLOW);
	          }
	    	  	  StdDrawPlus.filledSquare(i + .5, j + .5, .5);
	        
	    	  if (pieces[i][j] != null) {
	    		  if (pieces[i][j] instanceof Piece) {
	    			 if (pieces[i][j].side() == 0) {
	    				 StdDrawPlus.picture(i + .5, j + .5, "img/x.png", 1, 1);
	    			  } else {
	    				 StdDrawPlus.picture(i + .5, j + .5, "img/o.png", 1, 1);
	    			  }
	    		   }
	    	  	}
	         }
	      }    
  	   }
  
  
  /**
   * Starts a game
   */
  public static void main(String[] args) {
    Board board = new Board ();
    ConnectFourAI bot1 = new ConnectFourAI(1);
	StdDrawPlus.setScale(0, 7);
	
	while (board.inGame) { 
		board.drawBoard();
		
		if (board.side == 0) {
			if (StdDrawPlus.mousePressed()) {
				int x = (int)(StdDrawPlus.mouseX());
				int y = (int)(StdDrawPlus.mouseY());
				
				if (board.canPlace(x, y)) {
					board.place(x, y);
				}
			}
		} else {
			int[] coord = bot1.getOptimalMove(board.pieces);
			int comp_x = coord[0];
			int comp_y = coord[1];
			board.place(comp_x, comp_y);
		}
		board.drawBoard();
		if (board.winner() != null) {
			System.out.println("Good Game");
			if (board.side == 0) {
				System.out.println("Player wins!");
			} else {
				System.out.println(bot1.toString() + " wins!");
			}
    		board.endGame();
    	}
		if (board.canEndTurn()) {
    		board.endTurn();
    	}
    	
    	StdDrawPlus.show(100); 
	  }
    }
  }