package graphics;

import java.util.ArrayList;

import engine.GameObject;

public class Sprite extends GameObject implements IAnimable {

	protected final TexturedMesh mesh;

	protected boolean collidable;

	protected int pointer = 0; // Number that corresponds to the texture in the atlas

	private ArrayList<Animation> anims;
	private Animation currentAnim;

	public Sprite(float x, float y, Mesh mesh, Texture texture) {
		super(x, y);

		this.mesh = new TexturedMesh(mesh, texture);
		anims = new ArrayList<>();
	}

	public Sprite(Mesh mesh, Texture texture) {
		this(0, 0, mesh, texture);
	}

	public int getPointer() {
		return pointer;
	}

	public void setPointer(int pointer) {
		this.pointer = pointer;
	}

	public TexturedMesh getMesh() {
		return mesh;
	}

	public Texture getTexture() {
		return mesh.getTexture();
	}

	public void setTexture(Texture texture) {
		mesh.setTexture(texture);
	}

	public boolean isCollidable() {
		return collidable;
	}

	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}

	public float[] getTexOffsets() {
		int atlasSize = getTexture().getAtlasSize();

		return new float[] { (pointer % atlasSize) / (float) atlasSize,
				(pointer / atlasSize) / (float) atlasSize };
	}

	@Override
	public void addAnim(Animation anim) {
		anims.add(anim);
	}

	@Override
	public void updateAnim(float interval) {
		if (currentAnim != null) {
			currentAnim.update(interval);
			setPointer(currentAnim.getFrame());
		}
	}

	@Override
	public void play(int animIndex) {
		currentAnim = anims.get(animIndex);
		if (currentAnim == null) return;

		currentAnim.play();
	}

	@Override
	public void stop() {
		if (currentAnim != null) currentAnim.stop();
	}
}
