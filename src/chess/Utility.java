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
        FEN_string += " " + castleInfoToFEN(board);
        FEN_string += " " + (board.getEnPassant() == -1 ? "-" : getMove(board.getEnPassant()));
        
        return FEN_string;
    }

    public static void loadFEN(Board board, String FEN_line) {
        String[] FEN_split = FEN_line.split(" ");
        String[] FEN_board = FEN_split[0].split("/");

		for (int i = 0; i < 8; i++) {
			String[] rowData = FEN_board[i].split("");
			int counter = 0;
			for (int j = 0; j < 8; j++) {
				int index = (i * 8) + j;
				String s = rowData[counter++];
				if (Utility.isNumeric(s)) {
					for (int k = index; k < index + Integer.parseInt(s); k++) {
						board.setPiece(k, new Piece());
					}
					j += Integer.parseInt(s) - 1;
				} else {
					board.setPiece(index, determinePiece(s));
				}
			}
		}

        board.setColour(FEN_split[1].equals("w") ? Colour.WHITE : Colour.BLACK);
        board.setCastleInfo(FEN_toCastleInfo(FEN_split[2]));
        board.setEnPassant(getIndex(FEN_split[3]));
    }

    public static String castleInfoToFEN(Board board) {
    	int castleInfo = board.getCastleInfo();
    	String str = "";
    	
    	if ((castleInfo & 0b1000) == 0b1000) str += "K";
    	if ((castleInfo & 0b0100) == 0b0100) str += "Q";
    	if ((castleInfo & 0b0010) == 0b0010) str += "k";
    	if ((castleInfo & 0b0001) == 0b0001) str += "q";
    	
        return str.isEmpty() ? "-" : str;
    }
    
    public static int FEN_toCastleInfo(String castleFEN) {
    	int castleInfo = 0;
    	
    	if (castleFEN.contains("K")) castleInfo |= 0b1000;
    	if (castleFEN.contains("Q")) castleInfo |= 0b0100;
    	if (castleFEN.contains("k")) castleInfo |= 0b0010;
    	if (castleFEN.contains("q")) castleInfo |= 0b0001;
    	
    	return castleInfo;
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
        return COLUMNS[getCol(index)]
                + ROWS[7 - getRow(index)];
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
