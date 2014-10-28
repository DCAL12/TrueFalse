package data;

public class Flag extends Cell{
	public static final int MIN_FLAGS = 1;
	static final String SYMBOL = "F";
	static final String COLOR = "0xec402c";
	private static int count;

	public Flag() {
		super();
		count++;
		symbol = SYMBOL;
		revealedColor = COLOR;
	}
	
	static void resetCount() {
		count = 0;
	}
	
	static int getCount() {
		return count;
	}

}
