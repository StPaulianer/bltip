package bltip.storage.db;

import bltip.common.Constants;

/**
 * Dieses Interface b�ndelt Tabellen- und Spaltennamen der DB-Tabellen sowie Statements zum
 * Erstellen der DB.
 *
 * @author Nico
 * @version 28.08.2006
 */
public class DBTablesAndStatements extends Constants {

    /*
     * **************************************************************************
     * *
     * 
     * Allgemeine Konstanten
     * 
     * 
     * **************************************************************************
     */

    /**
     * SQL-Statement zur Abfrage der Gesamtanzahl
     */
    public final static String COUNT_OF_ALL = "COUNT(*)";
    /**
     * SQL-null
     */
    public final static String SQL_NULL = "NULL";

    /*
     * **************************************************************************
     * *
     * 
     * Tabellennamen
     * 
     * 
     * **************************************************************************
     */

    /**
     * Tabellenname der Tabelle f�r Paarungen
     */
    public final static String DBTABLE_GAMES = "game";
    /**
     * Tabellenname der Tabelle f�r User
     */
    public final static String DBTABLE_USER = "user";
    /**
     * Tabellenname der Tabelle f�r Mannschaften
     */
    public final static String DBTABLE_TEAMS = "team";
    /**
     * Tabellenname der Tabelle f�r die Tipps
     */
    public final static String DBTABLE_TIPS = "tip";
    /**
     * Tabellenname der Tabelle f�r die Tipptabellen
     */
    public final static String DBTABLE_TIPTABLES = "usertable";

    /*
     * **************************************************************************
     * *
     * 
     * Spaltennamen f�r die Tabelle "Paarungen"
     * 
     * 
     * **************************************************************************
     */

    /**
     * Eindeutiger Schl�ssel der Paarung
     */
    public static final String GAMES_ID = "gameID";
    /**
     * Spieltag
     */
    public static final String GAMES_ROUND = "round";
    /**
     * ID der Heimmannschaft
     */
    public static final String GAMES_HOMETEAM = "home";
    /**
     * ID der Ausw�rtsmannschaft
     */
    public static final String GAMES_GUESTTEAM = "guest";
    /**
     * Heimergebnis
     */
    public static final String GAMES_HOMERESULT = "hresult";
    /**
     * Ausw�rtsergebnis
     */
    public static final String GAMES_GUESTRESULT = "gresult";

    /*
     * **************************************************************************
     * *
     * 
     * Spaltennamen f�r die Tabelle "User"
     * 
     * 
     * **************************************************************************
     */

    /**
     * Eindeutiger Schl�ssel des Tippers
     */
    public static final String USER_ID = "userID";
    /**
     * Name des Tippers
     */
    public static final String USER_NAME = "name";
    /**
     * Tipppunkte
     */
    public static final String USER_TIPSCORE = "tipscore";
    /**
     * Tabellenpunkte
     */
    public static final String USER_TABLESCORE = "tabscore";
    /**
     * Punkte fuer Bonusfragen
     */
    public static final String USER_EXTRASCORE = "extrascore";

    /*
     * **************************************************************************
     * *
     * 
     * Spaltennamen f�r die Tabelle "Mannschaften"
     * 
     * 
     * **************************************************************************
     */
    /**
     * Name der Mannschaft
     */
    public final static String TEAMS_NAME = "name";
    /**
     * Anzahl der Heimsiege
     */
    public static final String TEAMS_HWINS = "hwins";
    /**
     * Anzahl der Heimunentschieden
     */
    public static final String TEAMS_HREMIS = "hremis";
    /**
     * Anzahl der Heimniederlagen
     */
    public static final String TEAMS_HLOSES = "hloses";
    /**
     * Anzahl der Ausw�rtssiege
     */
    public static final String TEAMS_GWINS = "gwins";
    /**
     * Anzahl der Ausw�rtsunentschieden
     */
    public static final String TEAMS_GREMIS = "gremis";
    /**
     * Anzahl der Ausw�rtsniederlagen
     */
    public static final String TEAMS_GLOSES = "gloses";
    /**
     * Anzahl der geschossenen Heimtore
     */
    public static final String TEAMS_HGOALS = "hgoals";
    /**
     * Anzahl der geschossenen Ausw�rtstore
     */
    public static final String TEAMS_GGOALS = "ggoals";
    /**
     * Anzahl der Heimgegentore
     */
    public static final String TEAMS_HGOALSAG = "hgoalsA";
    /**
     * Anzahl der Ausw�rtsgegentore
     */
    public static final String TEAMS_GGOALSAG = "ggoalsA";
    /**
     * Heimergebnis
     */
    public static final String TIP_HOME = "hometip";

    /*
     * **************************************************************************
     * *
     * 
     * Spaltennamen f�r die Tabelle "Tipps"
     * 
     * 
     * **************************************************************************
     */
    /**
     * Ausw�rtsergebnis
     */
    public static final String TIP_GUEST = "guesttip";
    /**
     * Joker?
     */
    public static final String TIP_JOKER = "joker";
    /**
     * Deluxe-Joker?
     */
    public static final String TIP_DELUXE_JOKER = "deluxeJoker";
    /**
     * Zuordnung zum Mannschaftsnamen
     */
    public static final String TIPTABLES_TEAMNAME = TEAMS_NAME;
    /**
     * Getippte Platzierung (aus Performance-Gr�nden trotz Redundanz)
     */
    public static final String TIPTABLES_POSITION = "tipPos";
    /**
     * Die getippte Anzahl der Heimsiege
     */
    public static final String TIPTABLES_HWINS = "tipHwins";

    /*
     * **************************************************************************
     * *
     * 
     * Spaltennamen f�r die Tabelle "Usertabellen"
     * 
     * 
     * **************************************************************************
     */
    /**
     * Die getippte Anzahl der Heimunentschieden
     */
    public static final String TIPTABLES_HREMIS = "tipHremis";
    /**
     * Die getippte Anzahl der Heimniederlagen
     */
    public static final String TIPTABLES_HLOSES = "tipHoses";
    /**
     * Die getippte Anzahl der Ausw�rtssiege
     */
    public static final String TIPTABLES_GWINS = "tipGwins";
    /**
     * Die getippte Anzahl der Ausw�rtsunentschieden
     */
    public static final String TIPTABLES_GREMIS = "tipGremis";
    /**
     * Die getippte Anzahl der Ausw�rtsniederlagen
     */
    public static final String TIPTABLES_GLOSES = "tipGloses";
    /**
     * Die getippte Anzahl der geschossenen Heimtore
     */
    public static final String TIPTABLES_HGOALS = "tipHg";
    /**
     * Die getippte Anzahl der geschossenen Ausw�rtstore
     */
    public static final String TIPTABLES_GGOALS = "tipGg";
    /**
     * Die getippte Anzahl der Heimgegentore
     */
    public static final String TIPTABLES_HGOALS_AG = "tipHgA";
    /**
     * Die getippte Anzahl der Ausw�rtsgegentore
     */
    public static final String TIPTABLES_GGOALS_AG = "tipGgA";
    /**
     * SQL-Befehl zur Erstellung der Tabelle "Paarungen"
     */
    public static final String CREATE_GAME_TABLE = "CREATE TABLE IF NOT EXISTS " + DBTABLE_GAMES + " (" + GAMES_ID
            + " BIGINT NOT NULL AUTO_INCREMENT, " + GAMES_ROUND + " INT NOT NULL, " + GAMES_HOMETEAM + " CHAR(" + LENGTH_TEAMNAME
            + ") REFERENCES " + DBTABLE_TEAMS + "." + TEAMS_NAME + " ON DELETE CASCADE ON UPDATE CASCADE, " + GAMES_GUESTTEAM
            + " CHAR(" + LENGTH_TEAMNAME + ") REFERENCES " + DBTABLE_TEAMS + "." + TEAMS_NAME
            + " ON DELETE CASCADE ON UPDATE CASCADE, " + GAMES_HOMERESULT + " INT DEFAULT " + NO_RESULT + ", "
            + GAMES_GUESTRESULT + " INT DEFAULT " + NO_RESULT + ", " + " PRIMARY KEY (" + GAMES_ID + "), " + " UNIQUE("
            + GAMES_ROUND + "," + GAMES_HOMETEAM + "," + GAMES_GUESTTEAM + "));";
    /**
     * SQL-Befehl zur Erstellung der Tabelle "User"
     */
    public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + DBTABLE_USER + " ("
            + USER_ID + " BIGINT NOT NULL AUTO_INCREMENT, "
            + USER_NAME + " CHAR(" + LENGTH_USERNAME + ") NOT NULL, "
            + USER_TIPSCORE + " INT DEFAULT " + NO_SCORES + ", "
            + USER_TABLESCORE + " INT DEFAULT " + NO_SCORES + ", "
            + USER_EXTRASCORE + " INT DEFAULT " + NO_SCORES + ", "
            + " PRIMARY KEY (" + USER_ID + "));";
    /**
     * Eindeutiger Schl�ssel der Mannschaft
     */
    private final static String TEAMS_ID = "teamID";
    /**
     * SQL-Befehl zur Erstellung der Tabelle "Mannschaften"
     */
    public static final String CREATE_TEAM_TABLE = "CREATE TABLE IF NOT EXISTS " + DBTABLE_TEAMS + " (" + TEAMS_ID
            + " BIGINT NOT NULL AUTO_INCREMENT, " + TEAMS_NAME + " CHAR(" + LENGTH_TEAMNAME + "), " + TEAMS_HWINS
            + " INT DEFAULT " + NO_SCORES + ", " + TEAMS_HREMIS + " INT DEFAULT " + NO_SCORES + ", " + TEAMS_HLOSES
            + " INT DEFAULT " + NO_SCORES + ", " + TEAMS_GWINS + " INT DEFAULT " + NO_SCORES + ", " + TEAMS_GREMIS
            + " INT DEFAULT " + NO_SCORES + ", " + TEAMS_GLOSES + " INT DEFAULT " + NO_SCORES + ", " + TEAMS_HGOALS
            + " INT DEFAULT " + NO_SCORES + ", " + TEAMS_GGOALS + " INT DEFAULT " + NO_SCORES + ", " + TEAMS_HGOALSAG
            + " INT DEFAULT " + NO_SCORES + ", " + TEAMS_GGOALSAG + " INT DEFAULT " + NO_SCORES + ", " + " PRIMARY KEY ("
            + TEAMS_ID + ")," + " UNIQUE(" + TEAMS_NAME + "));";

    /*
     * **************************************************************************
     * *
     * 
     * Statements zum Erstellen der Tabellen
     * 
     * 
     * **************************************************************************
     */
    /**
     * Zuordnung von Tipp zum Tipper
     */
    private static final String TIP_USER = USER_ID;
    /**
     * Zuordnung von Tipp zum Spiel
     */
    private static final String TIP_GAME = GAMES_ID;
    /**
     * SQL-Befehl zur Erstellung der Tabelle "Tipps"
     */
    public static final String CREATE_TIP_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DBTABLE_TIPS + " ("
                    + TIP_USER + " BIGINT NOT NULL REFERENCES " + DBTABLE_USER + "." + USER_ID + " ON DELETE CASCADE ON UPDATE CASCADE, "
                    + TIP_GAME + " BIGINT NOT NULL REFERENCES " + DBTABLE_GAMES + "." + GAMES_ID + " ON DELETE CASCADE ON UPDATE CASCADE, "
                    + TIP_HOME + " INT DEFAULT " + NO_RESULT + ", "
                    + TIP_GUEST + " INT DEFAULT " + NO_RESULT + ", "
                    + TIP_JOKER + " BOOL, "
                    + TIP_DELUXE_JOKER + " BOOL, "
                    + " PRIMARY KEY (" + TIP_USER + ", " + TIP_GAME + "));";
    /**
     * Zuordnung zum Tipper
     */
    private static final String TIPTABLES_USER = USER_ID;
    /**
     * SQL-Befehl zum Erstellen der User-Tipptabellen-Tabelle
     */
    public static final String CREATE_USER_TIPTABLES_TABLE = "CREATE TABLE IF NOT EXISTS " + DBTABLE_TIPTABLES + " ("
            + TIPTABLES_USER + " BIGINT NOT NULL REFERENCES " + DBTABLE_USER + "." + USER_ID
            + " ON DELETE CASCADE ON UPDATE CASCADE, " + TIPTABLES_TEAMNAME + " CHAR(" + LENGTH_TEAMNAME
            + ") NOT NULL REFERENCES " + DBTABLE_TEAMS + "." + TEAMS_NAME + " ON DELETE CASCADE ON UPDATE CASCADE, "
            + TIPTABLES_POSITION + " INT DEFAULT " + NO_POSITION + ", " + TIPTABLES_HWINS + " INT DEFAULT " + NO_SCORES + ", "
            + TIPTABLES_HREMIS + " INT DEFAULT " + NO_SCORES + ", " + TIPTABLES_HLOSES + " INT DEFAULT " + NO_SCORES + ", "
            + TIPTABLES_GWINS + " INT DEFAULT " + NO_SCORES + ", " + TIPTABLES_GREMIS + " INT DEFAULT " + NO_SCORES + ", "
            + TIPTABLES_GLOSES + " INT DEFAULT " + NO_SCORES + ", " + TIPTABLES_HGOALS + " INT DEFAULT " + NO_SCORES + ", "
            + TIPTABLES_GGOALS + " INT DEFAULT " + NO_SCORES + ", " + TIPTABLES_HGOALS_AG + " INT DEFAULT " + NO_SCORES + ", "
            + TIPTABLES_GGOALS_AG + " INT DEFAULT " + NO_SCORES + ", " + " PRIMARY KEY (" + USER_ID + ", " + TEAMS_NAME + "));";
}