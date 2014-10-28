package data;

import java.awt.Dimension;
import java.util.ArrayList;

import utility.Random;

public class Field {
	
	// Grid Size Restrictions
	public static final Dimension MIN_GRID_SIZE = new Dimension(4, 4);
	public static final Dimension MAX_GRID_SIZE = new Dimension(10, 10);
	public static final Dimension DEFAULT_GRID_SIZE = MAX_GRID_SIZE;

	private static Dimension size;
	private static Difficulty difficulty;
	private static int cellCount;
	private static Cell[][] cellGrid;
	private static boolean isPlaying;

	public Field() {
		size = DEFAULT_GRID_SIZE;
		difficulty = new Difficulty(Difficulty.EASY);
		cellCount = size.width * size.height;
		cellGrid = new Cell[size.width][size.height];
		populateGrid();
		isPlaying = true;
	}
	
	public Field(Dimension size, int[] difficulty) {
		Field.size = size;
		Field.difficulty = new Difficulty(difficulty);
		cellCount = size.width * size.height;
		cellGrid = new Cell[size.width][size.height];
		populateGrid();
		isPlaying = true;
	}

	public static boolean isPlaying() {
		return isPlaying;
	}
	
	public static void endGame() {
		isPlaying = false;
	}
	
	public static boolean isClear() {
		return (cellCount - Flag.getCount() - Cell.getRevealedCount() == 0);
	}
	
	public static Dimension getSize() {
		return size;
	}

	public static Difficulty getDifficulty() {
		return difficulty;
	}

	public static Cell getCell(int x, int y) {
		return cellGrid[x][y];
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
	
	private void populateGrid() {
		// Fill the grid with flags and treasure according to difficulty
		
		// Reset Cell counters
		Cell.resetRevealedCount();
		Flag.resetCount();

		// Get lists of unique, random locations for flags and treasure
		ArrayList<Integer> flagLocations = Random.getRandomInts(0, cellCount - 1,
				difficulty.getNumFlags(cellCount), true);
		ArrayList<Integer> treasureLocations = Random.getRandomInts(0, cellCount - 1,
				difficulty.getNumTreasure(cellCount), true, flagLocations);

		// Populate cells using random flag and treasure locations
		Integer cellNum = 0;
		for (int y = 0; y < size.height; y++) {
			for (int x = 0; x < size.width; x++) {
				if (flagLocations.contains(cellNum)) {
					cellGrid[x][y] = new Flag();
				}
				
				else if (treasureLocations.contains(cellNum)) {
					cellGrid[x][y] = new Treasure();
				}
				
				else {
					cellGrid[x][y] = new Cell();
				}
				cellNum++;
			}
		}

		// Update empty cells with surrounding cell information
		for (int y = 0; y < size.height; y++) {
			for (int x = 0; x < size.width; x++) {
				// Only empty cells need to be updated
				if (cellGrid[x][y].getClass() == Cell.class) {
					ArrayList<Cell> adjacentCells = getAdjacentCells(x, y);
					cellGrid[x][y].setAdjacentCells(adjacentCells);
					for (Cell adjacentCell : adjacentCells) {
						if (adjacentCell instanceof Flag) {
							cellGrid[x][y].addAdjacentFlag();
							continue;
						}
						if (adjacentCell instanceof Treasure) {
							cellGrid[x][y].addAdjacentTreasure();
							continue;
						}
					}
				}
			}
		}
	}

	private ArrayList<Cell> getAdjacentCells(int x, int y) {
		// Helper method finds and returns list of surrounding cells

		// Relative positions {col, row} for all possible (8) surrounding cells
		final int[][] relativeCellPositions = { { -1, -1 }, { 0, -1 },
				{ 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 } };

		ArrayList<Cell> adjacentCells = new ArrayList<>();
		for (int[] move : relativeCellPositions) {
			int checkX = x + move[0]; // Hypothetical column
			int checkY = y + move[1]; // Hypothetical row

			try {

				adjacentCells.add(cellGrid[checkX][checkY]);

			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}
		}
		return adjacentCells;
	}
}
