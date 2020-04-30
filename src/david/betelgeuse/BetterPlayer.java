package david.betelgeuse;

import java.util.ArrayList;
import java.util.Random;

import chess.Board;
import chess.Colour;
import chess.Move;
import chess.Player;

public class BetterPlayer extends Player {

	private int bestVal;
	private int bestMove;
	private int checkBonus;
	
	@Override
	protected void process() {
		Random rand = new Random();
		ArrayList<Integer> bestMoves = new ArrayList<>();
		ArrayList<Integer> bestVals = new ArrayList<>();
		
		Colour colour = board.getColour();
		
		checkBonus = 25;
		bestVal = colour == Colour.WHITE ? -10000 : 10000;
		bestMove = -1;
	
		ArrayList<Integer> values = new ArrayList<>();
		int counter = 0;
		for (Move m : moves) {
			Board tmpBoard = board.move(m);
			tmpBoard.setupNextMove();
			int val = minimax(m, board, 4);
			
			if (tmpBoard.isCheck()) {
				val += colour == Colour.WHITE ? checkBonus : -checkBonus;
			}
			
			if (tmpBoard.isCheckmate()) {
				setChosenMove(counter);
				return;
			}
			tmpBoard.swapColour();
			if (tmpBoard.isCheckmate()) {
				if (colour == Colour.WHITE) 
					val -= 100000;
				else
					val += 100000;
			}
			tmpBoard.swapColour();
			
			if ((val > bestVal) == (colour == Colour.WHITE)) {
				bestVal = val;
				bestMoves.add(counter);
				bestVals.add(val);
				bestMove = counter;
			}
			values.add(val);
			counter++;
		}
		
		int bM = bestMove;
		if (bestMoves.size() > 3) {
			do {
				bM = bestMoves.get(rand.nextInt(bestMoves.size()));
			} while (Math.abs(bestVals.get(bestMoves.indexOf(bM)) - bestVals.get(bestVals.size() - 1)) > 10);

		}
		
		setChosenMove(bM);
	}
	
	/*
	@Override
	protected void process() {
		Collections.shuffle(moves);
		colour = board.getColour();
		
		bestVal = colour == Colour.WHITE ? -10000 : 10000;
		
		for (int i = 0; i < moves.size(); i++) {
			int val = minimax(moves.get(i), board, 3);

			System.out.println(val);
			
			if (colour == Colour.WHITE) {
				if (val > bestVal) {
					bestMove = i;
					bestVal = val;
				}
			} else {
				if (val < bestVal) {
					bestMove = i;
					bestVal = val;
				}
			}
		}
		
		setChosenMove(bestMove);
	}
	*/
	private int minimax(Move move, Board board, int depth) {
		if (depth == 0) return board.getBoardValue(Colour.WHITE); 
		
		Board tmpBoard = board.move(move);
		tmpBoard.setupNextMove();
		
		int val = tmpBoard.getColour() == Colour.WHITE ? -10000 : 10000;
		
		for (Move m : tmpBoard.getPossibleMoves()) {
			int tmpVal = minimax(m, tmpBoard, depth - 1);
			if (tmpBoard.getColour() == Colour.WHITE) {
				val = Math.max(val, tmpVal);
			} else {
				val = Math.min(val, tmpVal);
			}
		}

		return val;
	}

}
