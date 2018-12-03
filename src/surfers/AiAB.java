package surfers;

public class AiAB extends AIPlayer{
	private int ht;
	private int evaluations = 0;
	
	public AiAB(int side, int ht) {
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
					int eval = alphaBeta(new Node(board.getSurfers().clone(), moves[i][j]), getSide(), ht, -10000, 10000);
					if (eval > topEval) {
						topEval = eval;
						sIndex = i < row * col ? index1 : index2;
						sPosition = i % (row * col);
						positions = moves[i][j];
					}
				}
			}
		}
		System.out.println("AlphaBeta evaluations: " + evaluations);
		evaluations = 0;
		board.putAi(sIndex, sPosition, positions);
	}

	
	public int alphaBeta(Node node, int side, int height, int achievable, int hope) {
		if (height == 0) {
			evaluations ++;
			return evaluate(node.getPositions(), node.getSurferPostitions(), side);
		}
		long[][] moves = getPossibleMoves(node.getPositions(), node.getSurferPostitions(), side);
		int size = possibleMoveCount(moves);
		if (size == 0){
			evaluations ++;
			return evaluate(node.getPositions(), node.getSurferPostitions(), side);
		}
		int i1 = 0;
		int i2 = 1;
		if (side < 0) {
			i1 = 2;
			i2 = 3;
		}
		int temp = 0;
		for (int i = 0; i < moves.length; i++) {
			if (moves[i] != null) {
				int[] sPos = node.getSurferPostitions().clone();
				if (i < row * col)
					sPos[i1] = i;
				else
					sPos[i2] = i % (row * col);
				for (int j = 0; j < moves[i].length; j++) {
					temp = -alphaBeta(new Node(sPos, moves[i][j]), -side, height - 1, -hope, -achievable);
					if (temp >= hope) {
						return temp;
					}
					achievable = Math.max(achievable, temp);
				}
			}
		}
		return achievable;
	}
}
