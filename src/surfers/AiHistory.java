package surfers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AiHistory extends AIPlayer {
	private int ht;
	private int evaluations = 0;
	private List<List<HistoryStruct>> pl1;
	private List<List<HistoryStruct>> pl2;
	private List<List<KillerMove>> buckets;

	public AiHistory(int side, int ht) {
		super(side);
		this.ht = ht;
		pl1 = new ArrayList<>();
		pl2 = new ArrayList<>();
		for (int i = 0; i < 2 * row * col; i++) {
			pl1.add(new ArrayList<>());
			pl2.add(new ArrayList<>());
		}
		buckets = new ArrayList<>();
		for (int i = 0; i < 2 * 8 * 8 * Board.MAX_MOVE * Board.MAX_MOVE; i++)
			buckets.add(new ArrayList<>());
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
					int eval = histAlphaBeta(new Node(board.getSurfers().clone(), moves[i][j]), getSide(), ht, -10000,
							10000);
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
			evaluations++;
			return evaluate(node.getPositions(), node.getSurferPostitions(), side);
		}
		long[][] moves = getPossibleMoves(node.getPositions(), node.getSurferPostitions(), side);
		int size = possibleMoveCount(moves);
		if (size == 0) {
			evaluations++;
			return evaluate(node.getPositions(), node.getSurferPostitions(), side);
		}
		List<KillerMove> mv = movesToList(moves, node, side);
		int temp = 0;
		int counter = 0;
		for (int i = 0; i < mv.size(); i++) {
			counter++;
			temp = -histAlphaBeta(new Node(mv.get(i).getSurfers(), mv.get(i).getBoard()), -side, height - 1, -hope,
					-achievable);
			if (temp >= hope) {
				if (side >= 0) {
					for (HistoryStruct h : pl1.get(mv.get(i).getChangedIndex())) {
						if (h.getPositions() == mv.get(i).getBoard())
							h.setCutoffs(size - counter);
					}
				} else {
					for (HistoryStruct h : pl2.get(mv.get(i).getChangedIndex())) {
						if (h.getPositions() == mv.get(i).getBoard())
							h.setCutoffs(size - counter);
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
		List<List<HistoryStruct>> checking = pl1;
		int i1 = 0;
		int i2 = 1;
		if (side < 0) {
			i1 = 2;
			i2 = 3;
			checking = pl2;
		}
		for (int i = 0; i < moves.length; i++) {
			if (moves[i] != null) {
				List<HistoryStruct> l = checking.get(i);
				int[] sPos = node.getSurferPostitions().clone();
				if (i < row * col)
					sPos[i1] = i;
				else
					sPos[i2] = i % (row * col);
				for (int j = 0; j < moves[i].length; j++) {
					boolean flag = false;
					for (HistoryStruct h : l) {
						if (h.getPositions() == moves[i][j]) {
							buckets.get(h.getCutoffs()).add(new KillerMove(h.getCutoffs(), sPos, h.getPositions(), i));
							flag = true;
							break;
						}

					}
					if (!flag) {
						checking.get(i).add(new HistoryStruct(moves[i][j], 0));
						buckets.get(0).add(new KillerMove(0, sPos, moves[i][j], i));
					}
				}
			}
		}
		for (int i = buckets.size() - 1; i >= 0; i--) {
			if (buckets.get(i) != null) {
				int j = buckets.get(i).size() - 1;
				while (!buckets.get(i).isEmpty()) {
					list.add(buckets.get(i).remove(j));
					j = buckets.get(i).size() - 1;
				}
			}
		}
		return list;
	}
}
