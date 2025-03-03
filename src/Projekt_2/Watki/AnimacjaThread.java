package Projekt_2.Watki;

import Projekt_2.Plansza.PlanszaFrame;
import Projekt_2.Plansza.PlanszaRenderer;

import javax.swing.*;

public class AnimacjaThread
    extends Thread{

    private ImageIcon[] prawo;
    private ImageIcon[] lewo;
    private ImageIcon[] gora;
    private ImageIcon[] dol;
    private PlanszaRenderer planszaRenderer;
    private GraczThread graczThread;
    private PlanszaFrame planszaFrame;

    public AnimacjaThread(PlanszaRenderer planszaRenderer, GraczThread graczThread, PlanszaFrame planszaFrame){
        this.planszaFrame = planszaFrame;
        this.graczThread = graczThread;
        this.planszaRenderer = planszaRenderer;
        this.prawo = new ImageIcon[4];
        this.lewo = new ImageIcon[4];
        this.gora = new ImageIcon[4];
        this.dol = new ImageIcon[4];

        this.prawo[0] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwPrawo1.png");
        this.prawo[1] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwPrawo2.png");
        this.prawo[2] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwPrawo1.png");
        this.prawo[3] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwPrawo0.png");

        this.lewo[0] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwLewo1.png");
        this.lewo[1] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwLewo2.png");
        this.lewo[2] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwLewo1.png");
        this.lewo[3] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwLewo0.png");

        this.gora[0] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwGora1.png");
        this.gora[1] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwGora2.png");
        this.gora[2] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwGora1.png");
        this.gora[3] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwGora0.png");

        this.dol[0] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwDol1.png");
        this.dol[1] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwDol2.png");
        this.dol[2] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwDol1.png");
        this.dol[3] = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwDol0.png");
    }

    @Override
    public void run() {
        int indeks = 0;
        int kierunek;
        while (!planszaFrame.isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (!Thread.interrupted() && planszaFrame.isRunning()){
            synchronized(this) {
                kierunek = graczThread.getKierunek();
            }
            switch (kierunek){
                case 0 -> { //prawo
                    planszaRenderer.setGracz(prawo[indeks]);
                    planszaFrame.repaint();
                }
                case 1 -> { //lewo
                    planszaRenderer.setGracz(lewo[indeks]);
                    planszaFrame.repaint();
                }
                case 2 -> { //dol
                    planszaRenderer.setGracz(dol[indeks]);
                    planszaFrame.repaint();
                }
                case 3 -> { //gora
                    planszaRenderer.setGracz(gora[indeks]);
                    planszaFrame.repaint();
                }
            }
            if (indeks == 3){
                indeks = 0;
            }else
                indeks += 1;
            try {
                sleep(50);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}
