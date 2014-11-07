package data;

import java.awt.Color;
import java.util.ArrayList;

public class Cell {
	
	private static int revealedCount = 0;
	
	private CellType cellType;
	private Contents contents;
	private ArrayList<Behavior> behaviors;
	private boolean revealed;
	private ArrayList<Cell> adjacentCells;
	
	Cell(CellType cellType) {
		this.cellType = cellType;
		contents = new Contents();
		behaviors = new ArrayList<>();
		behaviors.addAll(cellType.defaultBehaviors);
	}
	
	public enum Behavior {
		COUNTED, COUNT_ADJACENT, AUTO_REVEALED_BY_ADJACENT, KILL_ON_REVEAL;
	}
	
	public enum CellType { 
		EMPTY("0x77f33b", "0/0", 0, 
				Behavior.COUNT_ADJACENT, 
				Behavior.AUTO_REVEALED_BY_ADJACENT),
		FALSE_FLAG("0xec402c", "F", 1,
				Behavior.COUNTED,
				Behavior.KILL_ON_REVEAL),
		TRUE_FLAG("0x0044f7", "T", 1,
				Behavior.COUNTED);

		static final CellType DEFAULT = EMPTY;
		private final ArrayList<Behavior> defaultBehaviors = new ArrayList<>();
		private Color backgroundColor;
		private String symbol;
		private int minimumCount;
		
		CellType(String color, 
				String symbol,
				int minimumCount,
				Behavior ... behaviors) {
			
			backgroundColor = Color.decode(color);
			this.symbol = symbol;
			this.minimumCount = minimumCount;
			for (Behavior behavior : behaviors) {
				defaultBehaviors.add(behavior);
			}
		}
		
		static ArrayList<CellType> getMandatoryCellTypes() {
			ArrayList<CellType> mandatoryCellTypes = new ArrayList<>();
			for (CellType cellType : CellType.values()) {
				if (cellType.getMinimumCount() > 0) {
					mandatoryCellTypes.add(cellType);
				}
			}
			return mandatoryCellTypes;
		}
		
		static ArrayList<CellType> getcellTypesToCount() {
			ArrayList<CellType> countedCellTypes = new ArrayList<>();
			for (CellType cellType : CellType.values()) {
				if (cellType.defaultBehaviors.contains(Behavior.COUNTED)) {
					countedCellTypes.add(cellType);
				}
			}
			return countedCellTypes;
		}
		
		int getMinimumCount() {
			return minimumCount;
		}
	}
	
	
	
	static int getRevealedCount() {
		return revealedCount;
	}

	static void resetRevealedCount() {
		revealedCount = 0;
	}
	
	boolean isType(CellType cellType) {
		return (this.cellType == cellType);
	}
	
	void addAdjacentCells(ArrayList<Cell> adjacentCells) {
		this.adjacentCells = adjacentCells;
		contents.update();
	}
	
	public boolean hasBehavior(Behavior behavior) {
		return behaviors.contains(behavior);
	}
	
	public Contents getContents() {
		if (revealed) {
			return contents;
		}
		return null;
	}
	
	public class Contents {
		private Color backgroundColor;
		private String symbol;
		
		private Contents() {
			backgroundColor = cellType.backgroundColor;
			symbol = cellType.symbol;
		}
		
		public Color getBackgroundColor() {
			return backgroundColor;
		}
		
		public String getSymbol() {
			return symbol;
		}
		
		private void update() {
			// Set cell symbol to display countable adjacent cells by type
			
			StringBuilder stringBuilder = new StringBuilder();
			String seperator = "/";
			for (CellType countableCellType : CellType.getcellTypesToCount()) {
				stringBuilder.append(countAdjacentCellsByType(countableCellType) + seperator); 	
			}
			stringBuilder.deleteCharAt(
					stringBuilder.lastIndexOf(seperator)); // Trailing separator not necessary
			
			if (!stringBuilder.toString().equals(CellType.EMPTY.symbol)) {
				backgroundColor = Color.decode("0x808080");
				symbol = stringBuilder.toString();
				behaviors.remove(Behavior.AUTO_REVEALED_BY_ADJACENT);
			}
		}
		
		private int countAdjacentCellsByType(CellType type) {
			int count = 0;
			for (Cell adjacentCell : adjacentCells) {
				if (adjacentCell.isType(type)) {
					count++;
				}
			}
			return count;
		}
	}

	public void reveal() {
		if (revealed)
			return;
		else {
			revealed = true;
			revealedCount++;

			if (behaviors.contains(Behavior.AUTO_REVEALED_BY_ADJACENT)) {
				// Recursively reveal all touching auto-revealing cells
				for (Cell adjacentCell : adjacentCells) {

					if (adjacentCell.behaviors.contains(Behavior.AUTO_REVEALED_BY_ADJACENT)) {
						adjacentCell.reveal();
					}
				}
			}
		}
	}
}
