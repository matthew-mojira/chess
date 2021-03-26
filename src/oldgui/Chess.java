package oldgui;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import engines.*;
import game.*;

public class Chess {

	public static Board board;
	static ChessEngine chessEngine;
	static ChessEngine chessEngineWhite;
	static ChessEngine chessEngineBlack;
	static JFrame frame;
	static JTextArea moveHistory;
	static boolean autoAI;
	static JLabel topRightText;

	public static void createAndShowGUI() {
		frame = new JFrame("Chess");
		frame.setContentPane(new MainPanel());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setSize(550, 450);
		// frame.setResizable(false);
		frame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		board = new Board();
		System.out.println(board);
		
		// Fawzi code

		String className = new String("engines." + (JOptionPane.showInputDialog(null, "White engine", null))); // this is the name of the class you are trying to instantiate.
		// hardcoded. please do not do this.
		Constructor[] constructors = null; // will hold all of the class constructors (for me there was only 1)

		try {
			constructors = Class.forName(className).getConstructors();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("No class named " + className);
		}

		Object[] arguments = new Object[2]; // my constructor wanted 4 arguments

		// Now write code that will fill the args array with arguments that are needed
		// by the constructor
		
		Array.set(arguments, 0, Chess.board);
		Array.set(arguments, 1, Side.WHITE);

		ChessEngine newGuy;

		try {

			// The following line creates an instance of the class
			newGuy = (ChessEngine) constructors[0].newInstance(arguments); // 0 for me because there was only 1 constructor
																	// available

		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		// end fawzi code
		


//		chessEngine = new SimpleEngine();
		Chess.chessEngineWhite = newGuy;
		Chess.chessEngineBlack = new SimpleEngine(Chess.board, Side.BLACK);



		autoAI = false;

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
