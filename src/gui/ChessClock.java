package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

public class ChessClock extends JLabel {

	private static final int LOW_FRACTION = 5;

	javax.swing.Timer timer;
	int minutes, minutesBackup;
	int seconds, secondsBackup;
	int fractions, fractionsBackup;
	int increment;
	int initialTime;
	boolean americanDelay;
	boolean flagged;
	long nanoClock; // The Timer class doesn't seem to be totally accurate. System.nanoTime() should
					// be more reliable if used.

	public ChessClock(int initialTime, int increment) { // TODO: american delaY?
		this.minutes = initialTime / 60;
		this.seconds = initialTime % 60;
		this.initialTime = initialTime;
		this.increment = increment;
		this.americanDelay = americanDelay;

		timer = new Clock();

		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

		this.setFont(new Font("Arial Unicode MS", Font.BOLD, 40));
		this.setForeground(Color.DARK_GRAY);
		this.setText(this.toString());
		this.setHorizontalAlignment(SwingConstants.RIGHT);
		this.setOpaque(true); // redundant
		this.setVisible(true);
	}

	@Override
	public String toString() {
		String secondsString;
		if (seconds < 10) {
			secondsString = "0" + seconds;
		} else {
			secondsString = Integer.toString(seconds);
		}
		return minutes + ":" + secondsString + "." + fractions;
	}

	private void decrementClock() {
		if (--fractions < 0) {
			fractions = 9;
			if (--seconds < 0) {
				seconds = 59;
				if (--minutes < 0) {
					fractions = 0;
					flag();
				}
			}
		}
		checkLowOnTime();
		this.setText(this.toString());
	}

	public void startClock() {
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		nanoClock = System.nanoTime();
		minutesBackup = minutes;
		secondsBackup = seconds;
		fractionsBackup = fractions;

		timer.start();
		this.setForeground(Color.BLACK);
		this.setText(this.toString());
	}

	public void stopClock() {
//		if (flagged) {
//			System.out.println("here");
//			return;
//		}
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
//		System.out.println(elapsedFractions);
		timer.stop();

		if (nanoClock != 0) {
			
			long elapsedFractions = (long) ((System.nanoTime() - nanoClock) / 1e8);
			
			minutes = minutesBackup;
			seconds = secondsBackup;
			fractions = fractionsBackup;

			for (int i = 0; i < elapsedFractions; i++) {
				decrementClock();
			}
		}

		seconds += increment;
		if (seconds >= 60) {
			minutes += 1; // TODO: this could be weird if the increment was more than 60 seconds.
			seconds -= 60;
		}
		checkLowOnTime();
		this.setForeground(Color.DARK_GRAY);
		this.setText(this.toString());
	}

	private void flag() {
		this.stopClock();
		flagged = true;
	}

	private void checkLowOnTime() {
		if (seconds + (60 * minutes) < initialTime / LOW_FRACTION) {
			this.setForeground(Color.RED);
		} else {
			this.setForeground(Color.BLACK);
		}

	}

	public boolean isFlagged() {
		return flagged;
	}

	private class Clock extends Timer {

		public Clock() {
			super(93, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					decrementClock();
				}

			});
		}

	}

}
