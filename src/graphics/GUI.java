package graphics;

import java.util.ArrayList;

public class GUI {
	
	public static Texture DEFAULT_BG = new Texture("res/white.png", 1);
	
	public static Texture DEFAULT_FONT = new Texture("res/ascii.png", 16);
	
	private Camera guiCam;
	
	private ArrayList<RenderBlock> guiObjects;
	
	public GUI() {
		guiCam = new Camera();
		guiObjects = new ArrayList<>();
	}
	
	public void add(RenderBlock rb) {
		rb.setCamera(guiCam);
		guiObjects.add(rb);
	}
	
	public void remove(RenderBlock rb) {
		guiObjects.remove(rb);
	}
	
	public void clear() {
		guiObjects.clear();
	}
	
	public GUIImage image(Texture image, float x, float y, int width, int height, float scale) {
		GUIImage guiImg = new GUIImage(image, x, y, width, height, scale);
		add(guiImg);
		return guiImg;
	}
	
	public GUIText text(String text, float x, float y, int width, int height, float scale) {
		GUIText guiText = new GUIText(text, x, y, width, height, scale);
		add(guiText);
		return guiText;
	}
	
	public ArrayList<RenderBlock> getGUIObjects() {
		return guiObjects;
	}

	public Camera getCam() {
		return guiCam;
	}

	public void setCam(Camera cam) {
		this.guiCam = cam;
	}
	
	
	
}
