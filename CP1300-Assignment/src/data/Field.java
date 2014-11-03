package data;

import java.awt.Dimension;
import java.util.ArrayList;

import data.Cell.Contents;
import utility.Random;

public class Field {

	public static final Dimension MIN_GRID_SIZE = new Dimension(4, 4);
	public static final Dimension MAX_GRID_SIZE = new Dimension(10, 10);
	public static final Dimension DEFAULT_GRID_SIZE = MAX_GRID_SIZE;

	public enum Difficulty {
		EASY(5, 1), MEDIUM(10, 5), HARD(20, 10);

		final int MIN_TRUE_FLAGS = 1;
		final int MIN_FALSE_FLAGS = 1;
		private int percentTrueFlags;
		private int percentFalseFlags;

		Difficulty(int percentTrueFlags, int percentFalseFlags) {
			this.percentTrueFlags = percentTrueFlags;
			this.percentFalseFlags = percentFalseFlags;
		}

		int getNumTrueFlags(int totalCells) {
			return Math.max((int) (percentTrueFlags / 100.0 * totalCells),
					MIN_TRUE_FLAGS);
		}

		int getNumFalseFlags(int totalCells) {
			return Math.max((int) (percentFalseFlags / 100.0 * totalCells),
					MIN_FALSE_FLAGS);
		}
	}

	private static Dimension size;
	private static Difficulty difficulty;
	private static Cell[][] cellGrid;
	private static boolean isPlaying;
	private static int cellCount, falseFlagCount, trueFlagCount;

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
		falseFlagCount = difficulty.getNumFalseFlags(cellCount);
		trueFlagCount = difficulty.getNumTrueFlags(cellCount);
		populateGrid();
		updateAdjacentCells();
		isPlaying = true;
	}

	public static boolean isPlaying() {
		return isPlaying;
	}

	public static void endGame() {
		isPlaying = false;
	}

	public static boolean isClear() {
		return (cellCount - falseFlagCount - Cell.getRevealedCount() == 0);
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

		// Get lists of unique, random locations for flags and treasure
		ArrayList<Integer> falseFlagLocations = Random.getRandomInts(0,
				cellCount - 1, falseFlagCount, true);
		ArrayList<Integer> trueFlagLocations = Random.getRandomInts(0,
				cellCount - 1, trueFlagCount, true, falseFlagLocations);

		// Populate cells using random flag and treasure locations
		Integer cellNum = 0;
		for (int y = 0; y < size.height; y++) {
			for (int x = 0; x < size.width; x++) {

				if (falseFlagLocations.contains(cellNum)) {
					cellGrid[x][y] = new Cell(Contents.FALSE_FLAG);
				}

				else if (trueFlagLocations.contains(cellNum)) {
					cellGrid[x][y] = new Cell(Contents.TRUE_FLAG);
				}

				else {
					cellGrid[x][y] = new Cell(Contents.EMPTY);
				}
				cellNum++;
			}
		}
	}

	private void updateAdjacentCells() {
		// Update empty cells with surrounding cell list

		for (int y = 0; y < size.height; y++) {
			for (int x = 0; x < size.width; x++) {

				if (cellGrid[x][y].getContents() == Contents.EMPTY) {
					ArrayList<Cell> adjacentCells = getAdjacentCells(x, y);
					cellGrid[x][y].setAdjacentCells(adjacentCells);
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
}
