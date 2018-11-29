package surfers;

public class AIKiller extends AIPlayer {

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
				int[] sPos = node.getSurferPostitions();
				if (i < row * col)
					sPos[index1] = i;
				else
					sPos[index2] = i % (row * col);
				for (int j = 0; j < moves[i].length; j++) {
					temp = -killerAlphaBeta(new Node(sPos, moves[i][j]), -side, height, -hope, -achievable);
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
