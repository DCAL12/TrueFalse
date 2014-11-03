package data;

import java.util.ArrayList;

public class Cell {

	public enum Contents {
		EMPTY("0/0", "0x77f33b"), HAS_ADJACENT("%d/%d", "0x808080"), TRUE_FLAG(
				"T", "0x0044f7"), FALSE_FLAG("F", "0xec402c");

		private String symbol, color;

		Contents(String symbol, String color) {
			this.symbol = symbol;
			this.color = color;
		}

		public String getSymbol() {
			return symbol;
		}

		public String getColor() {
			return color;
		}
	}

	private Contents contents;
	private ArrayList<Cell> adjacentCells;
	private int adjacentFalseFlags;
	private int adjacentTrueFlags;
	private boolean revealed;
	private static int revealedCount = 0;

	Cell(Contents cellType) {
		contents = cellType;
	}

	void setAdjacentCells(ArrayList<Cell> adjacentCells) {
		this.adjacentCells = adjacentCells;
		updateContents();
	}

	void updateContents() {
		for (Cell adjacentCell : adjacentCells) {
			if (adjacentCell.getContents() == Contents.TRUE_FLAG
					|| adjacentCell.getContents() == Contents.FALSE_FLAG) {
				contents = Contents.HAS_ADJACENT;
				updateAdjacentCellTypeCounts(adjacentCell.getContents());
			}
		}
	}

	void updateAdjacentCellTypeCounts(Contents cellType) {
		switch (cellType) {
		case FALSE_FLAG:
			++adjacentFalseFlags;
			break;
		case TRUE_FLAG:
			++adjacentTrueFlags;
			break;
		default:
			return;
		}
	}

	public Contents getContents() {
		return contents;
	}

	public int getAdjacentFalseFlags() {
		return adjacentFalseFlags;
	}

	public int getAdjacentTrueFlags() {
		return adjacentTrueFlags;
	}

	public boolean isRevealed() {
		return revealed;
	}

	static int getRevealedCount() {
		return revealedCount;
	}

	static void resetRevealedCount() {
		revealedCount = 0;
	}

	public void reveal() {
		if (revealed)
			return;
		else {
			revealed = true;
			revealedCount++;

			if (contents == Contents.EMPTY) {
				// Recursively reveal all touching EMPTY cells
				for (Cell adjacentCell : adjacentCells) {

					if (adjacentCell.contents == Contents.EMPTY) {
						adjacentCell.reveal();
					}
				}
			}
		}

	}
}
