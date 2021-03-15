package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

import pieces.*;

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
						Chess.chessEngine.makeAMove();
						GridLayoutPanel.resetBackgroundColors();
						GridLayoutPanel.setBoardPieces();
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

				add(reset);
				add(computer);
//				add(simulation);

			}
		}, BorderLayout.SOUTH);

	}

}
