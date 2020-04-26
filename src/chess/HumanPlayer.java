package chess;

import java.util.Scanner;

public class HumanPlayer extends Player {

	private Scanner scanner = new Scanner(System.in); 
	
	@Override
	protected void process() {
		printMoves();
		
		int input = -1;
				
		do {
			System.out.println("\nEnter move: ");
			if (scanner.hasNextInt()) {
				input = scanner.nextInt();
			} else {
				scanner.nextLine();
			}
		} while (input < 0 || input >= moves.size());
		
		setChosenMove(input);
	}
	
	private void printMoves() {
		System.out.println("\nPossible Moves (" + board.getColour() + "\'s turn):\n");

		for (int i = 0; i < moves.size(); i++) {
			Move m = moves.get(i);
			Piece p = board.getPiece(m.from);

			String moveArrow = " -> ";
			if (p instanceof King && Math.abs(m.to - m.from) == 2) {
				moveArrow = " <> ";
			} else if (!board.getPiece(m.to).isEmpty()) {
				moveArrow = " >> ";
			} else if (p instanceof Pawn && Utility.getMove(m.to).equals(board.getEnPassant())) {
				moveArrow = " ~>> ";
			}

			System.out.printf("%-5s%-20s%s%n", i + ") ", 
					board.getPiece(m.from).getClass().getName(),
					Utility.getMove(m.from) + moveArrow + Utility.getMove(m.to));
		}
	}
}
