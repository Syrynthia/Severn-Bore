// Alicja Przybys, nr 18204233
package surfers;

// alphabeta algorithm with the implemented killer heuristic
public class AIKiller extends AIPlayer {
	private int ht;
	private KillerMove[] killers;
	private int currentMove;
	private int evaluations = 0;

	public AIKiller(int side, int ht) {
		super(side);
		this.ht = ht;
		killers = new KillerMove[200];
		currentMove = side >= 0 ? 0 : 1;
	}

	// unlike the pure alphabeta and history, this makeMove also keeps the count of which move we're on
	// to address the correct killer move for the current ply
	@Override
	public void makeMove(Board board) {
		long[][] moves = getPossibleMoves(board.getPositions(), board.getSurfers(), getSide());
		int topEval = -10000;
		int sIndex = 0;
		int sPosition = 0;
		long positions = 0;
		for (int i = 0; i < moves.length; i++) {
			if (moves[i] != null) {
				for (int j = 0; j < moves[i].length; j++) {
					int eval = killerAlphaBeta(new Node(board.getSurfers().clone(), moves[i][j]), getSide(), ht, -10000,
							10000, currentMove);
					if (eval > topEval) {
						topEval = eval;
						sIndex = i < row * col ? index1 : index2;
						sPosition = i % (row * col);
						positions = moves[i][j];
					}
				}
			}
		}
		currentMove += 2;
		System.out.println("Killer evaluations: "+evaluations);
		evaluations = 0;
		board.putAi(sIndex, sPosition, positions);
	}

	// alphabeta algorithm function
	public int killerAlphaBeta(Node node, int side, int height, int achievable, int hope, int ply) {
		if (height == 0) {
			evaluations++;
			return evaluate(node.getPositions(), node.getSurferPostitions(), side);
		}
		long[][] moves = getPossibleMoves(node.getPositions(), node.getSurferPostitions(), side);
		int size = possibleMoveCount(moves);
		if (size == 0){
			evaluations++;
			return evaluate(node.getPositions(), node.getSurferPostitions(), side);
		}

		int i1 = 0;
		int i2 = 1;
		if (side < 0) {
			i1 = 2;
			i2 = 3;
		}
		int temp = 0;
		// checking if the killer move for this ply exists and if it's still available 
		// then doing the search for it first
		if(killers[ply] != null) {
			temp = getKiller(killers[ply], side, height, hope, achievable, ply, moves);
			if (temp >= hope) {
				killers[ply] = new KillerMove(size - 1, killers[ply].getSurfers(), killers[ply].getBoard());
				return temp;
			}
		}
		int counter = 0;
		// main algorithm part
		for (int i = 0; i < moves.length; i++) {
			if (moves[i] != null) {
				int[] sPos = node.getSurferPostitions().clone();
				if (i < row * col)
					sPos[i1] = i;
				else
					sPos[i2] = i % (row * col);
				for (int j = 0; j < moves[i].length; j++) {
					counter++;
					temp = -killerAlphaBeta(new Node(sPos, moves[i][j]), -side, height - 1, -hope, -achievable,
							ply + 1);
					if (temp >= hope) {
						// recording a killer move additionally to doing the cutoff
						if (killers[ply] == null)
							killers[ply] = new KillerMove(size - counter, sPos, moves[i][j]);
						else if (killers[ply].getCutoffs() < size - counter)
							killers[ply] = new KillerMove(size - counter, sPos, moves[i][j]);
						return temp;
					}
					achievable = Math.max(achievable, temp);
				}
			}
		}
		return achievable;
	}

	// separated function for finding the evaluation for the killer move
	// separated because of the annoying search if the killer move is still available
	private int getKiller(KillerMove k, int side, int height, int hope, int achievable, int ply, long[][] moves) {
		for (int i = 0; i < moves.length; i++) {
			if (moves[i] != null) {
				for (int j = 0; j < moves[i].length; j++) {
					if (moves[i][j] == k.getBoard()) {
						for (int l = 0; l < k.getSurfers().length; l++) {
							if (k.getSurfers()[l] == i)
								return -killerAlphaBeta(new Node(k.getSurfers(), k.getBoard()), -side, height - 1,
										-hope, -achievable, ply + 1);
						}
					}
				}
			}
		}
		// basically -infinity just so it doesn't get picked if it's no longer killer
		return -100000;
	}

}
