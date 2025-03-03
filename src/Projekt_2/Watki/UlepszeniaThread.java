package Projekt_2.Watki;

import Projekt_2.Plansza.PlanszaFrame;
import Projekt_2.Plansza.PlanszaModel;

import java.util.Random;

public class UlepszeniaThread
    extends Thread{

    private PlanszaFrame planszaFrame;
    private PlanszaModel planszaModel;
    private int x, y;
    private final static int PRZYSPIESZENIE = 0;
    private final static int STOP_DUCHY = 1;
    private final static int ADD_SCORE = 2;
    private final static int DODATKOWE_ZYCIE = 3;
    private final static int SCIANA_ZNIKA = 4;

    public UlepszeniaThread(PlanszaFrame planszaFrame, PlanszaModel planszaModel) {
        this.planszaFrame = planszaFrame;
        this.planszaModel = planszaModel;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (!planszaFrame.isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (!Thread.interrupted() && planszaFrame.isRunning()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (Math.random() < 0.25){
                do {
                    x = random.nextInt(planszaModel.getSzerokosc() - 1);
                    y = random.nextInt(planszaModel.getWysokosc() - 1);
                }while (!planszaModel.isMovable(x, y) || !planszaModel.isMovableDuch(x, y));

                int ulepszenie = random.nextInt(5);
                switch (ulepszenie){
                    case PRZYSPIESZENIE -> planszaModel.dodajUlepszenie(x, y, PRZYSPIESZENIE);
                    case STOP_DUCHY -> planszaModel.dodajUlepszenie(x, y, STOP_DUCHY);
                    case ADD_SCORE -> planszaModel.dodajUlepszenie(x, y, ADD_SCORE);
                    case DODATKOWE_ZYCIE -> planszaModel.dodajUlepszenie(x, y, DODATKOWE_ZYCIE);
                    case SCIANA_ZNIKA -> planszaModel.dodajUlepszenie(x, y, SCIANA_ZNIKA);
                }
            }
        }
    }
}
