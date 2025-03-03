package Projekt_2.Plansza;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class PlanszaRenderer
    implements TableCellRenderer {

    private JLabel jLabel;
    private ImageIcon sciana;
    private ImageIcon gracz;
    private ImageIcon duch;
    private ImageIcon score;
    private ImageIcon ulepszenie;
    private PlanszaFrame planszaFrame;

    public PlanszaRenderer(PlanszaFrame planszaFrame){
        this.jLabel = new JLabel();
        this.planszaFrame = planszaFrame;
        jLabel.setOpaque(true);
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setVerticalAlignment(JLabel.CENTER);

        this.sciana = new ImageIcon("src/Projekt_2/PlikiGraficzne/sciana.png");
        this.gracz = new ImageIcon("src/Projekt_2/PlikiGraficzne/pacwPrawo1.png");
        this.duch = new ImageIcon("src/Projekt_2/PlikiGraficzne/duch.png");
        this.score = new ImageIcon("src/Projekt_2/PlikiGraficzne/score.png");
        this.ulepszenie = new ImageIcon("src/Projekt_2/PlikiGraficzne/ulepszenie.png");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        jLabel.setBackground(Color.BLACK);
        jLabel.setForeground(Color.BLACK);
        int szerokoscKomorka = table.getColumnModel().getColumn(column).getWidth();
        int wysokoscKomorka = table.getRowHeight(row);
        int iconWidth, iconHeight;

        if (value instanceof Integer && ((int)value == 1)){
            jLabel.setBackground(Color.GREEN);
            jLabel.setForeground(Color.GREEN);
            iconWidth = sciana.getIconWidth();
            iconHeight = sciana.getIconHeight();
            if (iconWidth > szerokoscKomorka || iconHeight > wysokoscKomorka) {
                Image img = sciana.getImage().getScaledInstance(szerokoscKomorka, wysokoscKomorka, Image.SCALE_SMOOTH);
                ImageIcon newIcon = new ImageIcon(img);
                jLabel.setIcon(newIcon);
            } else {
                jLabel.setIcon(sciana);
            }
        }else if (value instanceof Integer && ((int)value == 2)){
            iconWidth = gracz.getIconWidth();
            iconHeight = gracz.getIconHeight();
            if (iconWidth > szerokoscKomorka || iconHeight > wysokoscKomorka) {
                Image img = gracz.getImage().getScaledInstance(szerokoscKomorka, wysokoscKomorka, Image.SCALE_SMOOTH);
                ImageIcon newIcon = new ImageIcon(img);
                jLabel.setIcon(newIcon);
            } else {
                jLabel.setIcon(gracz);
            }
        }else if (value instanceof Integer && ((int)value == 3 || (int)value == 8 || (int)value == 28 || (int)value == 31 ||
                (int)value == 23 || (int)value == 26 || (int)value == 7 || (int)value == 16 || (int)value == 17 ||
                (int)value == 9 || (int)value == 21 || (int)value == 11 || (int)value == 10 || (int)value == 14 ||
                (int)value == 18 || (int)value == 19 || (int)value == 6 || (int)value == 33 || (int)value == 36)) {
            iconWidth = duch.getIconWidth();
            iconHeight = duch.getIconHeight();
            if (iconWidth > szerokoscKomorka || iconHeight > wysokoscKomorka) {
                Image img = duch.getImage().getScaledInstance(szerokoscKomorka, wysokoscKomorka, Image.SCALE_SMOOTH);
                ImageIcon newIcon = new ImageIcon(img);
                jLabel.setIcon(newIcon);
            } else {
                jLabel.setIcon(duch);
            }
        }else if (value instanceof Integer && ((int)value == 0)){
            iconWidth = score.getIconWidth();
            iconHeight = score.getIconHeight();
            if (iconWidth > szerokoscKomorka || iconHeight > wysokoscKomorka) {
                Image img = score.getImage().getScaledInstance(szerokoscKomorka, wysokoscKomorka, Image.SCALE_SMOOTH);
                ImageIcon newIcon = new ImageIcon(img);
                jLabel.setIcon(newIcon);
            } else {
                jLabel.setIcon(score);
            }
        }else if (value instanceof Integer && ((int)value == 4 || (int)value == 25 || (int)value == 20 || (int)value == 15 || (int)value == 30)){
            iconWidth = ulepszenie.getIconWidth();
            iconHeight = ulepszenie.getIconHeight();
            if (iconWidth > szerokoscKomorka || iconHeight > wysokoscKomorka) {
                Image img = ulepszenie.getImage().getScaledInstance(szerokoscKomorka, wysokoscKomorka, Image.SCALE_SMOOTH);
                ImageIcon newIcon = new ImageIcon(img);
                jLabel.setIcon(newIcon);
            } else {
                jLabel.setIcon(ulepszenie);
            }
        }else{
            jLabel.setIcon(null);
        }

        return jLabel;
    }

    public synchronized void setDuch(ImageIcon duch) {
        this.duch = duch;
    }

    public synchronized void setGracz(ImageIcon gracz) {
        this.gracz = gracz;
    }
}
