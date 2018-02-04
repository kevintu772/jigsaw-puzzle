import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JFrame;

/*
 * PuzzleGame 
 * 
 * This class graphically represents the hearts,clubs,spades, and diamonds jigsaw puzzle game.
 * The class prepares two graphical boards, one to hold unused pieces and one to hold used pieces.
 * The user is allowed to click and drag pieces between these two boards, as well as rotate pieces.
 * The class also instantiates buttons that allow the user to control the game.
 *
 */
public class PuzzleGame extends JComponent implements MouseMotionListener, MouseListener, MouseWheelListener{

	/*
	 * main method
	 */
	public static void main(String[]args){
		ArrayList<PuzzlePiece> a = new ArrayList<PuzzlePiece>();
		// (top, bottom, left, right)
		//Initializes the pieces to be used in the game
		PuzzlePiece a1 = new PieceComponent(-2, -1, 4, 2);
		PuzzlePiece a2 = new PieceComponent(-3, -4, 3, 1);
		PuzzlePiece a3 = new PieceComponent(-1, -3, 3, 2);
		PuzzlePiece a4 = new PieceComponent(-1, -4, 2, 2);
		PuzzlePiece a5 = new PieceComponent(-3, -3, 1, 2);
		PuzzlePiece a6 = new PieceComponent(-1, -4, 4, 1);
		PuzzlePiece a7 = new PieceComponent(-3, -4, 1, 4);
		PuzzlePiece a8 = new PieceComponent(-2, -1, 3, 1);
		PuzzlePiece a9 = new PieceComponent(-2, 2, 4, -4);
		a.add(a1);
		a.add(a2);
		a.add(a3);
		a.add(a4);
		a.add(a5);
		a.add(a6);
		a.add(a7);
		a.add(a8);
		a.add(a9);
		Puzzle p = new Puzzle(3, 3, a);
		PuzzleGame game = new PuzzleGame(p);
		game.play();

	}

	// represent spacing on the game's window
	public final int BANK_BOTTOM_SPACING = 354;
	public final int BANK_LEFT_SPACING = 306;
	public final int BANK_RIGHT_SPACING = 660;
	public final int BOARD_LEFT_SPACING = 48;
	public final int BOARD_RIGHT_SPACING = 258;
	public final int BOARD_GRID_WIDTH = 70;
	public final int IMAGE_WIDTH = 118;

	// offsets piece coordinates to fit the board
	public final int PIECE_OFFSET = 24;

	/*
	 * constructor that builds a PuzzleGame for the user to interact with
	 * 
	 * Parameters:
	 * 	Puzzle p = the puzzle the user will be playing with
	 */
	public PuzzleGame(Puzzle p){
		this.puzzle = p;
		this.allPieces = new ArrayList<PieceComponent>();
		for (int i = 0; i < p.getUnusedPieces().size(); i++){
			this.allPieces.add((PieceComponent)p.getUnusedPieces().get(i));
		}
		int puzzleWidth = puzzle.getWidth();
		int puzzleHeight = puzzle.getHeight();
		bankPieces = new PieceComponent[puzzleHeight][puzzleWidth];
		boardPieces = new PieceComponent[puzzleHeight][puzzleWidth];
		for (int i = 0, counter = 0; i < puzzleHeight; i++){
			for (int j = 0; j < puzzleWidth; j++, counter ++){
				allPieces.get(counter).setXS(BANK_LEFT_SPACING + IMAGE_WIDTH * j);
				allPieces.get(counter).setYS(IMAGE_WIDTH * i);
				bankPieces[i][j] = allPieces.get(counter);
			}
		}
	}
	private JFrame frame;

	/*
	 * plays the game
	 */
	public void play(){
		frame = new JFrame("Puzzle Game");
		frame.setLayout(new BorderLayout());
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.addMouseWheelListener(this);

		frame.add(this, BorderLayout.CENTER);
		ControlsComponent controls = new ControlsComponent(this);
		frame.add(controls.addControls(), BorderLayout.SOUTH);

		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(708, 506);
		frame.setVisible(true);

		frameUpdater.start();

	}


	// checks to see what action the selected piece is performing, then updates the graphics accordingly
	private ActionListener graphicalUpdate = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			if (translating){
				canDoSomething = false;
				transX += xInterv;
				transY += yInterv;
				selected.setX((int)transX);
				selected.setY((int)transY);
				if (transX > selected.getXS()-5 && transX < selected.getXS()+5 && transY > selected.getYS()-5 && transY < selected.getYS()+5){
					canDoSomething = true;
					selected.setUsed(false);
					translating = false;
				}
			}

			if (rotatingClockwise){
				canDoSomething = false;
				selected.setRotation(selected.getRotation() + 5);
				rotationCounter += 5;
				if (rotationCounter == 90){
					canDoSomething = true;
					selected.rotateNoGraphics(true);
					rotationCounter = 0;
					rotatingClockwise = false;
				}
			}
			if (rotatingCounterClockwise){
				canDoSomething = false;
				selected.setRotation(selected.getRotation() - 5);
				rotationCounter +=5;
				if (rotationCounter == 90){
					canDoSomething = true;
					selected.rotateNoGraphics(false);
					rotationCounter = 0;
					rotatingCounterClockwise = false;
				}
			}
			if (congratulating){
				Random gen = new Random();
				Color color = new Color(gen.nextInt(256), gen.nextInt(256), gen.nextInt(256));
				setBackground(color);
				frame.getContentPane().setBackground(color);
			}
			else {frame.getContentPane().setBackground(Color.WHITE);}
			//System.out.println("happening");
			PuzzleGame.this.repaint();
		}
	};
	// initial delay: 0 milliseconds; action to launch: taskPerformer; delay between fires: 5 milliseconds
	private Timer frameUpdater = new Timer(0, graphicalUpdate){{
		this.setDelay(10);
	}};

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 * paints the board, bank, and any pieces inside of either
	 * 
	 * Parameters:
	 * 	Graphics g = the graphics to draw the game
	 */
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		//System.out.println("happy");

		g2.setStroke(new BasicStroke(2));
		// painting board
		// horizontal lines
		for (int i = 0; i <= puzzle.getWidth(); i++){
			Point2D.Double from = new Point2D.Double(BOARD_LEFT_SPACING, BOARD_LEFT_SPACING + BOARD_GRID_WIDTH * i);
			Point2D.Double to = new Point2D.Double(BOARD_RIGHT_SPACING, BOARD_LEFT_SPACING + BOARD_GRID_WIDTH * i);		
			Line2D.Double line = new Line2D.Double(from, to);
			g2.draw(line);
		}
		// vertical lines
		for (int i = 0; i <= puzzle.getHeight(); i++){
			Point2D.Double from = new Point2D.Double(BOARD_LEFT_SPACING + BOARD_GRID_WIDTH * i, BOARD_LEFT_SPACING );
			Point2D.Double to = new Point2D.Double(BOARD_LEFT_SPACING + BOARD_GRID_WIDTH * i, BOARD_RIGHT_SPACING);		
			Line2D.Double line = new Line2D.Double(from, to);
			g2.draw(line);
		}
		// painting bank (delete lines later)
		// horizontal lines
		for (int i = 0; i <= puzzle.getWidth(); i++){
			Point2D.Double from = new Point2D.Double(BANK_LEFT_SPACING, IMAGE_WIDTH * i);
			Point2D.Double to = new Point2D.Double(BANK_RIGHT_SPACING,IMAGE_WIDTH * i);		
			Line2D.Double line = new Line2D.Double(from, to);
			g2.draw(line);
		}
		// vertical lines
		for (int i = 0; i <= puzzle.getHeight(); i++){
			Point2D.Double from = new Point2D.Double(BANK_LEFT_SPACING + IMAGE_WIDTH * i, 0 );
			Point2D.Double to = new Point2D.Double(BANK_LEFT_SPACING + IMAGE_WIDTH * i, BANK_BOTTOM_SPACING);		
			Line2D.Double line = new Line2D.Double(from, to);
			g2.draw(line);
		}
		for (int i = 0; i < bankPieces.length; i++){
			for (int j = 0; j < bankPieces[0].length; j++){
				PieceComponent a = bankPieces[i][j];
				float alphaTint = 1F;
				if (selected == a){
					alphaTint = .5F;
				}
				if (!a.used()){
					AffineTransform old = g2.getTransform();
					g2.rotate(a.getRotation()*Math.PI/180, a.getImage().getWidth()/2 + a.getXS(), a.getImage().getHeight()/2 + a.getYS());
					g2.setComposite(AlphaComposite.SrcOver.derive(alphaTint));
					g2.drawImage((Image)a.getImage(), a.getXS(), a.getYS(), a.getSide(), a.getSide(), null);
					g2.setTransform(old);
				} else if (a.used()){
					AffineTransform old = g2.getTransform();
					g2.rotate(a.getRotation()*Math.PI/180, a.getImage().getWidth()/2 + a.getX(), a.getImage().getHeight()/2 + a.getY());
					g2.setComposite(AlphaComposite.SrcOver.derive(alphaTint));
					g2.drawImage((Image)a.getImage(), a.getX(), a.getY(), a.getSide(), a.getSide(), null);
					g2.setTransform(old);
				}
			}
		}


	}

	private Puzzle puzzle;
	// not really used
	private ArrayList<PieceComponent> allPieces;
	// reflects the contents in Puzzle.board
	private PieceComponent[][] boardPieces;
	// our own 2D array reflecting unusedPieces in Puzzle
	private PieceComponent[][] bankPieces;
	private PieceComponent selected;


	private boolean translating;
	private double transX;
	private double transY;
	private double xInterv;
	private double yInterv;
	private boolean rotatingClockwise;
	private boolean rotatingCounterClockwise;
	private int rotationCounter = 0;
	private boolean canDoSomething = true;
	private boolean congratulating = false;

	/*
	 * set the piece's clockwise rotation
	 * 
	 * Parameters:
	 * 	boolean rotating = true if the piece is rotating clockwise
	 */
	public void setRotatingClockwise (boolean rotating){
		this.rotatingClockwise = rotating;
	}

	/*
	 * set the piece's counter clockwise rotation
	 * 
	 * Parameters:
	 * 	boolean rotating = true if the piece is rotating counter clockwise
	 */
	public void setRotatingCounterClockwise (boolean rotating){
		this.rotatingCounterClockwise = rotating;
	}

	/*
	 * returns true if a piece is selected
	 */
	public boolean isSomethingSelected(){
		return selected != null;
	}

	/*
	 * returns true if the current piece isn't already performing an action (ie. rotating)
	 */
	public boolean canDoSomething(){
		return canDoSomething;
	}

	/*
	 * returns the puzzle that the user is playing with
	 */
	public Puzzle getPuzzle(){
		return this.puzzle;
	}

	/*
	 * resets the bank (unused pieces) and clears the board (used pieces)
	 */
	public void resetBankPieces(){
		for (int i = 0; i < puzzle.getHeight(); i++){
			for (int j = 0; j < puzzle.getWidth(); j++){
				bankPieces[i][j].setUsed(false);
			}
		}
		puzzle.clear();
		canDoSomething = true;
	}

	/*
	 * displays the solution to the game
	 */
	public void displaySolve(){
		canDoSomething = false;
		for (int i = 0; i < puzzle.getHeight(); i++){
			for (int j = 0; j < puzzle.getWidth(); j++){

				PieceComponent a = (PieceComponent)(puzzle.getPiece(i, j));
				bankPieces[i][j] = a;
				a.setX(j * BOARD_GRID_WIDTH + PIECE_OFFSET);
				a.setY(i * BOARD_GRID_WIDTH + PIECE_OFFSET);
				a.setXS(BANK_LEFT_SPACING + IMAGE_WIDTH * j);
				a.setYS(IMAGE_WIDTH * i);
				a.setUsed(true);//}
			}
		}

		selected = null;
		congratulate(true);
	}

	/*
	 * congratulates the user for winning by giving them a seizure
	 */
	public void congratulate(boolean something){
		this.congratulating = something;
		if (congratulating){
			JOptionPane.showMessageDialog(null, "YOU IS WIN!!!111!!11!!1!");
		}
	}
	// 48 210 48 48 210 48

	/*
	 * @Override(non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 * 
	 * when the mouse is pressed, select the piece that has been clicked
	 * 
	 * Parameters:
	 * 	MouseEvent e = the mouse event from clicking
	 */
	public void mousePressed(MouseEvent e) {
		if (canDoSomething){
			selected = null;
			int mouseY = e.getY();
			int mouseX = e.getX();
			// if statements represent hit box on screen for bank and board
			if (mouseY < BANK_BOTTOM_SPACING && BANK_LEFT_SPACING < mouseX && mouseX < BANK_RIGHT_SPACING){
				int row = mouseY / IMAGE_WIDTH;
				int col = (mouseX - BANK_LEFT_SPACING) / IMAGE_WIDTH;
				PieceComponent a = bankPieces[row][col];
				if (!a.used()){
					this.selected = a;
					this.selected.setX( col * IMAGE_WIDTH + BANK_LEFT_SPACING);
					this.selected.setY( row * IMAGE_WIDTH );
					this.selected.setUsed(true);
				}
				//}
			}
			// use this to remove piece
			else if (BOARD_LEFT_SPACING < mouseY && mouseY < BOARD_RIGHT_SPACING && BOARD_LEFT_SPACING < mouseX && mouseX < BOARD_RIGHT_SPACING){
				int row = (mouseY - BOARD_LEFT_SPACING) / BOARD_GRID_WIDTH;
				int col = (mouseX - BOARD_LEFT_SPACING) / BOARD_GRID_WIDTH;
				if (puzzle.getPiece(row, col) != null){
					selected = ((PieceComponent)puzzle.remove(row, col));
				}
			}
		}
	}

	/*
	 * @Override(non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 * 
	 * when the mouse is dragged, moved the selected piece with the mouse
	 * 
	 * Parameters:
	 * 	MouseEvent e = the mouse event from dragging
	 */
	public void mouseDragged(MouseEvent e) {
		if (canDoSomething){
			int mouseY = e.getY();
			int mouseX = e.getX();
			if (selected != null){	
				selected.setY(mouseY - IMAGE_WIDTH / 2);
				selected.setX(mouseX - IMAGE_WIDTH / 2);
			}
		}
	}

	/*
	 * @Override(non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 * 
	 * when the mouse is released, place the selected piece where the mouse is
	 * 
	 * Parameters:
	 * 	MouseEvent e = the mouse event from releasing the mouse
	 */
	public void mouseReleased(MouseEvent e) {
		if (canDoSomething){
			if (selected != null){
				int mouseY = e.getY();
				int mouseX = e.getX();
				int row = (mouseY - BOARD_LEFT_SPACING)/ BOARD_GRID_WIDTH;
				int col = (mouseX - BOARD_LEFT_SPACING) / BOARD_GRID_WIDTH;

				if (!puzzle.isOccupied(row,col) && puzzle.doesFit(row, col, selected)){
					puzzle.placePiece(row, col, selected);
					selected.setX(col * BOARD_GRID_WIDTH + PIECE_OFFSET);
					selected.setY(row * BOARD_GRID_WIDTH + PIECE_OFFSET);

					// piece stays used
					this.selected = null;
					if (puzzle.isSolved()){
						congratulate(true);
					}

				} else {		
					this.transX = selected.getX();
					this.transY = selected.getY();
					this.xInterv = (double)(selected.getXS() - selected.getX()) / 20;
					this.yInterv = (double)(selected.getYS() - selected.getY()) / 20;
					translating = true;
				}
			}
		}
		if (puzzle.isSolved() && canDoSomething){
			this.congratulate(true);
		}
	}







	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {	}
	@Override
	public void mouseMoved(MouseEvent e) {	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 * 
	 * when scroll wheel is rotated, rotate a selected piece clockwise or counterclockwise
	 * 
	 * * Parameters:
	 * 	MouseEvent e = the mouse wheel event from scroll wheel
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		//System.out.println("hello");
		if (e.getWheelRotation() == 1){
			if (selected!= null && canDoSomething){
				this.setRotatingClockwise(true);
			}
		}
		else if (e.getWheelRotation() == -1){
			if (selected!= null && canDoSomething){
				this.setRotatingCounterClockwise(true);
			}
		}
	}



}
