package david.betelgeuse;

import java.util.ArrayList;
import java.util.Random;

import chess.Bishop;
import chess.Board;
import chess.Colour;
import chess.King;
import chess.Knight;
import chess.Move;
import chess.Pawn;
import chess.Piece;
import chess.Player;
import chess.Utility;

public class BetterPlayer extends Player {

	private int depth;
	private int bestVal;
	private int bestMove;
	
	private boolean isWhite;
	
	private int lastMovedTo;
	private int moveCounter;
	
	private Random rand = new Random();
	
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
			  -4, -9, -2, -2, -2, -2, -9, -4
			};
	
	private int[] bKnightWeights;
	
	private int[] wPawnWeights = new int[]
			{  0,  0,  0,  0,  0,  0,  0,  0,
			   5,  5,  5,  5,  5,  5,  5,  5,
			   1,  1,  2,  3,  3,  2,  1,  1,
		       0,  0,  1,  2,  2,  1,  0,  0,
			   0,  0,  1,  1,  1,  1,  0,  0,
			   0, -1, -1,  0,  0, -1, -1,  0,
			   0,  1,  1, -3, -3,  1,  1,  0,
			   0,  0,  0,  0,  0,  0,  0,  0,	
			};
	
	private int[] bPawnWeights;
	
	private int[] bishopWeights = new int[]
			{ -2, -1, -1, -1, -1, -1, -1, -2,
			  -1,  0,  0,  0,  0,  0,  0, -1,
			  -1,  1,  1,  1,  1,  1,  1, -1,
		      -1,  0,  0,  1,  1,  0,  0, -1,
			  -1,  0,  0,  1,  1,  0,  0, -1,
			  -1,  1,  1,  1,  1,  1,  1, -1,
			  -1,  0,  0,  0,  0,  0,  0, -1,
			  -2, -1, -1, -1, -1, -1, -1, -2
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
		moveCounter = 0;
		lastMovedTo = -1;
		bKingWeights = reverse(wKingWeights, 64);
		bKnightWeights = reverse(wKnightWeights, 64);
		bPawnWeights = reverse(wPawnWeights, 64);
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
			int val = minimax(tmpBoard, !isWhite, -10000, 10000, depth) + more_eval(board, m, isWhite);
			
			if ((val > bestVal) == isWhite) {
				bestVal = val;
				bestMove = counter;
			}
			values.add(val);
			counter++;
			
		}
		moveCounter++;
		if (bestMove != -1)
			lastMovedTo = moves.get(bestMove).to;
		
		System.out.println("DStats: " + getStatus(bestVal));
		
		setChosenMove(bestMove);
	}

	private int minimax(Board bd, boolean isWhite, int a, int b, int depth) {
		if (depth == 0) return boardEval(bd);
		
		bd.setupNextMove();
		
		int val = isWhite ? -9999 : 9999;
		
		for (Move m : bd.getPossibleMoves()) {
			Board newBoard = bd.move(m);
			int tmpVal = minimax(newBoard, !isWhite, a, b, depth - 1) + more_eval(bd, m, isWhite);
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
			avalue += 1 + (w ? wKingWeights[i] : bKingWeights[i]);
		} else if (p instanceof Knight) {
			avalue += (w ? wKnightWeights[i] : bKnightWeights[i]);
		} else if (p instanceof Pawn) {
			avalue += (w ? wPawnWeights[i] : bPawnWeights[i]);
		} else if (p instanceof Bishop) {
			avalue += bishopWeights[i];
		}
		
		if (bd.isCheckmate()) avalue += 6969420;
		if (bd.isCheck()) avalue += 7;
		
		return w ? avalue : -avalue;
	}
	
	private int more_eval(Board bd, Move m, boolean w) {
		int val = 0;
		int from = m.from;
		int to = m.to;
		Piece p = board.getPiece(from);
		
		if (moveCounter < 4) {		// opening
			if (lastMovedTo == m.from) val -= 10;	//discourage moving same piece twice at start of the game
		} else if (moveCounter > 4 && moveCounter < 15) {	// middlegame
			if (p instanceof Pawn) {
				if (!fileContainsPawn(bd, Utility.getCol(to - 1), p.getColour()) && !fileContainsPawn(bd, Utility.getCol(to + 1), p.getColour())) {	// discourage isolated pawns
					val -= 5;
				}
			}
		} else {
			
		}
		if (lastMovedTo == m.from) val -= rand.nextInt(2);

		return w ? val : -val;
		
	}
	
	private String getStatus(int myval) {
		if (myval < -80) {
			return ":(";
		} else if (myval < -50) {
			return "Ah shit not doing too well here.... ";
		} else if (myval < -30) {
			return "A minor setback, that is to be accepted";
		} else if (myval < 5) {
			return "Eh.";
		} else if (myval < 30) {
			return "Not baaaad, not baaaaaaaad";
		} else if (myval < 50) {
			return "fuckin owning";
		} else if (myval < 70) {
			return "Orange man bad, also fucking owning";
		} else if (myval < 90) {
			return "According to all known laws of aviation";
		} else {
			return "GOD MODE GODE MODE";
		}
	}
	
	private boolean fileContainsPawn(Board bd, int file, Colour c) {
		if (file < 0 || file > 7) return false;
		for (int i = file; i < 56 + file; i += 8) {
			if (bd.getPiece(i) instanceof Pawn && bd.getPiece(i).getColour() == c) {
				return true;
			}
		}
		return false;
	}

}
