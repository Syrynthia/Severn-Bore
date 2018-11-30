package surfers;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
	private Board board;

	private Scanner scan = new Scanner(System.in);
	private AIPlayer aiPlayer;
	final public static int PLAYER1 = 1;
	private Player[] players;

	public Game(Board board) {
		this.board = board;
	}

	public void init() {
		System.out.println("Hello! Glad you're up for a game of Severn Bore! " + "Which mode would you like to play?");
		System.out.println("Please select: ");
		System.out.println("1 - human vs human");
		System.out.println("2 - human vs ai");
		System.out.println("3 - ai vs ai");
		System.out.println("Press any other key to quit the game");
		int input = -1;
		try {
			input = scan.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Thanks for playing!");
			return;
		}
		switch (input) {
		case 2:
			humanVsAi();
			break;

		default:
			System.out.println("Thanks for playing!");
			break;
		}
	}

	private void humanVsAi() {
		System.out.println("Which side would you like to play?");
		System.out.println("Please select: ");
		System.out.println("1 - you'll play as player 1");
		System.out.println("2 - you'll play as player 2");
		System.out.println("Press any other key to quit the game");
		players = new Player[2];
		String input = scan.next();
		int playerSide = PLAYER1;
		try {
			int inpSide = Integer.parseInt(input);
			if (inpSide < 1 || inpSide > 2)
				return;
			if (inpSide == 1) {
				players[0] = new HumanPlayer(playerSide);
				players[1] = new AIPlayer(-playerSide);
			}
			if (inpSide == 2) {
				playerSide = -PLAYER1;
				players[0] = new AIPlayer(-playerSide);
				players[1] = new HumanPlayer(playerSide);
			}
		} catch (InputMismatchException e) {
			return;
		}
		board.print();
		while (!gameEnded()) {
			int ind = board.getCurrentPlayer() >= 0 ? 0 : 1;
			players[ind].makeMove(board);
			board.print();
		}
	}

	private boolean gameEnded() {
		long[] masks = new long[4];
		int[] surfers = board.getSurfers();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int k = 0; k < 4; k++) {
					int r = surfers[k] / Board.ROW;
					int c = surfers[k] % Board.COL;
					if (r + i >= 0 && r + i < Board.ROW && c + j >= 0 && c + j < Board.COL)
						masks[k] = masks[k] | 1L << ((r + i) * Board.ROW) + c + j;
				}
			}
		}
		boolean p1 = (masks[0] & board.getPositions()) == masks[0] && (masks[1] & board.getPositions()) == masks[1];
		boolean p2 = (masks[2] & board.getPositions()) == masks[2] && (masks[3] & board.getPositions()) == masks[3];
		if (p1 && p2) {
			System.out.println("It's a draw!");
			return true;
		}
		else if (p1) {
			System.out.println("Congratulations! Player 2 has won!");
			return true;
		}
		else if (p2) {
			System.out.println("Congratulations! Player 1 has won!");
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		Board board = new Board();
		Game game = new Game(board);
		game.init();
	}

}
