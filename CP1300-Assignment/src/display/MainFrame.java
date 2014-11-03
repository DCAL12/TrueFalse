package display;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import data.Field;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	private static GameDisplay gameDisplay;
	private static SizeDialog sizeDialog;
	private static JCheckBoxMenuItem[] difficultyChoices;

	public MainFrame(GameDisplay gameDisplay) {
		// General setup
		super("The True/False Game");
		MainFrame.gameDisplay = gameDisplay;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(500, 500));
		setLocationRelativeTo(null);

		// Configure settings menu
		JMenuBar menuBar = new JMenuBar();
		JLabel settingsLabel = new JLabel("Settings");
		JMenu settingsMenu = new JMenu("Settings");

		// Configure size menu
		sizeDialog = new SizeDialog(this);
		JMenuItem sizeMenu = new JMenuItem("Size");
		sizeMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Display the sizeDialog on sizeMenu click
				sizeDialog.pack();
				sizeDialog.setVisible(true);
			}
		});
		
		// Configure difficulty options
		difficultyChoices = new JCheckBoxMenuItem[3];
		difficultyChoices[0] = new JCheckBoxMenuItem("Easy", true);
		difficultyChoices[1] = new JCheckBoxMenuItem("Medium"); 
		difficultyChoices[2] = new JCheckBoxMenuItem("Hard");

		// Configure difficulty menu
		JMenu difficultyMenu = new JMenu("Difficulty");
		ButtonGroup difficultyChoicesGroup = new ButtonGroup();
		for (JCheckBoxMenuItem item : difficultyChoices) {
			difficultyChoicesGroup.add(item);
			difficultyMenu.add(item);
		}

		// Assemble entire settings menu
		settingsMenu.add(sizeMenu);
		settingsMenu.add(difficultyMenu);
		menuBar.add(settingsMenu);
		add(settingsLabel);
		setJMenuBar(menuBar);

		// Final setup
		add(gameDisplay);
		pack();
		setLocationRelativeTo(null);
	}

	public void addSizeHandler(ActionListener listener) {
		// Pass "okay" actionListener to sizeDialog
		sizeDialog.addSizeHandler(listener);
	}

	public void addDifficultyHandler(ActionListener listener) {
		// Listen for difficulty change requests
		for (JCheckBoxMenuItem item : difficultyChoices) {
			item.addActionListener(listener);
		}
	}

	public void addCellHandler(MouseAdapter mouseAdapter) {
		// Pass click actionListener to gameDisplay
		gameDisplay.addMouseListener(mouseAdapter);
	}

	public Dimension getSizeSetting() {
		// Get sizeDialog's current settings
		return sizeDialog.getSettings();
	}

	public void reset() {
		setVisible(false);

		// Set difficulty check boxes to current field's difficulty
		for (JCheckBoxMenuItem item : difficultyChoices) {
			if (item.getActionCommand()
					.equals(Field.getDifficulty())) {
				item.setSelected(true);
			} else {
				item.setSelected(false);
			}
		}
		setVisible(true);
	}
}
