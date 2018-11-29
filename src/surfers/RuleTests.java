package surfers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RuleTests {
	Board board = new Board();

	@Test
	void possibleMoves() {
		assertFalse(board.movePossible(0, -1, 0, 0, 0), 
				"No geting off the board");
		assertFalse(board.movePossible(0, 0, 7, 0, 0), 
				"No geting off the board");
		assertFalse(board.movePossible(0, 6, 2, 0, 0), 
				"No moving further than 5 paces");
		assertFalse(board.movePossible(0, 1, 0, 0, 0), 
				"Only moves available: diagonally, horizontally or vertically");
		board.movePlayer(0, 1,1, 6, 1);
		assertFalse(board.movePossible(0, 6, 1, 0, 0), "Don't move on top of a charge");
	}

}
