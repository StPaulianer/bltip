/*
 * Created on 31.08.2004
 */
package bltip.storage.db;

import static bltip.storage.db.DBTablesAndStatements.DBTABLE_GAMES;
import static bltip.storage.db.DBTablesAndStatements.DBTABLE_TEAMS;
import static bltip.storage.db.DBTablesAndStatements.DBTABLE_TIPS;
import static bltip.storage.db.DBTablesAndStatements.DBTABLE_TIPTABLES;
import static bltip.storage.db.DBTablesAndStatements.DBTABLE_USER;
import static bltip.storage.db.DBTablesAndStatements.GAMES_GUESTTEAM;
import static bltip.storage.db.DBTablesAndStatements.GAMES_HOMETEAM;
import static bltip.storage.db.DBTablesAndStatements.GAMES_ID;
import static bltip.storage.db.DBTablesAndStatements.GAMES_ROUND;
import static bltip.storage.db.DBTablesAndStatements.TEAMS_NAME;
import static bltip.storage.db.DBTablesAndStatements.TIPTABLES_GGOALS;
import static bltip.storage.db.DBTablesAndStatements.TIPTABLES_GGOALS_AG;
import static bltip.storage.db.DBTablesAndStatements.TIPTABLES_GLOSES;
import static bltip.storage.db.DBTablesAndStatements.TIPTABLES_GREMIS;
import static bltip.storage.db.DBTablesAndStatements.TIPTABLES_GWINS;
import static bltip.storage.db.DBTablesAndStatements.TIPTABLES_HGOALS;
import static bltip.storage.db.DBTablesAndStatements.TIPTABLES_HGOALS_AG;
import static bltip.storage.db.DBTablesAndStatements.TIPTABLES_HLOSES;
import static bltip.storage.db.DBTablesAndStatements.TIPTABLES_HREMIS;
import static bltip.storage.db.DBTablesAndStatements.TIPTABLES_HWINS;
import static bltip.storage.db.DBTablesAndStatements.TIPTABLES_POSITION;
import static bltip.storage.db.DBTablesAndStatements.TIP_GUEST;
import static bltip.storage.db.DBTablesAndStatements.TIP_HOME;
import static bltip.storage.db.DBTablesAndStatements.TIP_JOKER;
import static bltip.storage.db.DBTablesAndStatements.USER_ID;
import static bltip.storage.db.DBTablesAndStatements.USER_NAME;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import bltip.common.BlTipException;
import bltip.gui.Messages;
import bltip.util.BlTipUtility;
import bltip.valueobject.Team;

/**
 * Zum Einf�gen in die Datenbank
 * 
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 28.08.2006
 */
class DBInserter {

    private final static boolean DEBUG = false;

    private Statement stmt;

    /**
     * Konstruktor instanziiert auf der �bergebenen Verbindung ein Statement.
     * 
     * @param conn Verbindung zur Datenbank, die �bergabe von <code>null<code> f�hrt zu
	 * 		einer <code>NullPointerException<code>.
     * @throws BlTipException Bei Datenbankfehlern
     */
    public DBInserter(Connection conn) throws BlTipException {
        try {
            this.stmt = conn.createStatement();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }
    }

    /**
     * F�gt einen Tipp in die DB ein
     * 
     * @param userID Eindeutiger Schl�ssel des Users
     * @param gamenr Nummer des Spiels
     * @param hometip Getippte Tore f�r die Heimmannschaft
     * @param guesttip Getippte Tore f�r die Ausw�rtsmannschaft
     * @param joker Ist der Joker gesetzt?
     * @throws BlTipException Bei Datenbankfehlern
     */
    void insertTipIntoDB(int userID, int gamenr, int hometip, int guesttip, boolean joker) throws BlTipException {

        wln("insert tip of user with id " + userID);
        String insert_tip = "INSERT " + DBTABLE_TIPS + " (" + USER_ID + "," + GAMES_ID + "," + TIP_HOME + "," + TIP_GUEST + ","
                + TIP_JOKER + ")" + " VALUES (" + userID + "," + gamenr + "," + hometip + "," + guesttip + ",";
        insert_tip += joker ? "1);" : "0);";

        wlnStmt(insert_tip);
        try {
            stmt.executeUpdate(insert_tip);
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }
    }

    /**
     * F�gt eine Mannschaft in die Datenbank ein (nur den Namen der Mannschaft)
     * 
     * @param team Mannschaftsname
     * @throws BlTipException Bei Datenbankfehlern
     */
    void insertTeamIntoDB(String team) throws BlTipException {
        wln("insert team " + team);
        String insert_team = "INSERT " + DBTABLE_TEAMS + " (" + TEAMS_NAME + ")" + " VALUES (" + "'"
                + BlTipUtility.maskToSQL(team) + "');";

        wlnStmt(insert_team);
        try {
            stmt.executeUpdate(insert_team);
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }
    }

    /**
     * F�gt einen Tipper ein (nur Name)
     * 
     * @param user Tippername
     * @throws BlTipException Bei Datenbankfehlern
     */
    void insertUserIntoDB(String user) throws BlTipException {
        wln("insert user " + user);
        String insert_tipper = "INSERT " + DBTABLE_USER + " (" + USER_NAME + ")" + " VALUES (" + "'"
                + BlTipUtility.maskToSQL(user) + "');";

        wlnStmt(insert_tipper);
        try {
            stmt.executeUpdate(insert_tipper);
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }
    }

    /**
     * F�gt eine Paarung in die Datenbank ein, noch ohne Ergebnis
     * 
     * @param round Spieltag
     * @param home Name der Heimmannschaft
     * @param guest Name der Ausw�rtsmannschaft
     * @throws BlTipException Bei Datenbankfehlern
     */
    void insertGameIntoDB(int round, String home, String guest) throws BlTipException {
        wln("insert game " + home + "-" + guest);
        String insert_game = "INSERT " + DBTABLE_GAMES + " (" + GAMES_ROUND + "," + GAMES_HOMETEAM + "," + GAMES_GUESTTEAM + ")"
                + " VALUES (" + round + "," + "'" + BlTipUtility.maskToSQL(home) + "'," + "'" + BlTipUtility.maskToSQL(guest)
                + "');";

        wlnStmt(insert_game);
        try {
            stmt.executeUpdate(insert_game);
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }
    }

    /**
     * F�gt eine Tabelle, die vom User mit der �bergebenen ID getippt wurde, ein
     * 
     * @param userID ID des Users, dessen Tabelle eingef�gt werden soll
     * @param teams Array von Teams in Reihenfolge (<code>teams[0]</code> ist das beste Team
     *            usw.)
     * @throws BlTipException Bei Datenbankfehlern
     */
    void insertUserTableIntoDB(int userID, Team[] teams) throws BlTipException {
        try {
            wln("insert tiptable of user with id " + userID);
            for (int i = 0; i < teams.length; i++) {
                String insert_usertable = "INSERT " + DBTABLE_TIPTABLES + " (" + USER_ID + ", " + TEAMS_NAME + ", "
                        + TIPTABLES_POSITION + ", " + TIPTABLES_HWINS + ", " + TIPTABLES_HREMIS + ", " + TIPTABLES_HLOSES + ", "
                        + TIPTABLES_GWINS + ", " + TIPTABLES_GREMIS + ", " + TIPTABLES_GLOSES + ", " + TIPTABLES_HGOALS + ", "
                        + TIPTABLES_GGOALS + ", " + TIPTABLES_HGOALS_AG + ", " + TIPTABLES_GGOALS_AG + ")" + " VALUES (" + userID
                        + ", " + "'" + BlTipUtility.maskToSQL(teams[i].getName()) + "', " + (i + 1) + ", "
                        + teams[i].getHomewins() + ", " + teams[i].getHomeremis() + ", " + teams[i].getHomeloses() + ", "
                        + teams[i].getGuestwins() + ", " + teams[i].getGuestremis() + ", " + teams[i].getGuestloses() + ", "
                        + teams[i].getHomegoals() + ", " + teams[i].getGuestgoals() + ", " + teams[i].getHomegoalsAgainst()
                        + ", " + teams[i].getGuestgoalsAgainst() + ");";

                wlnStmt(insert_usertable);
                this.stmt.addBatch(insert_usertable);
            }

            this.stmt.executeBatch();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }
    }

    /**
     * Regelt das Exception-Handling
     * 
     * @param e Tats�chlich aufgetretene Ausnahme
     * @param title Titel des Fehlerfensters
     * @param msg Nachricht im Fehlerfenster
     * @throws BlTipException Bei Datenbankfehlern
     */
    private void handle(Exception e, String title, String msg) throws BlTipException {
        if (e != null) {
            e.printStackTrace();
            throw new BlTipException(e, title, msg);
        } else
            throw new BlTipException(title, msg);
    }

    /**
     * Methode zur Ausgabe von Uhrzeit + Statusmeldung �ber den <code>System.out.println</code>
     * -Stream, wenn <code>DEBUG = true</code> ist.
     * 
     * @param str Statusmeldung
     */
    private void wln(String str) {
        if (DEBUG) {
            System.out.println(BlTipUtility.currentTime() + "\tDBInserter\t\t\t" + str);
        }
    }

    /**
     * Methode zur Ausgabe von SQL-Statements �ber den <code>System.out.println</code> -Stream
     * 
     * @param str Statusmeldung
     */
    private void wlnStmt(String str) {
        if (Database.WRITE_STMTS) {
            System.out.println("SQL-Statement\t\tSQL\t\t\t\t" + str);
        }
    }
}
