package game;

import engine.Display;
import engine.IGameLogic;
import graphics.GUI;
import graphics.RenderBlock;
import input.Keyboard;
import input.Mouse;
import sound.SoundManager;

public class Game implements IGameLogic {

	private final Renderer renderer;
	
	private Display window;
	
	private GUI gui;
	
	private SoundManager soundManager;
	
	private Mouse mouse;
	private Keyboard keyboard;

	private IGameState gameState;

	public Game() {
		renderer = new Renderer();
		soundManager = new SoundManager();
		gameState = new ChessGameState();
	}

	@Override
	public void init(Display window) throws Exception {
		this.window = window;
		renderer.init(window);
		gui = new GUI();
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
		for (RenderBlock rb : gui.getGUIObjects())
			renderer.render(rb, false);
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

	public Display getWindow() {
		return window;
	}
	
	public GUI getGUI() {
		return gui;
	}
	
	public SoundManager getSoundManager() {
		return soundManager;
	}
	
	public Keyboard getKeyboard() {
		return keyboard;
	}
	
	public Mouse getMouse() { 
		return mouse;
	}

}