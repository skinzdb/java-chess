package chess;

import java.util.ArrayList;

public class King extends Piece {

	public King(Colour colour) {
		super(colour);
		
		FEN = colour == Colour.WHITE ? "K" : "k";
		
		value = 900;
		
		imageIndex = colour == Colour.WHITE ? 4 : 12;
	}
	
    public King(King king) {
    	this (king.getColour());
    	moved = king.isMoved();
    }
    
    @Override
    public King clone() {
    	return new King(this);
    }

	public static ArrayList<Integer> addCastleMoves(int index, Board board, ArrayList<Integer> moveMap) {
		if (board.getPiece(index).isMoved())
			return moveMap;

		Colour colour = board.getPiece(index).getColour();

		if (Detection.checkThreat(index, board, colour))
			return moveMap;

		String castleInfo = board.getCastleInfo();
		boolean canQueenside, canKingside; 

		if (colour == Colour.WHITE) {
			canQueenside = castleInfo.contains("Q");
			canKingside = castleInfo.contains("K");
		} else {
			canQueenside = castleInfo.contains("q");
			canKingside = castleInfo.contains("k");
		}
		
		int queenside = Utility.getRow(index) * 8;
		int kingside = queenside + 7;

		for (int i = queenside + 1; i < index; i++) {
			if (!board.getPiece(i).isEmpty() || Detection.checkThreat(i, board, colour)) {
				canQueenside = false;
				break;
			}
		}
		
		for (int j = index + 1; j < kingside; j++) {
			if (!board.getPiece(j).isEmpty() || Detection.checkThreat(j, board, colour)) {
				canKingside = false;
				break;
			}
		}

		if (canQueenside) {
			moveMap.add(index - 2);
		}
		if (canKingside) {
			moveMap.add(index + 2);
		}

		return moveMap;
	}

	@Override
	public ArrayList<Integer> getMoves(int index, Board board) {
		ArrayList<Integer> moveMap = new ArrayList<Integer>();

		int[] possibleMoves = new int[] {-9, -8, -7, -1, 1, 7, 8, 9};
		
		for (int i : possibleMoves) {
			int j = index + i;
			if (j < 0 || j > 63)
				continue;
			if(Math.abs(Utility.getCol(j) - Utility.getCol(index)) < 2 && Math.abs(Utility.getRow(j) - Utility.getRow(index)) < 2) {
				moveMap.add(j);
			}
		}
		
		return moveMap;
	}
}
