package project.board;

import project.threads.GhostThread;
import project.threads.PlayerThread;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.*;

public class BoardModel
    extends AbstractTableModel{

    private final int[][] board;
    private final int height;
    private final int width;
    private int playerStartX, playerStartY;
    private int playerCurrX, playerCurrY;
    private int score;
    private final JLabel scoreLabel;
    private int lives;
    private final JLabel livesLabel;
    private int scoreToGet;
    private final int ghostCount;
    private final int[][] xyGhosts;
    private final static double MIN_DISTANCE = 4;
    private final static int WALL = 1;
    private final static int POINT = 0;
    private final static int STOP_GHOSTS = 4;
    private final static int ACCELERATION = 30;
    private final static int ADD_SCORE = 25;
    private final static int ADDITIONAL_LIFE = 15;
    private final static int WALL_DIS = 20;
    private final static int PLAYER = 2;
    private final static int EMPTY = 5;
    private final static int GHOST = 3;
    private final BoardFrame boardFrame;
    private GhostThread[] ghostThreads;
    private PlayerThread playerThread;

    public BoardModel(int sizeX, int sizeY, JLabel scoreLabel, JLabel livesLabel, BoardFrame boardFrame) {
        this.livesLabel = livesLabel;
        this.scoreLabel = scoreLabel;
        this.boardFrame = boardFrame;
        this.board = new int[sizeX][sizeY];
        this.height = board[0].length;
        this.width = board.length;
        this.lives = 3;
        this.score = 0;
        this.scoreToGet = 0;
        this.ghostCount = (width + height)/10 >= 4 ? 4 : (width + height)/10;
        this.xyGhosts = new int[this.ghostCount][2];
        prepare();
        addAreas();
        for (int i = 1; i < width - 1; i++){
            for (int j = 1; j < height - 1; j++){
                if (i == 1 || j == 1 || i == width - 2 || j == height - 2){
                    board[i][j] = POINT;
                }
            }
        }
        setPlayerPosition();
        setGhostsPositions();
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                if (board[i][j] == POINT)
                    this.scoreToGet += 1;
            }
        }
        this.scoreToGet += ghostCount;
    }

    public void prepare() {
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                board[i][j] = WALL;
            }
        }

        int startX = 1;
        int startY = 1;

        generate(startX, startY);
    }

    public void generate(int x, int y) {
        board[x][y] = POINT;

        ArrayList<Integer> directions = new ArrayList<>();
        directions.add(0);
        directions.add(1);
        directions.add(2);
        directions.add(3);
        Collections.shuffle(directions);

        for (Integer direction : directions) {
            switch (direction) {
                case 0 -> { //up
                    if (x > 1 && board[x - 2][y] == WALL && x - 2 > 1) {
                        board[x - 2][y] = POINT;
                        board[x - 1][y] = POINT;
                        generate(x - 2, y);
                    }
                }
                case 1 -> { //right
                    if (y < height - 2 && board[x][y + 2] == WALL && y + 2 < height - 2) {
                        board[x][y + 2] = POINT;
                        board[x][y + 1] = POINT;
                        generate(x, y + 2);
                    }
                }
                case 2 -> { //down
                    if (x < width - 2 && board[x + 2][y] == WALL && x + 2 < width - 2) {
                        board[x + 2][y] = POINT;
                        board[x + 1][y] = POINT;
                        generate(x + 2, y);
                    }
                }
                case 3 -> { //left
                    if (y > 1 && board[x][y - 2] == WALL && y - 2 > 1) {
                        board[x][y - 2] = POINT;
                        board[x][y - 1] = POINT;
                        generate(x, y - 2);
                    }
                }
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if ((i == width - 2 && j != 0 && j != height - 1) || (j == height - 2 && i != 0 && i != width - 1)) {
                    board[i][j] = POINT;
                }
            }
        }
    }

    public void addAreas(){
        Random random = new Random();
        int areasCount = (height + width)/5;
        int maxSide = 0;
        if (height >= 50 || width >= 50){
            maxSide = 9;
            areasCount += 2;
        }
        for (int i = 0; i <= areasCount; i++){
            int side1 = random.nextInt(width /3);
            if (side1 < 2){
                side1 = 2;
            }
            if (height >= 50 || width >= 50){
                while (side1 > maxSide){
                    side1--;
                }
            }
            int side2 = random.nextInt(height /3);
            if (side2 < 2) {
                side2 = 2;
            }
            if (height >= 50 || width >= 50){
                while (side2 > maxSide){
                    side2--;
                }
            }
            int obX = random.nextInt(width - 2);
            while (obX + side1 > width - 2 || obX == 0 || obX == width - 1){
                if (side1 > 1) {
                    side1 -= 1;
                }else {
                    obX = random.nextInt(width - 2);
                }
            }
            int obY = random.nextInt(height - 2);
            while (obY + side2 > height - 2 || obY == 0 || obY == height - 1){
                if (side2 > 1) {
                    side2 -= 1;
                }else {
                    obY = random.nextInt(height - 2);
                }
            }

            for (int j = obX; j < side1 + obX; j++){
                for (int k = obY; k < side2 + obY; k++){
                    board[j][k] = POINT;
                }
            }
        }
    }

    public void setPlayerPosition(){
        Random random = new Random();
        int playerX, playerY;
        do{
            playerX = random.nextInt(width - 1);
            playerY = random.nextInt(height - 1);
        }while (board[playerX][playerY] == WALL);
        board[playerX][playerY] = PLAYER;
        this.playerStartX = playerX;
        this.playerStartY = playerY;
        this.playerCurrX = playerX;
        this.playerCurrY = playerY;
    }

    public void setGhostsPositions(){
        Random random = new Random();
        int ghostX, ghostY;
        for (int i = 0; i < this.ghostCount; i++) {
            do {
                ghostX = random.nextInt(width - 1);
                ghostY = random.nextInt(height - 1);
            } while (board[ghostX][ghostY] == WALL || getPlayerGhostDistance(ghostX, ghostY, this.playerStartX, this.playerStartY) < MIN_DISTANCE);
            board[ghostX][ghostY] = GHOST;
            this.xyGhosts[i] = new int[]{ghostX, ghostY};
        }

    }

    public double getPlayerGhostDistance(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public synchronized boolean isMovable(int x, int y){
        return board[x][y] != WALL;
    }

    public synchronized void playersMovement(int x1, int y1, int x2, int y2){
        board[x1][y1] = EMPTY;
        if (board[x2][y2] == POINT){
            board[x2][y2] = PLAYER;
            addScore();
            this.scoreToGet -= 1;
            this.scoreLabel.setText("Score: " + getScore());
            if (this.scoreToGet == 0){
                boardFrame.gameWon(score);
            }
        }
        if (board[x2][y2] == GHOST){
            lostLife();
            this.livesLabel.setText("Lifes: " + getLives());
            if (getLives() == 0){
                boardFrame.stopRunning();
            }
            this.playerCurrX = this.playerStartX;
            this.playerCurrY = this.playerStartY;
            board[x2][y2] = GHOST;

        }else if (board[x2][y2] == STOP_GHOSTS) {
            this.playerCurrX = x2;
            this.playerCurrY = y2;
            for (GhostThread ghostThread : ghostThreads) {
                ghostThread.setStopped(true);
            }
            board[x2][y2] = PLAYER;

        }else if (board[x2][y2] == ACCELERATION) {
            board[x2][y2] = PLAYER;
            this.playerCurrX = x2;
            this.playerCurrY = y2;
            playerThread.increaseSpeed();

        }else if (board[x2][y2] == ADD_SCORE) {
            board[x2][y2] = PLAYER;
            this.playerCurrX = x2;
            this.playerCurrY = y2;
            this.score += 50;
            this.scoreLabel.setText("Score: " + getScore());

        }else if (board[x2][y2] == ADDITIONAL_LIFE) {
            board[x2][y2] = PLAYER;
            this.playerCurrX = x2;
            this.playerCurrY = y2;
            this.lives += 1;
            this.livesLabel.setText("Lives: " + getLives());

        }else if (board[x2][y2] == WALL_DIS) {
            board[x2][y2] = PLAYER;
            this.playerCurrX = x2;
            this.playerCurrY = y2;
            this.scoreToGet += 1;

            boolean done = false;
            for (int i = 1; i < width - 1 && !done; i++) {
                for (int j = 1; j < height - 1 && !done; j++) {
                    if (board[i][j] == WALL) {
                        board[i][j] = POINT;
                        done = true;
                    }

                }

            }
        }else{
            board[x2][y2] = PLAYER;
            this.playerCurrX = x2;
            this.playerCurrY = y2;
        }

    }

    public synchronized boolean isMovableGhost(int x, int y){
        return board[x][y] != GHOST;
    }

    public synchronized void ghostsMovement(int x1, int y1, int x2, int y2) {
        int ghostIndeks = -1;
        for (int i = 0; i < this.ghostCount; i++) {
            int[] ghostCoords = this.xyGhosts[i];
            if (ghostCoords[0] == x1 && ghostCoords[1] == y1) {
                ghostIndeks = i;
                break;
            }
        }
        if (ghostIndeks != -1) {
            if (board[x2][y2] == PLAYER) {
                lostLife();
                this.livesLabel.setText("Lives: " + getLives());
                if (getLives() == 0) {
                    boardFrame.stopRunning();
                }

                this.playerCurrX = this.playerStartX;
                this.playerCurrY = this.playerStartY;
                board[x2][y2] = GHOST;
                board[x1][y1] = EMPTY;
            }else{
                board[x2][y2] += GHOST;
                board[x1][y1] -= GHOST;
            }
            this.xyGhosts[ghostIndeks] = new int[]{x2, y2};
        }

    }

    public synchronized void addUpgrade(int x, int y, int upgrade){
        switch (upgrade) {
            case 0 -> {
                if (board[x][y] == POINT)
                    this.scoreToGet -= 1;
                board[x][y] = ACCELERATION;
            }
            case 1 -> {
                if (board[x][y] == POINT)
                    this.scoreToGet -= 1;
                board[x][y] = STOP_GHOSTS;
            }
            case 2 ->{
                if (board[x][y] == POINT)
                    this.scoreToGet -= 1;
                board[x][y] = ADD_SCORE;
            }
            case 3 ->{
                if (board[x][y] == POINT)
                    this.scoreToGet -= 1;
                board[x][y] = ADDITIONAL_LIFE;
            }
            case 4 -> {
                if (board[x][y] == POINT)
                    this.scoreToGet -= 1;
                board[x][y] = WALL_DIS;
            }
        }
    }

    @Override
    public int getRowCount() {
        return this.board[0].length;
    }

    @Override
    public int getColumnCount() {
        return this.board.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return board[columnIndex][rowIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        board[columnIndex][rowIndex] = (int)aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public ArrayList<int[]> getNeighbours(int x, int y){
        ArrayList<int[]> neighbours = new ArrayList<>();
        if (isMovable(x+1, y))
            neighbours.add(new int[]{x+1, y});
        if (isMovable(x-1, y))
            neighbours.add(new int[]{x-1, y});
        if (isMovable(x, y+1))
            neighbours.add(new int[]{x, y+1});
        if (isMovable(x, y-1))
            neighbours.add(new int[]{x, y-1});
        return neighbours;
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

    public synchronized int getPlayerX() {
        return playerCurrX;
    }

    public synchronized int getPlayerY() {
        return playerCurrY;
    }

    public int getGhostCount() {
        return this.ghostCount;
    }

    public int[][] getXyGhosts() {
        return xyGhosts;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setGhostThreads(GhostThread[] ghostThreads) {
        this.ghostThreads = ghostThreads;
    }

    public void setPlayerThread(PlayerThread playerThread) {
        this.playerThread = playerThread;
    }
}
