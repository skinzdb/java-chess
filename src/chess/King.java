package chess;

import java.util.ArrayList;

public class King extends Piece {

	public King(Colour colour) {
		super(colour);
		
		FEN = colour == Colour.WHITE ? "K" : "k";
		
		value = 0;
		
		imageIndex = colour == Colour.WHITE ? 4 : 12;
	}

	public static ArrayList<Integer> addCastleMoves(int index, Board board, ArrayList<Integer> moveMap) {
		int castleInfo = board.getCastleInfo();
		Colour colour = board.getPiece(index).getColour();
		
		if (castleInfo == 0 || Detection.checkThreat(index, board, colour)) 
			return moveMap;

		boolean canQueenside, canKingside; 

		if (colour == Colour.WHITE) {
			canKingside = (castleInfo & 0b1000) == 0b1000;
			canQueenside = (castleInfo & 0b0100) == 0b0100;
		} else {
			canKingside = (castleInfo & 0b0010) == 0b0010;
			canQueenside = (castleInfo & 0b0001) == 0b0001;
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
