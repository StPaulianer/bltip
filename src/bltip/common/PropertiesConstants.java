package bltip.common;

import java.io.File;

/**
 * Dieses Interface b�ndelt absolute Pfade von Dateien, die f�r die Initialdaten der Datenbank
 * und auch die Ausgaben in XML und HTML ben�tigt werden.
 * 
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 29.12.2004
 */
public interface PropertiesConstants {

    /** Profile-Datei, enth�lt unter anderem den Pfad der Konfigurationsdatei */
    public static final File PROFILE = new File(".profile");
    /**
     * Schl�ssel des Pfades der Konfigurationsdatei (dieser steht in der Profile-Datei
     */
    public static final String PROFILEKEY_PROP_FILE = "path_properties_file";
    /** Schl�ssel des Look-and-Feel (in der Profile-Datei) */
    public static final String PROFILEKEY_LOOK_AND_FEEL = "look_and_feel";

    /** Key des Benutzernamens in der Config-Datei */
    public static final String KEY_DBUSER = "dbuser";
    /** Key des Passworts in der Config-Datei */
    public static final String KEY_DBPWD = "dbpwd";
    /** Key des Datenbanknamens in der Config-Datei */
    public static final String KEY_DBNAME = "dbname";
    /** Key der DB-URL in der Config-Datei */
    public static final String KEY_DBURL = "dburl";
    /** Key des DB-URL-Suffix in der Config-Datei */
    public static final String KEY_DBURL_SUFFIX = "dburl_suffix";
    /** Key des MySQL-Treibers in der Config-Datei */
    public static final String KEY_DB_MYSQL_DRIVER = "db_mysql_driver";
    /** Key des Pfads f�r das ROOT-BLTipp-Verzeichnis */
    public static final String KEY_ROOT_PATH = "root_path";
    /** Ein Array mit allen Schl�sseln, die in einer Konf.-Datei stehen sollen */
    public static final String[] ALL_KEYS = {KEY_DBUSER, KEY_DBPWD, KEY_DBNAME, KEY_DBURL, KEY_DBURL_SUFFIX, KEY_DB_MYSQL_DRIVER,
            KEY_ROOT_PATH};
}
