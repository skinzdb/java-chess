package chess;

import java.util.ArrayList;

/*
The first square of the board starts at the top left; its index is 0.
 */
public class Board {

	private Colour colour;

	private boolean check;
	private boolean checkmate;
	private boolean stalemate;

	private String castleInfo;
	private String enPassant;

	private Piece[] pieces;
	
	private ArrayList<String> gameMoves; // list of all the the moves made (saved in FEN)
	private ArrayList<Piece> takenPieces;

	public Board() {
		colour = Colour.WHITE;
		
		check = false;
		checkmate = false;
		stalemate = false;
		
		castleInfo = "";
		enPassant = "-";
		
		pieces = new Piece[64];
		
		gameMoves = new ArrayList<>();
		takenPieces = new ArrayList<>();
	}
	
	public Board(Board board) {
		this();
		
		colour = board.getColour();
		
		check = board.isCheck();
		checkmate = board.isCheckmate();
		stalemate = board.isStalemate();
		
		castleInfo = board.getCastleInfo();
		enPassant = board.getEnPassant();
		
		for (int i = 0; i < pieces.length; i++) {
			this.pieces[i] = board.getPiece(i).clone();
		}
		
		for (String move : board.getGameMoves()) {
			this.gameMoves.add(move);
		}
		
		for (Piece piece : board.getTakenPieces()) {
			this.takenPieces.add(piece);
		}
	}
	
	@Override
	public Board clone() {
		return new Board(this);
	}
	
	public boolean isCheck() {
		return check;
	}
	
	public boolean isCheckmate() {
		return checkmate;
	}

	public boolean isStalemate() {
		return stalemate;
	}

	public void setPiece(int index, Piece piece) {
		pieces[index] = piece;
	}

	public Piece[] getPieces() {
		return pieces;
	}

	public void setPieces(Piece[] pieces) {
		this.pieces = pieces;
	}

	public Piece getPiece(String square) {
		return pieces[Utility.getIndex(square)];
	}

	public Piece getPiece(int index) {
		return pieces[index];
	}

	public Colour getColour() {
		return colour;
	}

	public void setColour(Colour colour) {
		this.colour = colour;
	}
	
	public void swapColour() {
		colour = colour == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
	}

	public String getCastleInfo() {
		return castleInfo;
	}

	public void setCastleInfo(String info) {
		castleInfo = info;
	}

	public String getEnPassant() {
		return enPassant;
	}

	public void setEnPassant(String info) {
		enPassant = info;
	}
	
	public ArrayList<String> getGameMoves() {
		return gameMoves;
	}

	public void setGameMoves(ArrayList<String> gameMoves) {
		this.gameMoves = gameMoves;
	}

	public ArrayList<Piece> getTakenPieces() {
		return takenPieces;
	}

	public void setTakenPieces(ArrayList<Piece> takenPieces) {
		this.takenPieces = takenPieces;
	}

	public Board move(int from, int to) {
		if (checkmate || stalemate) {
			return this;
		}
		
		Board newBoard = clone();
	
		if (!pieces[to].isEmpty()) {
			newBoard.getTakenPieces().add(pieces[to]);
		}
		
		newBoard.handleSpecialMoves(from, to);
		newBoard.changePieces(from, to);
		
		newBoard.check();
		
		return newBoard;
	}
	
	public Board move(Move move) {
		return move(move.from, move.to);
	}
	
	public void setupNextMove() {
		swapColour();
		
		checkAll();
		
		gameMoves.add(Utility.toFEN(this));
		
		System.out.println(gameMoves.get(gameMoves.size() - 1));
	}
	
	public Board undo() {
		if (gameMoves.size() == 1 || checkmate || stalemate) {
			return this;
		}
		
		Board newBoard = clone();
		
		Utility.loadFEN(newBoard, gameMoves.get(gameMoves.size() - 2));
		
		newBoard.getGameMoves().remove(gameMoves.size() - 1); // remove current move
		newBoard.getTakenPieces().remove(takenPieces.size() - 1);	// undo taken piece
		
		newBoard.check(); 
		
		return newBoard;
	}

	private void handleSpecialMoves(int from, int to) {
		handleCastling(from, to);
		handleEnPassant(from, to);
		handlePromotion(from, to);
	}

	private void handlePromotion(int from, int to) {
		if (pieces[from] instanceof Pawn) { // is the piece a pawn?
			// if it is white and on rank 0 or is black and on rank 7 -> promote
			if (Utility.getRow(to) == (colour == Colour.WHITE ? 0 : 7)) {
				pieces[from] = new Queen(colour);
			}
		}
	}

	private void handleEnPassant(int from, int to) {
		if (pieces[from] instanceof Pawn) {
			if (Utility.getMove(to).equals(enPassant)) { // is this square the en passant square?
				// get position of the opposition pawn and remove it
				int n = pieces[from].getColour() == Colour.WHITE ? 8 : -8;
				pieces[to + n] = new Piece();
			}
		}

		enPassant = "-";

		// detect if a pawn has moved 16 squares upwards or downwards (2 ranks)
		// if so, set the en-passant square to be behind that pawn
		if (pieces[from] instanceof Pawn) {
			if (Math.abs(to - from) == 16) {
				int n = pieces[from].getColour() == Colour.WHITE ? -8 : 8;
				enPassant = Utility.getMove(from + n);
			}
		}
	}

	private void handleCastling(int from, int to) {
		int move = to - from;

		if (pieces[from] instanceof King) {
			// Queen-side
			if (move == -2) {			
				changePieces(Utility.getRow(from) * 8, from - 1);
			} // King-side
			else if (move == 2) {
				changePieces(((Utility.getRow(from) + 1) * 8) - 1, from + 1);
			}
		}
	}
	// move a piece to a new square and make it's starting square empty
	private void changePieces(int from, int to) {
		pieces[from].setMoved(true);

		pieces[to] = pieces[from];
		pieces[from] = new Piece();
	}

	public void check() {
		check = Detection.isCheck(colour, this);
	}
	
	public void checkAll() {
		check();
		
		checkmate = check && Detection.isCheckmate(colour, this);
		stalemate = !checkmate && Detection.isStalemate(colour, this);
	}
	
	public int getBoardValue(Colour colour) {
		int totalValue = 0;

		for (Piece p : pieces) {
			if (p.isEmpty())
				continue;
			int value = p.getValue();
			totalValue += p.getColour() == colour ? value : -value;
		}

		return totalValue;
	}

	public ArrayList<Move> getPossibleMoves() {
		ArrayList<Move> moves = new ArrayList<>();
		
		for (int i = 0; i < 64; i++) {
			if (pieces[i].getColour() == colour) {
				for (int j : Mapping.createMoveMap(i, this)) {
					moves.add(new Move(i, j));
				}
			}
		}

		return moves;
	}
}
