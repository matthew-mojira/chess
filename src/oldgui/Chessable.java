package oldgui;

/**
 * An interface which all chess engines must implement.
 * 
 * <p>
 * 
 * Warning: it is really possible to edit the game data and "cheat" because the
 * getters are poorly written and maintained. Please do not do this.
 * 
 * <p>
 * 
 * To get data from the board, I suggest you options like this:
 * 
 * You can collect all the entries using an
 * <code> Chess.getBoard().iterator() </code>, which will return everything on
 * the table sequentially from left to right, top to bottom. If a tile is empty,
 * you get a null reference.
 * 
 * You can get a certain piece at a piece you can use
 * <code> Chess.getBoard().getPiece(rank, file) </code>.
 * 
 * Once you have a piece, you can use the methods <code> getSide() </code>, and
 * <code> getClass() </code> or <code>instanceof</code> to get the color and
 * piece information.
 * 
 * In the Piece class, you may also find it useful to use
 * <code> isEnemiesWith(Chess.getBoard(), targetRank, targetFile) </code> to get
 * information about whether a piece has an opposite color to the current piece.
 * There is also <code> attackingKing(Chess.getBoard(), rank, file) </code> and
 * <code> generateLegalMoves(Chess.getBoard(), rank, file) </code>.
 * 
 * Warning 2: Rank and file are all integers and 0 based, so they actually do
 * not work out anything like real notation for chess.
 * 
 * @author Matthew
 *
 */
public interface Chessable {

	/**
	 * Pings the chess engine to make a move.
	 * <p>
	 * Classes that implement this interface must be careful to actually make sure a
	 * call to this method makes a call to the move method (this should be done with
	 * <code> Chess.getBoard().move(fromRank, fromFile, toRank, toFile) </code>),
	 * and also that the move that this method generates is valid so that the call
	 * of a computer move actually does something.
	 */
	public void makeAMove();

	// TODO: perhaps we could program some "simulator methods" to test valid moves?
	// we have honestly given the programmer very little useful things for them to
	// make an engine out of :(

}
