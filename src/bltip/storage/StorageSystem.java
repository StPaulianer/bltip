/*
 * Created on 08.10.2004
 */
package bltip.storage;

import bltip.common.BlTipException;
import bltip.valueobject.Game;
import bltip.valueobject.Team;
import bltip.valueobject.User;

import java.io.File;

/**
 * Ein System, das die Daten f�r den Bundesligatipp zur Verf�gung stellt, muss dieses Interface
 * implementieren und damit die Methoden zur Verf�gung stellen, auf die z. B. ein
 * GUI-Hauptfenster zugreift.
 *
 * @author Nico
 * @version 27.09.2005 TODO vielleicht k�nnt man statt File's (Input | Output)Streams �bergeben
 *          lassen, w�r besser f�r sp�ter mit Server und Client... allerdings macht es im
 *          Moment einige Probleme wegen des Verzeichnisses, dass man �bergibt, in dem die
 *          Tipps drin sind.
 */
public interface StorageSystem {

    /**
     * Wird initial einmal pro Programmablauf aufgerufen und gibt dem Speicherungssystem die
     * M�glichkeit, sich z.B. zu einer Datenbank zu verbinden. Diese initialen Aktionen sollen
     * also nicht im Konstruktor geschehen.
     *
     * @throws BlTipException Tritt eine Ausnahme auf, soll diese gefangen und eine
     *                        <code>BLTipException</code> geworfen werden.
     */
    void connect() throws BlTipException;

    /**
     * Wird einmalig aufgerufen, um dem Speicherungssystem die M�glichkeit zu geben, bsplw.
     * eine Datenbankverbindung zu schlie�en.
     *
     * @throws BlTipException Tritt eine Ausnahme auf, soll diese gefangen und eine
     *                        <code>BLTipException</code> geworfen werden.
     */
    void disconnect() throws BlTipException;

    /**
     * Erstellt die Datenstrukturen des Speicherungssystem. Die Initialdaten werden erst mit
     * dem Aufruf von #initialize(File, File, File) eingelesen. Nach dem Aufruf dieser Methode
     * sind die Daten, die vorher gespeichert waren, verloren.
     *
     * @throws BlTipException Tritt eine Ausnahme auf, soll diese gefangen und eine
     *                        <code>BLTipException</code> geworfen werden.
     */
    void create() throws BlTipException;

    /**
     * F�llt die Datenstrukturen mit Initialdaten. Es werden Dateien �bergeben, die die
     * Paarungen (und damit auch die Mannschaftsnamen) und die Namen der Tipper enthalten.
     * Au�erdem wird ein Verzeichnis �bergeben, in dem sich zu jedem Mittipper eine Datei mit
     * seinen Tipps befindet (<b>Tippername.txt</b>). Wird keine valide (nicht erwarteter
     * Inhalt, ...) Datei �bergeben, wird eine <code>BLTipException</code>, die �bergabe von
     * <code>null</code> f�hrt zu einer <code>NullPointerException</code>.
     *
     * @param games Die Datei, die die Paarungen enth�lt, die �bergabe von <code>null</code>
     *              f�hrt zu einer <code>NullPointerException</code>.
     * @param user  Die Datei, die die Namen der Tipper enth�lt, die �bergabe von
     *              <code>null</code> f�hrt zu einer <code>NullPointerException</code>.
     * @param tips  Das Verzeichnis, in dem zu jedem Tipper eine Datei existiert
     *              (<b>Tippername.txt</b>), die dessen Tipps enth�lt, die �bergabe von
     *              <code>null</code> f�hrt zu einer <code>NullPointerException</code>.
     * @throws BlTipException Tritt eine Ausnahme auf, soll diese gefangen und eine
     *                        <code>BLTipException</code> geworfen werden.
     */
    void initialize(File games, File user, File tips) throws BlTipException;

    /**
     * Liefert den fr�hesten Spieltag, an dem noch Ergebnisse offen sind. Sind bereits alle
     * Ergebnisse eingegeben, wird #getCountOfRounds() geliefert, bei Fehlern <code>-1</code>.
     *
     * @return Der fr�heste Spieltag, an dem noch Ergebnisse offen, also noch einzugeben sind,
     * bei Fehlern (keine Initialdaten, ...) <code>-1</code>.
     * @throws BlTipException Tritt eine Ausnahme auf, soll diese gefangen und eine
     *                        <code>BLTipException</code> geworfen werden.
     */
    int getFirstRoundWithoutResults() throws BlTipException;

    /**
     * Liefert den aktuellen Spieltag. Haben noch keine Spiele stattgefunden, ist dies der 1.,
     * sind alle Partien bereits gespielt der #getCountOfRounds() Spieltag. Ansonsten wird der
     * h�chste Spieltag geliefert, an dem bereits ein Ergebnis bekannt ist. Bei Fehlern wird
     * <code>-1</code> geliefert.
     *
     * @return Der aktuelle Spieltag, der h�chste Spieltag, an dem ein Ergebnis bekannt ist.
     * Bei Fehlern (keine Initialdaten, ...) <code>-1</code>.
     * @throws BlTipException Tritt eine Ausnahme auf, soll diese gefangen und eine
     *                        <code>BLTipException</code> geworfen werden.
     */
    int getActualRound() throws BlTipException;

    /**
     * Liefert die aktuelle Tabelle der Bundesliga. Sind keine Mannschaften vorhanden, wird ein
     * leeres Array zur�ck geliefert, also niemals <code>null</code>.
     *
     * @return Die aus den Ergebnissen errechnete Bundesligatabelle, niemals <code>null</code>.
     * @throws BlTipException Tritt eine Ausnahme auf, soll diese gefangen und eine
     *                        <code>BLTipException</code> geworfen werden.
     */
    Team[] getBLTable() throws BlTipException;

    /**
     * Liefert die getippte Tabelle eines Tippers. Sind keine Mannschaften vorhanden, wird ein
     * leeres Array zur�ck geliefert, also niemals <code>null</code>.
     *
     * @param userID Die ID des Tippers, dessen getippte Tabelle geliefert wird.
     * @return Die aus den Tipps errechnete Bundesligatabelle, niemals <code>null</code>.
     * @throws BlTipException Tritt eine Ausnahme auf, soll diese gefangen und eine
     *                        <code>BLTipException</code> geworfen werden.
     */
    Team[] getTableOfUser(int userID) throws BlTipException;

    /**
     * Liefert die Tabelle der Tipper. Sind keine Tipper vorhanden, wird ein leeres Array
     * zur�ck geliefert, also niemals <code>null</code>.
     *
     * @return Die Tabelle der Tipper, niemals <code>null</code>.
     * @throws BlTipException Tritt eine Ausnahme auf, soll diese gefangen und eine
     *                        <code>BLTipException</code> geworfen werden.
     */
    User[] getUsertable() throws BlTipException;

    /**
     * Liefert die Paarungen zu einem bestimmten Spieltag. Gibt es zu dem �bergebenen Spieltag
     * keine Paarungen, wird ein leeres Array zur�ck geliefert, also niemals <code>null</code>.
     *
     * @param round Der Spieltag, zu dem die Paarungen gew�nscht sind.
     * @return Die Paarungen des Spieltags, niemals <code>null</code>.
     * @throws BlTipException Tritt eine Ausnahme auf, soll diese gefangen und eine
     *                        <code>BLTipException</code> geworfen werden.
     */
    Game[] getGames(int round) throws BlTipException;

    /**
     * Erneuert die Ergebnisse der �bergebenen Spiele. Um einen konsistenten Zustand zu wahren,
     * werden innerhalb dieser Methode auch die neue Bundesligatabelle, die neuen Tipp- und
     * Tabellenpunkte und die neue Tipptabelle berechnet.
     *
     * @param games           Die Paarungen, zu denen die Ergebnisse gesetzt werden sollen, die �bergabe
     *                        von <code>null</code> f�hrt zu einer <code>NullPointerException</code>.
     * @param newHomeResults  Die neuen Heimergebnisse, <code>newHomeResults[i]</code> bezieht
     *                        sich hierbei auf <code>games[i]</code>.
     * @param newGuestResults Die neuen Ausw�rtsergebnisse, <code>newGuestResults[i]</code>
     *                        bezieht sich hierbei auf <code>games[i]</code>.
     * @throws BlTipException Tritt eine Ausnahme auf, soll diese gefangen und eine
     *                        <code>BLTipException</code> geworfen werden.
     */
    void setResults(Game[] games, int[] newHomeResults, int[] newGuestResults) throws BlTipException;

    /**
     * Erstellt eine HTML-Datei mit der Tipptabelle.
     *
     * @param file Die Datei, in die die Tipptabelle geschrieben wird, die �bergabe von
     *             <code>null</code> f�hrt zu einer <code>NullPointerException</code>.
     * @throws BlTipException Tritt eine Ausnahme auf, soll diese gefangen und eine
     *                        <code>BLTipException</code> geworfen werden.
     */
    void printUserTableInHTML(File file) throws BlTipException;

    /**
     * Erstellt im �bergebenen Verzeichnis f�r jeden Tipper eine Datei
     * (<b>Tippername_Tab.html</b>) mit seiner getippten Tabelle.
     *
     * @param file Das Verzeichnis, in das die getippten Tabellen geschrieben werden, die
     *             �bergabe von <code>null</code> f�hrt zu einer
     *             <code>NullPointerException</code>.
     * @throws BlTipException Tritt eine Ausnahme auf, soll diese gefangen und eine
     *                        <code>BLTipException</code> geworfen werden.
     */
    void printAllTiptables(File file) throws BlTipException;
}
