package game;

import java.util.ArrayList;

import chess.Board;
import chess.Colour;
import chess.GameLoader;
import chess.HumanPlayer;
import chess.King;
import chess.Mapping;
import chess.Move;
import chess.Player;
import graphics.Camera;
import graphics.Geometry;
import graphics.Sprite;
import graphics.Texture;
import joe.uranus.RandomPlayer;

public class ChessGameState implements IGameState {

	private Board board;
	
	private Player whitePlayer;
	private Player blackPlayer;
	
	private int currentSelSquare;
	
	private ArrayList<Move> currentMoves;
	
	private long startTime;
	private float duration;
	
	private Thread currentPlayerThread;
	
	private Texture boardTex;
	private Texture piecesTex;
	
	ArrayList<Sprite> boardTiles;
	ArrayList<Sprite> pieceTiles;
	
	private Camera cam;
	
	@Override
	public void initState(Game game) {
		board = GameLoader.loadDefault();
		
		currentMoves = new ArrayList<>();
		
		boardTex = new Texture("res/board.png", 2);
		piecesTex = new Texture("res/chess_pieces.png", 4);
		
		boardTiles = new ArrayList<>();
		pieceTiles = new ArrayList<>();
		
		cam = new Camera();
		cam.setScale(60);
		cam.translate(3.5f, -3.5f);
		
		whitePlayer = new HumanPlayer(cam, game.getMouse());
		blackPlayer = new HumanPlayer(cam, game.getMouse());
		
		currentSelSquare = -1;
		
		updateTiles();
		
		getNextMove();
	}
	
	private void updatePieces() {
		pieceTiles.clear();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Sprite pieceTile = new Sprite(j, -i, Geometry.quad, piecesTex);
				pieceTile.setPointer(board.getPiece(i * 8 + j).getImageIndex());
				pieceTile.setDepthLayer(1);
				pieceTiles.add(pieceTile);
			}
		}
	}
	
	private void updateTiles() {
		boardTiles.clear();
		
		ArrayList<Integer> moveMap = new ArrayList<>();
		
		if (currentSelSquare != - 1) {
			moveMap = Mapping.createMoveMap(currentSelSquare, board);
		}
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Sprite boardTile = new Sprite(i, -j, Geometry.quad, boardTex);
				boardTile.setPointer((i + j % 2) & 1);
				int index = j * 8 + i;
				if (board.isCheck() && board.getPiece(index) instanceof King && board.getPiece(index).getColour() == board.getColour()) { // Highlight the king in check
					boardTile.setPointer(3);
				}
				if (currentSelSquare == index) { // Highlight selected square
					boardTile.setPointer(2);
				} else if (moveMap.contains(index)) { // Highlight where the selected piece can move to
					boardTile.setPointer(2);
				}
				boardTile.setDepthLayer(0);
				
				boardTiles.add(boardTile);
			}
		}
	}
	
	private Player getCurrentPlayer() {
		return board.getColour() == Colour.WHITE ? whitePlayer : blackPlayer;
	}
	
	private void getNextMove() {
		updatePieces();
		
		if (board.isFinished()) {
			if (board.isCheckmate())
				System.out.println("CHECKMATE! " + (board.getColour() == Colour.WHITE ? "BLACK" : "WHITE") + "WINS");
			else if (board.isStalemate()) 
				System.out.println("STALEMATE!\n");
			
			whitePlayer.stop();
			blackPlayer.stop();
			
			return;
		}
		
		currentMoves = board.getPossibleMoves();

		getCurrentPlayer().makeMove(board.clone(), currentMoves);
		
		startTime = System.nanoTime();
		duration = 0;
		
		currentPlayerThread = new Thread(getCurrentPlayer());
		currentPlayerThread.start();
	}
	
	@Override
	public void loadState() {
		
	}

	@Override
	public void playState(float elapsedTime, Game game) {
		if (board.isFinished())
			return;
		
		if (!getCurrentPlayer().isChoosing()) {
			int move = getCurrentPlayer().getChosenMove();
			if (move < 0 || move >= currentMoves.size())
				move = 0;
			
			board = board.move(currentMoves.get(move));
			board.setupNextMove();
			
			long finishTime = (System.nanoTime() - startTime);
			
			duration = Math.round(finishTime / 10000000.0) / 100.0f;
			System.out.println("Move duration: " + duration + "s\n");
			
			getNextMove();
		}
		
		if (currentSelSquare != getCurrentPlayer().getSelectedSquare()) {
			currentSelSquare = getCurrentPlayer().getSelectedSquare();
			updateTiles();
		}
	}

	@Override
	public void render(Renderer renderer) {
		renderer.render(cam, boardTiles, true);
		renderer.render(cam, pieceTiles, false);
	}

	@Override
	public void exitState() {
		whitePlayer.stop();
		blackPlayer.stop();
	}
}
