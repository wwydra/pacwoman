package Projekt_2.Watki;

import Projekt_2.Plansza.PlanszaFrame;

import javax.swing.*;

public class TimeThread
    extends Thread{

    private JLabel timeLabel;
    private PlanszaFrame planszaFrame;
    int sek;
    int min;

    public TimeThread(JLabel time, PlanszaFrame planszaFrame){
        this.timeLabel = time;
        this.planszaFrame = planszaFrame;
        this.sek = 0;
        this.min = 0;
    }

    @Override
    public void run() {
        while (!planszaFrame.isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (!Thread.interrupted() && planszaFrame.isRunning()) {
            if (sek == 59){
                min += 1;
                sek = 0;
            }
            sek++;
            String czas = String.format("%02d:%02d", min, sek);
            timeLabel.setText("Time: " + czas + " ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
