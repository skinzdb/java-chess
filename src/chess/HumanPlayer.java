package chess;

import org.joml.Vector2f;

import graphics.Camera;
import input.Mouse;

public class HumanPlayer extends Player {

	private Camera cam;
	private Mouse mouse;
	
	private int currentSelection; // last square player clicked
	
	public HumanPlayer(Camera cam, Mouse mouse) {
		this.cam = cam;
		this.mouse = mouse;
	}
	
	@Override
	protected void process() {
		currentSelection = -1; 
		setSelectedSquare(-1); 
		
		System.out.println(board.getColour() + "'s turn\n");
		
		while (getChosenMove() == -1 && isRunning()) {
			Vector2f mousePos = mouse.getWorldPos(cam);
			
			int x = Math.round(mousePos.x);
			int y = Math.round(mousePos.y);
			
			if (mouse.isLeftButtonDown()) {	
				if (x >= 0 && x < 8 && y > -8 && y <= 0) { // check mouse is within board coordinates
					int index = y * -8 + x;	// calculate index on board
					
					if (board.getPiece(index).getColour() != board.getColour() && currentSelection == -1) { // don't allow selection of empty or enemy pieces
						continue;
					}
					
					if (currentSelection != -1 && index != currentSelection) { // if there is already a selected square and player clicks on a different square
						Move move = new Move(currentSelection, index);
						int moveIndex = moves.indexOf(move); // check if the move is valid, returns -1 if invalid
						if (moveIndex != -1) {
							setChosenMove(moveIndex); // make move
							break;
						} else { // reset selection if move is invalid
							currentSelection = -1;
							setSelectedSquare(-1);
							continue;
						}
					}
					// reset selection if player clicks on same square twice
					currentSelection = currentSelection == index ? -1 : index;
					setSelectedSquare(currentSelection);
				}
			}
			mouse.resetButtonState();
			try {
	            Thread.sleep(50); // update every 50ms
	        } catch (InterruptedException e) {
	        }
		}

	}
	
	//private Scanner scanner = new Scanner(System.in); 
	
	/*
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
	*/
	
	/*
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
	*/
}
