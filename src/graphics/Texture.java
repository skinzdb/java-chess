package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class Texture {

	private final int id;

	private final int atlasSize;

	public Texture(String fileName, int atlasSize) {
		this.id = loadTexture(fileName);
		this.atlasSize = atlasSize;
	}

	public int getId() {
		return id;
	}

	public int getAtlasSize() {
		return atlasSize;
	}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	private int loadTexture(String fileName) {
		BufferedImage bI;
		int textureId = -1;
		try {
			bI = ImageIO.read(new File(fileName));
			int width = bI.getWidth();
			int height = bI.getHeight();

			int[] rawPixels = new int[width * height * 4];
			rawPixels = bI.getRGB(0, 0, width, height, null, 0, width);

			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					int pixel = rawPixels[i * width + j];
					pixels.put((byte) ((pixel >> 16) & 0xFF)); // RED
					pixels.put((byte) ((pixel >> 8) & 0xFF)); // GREEN
					pixels.put((byte) (pixel & 0xFF)); // BLUE
					pixels.put((byte) ((pixel >> 24) & 0xFF)); // ALPHA
				}
			}
			pixels.flip();

			textureId = GL11.glGenTextures();
			// Bind the texture
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

			// Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

			// Upload the texture data
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA,
					GL11.GL_UNSIGNED_BYTE, pixels);
			// Generate mipmap
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return textureId;
	}

	public void cleanup() {
		GL11.glDeleteTextures(id);
	}
}
