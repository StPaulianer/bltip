/*
 * Created on 30.07.2005
 */
package bltip.storage.db;

import bltip.common.BlTipException;
import bltip.common.Constants;
import bltip.gui.Messages;
import bltip.model.Game;
import bltip.model.Team;
import bltip.model.User;
import bltip.util.BlTipUtility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static bltip.storage.db.DBTablesAndStatements.*;

/**
 * Verwaltet die Objekte, bevor sie in die Datenbank geschrieben werden.
 *
 * @author Nico
 * @version 28.08.2006
 */
class ObjectSaver {

    private Statement stmt;

    private Team[] teams;

    private Map<Integer, User> user;

    private Game[] games;

    public ObjectSaver(Connection conn) throws BlTipException {
        try {
            this.stmt = conn.createStatement();
        } catch (SQLException e) {
            handle(e, e.getMessage());
        }
    }

    /**
     * L�dt die Daten aus der Datenbank und speichert sie als Objekte.
     *
     * @throws BlTipException Ein Fehler ist aufgetreten
     */
    void load() throws BlTipException {
        this.teams = this.getTeamsFromDB();
        this.user = this.getUserFromDB();
    }

    /**
     * Schreibt die Objekte in die Datenbank.
     *
     * @throws BlTipException Ein Fehler ist aufgetreten
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

    Map<Integer, User> getUser() {
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
            handle(e, e.getMessage());
        }

        return teams;
    }

    private Map<Integer, User> getUserFromDB() throws BlTipException {
        Map<Integer, User> users = new HashMap<>();

        String getAllUser = "SELECT * FROM " + DBTABLE_USER + ";";

        try {
            ResultSet result = stmt.executeQuery(getAllUser);
            while (result.next()) {
                int userID = result.getInt(USER_ID);

                users.put(userID, new User(userID, result.getString(USER_NAME), result.getInt(USER_TIPSCORE),
                        result.getInt(USER_TABLESCORE), result.getInt(USER_EXTRASCORE)));
            }

            result.close();
        } catch (SQLException e) {
            handle(e, e.getMessage());
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
            handle(e, e.getMessage());
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
            handle(e, e.getMessage());
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
