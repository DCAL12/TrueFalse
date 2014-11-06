package data;

import java.awt.Point;
import java.util.ArrayList;

import data.Cell.CellType;

public class TestField {

	public static void main(String[] args) {
		// Test creating new cells of each type
		printTest("Create new empty cell\n", "true");
		Cell emptyCell = new Cell(CellType.EMPTY);
		System.out.println(emptyCell.isType(CellType.EMPTY));
		
		printTest("Create new False-Flag cell\n", "true");
		Cell falseFlagCell = new Cell(CellType.FALSE_FLAG);
		System.out.println(falseFlagCell.isType(CellType.FALSE_FLAG));
		
		printTest("Create new True-Flag cell\n", "true");
		Cell trueFlagCell = new Cell(CellType.TRUE_FLAG);
		System.out.println(trueFlagCell.isType(CellType.TRUE_FLAG));
		
		// Test the default field (Default size: 10x10, Difficulty: Easy (5% Flags, 1% Treasure)
		printTest("Grid size after creating new, default Field", "10x10");
		new Field();
		System.out.println(Field.getSize().width + "x" + Field.getSize().height);

		printTest("Display concealed grid", 
				  "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n");
		drawConcealedField();

		printTest("Display revealed grid", "Random distribution of: \n "
				+ "5x 'F' cell\n " + "1x 'T' cell\n "
				+ "95x 'F/T' cells where:\n"
				+ "\tF = # of adjacent flag cells\n"
				+ "\tT = # of adjacent treasure cells");
		drawRevealedField();

		printTest("Select (reveal) cells",
				"Grid will be concealed except for 5 randomly revealed cells "
				+ "and any '0/0' cells adjacent to a revealed cell");
		
		System.out.print("\tRevealed Cells: ");
		ArrayList<Point> randomCells = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Point randomCell = new Point();
			do {
				randomCell.x = utility.Random.getRandomInt(0, 9);
				randomCell.y = utility.Random.getRandomInt(0, 9);
			} while (randomCells.contains(randomCell));
			randomCells.add(randomCell);
			System.out.print(String.format("(%d, %d); ",
							randomCell.x,
							randomCell.y));
			Field.getCell(randomCell).reveal();
		}
		System.out.println();
		System.out.println();
		drawField();
	}

	private static void drawConcealedField() {
		for (int r = 0; r < Field.getSize().height; r++) {
			for (int c = 0; c < Field.getSize().width; c++) {
				System.out.print("X\t");
			}
			System.out.print("\n");
		}
	}
	
	private static void drawRevealedField() {
		for (int r = 0; r < Field.getSize().height; r++) {
			for (int c = 0; c < Field.getSize().width; c++) {
				Cell cell = Field.getCell(new Point(c, r));
				cell.reveal();
				String symbol = cell.getContents().getSymbol();
				System.out.print(symbol	+ "\t");
			}
			System.out.print("\n");
		}
	}
	
	private static void drawField() {
		for (int r = 0; r < Field.getSize().height; r++) {
			for (int c = 0; c < Field.getSize().width; c++) {
				Cell cell = Field.getCell(new Point(c, r));
				String symbol = cell.getContents().getSymbol();
				System.out.print(symbol	+ "\t");
			}
			System.out.print("\n");
		}
	}

	private static void printTest(String description, String expected) {
		System.out.println("\n******************************************************************************");
		System.out.print("\nTESTING:\n" + description + "\n\nExpected:\n"
				+ expected + "\n\nActual:\n");
	}
}