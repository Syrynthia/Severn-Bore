// Alicja Przybys, nr 18204233
package surfers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// superclass for the ai algorithms also serving as a random player
// contains the evaluation function as well as the move generator
public class AIPlayer implements Player {
	private int side;
	protected int row = Board.ROW;
	protected int col = Board.COL;
	private final int MAX_MOVE = 3;
	private List<Integer> shifts;
	private int dir = 8;
	protected int index1 = 0;
	protected int index2 = 1;

	public AIPlayer() {
		side = Game.PLAYER1;
		makeShifts();
	}

	public AIPlayer(int side) {
		this.side = side;
		makeShifts();
		if (side < 0) {
			index1 = 2;
			index2 = 3;
		}
	}

	// function for creating a list of directional shifts from the xy position
	private void makeShifts() {
		shifts = new ArrayList<>();
		// starting from one to get to the outer ring of the xy place
		for (int i = 1; i < MAX_MOVE + 1; i++) {
			shifts.add(-i * row); // vertical up
			shifts.add(i * row); // vertical down
			shifts.add(-i); // horizontal west
			shifts.add(-i * row - i); // diagonal northwest
			shifts.add(i * row - i); // diagonal southwest
			shifts.add(i); // horizontal east
			shifts.add(-i * row + i); // diagonal northeast
			shifts.add(i * row + i); // diagonal southeast
		}
	}

	// just give this function all the surfer positions, it will give an array
	// of possible positions for the player on the side
	public long[][] getPossibleMoves(long boardPos, int[] surfPos, int side) {
		long[][] moves = new long[2 * row * col][];
		int index1 = 0;
		int index2 = 1;
		if (side < 0) {
			index1 = 2;
			index2 = 3;
		}
		long[][] moves1 = possibleMoves(boardPos, surfPos[index1]);
		long[][] moves2 = possibleMoves(boardPos, surfPos[index2]);
		for (int i = 0; i < moves.length; i++) {
			if (i < row * col)
				moves[i] = moves1[i];
			else
				moves[i] = moves2[i % (row * col)];
		}
		return moves;
	}

	// checking possible moves for the surfer at x, y
	public long[][] possibleMoves(long currentBoard, int surfPos) {
		long[][] moves = new long[row * col][];
		// removing the surfer
		currentBoard = currentBoard ^ 1L << surfPos;
		// 8 directions we can go
		for (int i = 0; i < dir; i++) {
			// rings around xy
			for (int j = 0; j < MAX_MOVE; j++) {
				int newPos = getNewPosition(surfPos, i, j);
				if (newPos >= 0) {
					long step = 1L << newPos;
					if ((currentBoard & step) == 0)
						moves[newPos] = possibleCharges(currentBoard | (1L << newPos), newPos);
					else
						break; // we've stepped on a charge or a player
								// can't go further than this anyway
				} else
					break; // we're outside of the board
							// no reason to continue down this road
			}
		}
		return moves;
	}

	private int getNewPosition(int surfPos, int i, int j) {
		int newPos = surfPos + shifts.get(i + j * dir);
		int y = surfPos % row;
		boolean wrapping = false;
		// wrapping won't occur in vertical movement
		if (i > 1) {
			if (i < 5)
				y = y - j - 1;
			else
				y = y + j + 1;
			wrapping = !(y >= 0 && y < col);
		}
		if (newPos < 0 || newPos >= row * col || wrapping)
			return -1;
		return newPos;
	}

	// returns an array of longs representing new board positions
	private long[] possibleCharges(long positions, int surfPos) {
		List<Long> charges = new ArrayList<>();
		for (int i = 0; i < dir; i++) {
			for (int j = 0; j < MAX_MOVE; j++) {
				int newPos = getNewPosition(surfPos, i, j);
				if (newPos >= 0) {
					long step = 1L << newPos;
					if ((positions & step) == 0)
						charges.add(positions | 1L << newPos);
					else
						break; // we've stepped on a charge or a player
								// can't go further than this anyway
				} else
					break; // we're outside of the board
							// no reason to continue down this road
			}
		}
		if (charges.isEmpty())
			return null;

		long[] ch = new long[charges.size()];
		for (int i = 0; i < charges.size(); i++)
			ch[i] = charges.get(i);
		return ch;
	}

	public int possibleMoveCount(long[][] moves) {
		int count = 0;
		for (int i = 0; i < moves.length; i++) {
			if (moves[i] != null) {
				count += moves[i].length;
				// System.out.println(i + ": " + moves[i].length);
			}
		}
		return count;
	}

	// the evaluation function is focused on simply maximising the amount of full fields around the opponent
	// and minimising the amount of filled fields around the player
	// I've introduced a penalty and reward system which seems to prod it in the right direction
	public int evaluate(long position, int[] surfers, int side) {
		int result = 0;
		int reward = 50;
		int penalty = -100;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					int r = surfers[k] / Board.ROW;
					int c = surfers[k] % Board.COL;
					if (r + i >= 0 && r + i < Board.ROW && c + j >= 0 && c + j < Board.COL) {
						if ((position & 1L << ((r + i) * Board.ROW) + c + j) != 0) {
							if(side >= 0) result += penalty;
							else result += reward;
						}
					} else {
						if(side >= 0) result += penalty;
						else result += reward;
					}
				}
				for (int k = 2; k < 4; k++) {
					int r = surfers[k] / Board.ROW;
					int c = surfers[k] % Board.COL;
					if (r + i >= 0 && r + i < Board.ROW && c + j >= 0 && c + j < Board.COL) {
						if ((position & 1L << ((r + i) * Board.ROW) + c + j) != 0){
							if(side < 0) result += penalty;
							else result += reward;
						}
					} else {
						if(side < 0) result += penalty;
						else result += reward;
					}
				}
			}
		}
		
		//checking for corners
		for (int k = 0; k < surfers.length; k++) {
			if(surfers[k] == 0 || surfers[k] == row  || surfers[k] == row*(col-1)  || surfers[k] == (row*col - 1) ) {
				if((side >= 0 && k < surfers.length/2) || (side < 0 && k >= surfers.length/2)) {
					result -= 4*penalty;
				}
				else if((side >= 0 && k >= surfers.length/2) || (side < 0 && k < surfers.length/2)) {
					result += 4*reward;
				}
			}
		}

		return result;
	}

	// makes a random move - this being the superclass for the other ai, I figured I might make it the random player
	public void makeMove(Board board) {
		long[][] moves = getPossibleMoves(board.getPositions(), board.getSurfers(), this.side);
		int size = possibleMoveCount(moves);
		if (size <= 0) {
			System.out.println("I have no more moves");
			return;
		}
		Random rnd = new Random();
		int nr = rnd.nextInt(size);
		int index = -1;
		int counter = 0;
		for (int i = 0; i < moves.length; i++) {
			if (moves[i] != null) {
				for (int j = 0; j < moves[i].length; j++) {
					if (counter == nr) {
						if (i < row * col)
							index = index1;
						else
							index = index2;
						board.putAi(index, i % (row * col), moves[i][j]);
					}
					counter++;
				}
			}
		}

	}

	public int getSide() {
		return side;
	}

}
