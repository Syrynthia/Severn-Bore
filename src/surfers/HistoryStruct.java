// Alicja Przybys, nr 18204233
package surfers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// keeps just the board situation and the number of cuttoffs
// in the list its index is the new position of the surfer
// this move was made for
public class HistoryStruct {
	long positions;
	int cutoffs;
	
	public HistoryStruct(long positions, int cutoffs) {
		this.positions = positions;
		this.cutoffs = cutoffs;
	}
}
