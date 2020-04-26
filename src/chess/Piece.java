package chess;

import java.util.ArrayList;

public class Piece {
	protected Colour colour;
	
	protected int value = 0;
	
	protected String FEN = "";
	
	protected int imageIndex = 0;
	
	public Piece() {
		this.colour = Colour.NONE;
	}
	
	public Piece(Colour colour) {
		this.colour = colour;
	}
	
	public ArrayList<Integer> getMoves(int index, Board board) {
		return null;
	}
	
	public ArrayList<Integer> getAttacks(int index, Board board) {
		return getMoves(index, board);
	}
	
	public boolean isEmpty() {
		return colour == Colour.NONE;
	}

	public int getValue() {
		return value;
	}
	
	public String getFEN() {
		return FEN;
	}

	public Colour getColour() {
		return colour;
	}
	
	public int getImageIndex() {
		return imageIndex;
	}
}
