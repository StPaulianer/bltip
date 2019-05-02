/*
 * Created on 08.10.2004
 */
package bltip.storage.db;

import bltip.common.BlTipException;
import bltip.common.Constants;
import bltip.gui.Messages;
import bltip.model.Game;
import bltip.model.Team;
import bltip.model.User;
import bltip.storage.Printer;
import bltip.storage.StorageSystem;
import bltip.util.BlTipUtility;

import java.io.File;
import java.sql.*;
import java.util.Map;

import static bltip.storage.db.DBTablesAndStatements.*;

/**
 * Implementiert ein Speicherungssystem als MySQL-Datenbank.
 *
 * @author Nico
 * @version 28.08.2006
 */
public class Database implements StorageSystem {

    /**
     * Sollen die SQL-Statements ausgegeben werden?
     */
    public static final boolean WRITE_STMTS = false;
    private static final boolean DEBUG = true;
    private final String dbuser, dbpasswd, dbname, dburl, dburlSuffix, mysqlDriver;

    private Connection conn;

    private Statement stmt;

    private ObjectSaver os;

    /**
     * Der Konstruktor erwartet die Parameter der Datenbankverbindung.
     *
     * @param user         User der Datenbank, die �bergabe von <code>null</code> ist erlaubt, eine
     *                     Verbindung zu einer Datenbank dann aber unrealistisch...
     * @param pwd          Passwort des User, die �bergabe von <code>null</code> ist erlaubt, eine
     *                     Verbindung zu einer Datenbank dann aber unrealistisch...
     * @param name         Name der Datenbank, die �bergabe von <code>null</code> ist erlaubt, eine
     *                     Verbindung zu einer Datenbank dann aber unrealistisch...
     * @param url          URL der Datenbank, die �bergabe von <code>null</code> ist erlaubt, eine
     *                     Verbindung zu einer Datenbank dann aber unrealistisch...
     * @param url_suffix   Ein Anh�ngsel zur URL der Datenbank, die �bergabe von
     *                     <code>null</code> ist erlaubt.
     * @param mysql_driver Der verwendete MySQL-Treiber, die �bergabe von <code>null</code> ist
     *                     erlaubt, eine Verbindung zu einer Datenbank dann aber unrealistisch...
     */
    public Database(String user, String pwd, String name, String url, String url_suffix, String mysql_driver) {
        this.dbuser = user;
        this.dbpasswd = pwd;
        this.dbname = name;
        this.dburl = url;
        this.dburlSuffix = url_suffix;
        this.mysqlDriver = mysql_driver;
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#connect()
     */
    public void connect() throws BlTipException {
        try {
            wln("loading MySQL-Driver");
            Class.forName(this.mysqlDriver).newInstance();
        } catch (Exception e) {
            handle(e, Messages.ERRORTITLE_NO_DBCONNECTION, Messages.ERRORMSG_NO_DBCONNECTION);
        }

        try {
            String url = this.dburl + this.dbname + (this.dburlSuffix != null ? this.dburlSuffix : "");
            wln("connecting to " + url);
            this.conn = DriverManager.getConnection(url, this.dbuser, this.dbpasswd);

            wln("creating statement");
            this.stmt = this.conn.createStatement();

            this.os = new ObjectSaver(this.conn);
            this.os.load();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#disconnect()
     */
    public void disconnect() throws BlTipException {
        try {
            this.stmt.close();
            this.conn.close();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        } catch (NullPointerException e) {
            handle(e, Messages.ERRORTITLE_CLOSING_DBCONN, Messages.ERRORMSG_CLOSING_DBCONN);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#createStorageSystem()
     */
    public void create() throws BlTipException {
        DBCreator dbc = new DBCreator();
        dbc.createDatabase(this.dbuser, this.dbpasswd, this.dbname, this.dburl, this.dburlSuffix, this.mysqlDriver);
        if (os != null) {
            os.load();
            os.setGames(null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#initialize(java.io.File, java.io.File, java.io.File)
     */
    public void initialize(File games, File user, File tips) throws BlTipException {
        DbFiller dbf = new DbFiller(this, this.conn);
        dbf.fill(games, user, tips);
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#getFirstRoundWithoutResults()
     */
    public int getFirstRoundWithoutResults() throws BlTipException {
        try {
            String getRound = "SELECT MIN(" + GAMES_ROUND + ") FROM " + DBTABLE_GAMES + " WHERE " + GAMES_HOMERESULT
                    + "="
                    + NO_RESULT + " AND " + GAMES_GUESTRESULT + "=" + NO_RESULT + ";";

            ResultSet round = this.stmt.executeQuery(getRound);

            if (round.next()) {
                int actRound = round.getInt("MIN(" + GAMES_ROUND + ")");
                return (actRound == 0 ? Constants.COUNT_OF_ROUNDS : actRound);
            }

            round.close();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }

        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#getActualRound()
     */
    public int getActualRound() throws BlTipException {
        try {
            String getRound = "SELECT MAX(" + GAMES_ROUND + ") FROM " + DBTABLE_GAMES + " WHERE " + GAMES_HOMERESULT
                    + "<>"
                    + NO_RESULT + " AND " + GAMES_GUESTRESULT + "<>" + NO_RESULT + ";";

            ResultSet round = this.stmt.executeQuery(getRound);

            if (round.next()) {
                int actRound = round.getInt("MAX(" + GAMES_ROUND + ")");
                return (actRound == 0 ? 1 : actRound);
            }

            round.close();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }

        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#getBLTable()
     */
    public Team[] getBLTable() throws BlTipException {
        Team[] teams = new Team[Constants.COUNT_OF_TEAMS];

        try {
            String getAllTeams = "SELECT * FROM " + DBTABLE_TEAMS + ";";

            wlnStmt(getAllTeams);
            ResultSet result = stmt.executeQuery(getAllTeams);

            int index = 0;
            while (result.next()) {
                teams[index++] = new Team(result.getString(TEAMS_NAME), result.getInt(TEAMS_HWINS),
                        result.getInt(TEAMS_GWINS),
                        result.getInt(TEAMS_HREMIS), result.getInt(TEAMS_GREMIS), result.getInt(TEAMS_HLOSES),
                        result.getInt(TEAMS_GLOSES), result.getInt(TEAMS_HGOALS), result.getInt(TEAMS_GGOALS),
                        result.getInt(TEAMS_HGOALSAG), result.getInt(TEAMS_GGOALSAG));
            }
            sort(teams);

            result.close();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }

        return teams;
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#getCountOfUser()
     */
    public int getCountOfUser() throws BlTipException {
        try {
            String getCount = "SELECT " + COUNT_OF_ALL + " FROM " + DBTABLE_USER + ";";

            wlnStmt(getCount);

            ResultSet count = stmt.executeQuery(getCount);
            if (count.next()) {
                return count.getInt(COUNT_OF_ALL);
            }

            count.close();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }

        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#getTableOfUser(int)
     */
    public Team[] getTableOfUser(int userID) throws BlTipException {
        Team[] teams = new Team[Constants.COUNT_OF_TEAMS];

        try {
            String getTipteams = "SELECT * FROM " + DBTABLE_TIPTABLES + " WHERE " + USER_ID + "=" + userID + " ORDER " +
                    "BY "
                    + TIPTABLES_POSITION + " ASC;";

            wlnStmt(getTipteams);
            ResultSet result = stmt.executeQuery(getTipteams);

            int index = 0;
            while (result.next()) {
                teams[index++] = new Team(result.getString(TEAMS_NAME), result.getInt(TIPTABLES_HWINS),
                        result.getInt(TIPTABLES_GWINS), result.getInt(TIPTABLES_HREMIS),
                        result.getInt(TIPTABLES_GREMIS),
                        result.getInt(TIPTABLES_HLOSES), result.getInt(TIPTABLES_GLOSES),
                        result.getInt(TIPTABLES_HGOALS),
                        result.getInt(TIPTABLES_GGOALS), result.getInt(TIPTABLES_HGOALS_AG),
                        result.getInt(TIPTABLES_GGOALS_AG));
            }

            result.close();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }

        return teams;
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#getUsertable()
     */
    public User[] getUsertable() throws BlTipException {
        ResultSet result = null;
        try {
            int count = this.getCountOfUser();
            if (count != -1) {
                User[] user = new User[count];

                String getAllUser = "SELECT * FROM " + DBTABLE_USER + ";";
                wlnStmt(getAllUser);
                result = stmt.executeQuery(getAllUser);

                int index = 0;
                while (result.next()) {
                    user[index++] = new User(result.getInt(USER_ID), result.getString(USER_NAME),
                            result.getInt(USER_TIPSCORE), result.getInt(USER_TABLESCORE),
                            result.getInt(USER_EXTRASCORE));
                }

                sort(user);

                return user;
            }
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#getGames(int)
     */
    public Game[] getGames(int round) throws BlTipException {
        try {
            Game[] games = new Game[Constants.COUNT_OF_GAMES_PER_ROUND];

            String getGames = "SELECT * FROM " + DBTABLE_GAMES + " WHERE " + GAMES_ROUND + "=" + round + " ORDER BY "
                    + GAMES_ID + ";";

            wlnStmt(getGames);
            ResultSet result = stmt.executeQuery(getGames);

            int index = 0;
            while (result.next()) {
                games[index] = new Game(result.getInt(GAMES_ID),
                        result.getString(GAMES_HOMETEAM),
                        result.getString(GAMES_GUESTTEAM), result.getInt(GAMES_HOMERESULT),
                        result.getInt(GAMES_GUESTRESULT));

                index++;
            }

            result.close();

            return games;
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#setResults(bltipp.data.Game[], int[], int[])
     */
    public void setResults(Game[] games, int[] newHomeResults, int[] newGuestResults) throws BlTipException {

        for (int i = 0; i < games.length; i++) {

            if ((games[i].getHomeresult() != newHomeResults[i] || games[i].getGuestresult() != newGuestResults[i])) {

                // das Ergebnis hat sich ge�ndert
                wln("change result for game with id: " + games[i].getId() + " to " + newHomeResults[i] + ":" +
                        newGuestResults[i]);

                Team home = os.getTeam(games[i].getHome());
                Team guest = os.getTeam(games[i].getGuest());
                int oldHomeRes = games[i].getHomeresult();
                int oldGuestRes = games[i].getGuestresult();
                games[i].setResult(newHomeResults[i], newGuestResults[i]);

                resultUpdateTeams(home, guest, oldHomeRes, oldGuestRes, newHomeResults[i], newGuestResults[i]);

                resultUpdateTipscores(os.getUser(), games[i].getId(), oldHomeRes, oldGuestRes, newHomeResults[i],
                        newGuestResults[i]);
            }
        }

        // ... in ein sortiertes Array
        Team[] teamsSorted = sort(os.getTeams());

        // Berechnen der Tabellenpunkte
        calculateTableScores(os.getUser(), teamsSorted);

        os.setGames(games);
        os.save();
    }

    private void resultUpdateTeams(Team home, Team guest, int oldHomeRes, int oldGuestRes, int newHomeRes,
                                   int newGuestRes) {

        // altes Ergebnis r�ckg�ngig machen...
        if (oldHomeRes != NO_RESULT) {
            // ... Punkte...
            if (oldHomeRes - oldGuestRes < 0) {
                home.removeHomelose();
                guest.removeGuestwin();
            } else if (oldHomeRes - oldGuestRes > 0) {
                home.removeHomewin();
                guest.removeGuestlose();
            } else {
                home.removeHomeremis();
                guest.removeGuestremis();
            }

            // ... und Tore
            home.setGoals(home.getHomegoals() - oldHomeRes, home.getGuestgoals());
            home.setGoalsAgainst(home.getHomegoalsAgainst() - oldGuestRes, home.getGuestgoalsAgainst());
            guest.setGoals(guest.getHomegoals(), guest.getGuestgoals() - oldGuestRes);
            guest.setGoalsAgainst(guest.getHomegoalsAgainst(), guest.getGuestgoalsAgainst() - oldHomeRes);
        }

        if (newHomeRes != NO_RESULT) {
            // ... Punkte...
            if (newHomeRes - newGuestRes < 0) {
                home.addHomelose();
                guest.addGuestwin();
            } else if (newHomeRes - newGuestRes > 0) {
                home.addHomewin();
                guest.addGuestlose();
            } else {
                home.addHomeremis();
                guest.addGuestremis();
            }

            // ... und Tore
            home.setGoals(home.getHomegoals() + newHomeRes, home.getGuestgoals());
            home.setGoalsAgainst(home.getHomegoalsAgainst() + newGuestRes, home.getGuestgoalsAgainst());
            guest.setGoals(guest.getHomegoals(), guest.getGuestgoals() + newGuestRes);
            guest.setGoalsAgainst(guest.getHomegoalsAgainst(), guest.getGuestgoalsAgainst() + newHomeRes);
        }
    }

    /**
     * Berechnet zu der Paarung, deren ID �bergeben wird, die Tipppunkte der User und passt die
     * Punkte an. Es wird <b>nicht</b> auf der DB gearbeitet, sondern auf den Usern, die in der
     * �bergebenen <code>HashMap</code> sind.<br>
     * Seit der Saison 2004/2005 gibt es folgende Tipppunkte-Regelung:<br>
     * <ul>
     * <li><b>Richtiges Ergebnis</b>: (2 + Differenz) Punkte</li>
     * <li><b>Richtige Differenz</b>: (1 + Differenz) Punkte</li>
     * <li><b>Richtige Tendenz</b>: 1 Punkt</li>
     * <li><b>Richtiges Unentschieden</b>: 4 Punkte</li>
     * <li><b>"Falsches" Unentschieden</b>: 3 Punkte</li>
     * <li><b>Joker</b>: 5 Punkte mehr, wenn zumindest Tendenz richtig, -5 sonst</li>
     * </ul>
     *
     * @param gameID      ID der Paarung, zu der die Tipppunkte berechnet werden sollen
     * @param oldHomeRes  Altes Ergebnis der Heimmannschaft.
     * @param oldGuestRes Altes Ergebnis der Ausw�rtsmannschaft.
     * @param newHomeRes  Neues Ergebnis der Heimmannschaft.
     * @param newGuestRes Neues Ergebnis der Ausw�rtsmannschaft.
     * @throws BlTipException Bei Datenbankfehlern
     */
    private void resultUpdateTipscores(Map<Integer, User> allUser, int gameID, int oldHomeRes, int oldGuestRes,
                                       int newHomeRes, int newGuestRes) throws BlTipException {

        try {
            String getTipOfUser = "SELECT * FROM " + DBTABLE_TIPS + " NATURAL JOIN " + DBTABLE_USER + " WHERE " +
                    GAMES_ID + "=" + gameID + ";";

            wlnStmt(getTipOfUser);

            ResultSet matching = stmt.executeQuery(getTipOfUser);
            while (matching.next()) {
                User user = allUser.get(matching.getInt(USER_ID));

                int tipScore = user.getTipscore();
                boolean deluxeJoker = matching.getInt(TIP_DELUXE_JOKER) == 1;
                boolean joker = matching.getInt(TIP_JOKER) == 1;
                int tipH = matching.getInt(TIP_HOME);
                int tipG = matching.getInt(TIP_GUEST);
                int tipDiff = tipH - tipG;

                int oldDiff = oldHomeRes - oldGuestRes;
                int newDiff = newHomeRes - newGuestRes;

                if (oldHomeRes != NO_RESULT) {
                    if ((oldDiff < 0 && tipDiff < 0) || (oldDiff > 0 && tipDiff > 0)) {
                        // Joker passt...
                        if (joker) {
                            tipScore -= TIPSCORE_FOR_CORRECT_JOKER;
                        } else if (deluxeJoker) {
                            tipScore -= TIPSCORE_FOR_CORRECT_DELUXE_JOKER;
                        }

                        if (oldDiff == tipDiff) {
                            if (oldHomeRes == tipH) {
                                // richtiges Ergebnis
                                tipScore -= TIPSCORE_FOR_CORRECT_TIP + Math.abs(oldDiff);
                            } else {
                                // richtige Differenz
                                tipScore -= TIPSCORE_FOR_DIFFERENCE + Math.abs(oldDiff);
                            }
                        } else {
                            // richtige Tendenz
                            tipScore -= TIPSCORE_FOR_TENDENCY;
                        }
                    } else if (oldDiff == tipDiff) {
                        // Joker passt...
                        if (joker) {
                            tipScore -= TIPSCORE_FOR_CORRECT_JOKER;
                        } else if (deluxeJoker) {
                            tipScore -= TIPSCORE_FOR_CORRECT_DELUXE_JOKER;
                        }

                        if (oldHomeRes == tipH) {
                            // richtiges Unentschieden
                            tipScore -= TIPSCORE_FOR_CORRECT_DRAW;
                        } else {
                            // "falsches" Unentschieden
                            tipScore -= TIPSCORE_FOR_INCORRECT_DRAW;
                        }
                    } else {
                        // Joker passt nicht...
                        if (joker) {
                            tipScore -= TIPSCORE_FOR_INCORRECT_JOKER;
                        } else if (deluxeJoker) {
                            tipScore -= TIPSCORE_FOR_INCORRECT_DELUXE_JOKER;
                        }
                    }
                }

                if (newHomeRes != NO_RESULT) {
                    if ((newDiff < 0 && tipDiff < 0) || (newDiff > 0 && tipDiff > 0)) {
                        // Joker passt...
                        if (joker) {
                            tipScore += TIPSCORE_FOR_CORRECT_JOKER;
                        } else if (deluxeJoker) {
                            tipScore += TIPSCORE_FOR_CORRECT_DELUXE_JOKER;
                        }

                        if (newDiff == tipDiff) {
                            if (newHomeRes == tipH) {
                                // richtiges Ergebnis
                                tipScore += TIPSCORE_FOR_CORRECT_TIP + Math.abs(newDiff);
                            } else {
                                // richtige Differenz
                                tipScore += TIPSCORE_FOR_DIFFERENCE + Math.abs(newDiff);
                            }
                        } else {
                            // richtige Tendenz
                            tipScore += TIPSCORE_FOR_TENDENCY;
                        }
                    } else if (newDiff == tipDiff) {
                        // Joker passt...
                        if (joker) {
                            tipScore += TIPSCORE_FOR_CORRECT_JOKER;
                        } else if (deluxeJoker) {
                            tipScore += TIPSCORE_FOR_CORRECT_DELUXE_JOKER;
                        }

                        if (newHomeRes == tipH) {
                            // richtiges Unentschieden
                            tipScore += TIPSCORE_FOR_CORRECT_DRAW;
                        } else {
                            // "falsches" Unentschieden
                            tipScore += TIPSCORE_FOR_INCORRECT_DRAW;
                        }
                    } else {
                        // Joker passt nicht...
                        if (joker) {
                            tipScore += TIPSCORE_FOR_INCORRECT_JOKER;
                        } else if (deluxeJoker) {
                            tipScore += TIPSCORE_FOR_INCORRECT_DELUXE_JOKER;
                        }
                    }
                }

                // die Punkte werden geschrieben
                user.setScore(tipScore, user.getTablescore());
            }

            matching.close();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#printUserTableInHTML(java.io.File)
     */
    public void printUserTableInHTML(File file) throws BlTipException {
        Printer printer = new Printer();
        int round = this.getFirstRoundWithoutResults() - 1;
        printer.printHTMLSite("Tipptabelle, " + round + ". Spieltag", "Bundesligatipp 2018/2019 - " + round + ". " +
                        "Spieltag",
                printer.getHTMLUserTable(this.getUsertable()), file);
    }

    /*
     * (non-Javadoc)
     * 
     * @see bltipp.storage.StorageSystem#printAllTiptables(java.io.File)
     */
    public void printAllTiptables(File file) throws BlTipException {
        Printer printer = new Printer();
        printer.printAllTiptables(this, file);
    }

    /**
     * Liefert den Namen eines Tippers mit der �bergebenen ID. Ist kein Tipper unter dieser ID
     * gespeichert, wird <code>null</code> geliefert, ebenso bei Fehlern.
     *
     * @param id ID des Tippers.
     * @return Der Name des Tippers, wird kein Tipper unter der ID gefunden, wird
     * <code>null<code> geliefert, genauso bei Fehlern.
     * @throws BlTipException Bei Datenbankfehlern
     */
    public String getUsername(int id) throws BlTipException {
        try {
            String getUsername = "SELECT " + USER_NAME + " FROM " + DBTABLE_USER + " WHERE " + USER_ID + "=" + id + ";";

            if (WRITE_STMTS)
                wlnStmt(getUsername);
            ResultSet result = stmt.executeQuery(getUsername);
            if (result.next())
                return result.getString(USER_NAME);

            result.close();
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }

        return null;
    }

    /**
     * Liefert die nach den Tipps des Tippers mit der �bergebenen ID berechneten Tabelle in
     * einem Array von Mannschaften, in dem im Feld <code>0</code> die beste Mannschaft
     * gespeichert ist.
     *
     * @param userID ID des Users, dessen Tabelle berechnet werden soll.
     * @return Tabelle der Mannschaften, niemals <code>null</code>.
     * @throws BlTipException Bei Datenbankfehlern
     * @see #getTeamsWithPointsAndGoals(int)
     */
    public Team[] calculateTableOfUser(int userID) throws BlTipException {
        return this.sort(this.getTeamsWithPointsAndGoals(userID));
    }

    /**
     * Sortiert die Mannschaften nach folgenden Kriterien:
     * <ul>
     * <li>1. Punkte</li>
     * <li>2. Tordifferenz</li>
     * <li>3. Geschossene Tore</li>
     * <li>4. Direkter Vergleich (derzeit nicht implementiert)</li>
     * </ul>
     *
     * @param teams Feld unsortierter Mannschaften, die �bergabe von <code>null</code> f�hrt zu
     *              einer <code>NullPointerException</code>.
     * @return Feld sortierter Mannschaften
     */
    private Team[] sort(Team[] teams) {
        boolean changed;
        Team helpMe;

        do {
            changed = false;
            for (int i = 0; i < teams.length - 1; i++) {
                if (teams[i].getScore() < teams[i + 1].getScore()) {
                    helpMe = teams[i];
                    teams[i] = teams[i + 1];
                    teams[i + 1] = helpMe;
                    changed = true;
                } else if (teams[i].getScore() == teams[i + 1].getScore()) {

                    // Gleiche Punktzahl
                    if (teams[i].getDifference() < teams[i + 1].getDifference()) {
                        helpMe = teams[i];
                        teams[i] = teams[i + 1];
                        teams[i + 1] = helpMe;
                        changed = true;
                    } else if (teams[i].getDifference() == teams[i + 1].getDifference()) {

                        // Gleiche Tordifferenz
                        if (teams[i].getGoals() < teams[i + 1].getGoals()) {
                            helpMe = teams[i];
                            teams[i] = teams[i + 1];
                            teams[i + 1] = helpMe;
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);

        return teams;
    }

    /**
     * Gibt ein sortiertes Feld der Tipper zur�ck. Die Tipper werden hierbei nach ihren
     * Gesamtpunkten sortiert, sind zwei punktgleich, so stehen sie auf demselben
     * Tabellenplatz.
     *
     * @param user Array unsortierter Tipper, die �bergabe von <code>null</code> f�hrt zu einer
     *             <code>NullPointerException</code>.
     */
    private void sort(User[] user) {
        boolean changed;
        User helpMe;

        do {
            changed = false;
            for (int i = 0; i < user.length - 1; i++) {
                if (user[i].getScore() < user[i + 1].getScore()) {
                    helpMe = user[i];
                    user[i] = user[i + 1];
                    user[i + 1] = helpMe;
                    changed = true;
                }
            }
        } while (changed);
    }

    /**
     * Liefert die fast fertige getippte Tabelle eines Tippers, nur m�ssen die Mannschaften
     * noch sortiert werden (<code>#sortTeams(Team[]</code>).
     *
     * @param userID ID des Tippers, dessen Tipps zur Berechnung genommen werden.
     * @return Array von Mannschaften, die Punkte und Tore enthalten, bei Fehlern (bsplw.
     * nichts in der Datenbank) <code>null</code>.
     */
    private Team[] getTeamsWithPointsAndGoals(int userID) throws BlTipException {
        try {
            Team[] teams = getBLTable();

            for (Team team : teams) {
                int hwins = 0, hremis = 0, hloses = 0;
                int gwins = 0, gremis = 0, gloses = 0;
                int hgoals = 0, hgoalsAgainst = 0, ggoals = 0, ggoalsAgainst = 0;

                // Heimsiege
                String sqlString = "SELECT " + COUNT_OF_ALL + " FROM " + DBTABLE_GAMES + " NATURAL JOIN " + DBTABLE_TIPS
                        + " WHERE " + USER_ID + "=" + userID + " AND " + GAMES_HOMETEAM + "='"
                        + BlTipUtility.maskToSQL(team.getName()) + "'" + " AND " + TIP_HOME + ">" + TIP_GUEST + ";";

                wlnStmt("sqlString: " + sqlString);
                ResultSet result = stmt.executeQuery(sqlString);
                if (result.next()) {
                    hwins = result.getInt(COUNT_OF_ALL);
                }
                // Heimunentschieden
                sqlString = "SELECT " + COUNT_OF_ALL + " FROM " + DBTABLE_GAMES + " NATURAL JOIN " + DBTABLE_TIPS + "" +
                        " WHERE "
                        + USER_ID + "=" + userID + " AND " + GAMES_HOMETEAM + "='" + BlTipUtility.maskToSQL(team
                        .getName())
                        + "'" + " AND " + TIP_HOME + "=" + TIP_GUEST + ";";

                wlnStmt("sqlString: " + sqlString);
                result = stmt.executeQuery(sqlString);
                if (result.next()) {
                    hremis = result.getInt(COUNT_OF_ALL);
                }
                // Heimniederlagen
                sqlString = "SELECT " + COUNT_OF_ALL + " FROM " + DBTABLE_GAMES + " NATURAL JOIN " + DBTABLE_TIPS + "" +
                        " WHERE "
                        + USER_ID + "=" + userID + " AND " + GAMES_HOMETEAM + "='" + BlTipUtility.maskToSQL(team
                        .getName())
                        + "'" + " AND " + TIP_HOME + "<" + TIP_GUEST + ";";

                wlnStmt("sqlString: " + sqlString);
                result = stmt.executeQuery(sqlString);
                if (result.next()) {
                    hloses = result.getInt(COUNT_OF_ALL);
                }

                // Tore und Gegentore
                sqlString = "SELECT SUM(" + TIP_HOME + ")," + " SUM(" + TIP_GUEST + ")" + " FROM " + DBTABLE_GAMES
                        + " NATURAL JOIN " + DBTABLE_TIPS + " WHERE " + USER_ID + "=" + userID + " AND " +
                        GAMES_HOMETEAM + "='"
                        + BlTipUtility.maskToSQL(team.getName()) + "';";

                wlnStmt("sqlString: " + sqlString);
                result = stmt.executeQuery(sqlString);
                if (result.next()) {
                    hgoals = result.getInt("SUM(" + TIP_HOME + ")");
                    hgoalsAgainst = result.getInt("SUM(" + TIP_GUEST + ")");
                }

                // Ausw�rtssiege
                sqlString = "SELECT " + COUNT_OF_ALL + " FROM " + DBTABLE_GAMES + " NATURAL JOIN " + DBTABLE_TIPS + "" +
                        " WHERE "
                        + USER_ID + "=" + userID + " AND " + GAMES_GUESTTEAM + "='" + BlTipUtility.maskToSQL(team
                        .getName())
                        + "'" + " AND " + TIP_HOME + "<" + TIP_GUEST + ";";

                wlnStmt("sqlString: " + sqlString);
                result = stmt.executeQuery(sqlString);
                if (result.next()) {
                    gwins = result.getInt(COUNT_OF_ALL);
                }
                // Ausw�rtsunentschieden
                sqlString = "SELECT " + COUNT_OF_ALL + " FROM " + DBTABLE_GAMES + " NATURAL JOIN " + DBTABLE_TIPS + "" +
                        " WHERE "
                        + USER_ID + "=" + userID + " AND " + GAMES_GUESTTEAM + "='" + BlTipUtility.maskToSQL(team
                        .getName())
                        + "'" + " AND " + TIP_HOME + "=" + TIP_GUEST + ";";

                wlnStmt("sqlString: " + sqlString);
                result = stmt.executeQuery(sqlString);
                if (result.next()) {
                    gremis = result.getInt(COUNT_OF_ALL);
                }
                // Ausw�rtsniederlagen
                sqlString = "SELECT " + COUNT_OF_ALL + " FROM " + DBTABLE_GAMES + " NATURAL JOIN " + DBTABLE_TIPS + "" +
                        " WHERE "
                        + USER_ID + "=" + userID + " AND " + GAMES_GUESTTEAM + "='" + BlTipUtility.maskToSQL(team
                        .getName())
                        + "'" + " AND " + TIP_HOME + ">" + TIP_GUEST + ";";

                wlnStmt("sqlString: " + sqlString);
                result = stmt.executeQuery(sqlString);
                if (result.next()) {
                    gloses = result.getInt(COUNT_OF_ALL);
                }

                // Tore und Gegentore
                sqlString = "SELECT SUM(" + TIP_HOME + ")," + " SUM(" + TIP_GUEST + ")" + " FROM " + DBTABLE_GAMES
                        + " NATURAL JOIN " + DBTABLE_TIPS + " WHERE " + USER_ID + "=" + userID + " AND " +
                        GAMES_GUESTTEAM + "='"
                        + BlTipUtility.maskToSQL(team.getName()) + "';";

                wlnStmt("sql_string: " + sqlString);
                result = stmt.executeQuery(sqlString);
                if (result.next()) {
                    ggoals = result.getInt("SUM(" + TIP_GUEST + ")");
                    ggoalsAgainst = result.getInt("SUM(" + TIP_HOME + ")");
                }

                result.close();

                team.setWinsRemisLoses(hwins, hremis, hloses, gwins, gremis, gloses);
                team.setGoals(hgoals, ggoals);
                team.setGoalsAgainst(hgoalsAgainst, ggoalsAgainst);
            }

            return teams;
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }

        return null;
    }

    /**
     * Berechnet die Tabellenpunkte <b>aller</b> Tipper. Die bisherigen Tabellenpunkte werden
     * auf <code>0</code> gesetzt und die neuen in die Objekte der <code>HashMap</code>
     * gespeichert.
     *
     * @param allUser Alle User in einem Hash, referenzierbar �ber die User-ID
     * @param teams   Die Bundesligatabelle.
     * @throws BlTipException Bei Datenbankfehlern
     */
    private void calculateTableScores(Map<Integer, User> allUser, Team[] teams) throws BlTipException {

        int tablescore = NO_SCORES;
        User user = null;

        try {
            String getUsertables = "SELECT " + USER_ID + ", " + TIPTABLES_POSITION + ", " +
                    "" + TIPTABLES_TEAMNAME + " FROM "
                    + DBTABLE_TIPTABLES + ";";

            wlnStmt(getUsertables);

            ResultSet tables = this.stmt.executeQuery(getUsertables);
            while (tables.next()) {
                int userID = tables.getInt(USER_ID);
                if (user == null || user.getId() != userID) {
                    user = allUser.get(userID);
                    tablescore = NO_SCORES;
                }

                int pos = getPosition(tables.getString(TIPTABLES_TEAMNAME), teams);
                int posTip = tables.getInt(TIPTABLES_POSITION);

                tablescore += TABLESCORE_FOR_RIGHT_POSITION - Math.abs(pos - posTip);

                // Meister
                if (pos == CHAMPION_PLACE && posTip == CHAMPION_PLACE) {
                    tablescore += TABLESCORE_FOR_RIGHT_CHAMPION;
                }
                // CL-Teilnehmer
                if (CHAMPION_PLACE <= pos && pos <= LAST_CL_PLACE && CHAMPION_PLACE <= posTip && posTip <=
                        LAST_CL_PLACE) {
                    tablescore += TABLESCORE_FOR_RIGHT_CL;
                }
                // UEFA-Cup-Teilnehmer
                if (CHAMPION_PLACE <= pos && pos <= LAST_EL_PLACE && CHAMPION_PLACE <= posTip && posTip <=
                        LAST_EL_PLACE) {
                    tablescore += TABLESCORE_FOR_RIGHT_UEFACUP;
                }
                // Absteiger
                if (FIRST_DOWNSWINGER_PLACE <= pos && pos <= COUNT_OF_TEAMS && FIRST_DOWNSWINGER_PLACE <= posTip
                        && posTip <= COUNT_OF_TEAMS) {
                    tablescore += TABLESCORE_FOR_RIGHT_DOWNSWINGER;
                }

                // Setzen der Punkte
                if (user != null) {
                    user.setScore(user.getTipscore(), tablescore);
                }
            }
        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }
    }

    private int getPosition(String name, Team[] teams) {
        for (int i = 0; i < teams.length; i++) {
            if (name.equals(teams[i].getName()))
                return (i + 1);
        }

        return Constants.NO_POSITION;
    }

    /**
     * Regelt das Exception-Handling
     *
     * @param e     Tats�chlich aufgetretene Ausnahme
     * @param title Titel des Fehlerfensters
     * @param msg   Nachricht im Fehlerfenster
     * @throws BlTipException Bei Datenbankfehlern
     */
    private void handle(Exception e, String title, String msg) throws BlTipException {
        if (e != null) {
            e.printStackTrace();
            throw new BlTipException(title, msg);
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
            System.out.println(BlTipUtility.currentTime() + "\tDatabase\t\t" + str);
        }
    }

    /**
     * Methode zur Ausgabe von SQL-Statements �ber den <code>System.out.println</code> -Stream
     *
     * @param str Statusmeldung
     */
    private void wlnStmt(String str) {
        if (WRITE_STMTS) {
            System.out.println("SQL-Statement\t\tDatabase\t\t" + str);
        }
    }
}
