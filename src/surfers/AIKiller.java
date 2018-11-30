package surfers;

public class AIKiller extends AIPlayer {
	private int ht;

	public AIKiller(int side, int ht) {
		super(side);
		this.ht = ht;
	}

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
					int eval = -killerAlphaBeta(new Node(board.getSurfers().clone(), moves[i][j]), getSide(), ht, -10000, 10000);
					if(eval > topEval) {
						topEval = eval;
						sIndex = i < row*col ? index1 : index2;
						sPosition = i % (row*col);
						positions = moves[i][j];
					}
				}
			}
		}
		board.putAi(sIndex, sPosition, positions);
	}

	public int killerAlphaBeta(Node node, int side, int height, int achievable, int hope) {
		if (height == 0)
			return evaluate(node.getPositions(), node.getSurferPostitions(), side);
		int index1 = 0;
		int index2 = 1;
		if (side < 0) {
			index1 = 2;
			index2 = 3;
		}
		long[][] moves = getPossibleMoves(node.getPositions(), node.getSurferPostitions(), side);
		if (possibleMoveCount(moves) == 0)
			return evaluate(node.getPositions(), node.getSurferPostitions(), side);

		int temp = 0;
		for (int i = 0; i < moves.length; i++) {
			if (moves[i] != null) {
				int[] sPos = node.getSurferPostitions().clone();
				if (i < row * col)
					sPos[index1] = i;
				else
					sPos[index2] = i % (row * col);
				for (int j = 0; j < moves[i].length; j++) {
					temp = -killerAlphaBeta(new Node(sPos, moves[i][j]), -side, height-1, -hope, -achievable);
					if (temp >= hope) {
						node.setCutoffs(node.getCutoffs() + 1);
						return temp;
					}
					achievable = Math.max(achievable, temp);
				}
			}
		}
		return achievable;
	}

}
