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
import data.Field.Difficulty;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	private static final JMenuBar menuBar = new JMenuBar();
	private static final SettingsMenu settingsMenu = new SettingsMenu();
	private static GameDisplay gameDisplay;
	

	public MainFrame(GameDisplay gameDisplay) {
		// General setup
		super("The True/False Game");
		MainFrame.gameDisplay = gameDisplay;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(500, 500));
		setLocationRelativeTo(null);

		// Add frame components
		menuBar.add(settingsMenu);
		add(SettingsMenu.settingsLabel);
		setJMenuBar(menuBar);
		add(gameDisplay);
		
		// Final setup
		pack();
		setLocationRelativeTo(null);
	}
	
	static class SettingsMenu extends JMenu {
		
		final static JLabel settingsLabel = new JLabel("Settings");
		final static SizeMenu sizeMenu = new SizeMenu();
		final static DifficultyMenu difficultyMenu = new DifficultyMenu();
		
		SettingsMenu() {
			super("Settings");
			add(sizeMenu);
			add(difficultyMenu);
		}
		
		static class SizeMenu extends JMenuItem {
			final static SizeDialog sizeDialog = new SizeDialog();
			
			SizeMenu() {
				super("Size");
				addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// Display the sizeDialog on sizeMenu click
						sizeDialog.pack();
						sizeDialog.setVisible(true);
					}
				});
			}
		}
		
		static class DifficultyMenu extends JMenu {
			final static ButtonGroup difficultyChoicesGroup = new ButtonGroup();
			final static JCheckBoxMenuItem[] difficultyChoices = new JCheckBoxMenuItem[Difficulty.values().length];
			
			DifficultyMenu() {
				super("Difficulty");
				for (int i = 0; i < Difficulty.values().length; i++) {
					difficultyChoices[i] = new JCheckBoxMenuItem(Difficulty.values()[i].toString().toLowerCase());
					difficultyChoicesGroup.add(difficultyChoices[i]);
					if (difficultyChoices[i].getActionCommand().equals(Field.getDifficulty().toString().toLowerCase())) {
						difficultyChoices[i].setSelected(true);
					}
					add(difficultyChoices[i]);
				}
			}
		}
	}

	public void addSizeHandler(ActionListener listener) {
		// Pass "okay" actionListener to sizeDialog
		SettingsMenu.SizeMenu.sizeDialog.addHandler(listener);
	}

	public void addDifficultyHandler(ActionListener listener) {
		// Listen for difficulty change requests
		for (JCheckBoxMenuItem item : SettingsMenu.DifficultyMenu.difficultyChoices) {
			item.addActionListener(listener);
		}
	}

	public void addCellHandler(MouseAdapter mouseAdapter) {
		// Pass click actionListener to gameDisplay
		gameDisplay.addMouseListener(mouseAdapter);
	}

	public Dimension getSizeSetting() {
		return SettingsMenu.SizeMenu.sizeDialog.getSettings();
	}
}
