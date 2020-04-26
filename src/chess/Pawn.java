package chess;

import java.util.ArrayList;

public class Pawn extends Piece {

    public Pawn(Colour colour) {
        super(colour);

        FEN = colour == Colour.WHITE ? "P" : "p";
        
        value = 10;
        
        imageIndex = colour == Colour.WHITE ? 5 : 13;
    }
    
    public Pawn(Pawn pawn) {
    	this (pawn.getColour());
    	moved = pawn.isMoved();
    }
    
    @Override
    public Pawn clone() {
    	return new Pawn(this);
    }

    public static ArrayList<Integer> addAttackMoves(int index, Board board) {
        Pawn pawn = (Pawn) board.getPiece(index);

        ArrayList<Integer> moveMap = pawn.getMoves(index, board);
        ArrayList<Integer> attackMap = pawn.getAttacks(index, board);
        
        for (int i : attackMap) {
            if (Utility.getMove(i).equals(board.getEnPassant())) {
                moveMap.add(i);
            }
            if (board.getPiece(i).isEmpty()) {
                continue;
            }
            if (!moveMap.contains(i)) {
                moveMap.add(i);
            }
        }

        return Mapping.makeFriendly(index, board.getPieces(), moveMap);
    }

	@Override
	public ArrayList<Integer> getMoves(int index, Board board) {
		ArrayList<Integer> moveMap = new ArrayList<Integer>();

		int move = board.getPiece(index).getColour() == Colour.WHITE ? -8 : 8;
		int bottomRow = board.getPiece(index).getColour() == Colour.WHITE ? 6 : 1;

		if (Utility.getRow(index) != 0) {
			if (board.getPiece(index + move).isEmpty()) {
				moveMap.add(index + move);
				if (Utility.getRow(index) == bottomRow) {
					if (board.getPiece(index + (move * 2)).isEmpty()) {
						moveMap.add(index + (move * 2));
					}
				}
			}
		}

		return moveMap;
	}
	
	@Override
	public ArrayList<Integer> getAttacks(int index, Board board) {
		ArrayList<Integer> moveMap = new ArrayList<Integer>();

        int move = board.getPiece(index).getColour() == Colour.WHITE ? -8 : 8;
        
        if (index + move - 1 >= 0) {
            if (Utility.getCol(index) != 0) {
                moveMap.add(index + move - 1);
            }

        }
        if (index + move + 1 >= 0) {
            if (Utility.getCol(index) != 7) {
                moveMap.add(index + move + 1);
            }

        }

        return moveMap;
	}
}
