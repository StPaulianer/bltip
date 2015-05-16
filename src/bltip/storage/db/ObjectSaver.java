/*
 * Created on 30.07.2005
 */
package bltip.storage.db;

import static bltip.storage.db.DBTablesAndStatements.DBTABLE_GAMES;
import static bltip.storage.db.DBTablesAndStatements.DBTABLE_TEAMS;
import static bltip.storage.db.DBTablesAndStatements.DBTABLE_USER;
import static bltip.storage.db.DBTablesAndStatements.GAMES_GUESTRESULT;
import static bltip.storage.db.DBTablesAndStatements.GAMES_HOMERESULT;
import static bltip.storage.db.DBTablesAndStatements.GAMES_ID;
import static bltip.storage.db.DBTablesAndStatements.TEAMS_GGOALS;
import static bltip.storage.db.DBTablesAndStatements.TEAMS_GGOALSAG;
import static bltip.storage.db.DBTablesAndStatements.TEAMS_GLOSES;
import static bltip.storage.db.DBTablesAndStatements.TEAMS_GREMIS;
import static bltip.storage.db.DBTablesAndStatements.TEAMS_GWINS;
import static bltip.storage.db.DBTablesAndStatements.TEAMS_HGOALS;
import static bltip.storage.db.DBTablesAndStatements.TEAMS_HGOALSAG;
import static bltip.storage.db.DBTablesAndStatements.TEAMS_HLOSES;
import static bltip.storage.db.DBTablesAndStatements.TEAMS_HREMIS;
import static bltip.storage.db.DBTablesAndStatements.TEAMS_HWINS;
import static bltip.storage.db.DBTablesAndStatements.TEAMS_NAME;
import static bltip.storage.db.DBTablesAndStatements.USER_ID;
import static bltip.storage.db.DBTablesAndStatements.USER_NAME;
import static bltip.storage.db.DBTablesAndStatements.USER_TABLESCORE;
import static bltip.storage.db.DBTablesAndStatements.USER_TIPSCORE;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import bltip.common.BlTipException;
import bltip.common.Constants;
import bltip.gui.Messages;
import bltip.util.BlTipUtility;
import bltip.valueobject.Game;
import bltip.valueobject.Team;
import bltip.valueobject.User;

/**
 * Verwaltet die Objekte, bevor sie in die Datenbank geschrieben werden.
 * 
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 28.08.2006
 */
class ObjectSaver {

    private Statement stmt;

    private Team[] teams;

    private HashMap<Integer, User> user;

    private Game[] games;

    public ObjectSaver(Connection conn) throws BlTipException {
        try {
            this.stmt = conn.createStatement();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }
    }

    /**
     * L�dt die Daten aus der Datenbank und speichert sie als Objekte.
     * 
     * @throws BlTipException
     */
    void load() throws BlTipException {
        this.teams = this.getTeamsFromDB();
        this.user = this.getUserFromDB();
    }

    /**
     * Schreibt die Objekte in die Datenbank.
     * 
     * @throws BlTipException
     */
    void save() throws BlTipException {
        this.updateTeams();
        this.updateUser();
        this.updateGames();
    }

    Team getTeam(String name) {
        for (Team team : teams) {
            if (team.getName().equals(name))
                return team;
        }

        return null;
    }

    Team[] getTeams() {
        return this.teams;
    }

    protected User getUser(int id) {
        return user.get(new Integer(id));
    }

    HashMap<Integer, User> getUser() {
        return this.user;
    }

    void setGames(Game[] games) {
        this.games = games;
    }

    private Team[] getTeamsFromDB() throws BlTipException {
        Team[] teams = new Team[Constants.COUNT_OF_TEAMS];

        try {
            String getAllTeams = "SELECT * FROM " + DBTABLE_TEAMS + ";";

            wlnStmt(getAllTeams);
            ResultSet result = stmt.executeQuery(getAllTeams);

            int index = 0;
            while (result.next()) {
                teams[index++] = new Team(result.getString(TEAMS_NAME), result.getInt(TEAMS_HWINS), result.getInt(TEAMS_GWINS),
                        result.getInt(TEAMS_HREMIS), result.getInt(TEAMS_GREMIS), result.getInt(TEAMS_HLOSES), result
                                .getInt(TEAMS_GLOSES), result.getInt(TEAMS_HGOALS), result.getInt(TEAMS_GGOALS), result
                                .getInt(TEAMS_HGOALSAG), result.getInt(TEAMS_GGOALSAG));
            }

            result.close();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }

        return teams;
    }

    private HashMap<Integer, User> getUserFromDB() throws BlTipException {
        HashMap<Integer, User> users = new HashMap<Integer, User>();

        String getAllUser = "SELECT * FROM " + DBTABLE_USER + ";";

        try {
            ResultSet result = stmt.executeQuery(getAllUser);
            while (result.next()) {
                int userID = result.getInt(USER_ID);

                users.put(userID, new User(userID, result.getString(USER_NAME), result.getInt(USER_TIPSCORE), result
                        .getInt(USER_TABLESCORE)));
            }

            result.close();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }

        return users;
    }

    private void updateTeams() throws BlTipException {
        try {
            for (Team team : teams) {
                String actualize_team = "UPDATE " + DBTABLE_TEAMS + " SET " + TEAMS_HWINS + "=" + team.getHomewins()
                        + ", "
                        + TEAMS_HREMIS + "=" + team.getHomeremis() + ", " + TEAMS_HLOSES + "=" + team.getHomeloses()
                        + ", " + TEAMS_GWINS + "=" + team.getGuestwins() + ", " + TEAMS_GREMIS + "="
                        + team.getGuestremis() + ", " + TEAMS_GLOSES + "=" + team.getGuestloses() + ", " + TEAMS_HGOALS
                        + "=" + team.getHomegoals() + ", " + TEAMS_GGOALS + "=" + team.getGuestgoals() + ", "
                        + TEAMS_HGOALSAG + "=" + team.getHomegoalsAgainst() + ", " + TEAMS_GGOALSAG + "="
                        + team.getGuestgoalsAgainst() + " WHERE " + TEAMS_NAME + "='"
                        + BlTipUtility.maskToSQL(team.getName()) + "';";

                wlnStmt("actualize_team: " + actualize_team);
                this.stmt.addBatch(actualize_team);
            }

            this.stmt.executeBatch();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }
    }

    private void updateUser() throws BlTipException {
        try {
            for (User curUser : this.user.values()) {
                String actualize_user = "UPDATE " + DBTABLE_USER + " SET " + USER_TIPSCORE + "=" + curUser.getTipscore() + ","
                        + USER_TABLESCORE + "=" + curUser.getTablescore() + " WHERE " + USER_ID + "=" + curUser.getId() + ";";

                wlnStmt("actualize_user: " + actualize_user);
                this.stmt.addBatch(actualize_user);

            }

            this.stmt.executeBatch();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }
    }

    private void updateGames() throws BlTipException {
        try {
            for (Game game : this.games) {
                String actualize_team = "UPDATE " + DBTABLE_GAMES + " SET " + GAMES_HOMERESULT + "="
                        + game.getHomeresult() + ", " + GAMES_GUESTRESULT + "=" + game.getGuestresult()
                        + " WHERE " + GAMES_ID + "=" + game.getId() + ";";

                wlnStmt("actualize_team: " + actualize_team);
                this.stmt.addBatch(actualize_team);
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
     * Methode zur Ausgabe von SQL-Statements �ber den System.out.println - Stream
     * 
     * @param str Statusmeldung
     */
    private void wlnStmt(String str) {
        if (Database.WRITE_STMTS) {
            System.out.println("SQL-Statement\t\tObjectSaver\t\t" + str);
        }
    }
}
