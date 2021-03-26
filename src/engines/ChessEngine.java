package engines;

import game.Board;
import game.Side;

/**
 * Abstract class for chess engines to create.
 * 
 * @author Matthew
 *
 */
public abstract class ChessEngine {

	protected Board board;
	protected Side side;

	public ChessEngine(Board board, Side side) {
		this.board = board;
		this.side = side;
	}

	/**
	 * Makes a move on the board.
	 * <p>
	 * This method will be called on by the GUI when it's the computer's turn to
	 * move, and so this should do all the processing to determine what move to
	 * make. This method should also make a call to board.move() to make the move,
	 * and return as soon as possible.
	 * <p>
	 * <b>You could in theory make as many moves as you want in this method. Please
	 * pay careful attention so that the computer makes only one move.</b>
	 */
	public abstract void makeAMove();

//	public abstract void considerADraw();	// but there is no way to offer a draw?

}
