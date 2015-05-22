/*
 * Created on 17.09.2004
 */
package bltip.common;

/**
 * <b>Die</b> Ausnahme (schlechthin) f�r den Bundesligatipp.
 *
 * @author Nico
 * @version 28.08.2006
 */
public class BlTipException extends Exception {

    /**
     * long <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 4738473375975791836L;

    /**
     * Titel des Fehlerfensters
     */
    private final String title;

    /**
     * Nachricht an den Benutzer
     */
    private final String msg;

    /**
     * Konstruktor
     *  @param title Titel des Fehlerfensters
     * @param msg   Nachricht im Fehlerfenster
     */
    public BlTipException(String title, String msg) {
        this.title = title;
        this.msg = msg;
    }

    /**
     * @return Die Nachricht, die den Benutzer erreichen soll
     */
    public String getMsg() {
        return this.msg;
    }

    /**
     * @return Titel des Fehlerfensters
     */
    public String getTitle() {
        return this.title;
    }
}
