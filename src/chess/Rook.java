package chess;

import java.util.ArrayList;

public class Rook extends Piece {

    public Rook(Colour colour) {
        super(colour);

        FEN = colour == Colour.WHITE ? "R" : "r";
        
        value = 50;
        
        imageIndex = colour == Colour.WHITE ? 6 : 14;
    }
    
	@Override
	public ArrayList<Integer> getMoves(int index, Board board) {
		 return Mapping.getStraightMoves(index, board.getPieces());
	}
}
