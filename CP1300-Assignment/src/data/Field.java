package data;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import data.Cell.CellType;

import java.util.Random;

public class Field {

	public static final Dimension MIN_GRID_SIZE = new Dimension(4, 4);
	public static final Dimension MAX_GRID_SIZE = new Dimension(10, 10);
	public static final Dimension DEFAULT_GRID_SIZE = MAX_GRID_SIZE;

	public enum Difficulty {
		EASY(1, 5), MEDIUM(5, 10), HARD(10, 20);
		
		private final HashMap<CellType, Integer> percentCellsByType = new HashMap<>();

		Difficulty(int ... cellPercentages) {
			ArrayList<CellType> mandatoryCellTypes = CellType.getMandatoryCellTypes();
			for (int i = 0; i < mandatoryCellTypes.size(); i++) {
				percentCellsByType.put(mandatoryCellTypes.get(i), cellPercentages[i]);
			}
		}

		int getCellCountByType(CellType cellType) {
			// Max function ensures minimum cell count for mandatory cell types
			return Math.max((int) (percentCellsByType.get(cellType) / 100.0 * cellCount),
					cellType.getMinimumCount());
		}
	}

	private static Dimension size;
	private static Difficulty difficulty;
	private static Cell[][] cellGrid;
	private static boolean isPlaying;
	private static int cellCount;

	public Field() {
		size = DEFAULT_GRID_SIZE;
		difficulty = Difficulty.EASY;
		initializeGrid();
	}

	public Field(Dimension size, Difficulty difficulty) {
		Field.size = size;
		Field.difficulty = difficulty;
		initializeGrid();
	}

	private void initializeGrid() {
		cellGrid = new Cell[size.width][size.height];
		cellCount = size.width * size.height;
		Cell.resetRevealedCount();
		populateGrid();
		updateAdjacentCells();
		isPlaying = true;
	}
	
	private void populateGrid() {
		ArrayList<CellType> mandatoryCellTypes = CellType.getMandatoryCellTypes();
		Random random = new Random();
		// Place required number of each cell type in random, null cells
		for (CellType cellType : mandatoryCellTypes) {
			for (int i = 0; i < difficulty.getCellCountByType(cellType);) {
				
				int randomColumn = random.nextInt(size.width);
				int randomRow = random.nextInt(size.height);
				
				if (cellGrid[randomColumn][randomRow] == null) {
					cellGrid[randomColumn][randomRow] = new Cell(cellType);
					i++;
				}
			}
		}

		// Remaining cells will be of the default type
		for (int y = 0; y < size.height; y++) {
			for (int x = 0; x < size.width; x++) {
				if (cellGrid[x][y] == null) {
					cellGrid[x][y] = new Cell(CellType.DEFAULT);
				}
			}
		}
	}

	private void updateAdjacentCells() {
		for (int y = 0; y < size.height; y++) {
			for (int x = 0; x < size.width; x++) {
				if (cellGrid[x][y].isType(CellType.EMPTY)) {
					ArrayList<Cell> adjacentCells = getAdjacentCells(x, y);
					cellGrid[x][y].addAdjacentCells(adjacentCells);
				}
			}
		}
	}

	private ArrayList<Cell> getAdjacentCells(int x, int y) {
		// Find surrounding cells

		// Relative positions {col, row} for all possible (8) surrounding cells
		final int[][] relativeCellPositions = { { -1, -1 }, { 0, -1 },
				{ 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 } };

		// Add each surrounding cell (if it exists) to adjacentCells list
		ArrayList<Cell> adjacentCells = new ArrayList<>();
		for (int[] move : relativeCellPositions) {
			int checkX = x + move[0];
			int checkY = y + move[1];

			try {

				adjacentCells.add(cellGrid[checkX][checkY]);

			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}
		}
		return adjacentCells;
	}

	public static boolean isPlaying() {
		return isPlaying;
	}
	
	public static Dimension getSize() {
		return size;
	}

	public static Difficulty getDifficulty() {
		return difficulty;
	}

	public static Cell getCell(Point coordinate) {
		return cellGrid[coordinate.x][coordinate.y];
	}

	public static ArrayList<Cell> getCellList() {
		// Get list of all cells in grid for convenient access
		ArrayList<Cell> cellList = new ArrayList<>();
		for (int y = 0; y < size.height; y++) {
			for (int x = 0; x < size.width; x++) {
				cellList.add(cellGrid[x][y]);
			}
		}
		return cellList;
	}

	public static boolean isClear() {
		return (cellCount 
				- difficulty.getCellCountByType(CellType.FALSE_FLAG) 
				- Cell.getRevealedCount() == 0);
	}
	
	public static void endGame() {
		isPlaying = false;
	}
}
