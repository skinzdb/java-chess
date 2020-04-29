package graphics;

import java.util.ArrayList;

import org.joml.Vector2f;

public class GUIImage extends RenderBlock {
	
	protected int width;
	protected int height;
	
	protected Texture background;
	protected Texture foreground;
	
	protected ArrayList<Sprite> fgSprites;
	
	public GUIImage(Texture image, float x, float y, int width, int height, float scale) {
		super(new Camera(), new ArrayList<>());
		
		position = new Vector2f(x, y);
		
		this.width = width;
		this.height = height;
		this.scale = scale;
		
		background = GUI.DEFAULT_BG;
		foreground = image;
		
		fgSprites = new ArrayList<>();
		
		draw();
	}
	
	public GUIImage(Texture image, float x, float y, float scale) {
		this(image, x, y, 1, 1, scale);
	}
	
	public void draw() {
		clear();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int index = i * width + j;
				
				if (background != null) {
					Sprite b = new Sprite(Geometry.quad, background);
					b.setPointer(0);
					b.setPosition(position.x + j * scale, position.y - i * scale);
					b.setScale(scale);
					b.setDepthLayer(8);
					sprites.add(b);
				}
				
				if (foreground != null) {
					if (index >= fgSprites.size()) continue;
					
					Sprite s = fgSprites.get(index);
					if (s == null) continue;
					
					s.setTexture(foreground);
					s.setPosition(position.x + j * scale, position.y - i * scale);
					s.setScale(scale);
					s.setDepthLayer(10);
					sprites.add(s);
				}
			}
		}
	}

	public void setBackground(Texture background) {
		this.background = background;
		draw();
	}

	public void setForeground(Texture foreground) {
		this.foreground = foreground;
		draw();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		draw();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		draw();
	}
	
	@Override
	public void setSprites(ArrayList<Sprite> sprites) {
		this.fgSprites = sprites;
		draw();
	}
}
