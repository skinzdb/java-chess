package chess;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GameSaver {

    public static void saveGame(String filename, Board board) {
        BufferedWriter out;

        try {
            out = new BufferedWriter(new FileWriter(filename));

            for (String move : board.getGameMoves()) {
                out.write(move);
                out.newLine();
            }

            for (Piece piece : board.getTakenPieces()) {
                out.write(piece.getFEN());
            }
            out.newLine();

            out.close();

        } catch (IOException e) {
            //MessageBox.display("Error", "Error saving game file");
        }
    }
}
