package data;

public class Difficulty {

	// Difficulty Constants (% flags, % treasure)
	public static final int[] EASY = {5, 1};
	public static final int[] MEDIUM = {10, 5};
	public static final int[] HARD = {20, 10};

	private static int[] choice;
	private int percentFlags;
	private int percentTreasure;

	public Difficulty(int[] choice) {
		Difficulty.choice = choice;
		percentFlags = choice[0];
		percentTreasure = choice[1];
	}

	public int getNumFlags(int totalCells) {
		return Math.max((int) (percentFlags / 100.0 * totalCells),
				Flag.MIN_FLAGS);
	}

	public int getNumTreasure(int totalCells) {
		return Math.max((int) (percentTreasure / 100.0 * totalCells),
				Treasure.MIN_TREASURE);
	}
	
	public static int[] getChoice() {
		return choice;
	}
	
	@Override
	public String toString() {
		if (choice.equals(EASY)) return "Easy";
		if (choice.equals(MEDIUM)) return "Medium";
		if (choice.equals(HARD)) return "Hard";
		return "Other: (%Flags: " + percentFlags + ", %Treasure: " + percentTreasure + ")";
	}
}
