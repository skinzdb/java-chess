package chess;

public class Move {

	public int from;
	public int to;
	
	public Move(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	@Override
	public boolean equals(Object o) {
		Move move = (Move) o;
		return move.from == from && move.to == to;
	}
}
