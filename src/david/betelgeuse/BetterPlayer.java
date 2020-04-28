package david.betelgeuse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import chess.Board;
import chess.Colour;
import chess.Move;
import chess.Player;

public class BetterPlayer extends Player {

	int bestVal;
	int bestMove;
	int checkBonus;
	int checkmateBonus;
	
	@Override
	protected void process() {
		Random rand = new Random();
		ArrayList<Integer> bestMoves = new ArrayList<>();
		ArrayList<Integer> bestVals = new ArrayList<>();
		
		bestVal = -99999;
		bestMove = -1;
		
		Colour colour = board.getColour();
		ArrayList<Integer> values = new ArrayList<>();
		if (colour == Colour.BLACK) 
			bestVal = -bestVal;
		
		int counter = 0;
		for (Move m : moves) {
			Board tmpBoard = board.move(m);
			tmpBoard.setupNextMove();
			int val = tmpBoard.getBoardValue();
			
			if (tmpBoard.isCheckmate()) {
				setChosenMove(counter);
				return;
			}
			
			if ((colour == Colour.WHITE) == (val > bestVal)) {
				bestVal = val;
				bestMoves.add(counter);
				bestMove = counter;
			}
			values.add(val);
			counter++;
		}
		
		if (bestMoves.size() > 3) {
			int bM = 0;
			do {
				bM = bestMoves.get(rand.nextInt(bestMoves.size()));
			} while (Math.abs(bestVals.get(bestMoves.indexOf(bM)) - bestVals.get(bestMoves.indexOf(bestMove))) < 20);

		}
		
		setChosenMove(bestMove);
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
	
	private int minimax(Move move, Board board, int depth) {
		if (depth == 0) return board.getBoardValue(); 
		
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
	*/

}
