package Projekt_2.Okna;

import Projekt_2.Plansza.PlanszaFrame;
import Projekt_2.Watki.GraczThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PreGame
    extends JFrame {

    public PreGame(){
        setTitle("PacWoman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setBackground(Color.BLACK);
        setResizable(false);

        Image icon = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwPrawo1.png").getImage();
        setIconImage(icon);

        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.BLACK);
        jPanel.setPreferredSize(new Dimension(400, 400));

        JLabel jLabel = new JLabel("Enter the dimensions of the board:");
        jLabel.setAlignmentX(0.5f);
        jLabel.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));
        jLabel.setForeground(Color.CYAN);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jPanel.add(Box.createVerticalStrut(150));
        jPanel.add(jLabel);
        jPanel.add(Box.createVerticalStrut(20));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setPreferredSize(new Dimension(200, 150));
        inputPanel.setBackground(Color.BLACK);

        JTextField kolumny = new JTextField(10);
        kolumny.setPreferredSize(new Dimension(50, 30));
        kolumny.setToolTipText("columns");
        inputPanel.add(kolumny);

        inputPanel.add(Box.createVerticalStrut(10));
        JTextField wiersze = new JTextField(10);
        wiersze.setPreferredSize(new Dimension(50, 30));
        wiersze.setToolTipText("rows");
        wiersze.setAlignmentX(0.5f);
        inputPanel.add(wiersze);

        inputPanel.add(Box.createVerticalStrut(20));
        gotoweButton(inputPanel, kolumny, wiersze);
        jPanel.add(inputPanel);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Q){
                    dispose();
                    SwingUtilities.invokeLater(() -> new Menu());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        setFocusable(true);
        add(jPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void gotoweButton(JPanel inputPanel, JTextField kolumny, JTextField wiersze){
        JButton gotowe = new JButton("Done");
        gotowe.setAlignmentX(0.5f);
        gotowe.setBackground(Color.BLACK);
        gotowe.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));
        gotowe.setForeground(Color.GREEN);

        gotowe.addActionListener(e -> {
            String kolumnyText = kolumny.getText();
            String wierszeText = wiersze.getText();
            boolean isValidKol = kolumnyText.matches("\\d+");
            boolean isValidWie = wierszeText.matches("\\d+");
            int kolumnyInt, wierszeInt;

            if (gotowe.equals(e.getSource())){
                if (!isValidKol && !isValidWie){
                    JOptionPane.showMessageDialog(null, "The entered board dimensions are not integers.");
                }
                kolumnyInt = Integer.parseInt(kolumnyText);
                wierszeInt = Integer.parseInt(wierszeText);

                if ((kolumnyInt >= 10 && kolumnyInt <= 100) && (wierszeInt >= 10 && wierszeInt <= 100)) {
                    SwingUtilities.invokeLater(() -> new PlanszaFrame(kolumnyInt, wierszeInt));
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "The dimentions of the board are incorrect. \nPlease enter numbers between 10 and 100 inclusive.");
                }
            }
        });

        inputPanel.add(gotowe);
    }
}
