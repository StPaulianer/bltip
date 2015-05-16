package bltip.common;

/**
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 28.08.2006
 */
public class Constants {

    /** Anzahl der Gewinner von Geldpreisen */
    public final static int COUNT_OF_WINNERS = 4;

    /** Anzahl der Mannschaften der Bundesliga */
    public final static int COUNT_OF_TEAMS = 18;
    /** Anzahl der Spieltage der Bundesliga */
    public final static int COUNT_OF_ROUNDS = 34;
    /** Anzahl der Spiele an einem Spieltag */
    public final static int COUNT_OF_GAMES_PER_ROUND = 9;

    /** Anzahl der Punkte f�r einen Sieg in der Bundesliga */
    public final static int SCORE_FOR_VICTORY = 3;
    /** Anzahl der Punkte f�r ein Unentschieden in der Bundesliga */
    public final static int SCORE_FOR_DRAW = 1;
    /** Zusatzpunkte f�r einen richtigen Joker */
    public final static int TIPSCORE_FOR_CORRECT_JOKER = 5;
    /** Abzug f�r falschen Joker */
    public final static int TIPSCORE_FOR_INCORRECT_JOKER = -5;
    /** Rohpunkte f�r ein richtiges Ergebnis (+ Differenz, au�er bei Unentschieden) */
    public final static int TIPSCORE_FOR_CORRECT_TIP = 2;
    /** Rohpunkte f�r eine richtige Differenz (+ Differenz, au�er bei Unentschieden) */
    public final static int TIPSCORE_FOR_DIFFERENCE = 1;
    /** Tipppunkte f�r die richtig getippte Tendenz */
    public final static int TIPSCORE_FOR_TENDENCY = 1;
    /** Tipppunkte f�r richtig getipptes Unentschieden */
    public final static int TIPSCORE_FOR_CORRECT_DRAW = 4;
    /** Tipppunkte f�r ein getipptes Unentschieden mit falscher Toranzahl */
    public final static int TIPSCORE_FOR_INCORRECT_DRAW = 3;

    /** Tabellenpunkte f�r einen richtig getippten Tabellenplatz einer Mannschaft */
    public final static int TABLESCORE_FOR_RIGHT_POSITION = 18;
    /** Tabellenpunkte f�r einen richtig getippten Absteiger */
    public final static int TABLESCORE_FOR_RIGHT_DOWNSWINGER = 8;
    /** Tabellenpunkte f�r einen richtig getippten UEFA-Cup-Teilnehmer */
    public final static int TABLESCORE_FOR_RIGHT_UEFACUP = 6;
    /** Tabellenpunkte f�r einen richtig getippten CL-Teilnehmer */
    public final static int TABLESCORE_FOR_RIGHT_CL = 2;
    /** Zus�tzliche Tabellenpunkte f�r den richtig getippten Meister */
    public final static int TABLESCORE_FOR_RIGHT_CHAMPION = 2;

    /** Platz den der Meister am Ende der Saison belegt. */
    public static final int CHAMPION_PLACE = 1;
    /** Letzter Platz, der zur Teilnahme an der Champions League berechtigt. */
    public static final int LAST_CL_PLACE = 4;
    /** Letzter Platz, der zur Teilnahme an der Europa League berechtigt. */
    public static final int LAST_EL_PLACE = 6;
    /** Erster Abstiegsplatz. */
    public static final int FIRST_DOWNSWINGER_PLACE = 16;

    /** Maximale L�nge der Namen der Mannschaften */
    protected final static int LENGTH_TEAMNAME = 30;
    /** Maximale L�nge der Namen der User */
    protected final static int LENGTH_USERNAME = 20;

    /** Defaultergebnis */
    public final static int NO_RESULT = -1;
    /** Defaultpunkte- bzw Toranzahl */
    public final static int NO_SCORES = 0;
    /** Defaultplatzierung */
    public static final int NO_POSITION = 0;
}
