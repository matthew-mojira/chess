package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import game.Piece;

public class GameBoardPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	static Color backgroundColor;
	static int firstMoveX, firstMoveY;
	static ArrayList<JButton> buttons;
	static boolean flipped;

	public GameBoardPanel() {
		buttons = new ArrayList<>(64);

		setLayout(new GridLayout(8, 8));
		for (int i = 0; i < 64; i++) {
			ChessTile toAdd = new ChessTile();
			buttons.add(toAdd);
			add(toAdd);
		}
		setVisible(true);
		firstMoveX = -1;
		firstMoveY = -1;

		if (Driver.chessEngineWhite != null) { // I'm pretty sure this can't happen.
			Driver.chessEngineWhite.makeAMove();
		}

		resetBackgroundColors();
		setBoardPieces();
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	}

	static void resetBackgroundColors() {
		int i = 0;

		for (JButton b : buttons) {
			i += (i % 9 == 0) ? 2 : 1;
			if (i % 2 == 0) {
				b.setBackground(Color.WHITE);
			} else {
				b.setBackground(backgroundColor);

			}
			b.setForeground(Color.BLACK);
		}

	}

	static void setBoardPieces() {
		// TODO: this is unideal, as the board iterator implementation already converts
		// the board to an ArrayList to return its iterator. thus, this middle man of
		// the for each loop should be avoided.
		ArrayList<Piece> notAGoodIdea = new ArrayList<>();
		for (Piece p : Driver.chessBoard) {
			notAGoodIdea.add(p);
		}

		int i = (flipped) ? 63 : 0; // first or last
		boolean check = Driver.chessBoard.isInCheck();
		for (JButton b : buttons) {
			String sett;
			try {
				Piece next;
				if (flipped) {
					next = notAGoodIdea.get(i--);
				} else {
					next = notAGoodIdea.get(i++);
				}

				sett = next.toString();
				// highlight the king in check
				if (check && next instanceof game.King && next.getSide() == Driver.chessBoard.getSideToMove()) {
					b.setBackground(new Color(0xFF, 0x30, 0x30));
				}
			} catch (NullPointerException e) {
				sett = "";
			}
			b.setText(sett);
		}

		// set title
		Driver.mainFrame.setTitle("Chess: Move " + Driver.chessBoard.getMoveCount() / 2 + " / "
				+ Driver.chessBoard.getSideToMove() + " to move");

	}

	private static class ChessTile extends JButton {

		private static final long serialVersionUID = 1L;

		private static final Font buttonFont = new Font("Arial Unicode MS", Font.PLAIN, 40);

		private ChessTile() {
			setFont(buttonFont);
			setMargin(new Insets(0, 0, 0, 0));
			setOpaque(true);
			addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					if (Driver.chessBoard.checkGameOutcome().gameMustStop()
							|| RightInformationPanel.whiteClock.isFlagged()
							|| RightInformationPanel.blackClock.isFlagged()) {
						return;
					}

					JButton button = (JButton) e.getSource();
					int indexX = flipped ? 7 - buttons.indexOf(button) / 8 : buttons.indexOf(button) / 8;
					int indexY = flipped ? 7 - buttons.indexOf(button) % 8 : buttons.indexOf(button) % 8;

					// FIRST TILE SELECTED
					if (firstMoveX == -1 && firstMoveY == -1) {
						firstMoveX = indexX;
						firstMoveY = indexY;
						button.setBackground(Color.YELLOW);

						// FIRST PIECE SELECTED
						if (Driver.chessBoard.getPiece(firstMoveX, firstMoveY) != null) {
							Iterator<Integer> it = Driver.chessBoard.getPiece(firstMoveX, firstMoveY)
									.generateValidMoves().iterator();

							for (int i = flipped ? 63 : 0; valid(i); i += flipped ? -1 : 1) {
								switch (it.next()) {
								case 0:
								case 1:
								case 3:
								case 7:
									buttons.get(i).setForeground(new Color(0x50, 0x50, 0xF0));
									buttons.get(i).setText("\u2022");
									break;
								case 2:
								case 5:
								case 8:
									buttons.get(i).setBackground(Color.PINK);
									break;
								default:
									break;
								}
							}
						}

					} else { // SECOND TILE SELECTED
						if ((Driver.chessBoard.move(firstMoveX, firstMoveY, indexX, indexY))) {
							switch (Driver.chessBoard.checkGameOutcome()) {// TODO: DUPLICATE to above!
							case CHECKMATE:
								RightInformationPanel.whiteClock.stopClock();
								RightInformationPanel.blackClock.stopClock();
								resetBackgroundColors();
								setBoardPieces();
								RightInformationPanel.updateRightPanel();
								JOptionPane.showMessageDialog(new JFrame(),
										Driver.chessBoard.getSideToMove() + " has been checkmated!", "Checkmate!",
										JOptionPane.PLAIN_MESSAGE);
								return;
							case STALEMATE:
								RightInformationPanel.whiteClock.stopClock();
								RightInformationPanel.blackClock.stopClock();
								resetBackgroundColors();
								setBoardPieces();
								RightInformationPanel.updateRightPanel();
								JOptionPane.showMessageDialog(new JFrame(), "Stalemate. Neither side wins.",
										"Stalemate!", JOptionPane.PLAIN_MESSAGE);
								return; // redundant (not anymore lol)
							default:
							}

							switch (Driver.chessBoard.getSideToMove()) { // lol why?
							case WHITE:

								if (Driver.chessEngineWhite != null) {
									Driver.chessEngineWhite.makeAMove();
								} else {
									RightInformationPanel.blackClock.stopClock();
									RightInformationPanel.whiteClock.startClock(); // really lol why?
								}
								break;
							case BLACK:

								if (Driver.chessEngineBlack != null) {
									Driver.chessEngineBlack.makeAMove();
								} else {
									RightInformationPanel.whiteClock.stopClock();
									RightInformationPanel.blackClock.startClock();
								}
								break;
							}

						} else {
							// INVALID MOVE
						}

						firstMoveX = -1;
						firstMoveY = -1;
						resetBackgroundColors();
						setBoardPieces();
						RightInformationPanel.updateRightPanel();
					}

				}

				private boolean valid(int i) {
					if (flipped) {
						return i > 0;
					} else {
						return i < 64;
					}
				}

			});
		}
	}

}
