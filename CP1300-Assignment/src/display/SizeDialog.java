package display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import data.Field;
import display.SizeDialog.Control;

@SuppressWarnings("serial")
public class SizeDialog extends JDialog {

	private static ControlPanel rows;
	private static ControlPanel columns;
	private static JButton okay;
	
	enum Control {
		ROWS(Field.MIN_GRID_SIZE.height, Field.MAX_GRID_SIZE.height, Field.DEFAULT_GRID_SIZE.height), 
		COLUMNS(Field.MIN_GRID_SIZE.width, Field.MAX_GRID_SIZE.width, Field.DEFAULT_GRID_SIZE.width);
		
		private int minimum;
		private int maximum;
		private int initialValue;
		
		Control(int min, int max, int value) {
			minimum = min;
			maximum = max;
			initialValue = value;
		}
		
		int getMinimum() {
			return minimum;
		}
		
		int getMaximum() {
			return maximum;
		}
		
		int getInitialValue() {
			return initialValue;
		}
	}
	
	static {
		rows = new ControlPanel(Control.ROWS);
		columns = new ControlPanel(Control.COLUMNS);
		okay = new JButton("okay");
	}
	
	public SizeDialog() {
		setModal(true);
		setResizable(false);
		
		add(rows, BorderLayout.NORTH);
		add(columns, BorderLayout.CENTER);
		add(okay, BorderLayout.SOUTH);
		
		okay.addActionListener(new ActionListener() {
			// Hide size dialog when "okay" is pressed
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		pack();
		setLocationRelativeTo(null);
	}

	public Dimension getSettings() {
		return new Dimension(columns.getSetting(), rows.getSetting());
	}

	public void addHandler(ActionListener listener) {
		// Handle "okay" button press
		okay.addActionListener(listener);
	}
}

@SuppressWarnings("serial")
class ControlPanel extends JPanel{

	private JLabel label;
	private JSlider slideControl;

	ControlPanel(Control control) {		
		super(new BorderLayout());
		slideControl = new JSlider(control.getMinimum(), control.getMaximum(), control.getInitialValue());
		slideControl.setName(control.toString().toLowerCase());
		slideControl.addChangeListener(new ChangeListener() {
			// Handle changing slideControl position
			@Override
			public void stateChanged(ChangeEvent moveSlider) {
				label.setText(getLabelText());
			}
		});
		
		label = new JLabel(getLabelText());
		
		setBorder(BorderFactory.createLineBorder(getBackground(), 10));
		add(label, BorderLayout.WEST);
		add(slideControl, BorderLayout.EAST);
	}
	
	private String getLabelText() {
		return slideControl.getName() + ": " 
				+ slideControl.getValue();
	}
	
	int getSetting() {
		return slideControl.getValue();
	}
}
