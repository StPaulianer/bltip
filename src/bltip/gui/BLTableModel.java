/*
 * Created on 05.10.2004
 */
package bltip.gui;

import bltip.valueobject.Team;

import javax.swing.table.AbstractTableModel;

/**
 * @author Nico
 * @version 30.07.2005
 */
class BLTableModel extends AbstractTableModel {

    /**
     * long <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = -5756411297113125758L;
    /**
     * Die Bundesligatabelle
     */
    private final Team[] teams;

    /**
     * Kostruktor
     *
     * @param teams Die BL-Tabelle
     */
    public BLTableModel(Team[] teams) {
        this.teams = teams;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return this.teams.length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return BLTableDialog.COLHEADS.length;
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
                return this.teams[row].getName();
            case 2:
                return this.teams[row].getCountOfGames();
            case 3:
                return this.teams[row].getWins();
            case 4:
                return this.teams[row].getRemis();
            case 5:
                return this.teams[row].getLoses();
            case 6:
                return this.teams[row].getGoals() + GUIConstants.RESULT_SEPARATOR + this.teams[row].getGoalsAgainst();
            case 7:
                return this.teams[row].getDifference();
            case 8:
                return this.teams[row].getScore();
        }

        return null;
    }
}
