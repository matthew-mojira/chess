package game;

public class Queen extends Piece {

	public Queen(Board board, Side side) {
		super(board, side, '\u2655', '\u265B');
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
		
		return m;
	}

}
