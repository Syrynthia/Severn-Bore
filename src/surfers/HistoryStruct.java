package surfers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryStruct {
	long positions;
	int cutoffs;
	
	public HistoryStruct(long positions, int cutoffs) {
		this.positions = positions;
		this.cutoffs = cutoffs;
	}
}
