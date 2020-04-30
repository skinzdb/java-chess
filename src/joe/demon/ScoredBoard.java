package joe.demon;

import chess.Board;

public class ScoredBoard {

	public Board board;
	public int score;
	
	public ScoredBoard(Board board, int score) {
		super();
		this.board = board;
		this.score = score;
	}
}
