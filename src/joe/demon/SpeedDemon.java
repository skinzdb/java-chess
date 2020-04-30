package joe.demon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import chess.Board;
import chess.Colour;
import chess.Move;
import chess.Piece;
import chess.Player;
import joe.uranus.UUtils;
import joe.uranus.UranusPlayer;

public class SpeedDemon extends Player {

	Random rand = new Random();
	
	@Override
	protected void process() {

		int wPieces = 0;
		int bPieces = 0;
		for(Piece p : board.getPieces()) {
			if(!p.isEmpty()) {
				if(p.getColour() == Colour.WHITE) {
					wPieces++;
				}
				if(p.getColour() == Colour.BLACK) {
					bPieces++;
				}
			}
		}
		//if(Math.min(wPieces, bPieces)<5) {
		//	Player uPlayer = new UranusPlayer(Colour.WHITE);
		//	uPlayer.makeMove(board, moves);
		//	return;
		//}
		
		ArrayList<ScoredBoard> boards = exploreBoards(5);
		
		ArrayList<Board> moveBoards = new ArrayList<Board>();
		for(Move move : board.getPossibleMoves()) {
			Board nextBoard = board.move(move);
			nextBoard.setupNextMove();
			moveBoards.add(nextBoard);
		}

		if(boards.size() == 0) {
			setChosenMove(rand.nextInt(moves.size()));
			System.out.println("rand");
			return;
		}
		
		ArrayList<Integer> bestBoards = new ArrayList<Integer>();
		if(boards.get(0).board.getColour() == Colour.WHITE) {
			int bestScore = 1000000000;
			for(ScoredBoard scoredBoard : boards) {
				if(scoredBoard.score < bestScore) {
					bestBoards = new ArrayList<Integer>();
					for(int i = 0; i < moveBoards.size(); i++) {
						
						if(moveBoards.get(i).getGameMoves().get(moveBoards.get(i).getGameMoves().size()-1).equals(
								scoredBoard.board.getGameMoves().get(moveBoards.get(i).getGameMoves().size()-1))) {
							bestBoards.add(i);
						}
					}
				}
				else if(scoredBoard.score == bestScore) {
					for(int i = 0; i < moveBoards.size(); i++) {
						if(moveBoards.get(i).getGameMoves().get(moveBoards.get(i).getGameMoves().size()-1).equals(
								scoredBoard.board.getGameMoves().get(moveBoards.get(i).getGameMoves().size()-1))) {
							bestBoards.add(i);
						}
					}
				}
			}
		}
		else {
			int bestScore = -1000000000;
			for(ScoredBoard scoredBoard : boards) {
				if(scoredBoard.score > bestScore) {
					bestBoards = new ArrayList<Integer>();
					for(int i = 0; i < moveBoards.size(); i++) {
						
						if(moveBoards.get(i).getGameMoves().get(moveBoards.get(i).getGameMoves().size()-1).equals(
								scoredBoard.board.getGameMoves().get(moveBoards.get(i).getGameMoves().size()-1))) {
							bestBoards.add(i);
						}
					}
				}
				else if(scoredBoard.score == bestScore) {
					for(int i = 0; i < moveBoards.size(); i++) {
						if(moveBoards.get(i).getGameMoves().get(moveBoards.get(i).getGameMoves().size()-1).equals(
								scoredBoard.board.getGameMoves().get(moveBoards.get(i).getGameMoves().size()-1))) {
							bestBoards.add(i);
						}
					}
				}
			}
		}
		
		if(bestBoards.size() == 0) {
			setChosenMove(rand.nextInt(moves.size()));
			System.out.println(rand);
		}
		else {
			setChosenMove(bestBoards.get(rand.nextInt(bestBoards.size())));
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
		if(prevSet.size() == 0) return new ArrayList<ScoredBoard>();
		ArrayList<ScoredBoard> out = new ArrayList<ScoredBoard>();
		ArrayList<ScoredBoard> scoredBoards = new ArrayList<ScoredBoard>();
		for(ScoredBoard scBoard : prevSet) {
			for(Move move : scBoard.board.getPossibleMoves()) {
				Board nextBoard = scBoard.board.move(move);
				nextBoard.setupNextMove();
				scoredBoards.add(new ScoredBoard(nextBoard, UUtils.evalBoard(nextBoard)));
			}
		}
		if(scoredBoards.size() == 0) return new ArrayList<ScoredBoard>();
		int maxScore = prevSet.get(0).board.getColour()==Colour.WHITE?-100000000:1000000000;
		int totalScore = 0;
		int totalSquare = 0;
		for(ScoredBoard scb : scoredBoards) {
			totalScore += scb.score;
			totalSquare += scb.score*scb.score;
			maxScore = prevSet.get(0).board.getColour()==Colour.WHITE?Math.max(maxScore, scb.score):Math.min(maxScore, scb.score);
		}
		int meanScore = totalScore/scoredBoards.size();
		int meanSquare = totalSquare/scoredBoards.size();
		
		long variance = meanSquare - meanScore*meanScore;
		long stdDiv = (long) Math.sqrt(variance);

		for(ScoredBoard scb : scoredBoards) {
			if(Math.abs(scb.score-maxScore)-1 < stdDiv*1f) {
				out.add(scb);
			}
		}
		return out;
	}

}
