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
	
	private static final int[] BLACK_BONUS_MASK = {3,3,3,3,3,3,3,3,
												   3,3,3,3,3,3,3,3,
												   3,3,4,4,4,4,3,3,
												   3,4,4,5,5,4,4,3,
												   4,5,5,6,6,5,5,4,
												   5,5,6,6,6,6,5,5,
												   5,5,5,5,5,5,5,5,
												   5,5,5,5,5,5,5,5};
	
	private static final int[] WHITE_BONUS_MASK = {5,5,5,5,5,5,5,5,
												   5,5,5,5,5,5,5,5,
												   5,5,6,6,6,6,5,5,
												   4,5,5,6,6,5,5,4,
												   3,4,4,5,5,4,4,3,
												   3,3,4,4,4,4,3,3,
												   3,3,3,3,3,3,3,3,
												   3,3,3,3,3,3,3,3};
	
	public static int evalBoard(Board b) {
		int val = b.getBoardValue(Colour.WHITE)*25;
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
						val+=b.getPieces()[x+(y<<3)].getValue()*1*(b.getPieces()[x+(y<<3)].getColour()==Colour.WHITE?1:4) + WHITE_BONUS_MASK[x+(y<<3)]*5;
					}
					else {
						val-=b.getPieces()[x+(y<<3)].getValue()*1*(b.getPieces()[x+(y<<3)].getColour()==Colour.BLACK?1:4) + BLACK_BONUS_MASK[x+(y<<3)]*5;
					}
				}
			}
			posCounter++;
		}
		
		b.setColour(Colour.WHITE);
		b.checkAll();
		if(b.isCheckmate()) {
			//System.out.println("CheckMate w");
			val-=1000000;
		}
		//ArrayList<Move> moves = b.getPossibleMoves();
		//val+=moves.size()*5;
		b.setEnPassant(-1);
		
		b.setColour(Colour.BLACK);
		b.checkAll();
		if(b.isCheckmate()) {
			//System.out.println("CheckMate b");
			val+=10000000;
		}
		//moves = b.getPossibleMoves();
		//val-=moves.size()*5;
		b.setColour(boardCol);
		return val;
	}
	
}
