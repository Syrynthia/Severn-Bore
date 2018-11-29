package surfers;

import lombok.Data;

//I'm using lombok to generate my getters and setters for this class
//the @Data generates those getters and setters
@Data
public class Surfer {
	private int player;
	private int x = -1;
	private int y = -1;
	
	public Surfer(int player, int x, int y) {
		this.player = player;
		this.x = x;
		this.y = y;
	}
	public Surfer(int player) {
		this.player = player;
	}
}
