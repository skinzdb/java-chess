package graphics;

import java.util.ArrayList;

public class RenderBlock {
	public Camera camera;
	public ArrayList<Sprite> sprites;
	
	public RenderBlock() {}
	
	public RenderBlock(Camera camera, ArrayList<Sprite> sprites) {
		this.camera = camera;
		this.sprites = sprites;
	}
}
