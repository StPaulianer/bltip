/*
 * Created on 10.09.2004
 */
package bltip.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import bltip.common.BlTipException;
import bltip.storage.db.DBTablesAndStatements;

/**
 * Wie der Name schon sagt, stellt n�tzliche Methoden zur Verf�gung.
 * 
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 09.10.2004
 */
public class BlTipUtility {

    private BlTipUtility() {
    }

    /**
     * Macht einen String "SQL-fertig". Alle Escapesequenzen werden durch Backslash + Sequenz
     * ersetzt
     * 
     * @param str Zu maskierender String
     * @return Maskierter String, niemals <code>null</code>, wird <code>null</code> �bergeben,
     *         dann wird der String <code>NULL</code> geliefert (MySQL-Null).
     * @throws BlTipException Bei I/O-Fehlern
     */
    public static String maskToSQL(String str) {
        if (str == null)
            return DBTablesAndStatements.SQL_NULL;

        char BACKSLASH = '\\';
        char NIL = '\0';
        char SINGLE_QUOTE = '\'';
        char DOUBLE_QUOTE = '\"';

        char[] all = str.toCharArray();
        StringBuilder buff = new StringBuilder();

        for (char tmp : all) {
            switch (tmp) {
                case (92):
                    buff.append(BACKSLASH);
                    buff.append(BACKSLASH);
                    break;

                case (39):
                    buff.append(BACKSLASH);
                    buff.append(SINGLE_QUOTE);
                    break;

                case (34):
                    buff.append(BACKSLASH);
                    buff.append(DOUBLE_QUOTE);
                    break;

                case (0):
                    buff.append(BACKSLASH);
                    buff.append(NIL);
                    break;

                default:
                    buff.append(tmp);
            }
        }
        return buff.toString();
    }

    /**
     * Liefert das aktuelle Datum und die aktuelle Uhrzeit als formatierten String
     * 
     * @return aktuelles Datum und die aktuelle Uhrzeit im Format dd.MM.yyyy '-' HH:mm
     */
    public static String currentTime() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy '-' HH:mm");
        return formatter.format(now);
    }

    /**
     * Liefert das aktuelle Datum
     * 
     * @return aktuelles Datum im Format dd.MM.yyyy
     */
    public static String currentDate() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(now);
    }
}
