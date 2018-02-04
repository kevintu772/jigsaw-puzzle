import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/*
 * This class represents the controls used to play the jigsaw puzzle game.
 * The class contains buttons that allow for rotation of pieces,
 * restarting the game, and solving the game.
 */
public class ControlsComponent{

	private PuzzleGame game;
	/*
	 * constructs the ControlsComponent using a game for it to control
	 * 
	 * Parameters:
	 * 	PuzzleGame game = the game for these controls to work upon
	 */
	public ControlsComponent(PuzzleGame game){
		this.game = game;
	}

	/*
	 * adds the various buttons for controlling the game to a JPanel
	 * returns that JPanel of controls
	 */
	public JPanel addControls() {
		JPanel a = new JPanel();
		
		// purpose: to add text to the JButton panel (no ActionListener)
		JButton rotateLabel = new JButton("Rotate : : : ");
		rotateLabel.setBorder(null);
		rotateLabel. setBorderPainted(false);
		rotateLabel.setContentAreaFilled(false);
		
		// left rotate button
		JButton rotateLeft = new JButton("<");
		rotateLeft.setContentAreaFilled(false);

		rotateLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game.isSomethingSelected() && game.canDoSomething()){
					game.setRotatingCounterClockwise(true);
				}
			} 
		});
		
		// right rotate button
		JButton rotateRight = new JButton(">");
		rotateRight.setContentAreaFilled(false);
		rotateRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game.isSomethingSelected() && game.canDoSomething()){
					game.setRotatingClockwise(true);
				}
			} 
		});
		
		// restart button
		JButton restart = new JButton("Restart");
		restart.setContentAreaFilled(false);
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//game.getPuzzle().clear();
				game.congratulate(false);
				game.resetBankPieces();
				
			} 
		});
		
		// quit button
		JButton quit = new JButton("Quit");
		quit.setContentAreaFilled(false);
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		// solve button
		JButton solve = new JButton("Solve");
		solve.setContentAreaFilled(false);
		solve.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game.canDoSomething()){
					game.getPuzzle().clear();
					game.getPuzzle().solve();
					game.displaySolve();
				}

			}
		});

		a.add(rotateLabel); a.add(rotateLeft); a.add(rotateRight); a.add(restart); a.add(quit); a.add(solve);
		return a;
	}
}