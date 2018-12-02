package surfers;

import lombok.Data;

//I'm using lombok to generate my getters and setters for this class
//the @Data generates those getters and setters
@Data
public class KillerMove {
	private int cutoffs;
	private int[] surfers;
	private long board;

	public KillerMove(int cutoffs, int[] surfers, long board) {
		this.cutoffs = cutoffs;
		this.board = board;
		this.surfers = surfers;
	}
}
