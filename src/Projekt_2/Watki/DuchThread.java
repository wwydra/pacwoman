package Projekt_2.Watki;

import Projekt_2.Plansza.PlanszaFrame;
import Projekt_2.Plansza.PlanszaModel;
import Projekt_2.Plansza.PlanszaRenderer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class DuchThread
    extends Thread {

    private final PlanszaModel planszaModel;
    private PlanszaFrame planszaFrame;
    private PlanszaRenderer planszaRenderer;
    private boolean zatrzymany;
    private ImageIcon duch;
    private ImageIcon przestraszonyDuch;
    private int x, y;
    private boolean[][] zajete;

    public DuchThread(PlanszaModel planszaModel, PlanszaFrame planszaFrame, PlanszaRenderer planszaRenderer, int x, int y) {
        this.planszaModel = planszaModel;
        this.planszaFrame = planszaFrame;
        this.planszaRenderer = planszaRenderer;
        this.zatrzymany = false;
        this.x = x;
        this.y = y;
        this.duch = new ImageIcon("src/Projekt_2/PlikiGraficzne/duch.png");
        this.przestraszonyDuch = new ImageIcon("src/Projekt_2/PlikiGraficzne/przestraszonyDuch.png");
        this.zajete = new boolean[planszaModel.getSzerokosc()][planszaModel.getWysokosc()];
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
            ArrayList<int[]> sciezka = scigajGracza(x, y);
            if (sciezka.size() > 1) {
                int[] kolejnyRuch = sciezka.get(1);
                int[] spr;
                if (sciezka.size() > 2)
                    spr = sciezka.get(2);
                else
                    spr = sciezka.get(1);
                synchronized (planszaModel) {
                    while (!planszaModel.isMovableDuch(spr[0], spr[1]) || !planszaModel.isMovableDuch(kolejnyRuch[0], kolejnyRuch[1])) {
                        try {
                            planszaModel.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    planszaModel.ruchDucha(x, y, kolejnyRuch[0], kolejnyRuch[1]);
                    planszaFrame.repaint();
                    x = kolejnyRuch[0];
                    y = kolejnyRuch[1];
                    planszaModel.notifyAll();
                }
            }
            try {
                Thread.sleep(330);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            while (zatrzymany) {
                planszaRenderer.setDuch(przestraszonyDuch);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.zatrzymany = false;
                planszaRenderer.setDuch(duch);
            }
        }
    }

    public synchronized ArrayList<int[]> scigajGracza(int x, int y) {
        int graczX = planszaModel.getGraczX();
        int graczY = planszaModel.getGraczY();

        boolean[][] odwiedzone = new boolean[planszaModel.getSzerokosc()][planszaModel.getWysokosc()];
        int[][] parentX = new int[planszaModel.getSzerokosc()][planszaModel.getWysokosc()];
        int[][] parentY = new int[planszaModel.getSzerokosc()][planszaModel.getWysokosc()];

        for (int i = 0; i < planszaModel.getSzerokosc(); i++){
            for (int j = 0; j < planszaModel.getWysokosc(); j++) {
                odwiedzone[i][j] = false;
            }
        }

        Queue<int[]> kolejka = new LinkedList<>();
        int[] start = {x, y};
        odwiedzone[x][y] = true;
        kolejka.add(start);

        while (!kolejka.isEmpty()){
            int[] curr = kolejka.poll();

            ArrayList<int[]> sasiedzi = planszaModel.getSasiedzi(curr[0], curr[1]);
            for (int[] sasiad : sasiedzi) {
                if (!odwiedzone[sasiad[0]][sasiad[1]]) {
                    odwiedzone[sasiad[0]][sasiad[1]] = true;
                    parentX[sasiad[0]][sasiad[1]] = curr[0];
                    parentY[sasiad[0]][sasiad[1]] = curr[1];
                    kolejka.add(sasiad);
                }
            }
        }

        ArrayList<int[]> sciezka = new ArrayList<>();
        int[] punkt = {graczX, graczY};
        sciezka.add(punkt);

        while (punkt[0] != x || punkt[1] != y) {
            int[] rodzic = {parentX[punkt[0]][punkt[1]], parentY[punkt[0]][punkt[1]]};
            sciezka.add(0, rodzic);
            punkt = rodzic;
        }

        return sciezka;
    }

    public synchronized void setZatrzymany(boolean zatrzymany) {
        this.zatrzymany = zatrzymany;
    }
}

