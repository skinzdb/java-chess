package graphics;

public class Animation {
	private final int start;
	private final int finish;
	private int frame;

	private float frameTime = 1.0f / 20.0f;

	private float elapsed = 0;

	private boolean isPlaying = false;
	private boolean loop = false;

	public Animation(int start, int finish, float speed) {
		this.start = start;
		frame = start;
		this.finish = finish;
		this.frameTime = speed;
	}

	public void update(float interval) {
		if (isPlaying) {
			elapsed += interval;

			if (elapsed > frameTime) {
				incrementFrame();
				elapsed = 0;
			}
		}
	}

	public void play() {
		isPlaying = true;
	}

	public void stop() {
		elapsed = 0;
		frame = start;
		isPlaying = false;
	}

	public void incrementFrame() {
		frame++;

		if (frame > finish) {
			frame = start;
			if (!loop) stop();
		}
	}

	public int getFrame() {
		return frame;
	}

	public float getElapsed() {
		return elapsed;
	}

	public void setElapsed(float elapsed) {
		this.elapsed = elapsed;
	}

	public void addTime(float interval) {
		elapsed += interval;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public boolean isLooping() {
		return loop;
	}

	public void setLooping(boolean loop) {
		this.loop = loop;
	}

	public int getStart() {
		return start;
	}

	public int getFinish() {
		return finish;
	}

	public float getFrameTime() {
		return frameTime;
	}

	public void setFrameTime(float fps) {
		this.frameTime = 1.0f / fps;
	}

	public boolean equals(Animation anim) {
		return 	start == anim.getStart() && 
				finish == anim.getFinish() && 
				frameTime == anim.getFrameTime() && 
				loop == anim.isLooping();
	}
}
