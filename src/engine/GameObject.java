package engine;

import org.joml.Vector2f;

public class GameObject {
	
	protected Vector2f position;
	protected float rotation = 0;
	protected float scale = 1;

	protected int depthLayer = 0;
	
	public GameObject(float x, float y) {
		position = new Vector2f(x, y);
	}

	public GameObject() {
		this(0, 0);
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float degrees) {
		rotation = degrees;
	}
	
	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public void translate(float x, float y) {
		this.position.x += x;
		this.position.y += y;
	}
	
	public void rotate(float degrees) {
		rotation += degrees;
	}
	
	public void scale(float amount) {
		scale += amount;
	}

	public int getDepthLayer() {
		return depthLayer;
	}

	public void setDepthLayer(int layer) {
		depthLayer = layer;
	}
}
