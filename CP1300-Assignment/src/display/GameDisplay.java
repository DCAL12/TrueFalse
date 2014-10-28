package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import data.Cell;
import data.Field;

@SuppressWarnings("serial")
public class GameDisplay extends JPanel {
	
	private static final Dimension PREFERRED_SIZE = new Dimension(600, 600);
	private static final Dimension CELL_SIZE = new Dimension();
	private static final Color TEXT_COLOR = Color.BLACK;
	private static final Color BORDER_COLOR = Color.DARK_GRAY;

	public GameDisplay() {
		setPreferredSize(PREFERRED_SIZE);
	}

	public Cell reveal(int x, int y) {
		// Reveal cell in Field based on GUI (x,y) coordinates
		x /= CELL_SIZE.width;
		y /= CELL_SIZE.height;
		Cell selectedCell = Field.getCell(x, y);
		selectedCell.reveal();
		repaint();
		return selectedCell;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dynamically re-scale cell dimensions based on GameDisplay size
		CELL_SIZE.setSize(1 + getWidth() / Field.getSize().width, 1 + getHeight() / Field.getSize().height);

		Point cellOrigin;  // Cell top-left corner
		Rectangle2D cellBounds;
		String cellSymbol;
		Rectangle2D symbolBounds; // Bounding rectangle used to center symbol text
		Point symbolOrigin;
		Color cellColor;

		// Draw the grid of cells
		for (int row = 0; row < Field.getSize().height; row++) {
			for (int column = 0; column < Field.getSize().width; column++) {
				cellOrigin = new Point((int) (column * CELL_SIZE.getWidth()),
						(int) (row * CELL_SIZE.getHeight()));
				cellBounds = new Rectangle(cellOrigin, CELL_SIZE);
				String[] contents = Field.getCell(column, row).getContents();
				// contents[0]: cell visibility
				// contents[1]: symbol (e.g. "0/0")
				// contents[2]: cell color (hex code)

				if (contents[0].equals("true")) {
					cellSymbol = contents[1];
					cellColor = Color.decode(contents[2]);
				}

				else {
					cellSymbol = "";
					cellColor = Color.decode(Cell.HIDDEN_COLOR);
				}

				g.setColor(cellColor);
				g.fillRect(cellOrigin.x, cellOrigin.y, CELL_SIZE.width,
						CELL_SIZE.height);
				
				g.setColor(TEXT_COLOR);
				symbolBounds = g.getFontMetrics().getStringBounds(cellSymbol,
						getGraphics());
				symbolOrigin = center(cellBounds, symbolBounds);
				g.drawString(cellSymbol, symbolOrigin.x, symbolOrigin.y);
				
				g.setColor(BORDER_COLOR);
				g.drawRect(column * CELL_SIZE.width, row * CELL_SIZE.height,
						CELL_SIZE.width, CELL_SIZE.height);
			}
		}
	}

	private Point center(Rectangle2D centerIn, Rectangle2D toCenter) {
		/*
		 * Return a Dimension representing the bottom-left corner of an object
		 * toCenter, centered in a bounding rectangle (centerIn)
		 */
		Point coordinates = new Point();
		coordinates.x = (int) (centerIn.getCenterX() - (toCenter.getWidth() / 2));
		coordinates.y = (int) (centerIn.getCenterY() + (toCenter.getHeight() / 2));
		return coordinates;
	}
}
