public class ConnectFourAI {

	private int[] open_x_vals;
	private int[][] x_scores; 
	private int[][] filled_coordinates;
	private int[][] myTripleCoords;
	private int[][] oppTripleCoords;
	private int[][] myDoubleCoords;
	private int[][] oppDoubleCoords;
	private int[][] mySpacedCoords;
	private int[][] oppSpacedCoords;
	private int mySide;
	private int x_holder;
	private int y_holder;
	private boolean firstMove;
	private String myName;
	
	// ConnectFourAI Constructor
	public ConnectFourAI (int side) {
		open_x_vals = new int [7];
		x_scores = new int [7][2];
		filled_coordinates = new int [7][6];
		myTripleCoords = new int [7][6];
		oppTripleCoords = new int [7][6];
		myDoubleCoords = new int [7][6];
		oppDoubleCoords = new int [7][6];
		mySpacedCoords = new int [7][6];
		oppSpacedCoords = new int [7][6];
		mySide = side;
		firstMove = true;
		x_holder = -1;
		y_holder = -1;
		myName = "ConnectFourGenius";
	}

	// method that uses several factors to determine the best move for the current turn
	public int[] getOptimalMove (Piece[][] gameBoard) {
		int [] ret_coord = new int [2];
		
		fillCoordinates(gameBoard);
		fillGameBoard(gameBoard);
		
		for (int l = 0; l < 7; l++){
			System.out.println("" + l + ", " + x_scores[l][0]);
		}
		
		hasDoubleSelf(gameBoard);
		hasDoubleOpp(gameBoard);
		
		hasTripleSelf(gameBoard);
		hasTripleOpp(gameBoard);
		
		hasSpacedSelf(gameBoard);
		hasSpacedOpp(gameBoard);
		
		for (int i = 0; i < 7; i++){
			System.out.println("" + i + ": " + open_x_vals[i]);
		}
		
		if (firstMove) {
			firstMove = false;
			return getRandomMove (gameBoard);
		} 
		
		for (int x = 0; x < 7; x++) {
			if (open_x_vals[x] == 0) {
				int temp_y = x_scores[x][0];
				if (myTripleCoords[x][temp_y] == 1) {
					System.out.println("yay i have a triple");
					ret_coord[0] = x;
					ret_coord[1] = temp_y;
					return ret_coord;
				}
			}
		} 
		
		for (int x = 0; x < 7; x++) {
			if (open_x_vals[x] == 0) {
				int temp_y = x_scores[x][0];
				if (mySpacedCoords[x][temp_y] == 1) {
					System.out.println("yay i have a spaced triple");
					ret_coord[0] = x;
					ret_coord[1] = temp_y;
					return ret_coord;
				}
			}
		} 
		
		for (int x = 0; x < 7; x++) {
			if (open_x_vals[x] == 0) {
				int temp_y = x_scores[x][0];
				if (oppTripleCoords[x][temp_y] == 1) {
					System.out.println("opp has a triple must block!");
					ret_coord[0] = x;
					ret_coord[1] = temp_y;
					return ret_coord;
				}
			}	
		} 
		
		for (int x = 0; x < 7; x++) {
			if (open_x_vals[x] == 0) {
				int temp_y = x_scores[x][0];
				if (oppSpacedCoords[x][temp_y] == 1) {
					System.out.println("have to block a space 4x!");
					ret_coord[0] = x;
					ret_coord[1] = temp_y;
					return ret_coord;
				}
			}
		} 
		
		
		
		for (int x = 0; x < 7; x++) {
			if (open_x_vals[x] == 0) {
				int temp_y = x_scores[x][0];
				if (!testOppWinner (x, temp_y+1, gameBoard) && !testSelfWinner(x,temp_y+1,gameBoard)) {
					if (oppDoubleCoords[x][temp_y] == 1) {
						ret_coord[0] = x;
						ret_coord[1] = temp_y;
						return ret_coord;
					}
				}
			}
		} 
		
		
		
		for (int x = 0; x < 7; x++) {
			if (open_x_vals[x] == 0) {
				int temp_y = x_scores[x][0];
				if (!testOppWinner (x,temp_y+1, gameBoard) && !testSelfWinner(x,temp_y+1,gameBoard)) {
					if (myDoubleCoords[x][temp_y] == 1) {
						ret_coord[0] = x;
						ret_coord[1] = temp_y;
						return ret_coord;
					}
				}
			}
		} 
		
		return getRandomMove (gameBoard);
	}
	
	//holds array of the y value that correspond to each available x coordinate
	public void fillCoordinates(Piece[][] gameBoard) {
		for (int x = 0; x < x_scores.length; x++) {
			x_scores[x][0] = getY(x, gameBoard);
		}
	}
	
	//keeps track of already filled coordinates by having a 1 in filled_coordinates array
	public void fillGameBoard(Piece[][] gameBoard) {
		for (int x = 0; x < 7; x++) {
			for (int y = 0; y < 6; y++) {
				if (gameBoard[x][y] instanceof Piece) {
					filled_coordinates[x][y] = 1;
				}
			}
		}
	}
	
	// checks for boundaries of the board
	public boolean isValid (int x, int y) {
		return (x >= 0)&&(x<= 6)&&(y >= 0)&&(y <= 5);
	}
	
	// gets the y-value for the corresponding x-value that can be placed currently
	// if a column is completely filled, returns -1 and the AI notes that the column is no longer playable
	public int getY (int x, Piece[][] gameBoard) {
		int val = -1;
		for (int y = 0; y < 6; y++) {
			if (canPlace(x, y, gameBoard)) {
				val = y;
			}
		}
		if (val == -1) {
			open_x_vals[x] = 1;
		}
		return val;
	}
	
	/**
	   * The next section of code consists of various tests that check certain states of the board
	   */ 
	
	public void hasDoubleSelf(Piece[][] gameBoard) {
		
		int a, b, c, d = 0;
		int e, f, g, h = 0;
		int side = mySide;
			
		for (int x = 0; x < 7; x++) {
	    	for (int y = 0; y < 6; y++) {
	    		if (gameBoard[x][y] instanceof Piece && gameBoard[x][y].side() == side) {
	    			a = piece_exists(x, y, x+1, y+1, side, gameBoard);
	    			fillDoubleSelf (a, x_holder, y_holder);
	    			
	    			b = piece_exists(x, y, x, y+1, side, gameBoard);
	    			fillDoubleSelf (b, x_holder, y_holder);
		    			
	    			c = piece_exists(x, y, x+1, y, side, gameBoard);
	    			fillDoubleSelf (c, x_holder, y_holder);
		    			
	    			d = piece_exists(x, y, x-1, y+1, side, gameBoard);
	    			fillDoubleSelf (d, x_holder, y_holder);
		    			
		    		e = piece_exists(x, y, x-1, y-1, side, gameBoard);
		    		fillDoubleSelf (e, x_holder, y_holder);
		    		
		    		f = piece_exists(x, y, x, y-1, side, gameBoard);
		    		fillDoubleSelf (f, x_holder, y_holder);
		    		
		    		g = piece_exists(x, y, x-1, y, side, gameBoard);
		    		fillDoubleSelf (g, x_holder, y_holder);
		    		
		    		h = piece_exists(x, y, x+1, y-1, side, gameBoard);
		    		fillDoubleSelf (h, x_holder, y_holder);
	    		  }
	    	 }
	    }
	}
	
	public void hasDoubleOpp(Piece[][] gameBoard) {
		
		int a, b, c, d = 0;
		int e, f, g, h = 0;
		int side = (mySide+1)%2;
			
		for (int x = 0; x < 7; x++) {
	    	for (int y = 0; y < 6; y++) {
	    		if (gameBoard[x][y] instanceof Piece && gameBoard[x][y].side() == side) {
	    			a = piece_exists(x, y, x+1, y+1, side, gameBoard);
	    			fillDoubleOpp (a, x_holder, y_holder);
	    			
	    			b = piece_exists(x, y, x, y+1, side, gameBoard);
	    			fillDoubleOpp (b, x_holder, y_holder);
		    			
	    			c = piece_exists(x, y, x+1, y, side, gameBoard);
	    			fillDoubleOpp (c, x_holder, y_holder);
		    			
	    			d = piece_exists(x, y, x-1, y+1, side, gameBoard);
	    			fillDoubleOpp (d, x_holder, y_holder);
		    			
		    		e = piece_exists(x, y, x-1, y-1, side, gameBoard);
		    		fillDoubleOpp (e, x_holder, y_holder);
		    		
		    		f = piece_exists(x, y, x, y-1, side, gameBoard);
		    		fillDoubleOpp (f, x_holder, y_holder);
		    		
		    		g = piece_exists(x, y, x-1, y, side, gameBoard);
		    		fillDoubleOpp (g, x_holder, y_holder);
		    		
		    		h = piece_exists(x, y, x+1, y-1, side, gameBoard);
		    		fillDoubleOpp (h, x_holder, y_holder);
	    		  }
	    	 }
	    }
	}
	
	public void hasTripleSelf(Piece[][] gameBoard) {
		
		int a, b, c, d = 0;
		int e, f, g, h = 0;
		int side = mySide;
			
		for (int x = 0; x < 7; x++) {
	    	for (int y = 0; y < 6; y++) {
	    		if (gameBoard[x][y] instanceof Piece && gameBoard[x][y].side() == side) {
	    			a = piece_exists(x, y, x+1, y+1, side, gameBoard);
	    			fillTripleSelf (a, x_holder, y_holder);
	    			
	    			b = piece_exists(x, y, x, y+1, side, gameBoard);
	    			fillTripleSelf (b, x_holder, y_holder);
		    			
	    			c = piece_exists(x, y, x+1, y, side, gameBoard);
	    			fillTripleSelf (c, x_holder, y_holder);
		    			
	    			d = piece_exists(x, y, x-1, y+1, side, gameBoard);
	    			fillTripleSelf (d, x_holder, y_holder);
		    			
		    		e = piece_exists(x, y, x-1, y-1, side, gameBoard);
		    		fillTripleSelf (e, x_holder, y_holder);
		    		
		    		f = piece_exists(x, y, x, y-1, side, gameBoard);
		    		fillTripleSelf (f, x_holder, y_holder);
		    		
		    		g = piece_exists(x, y, x-1, y, side, gameBoard);
		    		fillTripleSelf (g, x_holder, y_holder);
		    		
		    		h = piece_exists(x, y, x+1, y-1, side, gameBoard);
		    		fillTripleSelf (h, x_holder, y_holder);
	    		  }
	    	 }
	    }
	}
	
	public void hasTripleOpp(Piece[][] gameBoard) {
		
		int a, b, c, d = 0;
		int e, f, g, h = 0;
		int side = (mySide+1)%2;
			
		for (int x = 0; x < 7; x++) {
	    	for (int y = 0; y < 6; y++) {
	    		if (gameBoard[x][y] instanceof Piece && gameBoard[x][y].side() == side) {
	    			a = piece_exists(x, y, x+1, y+1, side, gameBoard);
	    			fillTripleOpp (a, x_holder, y_holder);
	    			
	    			b = piece_exists(x, y, x, y+1, side, gameBoard);
	    			fillTripleOpp (b, x_holder, y_holder);
		    			
	    			c = piece_exists(x, y, x+1, y, side, gameBoard);
	    			fillTripleOpp (c, x_holder, y_holder);
		    			
	    			d = piece_exists(x, y, x-1, y+1, side, gameBoard);
	    			fillTripleOpp (d, x_holder, y_holder);
		    			
		    		e = piece_exists(x, y, x-1, y-1, side, gameBoard);
		    		fillTripleOpp (e, x_holder, y_holder);
		    		
		    		f = piece_exists(x, y, x, y-1, side, gameBoard);
		    		fillTripleOpp (f, x_holder, y_holder);
		    		
		    		g = piece_exists(x, y, x-1, y, side, gameBoard);
		    		fillTripleOpp (g, x_holder, y_holder);
		    		
		    		h = piece_exists(x, y, x+1, y-1, side, gameBoard);
		    		fillTripleOpp (h, x_holder, y_holder);
	    		  }
	    	 }
	    }
	}

	public void hasSpacedSelf(Piece[][] gameBoard) {
		
		int a, b, c, d = 0;
		int e, f, g, h = 0;
		int side = mySide;
			
		for (int x = 0; x < 7; x++) {
	    	for (int y = 0; y < 6; y++) {
	    		if (gameBoard[x][y] instanceof Piece && gameBoard[x][y].side() == side) {
	    			a = piece_exists(x, y, x+1, y+1, side, gameBoard);
	    			fillSpacedSelf (a, side, x_holder, y_holder, x_holder+1, y_holder+1, gameBoard);
	    			
	    			b = piece_exists(x, y, x, y+1, side, gameBoard);
	    			fillSpacedSelf (b, side, x_holder, y_holder, x_holder, y_holder+1, gameBoard);
		    			
	    			c = piece_exists(x, y, x+1, y, side, gameBoard);
	    			fillSpacedSelf (c, side, x_holder, y_holder, x_holder+1, y_holder, gameBoard);
		    			
	    			d = piece_exists(x, y, x-1, y+1, side, gameBoard);
	    			fillSpacedSelf (d, side, x_holder, y_holder, x_holder-1, y_holder+1, gameBoard);
		    			
		    		e = piece_exists(x, y, x-1, y-1, side, gameBoard);
		    		fillSpacedSelf (e, side, x_holder, y_holder, x_holder-1, y_holder-1, gameBoard);
		    		
		    		f = piece_exists(x, y, x, y-1, side, gameBoard);
		    		fillSpacedSelf (f, side, x_holder, y_holder, x_holder, y_holder-1, gameBoard);
		    		
		    		g = piece_exists(x, y, x-1, y, side, gameBoard);
		    		fillSpacedSelf (g, side, x_holder, y_holder, x_holder-1, y_holder, gameBoard);
		    		
		    		h = piece_exists(x, y, x+1, y-1, side, gameBoard);
		    		fillSpacedSelf (h, side, x_holder, y_holder, x_holder+1, y_holder-1, gameBoard);
	    		  }
	    	 }
	    }
	}
	
	public void hasSpacedOpp(Piece[][] gameBoard) {
		
		int a, b, c, d = 0;
		int e, f, g, h = 0;
		int side = (mySide+1)%2;
			
		for (int x = 0; x < 7; x++) {
	    	for (int y = 0; y < 6; y++) {
	    		if (gameBoard[x][y] instanceof Piece && gameBoard[x][y].side() == side) {
	    			a = piece_exists(x, y, x+1, y+1, side, gameBoard);
	    			fillSpacedOpp (a, side, x_holder, y_holder, x_holder+1, y_holder+1, gameBoard);
	    			
	    			b = piece_exists(x, y, x, y+1, side, gameBoard);
	    			fillSpacedOpp (b, side, x_holder, y_holder, x_holder, y_holder+1, gameBoard);
		    			
	    			c = piece_exists(x, y, x+1, y, side, gameBoard);
	    			fillSpacedOpp (c, side, x_holder, y_holder, x_holder+1, y_holder, gameBoard);
		    			
	    			d = piece_exists(x, y, x-1, y+1, side, gameBoard);
	    			fillSpacedOpp (d, side, x_holder, y_holder, x_holder-1, y_holder+1, gameBoard);
		    			
		    		e = piece_exists(x, y, x-1, y-1, side, gameBoard);
		    		fillSpacedOpp (e, side, x_holder, y_holder, x_holder-1, y_holder-1, gameBoard);
		    		
		    		f = piece_exists(x, y, x, y-1, side, gameBoard);
		    		fillSpacedOpp (f, side, x_holder, y_holder, x_holder, y_holder-1, gameBoard);
		    		
		    		g = piece_exists(x, y, x-1, y, side, gameBoard);
		    		fillSpacedOpp (g, side, x_holder, y_holder, x_holder-1, y_holder, gameBoard);
		    		
		    		h = piece_exists(x, y, x+1, y-1, side, gameBoard);
		    		fillSpacedOpp (h, side, x_holder, y_holder, x_holder+1, y_holder-1, gameBoard);
	    		  }
	    	 }
	    }
	}
	
	public void fillDoubleSelf (int val, int x, int y) {
		if (isValid(x,y) && val >= 1 && filled_coordinates[x][y] != 1) {
			myDoubleCoords[x][y] = 1;
		}
	}
	
	public void fillDoubleOpp (int val, int x, int y) {
		if (isValid(x,y) && val >= 1 && filled_coordinates[x][y] != 1) {
			oppDoubleCoords[x][y] = 1;
		}
	}
	
	public void fillTripleSelf (int val, int x, int y) {
		if (isValid(x,y) && val >= 2 && filled_coordinates[x][y] != 1) {
			myTripleCoords[x][y] = 1;
		}
	}
	
	public void fillTripleOpp (int val, int x, int y) {
		if (isValid(x,y) && val >= 2 && filled_coordinates[x][y] != 1) {
			System.out.println("opp triple registered");
			oppTripleCoords[x][y] = 1;
		}
	}
	
	public void fillSpacedSelf (int val, int side, int x, int y, int next_x, int next_y, Piece[][] gameBoard) {
		if (isValid(x,y) && isValid(next_x, next_y) && val >= 1 && filled_coordinates[x][y] != 1 && gameBoard[next_x][next_y] != null && gameBoard[next_x][next_y].side() == side) {
			mySpacedCoords[x][y] = 1;
		}
	}
	
	public void fillSpacedOpp (int val, int side, int x, int y, int next_x, int next_y, Piece[][] gameBoard) {
		if (isValid(x,y) && isValid(next_x, next_y) && val >= 1 && filled_coordinates[x][y] != 1 && gameBoard[next_x][next_y] != null && gameBoard[next_x][next_y].side() == side) {
			oppSpacedCoords[x][y] = 1;
		}
	}
	
	// recursive method to determine longest streak of a particular side
	public int piece_exists(int prev_x, int prev_y, int x, int y, int side, Piece[][] gameBoard) {
		 if ( (x>6)||(y>5)||(y<0)||(x<0) ) {
			return 0;
		 } else if (gameBoard[x][y] instanceof Piece && gameBoard[x][y].side() == side) {
			 x_holder = x + (x-prev_x);
			 y_holder = y + (y-prev_y);
			 return 1 + piece_exists(x, y, x + (x-prev_x), y + (y-prev_y), side, gameBoard);
		 }
		 return 0;
	  }
	
	// checks whether you can place a piece in a given (x,y) coordinate
	public boolean canPlace(int x, int y, Piece[][] gameBoard){
		if (gameBoard[x][y] instanceof Piece){
			return false;
		}
		boolean filled = true;
		for (int k = 0; k < y; k++) {
			if (!(gameBoard[x][k] instanceof Piece)){
				filled = false;
			}
		}
		return filled;
	}
	
	/**
	   * tests whether placing a piece in the coordinate sets up the above coordinate for an
	   * opponent win. method exists to prevent AI from placing it in such locations.
	   */ 
	public boolean testOppWinner (int x, int y, Piece[][] gameBoard) {
		Piece p = new Piece ((mySide+1)%2, null);
		Boolean b = false;
		if (isValid(x,y)) {
			gameBoard[x][y] = p;
			if (oppWinner(gameBoard)) {
				b = true;
			}
			gameBoard[x][y] = null;
		}
		return b;
	}
	
	/**
	   * tests whether placing a piece in the coordinate sets up the above coordinate for a win.
	   * method exists to prevent cases where AI allows the opponent to immediately block a winning
	   * combination by ConnectFourAI placing it there too soon.
	   */ 
	public boolean testSelfWinner (int x, int y, Piece[][] gameBoard) {
		Piece p = new Piece (mySide, null);
		Boolean b = false;
		if (isValid(x,y)) {
			gameBoard[x][y] = p;
			if (selfWinner(gameBoard)) {
				b = true;
			}
			gameBoard[x][y] = null;
		}
		return b;
	}
	
	// checks if the opponent has a winning combination of pieces
	public boolean oppWinner (Piece[][] gameBoard) {
		int a = 0;
		int b = 0;
		int c = 0;
		int d = 0;
		int max_streak = 0;
		int side = (mySide+1)%2;
	    for (int x = 0; x < 7; x++) {
	    	for (int y = 0; y < 6; y++) {
	    		if (gameBoard[x][y] instanceof Piece) {
	    			side = gameBoard[x][y].side();
	    			a = piece_exists(x, y, x+1, y+1, side, gameBoard);
	    			b = piece_exists(x, y, x, y+1, side, gameBoard);
	    			c = piece_exists(x, y, x+1, y, side, gameBoard);
	    			d = piece_exists(x, y, x-1, y+1, side, gameBoard);
	    		}
	    		int max_streak1 = Math.max(a,b);
	    		int max_streak2 = Math.max(c,d);
	    		int temp_streak = Math.max(max_streak1,  max_streak2);
	    		if (temp_streak > max_streak) {
	    			max_streak = temp_streak;
	    		}
	    		if (max_streak >= 3) {
	    			return true;
	    		}
	    	}
	     }
	     return false;
	}
	
	// checks if ConnectFourAI has a winning combination of pieces
	public boolean selfWinner (Piece[][] gameBoard) {
		int a = 0;
		int b = 0;
		int c = 0;
		int d = 0;
		int max_streak = 0;
		int side = mySide;
	    for (int x = 0; x < 7; x++) {
	    	for (int y = 0; y < 6; y++) {
	    		if (gameBoard[x][y] instanceof Piece) {
	    			side = gameBoard[x][y].side();
	    			a = piece_exists(x, y, x+1, y+1, side, gameBoard);
	    			b = piece_exists(x, y, x, y+1, side, gameBoard);
	    			c = piece_exists(x, y, x+1, y, side, gameBoard);
	    			d = piece_exists(x, y, x-1, y+1, side, gameBoard);
	    		}
	    		int max_streak1 = Math.max(a,b);
	    		int max_streak2 = Math.max(c,d);
	    		int temp_streak = Math.max(max_streak1,  max_streak2);
	    		if (temp_streak > max_streak) {
	    			max_streak = temp_streak;
	    		}
	    		if (max_streak >= 3) {
	    			return true;
	    		}
	    	}
	     }
	     return false;
	}
	
	// returns a random coordinate to be placed
	public int[] getRandomMove (Piece[][] gameBoard) {
		int[] ret_coord = new int [2];
		while (true) {
			int rand_x = (int)(Math.random()*7);
			if (open_x_vals[rand_x] == 1) {
				while (open_x_vals[rand_x] != 1) {
					rand_x = (int)(Math.random()*7);
				}
			}
			for (int y = 0; y < 6; y++) {
				if(canPlace(rand_x, y, gameBoard)) {
					ret_coord[0] = rand_x;
					ret_coord[1] = y;
					filled_coordinates[rand_x][y] = 1;
					return ret_coord;
				}
			}
		}
	}
	
	// override toString() method with myName variable
	public String toString() {
		return myName;
	}
	
}
