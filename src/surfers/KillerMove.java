// Alicja Przybys, nr 18204233
package surfers;

import lombok.Getter;
import lombok.Setter;

//I'm using lombok to generate my getters and setters for this class
@Getter
@Setter
// class keeping the moves with their numbers of cutoffs
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

}
