package pieces;

import game.Board;
import game.Moveset;

public class Empress extends Piece {



	public Empress(Board board, Side side) {
//		super(board, side, 0x1FA4F, 0x1FA52);
		super(board, side, 0x1D53C, 0x1D5D8);
	}

	@Override
	public Moveset generatePossibleMoves() {
		Moveset m = new Moveset();
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
