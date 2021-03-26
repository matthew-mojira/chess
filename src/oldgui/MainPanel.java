package oldgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import static javax.swing.ScrollPaneConstants.*;

import engines.ChessEngine;
import engines.RandomEngine;
import game.*;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MainPanel() {

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setLayout(new BorderLayout());

		add(new GridLayoutPanel(), BorderLayout.CENTER);

		add(new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				setLayout(new GridLayout(1, 1));
				JButton reset = new JButton("Reset");
				reset.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Chess.board = new Board();
						GridLayoutPanel.setBoardPieces();
						GridLayoutPanel.color = Color.getHSBColor((float) Math.random(), 0.25f, 1.0f);
						GridLayoutPanel.resetBackgroundColors();

					}
				});

				JButton computer = new JButton("Computer turn");
				computer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						
						Timer x = new Timer();
						x.start();
						
////						Chess.chessEngine.makeAMove();
//						
//						if (Chess.board.sideToMove == Side.WHITE) {
//							Chess.chessEngineWhite.makeAMove();
//						} else {
//							Chess.chessEngineBlack.makeAMove();
//						}
//						GridLayoutPanel.resetBackgroundColors();
//						GridLayoutPanel.setBoardPieces();
//						
//						
//						// inherited from other class
//						int whiteValue = 0;
//						int blackValue = 0;
//						for (Piece p : Chess.board) {
//							if (p == null)
//								continue;
//							if (p.getSide() == Side.WHITE) {
//								whiteValue += p.getValue();
//							} else {
//								blackValue += p.getValue();
//							}
//						}
//						
//						
//						
//						int differential = whiteValue - blackValue;
//						System.out.println(differential);
//						if (differential > 0) {
//							Chess.topRightText.setText("WHITE: +" + differential);
//						} else if (differential < 0) {
//							Chess.topRightText.setText("BLACK: +" + -differential);
//						} else {
//							Chess.topRightText.setText("WHITE/BLACK: +0");
//						}
//						
//						
//						Chess.moveHistory.setText(Chess.board
//								.getMovesInAListDisplayableForOnlyMyGUINotVeryGeneralizedPrettyBadMethodIfAllTheMethodsWerentJustAsBad());
						
						
						
					}
				});

				JButton simulation = new JButton("Simulate");
				simulation.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						while (true) {
							Chess.chessEngine.makeAMove();
							GridLayoutPanel.resetBackgroundColors();
							GridLayoutPanel.setBoardPieces();
							try {
								Thread.sleep(600);
								Chess.frame.repaint();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});
				
				JButton autoAI = new JButton("Toggle AI opponent");
				autoAI.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Chess.autoAI = !Chess.autoAI;
						if (Chess.autoAI) {
							autoAI.setBackground(Color.YELLOW);
						} else {
							autoAI.setBackground(null);
						}
					}
				});

				add(reset);
				add(computer);
				add(autoAI);
//				add(simulation);

			}
		}, BorderLayout.SOUTH);
		
		Chess.moveHistory = new JTextArea();
		Chess.moveHistory.setEditable(false);
		Chess.moveHistory.setColumns(14);
		
		JScrollPane scrollPane = new JScrollPane( Chess.moveHistory  );
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER); // hackfix
		
		Chess.topRightText = new JLabel();
		rightPanel.add(Chess.topRightText, BorderLayout.NORTH);
		
		
		
		
		add(rightPanel, BorderLayout.EAST);
		
		

		
	}

}
