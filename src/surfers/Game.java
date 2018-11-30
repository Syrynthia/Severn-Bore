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
		return false;
	}

	public static void main(String[] args) {
		Board board = new Board();
		Game game = new Game(board);
		game.init();
	}

}
