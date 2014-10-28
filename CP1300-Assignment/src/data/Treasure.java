package data;

public class Treasure extends Cell{
	public static final int MIN_TREASURE = 1;
	static final String SYMBOL = "T";
	static final String COLOR = "0x0044f7";

	public Treasure() {
		super();
		symbol = SYMBOL;
		revealedColor = COLOR;
	}
}
