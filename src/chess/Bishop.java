package chess;

import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(Colour colour) {
        super(colour);

        FEN = colour == Colour.WHITE ? "B" : "b";
        
        value = 30;
        
		imageIndex = colour == Colour.WHITE ? 2 : 10;
    }
    
	@Override
	public ArrayList<Integer> getMoves(int index, Board board) {
		return Mapping.getDiagonalMoves(index, board.getPieces());
	}
}

