package project.frames;

import project.board.BoardFrame;

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

        Image icon = new ImageIcon("src/project/graphics/pacwRight1.png").getImage();
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

        JTextField columns = new JTextField(10);
        columns.setPreferredSize(new Dimension(50, 30));
        columns.setToolTipText("columns");
        inputPanel.add(columns);

        inputPanel.add(Box.createVerticalStrut(10));
        JTextField rows = new JTextField(10);
        rows.setPreferredSize(new Dimension(50, 30));
        rows.setToolTipText("rows");
        rows.setAlignmentX(0.5f);
        inputPanel.add(rows);

        inputPanel.add(Box.createVerticalStrut(20));
        readyButton(inputPanel, columns, rows);
        jPanel.add(inputPanel);

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

        setFocusable(true);
        add(jPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void readyButton(JPanel inputPanel, JTextField columns, JTextField rows){
        JButton ready = new JButton("Done");
        ready.setAlignmentX(0.5f);
        ready.setBackground(Color.BLACK);
        ready.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 20));
        ready.setForeground(Color.GREEN);

        ready.addActionListener(e -> {
            String columnsText = columns.getText();
            String rowsText = rows.getText();
            boolean isValidKol = columnsText.matches("\\d+");
            boolean isValidWie = rowsText.matches("\\d+");
            int columnsInt, rowsInt;

            if (ready.equals(e.getSource())){
                if (!isValidKol && !isValidWie){
                    JOptionPane.showMessageDialog(null, "The entered board dimensions are not integers.");
                }
                columnsInt = Integer.parseInt(columnsText);
                rowsInt = Integer.parseInt(rowsText);

                if ((columnsInt >= 10 && columnsInt <= 100) && (rowsInt >= 10 && rowsInt <= 100)) {
                    SwingUtilities.invokeLater(() -> new BoardFrame(columnsInt, rowsInt));
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "The dimentions of the board are incorrect. \nPlease enter numbers between 10 and 100 inclusive.");
                }
            }
        });

        inputPanel.add(ready);
    }
}
