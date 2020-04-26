package graphics;

public class Geometry {
	
	/*
	 * Quad
	 */
	
	private final static float[] quadPositions = new float[] { 
			-0.5f, -0.5f, 0.0f, 
			0.5f, -0.5f, 0.0f, 
			0.5f, 0.5f, 0.0f, 
			-0.5f, 0.5f, 0.0f, 
	};

	private final static float[] quadTexCoords = new float[] { 
			0, 1, 
			1, 1, 
			1, 0, 
			0, 0 
	};
	
	private final static int[] quadIndices = new int[] { 0, 1, 2, 2, 3, 0, };

	public final static Mesh quad = new Mesh(quadPositions, quadTexCoords, quadIndices);

	public static Mesh quad() {
		return quad;
	}
}
