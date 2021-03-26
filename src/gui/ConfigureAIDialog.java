package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import engines.ChessEngine;
import game.Side;
import oldgui.Chess;

public class ConfigureAIDialog extends JDialog {

	static String whiteClass;
	static String blackClass;

	public ConfigureAIDialog() {

		JTextField className = new JTextField(10);
		className.setVisible(true);

		JRadioButton player = new JRadioButton("Player");
		player.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				className.setVisible(false);
			}
		});

		JRadioButton computer = new JRadioButton("Computer");
		computer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				className.setVisible(true);
				validate();
				repaint();
			}
		});

		// duplicated
		JTextField classNameBlack = new JTextField(10);
		classNameBlack.setVisible(true);

		JRadioButton playerBlack = new JRadioButton("Player");
		playerBlack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				classNameBlack.setVisible(false);
			}
		});

		JRadioButton computerBlack = new JRadioButton("Computer");
		computerBlack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				classNameBlack.setVisible(true);
				validate();
				repaint();
			}
		});

		// group buttons together so they're mutually exclusive
		ButtonGroup group = new ButtonGroup();
		group.add(player);
		group.add(computer);

		ButtonGroup groupBlack = new ButtonGroup();
		groupBlack.add(playerBlack);
		groupBlack.add(computerBlack);

		JButton confirm = new JButton("Confirm (will restart game!)");
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				whiteClass = (computer.isSelected()) ? className.getText() : null;
				blackClass = (computerBlack.isSelected()) ? classNameBlack.getText() : null;
				LowerButtonPanel.resetBoard();
			}

		});

		// set default values
		if (Driver.chessEngineWhite == null) {
			player.setSelected(true); // default option
		} else {
			computer.setSelected(true);
			className.setText(whiteClass);
		}
		if (Driver.chessEngineBlack == null) {
			playerBlack.setSelected(true); // default option
		} else {
			computerBlack.setSelected(true);
			classNameBlack.setText(blackClass);
		}

		// add to board
		add(new JLabel("WHITE:"));
		add(player);
		add(computer);
		add(className);
		add(new JLabel("BLACK:"));
		add(playerBlack);
		add(computerBlack);
		add(classNameBlack);
		add(confirm);

		setLayout(new FlowLayout());

		pack();

		setSize(380, 160);
		this.setTitle("Configure Players");

		this.setLocationRelativeTo(getParent());
		((JPanel) (this.getContentPane())).setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setResizable(false);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}

	static engines.ChessEngine fawzi(String engineName, Side side) {
		engineName = "engines." + engineName;
		// hardcoded. please do not do this.
		Constructor[] constructors = null; // will hold all of the class constructors (for me there was only 1)

		try {
			constructors = Class.forName(engineName).getConstructors();
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"The name of the class was not recognized as a chess engine. Try again.", "Engine not found",
					JOptionPane.WARNING_MESSAGE);
			new ConfigureAIDialog();
		}

		Object[] arguments = new Object[2]; // my constructor wanted 4 arguments

		// Now write code that will fill the args array with arguments that are needed
		// by the constructor

		Array.set(arguments, 0, Driver.chessBoard);
		Array.set(arguments, 1, side);

		try {

			// The following line creates an instance of the class
			return (ChessEngine) constructors[0].newInstance(arguments); // 0 for me because there was only 1
																			// constructor
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
	}
}
