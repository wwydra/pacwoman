package project.frames;

import project.scores.HighScores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu
extends JFrame implements ActionListener {

    private JButton startButton;
    private JButton hsButton;
    private JButton exitButton;

    public Menu () {

        setTitle("PacWoman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        Image icon = new ImageIcon("src/project/graphics/pacwRight1.png").getImage();
        setIconImage(icon);

        JLabel jLabel = new JLabel("PacWoman");
        jLabel.setBackground(Color.BLACK);
        jLabel.setForeground(Color.YELLOW);
        jLabel.setFont(new Font("Impact", Font.BOLD, 80));
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setOpaque(true);

        add(jLabel, BorderLayout.NORTH);

        JPanel buttonPanel = initButtons();

        add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.setBackground(Color.BLACK);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JPanel initButtons(){

        JPanel buttonPanel = new JPanel();
        buttonPanel.setMinimumSize(new Dimension(300, 300));
        buttonPanel.setPreferredSize(new Dimension(400, 400));
        buttonPanel.setMaximumSize(new Dimension(500, 500));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        ImageIcon icon = new ImageIcon("src/project/graphics/pacwRight1.png");
        JLabel label = new JLabel(icon);
        buttonPanel.add(Box.createVerticalStrut(100));
        label.setAlignmentX(0.5f);
        buttonPanel.add(label);
        buttonPanel.add(Box.createVerticalStrut(10));

        startButton = new JButton("Start Game");
        hsButton = new JButton("High Scores");
        exitButton = new JButton("Exit");

        buttonPanel.add(startButton);
        startButton.setAlignmentX(0.5f);
        startButton.setBackground(Color.BLACK);
        startButton.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));
        startButton.setForeground(Color.GREEN);
        startButton.addActionListener(this);

        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(hsButton);
        hsButton.setAlignmentX(0.5f);
        hsButton.setBackground(Color.BLACK);
        hsButton.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));
        hsButton.setForeground(Color.CYAN);
        hsButton.addActionListener(this);

        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(exitButton);
        exitButton.setAlignmentX(0.5f);
        exitButton.setBackground(Color.BLACK);
        exitButton.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));
        exitButton.setForeground(Color.MAGENTA);
        exitButton.addActionListener(this);

        buttonPanel.add(Box.createHorizontalStrut(100));

        return buttonPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (startButton.equals(e.getSource())){
            SwingUtilities.invokeLater(PreGame::new);
            dispose();
        } else if (hsButton.equals(e.getSource())){
            SwingUtilities.invokeLater(HighScores::new);
            dispose();
        }else if (exitButton.equals(e.getSource())){
            dispose();
            System.exit(0);
        }
    }
}
