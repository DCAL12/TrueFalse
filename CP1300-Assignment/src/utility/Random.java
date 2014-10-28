package utility;

import java.util.ArrayList;

public class Random {
	private static java.util.Random random;

	static {
		random = new java.util.Random();
	}

	public static int getRandomInt(int min, int max) {
		// Generate a random integer between min and max (inclusive)
		return min + random.nextInt(1 + max - min);
	}

	public static ArrayList<Integer> getRandomInts(int min, int max, int count,
			boolean uniqueValues) {
		/* Generate a list of random integers between min and max (inclusive)
		*	-uniqueValues argument forces elements to be unique
		*/
		
		// Check if min and max make sense
		if (min > max) return null;
		// Check if requested number of unique values can be generated
		int possibleValues = max - min + 1;
		if (uniqueValues && count > possibleValues) return null;

		ArrayList<Integer> randomInts = new ArrayList<>();

		for (int i = 0; i < count;) {
			int randomInt = getRandomInt(min, max);
			
			if (uniqueValues && !Unique.isUnique(randomInt, randomInts)) continue;
			
			randomInts.add(randomInt);
			i++;
		}
		return randomInts;
	}
	
	public static ArrayList<Integer> getRandomInts(int min, int max, int count,
			boolean uniqueValues, ArrayList<Integer> exclude) {
		/* Generate a list of random integers between min and max (inclusive)
		*-uniqueValues argument forces elements to be unique
		*-exclude argument generates a random ArrayList where no elements match
		*	elements of exclude ArrayList
		*/
	
	// Check if min and max make sense
	if (min > max) return null;
	// Check if requested number of unique values can be generated
	int possibleValues = (max - min + 1) - exclude.size();
	if (uniqueValues && count > possibleValues) return null;

	ArrayList<Integer> randomInts = new ArrayList<>();

	for (int i = 0; i < count;) {
		int randomInt = getRandomInt(min, max);
		
		if (uniqueValues && !Unique.isUnique(randomInt, randomInts)) continue;
		if (exclude.contains(randomInt)) continue;
		
		randomInts.add(randomInt);
		i++;
	}
	return randomInts;
	}
}
