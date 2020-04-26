package shaders;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

public class Shader {

	private final int programId;

	private int vertexShaderId;

	private int fragmentShaderId;

	private final Map<String, Integer> uniforms;

	public Shader() throws Exception {
		programId = glCreateProgram();
		if (programId == 0) {
			throw new Exception("Could not create Shader");
		}
		uniforms = new HashMap<String, Integer>();
	}

	public void createVertexShader(String shaderCode) throws Exception {
		vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
	}

	public void createFragmentShader(String shaderCode) throws Exception {
		fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
	}

	protected int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = glCreateShader(shaderType);
		if (shaderId == 0) {
			throw new Exception("Error creating shader. Type: " + shaderType);
		}

		glShaderSource(shaderId, shaderCode);
		glCompileShader(shaderId);

		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
		}

		glAttachShader(programId, shaderId);

		return shaderId;
	}

	public void createUniform(String uniformName) throws Exception {
		int uniformLocation = glGetUniformLocation(programId, uniformName);
		if (uniformLocation < -1) {
			throw new Exception("Could not find the uniform: " + uniformName);
		}
		uniforms.put(uniformName, uniformLocation);
	}

	public void setUniform(String uniformName, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
		}
	}

	public void setUniform(String uniformName, int val) {
		GL20.glUniform1i(uniforms.get(uniformName), val);
	}

	public void setUniform(String uniformName, float val) {
		GL20.glUniform1f(uniforms.get(uniformName), val);
	}

	public void setUniform(String uniformName, Vector2f vector) {
		GL20.glUniform2f(uniforms.get(uniformName), vector.x, vector.y);
	}

	public void setUniform(String uniformName, Vector3f vector) {
		GL20.glUniform3f(uniforms.get(uniformName), vector.x, vector.y, vector.z);
	}

	public void setUniform(String uniformName, Vector4f value) {
		GL20.glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
	}

	public void setUniform(String uniformName, boolean val) {
		GL20.glUniform1f(uniforms.get(uniformName), val ? 1 : 0);
	}

	public void setUniformVec3Arr(String uniformName, float[] value) {
		GL20.glUniform3fv(uniforms.get(uniformName), value);
	}

	public void link() throws Exception {
		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
		}

		if (vertexShaderId != 0) {
			glDetachShader(programId, vertexShaderId);
		}
		if (fragmentShaderId != 0) {
			glDetachShader(programId, fragmentShaderId);
		}

		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
			System.out.println("Warning validating Shader code: " + glGetShaderInfoLog(programId, 1024));
		}
	}

	public void bind() {
		glUseProgram(programId);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void cleanup() {
		unbind();
		if (programId != 0) {
			glDeleteProgram(programId);
		}
	}
}