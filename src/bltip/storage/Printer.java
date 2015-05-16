/*
 * Created on 12.09.2004
 */
package bltip.storage;

import static bltip.common.Constants.COUNT_OF_WINNERS;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import bltip.common.BlTipException;
import bltip.gui.Messages;
import bltip.util.BlTipUtility;
import bltip.valueobject.Team;
import bltip.valueobject.User;

/**
 * Stellt Methoden zur Ausgabe in XML bereit.
 *
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 09.10.2004 todo mithilfe von DOM eine XML-Ausgabe oder so
 */
public class Printer {

    /***************************************************************************
     *
     * HTML-Ausgabe
     *
     **************************************************************************/

    private static final String COLOR_WINNERS = "#FF0000";

    /**
     * Gibt alle getippten Tabellen der Mittipper in HTML aus
     *
     * @param storesys Speicherungssystem, das die Daten zur Verf�gung stellt.
     * @param parent <b>Verzeichnis</b>, in das die getippten Tabellen kommen
     * @throws BlTipException Bei Fehlern auf unteren Ebenen
     */
    public void printAllTiptables(StorageSystem storesys, File parent) throws BlTipException {
        User[] user = storesys.getUsertable();
        Team[] teams;

        for (User anUser : user) {
            teams = storesys.getTableOfUser(anUser.getId());
            File file = new File(parent + File.separator + anUser.getName() + "_Tab.html");

            printHTMLSite("Tabelle von " + anUser.getName(), "Getippte Tabelle von " + anUser.getName(),
                    getHTMLBLTable(teams),
                    file);
        }
    }

    /**
     * Erstellt mit den �bergebenen Strings eine HTML-Seite ohne irgendein Design.
     *
     * @param title Titel der Seite
     * @param header �berschrift
     * @param content Inhalt, damit es gut aussieht, bitte mit Zeilenumbr�chen �bergeben und
     *            jede Zeile mit drei Tabulatoren beginnen.
     * @param file Datei, in die geschrieben werden soll
     * @throws BlTipException Bei Fehlern auf unteren Ebenen
     */
    public void printHTMLSite(String title, String header, String content, File file) throws BlTipException {
        FileOutputStream outputStream = null;
        PrintStream out = null;
        try {
            outputStream = new FileOutputStream(file);
            out = new PrintStream(new BufferedOutputStream(outputStream), true, "UTF-8");

            out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML" + " 4.01 Transitional//EN\">");
            out.println("<html>");
            out.println("<head>");
            out.println("\t<title>" + title + "</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("\t<h2>" + header + "</h2>");
            out.println(content);
            out.println("\t<p> Eigentlich hasse " + "<a href=\"mailto:nico.mischok@gmx.de\">ich</a> Signaturen, " +
                    "aber was solls, "
                    + BlTipUtility.currentDate() + "</p>");
            out.println("</body>");
            out.println("</html>");

            out.flush();
        } catch (IOException e) {
            handle(e, Messages.ERRORTITLE_IO_GENERAL, e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * Erstellt und liefert eine HTML-Tabelle der �bergebenen User.
     *
     * @param user Tabelle der User
     */
    public String getHTMLUserTable(User[] user) {
        String html_usertable = "\t\t\t<table width=\"75%\" border=\"1\";" + " cellpadding=\"2\" cellspacing=\"0\">\n"
                + "\t\t\t  <tr>\n" + "\t\t\t    <th width=\"15%\">Platz</th>\n" + "\t\t\t    <th " +
                "width=\"35%\">Name</th>\n"
                + "\t\t\t    <th width=\"15%\">Tipp</th>\n" + "\t\t\t    <th width=\"15%\">Tabelle</th>\n"
                + "\t\t\t    <th width=\"20%\">Gesamt</th>\n" + "\t\t\t  </tr>\n";
        for (int i = 0; i < user.length; i++) {
            html_usertable += "\t\t\t  <tr>\n";
            html_usertable += "\t\t\t    <td width=\"15%\"" + (i < COUNT_OF_WINNERS ? "; bgcolor=\"" + COLOR_WINNERS + "\"" : "")
                    + "><center>" + (i + 1) + ".</center></td>\n";
            html_usertable += "\t\t\t    <td width=\"35%\">" + user[i].getName() + "</td>\n";
            html_usertable += "\t\t\t    <td width=\"15%\"><center>" + user[i].getTipscore() + "</center></td>\n";
            html_usertable += "\t\t\t    <td width=\"15%\"><center>" + user[i].getTablescore() + "</center></td>\n";
            html_usertable += "\t\t\t    <td width=\"20%\"><center>" + user[i].getScore() + "</center></td>\n";
            html_usertable += "\t\t\t  </tr>\n";
        }
        html_usertable += "\t\t\t</table>\n";

        return html_usertable;
    }

    /**
     * Erstellt und liefert eine Bundesligatabelle in HTML
     *
     * @param teams BL-Tabelle
     */
    String getHTMLBLTable(Team[] teams) {
        String html_bltable = "\t\t\t<table width=\"75%\" border=\"1\";" + " cellpadding=\"2\" cellspacing=\"0\">\n"
                + "\t\t\t  <tr>\n" + "\t\t\t    <th width=\"5%\">Platz</th>\n" + "\t\t\t    <th " +
                "width=\"45%\">Name</th>\n"
                + "\t\t\t    <th width=\"10%\">Spiele</th>\n" + "\t\t\t    <th width=\"10%\">Punkte</th>\n"
                + "\t\t\t    <th width=\"10%\">Tore</th>\n" + "\t\t\t    <th width=\"10%\">Ggtore</th>\n"
                + "\t\t\t    <th width=\"10%\">Diff.</th>\n" + "\t\t\t  </tr>\n";
        for (int i = 0; i < teams.length; i++) {
            html_bltable += "\t\t\t  <tr>\n";
            html_bltable += "\t\t\t    <td width=\"5%\"><center>" + (i + 1) + ".</center></td>\n";
            html_bltable += "\t\t\t    <td width=\"45%\">" + teams[i].getName() + "</td>\n";
            html_bltable += "\t\t\t    <td width=\"10%\"><center>" + teams[i].getCountOfGames() + "</center></td>\n";
            html_bltable += "\t\t\t    <td width=\"10%\"><center>" + teams[i].getScore() + "</center></td>\n";
            html_bltable += "\t\t\t    <td width=\"10%\"><center>" + teams[i].getGoals() + "</center></td>\n";
            html_bltable += "\t\t\t    <td width=\"10%\"><center>" + teams[i].getGoalsAgainst() + "</center></td>\n";
            html_bltable += "\t\t\t    <td width=\"10%\"><center>" + teams[i].getDifference() + "</center></td>\n";
            html_bltable += "\t\t\t  </tr>\n";
        }
        html_bltable += "\t\t\t</table>\n";

        return html_bltable;
    }

    /**
     * Regelt das Exception-Handling
     *
     * @param e Tats�chlich aufgetretene Ausnahme
     * @param title Titel des Fehlerfensters
     * @param msg Nachricht im Fehlerfenster
     * @throws BlTipException Bei I/O-Fehlern
     */
    private void handle(Exception e, String title, String msg) throws BlTipException {
        if (e != null) {
            e.printStackTrace();
            throw new BlTipException(e, title, msg);
        } else
            throw new BlTipException(title, msg);
    }
}
