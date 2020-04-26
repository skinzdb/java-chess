package chess;

import java.util.ArrayList;

public class Detection {

    public static boolean isCheck(Colour colour, Board board) {
        for (int i = 0; i < 64; i++) {
            if (board.getPiece(i) instanceof King && board.getPiece(i).getColour() == colour) {
            	return checkThreat(i, board, colour);
            }
        }
        return false;
    }

    public static boolean isCheckmate(Colour colour, Board board) {
        int counter = 0;
        for (Piece p : board.getPieces()) {
            if (p.getColour() == colour) {
                if (!Mapping.createMoveMap(counter, board).isEmpty())
                    return false;
            }
            counter++;
        }
        return true;
    }

    public static boolean isStalemate(Colour colour, Board board) {
        if (board.isCheck()) {
            return false;
        }

        Piece[] pieces = board.getPieces();

        for (int i = 0; i < 64; i++) {
            if (pieces[i].getColour() == colour) {
                if (!Mapping.createMoveMap(i, board).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean checkThreat(int index, Board board, Colour colour) {
        for (int i = 0; i < 64; i++) {
            if (board.getPiece(i).isEmpty()) {
                continue;
            }
            if (colour != board.getPiece(i).getColour()) {
            	ArrayList<Integer> moveMap = board.getPiece(i).getAttacks(i, board);
                if (moveMap.contains(index)) {
                    return true;
                }
            }
        }
        return false;
    }
}
