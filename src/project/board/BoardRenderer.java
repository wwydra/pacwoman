package project.board;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class BoardRenderer
    implements TableCellRenderer {

    private final JLabel jLabel;
    private final ImageIcon wall;
    private ImageIcon player;
    private ImageIcon ghost;
    private final ImageIcon score;
    private final ImageIcon upgrade;
    private BoardFrame boardFrame;

    public BoardRenderer(BoardFrame boardFrame){
        this.jLabel = new JLabel();
        this.boardFrame = boardFrame;
        jLabel.setOpaque(true);
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setVerticalAlignment(JLabel.CENTER);

        this.wall = new ImageIcon("src/project/graphics/wall.png");
        this.player = new ImageIcon("src/project/graphics/pacwRight1.png");
        this.ghost = new ImageIcon("src/project/graphics/ghost.png");
        this.score = new ImageIcon("src/project/graphics/score.png");
        this.upgrade = new ImageIcon("src/project/graphics/upgrade.png");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        jLabel.setBackground(Color.BLACK);
        jLabel.setForeground(Color.BLACK);
        int widthCell = table.getColumnModel().getColumn(column).getWidth();
        int heightCell = table.getRowHeight(row);
        int iconWidth, iconHeight;

        if (value instanceof Integer && ((int)value == 1)){
            jLabel.setBackground(Color.GREEN);
            jLabel.setForeground(Color.GREEN);
            setLabelContent(widthCell, heightCell, wall);
        }else if (value instanceof Integer && ((int)value == 2)){
            setLabelContent(widthCell, heightCell, player);
        }else if (value instanceof Integer && ((int)value == 3 || (int)value == 8 || (int)value == 28 || (int)value == 31 ||
                (int)value == 23 || (int)value == 26 || (int)value == 7 || (int)value == 16 || (int)value == 17 ||
                (int)value == 9 || (int)value == 21 || (int)value == 11 || (int)value == 10 || (int)value == 14 ||
                (int)value == 18 || (int)value == 19 || (int)value == 6 || (int)value == 33 || (int)value == 36)) {
            setLabelContent(widthCell, heightCell, ghost);
        }else if (value instanceof Integer && ((int)value == 0)){
            setLabelContent(widthCell, heightCell, score);
        }else if (value instanceof Integer && ((int)value == 4 || (int)value == 25 || (int)value == 20 || (int)value == 15 || (int)value == 30)){
            setLabelContent(widthCell, heightCell, upgrade);
        }else{
            jLabel.setIcon(null);
        }

        return jLabel;
    }

    private void setLabelContent(int widthCell, int heightCell, ImageIcon wall) {
        int iconWidth;
        int iconHeight;
        iconWidth = wall.getIconWidth();
        iconHeight = wall.getIconHeight();
        if (iconWidth > widthCell || iconHeight > heightCell) {
            Image img = wall.getImage().getScaledInstance(widthCell, heightCell, Image.SCALE_SMOOTH);
            ImageIcon newIcon = new ImageIcon(img);
            jLabel.setIcon(newIcon);
        } else {
            jLabel.setIcon(wall);
        }
    }

    public synchronized void setGhost(ImageIcon ghost) {
        this.ghost = ghost;
    }

    public synchronized void setPlayer(ImageIcon player) {
        this.player = player;
    }
}
