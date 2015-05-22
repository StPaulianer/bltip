/*
 * Created on 07.10.2004
 */
package bltip.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Ein Renderer, um die Tipptabelle zu zeichnen. Damit k�nnen z.B. die Geld-Gewinner (und
 * Absteiger...) in verschiedenen Farben dargestellt werden. Au�erdem kann das Alignment
 * verfeinert werden.
 *
 * @author Nico
 * @version 27.09.2005 TODO es gibt viel f�r den GC zu tun, da bei Aufruf von getTableCell...
 *          immer ein neues Label gebraucht wird TODO die Farbe f�r den letzen Platz ist hart
 *          reingecodet.
 */
class UserTableCellRenderer implements TableCellRenderer {

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
     * java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                   int column) {

        JLabel label = new JLabel(value != null ? value.toString() : "");
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        label.setFont(table.getFont());
        label.setForeground(table.getForeground());
        label.setBackground(table.getBackground());

        // Angezeigte Spalte in Modellspalte umwandeln (falls Spelten getauscht wurden)
        // column = table.convertColumnIndexToModel(column);
        if (column != 1) {
            label.setHorizontalAlignment(JLabel.RIGHT);
        }
        if (isSelected) {
            label.setBackground(Color.black);
            label.setForeground(Color.white);
        } else {
            switch (row) {
                case 0:
                    label.setBackground(GUIConstants.COLOR_TIPCHAMPION);
                    break;
                case 1:
                    label.setBackground(GUIConstants.COLOR_TIPSECOND);
                    break;
                case 2:
                    label.setBackground(GUIConstants.COLOR_TIPTHIRD);
                    break;
                case 3:
                    label.setBackground(GUIConstants.COLOR_TIPFOURTH);
                    break;
                case 12:
                    label.setBackground(GUIConstants.COLOR_TIPLOSER);
                    break;
            }
        }

        return label;
    }
}
