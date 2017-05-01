/*
 * Created on 12.09.2004
 */
package bltip.storage;

import bltip.common.BlTipException;
import bltip.gui.Messages;
import bltip.model.Team;
import bltip.model.User;
import bltip.util.BlTipUtility;

import java.io.*;

import static bltip.common.Constants.COUNT_OF_WINNERS;

/**
 * Stellt Methoden zur Ausgabe in XML bereit.
 *
 * @author Nico
 * @version 09.10.2004
 */
public class Printer {

    /**
     * ************************************************************************
     * <p>
     * HTML-Ausgabe
     * <p>
     * ************************************************************************
     */

    private static final String COLOR_WINNERS = "#FF0000";

    /**
     * Gibt alle getippten Tabellen der Mittipper in HTML aus
     *
     * @param storesys Speicherungssystem, das die Daten zur Verf�gung stellt.
     * @param parent   <b>Verzeichnis</b>, in das die getippten Tabellen kommen
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
     * @param title   Titel der Seite
     * @param header  �berschrift
     * @param content Inhalt, damit es gut aussieht, bitte mit Zeilenumbr�chen �bergeben und
     *                jede Zeile mit drei Tabulatoren beginnen.
     * @param file    Datei, in die geschrieben werden soll
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
            handle(e, e.getMessage());
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
        StringBuilder html_usertable = new StringBuilder("\t\t\t<table width=\"75%\" border=\"1\";" + " cellpadding=\"2\" cellspacing=\"0\">\n"
                + "\t\t\t  <tr>\n"
                + "\t\t\t    <th width=\"15%\">Platz</th>\n"
                + "\t\t\t    <th width=\"35%\">Name</th>\n"
                + "\t\t\t    <th width=\"15%\">Tipp</th>\n"
                + "\t\t\t    <th width=\"15%\">Tabelle</th>\n"
                + "\t\t\t    <th width=\"15%\">Bonus</th>\n"
                + "\t\t\t    <th width=\"20%\">Gesamt</th>\n"
                + "\t\t\t  </tr>\n");
        for (int i = 0; i < user.length; i++) {
            html_usertable.append("\t\t\t  <tr>\n");
            html_usertable.append("\t\t\t    <td width=\"15%\"").append(i < COUNT_OF_WINNERS ? "; bgcolor=\"" + COLOR_WINNERS + "\"" : "").append("><center>").append(i + 1).append(".</center></td>\n");
            html_usertable.append("\t\t\t    <td width=\"35%\">").append(user[i].getName()).append("</td>\n");
            html_usertable.append("\t\t\t    <td width=\"15%\"><center>").append(user[i].getTipscore()).append("</center></td>\n");
            html_usertable.append("\t\t\t    <td width=\"15%\"><center>").append(user[i].getTablescore()).append("</center></td>\n");
            html_usertable.append("\t\t\t    <td width=\"15%\"><center>").append(user[i].getExtrascore()).append("</center></td>\n");
            html_usertable.append("\t\t\t    <td width=\"20%\"><center>").append(user[i].getScore()).append("</center></td>\n");
            html_usertable.append("\t\t\t  </tr>\n");
        }
        html_usertable.append("\t\t\t</table>\n");

        return html_usertable.toString();
    }

    /**
     * Erstellt und liefert eine Bundesligatabelle in HTML
     *
     * @param teams BL-Tabelle
     */
    private String getHTMLBLTable(Team[] teams) {
        StringBuilder html_bltable = new StringBuilder("\t\t\t<table width=\"75%\" border=\"1\";" + " cellpadding=\"2\" cellspacing=\"0\">\n"
                + "\t\t\t  <tr>\n" + "\t\t\t    <th width=\"5%\">Platz</th>\n" + "\t\t\t    <th " +
                "width=\"45%\">Name</th>\n"
                + "\t\t\t    <th width=\"10%\">Spiele</th>\n" + "\t\t\t    <th width=\"10%\">Punkte</th>\n"
                + "\t\t\t    <th width=\"10%\">Tore</th>\n" + "\t\t\t    <th width=\"10%\">Ggtore</th>\n"
                + "\t\t\t    <th width=\"10%\">Diff.</th>\n" + "\t\t\t  </tr>\n");
        for (int i = 0; i < teams.length; i++) {
            html_bltable.append("\t\t\t  <tr>\n");
            html_bltable.append("\t\t\t    <td width=\"5%\"><center>").append(i + 1).append(".</center></td>\n");
            html_bltable.append("\t\t\t    <td width=\"45%\">").append(teams[i].getName()).append("</td>\n");
            html_bltable.append("\t\t\t    <td width=\"10%\"><center>").append(teams[i].getCountOfGames()).append("</center></td>\n");
            html_bltable.append("\t\t\t    <td width=\"10%\"><center>").append(teams[i].getScore()).append("</center></td>\n");
            html_bltable.append("\t\t\t    <td width=\"10%\"><center>").append(teams[i].getGoals()).append("</center></td>\n");
            html_bltable.append("\t\t\t    <td width=\"10%\"><center>").append(teams[i].getGoalsAgainst()).append("</center></td>\n");
            html_bltable.append("\t\t\t    <td width=\"10%\"><center>").append(teams[i].getDifference()).append("</center></td>\n");
            html_bltable.append("\t\t\t  </tr>\n");
        }
        html_bltable.append("\t\t\t</table>\n");

        return html_bltable.toString();
    }

    /**
     * Regelt das Exception-Handling
     *
     * @param e   Tats�chlich aufgetretene Ausnahme
     * @param msg Nachricht im Fehlerfenster
     * @throws BlTipException Bei I/O-Fehlern
     */
    private void handle(Exception e, String msg) throws BlTipException {
        if (e != null) {
            e.printStackTrace();
            throw new BlTipException(Messages.ERRORTITLE_IO_GENERAL, msg);
        } else
            throw new BlTipException(Messages.ERRORTITLE_IO_GENERAL, msg);
    }
}
