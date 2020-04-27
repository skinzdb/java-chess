package engine;

import input.Mouse;
import input.Keyboard;

public interface IGameLogic {
	void init(Display Window) throws Exception;

	void setupInput(Keyboard keyboardInput, Mouse mouseInput);

	void update(float interval);
	
	void input(float interval);

	void render();

	void cleanup();
}