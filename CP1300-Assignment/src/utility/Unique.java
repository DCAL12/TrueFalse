package utility;

import java.util.ArrayList;

public class Unique {
	public final static boolean isUnique(final int checkInt, final ArrayList<Integer> values) {
		// Check uniqueness of an integer in a list
		boolean isUnique;
		
		for (int value : values) {
			isUnique = (checkInt == value) ? false : true;
			if (!isUnique) return false;
		}
		return true;
	}
	
	public final static boolean isUnique(final ArrayList<Integer> values1, final ArrayList<Integer> values2) {
		// Check uniqueness of two arrays
		boolean isUnique;
		
		for (int value1 : values1) {
			for (int value2 : values2) {
				isUnique = (value1 == value2) ? false : true;
				if (!isUnique) return false;
			}
		}
		return true;
	}
}
