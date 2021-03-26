package gui;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.awt.BorderLayout;

import javax.swing.*;

import game.Piece;
import game.Side;

public class RightInformationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	static JTextArea moveHistory;
	static JLabel differentialDisplay;
	static ChessClock whiteClock;
	static ChessClock blackClock;

	public RightInformationPanel() {
		moveHistory = new JTextArea();
		differentialDisplay = new JLabel();

		JScrollPane scrollPane = new JScrollPane(moveHistory);
		moveHistory.setColumns(13);

		setLayout(new BorderLayout());
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER); // hackfix

		differentialDisplay = new JLabel();
		differentialDisplay.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

		whiteClock = new ChessClock(600, 10);
		add(whiteClock, BorderLayout.SOUTH);
		blackClock = new ChessClock(600, 10);
		add(blackClock, BorderLayout.NORTH);

		JPanel insideCenterPanel = new JPanel();
		insideCenterPanel.setLayout(new BorderLayout());
		insideCenterPanel.add(differentialDisplay, BorderLayout.NORTH);
		insideCenterPanel.add(scrollPane, BorderLayout.CENTER);
		insideCenterPanel.setVisible(true);

		add(insideCenterPanel, BorderLayout.CENTER);

		setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

		setVisible(true);
	}

	public static void updateRightPanel() {
		int whiteValue = 0;
		int blackValue = 0;
		for (Piece p : Driver.chessBoard) {
			if (p == null)
				continue;
			if (p.getSide() == Side.WHITE) {
				whiteValue += p.getValue();
			} else {
				blackValue += p.getValue();
			}
		}

		int differential = whiteValue - blackValue;
		if (differential > 0) {
			differentialDisplay.setText("WHITE: +" + differential);
		} else if (differential < 0) {
			differentialDisplay.setText("BLACK: +" + -differential);
		} else {
			differentialDisplay.setText("WHITE/BLACK: +0");
		}

		moveHistory.setText(Driver.chessBoard
				.getMovesInAListDisplayableForOnlyMyGUINotVeryGeneralizedPrettyBadMethodIfAllTheMethodsWerentJustAsBad());

	}

}
