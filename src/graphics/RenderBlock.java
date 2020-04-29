package graphics;

import java.util.ArrayList;

import engine.GameObject;

public class RenderBlock extends GameObject {

	protected Camera cam;
	protected ArrayList<Sprite> sprites;

	public RenderBlock(Camera camera, ArrayList<Sprite> sprites) {
		cam = camera;
		this.sprites = sprites;
	}
	
	public void add(Sprite sprite) {
		sprites.add(sprite);
	}
	
	public void remove(Sprite sprite) {
		sprites.remove(sprite);
	}
	
	public void clear() {
		sprites.clear();
	}
	
	@Override
	public void setPosition(float x, float y) {
		float dx = x - position.x;
		float dy = y - position.y;
		super.setPosition(x, y);
		for (int i = 0; i < sprites.size(); i++) {
			sprites.get(i).translate(dx, dy);
		}
	}
	
	@Override
	public void translate(float x, float y) { 
		super.translate(x, y);
		for (int i = 0; i < sprites.size(); i++) {
			sprites.get(i).translate(x, y);
		}
	}

	public Camera getCamera() {
		return cam;
	}

	public void setCamera(Camera cam) {
		this.cam = cam;
	}

	public ArrayList<Sprite> getSprites() {
		return sprites;
	}

	public void setSprites(ArrayList<Sprite> sprites) {
		this.sprites = sprites;
	}
}
