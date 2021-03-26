package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import engines.ChessEngine;
import game.Board;

public class Driver {

	static Board chessBoard;
	static ChessEngine chessEngineWhite; // if this is null, the player is playing.
	static ChessEngine chessEngineBlack;
	static JFrame mainFrame;
	static JPanel rightInformationPanel = new RightInformationPanel(); 

	public static void main(String[] args) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}

	public static void createAndShowGUI() {
		chessBoard = new Board();

		mainFrame = new JFrame("Chess");

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		mainFrame.setLayout(new BorderLayout());

		mainFrame.add(new GameBoardPanel(), BorderLayout.CENTER);
		mainFrame.add(rightInformationPanel, BorderLayout.EAST);
		mainFrame.add(new LowerButtonPanel(), BorderLayout.SOUTH);

		mainFrame.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setSize(580, 490);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}

}
