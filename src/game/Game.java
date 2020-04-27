package game;

import engine.Display;
import engine.IGameLogic;
import input.Keyboard;
import input.Mouse;
import sound.SoundManager;

public class Game implements IGameLogic {

	private final Renderer renderer;
	
	private Display window;
	
	private SoundManager soundManager;
	
	private Mouse mouse;
	private Keyboard keyboard;

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
	public void setupInput(Keyboard keyboardInput, Mouse mouseInput) {
		keyboard = keyboardInput;
		mouse = mouseInput;
	}

	@Override
	public void input(float interval) {

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
		gameState.exitState();
		renderer.cleanup();
		soundManager.cleanup();
	}

	public void setGameState(IGameState gameState) {
		this.gameState = gameState;
	}
	
	public Keyboard getKeyboard() {
		return keyboard;
	}
	
	public Mouse getMouse() { 
		return mouse;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public Display getWindow() {
		return window;
	}
}