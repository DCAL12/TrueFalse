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

	class CellSketch {

		class CellText {
			final Color TEXT_COLOR = Color.BLACK;
			String text;
			Graphics graphics;
			Point origin;

			CellText(String text, Rectangle2D bounds, Graphics graphics) {
				this.text = text;
				this.graphics = graphics;
				origin = originToCenterText(bounds);
			}

			void draw() {
				graphics.setColor(TEXT_COLOR);
				graphics.drawString(text, origin.x, origin.y);
			}

			private Point originToCenterText(Rectangle2D centerIn) {
				/*
				 * Return an origin point (x,y) representing the bottom-left
				 * corner of a rectangle toCenter, centered in a bounding
				 * rectangle (centerIn)
				 */
				Rectangle2D textBounds = graphics.getFontMetrics()
						.getStringBounds(text, getGraphics());
				Point originCoordinate = new Point();
				originCoordinate.x = (int) (centerIn.getCenterX() - (textBounds
						.getWidth() / 2));
				originCoordinate.y = (int) (centerIn.getCenterY() + (textBounds
						.getHeight() / 2));
				return originCoordinate;
			}
		}

		final Color BORDER_COLOR = Color.DARK_GRAY;
		final Color HIDDEN_CELL_COLOR = Color.decode("0xeeeeee");
		Cell cell;
		Point origin;
		CellText cellText;
		Graphics graphics;

		public CellSketch(Cell cell, int column, int row, Graphics graphics) {
			this.cell = cell;
			origin = new Point((int) (column * CELL_SIZE.getWidth()),
					(int) (row * CELL_SIZE.getHeight()));
			this.graphics = graphics;
			String symbol = cell.getContents() == Contents.HAS_ADJACENT ? String
					.format(cell.getContents().getSymbol(),
							cell.getAdjacentFalseFlags(),
							cell.getAdjacentTrueFlags()) : cell.getContents()
					.getSymbol();
			cellText = new CellText(symbol, new Rectangle(origin, CELL_SIZE),
					graphics);
		}

		void draw() {
			if (cell.isRevealed()) {
				// Display cell contents
				graphics.setColor(Color.decode(cell.getContents().getColor()));
				graphics.fillRect(origin.x, origin.y, CELL_SIZE.width,
						CELL_SIZE.height);
				cellText.draw();

			} else {
				// Display hidden cell
				graphics.setColor(HIDDEN_CELL_COLOR);
				graphics.fillRect(origin.x, origin.y, CELL_SIZE.width,
						CELL_SIZE.height);
			}
			// Draw border
			graphics.setColor(BORDER_COLOR);
			graphics.drawRect(origin.x, origin.y, CELL_SIZE.width,
					CELL_SIZE.height);
		}
	}

	private static final Dimension CELL_SIZE = new Dimension();

	public Dimension getCellSize() {
		return CELL_SIZE;
	}

	private void updateCellSize() {
		// Dynamically re-scale cell dimensions based on GameDisplay size
		CELL_SIZE.setSize(1 + getWidth() / Field.getSize().width, 1
				+ getHeight() / Field.getSize().height);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		updateCellSize();

		// Draw the grid of cells
		for (int row = 0; row < Field.getSize().height; row++) {
			for (int column = 0; column < Field.getSize().width; column++) {

				CellSketch cell = new CellSketch(Field.getCell(column, row),
						column, row, g);
				cell.draw();

			}
		}
	}

}
