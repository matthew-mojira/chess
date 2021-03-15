package engines;

import java.util.Random;

import game.Chess;
import game.Moveset;
import pieces.Piece;

public class SimpleEngine implements Chessable {

	static final int CAPTURE_ATTEMPTS = 20000;
	static final int UNHANGING_ATTEMPTS = 10000;
	static final int MOVE_ATTEMPTS = 30000;

	public SimpleEngine() {

	}

	@Override
	public void makeAMove() {

		int counter = 0;
		Random r = new Random();


		// try to capture a piece
		for (int i = 0; i < CAPTURE_ATTEMPTS; i++) {

			counter++;
			int a = r.nextInt(8), b = r.nextInt(8), c = r.nextInt(8), d = r.nextInt(8);

			Piece fromPiece = Chess.board.getPiece(a, b);
			Piece targetPiece = Chess.board.getPiece(c, d);
			if (fromPiece == null) {
				continue;
			}

			if (fromPiece.generateValidMoves().get(c, d) == Moveset.Move.CAPTURE) {
				if (targetPiece.getPiecesCovering().isEmpty()) {
					Chess.board.move(a, b, c, d);
					System.out.println(counter);
					return;
				} else if (getValue(targetPiece) >= getValue(fromPiece)) {
					Chess.board.move(a, b, c, d);
					System.out.println(counter);
					return;
				}
			}
		}
		
		// defend oneself
		for (int i = 0; i < UNHANGING_ATTEMPTS; i++) {

			counter++;
			int a = r.nextInt(8), b = r.nextInt(8), c = r.nextInt(8), d = r.nextInt(8);

			Piece fromPiece = Chess.board.getPiece(a, b);
			Piece targetPiece = Chess.board.getPiece(c, d);
			if (fromPiece == null) {
				continue;
			}

			if (!(fromPiece instanceof pieces.Pawn) && !fromPiece.getPiecesThreatening().isEmpty() && fromPiece.generateValidMoves().get(c, d).isLegal()) {
				if (targetPiece == null) {
					Chess.board.move(a, b, c, d);
					System.out.println(counter);
					return;
				} else if (targetPiece.getPiecesCovering().isEmpty()) {
					Chess.board.move(a, b, c, d);
					System.out.println(counter);
					return;
				} else if (getValue(targetPiece) >= getValue(fromPiece)) {
					Chess.board.move(a, b, c, d);
					System.out.println(counter);
					return;
				}
			}
		}


		for (int i = 0; i < MOVE_ATTEMPTS; i++) {

			counter++;
			int a = r.nextInt(8), b = r.nextInt(8), c = r.nextInt(8), d = r.nextInt(8);

			Piece fromPiece = Chess.board.getPiece(a, b);
			// targetPiece is presumably null
//
			if (fromPiece == null) {
				continue;
			}

			if (fromPiece.generateValidMoves().get(c, d) == Moveset.Move.MOVE) {
				if (Chess.board.getPiecesAttacking(c, d).isEmpty()) {
					Chess.board.move(a, b, c, d);
					System.out.println(counter);
					return;
				}
			}
		}

		while (!Chess.board.move(r.nextInt(8), r.nextInt(8), r.nextInt(8), r.nextInt(8))) {
			counter++;
		}

		System.out.println(counter);

	}

	private static int getValue(Piece p) {
		switch (p.getClass().getCanonicalName()) {
		case "Bishop":
			return 3;
		case "King":
			return 1000;
		case "Knight":
			return 3;
		case "Pawn":
			return 1;
		case "Queen":
			return 9;
		case "Rook":
			return 5;
		default:
			return 0;

		}
	}

}
