/*
 * Created on 31.08.2004
 */
package bltip.storage.db;

import bltip.common.BlTipException;
import bltip.gui.Messages;
import bltip.model.Team;
import bltip.model.Tip;
import bltip.util.BlTipUtility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static bltip.storage.db.DBTablesAndStatements.*;

/**
 * Zum Einf�gen in die Datenbank
 *
 * @author Nico
 * @version 28.08.2006
 */
class DBInserter {

    private final static boolean DEBUG = false;

    private Statement stmt;

    /**
     * Konstruktor instanziiert auf der �bergebenen Verbindung ein Statement.
     *
     * @param conn Verbindung zur Datenbank, die �bergabe von <code>null<code> f�hrt zu
     *             einer <code>NullPointerException<code>.
     * @throws BlTipException Bei Datenbankfehlern
     */
    DBInserter(Connection conn) throws BlTipException {
        try {
            this.stmt = conn.createStatement();
        } catch (SQLException e) {
            handle(e, e.getMessage());
        }
    }

    void insertTipIntoDB(int userID, int gamenr, Tip tip)
            throws BlTipException {
        wln("insert tip of user with id " + userID);
        String insert_tip =
                "INSERT " + DBTABLE_TIPS + " ("
                        + USER_ID + ","
                        + GAMES_ID + ","
                        + TIP_HOME + ","
                        + TIP_GUEST + ","
                        + TIP_JOKER + ","
                        + TIP_DELUXE_JOKER
                        + ") VALUES ("
                        + userID + ","
                        + gamenr + ","
                        + tip.getHomeTip() + ","
                        + tip.getGuestTip() + ",";
        insert_tip += Tip.TipType.JOKER.equals(tip.getTipType()) ? "1," : "0,";
        insert_tip += Tip.TipType.DELUXE_JOKER.equals(tip.getTipType()) ? "1);" : "0);";

        wlnStmt(insert_tip);
        try {
            stmt.executeUpdate(insert_tip);
        } catch (SQLException e) {
            handle(e, e.getMessage());
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
        String insert_team = "INSERT " + DBTABLE_TEAMS + " (" + TEAMS_NAME + ")"
                + " VALUES (" + "'" + BlTipUtility.maskToSQL(team) + "');";

        wlnStmt(insert_team);
        try {
            stmt.executeUpdate(insert_team);
        } catch (SQLException e) {
            handle(e, e.getMessage());
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
        String insert_tipper = "INSERT " + DBTABLE_USER + " (" + USER_NAME + ")"
                + " VALUES (" + "'" + BlTipUtility.maskToSQL(user) + "');";

        wlnStmt(insert_tipper);
        try {
            stmt.executeUpdate(insert_tipper);
        } catch (SQLException e) {
            handle(e, e.getMessage());
        }
    }

    /**
     * F�gt eine Paarung in die Datenbank ein, noch ohne Ergebnis
     *
     * @param round Spieltag
     * @param home  Name der Heimmannschaft
     * @param guest Name der Ausw�rtsmannschaft
     * @return ID des Spiels
     * @throws BlTipException Bei Datenbankfehlern
     */
    int insertGameIntoDB(int round, String home, String guest) throws BlTipException {
        wln("insert game " + home + "-" + guest);
        String insert_game = "INSERT " + DBTABLE_GAMES + " (" + GAMES_ROUND + "," + GAMES_HOMETEAM + "," + GAMES_GUESTTEAM + ")"
                + " VALUES (" + round + "," + "'" + BlTipUtility.maskToSQL(home) + "'," + "'" + BlTipUtility.maskToSQL(guest) + "');";

        wlnStmt(insert_game);
        try {
            stmt.executeUpdate(insert_game, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            handle(e, e.getMessage());
        }
        return -1;
    }

    /**
     * F�gt eine Tabelle, die vom User mit der �bergebenen ID getippt wurde, ein
     *
     * @param userID ID des Users, dessen Tabelle eingef�gt werden soll
     * @param teams  Array von Teams in Reihenfolge (<code>teams[0]</code> ist das beste Team
     *               usw.)
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
            handle(e, e.getMessage());
        }
    }

    /**
     * Regelt das Exception-Handling
     *
     * @param e   Tats�chlich aufgetretene Ausnahme
     * @param msg Nachricht im Fehlerfenster
     * @throws BlTipException Bei Datenbankfehlern
     */
    private void handle(Exception e, String msg) throws BlTipException {
        if (e != null) {
            e.printStackTrace();
            throw new BlTipException(Messages.ERRORTITLE_DB_GENERAL, msg);
        } else
            throw new BlTipException(Messages.ERRORTITLE_DB_GENERAL, msg);
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
