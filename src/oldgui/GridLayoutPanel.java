package oldgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import game.Piece;
import game.Side;
import game.Board.GameState;

public class GridLayoutPanel extends JPanel {

	static ArrayList<JButton> buttons;
	static Color color;
	int firstMoveX;
	int firstMoveY;
	static int fontSize = 40;
	static GridLayout grid = new GridLayout(8, 8);
	static final String FONT = Font.SANS_SERIF;

	public GridLayoutPanel() {

		setLayout(grid);

		buttons = new ArrayList<>();

		color = Color.getHSBColor((float) Math.random(), 0.25f, 1.0f);

		firstMoveX = -1;
		firstMoveY = -1;

		for (int i = 0; i < 64; i++) {
			JButton button = new JButton("?"); // (char)('A' + j) + (String.valueOf(8 - i)) +

			button.setFont(new Font("Ariel Unicode MS", Font.PLAIN, fontSize()));
			button.setMargin(new Insets(0, 0, 0, 0));
			button.setOpaque(true);

			buttons.add(button);

			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {

					int indexX = buttons.indexOf(button) / 8;
					int indexY = buttons.indexOf(button) % 8;

					System.out.println(indexX + " " + indexY);

					if (firstMoveX == -1 && firstMoveY == -1) {
						firstMoveX = indexX;
						firstMoveY = indexY;

						button.setBackground(Color.YELLOW);

						if (Chess.board.getPiece(firstMoveX, firstMoveY) != null) {
							Iterator<Integer> it = Chess.board.getPiece(firstMoveX, firstMoveY).generateValidMoves()
									.iterator();

							for (JButton b : buttons) {
								switch (it.next()) {
								case 0:
								case 1:
								case 3:
								case 7:
									b.setText("\u2022");
									break;
								case 2:
								case 5:
								case 8:
									b.setBackground(Color.PINK);
									break;
								default:
									break;
								}
							}
						}
					} else {
						if (!(Chess.board.move(firstMoveX, firstMoveY, indexX, indexY))) {
							System.out.println("Move invalid.");
							// System.out.println(indexX + " " + indexY);
							// System.out.println(firstMoveX + " " + firstMoveY);
							setBoardPieces();
						} else {
							// COMPUTER MOVE autoamtic

							if (Chess.autoAI && (Chess.board.checkGameOutcome() == GameState.NORMAL || Chess.board.checkGameOutcome() == GameState.CHECK)) {
								Chess.chessEngine.makeAMove();
							}
							
							int whiteValue = 0;
							int blackValue = 0;
							for (Piece p : Chess.board) {
								if (p == null)
									continue;
								if (p.getSide() == Side.WHITE) {
									whiteValue += p.getValue();
								} else {
									blackValue += p.getValue();
								}
							}
							
							
							
							int differential = whiteValue - blackValue;
							System.out.println(differential);
							if (differential > 0) {
								Chess.topRightText.setText("WHITE: +" + differential);
							} else if (differential < 0) {
								Chess.topRightText.setText("BLACK: +" + -differential);
							} else {
								Chess.topRightText.setText("WHITE/BLACK: +0");
							}
							
							
							
							
							GridLayoutPanel.resetBackgroundColors();
							GridLayoutPanel.setBoardPieces();

							Chess.moveHistory.setText(Chess.board
									.getMovesInAListDisplayableForOnlyMyGUINotVeryGeneralizedPrettyBadMethodIfAllTheMethodsWerentJustAsBad());

						}

						firstMoveX = -1;
						firstMoveY = -1;

//						System.out.println(Chess.board);

						resetBackgroundColors();
						setBoardPieces();

					}

					// System.out.println("What button is this?" + buttons.indexOf(button) / 8 +
					// buttons.indexOf(button) % 8);

//					setBoardPieces();
				}
			});

			add(button);

		}

		resetBackgroundColors();
		setBoardPieces();

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				fontSize = Math.min(((Component) e.getSource()).getHeight(), ((Component) e.getSource()).getWidth())
						/ 10 + 2;
				System.out.println(fontSize);
				setBoardPieces();
			}
		});
	}

	/**
	 * Sets all tiles to the background color checkerboard pattern.
	 */
	static void resetBackgroundColors() {
		int i = 0;

		for (JButton b : buttons) {
			i += (i % 9 == 0) ? 2 : 1;
			if (i % 2 == 0) {
				b.setBackground(Color.WHITE);
			} else {
				b.setBackground(color);
			}
		}
	}

	/**
	 * Refreshes all the board pieces so each tile displays what piece is on it.
	 */
	static void setBoardPieces() {
		Iterator<Piece> it = Chess.board.iterator();
		boolean check = Chess.board.isInCheck();
		for (JButton b : buttons) {
			b.setFont(new Font("Ariel Unicode MS", Font.PLAIN, fontSize()));
			String sett;
			try {
				Piece next = it.next();
				sett = next.toString();
				// highlight the king in check
				if (check && next instanceof game.King && next.getSide() == Chess.board.sideToMove) {
					b.setBackground(new Color(0xFF, 0x30, 0x30));
				}
			} catch (NullPointerException e) {
				sett = "";
			}
			b.setText(sett);
		}

		// set title

		Chess.frame
				.setTitle("Chess: Move " + Chess.board.moveCounter / 2 + " / " + Chess.board.sideToMove + " to move");

	}

	public static int fontSize() {
		return fontSize;
	}
}
