package Projekt_2.Watki;

import Projekt_2.Plansza.PlanszaFrame;
import Projekt_2.Plansza.PlanszaModel;
public class GraczThread
    extends Thread{

    private final PlanszaModel planszaModel;
    private final PlanszaFrame planszaFrame;
    private boolean zmienPredkosc = false;
    private int kierunek;
    private int x, y;

    public GraczThread(PlanszaModel planszaModel, PlanszaFrame planszaFrame) {
        this.planszaModel = planszaModel;
        this.planszaFrame = planszaFrame;
        this.x = planszaModel.getGraczX();
        this.y = planszaModel.getGraczY();
        this.kierunek = 0;
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
        int iter = 0;
        while (!Thread.interrupted() && planszaFrame.isRunning()) {
            if (!zmienPredkosc) {
                int kierunek;
                synchronized(this) {
                    kierunek = this.kierunek;
                }
                x = planszaModel.getGraczX();
                y = planszaModel.getGraczY();
                    switch (kierunek) {
                        case 0 -> { //prawo
                            if (planszaModel.isMovable(x + 1, y)) {
                                planszaModel.ruchGracza(x, y, x + 1, y);
                                x++;
                            }
                        }
                        case 1 -> { //lewo
                            if (planszaModel.isMovable(x - 1, y)) {
                                planszaModel.ruchGracza(x, y, x - 1, y);
                                x--;
                            }
                        }
                        case 2 -> { //dol
                            if (planszaModel.isMovable(x, y + 1)) {
                                planszaModel.ruchGracza(x, y, x, y + 1);
                                y++;
                            }
                        }
                        case 3 -> { //gora
                            if (planszaModel.isMovable(x, y - 1)) {
                                planszaModel.ruchGracza(x, y, x, y - 1);
                                y--;
                            }
                        }
                    }
                    try {
                        Thread.sleep(230);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
            }else{
                iter++;
                if (iter > 10){
                    zmienPredkosc = false;
                    iter = 0;
                }
                int kierunek;
                synchronized(this) {
                    kierunek = this.kierunek;
                }
                x = planszaModel.getGraczX();
                y = planszaModel.getGraczY();
                switch (kierunek) {
                    case 0 -> { //prawo
                        if (planszaModel.isMovable(x + 1, y)) {
                            planszaModel.ruchGracza(x, y, x + 1, y);
                            x++;
                        }
                    }
                    case 1 -> { //lewo
                        if (planszaModel.isMovable(x - 1, y)) {
                            planszaModel.ruchGracza(x, y, x - 1, y);
                            x--;
                        }
                    }
                    case 2 -> { //dol
                        if (planszaModel.isMovable(x, y + 1)) {
                            planszaModel.ruchGracza(x, y, x, y + 1);
                            y++;
                        }
                    }
                    case 3 -> { //gora
                        if (planszaModel.isMovable(x, y - 1)) {
                            planszaModel.ruchGracza(x, y, x, y - 1);
                            y--;
                        }
                    }
                }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        }
    }

    public synchronized void setKierunek(int kierunek) {
        this.kierunek = kierunek;
    }

    public synchronized int getKierunek() {
        return this.kierunek;
    }

    public synchronized void zwiekszPredkosc(){
        this.zmienPredkosc = true;
    }
}
