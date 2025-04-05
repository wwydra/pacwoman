package project.threads;

import project.board.BoardFrame;

public class RepaintThread
    extends Thread{

    private final BoardFrame boardFrame;

    public RepaintThread(BoardFrame boardFrame) {
        this.boardFrame = boardFrame;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            boardFrame.repaint();
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
