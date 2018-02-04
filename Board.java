/*
 * class Board
 * This class enables the creation of a Board object, which is a 2D array of PuzzlePieces, and has a width and height
 * It contains the methods to place and remove PuzzlePieces on the Board and to determine the validity of spots
 * 
 */
public class Board {
	//size: 
	private int width;
	private int height;
	private PuzzlePiece[][] board;
	//ctor
	public Board(int width, int height){
		this.width=width;
		this.height=height;
		board = new PuzzlePiece [height][width];
	}

	/*
	 * Constructor for a Board object, which is a 2D array of PuzzlePieces, and has a width and height
	 * Parameters: PuzzlePiece[][]board
	 */
	public Board(PuzzlePiece[][]board){
		this.board=board;
		height = getHeight();
		width = getWidth();
	}
	/*
	 * Getter method that returns the 2D array of PuzzlePieces
	 */
	public PuzzlePiece[][]getBoard(){
		return board;
	}
	/*
	 * Getter method that returns the width of the Board
	 */
	public int getWidth(){
		return width;
	}
	/*
	 * Getter method that returns the height of the Board
	 */
	public int getHeight(){
		return height;	
	}

	/*
	 * Method to return the PuzzlePiece at a given spot
	 * Parameters: int row, col
	 * Return: original spot
	 */
	public PuzzlePiece getSpot(int row, int col){
		if(isValid(row,col)){
			return board[row][col];
		}
		return null;
	}
	/*
	 * Changes the PuzzlePiece at a given spot
	 * Parameter: int row, col, PuzzlePiece piece
	 * Return: original spot
	 */
	public PuzzlePiece setSpot(int row, int col, PuzzlePiece piece){
		PuzzlePiece temp = null;
		if(isValid(row,col)){
			temp=getSpot(row,col);
			board[row][col]=piece;
			return temp;
		}
		return temp;
	}


	/*
	 * Method that removes a PuzzlePiece from a given spot on the Board
	 * Parameters: int row, col
	 * Return: Original PuzzlePiece
	 */
	public PuzzlePiece remove(int row, int col){
		PuzzlePiece temp=null;
		if(isValid(row,col)){
			temp=board[row][col];
			board[row][col]=null;
		}
		return temp;
	}

	/*
	 * Method that sets every spot on Board to null, 
	 * and adds all the PuzzlePieces from the Board to the ArrayList "unused"
	 */
	public void clear(){
		for(int i =0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				board[i][j]=null;
			}
		}
	}

	/*
	 * Method to return true if board is occupied at a given spot and false if it is null
	 * Parameters: int row,col
	 * Return: true if spot is occupied, false if null
	 */
	public boolean isOccupied(int row, int col){
		if(isValid(row,col)){
			return board[row][col] !=null;
		}
		return false;
	}

	public boolean isValid(int row, int col){
		return ((row>=0)&&(row<width)&&(col>=0)&&(col<height));
	}

	/*
	 * toString() method returns a String representation of the Board
	 */
	public String toString(){
		String toReturn = "";
		for (int i = 0; i < board.length; i ++){
			for (int j = 0; j < board[0].length; j ++){
				PuzzlePiece value = board[i][j];
				if (value == null){toReturn += " _ _ _ --";}
				else{toReturn += board[i][j] + "--";}
			}
			toReturn += "\n";
		}
		return toReturn;
	}


	public static void main(String[] args) {
		Board a=new Board(10,10);
		PuzzlePiece b=new PuzzlePiece(1,2,3,4);
		for(int i =0;i<a.getWidth();i++){
			for(int j=0; j<a.getHeight(); j++){
				a.setSpot(i,j,b);
			}
		}

		System.out.println(a.getSpot(1,1));

		a.remove(1,1);
		System.out.println(a.isOccupied(1,1));
		a.clear();
		System.out.println(a.isOccupied(2,5));
		System.out.println();
	}
}