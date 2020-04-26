package input;

import engine.Display;

import org.lwjgl.glfw.GLFW;

public class Keyboard {
	private Display window;

	public void init(Display window) {
		this.window = window;
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		GLFW.glfwSetKeyCallback(window.getWindowHandle(), (windowHandle, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
				GLFW.glfwSetWindowShouldClose(window.getWindowHandle(), true); // We will detect this in the rendering loop
			}
		});
	}

	public void update() {

	}

	public boolean keyDown(int keycode) {
		return GLFW.glfwGetKey(window.getWindowHandle(), keycode) == GLFW.GLFW_PRESS;
	}

	public boolean keyHeld(int keycode) {
		return GLFW.glfwGetKey(window.getWindowHandle(), keycode) == GLFW.GLFW_REPEAT;
	}
		
	public boolean keyUp(int keycode) {
		return GLFW.glfwGetKey(window.getWindowHandle(), keycode) == GLFW.GLFW_RELEASE;
	}
}