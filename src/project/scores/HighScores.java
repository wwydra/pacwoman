package project.scores;

import project.frames.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class HighScores
    extends JFrame{

    private final JList<Score> ranking;

    public HighScores(){
        setTitle("PACwoMAN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setBackground(Color.BLACK);
        setResizable(false);
        setLayout(new BorderLayout());

        Image icon = new ImageIcon("src/project/graphics/pacwRight1.png").getImage();
        setIconImage(icon);

        JLabel jLabel = new JLabel("High Scores");
        jLabel.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 30));
        jLabel.setForeground(Color.GREEN);
        jLabel.setOpaque(true);
        jLabel.setBackground(Color.BLACK);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(jLabel, BorderLayout.NORTH);

        this.ranking = new JList<>();
        this.ranking.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.ranking.setVisibleRowCount(50);
        this.ranking.setFont(new Font("Monospaced", Font.ITALIC, 25));
        this.ranking.setBackground(Color.BLACK);
        this.ranking.setForeground(Color.CYAN);
        displayScores();

        JScrollPane jScrollPane = new JScrollPane(this.ranking);
        jScrollPane.setBackground(Color.BLACK);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setPreferredSize(new Dimension(600, 600));
        add(jScrollPane, BorderLayout.CENTER);

        JPanel back = new JPanel();
        back.setBackground(Color.BLACK);
        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(0.5f);
        backButton.setBackground(Color.BLACK);
        backButton.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));
        backButton.setForeground(Color.MAGENTA);
        backButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(Menu::new);
        });
        back.add(backButton);
        add(back, BorderLayout.SOUTH);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Q){
                    dispose();
                    SwingUtilities.invokeLater(Menu::new);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        repaint();
        pack();
        setFocusable(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void displayScores(){
        Ranking rank = new Ranking();
        List<Score> scores = rank.getScores();

        DefaultListModel<Score> model = new DefaultListModel<>();
        for (Score score : scores){
            model.addElement(score);
        }
        ranking.setModel(model);
    }
}
