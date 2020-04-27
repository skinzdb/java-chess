package input;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import engine.Display;
import game.Main;
import graphics.Camera;

public class Mouse {
	private final Vector2f currentPos;

	private boolean leftBtnDown;
	private boolean rightBtnDown;

	private boolean leftBtnHeld;
	private boolean rightBtnHeld;

	private boolean leftBtnUp;
	private boolean rightBtnUp;

	private double yOffset;

	public Mouse() {
		currentPos = new Vector2f().zero();
		resetButtonState();
	}

	public void init(Display window) {
		GLFW.glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
			currentPos.x = (float) xpos;
			currentPos.y = (float) ypos;
		});
		GLFW.glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
			leftBtnDown = button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS;
			rightBtnDown = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS;
			leftBtnHeld = button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_REPEAT;
			rightBtnHeld = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_REPEAT;
			leftBtnUp = button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE;
			rightBtnUp = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_RELEASE;
		});
		GLFW.glfwSetScrollCallback(window.getWindowHandle(), (windowHandle, xoffset, yoffset) -> {
			yOffset = yoffset;
		});
	}

	public void resetButtonState() {
		leftBtnDown = false;
		rightBtnDown = false;
		
		leftBtnHeld = false;
		rightBtnHeld = false;
		
		leftBtnUp = false;
		rightBtnUp = false;
	}
	
	public void update() {

	}

	public boolean isLeftButtonDown() {
		return leftBtnDown;
	}

	public boolean isRightButtonDown() {
		return rightBtnDown;
	}

	public boolean isLeftButtonHeld() {
		return leftBtnHeld;
	}

	public boolean isRightButtonHeld() {
		return rightBtnHeld;
	}
	public boolean isLeftButtonUp() {
		return leftBtnUp;
	}

	public boolean isRightButtonUp() {
		return rightBtnUp;
	}

	public double getScrollOffset() {
		return yOffset;
	}

	public Vector2f getCurrentPos() {
		return currentPos;
	}
	
	public Vector2f getWorldPos(Camera camera) {
		// make origin the middle of the screen
		float x = currentPos.x - Main.WIDTH / 2.0f;
		float y = currentPos.y - Main.HEIGHT / 2.0f;
		
		// undo camera transformations
		Vector2f worldPos = new Vector2f(x, -y);
		worldPos.div(camera.getScale());
		worldPos.add(camera.getPosition());
		
		return worldPos;
	}

	public void setScrollOffset(int offset) {
		yOffset = offset;
	}
}
