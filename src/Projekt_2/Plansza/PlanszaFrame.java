package Projekt_2.Plansza;

import Projekt_2.Okna.Menu;
import Projekt_2.Watki.*;
import Projekt_2.Wyniki.Ranking;
import Projekt_2.Wyniki.Wynik;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlanszaFrame
        extends JFrame implements KeyListener {

    private final GraczThread graczThread;
    private boolean running = false;
    private JLabel startGameLabel;
    private JPanel bottom;
    private JLabel lives;
    private JPanel top;

    public PlanszaFrame(int sizeX, int sizeY){
        setTitle("PacWoman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setSize(sizeX*20, sizeY*20);
        setLayout(new BorderLayout());

        Image icon = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwPrawo1.png").getImage();
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

        PlanszaModel model = new PlanszaModel(sizeX, sizeY, score, lives, this);
        this.graczThread = new GraczThread(model, this);

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

        PlanszaRenderer renderer = new PlanszaRenderer(this);
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(renderer);
        }
        center.add(jTable, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                dostosuj(jTable, center);
            }
        });

        AnimacjaThread animacjaThread = new AnimacjaThread(renderer, this.graczThread, this);

        add(center, BorderLayout.CENTER);

        setFocusable(true);
        addKeyListener(this);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        this.graczThread.start();
        animacjaThread.start();
        int[][] xy = model.getXyDuchy();
        DuchThread[] duchThreads = new DuchThread[model.getLiczbaDuchow()];
        for (int i = 0; i < model.getLiczbaDuchow(); i++){
            DuchThread duchThread = new DuchThread(model,this, renderer, xy[i][0], xy[i][1]);
            duchThreads[i] = duchThread;
            duchThread.start();
        }
        model.setDuchThreads(duchThreads);
        model.setGraczThread(graczThread);

        UlepszeniaThread ulepszeniaThread = new UlepszeniaThread(this, model);
        ulepszeniaThread.start();

        TimeThread timeThread = new TimeThread(time, this);
        timeThread.start();
        RepaintThread repaintThread = new RepaintThread(this);
        repaintThread.start();
    }

    private void dostosuj(JTable jTable, JPanel jPanel) {
        int wysokoscPanel = jPanel.getHeight();
        int liczbaWierszy = jTable.getRowCount();
        int szerokoscPanel = jPanel.getWidth();
        int liczbaKolumn = jTable.getColumnCount();

        if (liczbaWierszy > 0){
            int wysokosc = wysokoscPanel / liczbaWierszy;
            int res = wysokoscPanel % liczbaWierszy;
            for (int i = 0; i < liczbaWierszy; i++){
                int docelowa = wysokosc;
                if (i == liczbaWierszy - 1)
                    docelowa += res;
                jTable.setRowHeight(i, docelowa);
            }
        }

        int kolumnySumaSzer = 0;
        for (int i = 0; i < liczbaKolumn; i++) {
            kolumnySumaSzer += jTable.getColumnModel().getColumn(i).getWidth();
        }

        if (kolumnySumaSzer != szerokoscPanel) {
            double wspolczynnik = (double) szerokoscPanel / (double) kolumnySumaSzer;
            for (int i = 0; i < liczbaKolumn; i++) {
                int aktualnaSzerokosc = jTable.getColumnModel().getColumn(i).getWidth();
                jTable.getColumnModel().getColumn(i).setPreferredWidth((int)(aktualnaSzerokosc * wspolczynnik));
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Q){
            dispose();
            SwingUtilities.invokeLater(() -> new Menu());
        }
        switch (e.getKeyCode()){
            case KeyEvent.VK_W -> this.graczThread.setKierunek(3);
            case KeyEvent.VK_A -> this.graczThread.setKierunek(1);
            case KeyEvent.VK_S -> this.graczThread.setKierunek(2);
            case KeyEvent.VK_D -> this.graczThread.setKierunek(0);
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
        gameOver.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 30));
        gameOver.setForeground(Color.RED);
        gameOver.setOpaque(true);
        gameOver.setBackground(Color.BLACK);
        gameOver.setHorizontalAlignment(SwingConstants.CENTER);
        gameOver.setVerticalAlignment(SwingConstants.CENTER);
        gameOver.setPreferredSize(new Dimension(50, 50));
        this.top.add(gameOver, BorderLayout.CENTER);

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

    public synchronized void gameWon(int score){
        this.running = false;
        JLabel gameWon = new JLabel("YOU WON!");
        gameWon.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 30));
        gameWon.setForeground(Color.RED);
        gameWon.setOpaque(true);
        gameWon.setBackground(Color.BLACK);
        gameWon.setHorizontalAlignment(SwingConstants.CENTER);
        gameWon.setVerticalAlignment(SwingConstants.CENTER);
        gameWon.setPreferredSize(new Dimension(50, 50));
        this.top.add(gameWon, BorderLayout.CENTER);

        JLabel returnLabel = new JLabel("Hold Ctrl+Shift+Q to go back");
        returnLabel.setOpaque(true);
        returnLabel.setBackground(Color.BLACK);
        returnLabel.setForeground(Color.RED);
        returnLabel.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));
        returnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.bottom.remove(this.lives);
        this.bottom.add(returnLabel, BorderLayout.CENTER);
        pack();

        JTextField textField = new JTextField();
        Object[] options = {"OK"};
        int result = JOptionPane.showOptionDialog(null, textField, "Enter your nickname:", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);

        if (result == JOptionPane.OK_OPTION) {
            String nickname = textField.getText();
            Wynik wynik = new Wynik(nickname, score);
            Ranking ranking = new Ranking();
            ranking.dodajWynik(wynik);
        }

    }
}

