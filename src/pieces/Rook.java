package pieces;

import game.Board;
import game.Moveset;

public class Rook extends Piece {
	
	private boolean rightToCastle;

	public Rook(Board board, Side side) {
		super(board, side, '\u2656', '\u265C');
		rightToCastle = true;
	}

	@Override
	public Moveset generatePossibleMoves() {
		Moveset m = new Moveset();
		raycast(m, Direction.UP);
		raycast(m, Direction.RIGHT);
		raycast(m, Direction.LEFT);
		raycast(m, Direction.DOWN);
		return m;
	}

	public void disableRightToCastle() {
		rightToCastle = false;
		
	}

	public boolean getRightToCastle() {
		return rightToCastle;
	}

}
