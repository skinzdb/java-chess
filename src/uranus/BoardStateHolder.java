package uranus;

import chess.Bishop;
import chess.Board;
import chess.Colour;
import chess.King;
import chess.Knight;
import chess.Pawn;
import chess.Piece;
import chess.Queen;
import chess.Rook;

public class BoardStateHolder {
	public long pieceMap;
	public long[] piecesStore = new long[2];
	public int extraState;
	
	public BoardStateHolder(Board b) {
		int pieceCountCounter = 0;
		Piece pieces[] = b.getPieces();
		for(int i = 0; i < 64; i++) {
			if(!pieces[i].isEmpty()) {
				pieceMap |= 1<<i;
				int pieceId = -1;
				if(pieces[i] instanceof Pawn) {
					pieceId = 0;
				}
				else if(pieces[i] instanceof Rook) {
					pieceId = 1;
				}
				else if(pieces[i] instanceof Knight) {
					pieceId = 2;
				}
				else if(pieces[i] instanceof Bishop) {
					pieceId = 3;
				}
				else if(pieces[i] instanceof Queen) {
					pieceId = 4;
				}
				else if(pieces[i] instanceof King) {
					pieceId = 5;
				}
				if(pieces[i].getColour() == Colour.WHITE) {
					pieceId+=8;
				}
				piecesStore[pieceCountCounter/16] |= pieceId<<((pieceCountCounter%16)*4);
			}
		}
<<<<<<< HEAD
		//b.get
		//extraState
=======
		b.get
		extraState
>>>>>>> bc7dce1... progress
		
	}
}
