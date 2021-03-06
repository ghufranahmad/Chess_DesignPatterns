package chess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * This is the Time Class. It contains all the required variables and functions
 * related to the timer on the Main GUI It uses a Timer Class
 *
 */

public class Time {
	private JLabel timeLabel;
	Timer countDownTimer;
	int remainingTime;
	Main mainClassRef;

	public Time(JLabel passedLabel, Main ref) {
		countDownTimer = new Timer(1000, new CountdownTimerListener());
		this.timeLabel = passedLabel;
		remainingTime = Main.timeRemaining;
		mainClassRef = ref;
	}

	public void startTimer() {
		countDownTimer.start();
	}

	public void resetTimer() {
		remainingTime = Main.timeRemaining;
	}

	class CountdownTimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int min, sec;
			if (remainingTime > 0) {
				min = remainingTime / 60;
				sec = remainingTime % 60;
				timeLabel.setText(
						String.valueOf(min) + ":" + (sec >= 10 ? String.valueOf(sec) : "0" + String.valueOf(sec)));
				remainingTime--;
			} else {
				timeLabel.setText("Time's up!");
				resetTimer();
				startTimer();
				mainClassRef.changeTurn();
			}
		}
	}
}