package pieces;

import game.Board;
import static game.Moveset.Move.*;
import game.Moveset;

public class Pawn extends Piece {

	private boolean vulnerableToEnPassant;

	public Pawn(Board board, Side side) {
		super(board, side, '\u2659', '\u265F');
		vulnerableToEnPassant = false;
	}

	@Override
	public Moveset generatePossibleMoves() {
		Moveset moveset = new Moveset();

		int forward = (getSide() == Side.WHITE) ? -1 : 1;

		// Is the space ahead empty? If so, we can move there.
		if (board.getPiece(rank + forward, file) == null) {
			moveset.set(rank + forward, file, MOVE);
			
			
			if (rank + forward == 0 || rank + forward == 7) {
				moveset.set(rank + forward, file, PROMOTE);
			}

			// Are we on the starting rank and is the space two ahead empty? If so, we can
			// move there. Note: since we are inside the other if-block, we've already
			// checked that it's okay to move one space ahead first.
			if ((rank == 1 || rank == 6) && board.getPiece(rank + (2 * forward), file) == null) {
				moveset.set(rank + (2 * forward), file, DOUBLE_MOVE);
			}
		}

		// Can we attack the piece ahead of us?
		for (int i = -1; i < 2; i += 2) {
			Piece pieceToCheck = board.getPiece(rank + forward, file + i);
			if (pieceToCheck == null) {

			} else if (pieceToCheck.isEnemiesWith(this)) {
				moveset.set(rank + forward, file + i, CAPTURE);
			} else {
				moveset.set(rank + forward, file + i, PROTECT);
			}

			// Can we en-passant capture the pawn ?
			// Assumption: the tile behind a piece that moves forward two must be empty,
			// since the pawn must have gone through there in the last turn to get where it
			// is.
			pieceToCheck = board.getPiece(rank, file + i);
			if (pieceToCheck instanceof Pawn && pieceToCheck.isEnemiesWith(this)
					&& ((Pawn) pieceToCheck).vulnerableToEnPassant()) {
				moveset.set(rank + forward, file + i, CAPTURE_EN_PASSANT);
			}
		}

		return moveset;

	}

	public boolean vulnerableToEnPassant() {
		return vulnerableToEnPassant;
	}
	
	public void makeVulnerableToEnPassant() {
		vulnerableToEnPassant = true;
	}
	
	public void disableEnPassant() {
		vulnerableToEnPassant = false;
	}
	
	

}
