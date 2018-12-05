// Alicja Przybys, nr 18204233
package surfers;

import java.util.Scanner;

public class HumanPlayer implements Player {
	private int side;

	public HumanPlayer(int side) {
		this.side = side;
	}

	final public static String[] LETTERS = Board.LETTERS;
	final static public String[] letters = { "a", "b", "c", "d", "e", "f", "g" };

	// asks for input, checks if the input is valid, if it's not asks again, if it is makes the move
	public void makeMove(Board board) {
		boolean inputting = true;
		int[] positions = new int[3];
		while (inputting) {
			System.out.println("Please put in the location of the surfer you want to move, "
					+ "its new position and where you want to put your charge");
			Scanner scan = new Scanner(System.in);
			positions = inputParser(scan.nextLine());
			if (positions != null && positions.length == 3) {
				int x = positions[1] / Board.ROW;
				int y = positions[1] % Board.ROW;
				int chargeX = positions[2] / Board.ROW;
				int chargeY = positions[2] % Board.ROW;
				int surferIndex = positions[0] == board.getSurfers(side)[0] ? 
						(side >= 0 ? 0 : 2) : (side >= 0 ? 1 : 3);
				if (board.movePlayer(surferIndex, x, y, chargeX, chargeY)) inputting = false;
			}
		}
	}
	
	// function for reading the "a1 a2 a4" input
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
	
	// takes a single "a1" and changes it into an integer value on the board
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
