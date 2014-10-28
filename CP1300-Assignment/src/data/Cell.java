package data;

import java.util.ArrayList;

public class Cell {

	public static final String HIDDEN_COLOR = "0xeeeeee";
	static final String DEFAULT_COLOR = "0x77f33b";
	static final String ADJACENT_OBJECT_COLOR = "0x808080";
	static final String EMPTY_CELL_SYMBOL = "%d/%d";

	private static int revealedCount;
	
	protected String symbol;
	protected String revealedColor;
	
	private ArrayList<Cell> adjacentCells;
	private int adjacentFlags;
	private int adjacentTreasure;
	private boolean revealed;
	
	public Cell() {
		symbol = String.format(EMPTY_CELL_SYMBOL, adjacentFlags, adjacentTreasure);
		revealedColor = DEFAULT_COLOR;
	}

	public static int getRevealedCount() {
		return revealedCount;
	}

	public static void resetRevealedCount() {
		revealedCount = 0;
	}
	
	public final void setAdjacentCells(ArrayList<Cell> adjacentCells) {
		this.adjacentCells = adjacentCells;
	}
	
	public final void addAdjacentFlag() {
		// Update symbol to indicate adjacent flag cell
		symbol = String.format(EMPTY_CELL_SYMBOL, ++adjacentFlags, adjacentTreasure);
		revealedColor = ADJACENT_OBJECT_COLOR;
	}
	
	public final void addAdjacentTreasure() {
		// Update symbol to indicate adjacent treasure cell
		symbol = String.format(EMPTY_CELL_SYMBOL, adjacentFlags, ++adjacentTreasure);
		revealedColor = ADJACENT_OBJECT_COLOR;
	}

	public void reveal() {
		if (revealed)
			return;
		revealed = true;
		revealedCount++;
		
		if (symbol.equals("0/0")) {
			// Recursively reveal all touching "0/0" cells
			for (Cell adjacentCell : adjacentCells) {

				if (adjacentCell.symbol.equals("0/0")) {
					adjacentCell.reveal();
				}
			}
		}
	}

	public String[] getContents() {
		String[] contents = { String.valueOf(revealed), symbol, revealedColor };
		return contents;
	}
}
