import java.util.ArrayList;

/**
 * Class Name: Puzzle
 * Class Purpose: To create a jig saw puzzle
 */
public class Puzzle {
	private Board board;
	private int width;
	private int height;
	private PuzzlePiece[] unused1;
	private ArrayList<PuzzlePiece> unused = new ArrayList<PuzzlePiece>();

	/**
	 * Constructor
	 * @param width
	 * @param height
	 * @param a
	 */
	public Puzzle(int width, int height, ArrayList<PuzzlePiece> a){
		this.width=width;
		this.height=height;
		this.unused=a;
		board=new Board(width, height);

	}
	/**
	 * Constructor
	 * @param width
	 * @param height
	 * @param a
	 */
	public Puzzle(int width, int height, PuzzlePiece[] a){
		this.width=width;
		this.height=height;
		this.unused1=a;
		board = new Board(width, height);
	}
	/**
	 * Method Name: GetWidth()
	 * Method Purpose: Retrieve the width of the puzzle
	 * @return width
	 */
	public int getWidth(){
		return width;
	}
	/**
	 * Method Name: getHeight()
	 * Method Purpose: Retrieve the height of the puzzle
	 * @return height
	 */
	public int getHeight(){
		return height;
	}
	/**
	 * Method Name: isSolved()
	 * Method Purpose: To find out whether the puzzle is solved or not
	 * @return size of unused array
	 */
	public boolean isSolved(){
		return (unused.size() == 0);
	}
	/**
	 * Method Name: solve
	 * Method Purpose: to solve the puzzle
	 */
	public void solve(){
		board.clear(); //clears the board so no problems with random pieces in the board

		helper(0,0);
	}
	/**
	 * Method Name: Helper
	 * Method Purpose: a helper method that helps the main solve method
	 * to solve the puzzle, returns whether the puzzle is solved or not
	 * @param row
	 * @param col
	 * @return boolean
	 */
	public boolean helper(int row, int col){ 
		if(isSolved()) return true;
		//this method uses recursion to help figure out if solved
		int length = unused.size(); 
		if(col >= board.getWidth()){
			col = 0;
			row++; //increments the rows inside the recursion
		}
		for(int i = 0; i < length; i++){

			for(int j = 0; j < 4; j++){

				if(doesFit(row,col, unused.get(i))){
					placePiece(row,col, unused.get(i)); //places a piece if the piece fits in the location
					
					if(helper(row, col+1)){ //increments the columns inside the recursion once 
											//finds out if a piece belongs
						return true;
					}
					remove(row, col, i); //removes it if the piece next to it doesn't fit
				}
				
				unused.get(i).rotate(true); //rotates the piece in order to find out if another rotation works
			}
		}
		return false;

	}
	/**
	 * Method Name: isValid
	 * Method Purpose: to find out whether the given spot is on the puzzle or not
	 * return a boolean according to if it is on the board or not
	 * @param row
	 * @param col
	 * @return boolean
	 */
	private boolean isValid(int row, int col){
		return ((row>=0)&&(row<height)&&(col>=0)&&(col<width));
	}

	/**
	 * Method Name: doesFit
	 * Method Purpose: to find out if the given puzzle piece will fit into the given spot according to the surrounding spaces
	 * @param row
	 * @param col
	 * @param a
	 * @return boolean
	 */
	public boolean doesFit(int row, int col, PuzzlePiece a){
		boolean mayContinue = false; //creates the boolean that will be returned, once it is set to true 
									 //it will break out of the method

		if(isValid(row,col)) { 
			if (isValid(row, col - 1)) { //Checks the piece below it
				PuzzlePiece pieceOnBoard = board.getBoard()[row][col-1]; 
				if (pieceOnBoard != null) {
					if (a.getSide(PuzzlePiece.LEFT) + pieceOnBoard.getSide(PuzzlePiece.RIGHT) == 0) {
						mayContinue = true;
					}
					else {
						mayContinue = false;
					}
				}
				else {
					mayContinue = true;
				}
			}
			else {
				mayContinue = true;
			}
			
			if (mayContinue) {
				if (isValid(row, col + 1)) { //checks the piece above it
					PuzzlePiece pieceOnBoard = board.getBoard()[row][col+1]; 
					if (pieceOnBoard != null) {
						if (a.getSide(PuzzlePiece.RIGHT) + pieceOnBoard.getSide(PuzzlePiece.LEFT) == 0) {
							mayContinue = true;
						}
						else {
							mayContinue = false;
						}
					}
					else {
						mayContinue = true;
					}

				}
				else {
					mayContinue = true;
				}
			}
			
			if (mayContinue) { 
				if (isValid(row - 1, col)) { //checks piece to the left
					PuzzlePiece pieceOnBoard = board.getBoard()[row - 1][col]; 
					if (pieceOnBoard != null) {
						if (a.getSide(PuzzlePiece.TOP) + pieceOnBoard.getSide(PuzzlePiece.BOTTOM) == 0) {
							mayContinue = true;
						}
						else {
							mayContinue = false;
						}
					}
					else {
						mayContinue = true;
					}
				}
				else {
					mayContinue = true;
				}
			}
			
			if (mayContinue) {
				if (isValid(row + 1, col)) { //checks piece to the right
					PuzzlePiece pieceOnBoard = board.getBoard()[row + 1][col]; 
					if (pieceOnBoard != null) {
						if (a.getSide(PuzzlePiece.BOTTOM) + pieceOnBoard.getSide(PuzzlePiece.TOP) == 0) {
							mayContinue = true;
						}
						else {
							mayContinue = false;
						}
					}
					else {
						mayContinue = true;
					}
				}
				else {
					mayContinue = true;
				}
			}
		}
		return mayContinue;
	}

	/**
	 * Method Name: getPiece
	 * Method Purpose: To retrieve the puzzle piece at the given spot
	 * @param row
	 * @param col
	 * @return PuzzlePiece
	 */
	public PuzzlePiece getPiece(int row, int col){
		if(isValid(row,col)){
			return board.getSpot(row,col);
		}
		return null;
	}

	/**
	 * Method Name: placePiece
	 * Method Purpose: to place the given puzzle piece at the given spot
	 * returns the piece that was is going to be replaced
	 * @param row
	 * @param col
	 * @param piece
	 * @return PuzzlePiece
	 */
	public PuzzlePiece placePiece(int row, int col, PuzzlePiece piece){

		if (unused.contains(piece)){
			unused.remove(piece);}

		else {System.err.println("error@ Puzzle.placePiece() piece not in unusedCollection");}

		return board.setSpot(row, col, piece);
	}
	/**
	 * Method Name: remove
	 * Method Purpose: to remove the puzzle piece at the given spot of the puzzle
	 * returns the piece that was removed
	 * @param row
	 * @param col
	 * @return PuzzlePiece
	 */
	public PuzzlePiece remove(int row, int col){

		if (this.isOccupied(row, col)){

			unused.add(board.getSpot(row, col));}
		return board.remove(row, col);
	}
	/**
	 * Method Name: remove
	 * Method Purpose: an overloaded method of the previous version that also adds the piece 
	 * that was removed to the unused board, does the same thing other than that
	 * @param row
	 * @param col
	 * @param index
	 * @return PuzzlePiece
	 */
	public PuzzlePiece remove(int row, int col, int index){
		PuzzlePiece temp=null;
		if(isValid(row,col)){
			temp=board.getBoard()[row][col];
			board.getBoard()[row][col]=null;
		}
		unused.add(index, temp);
		return temp;
	}
	/**
	 * Method Name: clear
	 * Method Purpose: to remove all the puzzle pieces from the puzzle board
	 */
	public void clear(){

		for(int i =0; i<board.getBoard().length; i++){
			for(int j=0; j<board.getBoard()[0].length; j++){
				if (this.isOccupied(i, j)){unused.add((PieceComponent)this.getPiece(i, j));}
				board.remove(i, j);

			}
		}

	}
	/**
	 * Method Name: getUnusedPieces
	 * Method Purpose: to retrieve an array full of the unused pieces of the puzzle
	 * @return ArrayList<PuzzlePieces>
	 */
	public ArrayList<PuzzlePiece> getUnusedPieces() {
		return unused;
	}
	/**
	 * Method Name: toString
	 * Method Purpose: to create a string version of the puzzle that can be printed out in the console
	 * @return String
	 */
	public String toString(){
		String s= "";
		for(int i =0;i<board.getBoard().length;i++){
			for(int j=0; j<board.getBoard()[0].length; j++){
				if (board.getSpot(i, j) != null) {
					s += board.getSpot(i, j).toString() + ";  ";
				}
				else {s+= "null;  ";}
			}
			s += "\n";
		}
		return s;
	}
	/**
	 * Method Name: isOccupied
	 * Method Purpose: to find out whether the given spot has a puzzle piece inside it or not
	 * @param row
	 * @param col
	 * @return boolean
	 */
	public boolean isOccupied(int row, int col){
		return board.getSpot(row,col)!=null;
	}

	/**
	 * Method Name: main
	 * Method Purpose: to run and test the class
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Started");

		/*
		 * public static final int HEART_IN = 1, HEART_OUT = -1, CLUB_IN = 2,
			CLUB_OUT = -2, SPADE_IN = 3, SPADE_OUT = -3, DIAMOND_IN = 4,
			DIAMOND_OUT = -4;
		 */
		ArrayList<PuzzlePiece> puzzelPieces= new ArrayList<PuzzlePiece>();

		PuzzlePiece p2= new PuzzlePiece(-3,-4,3,1);
		puzzelPieces.add(p2);

		PuzzlePiece p3= new PuzzlePiece(-1,-3,3,2);
		puzzelPieces.add(p3);
		
		PuzzlePiece p1= new PuzzlePiece(-2,-1,4,2);
		puzzelPieces.add(p1);

		PuzzlePiece p4= new PuzzlePiece(-1,-4,2,2);
		puzzelPieces.add(p4);
		
		PuzzlePiece p5= new PuzzlePiece(-3,-3,1,2);
		puzzelPieces.add(p5);

		PuzzlePiece p7= new PuzzlePiece(-3,-4,1,4);
		puzzelPieces.add(p7);

		PuzzlePiece p8= new PuzzlePiece(-2,-1,3,1);
		puzzelPieces.add(p8);

		PuzzlePiece p6= new PuzzlePiece(-1,-4,4,1);
		puzzelPieces.add(p6);

		PuzzlePiece p9= new PuzzlePiece(-2,2,4,-4);
		puzzelPieces.add(p9);

		Puzzle b= new Puzzle(3,3,puzzelPieces);	

		System.out.println(b + "before");

		System.out.println(b.getUnusedPieces());

		b.solve();
		System.out.println(b + "after");

		System.out.println("Done");

	}

}