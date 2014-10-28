import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import data.Cell;
import data.Difficulty;
import data.Field;
import data.Flag;
import display.MainFrame;

public class App {

	private static MainFrame display;

	static {
		new Field(); // Model
		display = new MainFrame(); // View
	}

	// Controller
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				
				// Add event handlers to GUI objects
				
				display.addSizeHandler(new ActionListener() {
					// Handle grid-size change requests

					@Override
					public void actionPerformed(ActionEvent e) {
						reset();
					}
				});

				display.addDifficultyHandler(new ActionListener() {
					// Handle difficulty change requests

					@Override
					public void actionPerformed(ActionEvent e) {
						switch (e.getActionCommand()) {
						case "Easy":
							reset(Difficulty.EASY);
							break;
						case "Medium":
							reset(Difficulty.MEDIUM);
							break;
						case "Hard":
							reset(Difficulty.HARD);
							break;
						}
					}
				});

				display.addCellHandler(new MouseListener() {
					// Handle cell clicks

					@Override
					public void mouseClicked(MouseEvent e) {
						if (!Field.isPlaying()) {
							JOptionPane.showMessageDialog(display,
									"not playing");
							return;
						}

						// Reveal selected cell from x,y coordinate of mouse
						// click
						Cell selectedCell = display.gameDisplay.reveal(
								e.getX(), e.getY());

						if (selectedCell instanceof Flag) {
							// Reveal all cells
							for (Cell cell : Field.getCellList()) {
								cell.reveal();
							}
							Field.endGame();
							JOptionPane.showMessageDialog(display, "game over");
						}

						// Check if player has cleared all non-flag cells
						if (Field.isClear()) {
							Field.endGame();
							JOptionPane.showMessageDialog(display,
									"you completed the game!");
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// Do nothing
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// Do nothing
					}

					@Override
					public void mousePressed(MouseEvent e) {
						// Do nothing
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						// Do nothing
					}
				});
				
				// Display GUI
				display.setVisible(true); 
			}
		});
	}

	private static void reset(int[] difficulty) {
		// Start a new game of current size and chosen difficulty
		new Field(display.getSizeSetting(), difficulty);
		display.reset();
	}

	private static void reset() {
		// Start a new game of chosen size and current difficulty
		new Field(display.getSizeSetting(), Difficulty.getChoice());
		display.reset();
	}
}
