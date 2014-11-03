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

@SuppressWarnings("serial")
public class SizeDialog extends JDialog {

	private static JButton okay;
	private static Dimension settings;
	private static JLabel rowLabel;
	private static JSlider rowSlider;
	private static JLabel columnLabel;
	private static JSlider columnSlider;

	public SizeDialog() {
		// General setup
		setModal(true);
		settings = Field.DEFAULT_GRID_SIZE;
		setResizable(false);

		// Construct rows selector
		rowLabel = new JLabel("rows: " + settings.height);
		rowSlider = new JSlider(Field.MIN_GRID_SIZE.height,
				Field.MAX_GRID_SIZE.height);
		rowSlider.setValue(settings.height);
		rowSlider.addChangeListener(new ChangeListener() {
			// Update label and settings as slider moves
			@Override
			public void stateChanged(ChangeEvent moveSlider) {
				settings.height = rowSlider.getValue();
				rowLabel.setText("rows: " + settings.height);
			}
		});

		// Construct columns selector
		columnLabel = new JLabel("columns: " + settings.width);
		columnSlider = new JSlider(Field.MIN_GRID_SIZE.width,
				Field.MAX_GRID_SIZE.width);
		columnSlider.setValue(settings.width);
		columnSlider.addChangeListener(new ChangeListener() {
			// Update label and settings as slider moves
			@Override
			public void stateChanged(ChangeEvent moveSlider) {
				settings.width = columnSlider.getValue();
				columnLabel.setText("columns: " + settings.width);
			}
		});
		
		okay = new JButton("okay");
		
		// Configure layout panels
		JPanel rows = new JPanel(new BorderLayout());
		rows.setBorder(BorderFactory.createLineBorder(rows.getBackground(), 10));
		JPanel columns = new JPanel(new BorderLayout());
		columns.setBorder(BorderFactory.createLineBorder(columns.getBackground(), 10));

		// Add components to sizeDialog
		rows.add(rowLabel, BorderLayout.WEST);
		rows.add(rowSlider, BorderLayout.EAST);
		columns.add(columnLabel, BorderLayout.WEST);
		columns.add(columnSlider, BorderLayout.EAST);
		add(rows, BorderLayout.NORTH);
		add(columns, BorderLayout.CENTER);
		add(okay, BorderLayout.SOUTH);
		
		// Final setup
		pack();
		setLocationRelativeTo(null);
	}

	public Dimension getSettings() {
		return settings;
	}

	public void addSizeHandler(ActionListener listener) {
		// Handle "okay" button press
		okay.addActionListener(listener); // Listener for controller

		okay.addActionListener(new ActionListener() {
			// Hide size dialog when "okay" is pressed
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
	}
}
