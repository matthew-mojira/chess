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

public class ConfigureClockDialog extends JDialog {

	static int whiteBegin = 600;
	static int whiteIncrement = 10;
	static int blackBegin = 600;
	static int blackIncrement = 10;

	public ConfigureClockDialog() {

		JTextField whiteBeginField = new JTextField(10);
		JTextField whiteIncrementField = new JTextField(10);
		JTextField blackBeginField = new JTextField(10);
		JTextField blackIncrementField = new JTextField(10);
		whiteBeginField.setVisible(true);
		whiteIncrementField.setVisible(true);
		blackBeginField.setVisible(true);
		blackIncrementField.setVisible(true);

		JButton confirm = new JButton("Confirm");
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setVisible(false);
					whiteBegin = Integer.valueOf(whiteBeginField.getText());
					whiteIncrement = Integer.valueOf(whiteIncrementField.getText());
					blackBegin = Integer.valueOf(blackBeginField.getText());
					blackIncrement = Integer.valueOf(blackIncrementField.getText());
					if (whiteBegin <= 0 || whiteIncrement < 0 || blackBegin <= 0 || blackIncrement < 0) {
						throw new IllegalArgumentException(); // go down
					}
//					JOptionPane.showMessageDialog(null,
//							"After your turn finishes, the time you may gain from the increment may not be accurate. \n"
//							+ "This is actually because of a bug in Java. Each 100 milliseconds, the clock should be\n"
//							+ " updated to remove your time by one-tenths of a second. However, Java apparently does\n"
//							+ " delays horribly wrong because it gets updated far slower than this. The proper clock\n"
//							+ " is stored internally and when your turn ends, the clock is updated to this and then \n"
//							+ "the increment is added. So, it should be accurate??? It's bugged, whatever Java sucks.",
//							"BUG!!!!!!", JOptionPane.WARNING_MESSAGE);
				} catch (RuntimeException ef) {
					JOptionPane.showMessageDialog(null, "Time supplied was invalid. Please try again.", "Time invalid",
							JOptionPane.WARNING_MESSAGE);
					new ConfigureClockDialog();
				}

				LowerButtonPanel.resetBoard();
			}

		});

		// set default values
		whiteBeginField.setText(Integer.toString(whiteBegin));
		whiteIncrementField.setText(Integer.toString(whiteIncrement));

		blackBeginField.setText(Integer.toString(blackBegin));
		blackIncrementField.setText(Integer.toString(blackIncrement));

		// add to board
		add(new JLabel());
		add(new JLabel("Time (seconds)"));
		add(new JLabel("Increment (seconds)"));
		add(new JLabel("WHITE:"));
		add(whiteBeginField);
		add(whiteIncrementField);
		add(new JLabel("BLACK:"));
		add(blackBeginField);
		add(blackIncrementField);

		add(new JLabel());
		add(confirm);
		add(new JLabel("(will reset game!)"));

		setLayout(new GridLayout(4, 3));

		pack();

		setSize(380, 160);
		this.setTitle("Configure Chess Clock");

		this.setLocationRelativeTo(getParent());
		((JPanel) (this.getContentPane())).setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setResizable(false);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}
}
