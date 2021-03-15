package pieces;

import game.Board;
import game.Moveset;

public class Knight extends Piece {

	public Knight(Board board, Side side) {
		super(board, side, '\u2658', '\u265E');
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


		return m;
	}

}
