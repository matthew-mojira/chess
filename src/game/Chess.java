package game;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import engines.*;
import pieces.*;

public class Chess {

	public static Board board;
	static Chessable chessEngine;
	static JFrame frame;

	public static void createAndShowGUI() {
		frame = new JFrame("Chess");
		frame.setContentPane(new MainPanel());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setSize(430, 450);
		//		frame.setResizable(false);
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		chessEngine = new SimpleEngine();

		board = new Board();
		System.out.println(board);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}



}

