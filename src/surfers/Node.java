package surfers;
import lombok.Data;

//I'm using lombok to generate my getters and setters for this class
//the @Data generates those getters and setters
@Data
public class Node {
	private int[] surferPostitions;
	private long positions;

	public Node(int[] surferPositions, long positions) {
		this.surferPostitions = surferPositions;
		this.positions = positions;
	}

}
