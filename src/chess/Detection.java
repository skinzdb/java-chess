package chess;

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

    /* SLOWER
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
    */
    
    public static boolean checkThreat(int index, Board board, Colour colour) {
    	Piece[] pieces = board.getPieces();
    	
    	for (int move : Mapping.getStraightMoves(index, pieces)) {
    		if (!pieces[move].isEmpty() && pieces[move].getColour() != colour) {
    			if (pieces[move] instanceof Rook || pieces[move] instanceof Queen) return true;
    		}
    	}
    	
    	for (int move : Mapping.getDiagonalMoves(index, pieces)) {
    		if (!pieces[move].isEmpty() && pieces[move].getColour() != colour) {
    			if (pieces[move] instanceof Bishop || pieces[move] instanceof Queen) return true;
    		}
    	}
    	
    	for (int move : new Knight(colour).getAttacks(index, board)) {
    		if (!pieces[move].isEmpty() && pieces[move].getColour() != colour) {
    			if (pieces[move] instanceof Knight) return true;
    		}
    	}
    	
    	for (int move : new Pawn(colour).getAttacks(index, board)) {
    		if (!pieces[move].isEmpty() && pieces[move].getColour() != colour) {
    			if (pieces[move] instanceof Pawn) return true;
    		}
    	}
    	
    	if (pieces[index] instanceof Pawn) {
    		if (index + (colour == Colour.WHITE ? 8 : -8)==(board.getEnPassant())) {
        		if (Utility.getCol(index) > 0) {
        	    	if (pieces[index - 1] instanceof Pawn) return true;
        		}
        		if (Utility.getCol(index) < 7) {
        			if (pieces[index + 1] instanceof Pawn) return true;
        		}
    		}
    	}
    	
    	for (int move : new King(colour).getAttacks(index, board)) {
    		if (!pieces[move].isEmpty() && pieces[move].getColour() != colour) {
    			if (pieces[move] instanceof King) return true;
    		}
    	}
    	
    	return false;
    }
}
