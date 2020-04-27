package chess;

import java.util.ArrayList;

public abstract class Player implements Runnable {
	
	private volatile boolean running = true;
	
	private volatile int chosenMove = -1;
	
	private volatile int selectedSquare = -1;
	
	protected Board board;
	
	protected ArrayList<Move> moves = new ArrayList<>();
	
	public void run() {
		process();
	}
	
	protected abstract void process();

	public void makeMove(Board board, ArrayList<Move> moves) {
		chosenMove = -1;
		selectedSquare = -1;
		
		this.board = board;
		this.moves.clear();
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
	
	public int getSelectedSquare() {
		return selectedSquare;
	}
	
	public void setSelectedSquare(int index) {
		this.selectedSquare = index;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void stop() {
		running = false;
	}
}
