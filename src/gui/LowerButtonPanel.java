package gui;

import java.awt.Dialog.ModalityType;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import game.Board;
import game.Board.GameState;
import game.Side;

public class LowerButtonPanel extends JPanel {
	
	static Timer myTimer = new Timer(1000, null);	// dummy timer that does nothing.

	public LowerButtonPanel() {
		setLayout(new GridLayout(1, 1));

		JButton reset = new JButton("Reset");
//		System.out.print(reset.getBackground().getClass().getCanonicalName());
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetBoard();
			}

		});

		JButton configureAI = new JButton("Configure Players");
		configureAI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ConfigureAIDialog();
			}

		});

		JButton configureClock = new JButton("Configure Clock");

		configureClock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ConfigureClockDialog();
			}

		});

		JButton flipBoard = new JButton("Flip Board");
		flipBoard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameBoardPanel.flipped = !GameBoardPanel.flipped;
				GameBoardPanel.resetBackgroundColors();
				GameBoardPanel.setBoardPieces();

				GameBoardPanel.firstMoveX = -1; // reset selected piece
				GameBoardPanel.firstMoveY = -1;

				// flip the clocks
				if (GameBoardPanel.flipped) { // board has now been flipped
					Driver.rightInformationPanel.add(RightInformationPanel.whiteClock, BorderLayout.NORTH);
					Driver.rightInformationPanel.add(RightInformationPanel.blackClock, BorderLayout.SOUTH);
				} else { // board unflipped
					Driver.rightInformationPanel.add(RightInformationPanel.whiteClock, BorderLayout.SOUTH);
					Driver.rightInformationPanel.add(RightInformationPanel.blackClock, BorderLayout.NORTH);
				}

			}
		});

		add(reset);
		add(flipBoard);
		add(configureClock);
		add(configureAI);

		setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

	}

	public static void resetBoard() {
		Driver.chessBoard = new Board();
		myTimer.stop();
		
		if (ConfigureAIDialog.whiteClass != null)
			Driver.chessEngineWhite = ConfigureAIDialog.fawzi(ConfigureAIDialog.whiteClass, game.Side.WHITE);
		if (ConfigureAIDialog.blackClass != null)
			Driver.chessEngineBlack = ConfigureAIDialog.fawzi(ConfigureAIDialog.blackClass, game.Side.BLACK);

		if (Driver.chessEngineWhite != null) {
			if (Driver.chessEngineBlack != null) {
				myTimer = new Timer(0, new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						if (Driver.chessBoard.getSideToMove() == Side.WHITE) {
							Driver.chessEngineWhite.makeAMove();
						} else {
							Driver.chessEngineBlack.makeAMove();
						}

						GameBoardPanel.resetBackgroundColors();
						GameBoardPanel.setBoardPieces();
						RightInformationPanel.updateRightPanel();

						if (Driver.chessBoard.checkGameOutcome().gameMustStop()) {
							// copied.
							switch (Driver.chessBoard.checkGameOutcome()) {
							case CHECKMATE:
								JOptionPane.showMessageDialog(new JFrame(),
										Driver.chessBoard.getSideToMove() + " has been checkmated!", "Checkmate!",
										JOptionPane.PLAIN_MESSAGE);
								break;
							case STALEMATE:
								JOptionPane.showMessageDialog(new JFrame(), "Stalemate. Neither side wins.",
										"Stalemate!", JOptionPane.PLAIN_MESSAGE);
							default:
							}

							((Timer) e.getSource()).stop();

						}

//						((Timer) e.getSource()).setDelay(Math.max(1, 400 - Driver.chessBoard.getMoveCount()));	// TODO: DUPLICATE to above!
					}
				});
				myTimer.start();
			} else {
				Driver.chessEngineWhite.makeAMove();
			}

		}

		// clocks.
		RightInformationPanel.whiteClock.stopClock();
		RightInformationPanel.blackClock.stopClock();

		RightInformationPanel.whiteClock = new ChessClock(ConfigureClockDialog.whiteBegin,
				ConfigureClockDialog.whiteIncrement);
		RightInformationPanel.blackClock = new ChessClock(ConfigureClockDialog.blackBegin,
				ConfigureClockDialog.blackIncrement);

		if (GameBoardPanel.flipped) { // board has now been flipped
			Driver.rightInformationPanel.add(RightInformationPanel.whiteClock, BorderLayout.NORTH);
			Driver.rightInformationPanel.add(RightInformationPanel.blackClock, BorderLayout.SOUTH);
		} else { // board unflipped
			Driver.rightInformationPanel.add(RightInformationPanel.whiteClock, BorderLayout.SOUTH);
			Driver.rightInformationPanel.add(RightInformationPanel.blackClock, BorderLayout.NORTH);
		}
		//

		GameBoardPanel.resetBackgroundColors();
		GameBoardPanel.setBoardPieces();
		RightInformationPanel.updateRightPanel();
	}
}
