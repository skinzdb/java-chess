package chess;

import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(Colour colour) {
        super(colour);
        
        FEN = colour == Colour.WHITE ? "Q" : "q";

        value = 90;
        
        imageIndex = colour == Colour.WHITE ? 3 : 11;
    }
    
    public Queen(Queen queen) {
    	this (queen.getColour());
    	moved = queen.isMoved();
    }
    
    @Override
    public Queen clone() {
    	return new Queen(this);
    }

	@Override
	public ArrayList<Integer> getMoves(int index, Board board) {
		ArrayList<Integer> moves = Mapping.getStraightMoves(index, board.getPieces());
        ArrayList<Integer> dMoves = Mapping.getDiagonalMoves(index, board.getPieces());

        for (int i : dMoves) {
            if (!moves.contains(i)) {
                moves.add(i);
            }
        }

        return moves;
	}
}
