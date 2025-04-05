package project.threads;

import project.board.BoardFrame;
import project.board.BoardRenderer;

import javax.swing.*;

public class Animation
    extends Thread{

    private final ImageIcon[] right;
    private final ImageIcon[] left;
    private final ImageIcon[] up;
    private final ImageIcon[] down;
    private final BoardRenderer boardRenderer;
    private final PlayerThread playerThread;
    private final BoardFrame boardFrame;

    public Animation(BoardRenderer boardRenderer, PlayerThread playerThread, BoardFrame boardFrame){
        this.boardFrame = boardFrame;
        this.playerThread = playerThread;
        this.boardRenderer = boardRenderer;
        this.right = new ImageIcon[4];
        this.left = new ImageIcon[4];
        this.up = new ImageIcon[4];
        this.down = new ImageIcon[4];

        this.right[0] = new ImageIcon("src/project/graphics/pacwRight1.png");
        this.right[1] = new ImageIcon("src/project/graphics/pacwRight2.png");
        this.right[2] = new ImageIcon("src/project/graphics/pacwRight1.png");
        this.right[3] = new ImageIcon("src/project/graphics/pacwRight0.png");

        this.left[0] = new ImageIcon("src/project/graphics/pacwLeft1.png");
        this.left[1] = new ImageIcon("src/project/graphics/pacwLeft2.png");
        this.left[2] = new ImageIcon("src/project/graphics/pacwLeft1.png");
        this.left[3] = new ImageIcon("src/project/graphics/pacwLeft0.png");

        this.up[0] = new ImageIcon("src/project/graphics/pacwUp1.png");
        this.up[1] = new ImageIcon("src/project/graphics/pacwUp2.png");
        this.up[2] = new ImageIcon("src/project/graphics/pacwUp1.png");
        this.up[3] = new ImageIcon("src/project/graphics/pacwUp0.png");

        this.down[0] = new ImageIcon("src/project/graphics/pacwDown1.png");
        this.down[1] = new ImageIcon("src/project/graphics/pacwDown2.png");
        this.down[2] = new ImageIcon("src/project/graphics/pacwDown1.png");
        this.down[3] = new ImageIcon("src/project/graphics/pacwDown0.png");
    }

    @Override
    public void run() {
        int index = 0;
        int direction;
        while (!boardFrame.isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (!Thread.interrupted() && boardFrame.isRunning()){
            synchronized(this) {
                direction = playerThread.getDirection();
            }
            switch (direction){
                case 0 -> { //right
                    boardRenderer.setPlayer(right[index]);
                    boardFrame.repaint();
                }
                case 1 -> { //left
                    boardRenderer.setPlayer(left[index]);
                    boardFrame.repaint();
                }
                case 2 -> { //down
                    boardRenderer.setPlayer(down[index]);
                    boardFrame.repaint();
                }
                case 3 -> { //up
                    boardRenderer.setPlayer(up[index]);
                    boardFrame.repaint();
                }
            }
            if (index == 3){
                index = 0;
            }else
                index += 1;
            try {
                sleep(50);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}
