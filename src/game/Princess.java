package game;

public class Princess extends Piece {

	public Princess(Board board, Side side) {
//		super(board, side, 0x1FA50, 0x1FA53);
		super(board, side, 0x2119, 0x1D5E3);
	}

	@Override
	public Moveset generatePossibleMoves() {
		Moveset m = new Moveset();

		lawyer(-2, -1, m);
		lawyer(-2, +1, m);
		lawyer(+2, -1, m);
		lawyer(+2, +1, m);
		lawyer(-1, -2, m);
		lawyer(-1, +2, m);
		lawyer(+1, -2, m);
		lawyer(+1, +2, m);
		
		raycast(m, Direction.UP_LEFT);
		raycast(m, Direction.UP_RIGHT);
		raycast(m, Direction.DOWN_LEFT);
		raycast(m, Direction.DOWN_RIGHT);
		
		return m;
		
	}

}
