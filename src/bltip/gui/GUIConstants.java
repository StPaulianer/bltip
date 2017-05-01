/*
 * Created on 16.09.2004
 */
package bltip.gui;

import bltip.common.PropertiesConstants;

import java.awt.*;

/**
 * Stellt Beschriftungen, Men�eintr�ge usw. zur Verf�gung.
 *
 * @author Nico
 * @version 27.09.2005
 */
interface GUIConstants extends PropertiesConstants {

    /**
     * Die initiale Ausrichtung der Fenster in X-Richtung
     */
    int LOCATION_X = 100;
    /**
     * Die initiale Ausrichtung der Fenster in Y-Richtung
     */
    int LOCATION_Y = 100;
    /**
     * Die bevorzugte Breite der Buttons
     */
    int WIDTH_BUTTONS = 90;
    /**
     * Die bevorzugte H�he der Buttons
     */
    int HEIGHT_BUTTONS = 35;
    /**
     * Die bevorzugte Breite der BL-Tabelle
     */
    int WIDTH_SCROLLPANE_BLTABLE = 500;
    /**
     * Die bevorzugte H�he der BL-Tabelle
     */
    int HEIGHT_SCROLLPANE_BLTABLE = 311;
    /**
     * Die bevorzugte Breite der Tipptabelle
     */
    int WIDTH_SCROLLPANE_USERTABLE = 400;
    /**
     * Die bevorzugte H�he der Tipptabelle
     */
    int HEIGHT_SCROLLPANE_USERTABLE = 247;
    /**
     * L�nge der Textfelder zur Eingabe der Ergebnisse
     */
    int LENGTH_RESULT_TEXTFIELD = 2;
    /**
     * L�nge der Textfelder f�r Dateipfade
     */
    int LENGTH_PATH_TEXTFIELD = 25;
    /**
     * Die bevorzugte Breite eines Labels, mit einer Mannschaft als Text
     */
    int WIDTH_TEAMLABEL = 170;
    /**
     * Die bevorzugte H�he eines Labels, mit einer Mannschaft als Text
     */
    int HEIGHT_TEAMLABEL = 20;
    /**
     * Horizontaler Abstand von Buttons, wenn das <code>FlowLayout</code> benutzt wird
     */
    int FLOWLAYOUT_BUTTONS_HGAP = 10;
    /**
     * Vertikaler Abstand von Buttons, wenn das <code>FlowLayout</code> benutzt wird
     */
    int FLOWLAYOUT_BUTTONS_VGAP = 5;
    /**
     * Horizontaler und vertikaler kleiner Default-Abstand beim <code>FlowLayout</code>
     */
    int FLOWLAYOUT_DEFAULT_GAP = 2;

    /**
     * Ein dunkle(re)s Orange
     */
    Color DARKER_ORANGE = new Color(255, 100, 0);
    /**
     * Ein dunkles Orange
     */
    Color DARK_ORANGE = new Color(255, 145, 0);
    /**
     * Die Hintergrundfarbe des BL-F�hrenden
     */
    Color COLOR_CHAMPION = DARKER_ORANGE;
    /**
     * Die Hintergrundfarbe der Mannschaften, die CL-Pl�tzen stehen
     */
    Color COLOR_CL = DARK_ORANGE;
    /**
     * Die Hintergrundfarbe der Mannschaften, die auf CL-Quali-Pl�tzen stehen
     */
    Color COLOR_CL_QUALIFY = Color.orange;
    /**
     * Die Hintergrundfarbe der Mannschaften, die auf UEFA-Cup-Pl�tzen stehen
     */
    Color COLOR_UEFACUP = Color.yellow;
    /**
     * Die Hintergrundfarbe der Mannschaften, die auf Abstiegspl�tzen stehen
     */
    Color COLOR_DESCENDER = Color.lightGray;
    /**
     * Die Hintergrundfarbe F�hrenden im Tippspiel
     */
    Color COLOR_TIPCHAMPION = DARKER_ORANGE;
    /**
     * Die Hintergrundfarbe Zweiten im Tippspiel
     */
    Color COLOR_TIPSECOND = DARK_ORANGE;
    /**
     * Die Hintergrundfarbe Dritten im Tippspiel
     */
    Color COLOR_TIPTHIRD = Color.orange;
    /**
     * Die Hintergrundfarbe Vierten im Tippspiel
     */
    Color COLOR_TIPFOURTH = Color.yellow;
    /**
     * Die Hintergrundfarbe des Tipper, der auf dem letzten Platz steht
     */
    Color COLOR_TIPLOSER = Color.lightGray;
    /**
     * Hintergrundfarbe der editierbaren Textfelder
     */
    Color COLOR_EDITABLE_TEXTFIELDS = Color.white;

    /**
     * Beschriftung eines Labels, das Heim- und Ausw�rtsergebnis trennt
     */
    String RESULT_SEPARATOR = ":";
    /**
     * Beschriftung eines Labels, das die Mannschaften trennt
     */
    String TEAM_SEPARATOR = "-";

    /**
     * Look-and-Feel f�r Windows
     */
    String PLAF_WINDOWS = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    /**
     * ID f�r Windows-Look-and-Feel
     */
    String LAFID_WINDOWS = "Windows";
    /**
     * Look-and-Feel f�r Motif
     */
    String PLAF_MOTIF = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    /**
     * ID f�r Motif-Look-and-Feel
     */
    String LAFID_MOTIF = "Motif";
    /**
     * Look-and-Feel f�r Metal
     */
    String PLAF_METAL = "javax.swing.plaf.metal.MetalLookAndFeel";
    /**
     * ID f�r Metal-Look-and-Feel
     */
    String LAFID_METAL = "Metal";

    /**
     * Beschriftung eines OK-Buttons
     */
    String BUTTON_OK = "OK";
    /**
     * Shortcut f�r den Button OK
     */
    char SHORTCUT_BUTTON_OK = 'O';
    /**
     * Beschriftung eines Cancel-Buttons
     */
    String BUTTON_CANCEL = "Abbrechen";
    /**
     * Shortcut f�r den Button Cancel
     */
    char SHORTCUT_BUTTON_CANCEL = 'A';

    /**
     * Shortcut f�r den Men�-Eintrag Windows im Look-and-Feel
     */
    char SHORTCUT_WINDOWS = 'W';
    /**
     * Men�-Eintrag f�r Windows im Look-and-Feel
     */
    String MENU_WINDOWS = "Windows";
    /**
     * Shortcut f�r den Men�-Eintrag Motif im Look-and-Feel
     */
    char SHORTCUT_MOTIF = 'o';
    /**
     * Men�-Eintrag f�r Motif im Look-and-Feel
     */
    String MENU_MOTIF = "Motif";
    /**
     * Shortcut f�r den Men�-Eintrag Metal im Look-and-Feel
     */
    char SHORTCUT_METAL = 'e';
    /**
     * Men�-Eintrag f�r Metal im Look-and-Feel
     */
    String MENU_METAL = "Metal";
    /**
     * Shortcut f�r das Look-and-Feel-Men�
     */
    char LOOK_AND_FEELMENU_SHORTCUT = 'L';
    /**
     * Titel des Look-and-Feel-Men�s
     */
    String LOOK_AND_FEELMENU_TITLE = "Look and Feel";
    /**
     * Shortcut f�rs F�llen der DB
     */
    char SHORTCUT_DB_FILL = 'I';
    /**
     * Men�-Eintrag f�rs F�llen der DB
     */
    String MENU_DB_FILL = "Initialdaten einlesen";
    /**
     * Shortcut f�rs Neu aufsetzen der DB
     */
    char SHORTCUT_DB_CREATE = 'N';
    /**
     * Men�-Eintrag f�rs Neu aufsetzen der DB
     */
    String MENU_DB_CREATE = "Neu aufsetzen";
    /**
     * Shortcut f�r das Datenbank-Men�
     */
    char DATABASEMENU_SHORTCUT = 'a';
    /**
     * Titel des Datenbank-Men�s
     */
    String DATEBASEMENU_TITLE = "Datenbank";
    /**
     * Shortcut f�r die Tipptabelle
     */
    char SHORTCUT_USERTABLE = 'T';
    /**
     * Men�-Eintrag f�r die Tipptabelle
     */
    String MENU_USERTABLE = "Tipptabelle";
    /**
     * Shortcut f�r die BL-Tabelle
     */
    char SHORTCUT_BLTABLE = 'B';
    /**
     * Men�-Eintrag f�r die BL-Tabelle
     */
    String MENU_BLTABLE = "Bundesligatabelle";
    /**
     * Shortcut f�rs Eingeben der Ergebnisse
     */
    char SHORTCUT_RESULTS = 'E';
    /**
     * Men�-Eintrag f�r die Ergebniseingabe
     */
    String MENU_RESULTS = "Ergebniseingabe";
    /**
     * Shortcut f�r das Bearbeiten-Men�
     */
    char WORKMENU_SHORTCUT = 'B';
    /**
     * Titel des Bearbeiten-Men�s
     */
    String WORKMENU_TITLE = "Bearbeiten";
    /**
     * Shortcut f�rs Exportieren der getippten Tabellen
     */
    char SHORTCUT_EXPORT_HTML_TIPTABLES = 'G';
    /**
     * Men�-Eintrag f�r den Export der getippten Tabellen
     */
    String MENU_EXPORT_HTML_TIPTABLES = "Getippte Tabellen";
    /**
     * Shortcut f�rs Exportieren der Tipptabelle
     */
    char SHORTCUT_EXPORT_HTML = 'T';
    /**
     * Men�-Eintrag f�r den Export der Tipptabelle
     */
    String MENU_EXPORT_HTML = "Tipptabelle";
    /**
     * Shortcut f�r das Datei-Men�
     */
    char FILEMENU_SHORTCUT = 'D';
    /**
     * Titel des Datei-Men�s
     */
    String FILEMENU_TITLE = "Datei";
    /**
     * Shortcut f�rs Beenden
     */
    char SHORTCUT_CLOSE = 'e';
    /**
     * Shortcut f�rs Exportieren
     */
    char SHORTCUT_EXPORT = 'x';
    /**
     * Men�-Eintrag f�rs Beenden
     */
    String MENU_CLOSE = "Beenden";
    /**
     * Men�-Eintrag f�rs Exportieren
     */
    String MENU_EXPORT = "Exportiere nach HTML";

    /**
     * Tooltip f�rs Exportieren einer HTML-Tipptabelle
     */
    String TOOLTIP_EXPORT_HTML = "Erstellen Sie eine HTML-Seite der Tipptabelle.";
    /**
     * Tooltip f�rs Exportieren der getippten Tabellen
     */
    String TOOLTIP_EXPORT_HTML_TIPTABLES = "Exportieren Sie die getippten Tabellen der Mittipper.";
    /**
     * Tooltip f�rs Eingeben der Ergebnisse
     */
    String TOOLTIP_RESULTS = "Geben Sie aktuelle Bundesliga-Ergebnisse ein.";
    /**
     * Tooltip f�rs Angucken der BL-Tabelle
     */
    String TOOLTIP_BLTABLE = "Schauen Sie sich die Bundesligatabelle an.";
    /**
     * Tooltip f�rs Angucken der Tipptabelle
     */
    String TOOLTIP_USERTABLE = "Schauen Sie sich die Tipptabelle an.";
    /**
     * Tooltip f�rs Neu aufsetzen der DB
     */
    String TOOLTIP_DB_CREATE = "Setzen Sie die Datenbank neu auf. Vorsicht! Nur f�r erfahrene Anwender.";
    /**
     * Tooltip f�rs Initialisieren der DB
     */
    String TOOLTIP_DB_FILL = "Initialisieren Sie die Datenbank. Vorsicht! Nur f�r erfahrene Anwender.";
    /**
     * Tooltip f�r den Pfad zu den Paarungen
     */
    String TOOLTIP_PATH_GAMES = "Bitte geben Sie eine Datei an, in der die Paarungen stehen.";
    /**
     * Tooltip f�r den Pfad der Namen der Tipper
     */
    String TOOLTIP_PATH_USER = "Bitte geben Sie eine Datei an, in der die Namen der Mittipper stehen.";
    /**
     * Tooltip f�r den Pfad zum Verzeichnis, in dem die Dateien der Tipps sind
     */
    String TOOLTIP_PATH_TIPS = "Bitte geben Sie ein Verzeichnis an, in dem sich Dateien mit den Tipps befinden.\n"
            + "Es muss f�r jeden Tipper eine Datei mit seinen Tipps vorhanden sein (Tippername.txt).";
}
