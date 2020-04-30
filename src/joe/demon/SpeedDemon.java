package joe.demon;

import java.util.ArrayList;
import java.util.LinkedList;

import chess.Board;
import chess.Colour;
import chess.Move;
import chess.Player;
import joe.uranus.UUtils;

public class SpeedDemon extends Player {

	@Override
	protected void process() {
		
		ArrayList<ScoredBoard> boards = exploreBoards(4);
		
		ArrayList<Board> moveBoards = new ArrayList<Board>();
		for(Move move : board.getPossibleMoves()) {
			Board nextBoard = board.move(move);
			nextBoard.setupNextMove();
			moveBoards.add(nextBoard);
		}
		
		
		
	}
	
	private ArrayList<ScoredBoard> exploreBoards(int depth) {
		ArrayList<ScoredBoard> boards = new ArrayList<ScoredBoard>();
		boards.add(new ScoredBoard(board, 0));
		for(int i = 0; i < depth; i++) {
			boards = getBoardsToExplore(boards);
		}
		return boards;
	}
	
	private ArrayList<ScoredBoard> getBoardsToExplore(ArrayList<ScoredBoard> prevSet) {
		ArrayList<ScoredBoard> out = new ArrayList<ScoredBoard>();
		ArrayList<ScoredBoard> scoredBoards = new ArrayList<ScoredBoard>();
		for(ScoredBoard scBoard : prevSet) {
			for(Move move : scBoard.board.getPossibleMoves()) {
				Board nextBoard = scBoard.board.move(move);
				nextBoard.setupNextMove();
				scoredBoards.add(new ScoredBoard(nextBoard, UUtils.evalBoard(nextBoard)));
			}
		}
		int maxScore = prevSet.get(0).board.getColour()==Colour.WHITE?100000000:-1000000000;
		int totalScore = 0;
		int totalSquare = 0;
		for(ScoredBoard scb : scoredBoards) {
			totalScore += scb.score;
			totalSquare += scb.score*scb.score;
			maxScore = prevSet.get(0).board.getColour()==Colour.WHITE?Math.min(maxScore, scb.score):Math.max(maxScore, scb.score);
		}
		int meanScore = totalScore/scoredBoards.size();
		int meanSquare = totalSquare/scoredBoards.size();
		
		long variance = meanSquare - meanScore*meanScore;
		long stdDiv = (long) Math.sqrt(variance);

		for(ScoredBoard scb : scoredBoards) {
			if(Math.abs(scb.score-maxScore) < stdDiv) {
				out.add(scb);
			}
		}
		return out;
	}

}
