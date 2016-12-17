/*
 * Created on 01.08.2003
 */
package bltip.gui;

import bltip.valueobject.User;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dieser Dialog zeigt die Tabelle der Tipper an
 *
 * @author Nico
 * @version 07.10.2004
 */
class UserTableDialog extends JDialog implements ActionListener {

    /**
     * Die Tabellen�berschriften<br>
     * <b>Achtung:</b> Die Spaltenzahl der Tabelle richtet sich nach
     * <code>COLHEADS.length</code>
     */
    public static final Object[] COLHEADS = {"Pl.", "Name", "Tipp", "Tab.", "Gesamt"};
    /**
     * long <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 2342473186574308369L;
    /**
     * Button
     */
    private final JButton ok;

    /**
     * Konstruktor initialisiert das komplette Layout
     *
     * @param mainfr Mainframe, von dem der Dialog aufgerufen wurde
     * @param title  Titel des Dialogs
     * @param user   Tipper-Tabelle
     */
    public UserTableDialog(MainFrame mainfr, String title, User[] user) {
        super(mainfr, title, false);

        // Spaltenmodell erzeugen
        DefaultTableColumnModel cm = new DefaultTableColumnModel();
        for (int i = 0; i < COLHEADS.length; i++) {
            TableColumn col;
            switch (i) {
                case 0:
                    col = new TableColumn(i, 5);
                    break;
                case 1:
                    col = new TableColumn(i, 150);
                    break;
                case 2:
                    col = new TableColumn(i, 10);
                    break;
                case 3:
                    col = new TableColumn(i, 10);
                    break;
                case 4:
                default:
                    col = new TableColumn(i, 20);
                    break;
            }
            col.setHeaderValue(COLHEADS[i]);
            cm.addColumn(col);
        }

        // Tabelle erzeugen
        JTable table = new JTable(new UserTableModel(user), cm);
        table.setBackground(Color.white);
        table.setDefaultRenderer(Object.class, new UserTableCellRenderer());
        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(GUIConstants.WIDTH_SCROLLPANE_USERTABLE,
                GUIConstants.HEIGHT_SCROLLPANE_USERTABLE));

        /*
      Panel f�r die Buttons
     */
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, GUIConstants.FLOWLAYOUT_DEFAULT_GAP,
                GUIConstants.FLOWLAYOUT_DEFAULT_GAP));
        ok = new JButton(GUIConstants.BUTTON_OK);
        ok.setMnemonic(GUIConstants.SHORTCUT_BUTTON_OK);
        ok.setPreferredSize(new Dimension(GUIConstants.WIDTH_BUTTONS, GUIConstants.HEIGHT_BUTTONS));
        ok.addActionListener(this);
        buttonPanel.add(ok, BorderLayout.EAST);

        Container cp = getContentPane();
        cp.add(sp, BorderLayout.NORTH);
        cp.add(buttonPanel, BorderLayout.SOUTH);

        pack();
    }

    /**
     * Spezifizierung der Aktionen
     *
     * @param e Event
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(ok)) {
            dispose();
        } else {
            // Fehlerbehandlung f�r unidentifizierte Quelle.
            dispose();
        }
    }
}
