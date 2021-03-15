package pieces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import game.Board;
import game.Moveset;
import game.Moveset.Move;

/**
 * Abstract classes for the pieces on the board.
 * <p>
 * The classes that extend this class generally inherit most of the final
 * methods written here, with only just a few methods having to be handled by
 * the subclasses.
 * 
 * @author Matthew
 *
 */
public abstract class Piece {

	/*
	 * We rely on the Unicode set of characters for chess pieces. As such, it is
	 * hard to make new chess pieces because we need a Unicode set of characters
	 * that exist nicely in both white and black variants. The ones that I have
	 * found thus far to work well are:
	 * 
	 * https://www.unicode.org/charts/PDF/U2600.pdf
	 * https://www.unicode.org/charts/PDF/U1FA00.pdf
	 * 
	 */
	protected final String whiteChar;
	protected final String blackChar;

	protected final Side side;
	protected final Board board;

	/*
	 * NOTE!!! The rank and file variables do not actually affect the position of
	 * the piece. These are simply here to save you from having to call
	 * <code>getRank()</code> or <code>getFile()</code> over and over again. The
	 * method <code>updatePosition()</code> will update these values based on the
	 * board this piece is on, which is the actual manager of piece positions.
	 * Therefore, you shouldn't edit these, because it won't do anything and you'll
	 * end up having a desychronized piece.
	 */
	protected int rank;
	protected int file;

	public Piece(Board board, Side side, char whiteChar, char blackChar) {
		this.board = board;
		this.side = side;
		this.whiteChar = String.valueOf(whiteChar);
		this.blackChar = String.valueOf(blackChar);
	}

	/*
	 * Annoyingly, Java's primitive char does not support the extended unicode set
	 * of characters, which extend beyond a 16 bit representation. Here, you can
	 * just supply the "code point" that is the hexadecimal index of the character
	 * and it should work fine in this constructor.
	 */
	public Piece(Board board, Side side, int whiteCharCodePoint, int blackCharCodePoint) {
		this.board = board;
		this.side = side;
		this.whiteChar = new String(Character.toChars(whiteCharCodePoint));
		this.blackChar = new String(Character.toChars(blackCharCodePoint));
	}

	public final int getRank() {
		for (int i = 0; i < Board.BOARD_SIZE; i++) {
			for (int j = 0; j < Board.BOARD_SIZE; j++) {
				if (board.getPiece(i, j) == this) {
					return i;
				}
			}
		}
		throw new NoSuchElementException();
	}

	public final int getFile() {
		for (int i = 0; i < Board.BOARD_SIZE; i++) {
			for (int j = 0; j < Board.BOARD_SIZE; j++) {
				if (board.getPiece(i, j) == this) {
					return j;
				}
			}
		}
		throw new NoSuchElementException();
	}

	protected final int ccI(int x) {
		return 8 - x;
	}

	public final Side getSide() {
		return this.side;
	}

	/**
	 * Finds if this piece is the opponent piece, based on whose turn to move it is.
	 * 
	 * @return
	 */
	public final boolean isEnemy() {
		return this.getSide() != board.getSideToMove();
	}

	/**
	 * Finds if this piece is a friendly piece, based on whose turn to move it is.
	 * 
	 * @return
	 */
	public final boolean isFriendly() {
		return this.getSide() == board.getSideToMove();
	}

	/**
	 * Finds if a piece is an enemy with this piece.
	 * 
	 * @param p piece to be checked against
	 * @return
	 * @throws NullPointerException if p is null
	 */
	public final boolean isEnemiesWith(Piece p) {
		return this.getSide() != p.getSide();
	}

	/**
	 * Checks if this piece has any legal moves available.
	 * 
	 * @return
	 */
	public final boolean hasLegalMoves() {
		Moveset m = this.generateValidMoves();
		for (int i = 0; i < Board.BOARD_SIZE; i++) {
			for (int j = 0; j < Board.BOARD_SIZE; j++) {
				if (m.checkLegalMove(i, j)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Finds all pieces if any that this piece is threatening with capture.
	 * 
	 * @return
	 */
	public List<Piece> getThreatenedPieces() {
		List<Piece> targets = new ArrayList<>();
		Move[][] m = this.generatePossibleMoves().getBoard();
		for (int i = 0; i < Board.BOARD_SIZE; i++) {
			for (int j = 0; j < Board.BOARD_SIZE; j++) {

				if (m[i][j] == Move.CAPTURE) {
					targets.add(board.getPiece(i, j));
				}

			}
		}
		return targets;
	}

	/**
	 * Finds all pieces if any that this piece is covering.
	 * 
	 * @return
	 */
	public List<Piece> getCoveredPieces() {
		List<Piece> targets = new ArrayList<>();
		Move[][] m = this.generatePossibleMoves().getBoard();
		for (int i = 0; i < Board.BOARD_SIZE; i++) {
			for (int j = 0; j < Board.BOARD_SIZE; j++) {

				if (m[i][j] == Move.PROTECT) {
					targets.add(board.getPiece(i, j));
				}

			}
		}
		return targets;
	}

	/**
	 * Finds all pieces if any that are threatening this piece with capture.
	 * 
	 * @return
	 */
	public List<Piece> getPiecesThreatening() {
		List<Piece> threats = new ArrayList<>();
		for (Piece p : board) {
			if (p != null && p.getThreatenedPieces().contains(this)) {
				threats.add(p);
			}
		}
		return threats;
	}

	/**
	 * Finds all pieces if any that are covering this piece.
	 * 
	 * @return
	 */
	public List<Piece> getPiecesCovering() {
		List<Piece> threats = new ArrayList<>();
		for (Piece p : board) {
			if (p != null && p.getCoveredPieces().contains(this)) {
				threats.add(p);
			}
		}
		return threats;
	}

	@Override
	public final String toString() {
		return (getSide() == Side.WHITE ? whiteChar : blackChar);
	}

	/**
	 * Determines all the legal moves that this chess piece may take except CHECK.
	 * <p>
	 * NOTE: this method must NOT include logic to determine whether or not a move
	 * will put a player in check, either by the move putting the opponent's king in
	 * check or the move putting the current player into check themself. There is
	 * another method that governs this movement and will handle it for you.
	 * 
	 * @return an array that represents the chess board, with each square signifying
	 *         the move (if allowed) that a piece may make onto that square
	 */
	public abstract Moveset generatePossibleMoves();

	/**
	 * Determines every legal move for this piece.
	 * <p>
	 * In essence, this function does the operations in
	 * <code>generatePossibleMoves()</code> but then for every supposedly valid
	 * position returned by that method, this method checks if that move would put
	 * the player in check, which would make it illegal. It also handles rules like
	 * the inability to castle out of check.
	 * 
	 * @return
	 */
	public final Moveset generateValidMoves() {
		Moveset m = generatePossibleMoves();

		for (int i = 0; i < Board.BOARD_SIZE; i++) {
			for (int j = 0; j < Board.BOARD_SIZE; j++) {

				if (m.get(i, j).isLegal() && !board.tryMove(rank, file, i, j)) {

					m.set(i, j, Move.ILLEGAL);
				}

			}

		}

		for (int i = 0; i < Board.BOARD_SIZE; i++) {
			for (int j = 0; j < Board.BOARD_SIZE; j++) {

				// Stupid hackfix. cannot castle out of check,
				if (m.get(i, j) == Move.CASTLE && board.isInCheck()) {
					m.set(i, j, Move.ILLEGAL);
				} else if (m.get(i, j) == Move.CASTLE && m.get(i, 2 * j / 7 + 3) == Move.ILLEGAL) {
					m.set(i, j, Move.ILLEGAL);
				}

			}

		}

//		System.out.println(Arrays.deepToString(m.getBoard()));

		return m;

	}

	/**
	 * Updates the position of the piece on the board.
	 * <p>
	 * NOTE: this should not be used by a programmer for a chess engine. It's not
	 * for you.
	 */
	public final void updatePosition() {
		this.rank = this.getRank();
		this.file = this.getFile();
	}

	protected enum Direction {
		UP_LEFT(-1, -1), UP_RIGHT(-1, 1), DOWN_LEFT(1, -1), DOWN_RIGHT(1, 1), UP(-1, 0), DOWN(1, 0), LEFT(0, -1),
		RIGHT(0, 1);

		int rankIncrement, fileIncrement;

		private Direction(int rankIncrement, int fileIncrement) {
			this.rankIncrement = rankIncrement;
			this.fileIncrement = fileIncrement;
		}
	}

	protected final Moveset raycast(Moveset input, Direction d) {
		return raycast(input, d, Board.BOARD_SIZE);
	}

	protected final Moveset raycast(Moveset input, Direction d, int magnitude) {
		if (input == null) {
			input = new Moveset();
		}

		int rank2 = rank, file2 = file;
		int rankIncrement = d.rankIncrement;
		int fileIncrement = d.fileIncrement;

		for (int i = 0; i < magnitude; i++) {
			rank2 += rankIncrement;
			file2 += fileIncrement;

			if (board.getPiece(rank2, file2) == null) {
				input.set(rank2, file2, Move.MOVE);
			} else if (this.isEnemiesWith(board.getPiece(rank2, file2))) {
				input.set(rank2, file2, Move.CAPTURE);
				break;
			} else { // if it's not empty or enemy it's a teammate
				input.set(rank2, file2, Move.PROTECT);
				break;
			}
		}

		return input;
		// NOTE: this edits the argument Moveset, so you really don't have to do
		// anything with the result.
	}

	// TODO: apparently some methods use "isEnemiesWith" and "isEnemy". I think we
	// should prefer isEnemiesWith because that is independent of whose turn it is?
	// I guess...
	protected final void lawyer(int offsetRank, int offsetFile, Moveset m) {
		if (board.getPiece(rank + offsetRank, file + offsetFile) == null) {
			m.set(rank + offsetRank, file + offsetFile, Move.MOVE);
			return;
		}

		if (isEnemiesWith(board.getPiece(rank + offsetRank, file + offsetFile))) {
			m.set(rank + offsetRank, file + offsetFile, Move.CAPTURE);
		} else {
			m.set(rank + offsetRank, file + offsetFile, Move.PROTECT);
		}
	}

}
