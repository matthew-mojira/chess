package engines;

import java.util.List;
import java.util.Random;

import game.Board;
import game.Board.GameState;
import game.Moveset;
import game.Pawn;
import game.Piece;
import game.Side;

public class SimpleEngine extends ChessEngine {

	static final int CHECKMATE_ATTEMPTS = 5000;
	static final int MATE_IN_TWO_ATTEMPTS = 1000;
	static final int CAPTURE_ATTEMPTS = 5000;
	static final int UNHANGING_ATTEMPTS = 5000;
	static final int ATTACKS_ATTEMPTS = 5000;
	static final int MOVE_ATTEMPTS = 5000;

	public SimpleEngine(Board board, Side side) {
		super(board, side);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void makeAMove() {
		long startingTime = System.nanoTime();
		Random r = new Random();

		// try to see if we have checkmate open (stupid and dumb)
		for (int i = 0; i < CHECKMATE_ATTEMPTS; i++) {
			int a = r.nextInt(8), b = r.nextInt(8), c = r.nextInt(8), d = r.nextInt(8);

			Piece fromPiece = board.getPiece(a, b);

			if (fromPiece == null || !fromPiece.isFriendly()) {
				continue;
			}

			Board attempt = board.clone();

			if (attempt.move(a, b, c, d) && attempt.checkGameOutcome() == GameState.CHECKMATE) {
				board.move(a, b, c, d);
				System.out.println("CHECKMATE!");
				System.out.println((System.nanoTime() - startingTime) / 1e9);
				return;
			}
		}
//
//		int a1,b1,c1,d1;
//		MateInTwo:
//		for (int i = 0; i < MATE_IN_TWO_ATTEMPTS; i++) {
//			Board copy = board.clone();
//			int a = r.nextInt(8), b = r.nextInt(8), c = r.nextInt(8), d = r.nextInt(8);
//			a1 = a;
//			b1 = b;
//			c1 = c;
//			d1 = d;
//			
//			Piece fromPiece = board.getPiece(a, b);
//
//			if (fromPiece == null || !fromPiece.isFriendly()) {
//				continue;
//			}
//
//			if (copy.move(a, b, c, d)) {
//				for (int j = 0; j < MATE_IN_TWO_ATTEMPTS; j++) {
//					a = r.nextInt(8);
//					b = r.nextInt(8);
//					c = r.nextInt(8);
//					d = r.nextInt(8);
//					fromPiece = copy.getPiece(a, b);
//					if (fromPiece == null || !fromPiece.isFriendly()) {
//						continue;
//					} else if (copy.move(a, b, c, d)) {
//						for (int k = 0; k < MATE_IN_TWO_ATTEMPTS; k++) {
//							a = r.nextInt(8);
//							b = r.nextInt(8);
//							c = r.nextInt(8);
//							d = r.nextInt(8);
//							fromPiece = copy.getPiece(a, b);
//
//							if (fromPiece == null || !fromPiece.isFriendly()) {
//								continue;
//							} else if (copy.move(a, b, c, d) && copy.checkGameOutcome() != GameState.CHECKMATE) {
////								System.out.println(i + " " + j + " " + k);
//								continue MateInTwo;
//							}
//
//						}
//					}
//				}
//
//			}
//			
//			board.move(a,b,c,d);
//			System.out.println("mate in two");
//			return;
//
//		}
		
		

		// try to capture a piece
		for (int i = 0; i < CAPTURE_ATTEMPTS; i++) {

			int a = r.nextInt(8), b = r.nextInt(8), c = r.nextInt(8), d = r.nextInt(8);

			Piece fromPiece = board.getPiece(a, b);
			Piece targetPiece = board.getPiece(c, d);
			if (fromPiece == null || !fromPiece.isFriendly()) {
				continue;
			}

			if (fromPiece.generateValidMoves().get(c, d) == Moveset.Move.CAPTURE) {
				if (targetPiece.getPiecesCovering().isEmpty()) {
					board.move(a, b, c, d);
					System.out.println("We attack this piece because it's undefended.");
					System.out.println((System.nanoTime() - startingTime) / 1e9);
					return;
				} else if (getValue(targetPiece) >= getValue(fromPiece)) {
					System.out.println(
							"We attack this piece because a trade here would be equal or beneficial for us. The point differential of this is "
									+ (getValue(targetPiece) - getValue(fromPiece)));
					board.move(a, b, c, d);
					System.out.println((System.nanoTime() - startingTime) / 1e9);
					return;
				}
			}
		}

		// defend oneself
		for (int i = 0; i < UNHANGING_ATTEMPTS; i++) {

			int a = r.nextInt(8), b = r.nextInt(8), c = r.nextInt(8), d = r.nextInt(8);

			Piece fromPiece = board.getPiece(a, b);
			Piece targetPiece = board.getPiece(c, d);
			if (fromPiece == null || !fromPiece.isFriendly()) {
				continue;
			}

			if (!(fromPiece instanceof game.Pawn) && !fromPiece.getPiecesThreatening().isEmpty()
					&& fromPiece.generateValidMoves().get(c, d).isLegal() && !coveredWell(fromPiece)) { // removed &&
																										// fromPiece.getPiecesCovering().isEmpty()

				if (targetPiece == null) {

					if (board.getPiecesAttacking(c, d).isEmpty()) {
						System.out.println("We move here because it's open/");
						board.move(a, b, c, d);
						System.out.println((System.nanoTime() - startingTime) / 1e9);
						return;
					}
				} else if (targetPiece.getPiecesCovering().isEmpty()) {
					System.out.println("We move to attack this piece because it's undefended.");
					board.move(a, b, c, d);
					System.out.println((System.nanoTime() - startingTime) / 1e9);
					return;
				} else if (getValue(targetPiece) >= getValue(fromPiece)) {
					System.out.println(
							"We move to attack this piece because a trade here would be equal or beneficial for us.");
					board.move(a, b, c, d);
					System.out.println((System.nanoTime() - startingTime) / 1e9);
					return;
				}
			}
		}

		for (int i = 0; i < ATTACKS_ATTEMPTS; i++) {

			int a = r.nextInt(8), b = r.nextInt(8), c = r.nextInt(8), d = r.nextInt(8);

			Piece fromPiece = board.getPiece(a, b);
			// targetPiece is presumably null
//
			if (fromPiece == null || !fromPiece.isFriendly()) {
				continue;
			}

			if (fromPiece.generateValidMoves().get(c, d).isStrictlyMovement()) {
				Board simulatedMove = board.clone();

				simulatedMove.move(a, b, c, d);
				simulatedMove.switchSides();
				if (!simulatedMove.getPiece(c, d).getThreatenedPieces().isEmpty() // we are threatening a piece
						&& simulatedMove.getPiecesAttacking(c, d).isEmpty()) { // nothing is threatening the tile we are
																				// entering

					List<Piece> threatenedPieces = simulatedMove.getPiece(c, d).getThreatenedPieces();
					for (Piece p : threatenedPieces) {
						if (p.getPiecesCovering().isEmpty()) {
							System.out.println("Move to attack...?");
							board.move(a, b, c, d);
							System.out.println((System.nanoTime() - startingTime) / 1e9);
							return;
						}
					}

				}
			}
		}

		// FAVOR MOVING PAWNS
		for (int i = 0; i < MOVE_ATTEMPTS / 20; i++) {

			int a = r.nextInt(8), b = r.nextInt(8), c = r.nextInt(8), d = r.nextInt(8);

			Piece fromPiece = board.getPiece(a, b);
			// targetPiece is presumably null
//
			if (fromPiece == null || !fromPiece.isFriendly() || !(fromPiece instanceof Pawn)) {
				continue;
			}

			if (fromPiece.generateValidMoves().get(c, d).isStrictlyMovement()) {
				if (board.getPiecesAttacking(c, d).isEmpty()) {
					System.out.println("PAWN SPECIAL!!!");
					board.move(a, b, c, d);
					System.out.println((System.nanoTime() - startingTime) / 1e9);
					return;
				}
			}
		}

		for (int i = 0; i < MOVE_ATTEMPTS; i++) {

			int a = r.nextInt(8), b = r.nextInt(8), c = r.nextInt(8), d = r.nextInt(8);

			Piece fromPiece = board.getPiece(a, b);
			// targetPiece is presumably null
//
			if (fromPiece == null || !fromPiece.isFriendly()) {
				continue;
			}

			if (fromPiece.generateValidMoves().get(c, d).isStrictlyMovement()) {
				if (board.getPiecesAttacking(c, d).isEmpty()) {
					System.out.println("We do this move because it.s legal.");
					board.move(a, b, c, d);
					System.out.println((System.nanoTime() - startingTime) / 1e9);
					return;
				}
			}
		}

		while (!board.move(r.nextInt(8), r.nextInt(8), r.nextInt(8), r.nextInt(8))) {

		}

		System.out.println("WEIRD!!!!");

	}

	private boolean coveredWell(Piece fromPiece) {
		List<Piece> threateningPiece = fromPiece.getPiecesThreatening();
		List<Piece> coveringPiece = fromPiece.getPiecesCovering();

		int minThreatValue = 1001;
		for (Piece p : threateningPiece) {
			if (getValue(p) < minThreatValue) {
				minThreatValue = getValue(p);
			}
		}
		int minCoverValue = 1001;
		for (Piece p : coveringPiece) {
			if (getValue(p) < minCoverValue) {
				minCoverValue = getValue(p);
			}
		}

		return minThreatValue > minCoverValue;
	}

	private static int getValue(Piece p) {
		switch (p.getClass().getSimpleName()) {
		case "Bishop":
			return 4;
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
