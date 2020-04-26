package game;

import org.joml.Vector2d;

import engine.Display;
import engine.IGameLogic;
import input.Keyboard;
import input.Mouse;
import sound.SoundManager;

public class Game implements IGameLogic {

	private final Renderer renderer;
	
	private Display window;
	
	private SoundManager soundManager;
	
	private Vector2d mousePos;
	private boolean mouseLeftDown;

	private IGameState gameState;

	public Game() {
		renderer = new Renderer();
		gameState = new ChessGameState();
		soundManager = new SoundManager();
	}

	@Override
	public void init(Display window) throws Exception {
		this.window = window;
		renderer.init(window);
		gameState.initState(this);
		gameState.loadState();
	}

	@Override
	public void input(float interval, Keyboard keyboard, Mouse mouse) {
		mousePos = mouse.getCurrentPos();
		mouseLeftDown = mouse.isLeftButtonDown();
	}
	
	@Override
	public void update(float interval) {
		gameState.playState(interval, this);
	}

	@Override
	public void render() {
		gameState.render(renderer);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		soundManager.cleanup();
	}

	public void setGameState(IGameState gameState) {
		this.gameState = gameState;
	}
	
	public Vector2d getMousePos() {
		return mousePos;
	}
	
	public boolean isMouseLeftDown() {
		return mouseLeftDown;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public Display getWindow() {
		return window;
	}
	

}