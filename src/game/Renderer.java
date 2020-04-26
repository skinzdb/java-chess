package game;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import engine.Display;
import engine.Utils;
import graphics.Camera;
import graphics.Light;
import graphics.Sprite;
import graphics.TexturedMesh;
import graphics.Transformation;
import shaders.Shader;

public class Renderer {

	private Shader shaderProgram;

	private final Transformation transformation;
	
	private Display window;

	public static final int MAX_LIGHTS = 8;

	public Renderer() {
		transformation = new Transformation();
	}

	public void init(Display window) throws Exception {
		this.window = window;
		shaderProgram = new Shader();
		shaderProgram.createVertexShader(Utils.loadResource("/shaders/Vertex.vs"));
		shaderProgram.createFragmentShader(Utils.loadResource("/shaders/Fragment.fs"));
		shaderProgram.link();

		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("viewMatrix");
		shaderProgram.createUniform("transformationMatrix");
		shaderProgram.createUniform("textureSampler");

		// Texture atlas variables
		shaderProgram.createUniform("atlasSize");
		shaderProgram.createUniform("offset");

		// Lighting
		shaderProgram.createUniform("minLight");
		shaderProgram.createUniform("maxLight");
		shaderProgram.createUniform("light");
		shaderProgram.createUniform("lightAtt");
		shaderProgram.createUniform("lightCol");

		window.setClearColour(0.0f, 0.0f, 0.0f, 0.0f);

		// Don't draw back faces
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		// Enable depth
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);

		// Enable transparency
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void clear() {
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void render(Camera camera, ArrayList<Sprite> sprites, boolean clear) {
		setMinLight(camera.getMinBright());
		setMaxLight(camera.getMaxBright());
		if (clear)
			clear();

		if (window.isResized()) {
			GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		shaderProgram.bind();

		// Update projection matrix
		Matrix4f projectionMatrix = transformation.getOrhtoProjectionMatrix(camera, window.getWidth(),
				window.getHeight(), camera.getScale());
		shaderProgram.setUniform("projectionMatrix", projectionMatrix);

		// Update view matrix
		Matrix4f viewMatrix = transformation.getViewMatrix(camera);
		shaderProgram.setUniform("viewMatrix", viewMatrix);

		shaderProgram.setUniform("textureSampler", 0);

		// Render each gameObject
		if (sprites != null) {
			for (Sprite sprite : sprites) {
				TexturedMesh mesh = sprite.getMesh();

				// Set the model view matrix for this object
				Matrix4f transformationMatrix = transformation.getTransformationMatrix(sprite);
				shaderProgram.setUniform("transformationMatrix", transformationMatrix);

				shaderProgram.setUniform("atlasSize", mesh.getTexture().getAtlasSize());

				float[] offsets = sprite.getTexOffsets();
				shaderProgram.setUniform("offset", new Vector2f(offsets[0], offsets[1]));
				mesh.render();
			}
		}

		shaderProgram.unbind();
	}

	public void setLights(ArrayList<Light> lights) {
		shaderProgram.bind();

		float[] lightData = new float[3 * MAX_LIGHTS];
		float[] lightAttData = new float[3 * MAX_LIGHTS];
		float[] lightColData = new float[3 * MAX_LIGHTS];
		
		for(int i = 0; i < lightAttData.length; i++) {
			lightAttData[i] = 1;
		}
		
		for (int i = 0; i < lights.size() && i < MAX_LIGHTS; i++) {

			Light light = lights.get(i);

			lightData[i * 3] = light.getPosition().x;
			lightData[i * 3 + 1] = light.getPosition().y;
			lightData[i * 3 + 2] = light.getIntensity();
			
			lightAttData[i * 3] = light.getAttenuation().x;
			lightAttData[i * 3 + 1] = light.getAttenuation().y;
			lightAttData[i * 3 + 2] = light.getAttenuation().z;
			
			lightColData[i * 3] = light.getColour().x;
			lightColData[i * 3 + 1] = light.getColour().y;
			lightColData[i * 3 + 2] = light.getColour().z;
		}

		shaderProgram.setUniformVec3Arr("light", lightData);
		shaderProgram.setUniformVec3Arr("lightAtt", lightAttData);
		shaderProgram.setUniformVec3Arr("lightCol", lightColData);

		shaderProgram.unbind();
	}

	public void setMinLight(float value) {
		shaderProgram.bind();
		shaderProgram.setUniform("minLight", value);
		shaderProgram.unbind();
	}

	public void setMaxLight(float value) {
		shaderProgram.bind();
		shaderProgram.setUniform("maxLight", value);
		shaderProgram.unbind();
	}

	public void cleanup() {
		if (shaderProgram != null) {
			shaderProgram.cleanup();
		}
	}
}