package bltip.storage.db;

import bltip.common.BlTipException;
import bltip.common.Constants;
import bltip.gui.Messages;
import bltip.model.Team;
import bltip.model.Tip;
import bltip.model.User;
import bltip.util.BlTipUtility;

import java.io.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasse zum Erstellen der initialen DB-Inhalten
 *
 * @author Nico
 */
class DbFiller {

    private final static boolean DEBUG = true;
    private static final String GAME_PATTERN = "([\\w|\\W|\\d|\\.|\\s]*)-([\\w|\\W|\\d|\\.|\\s]*)";
    private static final Pattern TIPP_PATTERN = Pattern.compile("(\\d+):(\\d+)\\s*([xX])?([xX])?");
    private final DBInserter dbi;
    private final Database db;

    /**
     * Konstruktor l�dt die Properties und verbindet sich zur Datenbank
     *
     * @param db   Stellt sinnvolle Methoden zur Verf�gung
     * @param conn Verbindung zur Datenbank, die �bergabe von <code>null</code> f�hrt zu einer
     *             <code>NullPointerException</code>
     * @throws BlTipException Bei Datenbank- oder I/O-Fehlern
     */
    DbFiller(Database db, Connection conn) throws BlTipException {
        this.db = db;
        this.dbi = new DBInserter(conn);
    }

    /**
     * Was soll ich sage, die Methode f�llt alles...
     *
     * @param games    Datei, in der die Paarungen stehen
     * @param user     Datei, in der die Namen der Tipper stehen
     * @param tipfiles <b>Verzeichnis</b>, in dem die Tipps liegen, jeweils <Tippername>.txt
     * @throws BlTipException Bei Datenbank- oder I/O-Fehlern
     */
    public void fill(File games, File user, File tipfiles) throws BlTipException {
        wln("filling games");
        Map<Integer, String[]> gamesMap = fillGames(games);

        wln("filling user");
        fillUser(user);

        wln("filling tips");
        fillTips(tipfiles, gamesMap);

        wln("filling tiptables");
        fillTiptables();

        wln("done");
    }

    /**
     * F�llt die DB-Tabellen "Paarungen" und "Mannschaften" mit den entsprechenden Daten, die
     * in einer Text-Datei vorliegen und zwar in folgender Form (Leerzeilen d�rfen drin sein):<br>
     * <br>
     * ********************************************************************<br>
     * 1. Spieltag 09.08.2002 - 11.08.2002<br>
     * Borussia Dortmund - Hertha BSC Berlin<br>
     * Energie Cottbus - Bayer Leverkusen<br>
     * 1. FC N�rnberg - VfL Bochum<br>
     * 1860 M�nchen - Hansa Rostock<br>
     * Hamburger SV - Hannover 96<br>
     * Arminia Bielefeld - Werder Bremen<br>
     * Borussia M'gladbach - Bayern M�nchen<br>
     * Schalke 04 - VfL Wolfsburg<br>
     * VfB Stuttgart - 1. FC K'lautern<br>
     * <br>
     * 2. Spieltag 17.08. - 18.08.2002<br>
     * ...<br>
     * ********************************************************************
     *
     * @param games Datei, in der die Paarungen stehen
     * @throws BlTipException Bei Datenbank- oder I/O-Fehlern
     */
    private Map<Integer, String[]> fillGames(File games) throws BlTipException {
        Map<Integer, String[]> gamesMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(games)))) {
            String line;
            int round = -1;

            final Pattern roundPattern = Pattern.compile("([1-9][0-9]?)\\.\\s*Spieltag\\s*");
            final Pattern gamePattern = Pattern.compile(GAME_PATTERN);
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    final Matcher roundMatcher = roundPattern.matcher(line);
                    final Matcher gameMatcher = gamePattern.matcher(line);
                    if (roundMatcher.matches()) {
                        round = Integer.parseInt(roundMatcher.group(1));
                    } else if (gameMatcher.matches()) {
                        final String home = gameMatcher.group(1).trim();
                        final String guest = gameMatcher.group(2).trim();
                        if (round == 1) {
                            dbi.insertTeamIntoDB(home);
                            dbi.insertTeamIntoDB(guest);
                        }

                        int id = dbi.insertGameIntoDB(round, home, guest);
                        gamesMap.put(id, new String[]{home, guest});
                    } else {
                        wln("Unbekannte Zeile in Paarungen: " + line);
                    }
                }
            }
        } catch (IOException e) {
            handle(e, e.getMessage());
        }
        return gamesMap;
    }

    private void close(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            // ignore
        }
    }

    /**
     * F�llt die DB-Tabelle "User" mit den entsprechenden Daten, die in einer Text-Datei
     * vorliegen und zwar in folgender Form (Leerzeilen sind erlaubt):<br>
     * <br>
     * ********************************************************************<br>
     * Nico Mischok<br>
     * Holger Gohlke<br>
     * Rainer Weiss<br>
     * Daniel Nuess<br>
     * ...<br>
     * ********************************************************************
     *
     * @param user Datei, in der die Namen der Tipper stehen
     * @throws BlTipException Bei Datenbank- oder I/O-Fehlern
     */
    private void fillUser(File user) throws BlTipException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(user)));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    // Einf�gen in die DB
                    dbi.insertUserIntoDB(line.trim());
                }
            }

            reader.close();
        } catch (IOException e) {
            handle(e, e.getMessage());
        } finally {
            close(reader);
        }
    }

    /**
     * F�llt die DB-Tabelle "User" mit den entsprechenden Daten. Dazu wird ein Ordner
     * �bergeben, in dem f�r jeden User eine Textdatei liegt ('Tippername'.txt), in der die
     * Tipps in folgender Form vorliegen (Leerzeilen sind auch hier erlaubt):<br>
     * <br>
     * ********************************************************************<br>
     * 'Tippernamenk�rzel'<br>
     * 2:0<br>
     * 1:0<br>
     * 1:1<br>
     * 2:1<br>
     * 1:3<br>
     * 2:2<br>
     * 1:0<br>
     * 1:1<br>
     * 1:0<br>
     * <br>
     * 'Tippernamenk�rzel'<br>
     * 2:0<br>
     * ...<br>
     * ********************************************************************
     *
     * @param tipfiles <b>Verzeichnis</b>, in dem die Dateien mit den Tipps liegen (jeweils
     *                 Tippername.txt)
     * @param gamesMap map of games
     * @throws BlTipException Bei Datenbank- oder I/O-Fehlern
     */
    private void fillTips(File tipfiles, Map<Integer, String[]> gamesMap) throws BlTipException {
        int countOfUser = db.getCountOfUser();
        for (int userId = 1; userId <= countOfUser; userId++) {
            final String username = db.getUsername(userId);
            final File userfile = new File(tipfiles + File.separator + username + ".txt");
            wln("reading tips from " + userfile.getName());


            try (FileInputStream in = new FileInputStream(userfile);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                fillTipsForUser(userId, userfile, reader, gamesMap);
            } catch (IOException e) {
                handle(e, e.getMessage());
            }
        }
    }

    private void fillTipsForUser(int tipperId, File userfile, BufferedReader reader, Map<Integer, String[]> gamesMap) throws IOException,
            BlTipException {
        // erste Paarung (hat in der DB auch 1)
        int gameNo = 1;
        int jokerPerRound = 0;
        int deluxeJokerPerRound = 0;

        String line;
        while (true) {
            line = reader.readLine();
            if (line == null) {
                break;
            }

            final Matcher matcher = TIPP_PATTERN.matcher(line);
            if (line.trim().length() > 0) {
                if (!matcher.matches()) {
                    throw new IllegalStateException("Fehler in " + userfile + ", unbekannte Zeile: " + line);
                } else {
                    final int homeTip = Integer.parseInt(matcher.group(1));
                    final int awayTip = Integer.parseInt(matcher.group(2));
                    final boolean joker = matcher.group(3) != null;
                    final boolean deluxeJoker = matcher.group(4) != null;
                    dbi.insertTipIntoDB(tipperId, gameNo, new Tip(homeTip, awayTip, Tip.TipType.of(joker, deluxeJoker)));
                    if (deluxeJoker) {
                        deluxeJokerPerRound++;
                        String[] homeAndAway = gamesMap.get(gameNo);
                        validateDeluxeJokerNotOnBayern(userfile, homeTip, awayTip, homeAndAway, gameNo);
                    } else if (joker) {
                        jokerPerRound++;
                    }
                    if (gameNo % Constants.COUNT_OF_GAMES_PER_ROUND == 0) {
                        validateJoker(userfile, jokerPerRound, deluxeJokerPerRound, gameNo);
                        jokerPerRound = 0;
                        deluxeJokerPerRound = 0;
                    }

                    gameNo++;
                }
            }
        }

        validateCountOfTips(gameNo - 1, userfile);
    }

    private void validateDeluxeJokerNotOnBayern(File userfile, int homeTip, int awayTip, String[] homeAndAway, int gameNo) {
        int round = (gameNo + 1) / Constants.COUNT_OF_GAMES_PER_ROUND + 1;
        if ((homeTip > awayTip && homeAndAway[0].contains("Bayern"))
                || (homeTip < awayTip && homeAndAway[1].contains("Bayern"))) {
            wln("Fehler in " + userfile + ", Deluxe-Joker auf Bayern an Spieltag: " + round);
        }
    }

    private void validateJoker(File userfile, int jokerPerRound, int deluxeJokerPerRound, int gameNo) {
        int round = (gameNo + 1) / Constants.COUNT_OF_GAMES_PER_ROUND + 1;
        if (jokerPerRound == 0 && deluxeJokerPerRound == 0) {
            wln("Fehler in " + userfile + ", kein Joker gesetzt an Spieltag: " + round);
        }
        if (deluxeJokerPerRound > 1) {
            wln("Fehler in " + userfile + ", mehr als einen Deluxe-Joker gesetzt an Spieltag: " + round);
        }
    }

    private void validateCountOfTips(int gameNo, File userfile) {
        final int expectedCountOfTips = Constants.COUNT_OF_ROUNDS * Constants.COUNT_OF_GAMES_PER_ROUND;
        if (gameNo != expectedCountOfTips) {
            throw new IllegalStateException("Fehler in " + userfile + ", keine korrekte Anzahl an Tipps: " + gameNo
                    + " (erwartet: " + expectedCountOfTips + ")");
        }
    }

    /**
     * Schreibt die Tipptabellen der User in die DB.
     *
     * @throws BlTipException Bei Datenbankfehlern
     */
    private void fillTiptables() throws BlTipException {
        User[] user = db.getUsertable();
        Team[] teams;

        for (User anUser : user) {
            teams = db.calculateTableOfUser(anUser.getId());
            dbi.insertUserTableIntoDB(anUser.getId(), teams);
        }
    }

    /**
     * Regelt das Exception-Handling
     *
     * @param e   Tats�chlich aufgetretene Ausnahme
     * @param msg Nachricht im Fehlerfenster
     * @throws BlTipException Bei Datenbank- oder I/O-Fehlern
     */
    private void handle(Exception e, String msg) throws BlTipException {
        if (e != null) {
            e.printStackTrace();
            throw new BlTipException(Messages.ERRORTITLE_IO_GENERAL, msg);
        } else
            throw new BlTipException(Messages.ERRORTITLE_IO_GENERAL, msg);
    }

    /**
     * Methode zur Ausgabe von Uhrzeit + Statusmeldung �ber den <code>System.out.println</code>
     * - Stream, wenn <code>DEBUG = true</code> ist.
     *
     * @param str Statusmeldung
     */
    private void wln(String str) {
        if (DEBUG) {
            System.out.println(BlTipUtility.currentTime() + "\t" + DbFiller.class + "\t\t" + str);
        }
    }
}
