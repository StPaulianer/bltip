package bltip.storage.db;

import bltip.common.BlTipException;
import bltip.gui.Messages;
import bltip.util.BlTipUtility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static bltip.storage.db.DBTablesAndStatements.*;

/**
 * Klasse zum Erstellen der Datenbank
 *
 * @author Nico
 * @version 28.08.2006
 */
class DBCreator {

    private final static boolean DEBUG = true;

    /**
     * Erstellt eine neue Datenbankverbindung mit den �bergebenen Parameter
     *
     * @param user         User der Datenbank, die �bergabe von <code>null</code> ist erlaubt, eine
     *                     Verbindung zu einer Datenbank dann aber unrealistisch...
     * @param pwd          Passwort des User, die �bergabe von <code>null</code> ist erlaubt, eine
     *                     Verbindung zu einer Datenbank dann aber unrealistisch...
     * @param name         Name der Datenbank, die �bergabe von <code>null</code> ist erlaubt, eine
     *                     Verbindung zu einer Datenbank dann aber unrealistisch...
     * @param url          URL der Datenbank, die �bergabe von <code>null</code> ist erlaubt, eine
     *                     Verbindung zu einer Datenbank dann aber unrealistisch...
     * @param url_suffix   Ein Anh�ngsel zur URL der Datenbank, die �bergabe von
     *                     <code>null</code> ist erlaubt.
     * @param mysql_driver Der verwendete MySQL-Treiber, die �bergabe von <code>null</code> ist
     *                     erlaubt, eine Verbindung zu einer Datenbank dann aber unrealistisch...
     * @throws BlTipException Bei Datenbank- oder I/O-Fehlern
     */
    public void createDatabase(String user, String pwd, String name, String url, String url_suffix, String mysql_driver)
            throws BlTipException {

        try {
            wln("loading MySQL-Driver");
            Class.forName(mysql_driver).newInstance();
        } catch (Exception e) {
            handle(e, Messages.ERRORTITLE_NO_DBCONNECTION, Messages.ERRORMSG_NO_DBCONNECTION);
            // das wird nix mehr
            return;
        }

        try {
            String dburl = url + (url_suffix != null ? url_suffix : "");
            wln("connecting to " + dburl);
            Connection conn = DriverManager.getConnection(dburl, user, pwd);

            wln("creating statement");
            Statement stmt = conn.createStatement();

            wln("delete " + name + ", if exists");
            stmt.executeUpdate("DROP DATABASE IF EXISTS " + name + ";");

            wln("create " + name);
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + name + ";");

            stmt.executeUpdate("USE " + name);

            /*
             * wln("grant usage to " + user); stmt.executeUpdate("GRANT USAGE ON *.* TO " +
             * user + ";");
             * 
             * 
             * wln("connecting to "+this.dburl+this.dbname+"/"+this.dburl_suffix+
             * " as "+user+" with pwd: "+pwd); conn = DriverManager.getConnection
             * (this.dburl+this.dbname+"/"+this.dburl_suffix, user, pwd);
             * 
             * wln("grant all on " + this.dbname + " to " + user); stmt.executeUpdate(
             * "GRANT ALL ON " + this.dbname + ".* TO " + user + ";" );
             * 
             * wln("set pwd for " + user + " to " + pwd); stmt.executeUpdate(
             * "SET PASSWORD FOR " + user + "@\"%\" = PASSWORD('" + pwd + "');" );
             */
            wln("create table for teams");
            wlnStmt(CREATE_TEAM_TABLE);
            stmt.executeUpdate(CREATE_TEAM_TABLE);

            wln("create table for games");
            wlnStmt(CREATE_GAME_TABLE);
            stmt.executeUpdate(CREATE_GAME_TABLE);

            wln("create table for users");
            wlnStmt(CREATE_USER_TABLE);
            stmt.executeUpdate(CREATE_USER_TABLE);

            wln("create table for tips");
            wlnStmt(CREATE_TIP_TABLE);
            stmt.executeUpdate(CREATE_TIP_TABLE);

            wln("create table for tiptables");
            wlnStmt(CREATE_USER_TIPTABLES_TABLE);
            stmt.executeUpdate(CREATE_USER_TIPTABLES_TABLE);

            wln("closing connection");
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            handle(e, Messages.ERRORTITLE_DB_GENERAL, e.getMessage());
        }
    }

    /**
     * Regelt das Exception-Handling
     *
     * @param e     Ausnahme
     * @param title Titel des Fehlerfensters
     * @param msg   Fehlernachricht an den Benutzer
     * @throws BlTipException Bei Datenbank- oder I/O-Fehlern
     */
    private void handle(Exception e, String title, String msg) throws BlTipException {
        if (e != null) {
            e.printStackTrace();
            throw new BlTipException(title, msg);
        } else
            throw new BlTipException(title, msg);
    }

    /**
     * Methode zur Ausgabe von Uhrzeit + Statusmeldung �ber den System.out.println - Stream,
     * wenn DEBUG = true ist.
     *
     * @param str Statusmeldung
     */
    private void wln(String str) {
        if (DEBUG) {
            System.out.println(BlTipUtility.currentTime() + "\tDBCreator\t\t" + str);
        }
    }

    /**
     * Methode zur Ausgabe von SQL-Statements �ber den System.out.println - Stream
     *
     * @param str Statusmeldung
     */
    private void wlnStmt(String str) {
        if (Database.WRITE_STMTS) {
            System.out.println("SQL-Statement\t\tDBCreator\t\t" + str);
        }
    }
}