package chess;

import java.util.Arrays;

public class Utility {
	
	public static final String[] COLUMNS = new String[] { "a", "b", "c", "d", "e", "f", "g", "h" };
	public static final String[] ROWS = new String[] { "1", "2", "3", "4", "5", "6", "7", "8" };

    public static String toFEN(Board board) {
        String FEN_string = "";
        int emptySquares = 0;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				int index = (i * 8) + j;
				if (board.getPiece(index).isEmpty()) {
					emptySquares++;
				} else {
					if (emptySquares > 0) {
						FEN_string += emptySquares;
						emptySquares = 0;
					}
					FEN_string += board.getPiece(index).getFEN();
				}
			}
			if (emptySquares > 0) {
				FEN_string += emptySquares;
				emptySquares = 0;
			}
			if (i != 7) {
				FEN_string += "/";
			}

		}
		
        FEN_string += " " + (board.getColour() == Colour.WHITE ? "w" : "b");
        FEN_string += " " + FEN_castleAvailability(board);
        FEN_string += " " + board.getEnPassant();
        
        return FEN_string;
    }

    public static void loadFEN(Board board, String FEN_line) {
        String[] FEN_split = FEN_line.split(" ");
        String[] FEN_board = FEN_split[0].split("/");

		for (int i = 0; i < 8; i++) {
			String[] rowData = FEN_board[i].split("");
			for (int j = 0; j < 8; j++) {
				int index = (i * 8) + j;
				String s = rowData[j];
				
				if (Utility.isNumeric(s)) {
					for (int k = index; k < index + Integer.parseInt(s); k++) {
						board.setPiece(k, new Piece());
					}
					j += Integer.parseInt(s);
				} else {
					board.setPiece(index, Utility.determinePiece(s));
				}
			}
		}

        board.setColour(FEN_split[1].equals("w") ? Colour.WHITE : Colour.BLACK);
        board.setCastleInfo(FEN_split[2]);
        board.setEnPassant(FEN_split[3]);
    }

    public static String FEN_castleAvailability(Board board) {
        if (board.getCastleInfo().isEmpty()) {
            return "-";
        }
        
        String castleAvailability = "";
        Piece[] pieces = board.getPieces();

        for (int i = 60; i >= 4; i -= 56) {
        	if (pieces[i] instanceof King && !pieces[i].isMoved()) {
        		
    			int queenside = Utility.getRow(i) * 8;
    			int kingside = queenside + 7;

    			if (pieces[kingside] instanceof Rook && !pieces[kingside].isMoved()) {
    				castleAvailability += pieces[i].getColour() == Colour.WHITE ? "K" : "k";
    			}
    			if (pieces[queenside] instanceof Rook && !pieces[queenside].isMoved()) {
    				castleAvailability += pieces[i].getColour() == Colour.WHITE ? "Q" : "q";
    			}
    		}
        }
	
        if (castleAvailability.isEmpty()) {
            castleAvailability = "-";
        }
      
        return castleAvailability;
    }

    public static Colour FEN_colour(String FEN) {
        return FEN.split("")[1].equals("w") ? Colour.WHITE : Colour.BLACK;
    }

    public static int getIndex(String move) {
        int column = Arrays.asList(COLUMNS).indexOf(move.substring(0, 1));
        int row = Arrays.asList(ROWS).indexOf(move.substring(move.length() - 1));

        return (7 - row) * 8 + column;
    }

    public static String getMove(int index) {
        return COLUMNS[Utility.getCol(index)]
                + ROWS[8 - Utility.getRow(index) - 1];
    }

    public static Piece determinePiece(String s) {
        Colour pieceColour = s.equals(s.toUpperCase()) ? Colour.WHITE : Colour.BLACK;

        switch (s.toLowerCase()) {
            case "p":
                return new Pawn(pieceColour);
            case "n":
                return new Knight(pieceColour);
            case "b":
                return new Bishop(pieceColour);
            case "r":
                return new Rook(pieceColour);
            case "q":
                return new Queen(pieceColour);
            case "k":
                return new King(pieceColour);
            default:
                return new Piece();
        }
    }

    public static int getCol(int index) {
        return index % 8;
    }

    public static int getRow(int index) {
        return index / 8;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
