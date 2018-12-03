package surfers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AiHistory extends AIPlayer {
	private int ht;
	private List<KillerMove> p1;
	private List<KillerMove> p2;
	private int evaluations = 0;

	public AiHistory(int side, int ht) {
		super(side);
		this.ht = ht;
		p1 = new ArrayList<>();
		p2 = new ArrayList<>();
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
					int eval = histAlphaBeta(new Node(board.getSurfers().clone(), moves[i][j]), getSide(), ht, -10000, 10000);
					if (eval > topEval) {
						topEval = eval;
						sIndex = i < row * col ? index1 : index2;
						sPosition = i % (row * col);
						positions = moves[i][j];
					}
				}
			}
		}
		System.out.println("History evaluations: " + evaluations);
		evaluations = 0;
		board.putAi(sIndex, sPosition, positions);
	}

	public int histAlphaBeta(Node node, int side, int height, int achievable, int hope) {
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
		List<KillerMove> mv = movesToList(moves, node, side);
		int temp = 0;
		for (int i = 0; i < mv.size(); i++) {
			temp = -histAlphaBeta(new Node(mv.get(i).getSurfers(), mv.get(i).getBoard()), -side, height - 1, -hope,
					-achievable);
			if (temp >= hope) {
				if (side < 0) {
					if (p2.contains(mv.get(i)))
						p2.get(p2.indexOf(mv.get(i))).setCutoffs(size - i + 1);
					else {
						mv.get(i).setCutoffs(size - i + 1);
						p2.add(mv.get(i));
					}
				} else {
					if (p1.contains(mv.get(i)))
						p1.get(p1.indexOf(mv.get(i))).setCutoffs(size - i + 1);
					else {
						mv.get(i).setCutoffs(size - i + 1);
						p1.add(mv.get(i));
					}
				}
				return temp;
			}
			achievable = Math.max(achievable, temp);
		}
		return achievable;
	}

	private List<KillerMove> movesToList(long[][] moves, Node node, int side) {
		List<KillerMove> list = new ArrayList<>();
		int i1 = 0;
		int i2 = 1;
		if (side < 0) {
			i1 = 2;
			i2 = 3;
		}
		for (int i = 0; i < moves.length; i++) {
			if (moves[i] != null) {
				int[] sPos = node.getSurferPostitions().clone();
				if (i < row * col)
					sPos[i1] = i;
				else
					sPos[i2] = i % (row * col);
				for (int j = 0; j < moves[i].length; j++) {
					KillerMove k = new KillerMove(0, sPos, moves[i][j]);
					if (side < 0) {
						if (p2.contains(k))
							list.add(p2.get(p2.indexOf(k)));
						else {
							p2.add(k);
							list.add(k);
						}
					} else {
						if (p1.contains(k))
							list.add(p1.get(p1.indexOf(k)));
						else {
							p1.add(k);
							list.add(k);
						}
					}
				}
			}
		}
		list.sort(new Comparator<KillerMove>() {
			@Override
			public int compare(KillerMove arg0, KillerMove arg1) {
				// sorting in descending order depending on the number of cutoffs
				return arg1.getCutoffs() - arg0.getCutoffs();
			}
		});
		return list;
	}
}
