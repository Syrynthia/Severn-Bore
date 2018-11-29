package surfers;

import java.util.Scanner;

public class HumanPlayer implements Player {
	final public static String[] LETTERS = Board.LETTERS;
	final static public String[] letters = { "a", "b", "c", "d", "e", "f", "g" };

	@Override
	public int[] getMove(long boardPos, int[] surfPos) {

		boolean inputting = true;
		int[] positions = new int[3];
		while (inputting) {
			System.out.println("Please put in the location of the surfer you want to move, "
					+ "it's new position and where you want to put your charge");
			Scanner scan = new Scanner(System.in);
			positions = inputParser(scan.nextLine());
			if (positions != null && positions.length == 3) {

			}
		}
		return positions;
	}

	public int[] inputParser(String input) {
		String[] spl = input.split(" ");
		if (spl.length != 3)
			return null;
		int[] values = new int[spl.length];
		for (int i = 0; i < spl.length; i++) {
			int v = letters2numbers(spl[i]);
			if (v < 0)
				return null;
			values[i] = v;
		}
		return values;
	}

	private int letters2numbers(String s) {
		int val = 0;
		boolean flag = false;
		for (int i = 0; i < Board.ROW; i++) {
			if (LETTERS[i].equals(s.substring(0, 1)) || letters[i].equals(s.substring(0, 1))) {
				val += i * Board.ROW;
				flag = true;
			}
		}
		if (!flag)
			return -1;
		int y = Integer.parseInt(s.substring(1, 2)) - 1;
		if (y >= Board.ROW)
			return -1;
		val += y;
		return val;
	}
}
