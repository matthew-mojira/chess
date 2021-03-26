package game;

import static game.Side.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game.Moveset.Move;

/**
 * 
 * Chess board and pieces manager.
 * 
 * @author Matthew
 *
 */
public class Board implements Iterable<Piece>, Cloneable {

	public static final int BOARD_SIZE = 8;
	static final int A = 8, B = 7, C = 6, D = 5, E = 4, F = 3, G = 2, H = 1;

	private Piece[][] board;
	private Piece[][] boardBackup;
	private List<String> moveHistory;
	private Side sideToMove;
	private int moveCounter;

	/**
	 * Standard initializer. Will initialize the board with the regular formation of
	 * pieces on the board.
	 */
	public Board() {
		board = new Piece[BOARD_SIZE][BOARD_SIZE];

		sideToMove = WHITE;
		moveCounter = 2; // lol. starts at 2????

		moveHistory = new ArrayList<>();

		// Standard initialization of the board.
		initializePieces();
	}

	// TODO: MAKE A CONSTRUCTOR FOR A CUSTOM BOARD.

	/**
	 * Creates a deep copy of this board.
	 * <p>
	 * This deep copy is hopefully sufficient for chess engines to "test" moves.
	 * Basically you could use this method to copy the current board, then make a
	 * move on that, and then judge that board to ascertain whether that position is
	 * favorable or not, without having to touch the real board, before submitting a
	 * real move.
	 */
	@Override
	public Board clone() {
		Board clone = new Board();
		clone.board = new Piece[BOARD_SIZE][BOARD_SIZE]; // empty the board.

		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				try {
					clone.set((Piece) board[i][j].cloneWithBenefits(clone), cc(j), cc(i));
				} catch (NullPointerException e) {

				}
			}
		}
		clone.sideToMove = this.sideToMove;
		clone.moveCounter = this.moveCounter;
		clone.moveHistory = (List<String>) ((ArrayList) this.moveHistory).clone(); // WHAT ?!
		return clone;
	}

//	/**
//	 * Initializes the board but does not put pieces on it. You have to do that
//	 * yourself presumably.
//	 * 
//	 * @param b a flag that signifies that this constructor should be used. Other
//	 *          than that, this doesn't do anything.
//	 */
//	Board(boolean b) {
//		board = new Piece[BOARD_SIZE][BOARD_SIZE];
//
//		sideToMove = WHITE;
//		moveCounter = 2; // lol. starts at 2????
//	}

	/**
	 * Converts a standard chess coordinate to the zero-based indexing system used
	 * internally.
	 * <p>
	 * For example, the eighth rank is at the top of the board, so
	 * <code>cc(8)</code> will return 0 so it is placed in the array correctly.
	 * 
	 * @param x
	 * @return
	 */
	private static int cc(int x) {
		return -x + 8;
	}

	/**
	 * Places a piece on the board.
	 * <p>
	 * This method will try to overwrite an existing piece on the board. This method
	 * should only be used for debugging purposes or initializing the board before
	 * the start of the game. <b>This method should not be used during normal
	 * gameplay.</b>
	 * 
	 * @param p    the piece to place on the board
	 * @param file file in standard chess notation
	 * @param rank rank in standard chess notation
	 */
	private void set(Piece p, int file, int rank) {
		board[cc(rank)][cc(file)] = p;
	}

	/**
	 * Sets up the chessboard with standard pieces and locations.
	 */
	private void initializePieces() {
		set(new Pawn(this, WHITE), A, 2);
		set(new Pawn(this, WHITE), B, 2);
		set(new Pawn(this, WHITE), C, 2);
		set(new Pawn(this, WHITE), D, 2);
		set(new Pawn(this, WHITE), E, 2);
		set(new Pawn(this, WHITE), F, 2);
		set(new Pawn(this, WHITE), G, 2);
		set(new Pawn(this, WHITE), H, 2);
		set(new Pawn(this, BLACK), A, 7);
		set(new Pawn(this, BLACK), B, 7);
		set(new Pawn(this, BLACK), C, 7);
		set(new Pawn(this, BLACK), D, 7);
		set(new Pawn(this, BLACK), E, 7);
		set(new Pawn(this, BLACK), F, 7);
		set(new Pawn(this, BLACK), G, 7);
		set(new Pawn(this, BLACK), H, 7);

		set(new Rook(this, WHITE), A, 1);
		set(new Rook(this, WHITE), H, 1);
		set(new Rook(this, BLACK), A, 8);
		set(new Rook(this, BLACK), H, 8);

		set(new King(this, WHITE), E, 1);
		set(new King(this, BLACK), E, 8);

		set(new Knight(this, WHITE), B, 1);
		set(new Knight(this, WHITE), G, 1);
		set(new Knight(this, BLACK), B, 8);
		set(new Knight(this, BLACK), G, 8);

		set(new Bishop(this, WHITE), C, 1);
		set(new Bishop(this, WHITE), F, 1);
		set(new Bishop(this, BLACK), C, 8);
		set(new Bishop(this, BLACK), F, 8);

		set(new Queen(this, WHITE), D, 1);
		set(new Queen(this, BLACK), D, 8);

		updatePositions();
	}

	/**
	 * Gets whose turn to move it is.
	 * 
	 * @return
	 */
	public Side getSideToMove() {
		return sideToMove;
	}

	public int getMoveCount() {
		return moveCounter;
	}

	private void updatePositions() {
		for (Piece p : this) {
			if (p == null) {
				continue;
			}
			p.updatePosition();
		}
	}

	/**
	 * Directly returns the piece that is on the board at a certain position.
	 * 
	 * @param rank in internal notation
	 * @param file in internal notation
	 * @return the piece that is at the specified rank and file. returns
	 *         <code>null</code> if empty OR if the selected file and rank are out
	 *         of bounds
	 * 
	 * 
	 */
	public Piece getPiece(int rank, int file) {
		try {
			return board[rank][file];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Gets all the enemy pieces that may attack this tile. NOTE: This method is
	 * susceptible to bugs.
	 * 
	 * <p>
	 * Note: the enemy is defined by the set of pieces whose side is not currently
	 * the side who must move.
	 * <p>
	 * The list of pieces contains all pieces that can move onto the specified tile.
	 * The tile specified doesn't have to have a piece on it.
	 * 
	 * @param file
	 * @param rank
	 * @return an <ArrayList> of all enemy pieces who can move to the specified rank
	 *         and file. If there are no pieces that can move to that rank and file,
	 *         the <ArrayList> will be empty.
	 */
	public List<Piece> getPiecesAttacking(int rank, int file) {
		List<Piece> list = new ArrayList<>();
		for (Piece p : this) {
			// special pawn case... it attacks differently than it moves
			if (p != null && p.isEnemy() && p instanceof Pawn) {
				int forward = (p.getSide() == Side.WHITE) ? -1 : 1;
				if ((p.getRank() + forward == rank && p.getFile() - 1 == file)
						|| (p.getRank() + forward == rank && p.getFile() + 1 == file))
					list.add(p);
			} else if (p != null && p.isEnemy() && (p.generatePossibleMoves().get(rank, file).isLegal())) {
				list.add(p);
			}

		}
		return list;
//		throw new UnsupportedOperationException();
	}

	/**
	 * Gets all the friendly pieces that are covering this tile. NOTE: This method
	 * is susceptible to bugs.
	 * 
	 * <p>
	 * Note: friendly pieces are pieces whose side is currently the side who must
	 * move.
	 * <p>
	 * The list of pieces contains all the pieces that can move onto the specified
	 * tile. The tile specified doesn't have to have a piece on it.
	 * 
	 * @param file
	 * @param rank
	 * @return an <ArrayList> of all friendly pieces who can move to the specified
	 *         rank and file. If there are no pieces that can move to that rank and
	 *         file, the <ArrayList> will be empty.
	 * 
	 */
	public List<Piece> getPiecesDefending(int rank, int file) {
		List<Piece> list = new ArrayList<>();
		for (Piece p : this) {
			// special pawn case... it attacks differently than it moves
			if (p != null && p.isFriendly() && p instanceof Pawn) {
				int forward = (p.getSide() == Side.WHITE) ? -1 : 1;
				if ((p.getRank() + forward == rank && p.getFile() - 1 == file)
						|| (p.getRank() + forward == rank && p.getFile() + 1 == file))
					list.add(p);
			} else if (p != null && p.isFriendly() && (p.generatePossibleMoves().get(rank, file).isLegal()
					|| p.generatePossibleMoves().get(rank, file) == Move.PROTECT)) {
				list.add(p);
			}

		}
		return list;
//		throw new UnsupportedOperationException();
	}

	private void backupBoard() {
		boardBackup = new Piece[board.length][board.length];
		for (int i = 0; i < board.length; i++) {
			boardBackup[i] = board[i].clone();
		}
	}

	private void loadBackup() {
		board = boardBackup;
	}

	/**
	 * Determines if the side to move is in check.
	 * 
	 * @return
	 */
	public boolean isInCheck() {
		for (Piece p : this) {
			if (p instanceof King && p.isFriendly() && !p.getPiecesThreatening().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private static String coordinatesConverter(int rank, int file) {
		String c = getFileConverted(file);
		c += 8 - rank;
		return c;
	}

	private static String getFileConverted(int file) {
		return Character.toString((char) ('a' + file));
	}

	/**
	 * Checks the legality of a move and makes the move if it is legal.
	 * 
	 * <p>
	 * 
	 * After the legality of the move is checked, many things like setting
	 * en-passant status and castle status, promotions, and updating move counter
	 * are done.
	 * 
	 * @param originRank rank of the origin piece
	 * @param originFile file of the origin piece
	 * @param targetRank rank of the target piece
	 * @param targetFile file of the target piece
	 * @return true if the attempted move is legal, false otherwise
	 */
	/*
	 * TODO: This method is unideal for a good number of reasons, and needs serious
	 * refactoring. (1) The code that generates the string for the move (in
	 * algebraic notation) is buried inside this method in different parts of thie
	 * method. (2) The actual movement along the board is performed by the side
	 * effect of the moveHelper(). Presumably, this is not good design and may also
	 * be susceptible to unoptimized code. (3) For the promotion, using a GUI popup
	 * witn this method is not at all a good idea, as we can't induce a chess
	 * computer to decide what piece to promote (even if it is obvious).
	 * 
	 */
	public boolean move(int originRank, int originFile, int targetRank, int targetFile) {
		if (getPiece(originRank, originFile) == null) {
			return false;
		}

		Move thisMove = getPiece(originRank, originFile).generateValidMoves().get(targetRank, targetFile);

//		System.out.println(Arrays.deepToString(getPiece(originRank, originFile).generatePossibleMoves().getBoard()));
		/*
		 * Move notator block info.
		 */
		String moveString = "";

		Piece originPiece = getPiece(originRank, originFile);
		Piece targetPiece = getPiece(targetRank, targetFile);

		List<Piece> disambiguator = this.getPiecesDefending(targetRank, targetFile); // this is for the move notator.
//		System.out.println("The length of the disambiguator is " + disambiguator.size());
		Iterator<Piece> it = disambiguator.iterator();
		while (it.hasNext()) {
			Piece p = it.next();
			if (!p.getClass().getSimpleName().equals(originPiece.getClass().getSimpleName())) {
				it.remove();
			}
		}

		moveString += originPiece.getMoveChar();

//		System.out.println("The length of the disambiguator is " + disambiguator.size());

		if (!(originPiece instanceof Pawn)) {
			if (disambiguator.size() > 2) {
				moveString += coordinatesConverter(originRank, originFile);
			} else if (disambiguator.size() > 1) {
				if (disambiguator.get(0).getFile() != disambiguator.get(1).getFile()) {
					moveString += Character.toChars('a' + originPiece.getFile())[0];
				} else { // ranks differ
					moveString += 8 - originPiece.getRank();
				}

			}
		}
		///

		///

		switch (thisMove) {
		case CAPTURE:
		case CAPTURE_EN_PASSANT:
			if (originPiece instanceof Pawn) {
				moveString += getFileConverted(originFile);
			}
			moveString += "x" + coordinatesConverter(targetRank, targetFile);
			moveString += (thisMove == Move.CAPTURE_EN_PASSANT) ? " e.p." : "";
			break;

		case CASTLE:
			moveString = (targetFile == 0) ? "O-O-O" : "O-O";
			break;

		case MOVE:
		case DOUBLE_MOVE:
		case PROMOTE:
			moveString += coordinatesConverter(targetRank, targetFile);
			break;

		default:
			break;

		}
		///

		if (thisMove.isLegal() && this.moveHelper(originRank, originFile, targetRank, targetFile)) {
			// moveHelper() has the side effect of actually executing the move.
			if (thisMove == Move.DOUBLE_MOVE) {
				((Pawn) originPiece).makeVulnerableToEnPassant();
			} else if (originPiece instanceof King) {
				((King) (originPiece)).disableRightToCastle();
			} else if (originPiece instanceof Rook) {
				((Rook) (originPiece)).disableRightToCastle();
			}

			for (Piece c : this) {
				if (c instanceof Pawn) {
					Pawn p = ((Pawn) c);
					if (p.getSide() != sideToMove) {
						p.disableEnPassant();
					}
				}
			}

			if (thisMove == Move.PROMOTE || (originPiece instanceof Pawn) && (targetRank == 0 || targetRank == 7)) { // hackfix.
																														// capture
																														// +
																														// promote.
				JFrame frame = new JFrame();
				String[] options = { "Queen", "Rook", "Bishop", "Knight" };
				// TODO: THIS DIALOG SHOULD NOT BE IN THIS CLASS!!!
				// we have a solution. it's not good, but it's better.

//				System.out.println(Thread.currentThread().getStackTrace()[2].getClassName().equals("gui.GameBoardPanel$ChessTile$1"));	// lol!

				int dialog;
				if (Thread.currentThread().getStackTrace()[2].getClassName().equals("gui.GameBoardPanel$ChessTile$1")) {
					dialog = JOptionPane.showOptionDialog(frame, "Promote this pawn:", "Promotion",
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				} else {
					dialog = 0;
				}

				switch (dialog) {
				case 0:
					board[targetRank][targetFile] = new Queen(this, sideToMove);
					moveString += "Q";
					break;
				case 1:
					board[targetRank][targetFile] = new Rook(this, sideToMove);
					moveString += "R";
					break;
				case 2:
					board[targetRank][targetFile] = new Bishop(this, sideToMove);
					moveString += "B";
					break;
				case 3:
					board[targetRank][targetFile] = new Knight(this, sideToMove);
					moveString += "K";
					break;
				default:
					loadBackup();
					return false;
				}
			}

			switchSides();
			updatePositions();
			moveCounter++;

			// TODO: really weird place for this to be.
			switch (this.checkGameOutcome()) {
			case CHECK:
				moveString += "+";
				break;
			case CHECKMATE:
				moveString += "#";
				break;
			default:
				break;

			}
			moveHistory.add(moveString);

//			System.out.println(this.getMovesInAListDisplayableForOnlyMyGUINotVeryGeneralizedPrettyBadMethodIfAllTheMethodsWerentJustAsBad());

			return true;
		} else {

			loadBackup();
			return false;
		}

	}

	/**
	 * Attempts a move and determines its legality.
	 * 
	 * @param originRank rank of the origin piece
	 * @param originFile file of the origin piece
	 * @param targetRank rank of the target piece
	 * @param targetFile file of the target piece
	 * @return true if the attempted move is legal, false otherwise
	 */
	public boolean tryMove(int originRank, int originFile, int targetRank, int targetFile) {
		boolean status = this.moveHelper(originRank, originFile, targetRank, targetFile);

		loadBackup();
		return status;
	}
	// This method is kinda useless. We might as well find the piece at this tile,
	// and then generate its valid moves and review that board instead.

	private boolean moveHelper(int originRank, int originFile, int targetRank, int targetFile) {

		backupBoard();

		Piece originPiece = getPiece(originRank, originFile);
		Piece targetPiece = getPiece(targetRank, targetFile);

		if (originPiece == null || !originPiece.isFriendly()
				|| !originPiece.generatePossibleMoves().checkLegalMove(targetRank, targetFile)) {
			// you can only move pieces that are yours
			return false;
		}

		// We will try to make the move, then check if this puts us in check.
		Move thisMove = originPiece.generatePossibleMoves().get(targetRank, targetFile);
		switch (thisMove) {
		case CASTLE:
			board[targetRank][targetFile * 2 / 7 + 3] = targetPiece; // rook movement
			board[targetRank][targetFile] = null;
//			((Rook) (targetPiece)).disableRightToCastle();
			board[originRank][targetFile * 4 / 7 + 2] = originPiece; // king movement
			board[originRank][originFile] = null;
//			((King) (originPiece)).disableRightToCastle();
			break;

		case PROMOTE:

			board[originRank][originFile] = null;
			board[targetRank][targetFile] = originPiece;

			break;

		case CAPTURE_EN_PASSANT:
			board[targetRank][targetFile] = originPiece;
			board[originRank][originFile] = null;
			board[originRank][targetFile] = null; // captured piece "in passing"
			break;
		default:
			board[targetRank][targetFile] = originPiece;
			board[originRank][originFile] = null;
			break;
		}

//		switchSides();

		// The Part that matters.
		boolean check = this.isInCheck();

//		switchSides();
		return !check;

//		if (thisMove == Move.DOUBLE_MOVE) {
//			((Pawn)originPiece).makeVulnerableToEnPassant();
//		} else if (originPiece instanceof King) {
//			((King) (originPiece)).disableRightToCastle();
//		} else if (originPiece instanceof Rook) {
//			((Rook) (targetPiece)).disableRightToCastle();
//		}
//		
//		for (Piece c : this) {
//			if (c instanceof Pawn) {
//				Pawn p = ((Pawn) c);
//				if (p.getSide() == sideToMove) {
//					p.disableEnPassant();
//				}
//			}
//		}
//		
//		return true;

	}

	/**
	 * Switches the side of whose turn it is to move on the board without having to
	 * make a move.
	 * <p>
	 * Please do not use this on the actual board, as it will mess things up.
	 * Rather, you can use this method on a copy of the board so you can switch
	 * sides to get information about the state of the board.
	 */
	public void switchSides() {
		sideToMove = (sideToMove == Side.WHITE) ? Side.BLACK : Side.WHITE;
	}

	public List<Piece> getAllPieces(Side side) {
		List<Piece> all = new ArrayList<>();
		for (Piece p : this) {
			if (p != null && p.getSide() == side) {
				all.add(p);
			}
		}
		return all;
	}

	/**
	 * Checks if the side playing has legal moves to make.
	 * 
	 * @return
	 */
	public boolean checkForLegalMoves() {
		for (Piece p : this) {
			if (p != null && p.isFriendly()) {
				if (p.hasLegalMoves()) {
					return true;
				}
			}
		}
		return false;
	}

	// TODO: is this even a good idea??
	public enum GameState {
		NORMAL, CHECK, CHECKMATE, STALEMATE, DRAW;

		public boolean gameMustStop() {
			return (this == CHECKMATE || this == STALEMATE);
		}
	}

	/**
	 * Gets the game outcome.
	 * 
	 * <p>
	 * 
	 * 
	 * 
	 * @return NORMAL if the game is fine, CHECKMATE if there has been a checkmate,
	 *         CHECK if in check, STALEMATE if the side to move has no legal moves.
	 */
	public GameState checkGameOutcome() {
		if (checkForLegalMoves()) {
			return isInCheck() ? GameState.CHECK : GameState.NORMAL;
		} else {
			if (isInCheck()) {
				return GameState.CHECKMATE;
			} else {
				return GameState.STALEMATE;
			}
		}

	}

	@Override
	public String toString() {
		String toString = "";
		for (int i = 0; i < board.length; i++) {
			for (int x = 0; x < 2 * BOARD_SIZE + 1; x++) {
				toString += "-";
			}
			toString += "\n";
			toString += "|";
			for (int j = 0; j < BOARD_SIZE; j++) {

				if (board[i][j] == null) {
					toString += " ";
				} else {
					toString += board[i][j].toString();
				}
				toString += "|";
			}
			toString += "\n";
		}
		for (int x = 0; x < 2 * BOARD_SIZE + 1; x++) {
			toString += "-";
		}
		toString += "\n";
		toString += sideToMove + " to move.";
		return toString;
	}

	// TODO: this method is strange, and should be handled on the GUI end
	public String getMovesInAListDisplayableForOnlyMyGUINotVeryGeneralizedPrettyBadMethodIfAllTheMethodsWerentJustAsBad() {
		int i = 1;
		String finalString = "";
		Iterator<String> it = this.moveHistory.iterator();
		while (it.hasNext()) {
			finalString += " " + i + ". " + it.next(); // was :
			if (it.hasNext()) {
				finalString += " " + it.next() + " "; // was \t \n
			}
			i++;
		}
		return finalString;
	}

	/**
	 * Provides an iterator over all the pieces of the board.
	 * <p>
	 * For each call of <code>next()</code>, the method returns the next position in
	 * the chessboard. The order of the pieces returned by the iterator is starting
	 * from the top left, and then going left to right down each row. The iterator
	 * will return null references, that is, empty tiles on the board as well as
	 * tiles with actual pieces on them.
	 */
	@Override
	public Iterator<Piece> iterator() {
		List<Piece> a = new ArrayList<>();
		// oh dear
		for (Piece[] p1 : board) {
			for (Piece p : p1) {
				a.add(p);
			}
		}
		return a.iterator();

	}

}
