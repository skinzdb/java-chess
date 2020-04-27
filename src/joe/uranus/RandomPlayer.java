package joe.uranus;

import java.util.Random;

import chess.Player;

public class RandomPlayer extends Player{
	
	Random rand = new Random();

	@Override
	protected void process() {
		setChosenMove(rand.nextInt(moves.size()));
	}
	
}
