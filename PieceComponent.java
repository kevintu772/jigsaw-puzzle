import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;
/* 
 * 
 * PieceComponent class: provides a graphical version of PuzzlePiece; subclass of PuzzlePiece
 * 		extends PuzzlePiece
 * 
 * 
 * CONSTRUCTORS:
 * PieceComponent(int top, int right, int bottom, int left)
 * 		constructs a new PieceComponent containing given values
 * PieceComponent(PuzzlePiece puzzlePiece)
 * 		constructs a new PieceComponent based on existing PuzzlePiece
 * 
 * METHODS:
 * highlight(boolean selected): changes alphaTint of piece to clear if selected, else changes to opaque
 * rotate(boolean clockwise): rotates a piece 90 degrees clockwise if true, else counterclockwise
 * rotateNoGraphics(boolean clockwise): same as rotate, except only calls PuzzlePiece rotate, does not change rotation var
 * toString(): returns a String representation of PieceComponent
 * 
 * GETTERS AND SETTERS:
 * get/set...
 * X, Y		primary coordinates
 * XS, YS	secondary coordinates
 * Image	image
 * Rotation	rotation
 * AlphaTint tint of piece
 * Side		sidelength
 * Used		whether piece is used or not
 * 		*getter for boolean used is .used()
 */

public class PieceComponent extends PuzzlePiece{

	//////////////////////////////////////////////////////////////////////
	private PuzzlePiece puzzlePiece;
	// primary coordinates
	private int x;
	private int y;
	// secondary coordinates
	private int xS;
	private int yS;
	// image of the PieceComponent
	private BufferedImage image;
	// degrees rotated
	private int rotation;
	// opaqueness of piece
	private float alphaTint;
	// side length of piece
	private int side;
	// true or false whether PieceComponent has been used or not
	private boolean used;
	// GETTERS AND SETTERS
	public void setUsed(boolean used){this.used = used;}
	public boolean used(){return this.used;}
	public int getXS(){return this.xS;}
	public void setXS(int xS) {this.xS = xS;}//
	public int getYS() {return yS;}
	public void setYS(int yS) {this.yS = yS;}// this.repaint();}
	public int getX() {return x;}
	public void setX(int x) {this.x = x;}//
	public int getY() {return y;}
	public void setY(int y) {this.y = y;}// this.repaint();}
	public int getRotation() {return rotation;}
	public void setRotation(int rotation) {this.rotation = rotation;}
	public int getSide() {return side;}
	public void setSide(int side) {this.side = side;}
	public BufferedImage getImage() {return image;}
	public void setImage(BufferedImage image) {this.image = image;}
	public float getAlphaTint() {return alphaTint;}
	public void setAlphaTint(float alphaTint) {this.alphaTint = alphaTint;}
	//////////////////////////////////////////////////////////////////////////////

	/* highlight method: changes the tint of PieceComponent
	 * param: boolean selected (true= clear; false= opaque)
	 * no return
	 */
	public void highLight(boolean selected){
		if (selected){this.alphaTint = .5f;}
		else if (!selected){this.alphaTint = 1f;}
	}

	/* constructor: based on data for sides of puzzlePiece
	 * param: int top, right, bottom, left (data values for sides)
	 */
	public PieceComponent(int top, int right, int bottom, int left){
		super(top, right, bottom, left);
		this.alphaTint = 1f;
		this.rotation = 0;
		//this.scale = .9;
		this.side = 118;
		//this.puzzlePiece = puzzlePiece;
		used = false;
		int pieceNum = decodePieceNum();
		try{this.image = ImageIO.read(new File("PuzzleImages/piece_" + pieceNum + ".png"));}
		catch (IOException e) {System.err.println("error @PieceComponent constructor");};
	}

	/* constructor: based on puzzlePiece
	 * param: PuzzlePiece puzzlePieces
	 */
	public PieceComponent(PuzzlePiece puzzlePiece){
		super(puzzlePiece.getSide(PuzzlePiece.TOP), puzzlePiece.getSide(PuzzlePiece.RIGHT), puzzlePiece.getSide(PuzzlePiece.BOTTOM), puzzlePiece.getSide(PuzzlePiece.LEFT));
		this.alphaTint = 1f;
		this.rotation = 0;
		//this.scale = .9;
		this.side = 118;
		used = false;
		//		if (pieceNum == 1){this.puzzlePiece = new PuzzlePiece(-2, -1, 4, 2);} 16
		//		else if (pieceNum == 2){this.puzzlePiece = new PuzzlePiece(-3, -4, 3, 1);} 36
		//		else if (pieceNum == 3){this.puzzlePiece = new PuzzlePiece(-1, -3, 3, 2);} 18
		//		else if (pieceNum == 4){this.puzzlePiece = new PuzzlePiece(-1, -4, 2, 2);} 16
		//		else if (pieceNum == 5){this.puzzlePiece = new PuzzlePiece(-3, -3, 1, 2);} 18
		//		else if (pieceNum == 6){this.puzzlePiece = new PuzzlePiece(-1, -4, 4, 1);} 16
		//		else if (pieceNum == 7){this.puzzlePiece = new PuzzlePiece(-3, -4, 1, 4);} 48
		//		else if (pieceNum == 8){this.puzzlePiece = new PuzzlePiece(-2, -1, 3, 1);} 6
		//		else if (pieceNum == 9){this.puzzlePiece = new PuzzlePiece(-2, 2, 4, -4);} 64
		int pieceNum = decodePieceNum();
		try{this.image = ImageIO.read(new File("PuzzleImages/piece_" + pieceNum + ".png"));}
		catch (IOException e) {System.err.println("error @PieceComponent constructor");};
	}

	/* rotate method: calls PuzzlePiece rotate and changes rotation var
	 * param: boolean clockwise (true for clockwise, false for counter)
	 * no return
	 */
	public void rotate(boolean clockwise){
		//System.out.println("good");
		super.rotate(clockwise);
		//System.out.println(super.toString());
		if (clockwise){rotation += 90;}
		else {rotation -= 90;}
	}

	/* rotateNoGraphics: only calls the rotate method in PuzzlePiece
	 * no param
	 * no return
	 */
	public void rotateNoGraphics(boolean clockwise){
		super.rotate(clockwise);
		//System.out.println(super.toString());
	}

	/* toString method
	 * no param
	 * return: PuzzlePiece representation of PieceComponent
	 */
	public String toString(){
		return super.toString();
	}

	/* decodePieceNum method: "decodes" a PuzzlePiece to find corresponding image
	 * no param
	 * return: int from 1 to 9, each of which corresponds with a png image
	 */
	private int decodePieceNum(){
		int top = this.getSide(PuzzlePiece.TOP);
		int left = this.getSide(PuzzlePiece.LEFT);
		int product = top*left*this.getSide(PuzzlePiece.BOTTOM)*this.getSide(PuzzlePiece.RIGHT);
		if (product == 6){return 8;}
		else if(product == 64){return 9;}
		else if(product == 48){return 7;}
		else if(product == 36){return 2;}
		else if(product == 18){
			if (top == -1){return 3;}
			if (top == -3){return 5;}
		}
		else if(product == 16){
			if (top == -2){return 1;}
			else if (left == 2){return 4;}
			else if (left == 1){return 6;}
		}
		return -1;
	}

}
