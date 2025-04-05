package project.threads;

import project.board.BoardFrame;
import project.board.BoardModel;
public class PlayerThread
    extends Thread{

    private final BoardModel boardModel;
    private final BoardFrame boardFrame;
    private boolean changeSpeed = false;
    private int direction;
    private int x, y;

    public PlayerThread(BoardModel boardModel, BoardFrame boardFrame) {
        this.boardModel = boardModel;
        this.boardFrame = boardFrame;
        this.x = boardModel.getPlayerX();
        this.y = boardModel.getPlayerY();
        this.direction = 0;
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
        int iter = 0;
        while (!Thread.interrupted() && boardFrame.isRunning()) {
            if (!changeSpeed) {
                int direction;
                synchronized(this) {
                    direction = this.direction;
                }
                x = boardModel.getPlayerX();
                y = boardModel.getPlayerY();
                    switch (direction) {
                        case 0 -> { //right
                            if (boardModel.isMovable(x + 1, y)) {
                                boardModel.playersMovement(x, y, x + 1, y);
                                x++;
                            }
                        }
                        case 1 -> { //left
                            if (boardModel.isMovable(x - 1, y)) {
                                boardModel.playersMovement(x, y, x - 1, y);
                                x--;
                            }
                        }
                        case 2 -> { //down
                            if (boardModel.isMovable(x, y + 1)) {
                                boardModel.playersMovement(x, y, x, y + 1);
                                y++;
                            }
                        }
                        case 3 -> { //up
                            if (boardModel.isMovable(x, y - 1)) {
                                boardModel.playersMovement(x, y, x, y - 1);
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
                    changeSpeed = false;
                    iter = 0;
                }
                int direction;
                synchronized(this) {
                    direction = this.direction;
                }
                x = boardModel.getPlayerX();
                y = boardModel.getPlayerY();
                switch (direction) {
                    case 0 -> { //right
                        if (boardModel.isMovable(x + 1, y)) {
                            boardModel.playersMovement(x, y, x + 1, y);
                            x++;
                        }
                    }
                    case 1 -> { //left
                        if (boardModel.isMovable(x - 1, y)) {
                            boardModel.playersMovement(x, y, x - 1, y);
                            x--;
                        }
                    }
                    case 2 -> { //down
                        if (boardModel.isMovable(x, y + 1)) {
                            boardModel.playersMovement(x, y, x, y + 1);
                            y++;
                        }
                    }
                    case 3 -> { //up
                        if (boardModel.isMovable(x, y - 1)) {
                            boardModel.playersMovement(x, y, x, y - 1);
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

    public synchronized void setDirection(int direction) {
        this.direction = direction;
    }

    public synchronized int getDirection() {
        return this.direction;
    }

    public synchronized void increaseSpeed(){
        this.changeSpeed = true;
    }
}
