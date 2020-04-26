package chess;

import java.util.ArrayList;

public abstract class Player implements Runnable {
	
	private volatile int chosenMove;
	
	protected Board board;
	
	protected ArrayList<Move> moves;
	
	public void run() {
		init();

		while (true) {
			if (chosenMove == -1) {
				process();
				break;
			}
		}
	}
	
	protected abstract void init();
	
	protected abstract void process();

	public void makeMove(Board board, ArrayList<Move> moves) {
		chosenMove = -1;
		
		this.board = board;
		this.moves = moves;
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
