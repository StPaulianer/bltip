/*
 * Created on 07.10.2004
 */
package bltip.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Ein Renderer, um die Bundesligatabelle zu zeichnen. Damit k�nnen z.B. die Absteiger und
 * UEFA-Cup-Teilnehmer in verschiedenen Farben dargestellt werden. Au�erdem kann das Alignment
 * verfeinert werden.
 *
 * @author Nico
 * @version 07.10.2004
 */
class BLTableCellRenderer implements TableCellRenderer {

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

        // Angezeigte Spalte in Modellspalte umwandeln (falls Spalten getauscht wurden)
        int col = column;
        col = table.convertColumnIndexToModel(col);
        if (col == 1) {
            label.setHorizontalAlignment(JLabel.LEFT);
        } else if (col == 6) {
            label.setHorizontalAlignment(JLabel.CENTER);
        } else {
            label.setHorizontalAlignment(JLabel.RIGHT);
        }
        if (isSelected) {
            label.setBackground(Color.black);
            label.setForeground(Color.white);
        } else {
            switch (row) {
                case 0:
                    label.setBackground(GUIConstants.COLOR_CHAMPION);
                    break;
                case 1:
                    label.setBackground(GUIConstants.COLOR_CL);
                    break;
                case 2:
                    label.setBackground(GUIConstants.COLOR_CL);
                    break;
                case 3:
                    label.setBackground(GUIConstants.COLOR_CL_QUALIFY);
                    break;
                case 4:
                case 5:
                    label.setBackground(GUIConstants.COLOR_UEFACUP);
                    break;
                case 15:
                case 16:
                case 17:
                    label.setBackground(GUIConstants.COLOR_DESCENDER);
                    break;
            }
        }

        return label;
    }
}
