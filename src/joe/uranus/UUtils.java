package joe.uranus;

import java.util.ArrayList;

import chess.Board;
import chess.Colour;
import chess.Move;
import chess.Piece;

public class UUtils {

	//+good white
	//-good black
	private static final int[] KING_MASK = {1,1, 1,0, 1,-1,
											0,1, 0,-1,
											-1,1, -1,0, -1,-1}; 
	
	private static final int[] WPAWN_MASK = {1,1, -1,1}; 
	
	private static final int[] BPAWN_MASK = {1,-1, -1,-1}; 
	
	private static final int[] KNIGHT_MASK = {2,1, 2,-1, 1,2, 1,-2, -2,1, -2,-1, -1,2, -1,-2};
	
	private static final int[] ROOK_MASK = {0,1, 0,2, 0,3, 0,4, 0,5, 0,6, 0,7,
											1,0, 2,0, 3,0, 4,0, 5,0, 6,0, 7,0,
											0,-1, 0,-2, 0,-3, 0,-4, 0,-5, 0,-6, 0,-7,
											-1,0, -2,0, -3,0, -4,0, -5,0, -6,0, -7,0};
	
	private static final int[] BISHOP_MASK = {1,1, 2,2, 3,3, 4,4, 5,5, 6,6, 7,7,
											  1,-1, 2,-2, 3,-3, 4,-4, 5,-5, 6,-6, 7,-7,
											  -1,-1, -2,-2, -3,-3, -4,-4, -5,-5, -6,-6, -7,-7,
											  -1,1, -2,2, -3,3, -4,4, -5,5, -6,6, -7,7};
	
	private static final int[] QUEEN_MASK = {1,1, 2,2, 3,3, 4,4, 5,5, 6,6, 7,7,
											 1,-1, 2,-2, 3,-3, 4,-4, 5,-5, 6,-6, 7,-7,
											 -1,-1, -2,-2, -3,-3, -4,-4, -5,-5, -6,-6, -7,-7,
											 -1,1, -2,2, -3,3, -4,4, -5,5, -6,6, -7,7,
											 0,1, 0,2, 0,3, 0,4, 0,5, 0,6, 0,7,
											 1,0, 2,0, 3,0, 4,0, 5,0, 6,0, 7,0,
											 0,-1, 0,-2, 0,-3, 0,-4, 0,-5, 0,-6, 0,-7,
											 -1,0, -2,0, -3,0, -4,0, -5,0, -6,0, -7,0};
	
	private static final int [][] MASK_MAP = {{}, KNIGHT_MASK, BISHOP_MASK, QUEEN_MASK, KING_MASK, WPAWN_MASK, ROOK_MASK, {},
											  {}, KNIGHT_MASK, BISHOP_MASK, QUEEN_MASK, KING_MASK, BPAWN_MASK, ROOK_MASK, {}};
	
	public static int evalBoard(Board b) {
		int val = b.getBoardValue(Colour.WHITE)*5;
		Colour boardCol = b.getColour();

		int posCounter = 0;
		for(Piece p : b.getPieces()) {
			int xPos = posCounter&7;
			int yPos = posCounter>>3;
			int[] mask = MASK_MAP[p.getImageIndex()];
			for(int i = 0; i < mask.length; i+=2) {
				int x = xPos+ mask[i];
				int y = yPos+ mask[i+1];
				if(x>=0 && x<8 && y>=0 && y<8) {
					if(p.getColour() == Colour.WHITE) {
						val+=b.getPieces()[x+(y<<3)].getValue();
					}
					else {
						val-=b.getPieces()[x+(y<<3)].getValue();
					}
				}
			}
			posCounter++;
		}
		
		b.setColour(Colour.WHITE);
		b.checkAll();
		if(b.isCheckmate()) {
			val-=100000;
		}
		ArrayList<Move> moves = b.getPossibleMoves();
		val+=moves.size();
		b.setEnPassant(-1);
		
		b.setColour(Colour.BLACK);
		b.checkAll();
		if(b.isCheckmate()) {
			val+=100000;
		}
		moves = b.getPossibleMoves();
		val-=moves.size();
		b.setColour(boardCol);
		return val;
	}
	
}