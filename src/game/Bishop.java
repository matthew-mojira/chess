package game;

public class Bishop extends Piece {

	public Bishop(Board board, Side side) {
		super(board, side, '\u2657', '\u265D');
	}

	@Override
	public Moveset generatePossibleMoves() {
		Moveset m = new Moveset();
		
		raycast(m, Direction.UP_LEFT);
		raycast(m, Direction.UP_RIGHT);
		raycast(m, Direction.DOWN_LEFT);
		raycast(m, Direction.DOWN_RIGHT);
		
		return m;
	}

}
