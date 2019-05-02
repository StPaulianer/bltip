/*
 * Created on 05.10.2004
 */
package bltip.gui;

import bltip.common.PropertiesConstants;

/**
 * Kapselt Nachrichten und Titel von Fehlerfenstern, Sicherheitsabfragen etc.
 *
 * @author Nico
 * @version 21.08.2006
 */
public interface Messages extends PropertiesConstants {
    /**
     * Nachricht, wenn beim Schlie�en der DB-Verbindung Fehler auftreten
     */
    String ERRORMSG_CLOSING_DBCONN = "Bitte �berpr�fen Sie Ihre Datenbank-Verbindung.";
    /**
     * Titel des Fehlerfensters, wenn Fehler beim Schlie�en der DB-Verbindung auftreten
     */
    String ERRORTITLE_CLOSING_DBCONN = "Fehler: Datenbank-Verbindung konnte nicht geschlossen werden";
    /**
     * Nachricht, wenn die DB-Verbindung nicht aufgebaut werden kann
     */
    String ERRORMSG_NO_DBCONNECTION = "Bitte �berpr�fen Sie Ihre Datenbank-Properties.";
    /**
     * Titel des Fehlerfensters, wenn die DB-Verbindung nicht aufgebaut werden kann
     */
    String ERRORTITLE_NO_DBCONNECTION = "Fehler: Datenbank-Verbindung konnte nicht aufgebaut werden";
    /**
     * Nicht sehr viel sagender Fehlerfenstertitel bei Fehlern in der DB
     */
    String ERRORTITLE_DB_GENERAL = "Fehler: Datenbank verursachte einen Fehler";
    /**
     * Nicht sehr viel sagender Fehlerfenstertitel bei I/O-Fehlern
     */
    String ERRORTITLE_IO_GENERAL = "Fehler: I/O-Problem ist aufgetreten";
    /**
     * Fehlernachricht, wenn das Look-and-Feel nicht umgeschaltet werden konnte
     */
    String ERRORMSG_CHANGE_LOOKANDFEEL = "Das Look-and-Feel konnte nicht umgeschaltet werden.";
    /**
     * Titel des Fehlerfenstern, wenn das Look-and-Feel nicht umgeschaltet werden konnte
     */
    String ERRORTITLE_CHANGE_LOOKANDFEEL = "Fehler: Umschalten vom Look-and-Feel nicht m�glich";
    /**
     * Sicherheitsabfrage, ob die Datenbank wirklich neu aufgesetzt werden soll
     */
    String ENQUIRYMSG_DB_CREATE = "Wenn Sie fortfahren, gehen die aktuellen Daten verloren.\n"
            + "Wollen Sie die Datenbank wirklich neu aufsetzen?";
    /**
     * Titel der Sicherheitsabfrage, ob die Datenbank wirklich neu aufgesetzt werden soll
     */
    String ENQUIRYTITLE_DB_CREATE = "Sicherheitsabfrage: Datenbank neu aufsetzen";
    /**
     * Sicherheitsabfrage, ob die Datenbank wirklich mit neuen Daten gef�llt werden soll
     */
    String ENQUIRYMSG_DB_FILL = "Wollen Sie wirklich die Initialdaten in die Datenbank einlesen?";
    /**
     * Titel der Sicherheitsabfrage, ob die Datenbank wirklich mit neuen Daten gef�llt werden
     * soll
     */
    String ENQUIRYTITLE_DB_FILL = "Sicherheitsabfrage: Initialdaten einlesen";
    /**
     * Fehlernachricht, wenn nichts in der Datenbank ist
     */
    String ERRORMSG_NOTHING_IN_DB = "Bitte f�llen Sie die Datenbank mit Initialdaten.";
    /**
     * Titel des Fehlerfensters, wenn nichts in der Datenbank ist
     */
    String ERRORTITLE_NOTHING_IN_DB = "Fehler: Keine Daten in der Datenbank";
    /**
     * Erster Teil der Frage, ob eine Datei �berschrieben werden soll
     */
    String ENQUIRYMSG1_OVERWRITE = "M�chten Sie ";
    /**
     * Zweiter Teil der Frage, ob eine Datei �berschrieben werden soll
     */
    String ENQUIRYMSG2_OVERWRITE = " �berschreiben?";
    /**
     * Titel der Sicherheitsabfrage zum �berschreiben
     */
    String ENQUIRYTITLE_OVERWRITE = "Sicherheitsabfrage: Datei �berschreiben";
    /**
     * Sicherheitsabfrage, falls nicht alle �nderungen gespeichert wurden
     */
    String ENQUIRYMSG_SAVE_CHANGES = "Es sind noch nicht alle �nderungen gespeichert. M�chten Sie jetzt speichern?";
    /**
     * Titel der Sicherheitsabfrage, wenn nicht alle �nderungen gespeichert wurden
     */
    String ENQUIRYTITLE_SAVE_CHANGES = "Sicherheitsabfrage: �nderungen speichern";
    /**
     * Titel des Bundesligatabellen-Dialogs
     */
    String TITLE_BLTABLE_PART1 = "Bundesligatabelle, ";
    /**
     * Titel des Tipptabellen-Dialogs
     */
    String TITLE_USERTABLE_PART1 = "Tipptabelle, ";
    /**
     * Titel des Dialogs zur Eingabe der Initialisierungsdateien
     */
    String TITLE_INIT_DIALOG = "Eingabe der Initialisierungsdateien";
    /**
     * Titel des Dialogs zur Eingabe der Konfigurationsdateien
     */
    String TITLE_PROP_DIALOG = "Erstellen einer Konfigurationsdatei";
    /**
     * Erster Teil des Titels des Dialogs zur Eingabe der Ergebnisse
     */
    String TITLE_RESULTINPUT_PART1 = "Ergebniseingabe ";
    /**
     * Titel des Dialogs zur Auswahl des Spieltages
     */
    String TITLE_ROUNDCHOICE = "Spieltagsauswahl";
    /**
     * Nchricht im Dialog zur Spieltagsauswahl
     */
    String MSG_ROUNDCHOICE = "Bitte w�hlen Sie einen Spieltag.";
    /**
     * "Anh�ngsel" zum Spieltag
     */
    String ROUND_EXTENSION = ". Spieltag";
    /**
     * Titel des Hauptfensters
     */
    String TITLE_MAINFRAME = "Bundesligatipp 2018/2019";
}
