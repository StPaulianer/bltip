/*
 * Created on 05.10.2004
 */
package bltip.gui;

import javax.swing.table.AbstractTableModel;

import bltip.valueobject.User;

/**
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 30.07.2005
 */
public class UserTableModel extends AbstractTableModel {

    /** long <code>serialVersionUID</code>. */
    private static final long serialVersionUID = -2462369361828703146L;
    /** Die Tabelle der Tipper */
    private final User[] user;

    /**
     * Konstruktor
     * 
     * @param user Die Tabelle der User
     */
    public UserTableModel(User[] user) {
        this.user = user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return this.user.length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return UserTableDialog.COLHEADS.length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int column) {
        switch (column) {
        case 0:
            return row + 1;
        case 1:
            return this.user[row].getName();
        case 2:
            return this.user[row].getTipscore();
        case 3:
            return this.user[row].getTablescore();
        case 4:
            return this.user[row].getScore();
        }

        return null;
    }
}
