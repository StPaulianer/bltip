/*
 * Created on 01.08.2003
 */
package bltip.gui;

import bltip.valueobject.Team;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dieser Dialog zeigt die Bundesligatabelle an
 *
 * @author Nico
 * @version 07.10.2004
 */
public class BLTableDialog extends JDialog implements ActionListener {

    /**
     * Die Tabellen�berschriften<br>
     * <b>Achtung:</b> Die Spaltenzahl der Tabelle richtet sich nach
     * <code>COLHEADS.length</code>
     */
    public static final Object[] COLHEADS = {"Pl.", "Verein", "Sp.", "g.", "u.", "v.", "Tore", "Diff.", "Pkte."};
    /**
     * long <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 7957677822647722141L;
    /**
     * Button
     */
    private final JButton ok;

    /**
     * Konstruktor initialisiert das komplette Layout
     *
     * @param mainfr Mainframe, von dem der Dialog aufgerufen wurde
     * @param title  Titel des Dialogs
     * @param teams  BL-Tabelle
     */
    public BLTableDialog(MainFrame mainfr, String title, Team[] teams) {
        super(mainfr, title, false);

        // Spaltenmodell erzeugen
        DefaultTableColumnModel cm = new DefaultTableColumnModel();
        for (int i = 0; i < COLHEADS.length; i++) {
            TableColumn col;
            if (i == 1) {
                // Name
                col = new TableColumn(i, 150);
            } else if (i == 6) {
                // Tore
                col = new TableColumn(i, 20);
            } else {
                col = new TableColumn(i, 1);
            }
            col.setHeaderValue(COLHEADS[i]);
            cm.addColumn(col);
        }

        // Tabelle erzeugen
        /* Die Tabelle */
        JTable table = new JTable(new BLTableModel(teams), cm);
        table.setBackground(Color.white);
        table.setDefaultRenderer(Object.class, new BLTableCellRenderer());
        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(GUIConstants.WIDTH_SCROLLPANE_BLTABLE, GUIConstants.HEIGHT_SCROLLPANE_BLTABLE));

        ok = new JButton(GUIConstants.BUTTON_OK);
        ok.setMnemonic(GUIConstants.SHORTCUT_BUTTON_OK);
        ok.setPreferredSize(new Dimension(GUIConstants.WIDTH_BUTTONS, GUIConstants.HEIGHT_BUTTONS));
        ok.addActionListener(this);

        /* Panel f�r OK-Button und die Legende */
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, GUIConstants.FLOWLAYOUT_DEFAULT_GAP,
                GUIConstants.FLOWLAYOUT_DEFAULT_GAP));
        panel.add(ok);

        Container cp = getContentPane();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
        cp.add(sp);
        cp.add(panel);

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
