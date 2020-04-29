package graphics;

public class GUIText extends GUIImage {
	
	private String text;
	
	public GUIText(String text, float x, float y, int width, int height, float scale) {
		super(GUI.DEFAULT_FONT, x, y, width, height, scale);
		
		this.text = text;
		
		draw();
	}
	
	public GUIText(String text, float x, float y, float scale) {
		this(text, x, y, 100, 100, scale);
	}
	
	@Override
	public void draw() {
		sprites.clear();
		
		if (foreground == null || text == null)
			return;

		String[] lines = text.split("\n");		
		int extraLines = 0; // counter to show how many times text exceeds max width
		int xOff = 0;

		for (int i = 0; i < lines.length; i++) {
			if (i > height) return; // reached maximum height so stop drawing
			char[] chars = lines[i].toCharArray();
			for (int j = 0; j < chars.length; j++) {
				xOff = j % width; // keep char position within width
				if (xOff == 0 & j != 0)	// exceeded max width so add an extra line
					extraLines++;
				Sprite s = new Sprite(
						position.x + xOff * scale, 
						position.y - (i + extraLines) * scale,
						Geometry.quad, foreground
						);
				s.setPointer(chars[j]);
				s.setScale(scale);
				s.setDepthLayer(10);
				sprites.add(s);

				if (background != null) { // draw background
					Sprite b = new Sprite(Geometry.quad, background);
					b.setPointer(0);
					b.setPosition(s.getPosition().x, s.getPosition().y);
					b.setScale(s.getScale());
					b.setDepthLayer(9);
					sprites.add(b);
				}
			}
		}
	}
	
	public void setText(String text) {
		this.text = text;
		draw();
	}
	
	public void setFont(Texture font) {
		this.foreground = font;
	}
}
