package game;

import game.Moveset.Move;

public class King extends Piece {

	private boolean rightToCastle;

	public King(Board board, Side side) {
		super(board, side, '\u2654', '\u265A');
		rightToCastle = true;
		
	}

	@Override
	public Moveset generatePossibleMoves() {
		Moveset m = new Moveset();
		
		raycast(m, Direction.UP_LEFT, 1);
		raycast(m, Direction.UP_RIGHT, 1);
		raycast(m, Direction.DOWN_LEFT, 1);
		raycast(m, Direction.DOWN_RIGHT, 1);
		raycast(m, Direction.UP, 1);
		raycast(m, Direction.RIGHT, 1);
		raycast(m, Direction.LEFT, 1);
		raycast(m, Direction.DOWN, 1);

		if (rightToCastle && (board.getPiece(rank, file - 1) == null && board.getPiece(rank, file - 2) == null)
				&& board.getPiece(rank, file - 3) == null && board.getPiece(rank, file - 4) instanceof Rook) {
			Rook r = (Rook) board.getPiece(rank, file - 4);
			if (r.getRightToCastle() && r.isFriendly()) {
				m.set(rank, file - 4, Move.CASTLE);
			}
		}
		if (rightToCastle && (board.getPiece(rank, file + 1) == null && board.getPiece(rank, file + 2) == null)
				&& board.getPiece(rank, file + 3) instanceof Rook) {
			Rook r = (Rook) board.getPiece(rank, file + 3);
			if (r.getRightToCastle() && r.isFriendly()) {
				m.set(rank, file + 3, Move.CASTLE);
			}
		}

		return m;
	}

	public void disableRightToCastle() {
		rightToCastle = false;

	}

}
