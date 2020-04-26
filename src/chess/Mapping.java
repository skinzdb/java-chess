package chess;

import java.util.ArrayList;

public class Mapping {

    public static ArrayList<Integer> getCheckMoves(int index, Board board,
            ArrayList<Integer> moveMap) {
        ArrayList<Integer> newMap = new ArrayList<Integer>();
        
        for (int i = 0; i < moveMap.size(); i++) {
            int to = moveMap.get(i);

            Board tmpBoard = board.move(index, to);
            tmpBoard.justCheck();
            
            if (!tmpBoard.isCheck()) {
                newMap.add(to);
            }
        }

        return newMap;
    }

    public static ArrayList<Integer> getStraightMoves(int index, Piece[] pieces) {
        ArrayList<Integer> moveMap = new ArrayList<Integer>();
        // left
        for (int i = index - 1; i > Utility.getRow(index) * 8 - 1; i--) {
            moveMap.add(i);
            if (!pieces[i].isEmpty()) {
                break;
            }
        }
        // right
        for (int i = index + 1; i < (Utility.getRow(index) + 1) * 8; i++) {
            moveMap.add(i);
            if (!pieces[i].isEmpty()) {
                break;
            }
        }
        // up
        for (int i = index - 8; i > -1; i -= 8) {
            moveMap.add(i);
            if (!pieces[i].isEmpty()) {
                break;
            }
        }
        // down
        for (int i = index + 8; i < 64; i += 8) {
            moveMap.add(i);
            if (!pieces[i].isEmpty()) {
                break;
            }
        }

        return moveMap;
    }

    public static ArrayList<Integer> getDiagonalMoves(int index, Piece[] pieces) {
        ArrayList<Integer> moveMap = new ArrayList<Integer>();
        // bottom right
        for (int i = index + 9; i < 64; i += 9) {
            if (Utility.getCol(index) == 7) {
                break;
            }
            moveMap.add(i);
            if (Utility.getCol(i) == 7) {
                break;
            }
            if (!pieces[i].isEmpty()) {
                break;
            }
        }
        // top left
        for (int i = index - 9; i > -1; i -= 9) {
            if (Utility.getCol(index) == 0) {
                break;
            }
            moveMap.add(i);
            if (Utility.getCol(i) == 0) {
                break;
            }
            if (!pieces[i].isEmpty()) {
                break;
            }
        }
        // top right
        for (int i = index - 7; i > -1; i -= 7) {
            if (Utility.getCol(index) == 7) {
                break;
            }
            moveMap.add(i);
            if (Utility.getCol(i) == 7) {
                break;
            }
            if (!pieces[i].isEmpty()) {
                break;
            }
        }
        // bottom left
        for (int i = index + 7; i < 64; i += 7) {
            if (Utility.getCol(index) == 0) {
                break;
            }
            moveMap.add(i);
            if (Utility.getCol(i) == 0) {
                break;
            }
            if (!pieces[i].isEmpty()) {
                break;
            }
        }

        return moveMap;
    }

    public static ArrayList<Integer> makeFriendly(int index, Piece[] pieces, ArrayList<Integer> moveMap) {
        ArrayList<Integer> newMap = new ArrayList<Integer>();

        for (int i : moveMap) {
        	if (pieces[i].getColour() != pieces[index].getColour())
        		newMap.add(i);
        }

        return newMap;
    }

    public static ArrayList<Integer> createMoveMap(int index, Board board) {
        ArrayList<Integer> moveMap = new ArrayList<Integer>();

        Piece[] pieces = board.getPieces();
        Colour colour = pieces[index].getColour();

        moveMap = pieces[index].getMoves(index, board);
        moveMap = makeFriendly(index, pieces, moveMap);

        if (pieces[index] instanceof King) {
            moveMap = getCheckMoves(index, board, moveMap);
            moveMap = King.addCastleMoves(index, board, moveMap);
        } else if (pieces[index] instanceof Pawn) {
            moveMap = Pawn.addAttackMoves(index, board);
        }

        if (board.isCheck() || Detection.checkThreat(index, board, colour)) {
            moveMap = getCheckMoves(index, board, moveMap);
        }

        return moveMap;
    }
}
