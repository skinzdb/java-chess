package engine;

import input.Keyboard;
import input.Mouse;

public class GameEngine implements Runnable {

	public static final int TARGET_FPS = 60;
	public static final int TARGET_UPS = 30;

	private final Display window;

	private final Thread gameLoopThread;

	private final Timer timer;

	private final IGameLogic gameLogic;

	private Keyboard keyboardInput;

	private Mouse mouseInput;

	public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) throws Exception {
		gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
		window = new Display(windowTitle, width, height, vSync);
		this.gameLogic = gameLogic;
		timer = new Timer();
		keyboardInput = new Keyboard();
		mouseInput = new Mouse();
	}

	public void start() {
		String osName = System.getProperty("os.name");
		if (osName.contains("Mac")) {
			gameLoopThread.run();
		} else {
			gameLoopThread.start();
		}
	}

	@Override
	public void run() {
		try {
			init();
			gameLoop();
		} catch (Exception excp) {
			excp.printStackTrace();
		} finally {
			cleanup();
		}
	}

	protected void init() throws Exception {
		window.init();
		timer.init();
		mouseInput.init(window);
		keyboardInput.init(window);
		gameLogic.init(window);
	}

	protected void gameLoop() {
		float elapsedTime;
		float accumulator = 0f;
		float interval = 1f / TARGET_UPS;

		boolean running = true;
		while (running && !window.windowShouldClose()) {
			elapsedTime = timer.getElapsedTime();
			accumulator += elapsedTime;

			input(interval);

			while (accumulator >= interval) {
				update(interval);
				accumulator -= interval;
			}

			render();

			if (!window.isvSync()) {
				sync();
			}
		}
	}

	protected void cleanup() {
		gameLogic.cleanup();
	}

	private void sync() {
		float loopSlot = 1f / TARGET_FPS;
		double endTime = timer.getLastLoopTime() + loopSlot;
		while (timer.getTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException ie) {
			}
		}
	}

	protected void input(float interval) {
		gameLogic.input(interval, keyboardInput, mouseInput);
	}

	protected void update(float interval) {
		gameLogic.update(interval);
		keyboardInput.update();
		mouseInput.update();
	}

	protected void render() {
		gameLogic.render();
		window.update();
	}
}