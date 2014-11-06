package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import data.Cell;
import data.Cell.Contents;
import data.Field;

@SuppressWarnings("serial")
public class GameDisplay extends JPanel {
	
	private static final Dimension CELL_SIZE = new Dimension();
	private Graphics graphics;
	
	public Dimension getCellSize() {
		return CELL_SIZE;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		updateCellSize();
		graphics = g;
		// Draw the grid of cells
		for (int row = 0; row < Field.getSize().height; row++) {
			for (int column = 0; column < Field.getSize().width; column++) {
				Point coordinate = new Point(column, row);
				CellSketch cell = new CellSketch(coordinate);
				cell.draw();
			}
		}
	}

	private void updateCellSize() {
		// Dynamically re-scale cell dimensions based on GameDisplay size
		CELL_SIZE.setSize(1 + getWidth() / Field.getSize().width, 1
				+ getHeight() / Field.getSize().height);
	}

	class CellSketch {
		final Color TEXT_COLOR = Color.BLACK;
		final Color BORDER_COLOR = Color.DARK_GRAY;
		final Color HIDDEN_CELL_COLOR = Color.decode("0xeeeeee");
		Cell cell;
		Point origin;
		Contents contents;

		CellSketch(Point coordinate) {
			cell = Field.getCell(coordinate);
			origin = new Point((int) (coordinate.x * CELL_SIZE.getWidth()),
								(int) (coordinate.y * CELL_SIZE.getHeight()));
			contents = cell.getContents();
		}

		void draw() {
			if (contents != null) {
				// Display cell contents
				graphics.setColor(contents.getBackgroundColor());
				graphics.fillRect(origin.x, origin.y, 
					CELL_SIZE.width, CELL_SIZE.height);
				graphics.setColor(TEXT_COLOR);
				Point textOrigin = originToCenterText();
				graphics.drawString(contents.getSymbol(), textOrigin.x, textOrigin.y);

			} else {
				// Display hidden cell
				graphics.setColor(HIDDEN_CELL_COLOR);
				graphics.fillRect(origin.x, origin.y, 
					CELL_SIZE.width, CELL_SIZE.height);
			}
			
			graphics.setColor(BORDER_COLOR);
			graphics.drawRect(origin.x, origin.y, 
					CELL_SIZE.width, CELL_SIZE.height);
		}
		
		private Point originToCenterText() {
			/*
			 * Return an origin point (x,y) representing the bottom-left
			 * corner of a rectangle toCenter, centered in a bounding
			 * rectangle (centerIn)
			 */
			Rectangle2D cellBounds = new Rectangle(origin, CELL_SIZE);
			Rectangle2D textBounds = graphics.getFontMetrics().
					getStringBounds(contents.getSymbol(), getGraphics());
			
			Point originCoordinate = new Point();
			originCoordinate.x = (int) (cellBounds.getCenterX() 
										- (textBounds.getWidth() / 2));
			originCoordinate.y = (int) (cellBounds.getCenterY() 
										+ (textBounds.getHeight() / 2));
			return originCoordinate;
		}
	}

}
