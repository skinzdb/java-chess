package chess;

import java.util.ArrayList;

public class Knight extends Piece {

	public Knight(Colour colour) {
		super(colour);
		
        FEN = colour == Colour.WHITE ? "N" : "n";
		
		value = 30;
		
		imageIndex = colour == Colour.WHITE ? 1 : 9;
	}

	@Override
	public ArrayList<Integer> getMoves(int index, Board board) {
		ArrayList<Integer> moveMap = new ArrayList<Integer>();

		int col = Utility.getCol(index);
		int row = Utility.getRow(index);

		if (col >= 2 && row >= 1) {
			moveMap.add(index - 10);
		}
		if (col <= 5 && row >= 1) {
			moveMap.add(index - 6);
		}
		if (col >= 2 && row <= 6) {
			moveMap.add(index + 6);
		}
		if (col <= 5 && row <= 6) {
			moveMap.add(index + 10);
		}

		if (col >= 1 && row >= 2) {
			moveMap.add(index - 17);
		}
		if (col <= 6 && row >= 2) {
			moveMap.add(index - 15);
		}
		if (col >= 1 && row <= 5) {
			moveMap.add(index + 15);
		}
		if (col <= 6 && row <= 5) {
			moveMap.add(index + 17);
		}

		return moveMap;
	}
}
