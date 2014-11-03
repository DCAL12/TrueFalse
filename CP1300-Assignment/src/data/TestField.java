package data;

import java.util.ArrayList;

import data.Cell.Contents;

public class TestField {

	public static void main(String[] args) {
		// Test creating new cells
		printTest("Create new empty cell and get its contents\n"
				+ "[revealed, symbol, color]", 
				"[false, 0/0, 0x77f33b]");
		Cell emptyCell = new Cell(Contents.EMPTY);
		System.out.println(String.format("[%s, %s, %s]", emptyCell.isRevealed(), emptyCell.getContents().getSymbol(), emptyCell.getContents().getColor()));
		
		printTest("Create new falseFlag cell and get its contents\n"
				+ "[revealed, symbol, color]"
				, "[false, F, 0xec402c]");
		Cell falseFlagCell = new Cell(Contents.FALSE_FLAG);
		System.out.println(String.format("[%s, %s, %s]", falseFlagCell.isRevealed(), falseFlagCell.getContents().getSymbol(), falseFlagCell.getContents().getColor()));
		
		printTest("Create new trueFlag cell and get its contents\n"
				+ "[revealed, symbol, color]"
				, "[false, T, 0x0044f7]");
		Cell trueFlagCell = new Cell(Contents.TRUE_FLAG);
		System.out.println(String.format("[%s, %s, %s]", trueFlagCell.isRevealed(), trueFlagCell.getContents().getSymbol(), trueFlagCell.getContents().getColor()));
		
		// Test the default field (Default size: 10x10, Difficulty: Easy (5% Flags, 1% Treasure)
		printTest("Grid size after creating new, default Field", "10x10");
		new Field();
		System.out
				.println(Field.getSize().width + "x" + Field.getSize().height);

		printTest("Display concealed grid", "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n"
				+ "X\tX\tX\tX\tX\tX\tX\tX\tX\tX\n");
		drawField(false);

		printTest("Display revealed grid", "Random distribution of: \n "
				+ "5x 'F' cell\n " + "1x 'T' cell\n "
				+ "95x 'F/T' cells where:\n"
				+ "\tF = # of adjacent flag cells\n"
				+ "\tT = # of adjacent treasure cells");
		drawField(true);

		printTest(
				"Select (reveal) cells",
				"Grid will be concealed except for 5 randomly revealed cells and any '0/0' cells adjacent to a revealed cell");
		
		System.out.print("\tRevealed Cells: ");
		ArrayList<int[]> randomCells = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			int[] randomCell = new int[2];
			do {
				randomCell[0] = utility.Random.getRandomInt(0, 9);
				randomCell[1] = utility.Random.getRandomInt(0, 9);
			} while (randomCells.contains(randomCell));
			System.out.print(String.format("(%d, %d); ",
							randomCell[0],
							randomCell[1]));
			Field.getCell(randomCell[0], randomCell[1]).reveal();
		}
		System.out.println();
		System.out.println();
		drawField(false);
	}

	private static void drawField(boolean showAll) {
		/* Re-draw the grid. Concealed cells will display as 'X',
		 * revealed cells will show their contents
		 */
		for (int r = 0; r < Field.getSize().height; r++) {
			for (int c = 0; c < Field.getSize().width; c++) {
				if (showAll
						|| Field.getCell(c, r).isRevealed()) {
					// Print the revealed cell
					Cell cell = Field.getCell(c, r);
					String symbol = cell.getContents() == Contents.HAS_ADJACENT ? 
							String.format(cell.getContents().getSymbol(),
									cell.getAdjacentFalseFlags(),
									cell.getAdjacentTrueFlags()) : 
							cell.getContents().getSymbol();
					System.out.print(symbol	+ "\t");
				} else {
					// Print the concealed ('X') cell
					System.out.print("X\t");
				}
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