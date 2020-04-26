package engine;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import graphics.Mesh;

public class OBJLoader {

	public static Mesh loadMesh(String fileName) throws Exception {
		List<String> lines = Utils.readAllLines(fileName);

		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Face> faces = new ArrayList<>();

		for (String line : lines) {
			String[] tokens = line.split("\\s+");
			switch (tokens[0]) {
			case "v":
				// Geometric vertex
				Vector3f vec3f = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3]));
				vertices.add(vec3f);
				break;
			case "vt":
				// Texture coordinate
				Vector2f vec2f = new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
				textures.add(vec2f);
				break;
			case "vn":
				// Vertex normal
				Vector3f vec3fNorm = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3]));
				normals.add(vec3fNorm);
				break;
			case "f":
				Face face = new Face(tokens[1], tokens[2], tokens[3]);
				faces.add(face);
				break;
			default:
				// Ignore other lines
				break;
			}
		}
		return reorderLists(vertices, textures, normals, faces);
	}

	private static Mesh reorderLists(List<Vector3f> posList, List<Vector2f> texCoordList, List<Vector3f> normList,
			List<Face> facesList) {

		List<Integer> indices = new ArrayList<Integer>();
		// Create position array in the order it has been declared
		float[] posArr = new float[posList.size() * 3];
		int i = 0;
		for (Vector3f pos : posList) {
			posArr[i * 3] = pos.x;
			posArr[i * 3 + 1] = pos.y;
			posArr[i * 3 + 2] = pos.z;
			i++;
		}
		float[] texCoordArr = new float[posList.size() * 2];
		float[] normArr = new float[posList.size() * 3];

		for (Face face : facesList) {
			IndexGroup[] faceVertexIndices = face.getFaceVertexIndices();
			for (IndexGroup indexValue : faceVertexIndices) {
				processFaceVertex(indexValue, texCoordList, normList, indices, texCoordArr, normArr);
			}
		}
		int[] indicesArr = new int[indices.size()];
		indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
		Mesh mesh = new Mesh(posArr, texCoordArr, indicesArr);
		return mesh;
	}

	private static void processFaceVertex(IndexGroup indices, List<Vector2f> texCoordList, List<Vector3f> normList,
			List<Integer> indicesList, float[] texCoordArr, float[] normArr) {

		// Set index for vertex coordinates
		int posIndex = indices.indexPos;
		indicesList.add(posIndex);

		// Reorder texture coordinates
		if (indices.indexTexCoord >= 0) {
			Vector2f texCoord = texCoordList.get(indices.indexTexCoord);
			texCoordArr[posIndex * 2] = texCoord.x;
			texCoordArr[posIndex * 2 + 1] = 1 - texCoord.y;
		}
		if (indices.indexVecNormal >= 0) {
			// Reorder vector normals
			Vector3f vecNorm = normList.get(indices.indexVecNormal);
			normArr[posIndex * 3] = vecNorm.x;
			normArr[posIndex * 3 + 1] = vecNorm.y;
			normArr[posIndex * 3 + 2] = vecNorm.z;
		}
	}

	protected static class Face {

		/**
		 * List of idxGroup groups for a face triangle (3 vertices per face).
		 */
		private IndexGroup[] indexGroups = new IndexGroup[3];

		public Face(String v1, String v2, String v3) {
			indexGroups = new IndexGroup[3];
			// Parse the lines
			indexGroups[0] = parseLine(v1);
			indexGroups[1] = parseLine(v2);
			indexGroups[2] = parseLine(v3);
		}

		private IndexGroup parseLine(String line) {
			IndexGroup indexGroup = new IndexGroup();

			String[] lineTokens = line.split("/");
			int length = lineTokens.length;
			indexGroup.indexPos = Integer.parseInt(lineTokens[0]) - 1;
			if (length > 1) {
				// It can be empty if the obj does not define text coords
				String textCoord = lineTokens[1];
				indexGroup.indexTexCoord = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : IndexGroup.NO_VAL;
				if (length > 2) {
					indexGroup.indexVecNormal = Integer.parseInt(lineTokens[2]) - 1;
				}
			}

			return indexGroup;
		}

		public IndexGroup[] getFaceVertexIndices() {
			return indexGroups;
		}
	}

	protected static class IndexGroup {

		public static final int NO_VAL = -1;

		public int indexPos;

		public int indexTexCoord;

		public int indexVecNormal;

		public IndexGroup() {
			indexPos = NO_VAL;
			indexTexCoord = NO_VAL;
			indexVecNormal = NO_VAL;
		}
	}
}
