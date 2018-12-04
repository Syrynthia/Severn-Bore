package surfers;

import lombok.Getter;
import lombok.Setter;

//I'm using lombok to generate my getters and setters for this class
@Getter
@Setter
public class KillerMove {
	private int cutoffs;
	private int[] surfers;
	private long board;
	private int changedIndex = 0;

	public KillerMove(int cutoffs, int[] surfers, long board) {
		this.cutoffs = cutoffs;
		this.board = board;
		this.surfers = surfers;
	}	
	
	public KillerMove(int cutoffs, int[] surfers, long board, int changedIndex) {
		this.cutoffs = cutoffs;
		this.board = board;
		this.surfers = surfers;
		this.changedIndex = changedIndex;
	}

	// overriding the equals() method for the contains() method I'm using
	// in the history heuristic
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof KillerMove))
			return false;
		KillerMove o = (KillerMove) obj;
		if (o.getBoard() != board)
			return false;
		for (int i = 0; i < surfers.length; i++)
			if (surfers[i] != o.getSurfers()[i])
				return false;
		return true;
	}
}
