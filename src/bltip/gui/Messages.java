/*
 * Created on 05.10.2004
 */
package bltip.gui;

import bltip.common.PropertiesConstants;

/**
 * Kapselt Nachrichten und Titel von Fehlerfenstern, Sicherheitsabfragen etc.
 * 
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 21.08.2006
 */
public interface Messages extends PropertiesConstants {
    /** Nachricht, wenn beim Schlie�en der DB-Verbindung Fehler auftreten */
    public static final String ERRORMSG_CLOSING_DBCONN = "Bitte �berpr�fen Sie Ihre Datenbank-Verbindung.";
    /** Titel des Fehlerfensters, wenn Fehler beim Schlie�en der DB-Verbindung auftreten */
    public static final String ERRORTITLE_CLOSING_DBCONN = "Fehler: Datenbank-Verbindung konnte nicht geschlossen werden";
    /** Nachricht, wenn die DB-Verbindung nicht aufgebaut werden kann */
    public static final String ERRORMSG_NO_DBCONNECTION = "Bitte �berpr�fen Sie Ihre Datenbank-Properties.";
    /** Titel des Fehlerfensters, wenn die DB-Verbindung nicht aufgebaut werden kann */
    public static final String ERRORTITLE_NO_DBCONNECTION = "Fehler: Datenbank-Verbindung konnte nicht aufgebaut werden";
    /** Fehlernachricht, wenn Properties nicht definiert sind */
    public static final String ERRORMSG_MISSING_PROPKEYS = "Bitte legen Sie Werte f�r folgende Schl�ssel in "
            + PropertiesConstants.PROFILE + " fest:\n" + PropertiesConstants.KEY_ROOT_PATH + ", "
            + PropertiesConstants.KEY_DBNAME + ", " + PropertiesConstants.KEY_DBURL + ", " + PropertiesConstants.KEY_DBURL_SUFFIX
            + ", " + PropertiesConstants.KEY_DB_MYSQL_DRIVER;
    /** Titel des Fehlerfensters, wenn Properties nicht definiert sind */
    public static final String ERRORTITLE_MISSING_PROPKEYS = "Fehler: Fehlende Schl�ssel-Wert-Paare";
    /** Fehlernachricht, wenn kein Properties-File angelegt ist */
    public static final String ERRORMSG_NO_CONFIGFILE = "Bitte erstellen Sie eine Konfigurationsdatei unter "
            + PropertiesConstants.PROFILE + ".";
    /** Titel des Fehlerfensters, wenn kein Properties-File angelegt ist */
    public static final String ERRORTITLE_NO_CONFIGFILE = "Fehler: Konfigurationsdatei konnte nicht gefunden werden";
    /** Titel des Fehlerfensters, wenn beim Maskieren eines String f�r die DB Fehler auftreten */
    public static final String ERRORTITLE_STRINGMASK = "Fehler: Problem beim Maskieren eines Strings";
    /** Nicht sehr viel sagender Fehlerfenstertitel bei Fehlern in der DB */
    public static final String ERRORTITLE_DB_GENERAL = "Fehler: Datenbank verursachte einen Fehler";
    /** Nicht sehr viel sagender Fehlerfenstertitel bei I/O-Fehlern */
    public static final String ERRORTITLE_IO_GENERAL = "Fehler: I/O-Problem ist aufgetreten";
    /** Fehlernachricht, wenn das Look-and-Feel nicht umgeschaltet werden konnte */
    public static final String ERRORMSG_CHANGE_LOOKANDFEEL = "Das Look-and-Feel konnte nicht umgeschaltet werden.";
    /** Titel des Fehlerfenstern, wenn das Look-and-Feel nicht umgeschaltet werden konnte */
    public static final String ERRORTITLE_CHANGE_LOOKANDFEEL = "Fehler: Umschalten vom Look-and-Feel nicht m�glich";
    /** Sicherheitsabfrage, ob die Datenbank wirklich neu aufgesetzt werden soll */
    public static final String ENQUIRYMSG_DB_CREATE = "Wenn Sie fortfahren, gehen die aktuellen Daten verloren.\n"
            + "Wollen Sie die Datenbank wirklich neu aufsetzen?";
    /** Titel der Sicherheitsabfrage, ob die Datenbank wirklich neu aufgesetzt werden soll */
    public static final String ENQUIRYTITLE_DB_CREATE = "Sicherheitsabfrage: Datenbank neu aufsetzen";
    /** Sicherheitsabfrage, ob die Datenbank wirklich mit neuen Daten gef�llt werden soll */
    public static final String ENQUIRYMSG_DB_FILL = "Wollen Sie wirklich die Initialdaten in die Datenbank einlesen?";
    /**
     * Titel der Sicherheitsabfrage, ob die Datenbank wirklich mit neuen Daten gef�llt werden
     * soll
     */
    public static final String ENQUIRYTITLE_DB_FILL = "Sicherheitsabfrage: Initialdaten einlesen";
    /** Fehlernachricht, wenn nichts in der Datenbank ist */
    public static final String ERRORMSG_NOTHING_IN_DB = "Bitte f�llen Sie die Datenbank mit Initialdaten.";
    /** Titel des Fehlerfensters, wenn nichts in der Datenbank ist */
    public static final String ERRORTITLE_NOTHING_IN_DB = "Fehler: Keine Daten in der Datenbank";
    /** Erster Teil der Frage, ob eine Datei �berschrieben werden soll */
    public static final String ENQUIRYMSG1_OVERWRITE = "M�chten Sie ";
    /** Zweiter Teil der Frage, ob eine Datei �berschrieben werden soll */
    public static final String ENQUIRYMSG2_OVERWRITE = " �berschreiben?";
    /** Titel der Sicherheitsabfrage zum �berschreiben */
    public static final String ENQUIRYTITLE_OVERWRITE = "Sicherheitsabfrage: Datei �berschreiben";
    /** Sicherheitsabfrage, falls nicht alle �nderungen gespeichert wurden */
    public static final String ENQUIRYMSG_SAVE_CHANGES = "Es sind noch nicht alle �nderungen gespeichert. M�chten Sie jetzt speichern?";
    /** Titel der Sicherheitsabfrage, wenn nicht alle �nderungen gespeichert wurden */
    public static final String ENQUIRYTITLE_SAVE_CHANGES = "Sicherheitsabfrage: �nderungen speichern";
    /** Titel des Bundesligatabellen-Dialogs */
    public static final String TITLE_BLTABLE_PART1 = "Bundesligatabelle, ";
    /** Titel des Tipptabellen-Dialogs */
    public static final String TITLE_USERTABLE_PART1 = "Tipptabelle, ";
    /** Titel des Dialogs zur Eingabe der Initialisierungsdateien */
    public static final String TITLE_INIT_DIALOG = "Eingabe der Initialisierungsdateien";
    /** Titel des Dialogs zur Eingabe der Konfigurationsdateien */
    public static final String TITLE_PROP_DIALOG = "Erstellen einer Konfigurationsdatei";
    /** Erster Teil des Titels des Dialogs zur Eingabe der Ergebnisse */
    public static final String TITLE_RESULTINPUT_PART1 = "Ergebniseingabe ";
    /** Titel des Dialogs zur Auswahl des Spieltages */
    public static final String TITLE_ROUNDCHOICE = "Spieltagsauswahl";
    /** Nchricht im Dialog zur Spieltagsauswahl */
    public static final String MSG_ROUNDCHOICE = "Bitte w�hlen Sie einen Spieltag.";
    /** "Anh�ngsel" zum Spieltag */
    public static final String ROUND_EXTENSION = ". Spieltag";
    /** Titel des Hauptfensters */
    public static final String TITLE_MAINFRAME = "Bundesligatipp 2014/2015";
    /** Fehlernachricht, wenn eine Datei nicht den erwarteten Inhalt hat */
    public static final String ERRORMSG_FILE_NOT_VALID = "Eine angegebene Datei war nicht valide.";
}
