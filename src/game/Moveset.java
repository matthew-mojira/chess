package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import pieces.Piece;
import pieces.Side;

/**
 * A collection displaying possible moves for a chess piece on the chess board.
 * <p>
 * This class wraps around an array of Moves which is the same size as the
 * chess board. An instance of this class should be created when a piece is
 * trying to move. Every entry in the array governs for that moment what kind of
 * move can be made with that piece.
 * 
 * @author Matthew
 *
 */
public class Moveset implements Iterable<Integer> {

	Move[][] board;

	/**
	 * Types of board moves that a piece can make.
	 * 
	 * @author Matthew
	 *
	 */
	public enum Move {
		MOVE(true), DOUBLE_MOVE(true), CAPTURE(true), CHECK(true), PROTECT(false), CASTLE(true), ILLEGAL(false), PROMOTE(true), CAPTURE_EN_PASSANT(true);
		
		/**
		 * Whether or not the type of move can be made or if the movement is a marker for something else.
		 */
		private boolean legality;
		
		private Move(boolean legality) {
			this.legality = legality;
		}
		
		public boolean isLegal() {
			return this.legality;
		}
		
		
		@Override
		public String toString() {
			return legality == true ? "Y" : "N";
		}
	}

	/**
	 * Initializes an array of movements where all are Illegal.
	 */
	public Moveset() {
		board = new Move[Board.BOARD_SIZE][Board.BOARD_SIZE];
		for (int i = 0; i < Board.BOARD_SIZE; i++) {
			for (int j = 0; j < Board.BOARD_SIZE; j++) {
				board[i][j] = Move.ILLEGAL;
			}
		}
	}

	/**
	 * Sets a kind of movement on the board.
	 * 
	 * @param rank
	 * @param file
	 * @param m
	 */
	public void set(int rank, int file, Move m) {
		try {
			board[rank][file] = m;
		} catch (ArrayIndexOutOfBoundsException e) {
			
		}
	}

	public boolean checkLegalMove(int rank, int file) {
		try {
			return board[rank][file].isLegal();
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	public Move get(int rank, int file) {
		try {
			return board[rank][file];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public Move[][] getBoard() {
		return board;
	}
	
	public Iterator<Integer> iterator() {

		List<Integer> a = new ArrayList<>();
		// oh dear
		for (Move[] p1 : board) {
			for (Move p : p1) {
				a.add(p.ordinal());
			}
		}

		return a.iterator();

	}

}
