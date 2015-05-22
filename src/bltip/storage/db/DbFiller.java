package bltip.storage.db;

import bltip.common.BlTipException;
import bltip.common.Constants;
import bltip.gui.Messages;
import bltip.util.BlTipUtility;
import bltip.valueobject.Team;
import bltip.valueobject.User;

import java.io.*;
import java.sql.Connection;
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
    private static final Pattern TIPP_PATTERN = Pattern.compile("(\\d+):(\\d+)\\s*([xX])?");
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
    public DbFiller(Database db, Connection conn) throws BlTipException {
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
        fillGames(games);

        wln("filling user");
        fillUser(user);

        wln("filling tips");
        fillTips(tipfiles);

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
    private void fillGames(File games) throws BlTipException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(games)));

            String line;

            int round = 0;
            int countOfTeams = 0;

            final Pattern roundPattern = Pattern.compile("([1-9][0-9]?)\\.\\s*Spieltag\\s*");
            final Pattern gamePattern = Pattern.compile(GAME_PATTERN);
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    final Matcher roundMatcher = roundPattern.matcher(line);
                    final Matcher gameMatcher = gamePattern.matcher(line);
                    if (roundMatcher.matches()) {
                        round = Integer.parseInt(roundMatcher.group(1));
                    } else if (gameMatcher.matches()) {
                        final String heim = gameMatcher.group(1).trim();
                        final String auswaerts = gameMatcher.group(2).trim();
                        // Einf�gen der Mannschaften, falls noch nicht drin
                        // (nur einmal, deswegen die Abfrage nach ersten
                        // Spieltag)
                        if (round == 1) {
                            dbi.insertTeamIntoDB(heim);
                            countOfTeams++;
                            dbi.insertTeamIntoDB(auswaerts);
                            countOfTeams++;
                        }

                        // Einf�gen in die DB
                        dbi.insertGameIntoDB(round, heim, auswaerts);
                    } else {
                        wln("Unbekannte Zeile in Paarungen: " + line);
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            handle(e, e.getMessage());
        } finally {
            close(reader);
        }
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
     * @throws BlTipException Bei Datenbank- oder I/O-Fehlern
     */
    private void fillTips(File tipfiles) throws BlTipException {
        for (int userId = 0; userId < db.getCountOfUser(); userId++) {
            final String username = db.getUsername(userId + 1);
            final File userfile = new File(tipfiles + File.separator + username + ".txt");

            BufferedReader reader = null;
            FileInputStream in = null;
            try {
                in = new FileInputStream(userfile);
                reader = new BufferedReader(new InputStreamReader(in));
                fillTipsForUser(userId, userfile, reader);
            } catch (IOException e) {
                handle(e, e.getMessage());
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    // ignore
                }
            }

        }
    }

    private void fillTipsForUser(int tipperId, File userfile, BufferedReader reader) throws IOException,
            BlTipException {
        // erste Paarung (hat in der DB auch 1)
        int spielnr = 1;
        int jokerPerRound = 0;
        // solange Zeilen da sind, sollen sie gelesen werden
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
                    // Speichern in der DB
                    final boolean joker = insertTip(tipperId, spielnr, matcher);
                    jokerPerRound = validateJokerPerRound(spielnr, joker, jokerPerRound, userfile);
                    // n�chstes Game
                    spielnr++;
                }
            }
        }

        validateCountOfTips(spielnr - 1, userfile);
    }

    private void validateCountOfTips(int spielnr, File userfile) {
        final int expectedCountOfTips = Constants.COUNT_OF_ROUNDS * Constants.COUNT_OF_GAMES_PER_ROUND;
        if (spielnr != expectedCountOfTips) {
            throw new IllegalStateException("Fehler in " + userfile + ", keine korrekte Anzahl an Tipps: " + spielnr
                    + " (erwartet: " + expectedCountOfTips + ")");
        }
    }

    private boolean insertTip(int tipperId, int spielnr, Matcher matcher) throws BlTipException {
        final int homeTip = Integer.parseInt(matcher.group(1));
        final int awayTip = Integer.parseInt(matcher.group(2));
        final boolean joker = matcher.group(3) != null;
        dbi.insertTipIntoDB(tipperId + 1, spielnr, homeTip, awayTip, joker);
        return joker;
    }

    private int validateJokerPerRound(int spielnr, boolean joker, int jokerPerRound, File userfile) {
        if (joker) {
            jokerPerRound++;
        }
        if (spielnr % Constants.COUNT_OF_GAMES_PER_ROUND == 0) {
            if (jokerPerRound == 0) {
                throw new IllegalStateException("Fehler in " + userfile + ", " +
                        "kein Joker gesetzt an Spieltag: " + (spielnr / Constants
                        .COUNT_OF_GAMES_PER_ROUND));
            }
            jokerPerRound = 0;
        }
        return jokerPerRound;
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
