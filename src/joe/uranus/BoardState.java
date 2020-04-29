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

public class BoardState {
	
	public static final int COLOUR_MASK = 0x800;//1<<11
	
	public long pieceMap;
	public long[] pieceData = new long[2];
	public long extraData;
	
	public int order;
	public int score;

	public HashSet<BoardState> children = new HashSet<BoardState>();
	//public HashSet<BoardState> ParentStates = new HashSet<BoardState>();
	
	public BoardState(Board b) {
		int pieceNo = 0;
		Piece BoardPieceMap[] = b.getPieces();
		for(int i = 0; i < 64; i++) {
			if(!BoardPieceMap[i].isEmpty()) {
				pieceMap |= 1L<<i;
				
				long pieceval = 0;
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
				
				pieceData[pieceNo/16] |= pieceval<<((pieceNo%16)*4);
				pieceNo++;
			}
		}
		extraData = b.getEnPassant()&0x7f;
		extraData |= b.getCastleInfo()<<7;
		extraData |= (b.getColour() == Colour.WHITE)?1<<11:0;

		order = 0;
		if(b.getColour() == Colour.WHITE) {
			score = 1000000000;//bad for white
		}
		else {
			score = -1000000000;//bad for black
		}
	}
	
	public Board toBoard() {
		Board b = new Board();
		Piece[] ps = b.getPieces();
		int pCounter = 0;
		for(int i = 0; i < 64; i++) {
			if((pieceMap&(1L<<i)) != 0) {
				long pieceCode = (pieceData[pCounter / 16] >> ((pCounter % 16) * 4)) & 0xf;
				if(pieceCode == 0) {
					ps[i] = new Pawn(Colour.WHITE);
				}
				else if(pieceCode == 1) {
					ps[i] = new Rook(Colour.WHITE);
				}
				else if(pieceCode == 2) {
					ps[i] = new Knight(Colour.WHITE);
				}
				else if(pieceCode == 3) {
					ps[i] = new Bishop(Colour.WHITE);
				}
				else if(pieceCode == 4) {
					ps[i] = new Queen(Colour.WHITE);
				}
				else if(pieceCode == 5) {
					ps[i] = new King(Colour.WHITE);
				}
				else if(pieceCode == 8) {
					ps[i] = new Pawn(Colour.BLACK);
				}
				else if(pieceCode == 9) {
					ps[i] = new Rook(Colour.BLACK);
				}
				else if(pieceCode == 10) {
					ps[i] = new Knight(Colour.BLACK);
				}
				else if(pieceCode == 11) {
					ps[i] = new Bishop(Colour.BLACK);
				}
				else if(pieceCode == 12) {
					ps[i] = new Queen(Colour.BLACK);
				}
				else if(pieceCode == 13) {
					ps[i] = new King(Colour.BLACK);
				}
				pCounter++;
			}
			else {
				ps[i] = new Piece();
			}
		}
		b.setCastleInfo((int) ((extraData>>7)&0xf));
		b.setEnPassant((int) (extraData&0x7f));
		b.setColour((extraData&11)==0 ? Colour.WHITE: Colour.BLACK);
		return b;
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
