package surfers;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
	private Board board;

	private Scanner scan = new Scanner(System.in);
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
		System.out.println("4 - test");
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
		case 3:
			aiVsAi();
			break;
		case 4:
			test();
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
				players[1] = new AIKiller(-playerSide, 3);
			}
			if (inpSide == 2) {
				playerSide = -PLAYER1;
				players[0] = new AIKiller(-playerSide, 3);
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
	
	private void aiVsAi() {
		players = new Player[2];
		players[0] = new AIKiller(1, 3);
		players[1] = new AiAB(-1, 3);
		board.print();
		while (!gameEnded()) {
			int ind = board.getCurrentPlayer() >= 0 ? 0 : 1;
			players[ind].makeMove(board);
			board.print();
		}
	}
	
	private void test() {
		System.out.println("Input the height to which you want the ai searching");
		int ht = 0;
		try {
			ht = scan.nextInt();
		} catch (InputMismatchException e) {
			return;
		}
		board.print();
		players = new Player[4];
		players[0] = new HumanPlayer(1);
		players[1] = new AiAB(-1, ht);
		players[2] = new AIKiller(-1, ht);
		players[3] = new AiHistory(-1, ht);
		while (!gameEnded()) {
			if(board.getCurrentPlayer() >= 0 ) {
				players[0].makeMove(board);
				board.print();
			}
			else {
				Board[] b = new Board[3];
				for(int i = 1; i < players.length; i++) {
					b[i-1] = new Board(board.getSurfers().clone(), board.getPositions(), board.getCurrentPlayer());
					players[i].makeMove(b[i-1]);
					b[i-1].print();
				}
				System.out.println("Choose the move you like 0, 1 or 2");
				int index = 0;
				try {
					index = scan.nextInt();
				} catch (InputMismatchException e) {
					
				}
				board = index >= 0 && index < 3 ? b[index] : b[0];
			}
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
