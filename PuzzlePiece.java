/**
 * Class Name: PuzzlePiece
 * Class Purpose: to create a piece of a jig saw puzzle
 */

public class PuzzlePiece {
	
	public static final int HEART_OUT = -1, HEART_IN = 1, CLUB_OUT = -2, CLUB_IN = 2;
	public static final int SPADE_OUT = -3, SPADE_IN = 3, DIAMOND_OUT = -4, DIAMOND_IN = 4;
	
	public static final int TOP = 1, BOTTOM = 2, RIGHT = 3, LEFT = 4;
	
	private int top, bottom, left, right;
	
	/**
	 * Constructor
	 * @param top
	 * @param right
	 * @param bottom
	 * @param left
	 */
	public PuzzlePiece (int top, int right, int bottom, int left){
		this.top = top;
		this.right = right;
		this.left = left;
		this.bottom = bottom;
	}
	/**
	 * Method Name: rotate
	 * Method Purpose: rotates the puzzle piece clockwise or counter-clockwise
	 *  according to the boolean given
	 * @param clockwise
	 */
	public void rotate(boolean clockwise){
		int temp = this.top;
		if (clockwise){
			this.top = left;
			this.left = bottom;
			this.bottom = right;
			this.right = temp;}
		else {
			this.top = right;
			this.right = bottom;
			this.bottom = left;
			this.left = temp;}
	}
	/**
	 * Method Name: getSide
	 * Method Purpose: to retrieve the what part of the puzzlepiece is at the given side
	 * @param side
	 * @return int
	 */
	public int getSide(int side) {
		if (side == 1){return this.top;}
		if (side == 2){return this.bottom;}
		if (side == 3){return this.right;}
		if (side == 4){return this.left;}
		System.err.println("error@PuzzlePiece.getSide(): int side not valid value");
		return -1;
	}
	/**
	 * Method Name: toString
	 * Method Purpose: to create a string version of PuzzlePiece that can be printed in console
	 * @return String
	 */
	public String toString(){return this.top + "_" + this.right + "_" +this. bottom + "_" + this.left;}

}
