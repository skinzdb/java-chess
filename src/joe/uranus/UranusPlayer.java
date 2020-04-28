package joe.uranus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import chess.Board;
import chess.Colour;
import chess.Move;
import chess.Player;

public class UranusPlayer extends Player{

	//private final long treeTime = 1000000000L;
	private final long treeTime = 4000000000L;
	private Colour colour;
	private PrintWriter joesdump;
	
	private Random rand = new Random();
	
	public UranusPlayer(Colour col) {
		colour = col;
	}
	
	@Override
	protected void process() {
		try {
			joesdump = new PrintWriter("JDump.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTimeTarget = System.nanoTime() + treeTime;
		
		int repeats = 0;
		int boardsAdded = 0;
		int boardsMoved = 0;
		int maxOrder = 0;

		HashMap<BoardState, BoardState> visitedStates = new HashMap<BoardState, BoardState>();
		
		Queue<BoardBoredBoardStateStruct> boardStates = new LinkedList<BoardBoredBoardStateStruct>();
		
		BoardState masterState = new BoardState(board);
		//boardStates.add(new BoardBoredBoardStateStruct(board, masterState));
		
		visitedStates.put(masterState, masterState);
		
		for(Move m : moves) {
			Board newBoard = board.move(m);
			newBoard.setupNextMove();
			boardStates.add(new BoardBoredBoardStateStruct(newBoard, masterState));
			boardsMoved++;
		}
		
		while(endTimeTarget > System.nanoTime() && !boardStates.isEmpty()) {
			BoardBoredBoardStateStruct bss = boardStates.remove();
			BoardState boardState = new BoardState(bss.board);
			BoardState parentState = bss.parent;

			boardState = visitedStates.getOrDefault(boardState, boardState);
			parentState = visitedStates.get(parentState);
			
			parentState.children.add(boardState);
			//boardState.ParentStates.add(parentState);
			if(!visitedStates.containsKey(boardState)) {
				visitedStates.put(boardState, boardState);
				boardState.order = parentState.order+1;
				maxOrder = boardState.order;
				boardsAdded++;
				ArrayList<Move> mvs = bss.board.getPossibleMoves();
				if(mvs.size() == 0) {
					joesdump.println("THING WITH 0!");
				}
				for(Move m : mvs) {
					Board newBoard = bss.board.move(m);
					newBoard.setupNextMove();
					boardStates.add(new BoardBoredBoardStateStruct(newBoard, boardState));
					boardsMoved++;
				}
			}
			else {
				repeats++;
			}
		}
		
		HashSet<BoardState> bottomStates = new HashSet<BoardState>();
		
		for (BoardBoredBoardStateStruct bss : boardStates) {
			if(bss.board.getColour() == Colour.WHITE) {
				bss.parent.score = Math.min(bss.parent.score, UUtils.evalBoard(bss.board));
			}
			else {
				bss.parent.score = Math.max(bss.parent.score, UUtils.evalBoard(bss.board));
			}
			//joesdump.println("bss score: " + bss.parent.score);
			bottomStates.add(bss.parent);//sort out the bottom
		}
		
		int best = getBest(masterState);

		//joesdump.println(best);
		System.out.println(UUtils.evalBoard(board) + " -> " + best);
		System.out.println("JStats: " + repeats + " " + boardsAdded + " " + boardsMoved + " " + maxOrder);
		
		ArrayList<Integer> bestMoves = new ArrayList<Integer>();
		int moveIndex = 0;
		for(Move m : moves) {
			Board tBoard = board.move(m);
			tBoard.setupNextMove();
			BoardState bs = new BoardState(tBoard);
			bs = visitedStates.getOrDefault(bs, bs);
			//System.out.println(moveIndex + " " + bs.score);
			if(bs.score == best) {
				bestMoves.add(moveIndex);
			}
			moveIndex++;
		}
		

		//joesdump.close();
		//setChosenMove(0);
		setChosenMove(bestMoves.get(rand.nextInt(bestMoves.size())));
	}
	
	public int getBest(BoardState masterState) {//recursive hell
		//int best = 
		if(!masterState.children.isEmpty()) {
			int best = (masterState.extraData&BoardState.COLOUR_MASK)==0 ? 1000000000 : -1000000000;
			int numUsed = 0;
			for (BoardState child : masterState.children) {
				if(child.order > masterState.order) {
					best = (masterState.extraData&BoardState.COLOUR_MASK)==0 ? Math.min(best, getBest(child)) : Math.max(best, getBest(child));
					numUsed++;
				}
			}
			if (numUsed == 0) {
				masterState.score = UUtils.evalBoard(masterState.toBoard());
				return masterState.score;
			}
			masterState.score = best;
			return best;
		}
		else {
			//joesdump.println("sc: " + masterState.score);
			return masterState.score;
		}
	}

}
