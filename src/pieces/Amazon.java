package pieces;

import game.Board;
import game.Moveset;

public class Amazon extends Piece {


	public Amazon(Board board, Side side) {
//		super(board, side, 0x1FA4E, 0x1FA51);
		super(board, side, 0x1D538, 0x1D5E3);
	}

	@Override
	public Moveset generatePossibleMoves() {
		Moveset m = new Moveset();
		
		raycast(m, Direction.UP_LEFT);
		raycast(m, Direction.UP_RIGHT);
		raycast(m, Direction.DOWN_LEFT);
		raycast(m, Direction.DOWN_RIGHT);
		
		raycast(m, Direction.UP);
		raycast(m, Direction.RIGHT);
		raycast(m, Direction.LEFT);
		raycast(m, Direction.DOWN);

		lawyer(-2, -1, m);
		lawyer(-2, +1, m);
		lawyer(+2, -1, m);
		lawyer(+2, +1, m);
		lawyer(-1, -2, m);
		lawyer(-1, +2, m);
		lawyer(+1, -2, m);
		lawyer(+1, +2, m);

		return m;
	}

}
