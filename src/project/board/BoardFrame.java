package project.board;

import project.frames.Menu;
import project.threads.*;
import project.scores.Ranking;
import project.scores.Score;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BoardFrame
        extends JFrame implements KeyListener {

    private final PlayerThread playerThread;
    private boolean running = false;
    private final JLabel startGameLabel;
    private final JPanel bottom;
    private final JLabel lives;
    private final JPanel top;

    public BoardFrame(int sizeX, int sizeY){
        setTitle("PacWoman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setSize(sizeX*20, sizeY*20);
        setLayout(new BorderLayout());

        Image icon = new ImageIcon("src/project/graphics/pacwRight1.png").getImage();
        setIconImage(icon);

        this.top = new JPanel();
        top.setBackground(Color.BLACK);
        top.setLayout(new BorderLayout());

        this.startGameLabel = new JLabel("Press SPACE to start!");
        startGameLabel.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 25));
        startGameLabel.setForeground(Color.RED);
        startGameLabel.setOpaque(true);
        startGameLabel.setBackground(Color.BLACK);
        startGameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        startGameLabel.setVerticalAlignment(SwingConstants.CENTER);
        top.add(startGameLabel, BorderLayout.CENTER);

        JLabel score = new JLabel("Score: 0");
        score.setOpaque(true);
        score.setBackground(Color.BLACK);
        score.setForeground(Color.WHITE);
        score.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));
        score.setHorizontalAlignment(SwingConstants.CENTER);
        top.add(score, BorderLayout.WEST);

        JLabel time = new JLabel("Time: 00:00");
        time.setOpaque(true);
        time.setBackground(Color.BLACK);
        time.setForeground(Color.WHITE);
        time.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));
        time.setHorizontalAlignment(SwingConstants.CENTER);
        top.add(time, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        this.bottom = new JPanel();
        bottom.setBackground(Color.BLACK);
        bottom.setLayout(new BorderLayout());

        this.lives = new JLabel("Lives: 3");
        lives.setOpaque(true);
        lives.setBackground(Color.BLACK);
        lives.setForeground(Color.WHITE);
        lives.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));
        lives.setHorizontalAlignment(SwingConstants.CENTER);
        bottom.add(lives, BorderLayout.CENTER);

        add(bottom, BorderLayout.SOUTH);

        BoardModel model = new BoardModel(sizeX, sizeY, score, lives, this);
        this.playerThread = new PlayerThread(model, this);

        JPanel center = new JPanel();
        center.setBackground(Color.BLACK);

        JTable jTable = new JTable(model);
        jTable.setGridColor(Color.BLACK);
        jTable.setRowSelectionAllowed(false);
        jTable.setCellSelectionEnabled(false);

        ImageIcon tmp = new ImageIcon(icon);
        int iconWidth = tmp.getIconWidth();
        int iconHeight = tmp.getIconHeight();
        TableColumnModel columnModel = jTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(iconWidth);
        }
        for (int i = 0; i < jTable.getRowCount(); i++){
            jTable.setRowHeight(i, iconHeight);
        }

        BoardRenderer renderer = new BoardRenderer(this);
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(renderer);
        }
        center.add(jTable, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjust(jTable, center);
            }
        });

        Animation animation = new Animation(renderer, this.playerThread, this);

        add(center, BorderLayout.CENTER);

        setFocusable(true);
        addKeyListener(this);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        this.playerThread.start();
        animation.start();
        int[][] xy = model.getXyGhosts();
        GhostThread[] ghostThreads = new GhostThread[model.getGhostCount()];
        for (int i = 0; i < model.getGhostCount(); i++){
            GhostThread ghostThread = new GhostThread(model,this, renderer, xy[i][0], xy[i][1]);
            ghostThreads[i] = ghostThread;
            ghostThread.start();
        }
        model.setGhostThreads(ghostThreads);
        model.setPlayerThread(playerThread);

        UpgradesThread upgradesThread = new UpgradesThread(this, model);
        upgradesThread.start();

        TimeThread timeThread = new TimeThread(time, this);
        timeThread.start();
        RepaintThread repaintThread = new RepaintThread(this);
        repaintThread.start();
    }

    private void adjust(JTable jTable, JPanel jPanel) {
        int panelHeight = jPanel.getHeight();
        int rowCount = jTable.getRowCount();
        int panelWidth = jPanel.getWidth();
        int columnCount = jTable.getColumnCount();

        if (rowCount > 0){
            int height = panelHeight / rowCount;
            int res = panelHeight % rowCount;
            for (int i = 0; i < rowCount; i++){
                int target = height;
                if (i == rowCount - 1)
                    target += res;
                jTable.setRowHeight(i, target);
            }
        }

        int columnSum = 0;
        for (int i = 0; i < columnCount; i++) {
            columnSum += jTable.getColumnModel().getColumn(i).getWidth();
        }

        if (columnSum != panelWidth) {
            double factor = (double) panelWidth / (double) columnSum;
            for (int i = 0; i < columnCount; i++) {
                int currentWidth = jTable.getColumnModel().getColumn(i).getWidth();
                jTable.getColumnModel().getColumn(i).setPreferredWidth((int)(currentWidth * factor));
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Q){
            dispose();
            SwingUtilities.invokeLater(Menu::new);
        }
        switch (e.getKeyCode()){
            case KeyEvent.VK_W -> this.playerThread.setDirection(3);
            case KeyEvent.VK_A -> this.playerThread.setDirection(1);
            case KeyEvent.VK_S -> this.playerThread.setDirection(2);
            case KeyEvent.VK_D -> this.playerThread.setDirection(0);
            case KeyEvent.VK_SPACE -> {
                this.running = true;
                this.startGameLabel.setVisible(false);
            }
            default -> {}
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void stopRunning() {
        this.running = false;
        JLabel gameOver = new JLabel("GAME OVER!");
        setLabelContent(gameOver);
    }

    public synchronized void gameWon(int score){
        this.running = false;
        JLabel gameWon = new JLabel("YOU WON!");
        setLabelContent(gameWon);

        JTextField textField = new JTextField();
        Object[] options = {"OK"};
        int result = JOptionPane.showOptionDialog(null, textField, "Enter your nickname:", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);

        if (result == JOptionPane.OK_OPTION) {
            String nickname = textField.getText();
            Score playerScore = new Score(nickname, score);
            Ranking ranking = new Ranking();
            ranking.addScore(playerScore);
        }

    }

    private void setLabelContent(JLabel gameStatus) {
        gameStatus.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 30));
        gameStatus.setForeground(Color.RED);
        gameStatus.setOpaque(true);
        gameStatus.setBackground(Color.BLACK);
        gameStatus.setHorizontalAlignment(SwingConstants.CENTER);
        gameStatus.setVerticalAlignment(SwingConstants.CENTER);
        gameStatus.setPreferredSize(new Dimension(50, 50));
        this.top.add(gameStatus, BorderLayout.CENTER);

        JLabel returnLabel = new JLabel("Hold Ctrl+Shift+Q to go back");
        returnLabel.setOpaque(true);
        returnLabel.setBackground(Color.BLACK);
        returnLabel.setForeground(Color.RED);
        returnLabel.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));
        returnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.bottom.remove(this.lives);
        this.bottom.add(returnLabel, BorderLayout.CENTER);
        pack();
    }
}

