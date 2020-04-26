package chess;

import java.util.ArrayList;

public class Piece {
	protected Colour colour;
	
	protected int value;
	
	protected boolean moved;
	
	protected String FEN;
	
	protected int imageIndex;
	
	public Piece(Colour colour) {
		this();
		this.colour = colour;
	}
	
	public Piece() {
		colour = Colour.NONE;
		
		value = 0;
		moved = false;
		FEN = "";
		imageIndex = 0;
	}
	
	public Piece clone() {
		return new Piece();
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
	
	public boolean isMoved() {
		return moved;
	}
	
	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	public int getImageIndex() {
		return imageIndex;
	}
}
