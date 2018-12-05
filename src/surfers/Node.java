// Alicja Przybys, nr 18204233
package surfers;
import lombok.Data;

//I'm using lombok to generate my getters and setters for this class
//the @Data generates those getters and setters
@Data
// a wrapper class for the alphabeta algorithm, keeping the surfer positions and the board
public class Node {
	private int[] surferPostitions;
	private long positions;

	public Node(int[] surferPositions, long positions) {
		this.surferPostitions = surferPositions;
		this.positions = positions;
	}

}
