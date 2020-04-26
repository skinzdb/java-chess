package game;

import java.util.ArrayList;

import chess.Board;
import chess.Colour;
import chess.GameLoader;
import chess.HumanPlayer;
import chess.Move;
import chess.Player;
import chess.Utility;
import graphics.Camera;
import graphics.Geometry;
import graphics.Sprite;
import graphics.Texture;

public class ChessGameState implements IGameState {

	private Board board;
	
	private Player whitePlayer;
	private Player blackPlayer;
	
	private ArrayList<Move> currentMoves;
	
	private Thread currentPlayerThread;
	
	private Texture boardTex;
	private Texture piecesTex;
	
	private Camera cam;
	
	@Override
	public void initState(Game game) {
		board = GameLoader.loadDefault();
		
		whitePlayer = new HumanPlayer();
		blackPlayer = new HumanPlayer();
		
		currentMoves = new ArrayList<>();
		
		boardTex = new Texture("res/board.png", 2);
		piecesTex = new Texture("res/chess_pieces.png", 4);
		
		cam = new Camera();
		cam.setScale(60);
		cam.translate(3.5f, 3.5f);

		getNextMove();
	}
	
	private Player getCurrentPlayer() {
		return board.getColour() == Colour.WHITE ? whitePlayer : blackPlayer;
	}
	
	private void getNextMove() {
		currentMoves = board.getPossibleMoves();
		
		getCurrentPlayer().makeMove(board.clone(), currentMoves);
		
		currentPlayerThread = new Thread(getCurrentPlayer());
		currentPlayerThread.start();
	}
	

	@Override
	public void loadState() {
		
	}

	@Override
	public void playState(float elapsedTime, Game game) {
		if (!getCurrentPlayer().isChoosing()) {
			board = board.move(currentMoves.get(getCurrentPlayer().getChosenMove()));
			board.setupNextMove();
			
			getNextMove();
		}
	}

	@Override
	public void render(Renderer renderer) {
		ArrayList<Sprite> boardTiles = new ArrayList<>();
		ArrayList<Sprite> pieceTiles = new ArrayList<>();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Sprite boardTile = new Sprite(i, j, Geometry.quad, boardTex);
				boardTile.setPointer((i + j % 2) & 1);
				boardTiles.add(boardTile);
				
				Sprite pieceTile = new Sprite(j, 7 - i, Geometry.quad, piecesTex);
				pieceTile.setPointer(board.getPiece(i * 8 + j).getImageIndex());
				pieceTiles.add(pieceTile);
			}
		}
		renderer.render(cam, boardTiles, true);
		renderer.render(cam, pieceTiles, false);
	}
}
