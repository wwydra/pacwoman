package project.threads;

import project.board.BoardFrame;
import project.board.BoardModel;

import java.util.Random;

public class UpgradesThread
    extends Thread{

    private final BoardFrame boardFrame;
    private final BoardModel boardModel;
    private final static int ACCELERATION = 0;
    private final static int STOP_GHOSTS = 1;
    private final static int ADD_SCORE = 2;
    private final static int ADDITIONAL_LIFE = 3;
    private final static int WALL_DIS = 4;

    public UpgradesThread(BoardFrame boardFrame, BoardModel boardModel) {
        this.boardFrame = boardFrame;
        this.boardModel = boardModel;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (!boardFrame.isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (!Thread.interrupted() && boardFrame.isRunning()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (Math.random() < 0.25){
                int x;
                int y;
                do {
                    x = random.nextInt(boardModel.getWidth() - 1);
                    y = random.nextInt(boardModel.getHeight() - 1);
                }while (!boardModel.isMovable(x, y) || !boardModel.isMovableGhost(x, y));

                int upgrade = random.nextInt(5);
                switch (upgrade){
                    case ACCELERATION -> boardModel.addUpgrade(x, y, ACCELERATION);
                    case STOP_GHOSTS -> boardModel.addUpgrade(x, y, STOP_GHOSTS);
                    case ADD_SCORE -> boardModel.addUpgrade(x, y, ADD_SCORE);
                    case ADDITIONAL_LIFE -> boardModel.addUpgrade(x, y, ADDITIONAL_LIFE);
                    case WALL_DIS -> boardModel.addUpgrade(x, y, WALL_DIS);
                }
            }
        }
    }
}
