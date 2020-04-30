package chess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameLoader {

	public static Board load(String filename) {
		Board board = new Board();

		try {
			List<String> FEN = readFile(new File(filename));

			if (FEN == null) {
				return null;
			}

			for (int i = 0; i < FEN.size(); i++) {
				String line = FEN.get(i);

				if (line.startsWith("~")) { // line that stores taken pieces starts with tilde, ~
					for (String piece : line.split("")) {
						board.getTakenPieces().add(Utility.determinePiece(piece));
					}
				} else {
					board.getGameMoves().add(line);
					if (i == FEN.size() - 1) Utility.loadFEN(board, line);
				}
			}
			
		board.checkAll();

		} catch (Exception e) {
			e.printStackTrace();
			//MessageBox.display("Error", "Could not load game file");
			return null;
		}

		return board;
	}

	public static Board loadDefault() {
		Board board = new Board();

		Utility.loadFEN(board, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -");
		board.getGameMoves().add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -");

		return board;
	}

	private static List<String> readFile(File file) {
		List<String> out = new ArrayList<String>();
		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader(new FileReader(file));

			String text;
			while ((text = bufferedReader.readLine()) != null) {
				out.add(text);
			}

		} catch (FileNotFoundException ex) {
			//MessageBox.display("Error", "File doesn't exist");
			return null;
		} catch (IOException ex) {
			//MessageBox.display("Error", "I/O error");
			return null;
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException ex) {
				//MessageBox.display("Error", "I/O error");
				return null;
			}
		}
		return out;
	}

}
