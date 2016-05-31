/*Piece.java*/

/**
 *  Represents a Normal Piece in Checkers61bl
 * @author 
 */

public class Piece {
  
  /**
   *  Define any variables associated with a Piece object here.  These
   *  variables MUST be private or package private.
   */
	private int  mySide;
	private Board myBoard;
	private int x_coord;
	private int y_coord;

  /**
	* Initializes a Piece
	* @param  side The side of the Piece
	* @param  b    The Board the Piece is on
	*/
  Piece(int side, Board b) {
	mySide = side;
	myBoard = b;    
  }	

  /**
   * Returns the side that the piece is on
   * @return 0 if the piece is fire and 1 if the piece is water
   */
  public int side() {
    return mySide;
  }
  
  // gets the board the piece is on
  public Board getBoard(){
	  return myBoard;
  }

  // set the x-coordinate
  public void set_X(int x){
	  x_coord = x;
  }
  
  // set the y-coordinate
  public void set_Y(int y){
	  y_coord = y;
  }
  
  // get the x-coordinate
  public int get_X() {
	  return x_coord;
  }
  
  // get the y-coordinate
  public int get_Y() {
	  return y_coord;
  }
  
 // Replaces the toString method to return type Piece
  public String toString (){
	  return "Piece";
  }

}