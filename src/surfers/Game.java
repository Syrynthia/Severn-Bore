package surfers;

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
				players[0] = new HumanPlayer();
				players[1] = new AIPlayer(- playerSide);
			}
			if (inpSide == 2) {
				playerSide = -PLAYER1;
				players[0] = new AIPlayer(- playerSide);
				players[1] = new HumanPlayer();
			}
		} catch (NumberFormatException e) {
			return;
		}
		board.print();
		while (!gameEnded()) {
			// depending on the side, either the player moves first or the ai
			// a functions playerMove()
			// a function aiMove()

		}
	}

	private boolean gameEnded() {
		return false;
	}

	public static void main(String[] args) {
		Board board = new Board();
		board.print();
		Game game = new Game(board);
//		int[] vals = game.inputParser("e1 e2 e3");
//		for (int i = 0; i < vals.length; i++)
//			System.out.print(vals[i] + ", ");
		/*
		 * board.movePlayer(0, 3, 5, 3, 6); board.print(); AIPlayer ai = new
		 * AIPlayer(1); long[][] mv = ai.possibleMoves(board.getPositions(), 1);
		 * System.out.println("Total: " + ai.possibleMoveCount(mv)); int[] surfers =
		 * board.getSurfers(); surfers[0] = 2; board.print(mv[2][0], surfers);
		 */
	}

}
