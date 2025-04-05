package project.threads;

import project.board.BoardFrame;

import javax.swing.*;

public class TimeThread
    extends Thread{

    private final JLabel timeLabel;
    private final BoardFrame boardFrame;
    int sec;
    int min;

    public TimeThread(JLabel time, BoardFrame boardFrame){
        this.timeLabel = time;
        this.boardFrame = boardFrame;
        this.sec = 0;
        this.min = 0;
    }

    @Override
    public void run() {
        while (!boardFrame.isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (!Thread.interrupted() && boardFrame.isRunning()) {
            if (sec == 59){
                min += 1;
                sec = 0;
            }
            sec++;
            String time = String.format("%02d:%02d", min, sec);
            timeLabel.setText("Time: " + time + " ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
