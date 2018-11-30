package surfers;

import java.util.Arrays;

public class Board {
	final static public int ROW = 5;
	final static public int COL = 5;
	final private int NUM_SURFERS = 4;
	private long positions = 0;
	int[] surfers;
	private int currentPlayer = Game.PLAYER1;
	// letters reversed for printing
	final static public String[] LETTERS = { "A", "B", "C", "D", "E", "F", "G" };

	// default constructor setting up the board for the game
	public Board() {
		surfers = new int[NUM_SURFERS];
		putSurfer(0, 1);
		putSurfer(1, 5);
		putSurfer(2, 19);
		putSurfer(3, 23);
	}

	private void putSurfer(int surferIndex, int position) {
		surfers[surferIndex] = position;
		positions = positions | 1L << (position);
	}
	
	public void putAi(int sIndex, int sPosition, long board) {
		surfers[sIndex] = sPosition;
		positions = board;
		currentPlayer = -currentPlayer;
	}

	private void removeSurfer(int surferIndex) {
		positions = positions ^ 1L << (surfers[surferIndex]);
		surfers[surferIndex] = -1;
	}

	private void putCharge(int x, int y) {
		positions = positions | 1L << (x * ROW + y);
	}

	public boolean movePlayer(int surferIndex, int x, int y, int chargeX, int chargeY) {
		if (movePossible(surfers[surferIndex], x, y, chargeX, chargeY)) {
			removeSurfer(surferIndex);
			putSurfer(surferIndex, x*ROW + y);
			putCharge(chargeX, chargeY);
			currentPlayer = -currentPlayer;
			return true;
		} else
			System.out.println("Move not possible");
		return false;
	}

	public boolean movePossible(int s, int x, int y, int chargeX, int chargeY) {
		// checking if the move doesn't go off board
		if (x < 0 || x > 6 || y < 0 || y > 6)
			return false;
		// checking if the charge doesn't go off board
		if (chargeX < 0 || chargeX > 6 || chargeY < 0 || chargeY > 6)
			return false;
		long newPosition = positions;
		if (!canMove(newPosition, s / ROW, s % ROW, x, y)) {
			System.out.println("You swam on a charge or another player");
			return false;
		}
		newPosition = newPosition ^ (1L << s);
		newPosition = newPosition | (1L << (x * ROW + y));
		if (!canMove(newPosition, x, y, chargeX, chargeY)) {
			System.out.println("You put a charge on a charge or another player");
			return false;
		}
		return true;
	}

	public void print() {
		print(positions, surfers);
	}

	public void print(long pos, int[] s) {

		System.out.println("   1 2 3 4 5 ");
		for (int i = 0; i < ROW; i++) {
			System.out.print(LETTERS[i] + " |");
			for (int j = 0; j < COL; j++) {
				if ((pos & 1L << (i * ROW + j)) == 0)
					System.out.print(" |");
				else {
					if (s[0] == (i * ROW + j) || s[1] == (i * ROW + j))
						System.out.print("#|");
					else if (s[2] == (i * ROW + j) || s[3] == (i * ROW + j))
						System.out.print("o|");
					else
						System.out.print("*|");
				}
			}
			System.out.println();
		}
		if (currentPlayer > 0)
			System.out.println("It's player 1's time to move");
		else
			System.out.println("It's player 2's time to move");
	}

	private boolean canMove(long pos, int x1, int y1, int x2, int y2) {
		int xDif = Math.abs(x1 - x2);
		int yDif = Math.abs(y1 - y2);
		// checking if the move is up to 5 paces from the original position
		if (xDif > 5 || yDif > 5) {
			System.out.println("Move up to 5 paces");
			return false;
		}
		int xyDif = Math.abs(xDif - yDif);
		// checking if the move is either vertical, horizontal or diagonal
		if (xyDif != Math.max(xDif, yDif) && xyDif != 0) {
			System.out.println("You can only move horizontally, vertically or diagonally");
			return false;
		}
		// horizontal move
		if (yDif == 0) {
			if (x1 < x2) {
				for (int i = x1 + 1; i < x2 + 1; i++) {
					if ((pos & 1L << (i * ROW + y1)) != 0)
						return false;
				}
			} else {
				for (int i = x1 - 1; i > x2 - 1; i--) {
					if ((pos & 1L << (i * ROW + y1)) != 0)
						return false;
				}
			}
		}
		// vertical move
		else if (xDif == 0) {
			if (y1 < y2) {
				for (int i = y1 + 1; i < y2 + 1; i++) {
					if ((pos & 1L << (x1 * ROW + i)) != 0)
						return false;
				}
			} else {
				for (int i = y1 - 1; i > y2 - 1; i--) {
					if ((pos & 1L << (x1 * ROW + i)) != 0)
						return false;
				}
			}
		}
		// diagonals
		else {
			// moving from left to right
			if (x1 < x2) {
				if (y1 < y2) {
					for (int i = 1; i <= xDif; i++) {
						if ((pos & 1L << ((x1 + i) * ROW + y1 + i)) != 0)
							return false;
					}
				} else {
					for (int i = 1; i <= xDif; i++) {
						if ((pos & 1L << ((x1 + i) * ROW + y1 - i)) != 0)
							return false;
					}
				}
			}
			// moving from right to left
			else {
				if (y1 < y2) {
					for (int i = 1; i <= xDif; i++) {
						if ((pos & 1L << ((x1 - i) * ROW + y1 + i)) != 0)
							return false;
					}
				} else {
					for (int i = 1; i <= xDif; i++) {
						if ((pos & 1L << ((x1 - i) * ROW + y1 - i)) != 0)
							return false;
					}
				}
			}
		}
		return true;
	}

	public long getPositions() {
		return positions;
	}
	public int[] getSurfers() {
		return surfers;
	}
	public int[] getSurfers(int side) {
		if(side >= 0)
			return new int[]{surfers[0], surfers[1]};
		return new int[]{surfers[2], surfers[3]};
	}
	public int getCurrentPlayer() {
		return currentPlayer;
	}
}
