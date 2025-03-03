package Projekt_2.Plansza;

import Projekt_2.Watki.DuchThread;
import Projekt_2.Watki.GraczThread;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.*;

public class PlanszaModel
    extends AbstractTableModel{

    private int[][] plansza;
    private int wysokosc;
    private int szerokosc;
    private int graczStartX, graczStartY;
    private int graczCurrX, graczCurrY;
    private int score;
    private JLabel scoreLabel;
    private int lives;
    private JLabel livesLabel;
    private int scoreDoZebrania;
    private int liczbaDuchow;
    private int[][] xyDuchy;
    private final static double MIN_ODLEGLOSC = 4;
    private final static int SCIANA = 1;
    private final static int PUNKT = 0;
    private final static int STOP_DUCHY = 4;
    private final static int PRZYSPIESZENIE = 30;
    private final static int ADD_SCORE = 25;
    private final static int DODATKOWE_ZYCIE = 15;
    private final static int SCIANA_ZNIKA = 20;
    private final static int GRACZ = 2;
    private final static int PUSTE_POLE = 5;
    private final static int DUCH = 3;
    private final PlanszaFrame planszaFrame;
    private DuchThread[] duchThreads;
    private GraczThread graczThread;

    public PlanszaModel(int sizeX, int sizeY, JLabel scoreLabel, JLabel livesLabel, PlanszaFrame planszaFrame) {
        this.livesLabel = livesLabel;
        this.scoreLabel = scoreLabel;
        this.planszaFrame = planszaFrame;
        this.plansza = new int[sizeX][sizeY];
        this.wysokosc = plansza[0].length;
        this.szerokosc = plansza.length;
        this.lives = 3;
        this.score = 0;
        this.scoreDoZebrania = 0;
        this.liczbaDuchow = (szerokosc+wysokosc)/10 >= 4 ? 4 : (szerokosc+wysokosc)/10;
        this.xyDuchy = new int[this.liczbaDuchow][2];
        przygotuj();
        dodajObszary();
        for (int i = 1; i < szerokosc - 1; i++){
            for (int j = 1; j < wysokosc - 1; j++){
                if (i == 1 || j == 1 || i == szerokosc - 2 || j == wysokosc - 2){
                    plansza[i][j] = PUNKT;
                }
            }
        }
        ustalPozycjeGracza();
        ustalPozycjeDuchow();
        for (int i = 0; i < szerokosc; i++){
            for (int j = 0; j < wysokosc; j++){
                if (plansza[i][j] == PUNKT)
                    this.scoreDoZebrania += 1;
            }
        }
        this.scoreDoZebrania += liczbaDuchow;
    }

    public void przygotuj() {
        for (int i = 0; i < szerokosc; i++){
            for (int j = 0; j < wysokosc; j++){
                plansza[i][j] = SCIANA;
            }
        }

        int startX = 1;
        int startY = 1;

        generuj(startX, startY);
    }

    public void generuj(int x, int y) {
        plansza[x][y] = PUNKT;

        ArrayList<Integer> kierunki = new ArrayList<>();
        kierunki.add(0);
        kierunki.add(1);
        kierunki.add(2);
        kierunki.add(3);
        Collections.shuffle(kierunki);

        for (Integer kierunek : kierunki) {
            switch (kierunek) {
                case 0 -> { //gora
                    if (x > 1 && plansza[x - 2][y] == SCIANA && x - 2 > 1) {
                        plansza[x - 2][y] = PUNKT;
                        plansza[x - 1][y] = PUNKT;
                        generuj(x - 2, y);
                    }
                }
                case 1 -> { //prawo
                    if (y < wysokosc - 2 && plansza[x][y + 2] == SCIANA && y + 2 < wysokosc - 2) {
                        plansza[x][y + 2] = PUNKT;
                        plansza[x][y + 1] = PUNKT;
                        generuj(x, y + 2);
                    }
                }
                case 2 -> { //dol
                    if (x < szerokosc - 2 && plansza[x + 2][y] == SCIANA && x + 2 < szerokosc - 2) {
                        plansza[x + 2][y] = PUNKT;
                        plansza[x + 1][y] = PUNKT;
                        generuj(x + 2, y);
                    }
                }
                case 3 -> { //lewo
                    if (y > 1 && plansza[x][y - 2] == SCIANA && y - 2 > 1) {
                        plansza[x][y - 2] = PUNKT;
                        plansza[x][y - 1] = PUNKT;
                        generuj(x, y - 2);
                    }
                }
            }
        }
        for (int i = 0; i < szerokosc; i++) {
            for (int j = 0; j < wysokosc; j++) {
                if ((i == szerokosc - 2 && j != 0 && j != wysokosc - 1) || (j == wysokosc - 2 && i != 0 && i != szerokosc - 1)) {
                    plansza[i][j] = PUNKT;
                }
            }
        }
    }

    public void dodajObszary(){
        Random random = new Random();
        int liczbaObszarow = (wysokosc + szerokosc)/5;
        int maxBok = 0;
        if (wysokosc >= 50 || szerokosc >= 50){
            maxBok = 9;
            liczbaObszarow += 2;
        }
        for (int i = 0; i <= liczbaObszarow; i++){
            int bok1 = random.nextInt(szerokosc/3);
            if (bok1 < 2){
                bok1 = 2;
            }
            if (wysokosc >= 50 || szerokosc >= 50){
                while (bok1 > maxBok){
                    bok1--;
                }
            }
            int bok2 = random.nextInt(wysokosc/3);
            if (bok2 < 2) {
                bok2 = 2;
            }
            if (wysokosc >= 50 || szerokosc >= 50){
                while (bok2 > maxBok){
                    bok2--;
                }
            }
            int obX = random.nextInt(szerokosc - 2);
            while (obX + bok1 > szerokosc - 2 || obX == 0 || obX == szerokosc - 1){
                if (bok1 > 1) {
                    bok1 -= 1;
                }else {
                    obX = random.nextInt(szerokosc - 2);
                }
            }
            int obY = random.nextInt(wysokosc - 2);
            while (obY + bok2 > wysokosc - 2 || obY == 0 || obY == wysokosc - 1){
                if (bok2 > 1) {
                    bok2 -= 1;
                }else {
                    obY = random.nextInt(wysokosc - 2);
                }
            }

            for (int j = obX; j < bok1 + obX; j++){
                for (int k = obY; k < bok2 + obY; k++){
                    plansza[j][k] = PUNKT;
                }
            }
        }
    }

    public void ustalPozycjeGracza(){
        Random random = new Random();
        int graczX, graczY;
        do{
            graczX = random.nextInt(szerokosc - 1);
            graczY = random.nextInt(wysokosc - 1);
        }while (plansza[graczX][graczY] == SCIANA);
        plansza[graczX][graczY] = GRACZ;
        this.graczStartX = graczX;
        this.graczStartY = graczY;
        this.graczCurrX = graczX;
        this.graczCurrY = graczY;

    }

    public void ustalPozycjeDuchow(){
        Random random = new Random();
        int duchX, duchY;
        for (int i = 0; i < this.liczbaDuchow; i++) {
            do {
                duchX = random.nextInt(szerokosc - 1);
                duchY = random.nextInt(wysokosc - 1);
            } while (plansza[duchX][duchY] == SCIANA || odlegloscGraczDuch(duchX, duchY, this.graczStartX, this.graczStartY) < MIN_ODLEGLOSC);
            plansza[duchX][duchY] = DUCH;
            this.xyDuchy[i] = new int[]{duchX, duchY};
        }

    }

    public double odlegloscGraczDuch(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public synchronized boolean isMovable(int x, int y){
        return plansza[x][y] != SCIANA;
    }

    public synchronized void ruchGracza(int x1, int y1, int x2, int y2){
        plansza[x1][y1] = PUSTE_POLE;
        if (plansza[x2][y2] == PUNKT){
            plansza[x2][y2] = GRACZ;
            addScore();
            this.scoreDoZebrania -= 1;
            this.scoreLabel.setText("Score: " + getScore());
            if (this.scoreDoZebrania == 0){
                planszaFrame.gameWon(score);
            }
        }
        if (plansza[x2][y2] == DUCH){
            lostLife();
            this.livesLabel.setText("Lifes: " + getLives());
            if (getLives() == 0){
                planszaFrame.stopRunning();
            }
            this.graczCurrX = this.graczStartX;
            this.graczCurrY = this.graczStartY;
            plansza[x2][y2] = DUCH;

        }else if (plansza[x2][y2] == STOP_DUCHY) {
            this.graczCurrX = x2;
            this.graczCurrY = y2;
            for (DuchThread duchThread : duchThreads) {
                duchThread.setZatrzymany(true);
            }
            plansza[x2][y2] = GRACZ;

        }else if (plansza[x2][y2] == PRZYSPIESZENIE) {
            plansza[x2][y2] = GRACZ;
            this.graczCurrX = x2;
            this.graczCurrY = y2;
            graczThread.zwiekszPredkosc();

        }else if (plansza[x2][y2] == ADD_SCORE) {
            plansza[x2][y2] = GRACZ;
            this.graczCurrX = x2;
            this.graczCurrY = y2;
            this.score += 50;
            this.scoreLabel.setText("Score: " + getScore());

        }else if (plansza[x2][y2] == DODATKOWE_ZYCIE) {
            plansza[x2][y2] = GRACZ;
            this.graczCurrX = x2;
            this.graczCurrY = y2;
            this.lives += 1;
            this.livesLabel.setText("Lives: " + getLives());

        }else if (plansza[x2][y2] == SCIANA_ZNIKA) {
            plansza[x2][y2] = GRACZ;
            this.graczCurrX = x2;
            this.graczCurrY = y2;
            this.scoreDoZebrania += 1;

            boolean done = false;
            for (int i = 1; i < szerokosc - 1 && !done; i++) {
                for (int j = 1; j < wysokosc - 1 && !done; j++) {
                    if (plansza[i][j] == SCIANA) {
                        plansza[i][j] = PUNKT;
                        done = true;
                    }

                }

            }
        }else{
            plansza[x2][y2] = GRACZ;
            this.graczCurrX = x2;
            this.graczCurrY = y2;
        }

    }

    public synchronized boolean isMovableDuch(int x, int y){
        return plansza[x][y] != DUCH;
    }

    public synchronized void ruchDucha(int x1, int y1, int x2, int y2) {
        int duchIndeks = -1;
        for (int i = 0; i < this.liczbaDuchow; i++) {
            int[] duchCoords = this.xyDuchy[i];
            if (duchCoords[0] == x1 && duchCoords[1] == y1) {
                duchIndeks = i;
                break;
            }
        }
        if (duchIndeks != -1) {
            if (plansza[x2][y2] == GRACZ) {
                lostLife();
                this.livesLabel.setText("Lives: " + getLives());
                if (getLives() == 0) {
                    planszaFrame.stopRunning();
                }

                this.graczCurrX = this.graczStartX;
                this.graczCurrY = this.graczStartY;
                plansza[x2][y2] = DUCH;
                plansza[x1][y1] = PUSTE_POLE;
                this.xyDuchy[duchIndeks] = new int[]{x2, y2};
            }else{
                plansza[x2][y2] += DUCH;
                plansza[x1][y1] -= DUCH;
                this.xyDuchy[duchIndeks] = new int[]{x2, y2};
            }
        }

    }

    public synchronized void dodajUlepszenie(int x, int y, int ulepszenie){
        switch (ulepszenie) {
            case 0 -> {
                if (plansza[x][y] == PUNKT)
                    this.scoreDoZebrania -= 1;
                plansza[x][y] = PRZYSPIESZENIE;
            }
            case 1 -> {
                if (plansza[x][y] == PUNKT)
                    this.scoreDoZebrania -= 1;
                plansza[x][y] = STOP_DUCHY;
            }
            case 2 ->{
                if (plansza[x][y] == PUNKT)
                    this.scoreDoZebrania -= 1;
                plansza[x][y] = ADD_SCORE;
            }
            case 3 ->{
                if (plansza[x][y] == PUNKT)
                    this.scoreDoZebrania -= 1;
                plansza[x][y] = DODATKOWE_ZYCIE;
            }
            case 4 -> {
                if (plansza[x][y] == PUNKT)
                    this.scoreDoZebrania -= 1;
                plansza[x][y] = SCIANA_ZNIKA;
            }
        }
    }

    @Override
    public int getRowCount() {
        return this.plansza[0].length;
    }

    @Override
    public int getColumnCount() {
        return this.plansza.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return plansza[columnIndex][rowIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        plansza[columnIndex][rowIndex] = (int)aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public ArrayList<int[]> getSasiedzi(int x, int y){
        ArrayList<int[]> sasiedzi = new ArrayList<>();
        if (isMovable(x+1, y))
            sasiedzi.add(new int[]{x+1, y});
        if (isMovable(x-1, y))
            sasiedzi.add(new int[]{x-1, y});
        if (isMovable(x, y+1))
            sasiedzi.add(new int[]{x, y+1});
        if (isMovable(x, y-1))
            sasiedzi.add(new int[]{x, y-1});
        return sasiedzi;
    }

    public synchronized int getScore() {
        return score;
    }

    public synchronized void addScore() {
        this.score += 1;
    }

    public synchronized int getLives() {
        return lives;
    }

    public synchronized void lostLife() {
        this.lives -= 1;
    }

    public synchronized int getGraczX() {
        return graczCurrX;
    }

    public synchronized int getGraczY() {
        return graczCurrY;
    }

    public int getLiczbaDuchow() {
        return this.liczbaDuchow;
    }

    public int[][] getXyDuchy() {
        return xyDuchy;
    }

    public int getWysokosc() {
        return wysokosc;
    }

    public int getSzerokosc() {
        return szerokosc;
    }

    public void setDuchThreads(DuchThread[] duchThreads) {
        this.duchThreads = duchThreads;
    }

    public void setGraczThread(GraczThread graczThread) {
        this.graczThread = graczThread;
    }
}
