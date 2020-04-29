package input;

import engine.Display;

import org.lwjgl.glfw.GLFW;

public class Keyboard {
	private Display window;

	private boolean keyDown;
	private boolean keyHeld;
	private boolean keyUp;
	
	public Keyboard() {
		resetState();
	}
	
	public void init(Display window) {
		this.window = window;
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		GLFW.glfwSetKeyCallback(window.getWindowHandle(), (windowHandle, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
				GLFW.glfwSetWindowShouldClose(window.getWindowHandle(), true); // We will detect this in the rendering loop
			}
			
			keyDown = action == GLFW.GLFW_PRESS;
			keyHeld = action == GLFW.GLFW_REPEAT;
			keyUp = action == GLFW.GLFW_RELEASE;
		});
	}

	public void resetState() {
		keyDown = false;
		keyHeld = false;
		keyUp = false;
	}
	
	public void update() {

	}

	public boolean keyDown(int keycode) {
		return keyDown && GLFW.glfwGetKey(window.getWindowHandle(), keycode) == GLFW.GLFW_PRESS;
	}

	public boolean keyHeld(int keycode) {
		return keyHeld && GLFW.glfwGetKey(window.getWindowHandle(), keycode) == GLFW.GLFW_REPEAT;
	}
		
	public boolean keyUp(int keycode) {
		return keyUp && GLFW.glfwGetKey(window.getWindowHandle(), keycode) == GLFW.GLFW_RELEASE;
	}
}