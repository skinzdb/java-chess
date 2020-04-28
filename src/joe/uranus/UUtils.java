package joe.uranus;

import java.util.ArrayList;

import chess.Board;
import chess.Colour;
import chess.Move;

public class UUtils {

	//+good white
	//-good black
	
	public static int evalBoard(Board b) {
		int val = b.getBoardValue(Colour.WHITE)*5;
		Colour boardCol = b.getColour();
		b.setColour(Colour.WHITE);
		b.checkAll();
		if(b.isCheckmate()) {
			val-=100000;
		}
		ArrayList<Move> moves = b.getPossibleMoves();
		for(Move move : moves) {
			val+= b.getPiece(move.to).getValue();
		}
		b.setEnPassant(-1);
		b.setColour(Colour.BLACK);
		b.checkAll();
		if(b.isCheckmate()) {
			val+=100000;
		}
		moves = b.getPossibleMoves();
		for(Move move : moves) {
			val-= b.getPiece(move.to).getValue();
		}
		b.setColour(boardCol);
		return val;
	}
	
}
