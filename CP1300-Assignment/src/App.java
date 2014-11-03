import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import data.Cell;
import data.Cell.Contents;
import data.Field;
import data.Field.Difficulty;
import display.GameDisplay;
import display.MainFrame;

public class App {

	private static MainFrame mainFrame;
	private static GameDisplay gameDisplay;

	static {
		new Field();
		gameDisplay = new GameDisplay();
		mainFrame = new MainFrame(gameDisplay); 
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				// Add event handlers to GUI objects
				mainFrame.addSizeHandler(new ActionListener() {
					// Handle grid-size change requests

					@Override
					public void actionPerformed(ActionEvent e) {
						reset();
					}
				});

				mainFrame.addDifficultyHandler(new ActionListener() {
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

				mainFrame.addCellHandler(new MouseAdapter() {
					// Handle cell clicks

					@Override
					public void mouseReleased(MouseEvent e) {
						
						if (!Field.isPlaying()) {
							// Prompt user if game not active
							JOptionPane.showMessageDialog(mainFrame,
									"not playing");
							return;
						}

						// Reveal cell from x,y coordinate of mouse click
						int column = (int) (e.getX() / (double) gameDisplay.getWidth() * Field.getSize().width);
						int row = (int) (e.getY() / (double) gameDisplay.getHeight() * Field.getSize().height);						
						Cell selectedCell = Field.getCell(column, row);
						selectedCell.reveal();

						if (selectedCell.getContents() == Contents.FALSE_FLAG) {
							// Reveal all cells
							for (Cell cell : Field.getCellList()) {
								cell.reveal();
							}
							Field.endGame();
							JOptionPane.showMessageDialog(mainFrame, "game over");
						}

						// Check if player has cleared all non-flag cells
						if (Field.isClear()) {
							Field.endGame();
							JOptionPane.showMessageDialog(mainFrame,
									"you completed the game!");
						}
						gameDisplay.repaint();
					}
				});

				// Display GUI
				mainFrame.setVisible(true);
			}
		});
	}

	private static void reset(Difficulty difficulty) {
		// Start a new game of current size and chosen difficulty
		new Field(mainFrame.getSizeSetting(), difficulty);
		mainFrame.reset();
	}

	private static void reset() {
		// Start a new game of chosen size and current difficulty
		new Field(mainFrame.getSizeSetting(), Field.getDifficulty());
		mainFrame.reset();
	}
}
