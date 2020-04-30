package david.betelgeuse;

import java.util.ArrayList;

import chess.Board;
import chess.Colour;
import chess.King;
import chess.Knight;
import chess.Move;
import chess.Piece;
import chess.Player;

public class BetterPlayer extends Player {

	private int depth;
	private int bestVal;
	private int bestMove;
	
	private boolean isWhite;
	
	private int[] wKingWeights = new int[] 
			{ -2, -2, -3, -4, -4, -3, -2, -2,
			  -2, -2, -3, -4, -4, -3, -2, -2,
			  -2, -2, -3, -4, -4, -3, -2, -2,
			  -2, -2, -3, -4, -4, -3, -2, -2,
			  -2, -2, -3, -3, -3, -3, -2, -2,
			  -1, -1, -3, -2, -2, -3, -1, -1,
			   1,  1,  0,  0,  0,  0,  1,  1,
			   2,  2,  1,  0,  0,  1,  2,  2
			  };
	
	private int[] bKingWeights;
	
	private int[] wKnightWeights = new int[]
			{ -4, -3, -2, -2, -2, -2, -3, -4,
			  -3, -2,  0, -1, -1,  0, -2, -2,
			  -2,  0,  0,  0,  0,  0,  0, -2,
		      -2,  0,  1,  1,  1,  1,  0, -2,
			  -2,  0,  1,  1,  1,  1,  0, -2,
			  -2,  0,  0,  0,  0,  0,  0, -2,
			  -3, -2,  0,  0,  0,  0, -2, -3,
			  -4, -3, -2, -2, -2, -2, -3, -4
			};
	
	private int[] bKnightWeights;
	
	private int[] wPawnWeights = new int[]
			{
					
					
					
			};

	private int[] reverse(int a[], int n) {
		int[] b = new int[n];
		int j = n;
		for (int i = 0; i < n; i++) {
			b[j - 1] = a[i];
			j = j - 1;
		}

		return b;
	}
	
	public BetterPlayer(int depth) {
		this.depth = depth;
		bKingWeights = reverse(wKingWeights, 64);
		bKnightWeights = reverse(wKnightWeights, 64);
	}
	
	@Override
	protected void process() {
		isWhite = board.getColour() == Colour.WHITE;
		
		bestVal = isWhite ? -10000 : 10000;
		bestMove = -1;
	
		ArrayList<Integer> values = new ArrayList<>();
		int counter = 0;
		for (Move m : moves) {
			Board tmpBoard = board.move(m);
			int val = minimax(tmpBoard, !isWhite, -10000, 10000, depth);
			
			if ((val > bestVal) == isWhite) {
				bestVal = val;
				bestMove = counter;
			}
			values.add(val);
			counter++;
		}
		
		setChosenMove(bestMove);
	}

	private int minimax(Board bd, boolean isWhite, int a, int b, int depth) {
		if (depth == 0) return boardEval(bd);
		
		bd.setupNextMove();
		
		int val = isWhite ? -9999 : 9999;
		
		for (Move m : bd.getPossibleMoves()) {
			Board newBoard = bd.move(m);
			int tmpVal = minimax(newBoard, !isWhite, a, b, depth - 1);
			if (isWhite) {
				val = Math.max(val, tmpVal);
				a = Math.max(a, val);
			} else {
				val = Math.min(val, tmpVal);
				b = Math.min(b, val);
			}
			if (b <= a) break;
		}

		return val;
	}
	
	private int boardEval(Board bd) {
		int total = 0;
		for (int i = 0; i < 64; i++) {
			total += eval(bd, i);
		}
		return total;
	}
	
	private int eval(Board bd, int i) {
		Piece p = bd.getPiece(i);
		
		if (p.isEmpty()) return 0;
		
		boolean w = p.getColour() == Colour.WHITE;
		int avalue = p.getValue();
		
		if (p instanceof King) {
			avalue *= 5 + (w ? wKingWeights[i] : bKingWeights[i]);
		} else if (p instanceof Knight) {
			avalue += (w ? wKnightWeights[i] : bKnightWeights[i]);
		} 
		
		if (bd.isCheckmate()) avalue += 1000;
		if (bd.isCheck()) avalue += 5;
		
		return w ? avalue : -avalue;
	}

}
