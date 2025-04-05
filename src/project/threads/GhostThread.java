package project.threads;

import project.board.BoardFrame;
import project.board.BoardModel;
import project.board.BoardRenderer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GhostThread
    extends Thread {

    private final BoardModel boardModel;
    private final BoardFrame boardFrame;
    private final BoardRenderer boardRenderer;
    private boolean stopped;
    private final ImageIcon ghost;
    private final ImageIcon scaredGhost;
    private int x, y;
    private boolean[][] taken;

    public GhostThread(BoardModel boardModel, BoardFrame boardFrame, BoardRenderer boardRenderer, int x, int y) {
        this.boardModel = boardModel;
        this.boardFrame = boardFrame;
        this.boardRenderer = boardRenderer;
        this.stopped = false;
        this.x = x;
        this.y = y;
        this.ghost = new ImageIcon("src/project/graphics/ghost.png");
        this.scaredGhost = new ImageIcon("src/project/graphics/scaredGhost.png");
        this.taken = new boolean[boardModel.getWidth()][boardModel.getHeight()];
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
        while (!Thread.interrupted() && boardFrame.isRunning()) {
            ArrayList<int[]> path = followPlayer(x, y);
            if (path.size() > 1) {
                int[] nextMove = path.get(1);
                int[] check;
                if (path.size() > 2)
                    check = path.get(2);
                else
                    check = path.get(1);
                synchronized (boardModel) {
                    while (!boardModel.isMovableGhost(check[0], check[1]) || !boardModel.isMovableGhost(nextMove[0], nextMove[1])) {
                        try {
                            boardModel.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    boardModel.ghostsMovement(x, y, nextMove[0], nextMove[1]);
                    boardFrame.repaint();
                    x = nextMove[0];
                    y = nextMove[1];
                    boardModel.notifyAll();
                }
            }
            try {
                Thread.sleep(330);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            while (stopped) {
                boardRenderer.setGhost(scaredGhost);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.stopped = false;
                boardRenderer.setGhost(ghost);
            }
        }
    }

    public synchronized ArrayList<int[]> followPlayer(int x, int y) {
        int playerX = boardModel.getPlayerX();
        int playerY = boardModel.getPlayerY();

        boolean[][] visited = new boolean[boardModel.getWidth()][boardModel.getHeight()];
        int[][] parentX = new int[boardModel.getWidth()][boardModel.getHeight()];
        int[][] parentY = new int[boardModel.getWidth()][boardModel.getHeight()];

        for (int i = 0; i < boardModel.getWidth(); i++){
            for (int j = 0; j < boardModel.getHeight(); j++) {
                visited[i][j] = false;
            }
        }

        Queue<int[]> queue = new LinkedList<>();
        int[] start = {x, y};
        visited[x][y] = true;
        queue.add(start);

        while (!queue.isEmpty()){
            int[] curr = queue.poll();

            ArrayList<int[]> neighbours = boardModel.getNeighbours(curr[0], curr[1]);
            for (int[] neighbour : neighbours) {
                if (!visited[neighbour[0]][neighbour[1]]) {
                    visited[neighbour[0]][neighbour[1]] = true;
                    parentX[neighbour[0]][neighbour[1]] = curr[0];
                    parentY[neighbour[0]][neighbour[1]] = curr[1];
                    queue.add(neighbour);
                }
            }
        }

        ArrayList<int[]> path = new ArrayList<>();
        int[] point = {playerX, playerY};
        path.add(point);

        while (point[0] != x || point[1] != y) {
            int[] parent = {parentX[point[0]][point[1]], parentY[point[0]][point[1]]};
            path.add(0, parent);
            point = parent;
        }

        return path;
    }

    public synchronized void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}

