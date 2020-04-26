package chess;

import java.util.ArrayList;

public abstract class Player implements Runnable {
	
	private volatile int chosenMove;
	
	protected Board board;
	
	protected ArrayList<Move> moves;
	
	public void run() {
		process();
	}
	
	protected abstract void process();

	public void makeMove(Board board, ArrayList<Move> moves) {
		chosenMove = -1;
		
		this.board = board;
		for (Move move : moves) {
			this.moves.add(move);
		}
	}

	public boolean isChoosing() {
		return chosenMove == -1;
	}

	public int getChosenMove() {
		return chosenMove;
	}
	
	public void setChosenMove(int index) {
		chosenMove = index;
	}
}
