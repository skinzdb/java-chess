package engine;

import input.Mouse;
import input.Keyboard;

public interface IGameLogic {

	void init(Display Window) throws Exception;

	void input(float interval, Keyboard keyboardInput, Mouse mouseInput);

	void update(float interval);

	void render();

	void cleanup();
}