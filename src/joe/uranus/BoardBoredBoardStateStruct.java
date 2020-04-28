package joe.uranus;

import chess.Board;

public class BoardBoredBoardStateStruct {

	public Board board;
	public BoardState parent;
	public BoardBoredBoardStateStruct(Board board, BoardState parent) {
		super();
		this.board = board;
		this.parent = parent;
	}
	
	
}
