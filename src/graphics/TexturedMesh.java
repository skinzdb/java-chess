package graphics;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import org.lwjgl.opengl.GL13;

public class TexturedMesh {

    private Mesh mesh;

    private Texture texture;

    public TexturedMesh(Mesh mesh, Texture texture) {
		this.mesh = mesh;
		this.texture = texture;
    }

    public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

    public void render() {
        if (texture != null) {
			// Activate first texture bank
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			// Bind the texture
			glBindTexture(GL_TEXTURE_2D, texture.getId());
		}

        mesh.render();
    }

    public void cleanup() {
        mesh.cleanup();
        // Delete the texture
        if (texture != null) {
            texture.cleanup();
        }
    }
    
}