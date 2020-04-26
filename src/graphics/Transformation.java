package graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transformation {

	private final Matrix4f projectionMatrix;

	private final Matrix4f transformationMatrix;

	private final Matrix4f viewMatrix;

	public Transformation() {
		projectionMatrix = new Matrix4f();
		transformationMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();
	}

	public Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspect = width / height;
		projectionMatrix.identity();
		projectionMatrix.perspective(fov, aspect, zNear, zFar);
		return projectionMatrix;
	}

	public Matrix4f getOrhtoProjectionMatrix(Camera cam, float width, float height, float scale) {
		projectionMatrix.identity();
		projectionMatrix.ortho2D(-width / 2, width / 2, -height / 2, height / 2);
		projectionMatrix.scale(scale, scale, 1.0f);

		return projectionMatrix;
	}

	public Matrix4f getViewMatrix(Camera cam) {
		Vector2f cameraPos = cam.getPosition();
		float rotation = cam.getRotation();

		viewMatrix.identity();
		// Do the rotation first
		viewMatrix.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1));
		// Now do the translation
		viewMatrix.translate(-cameraPos.x, -cameraPos.y, 0);
		return viewMatrix;
	}

	public Matrix4f getTransformationMatrix(Sprite sprite) {
		transformationMatrix.identity()
				.translate(sprite.getPosition().x, sprite.getPosition().y,
						(float) sprite.getDepthLayer() / 10.0f)
				.rotateZ((float) Math.toRadians(-sprite.getRotation()))
				.scale(sprite.getScale(), sprite.getScale(), 1);
		return transformationMatrix;
	}
}
