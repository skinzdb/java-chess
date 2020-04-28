package joe.uranus;

import java.util.ArrayList;
import java.util.HashSet;

import chess.Bishop;
import chess.Board;
import chess.Colour;
import chess.King;
import chess.Knight;
import chess.Pawn;
import chess.Piece;
import chess.Queen;
import chess.Rook;
import chess.Utility;

public class BoardState {
	
	public static final int COLOUR_MASK = 0x800;//1<<11
	
	public long pieceMap;
	public long[] pieceData = new long[2];
	public long extraData;
	
	public int order;
	public int score;

	public HashSet<BoardState> children = new HashSet<BoardState>();
	public HashSet<BoardState> ParentStates = new HashSet<BoardState>();
	
	public BoardState(Board b) {
		int pieceNo = 0;
		Piece BoardPieceMap[] = b.getPieces();
		for(int i = 0; i < 64; i++) {
			if(!BoardPieceMap[i].isEmpty()) {
				pieceMap |= 1<<i;
				
				int pieceval = 0;
				if(BoardPieceMap[i] instanceof Pawn) {
					pieceval = 0;
				}
				else if(BoardPieceMap[i] instanceof Rook) {
					pieceval = 1;
				}
				else if(BoardPieceMap[i] instanceof Knight) {
					pieceval = 2;
				}
				else if(BoardPieceMap[i] instanceof Bishop) {
					pieceval = 3;
				}
				else if(BoardPieceMap[i] instanceof Queen) {
					pieceval = 4;
				}
				else if(BoardPieceMap[i] instanceof King) {
					pieceval = 5;
				}
				if(BoardPieceMap[i].getColour() == Colour.BLACK) {
					pieceval += 8;
				}
				
				pieceData[pieceNo/16] = pieceval<<((pieceNo%16)*4);
				pieceNo++;
			}
		}
		extraData = b.getEnPassant()&0x7f;
		extraData |= b.getCastleInfo()<<7;
		extraData |= (b.getColour() == Colour.WHITE)?1<<11:0;

		order = 0;
		if(b.getColour() == Colour.WHITE) {
			score = -1000000000;//bad for white
		}
		else {
			score = 1000000000;//bad for black
		}
	}
	
	public int hashCode() {
		long x = pieceMap ^ extraData ^ pieceData[0] ^ pieceData[1];
		return (int)((x>>32) ^ (x&0xffffffff));
	}
	
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof BoardState)) {
			return false;
		}
		BoardState bs = (BoardState)o;
		return (bs.extraData==extraData) && (bs.pieceData[0] == pieceData[0]) && (bs.pieceData[1] == pieceData[1]) && (bs.pieceMap == pieceMap);
		
	}
	
}
