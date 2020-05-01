package game;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.lwjgl.glfw.GLFW;

import chess.Board;
import chess.Colour;
import chess.GameLoader;
import chess.GameSaver;
import chess.King;
import chess.Mapping;
import chess.Move;
import chess.Piece;
import chess.Player;
import david.betelgeuse.BetterPlayer;
import graphics.Camera;
import graphics.GUIImage;
import graphics.GUIText;
import graphics.Geometry;
import graphics.Sprite;
import graphics.Texture;
import joe.demon.SpeedDemon;
import joe.uranus.RandomPlayer;
import joe.uranus.UranusPlayer;
import input.Keyboard;
import joe.uranus.RandomPlayer;

public class ChessGameState implements IGameState {

	private Game game;
	
	private Board board;
	
	private Player whitePlayer;
	private Player blackPlayer;
	
	private int whiteScore = 1000;
	private int blackScore = 1000;
	private float timeLimit = 10.0f;
	
	private ArrayList<Float> moveDurations = new ArrayList<>();
	
	private int currentSelSquare;
	
	private ArrayList<Move> currentMoves;
	
	private long startTime;
	private float duration;
	
	private Thread currentPlayerThread;
	
	private Texture boardTex;
	private Texture piecesTex;
	
	private ArrayList<Sprite> boardTiles;
	private ArrayList<Sprite> pieceTiles;
	
	private ArrayList<Sprite> takenWhitePieces;
	private ArrayList<Sprite> takenBlackPieces;
	
	private Camera cam;
	
	private GUIText turnText;
	private GUIImage takenWImg;
	private GUIImage takenBImg;
	
	private DateFormat saveDateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
	
	@Override
	public void initState(Game game) {
		this.game = game;
		//board = GameLoader.load("res/game.txt");
		board = GameLoader.loadDefault();
		
		currentMoves = new ArrayList<>();
		
		boardTex = new Texture("res/board.png", 2);
		piecesTex = new Texture("res/chess_pieces.png", 4);
		
		boardTiles = new ArrayList<>();
		pieceTiles = new ArrayList<>();
		
		takenWhitePieces = new ArrayList<>();
		takenBlackPieces = new ArrayList<>();
		
		cam = new Camera();
		cam.setScale(60);
		cam.translate(3.5f, -2.5f);
		
		game.getGUI().getCam().setScale(16);
		game.getGUI().getCam().translate(14f, -18f);

		//whitePlayer = new RandomPlayer();
		//whitePlayer = new BetterPlayer(3);
		//whitePlayer = new SpeedDemon();
		whitePlayer = new UranusPlayer(Colour.WHITE);
		//whitePlayer = new HumanPlayer(cam, game.getMouse());
		blackPlayer = new BetterPlayer(3);
		//blackPlayer = new RandomPlayer();
		//blackPlayer = new UranusPlayer(Colour.BLACK);
		//blackPlayer = new HumanPlayer(cam, game.getMouse());
		
		currentSelSquare = -1;
		
		turnText = game.getGUI().text("", 0, -0.5f, 16, 2, 1.55f);
		takenWImg = game.getGUI().image(piecesTex, 0, -4f, 10, 2, 1.45f);
		takenBImg = game.getGUI().image(piecesTex, 15, -4f, 10, 2, 1.45f);

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
		
		if (board.isFinished()) {
			currentSelSquare = -1;
		}
		
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
	
	private void updateGUI() {
		turnText.setText(board.getColour() + "'S TURN");
		
		takenWhitePieces.clear();
		takenBlackPieces.clear();
		
		for (Piece piece : board.getTakenPieces()) {
			Sprite s = new Sprite(Geometry.quad, piecesTex);
			s.setPointer(piece.getImageIndex());
			
			if (piece.getColour() == Colour.WHITE) {
				takenWhitePieces.add(s);
			} else if (piece.getColour() == Colour.BLACK) {
				takenBlackPieces.add(s);
			}
		}
		
		takenWImg.setSprites(takenWhitePieces);
		takenBImg.setSprites(takenBlackPieces);
	}
	
	private Player getCurrentPlayer() {
		return board.getColour() == Colour.WHITE ? whitePlayer : blackPlayer;
	}
	
	private void getNextMove() {
		updatePieces();
		
		currentSelSquare = -1;
		updateTiles();
		
		updateGUI();
		
		if (board.getTakenPieces().size() == 30) { // only two kings left
			board.finish();
		}
		
		if (board.isFinished()) {
			if (board.isCheckmate()) {
				calcScores();
				if (board.getColour() == Colour.WHITE) {
					whiteScore -= 1000;
				} else {
					blackScore -= 1000;
				}
				turnText.setText("CHECKMATE!\n" + (board.getColour() == Colour.WHITE ? "BLACK" : "WHITE") + " WINS!");
				System.out.println("Scores:\n" + " -> WHITE: " + whiteScore + "\n -> BLACK: " + blackScore); 
			}
			else if (board.isStalemate()) 
				turnText.setText("STALEMATE!\nIT'S A DRAW!");
			else {
				turnText.setText("GAME ENDED!\nIT'S A DRAW!");
			}
			
			saveGame("GAME (FINISHED)");
			
			whitePlayer.stop();
			blackPlayer.stop();
			
			return;
		}
		
		currentMoves = board.getPossibleMoves();

		getCurrentPlayer().makeMove(board.clone(), currentMoves);
		
		startTime = System.nanoTime();
		duration = 0;
		
		currentPlayerThread = new Thread(getCurrentPlayer(), "CURRENT_PLAYER_THREAD");
		currentPlayerThread.start();
	}
	
	private void calcScores() {
		//whiteScore = 1000;
		//blackScore = 1000;
		float meanWhite = 0, meanBlack = 0;
		
		int moveNo = moveDurations.size();
		
		for (int i = 0; i < moveNo; i++) {
			if ((i & 1) == 0) {
				meanWhite += moveDurations.get(i);
			} else {
				meanBlack += moveDurations.get(i);
			}
		}
		
		meanWhite = meanWhite / (float) Math.ceil((moveNo / 2.0f));
		meanBlack = meanBlack / (float) (moveNo / 2);
		
		if (meanWhite > timeLimit) 
			whiteScore = (int) ((timeLimit / meanWhite) * whiteScore);
		if (meanBlack > timeLimit)
			blackScore = (int) ((timeLimit / meanBlack) * blackScore);
		
		System.out.println("Mean time:\n" + " -> WHITE: " + meanWhite + "s\n -> BLACK: " + meanBlack + "s"); 
	}
	
	@Override
	public void loadState() {
		
	}

	@Override
	public void playState(float elapsedTime, Game game) {
		updateInput();
		
		if (board.isFinished())
			return;

		if (!getCurrentPlayer().isChoosing()) {
			int move = getCurrentPlayer().getChosenMove();
			if (move < 0 || move >= currentMoves.size())
				move = 0;
			
			board = board.move(currentMoves.get(move));
			board.setupNextMove();

			long rawDuration = (System.nanoTime() - startTime);
			duration = rawDuration / 100000L / 10000.0f;
			System.out.println("Move duration: " + duration + "s\n");
			moveDurations.add(duration);
			
			getNextMove();
		}
		
		if (currentSelSquare != getCurrentPlayer().getSelectedSquare()) {
			currentSelSquare = getCurrentPlayer().getSelectedSquare();
			updateTiles();
		}
	}
	
	private void updateInput() {
		Keyboard keyboard = game.getKeyboard();

		if (keyboard.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && keyboard.keyDown(GLFW.GLFW_KEY_S)) { // save game
			saveGame("GAME (MIDWAY)");
		}
		
		keyboard.resetState();
		
	}

	private void saveGame(String filename) {
		String name = filename + " " + saveDateFormat.format(new Date()) + ".txt";
		GameSaver.saveGame(name, board);
		System.out.println("SAVED: " + name);
	}
	
	@Override
	public void render(Renderer renderer) {
		renderer.render(cam, boardTiles, true);
		renderer.render(cam, pieceTiles, false);
	}

	@Override
	public void exitState() {
		if(!board.isFinished()) {
			calcScores();
		}
		System.out.println("Scores:\n" + " -> WHITE: " + whiteScore + "\n -> BLACK: " + blackScore); 
		whitePlayer.stop();
		blackPlayer.stop();
	}
}
