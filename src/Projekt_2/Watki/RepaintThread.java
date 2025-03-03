package Projekt_2.Watki;

import Projekt_2.Plansza.PlanszaFrame;

public class RepaintThread
    extends Thread{

    private PlanszaFrame planszaFrame;

    public RepaintThread(PlanszaFrame planszaFrame) {
        this.planszaFrame = planszaFrame;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            planszaFrame.repaint();
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
