package input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import engine.Display;

public class Mouse {
	private final Vector2d currentPos;

	private final Vector2f displayVec;

	private boolean leftBtnDown = false;
	private boolean rightBtnDown = false;

	private boolean leftBtnHeld = false;
	private boolean rightBtnHeld = false;

	private boolean leftBtnUp = false;
	private boolean rightBtnUp = false;

	private double yOffset;

	public Mouse() {
		currentPos = new Vector2d().zero();
		displayVec = new Vector2f();
	}

	public void init(Display window) {
		GLFW.glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
			currentPos.x = xpos;
			currentPos.y = ypos;
		});
		GLFW.glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
			leftBtnDown = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
			rightBtnDown = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
			leftBtnHeld = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_REPEAT;
			rightBtnHeld = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_REPEAT;
			leftBtnUp = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_RELEASE;
			rightBtnUp = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_RELEASE;
		});
		GLFW.glfwSetScrollCallback(window.getWindowHandle(), (windowHandle, xoffset, yoffset) -> {
			yOffset = yoffset;
		});
	}

	public void update() {

	}

	public Vector2f getDisplayVec() {
		return displayVec;
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

	public Vector2d getCurrentPos() {
		return currentPos;
	}

	public void setScrollOffset(int offset) {
		yOffset = offset;
	}
}
