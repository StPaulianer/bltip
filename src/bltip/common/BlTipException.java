/*
 * Created on 17.09.2004
 */
package bltip.common;

/**
 * <b>Die</b> Ausnahme (schlechthin) f�r den Bundesligatipp.
 * 
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 28.08.2006
 */
public class BlTipException extends Exception {

    /** long <code>serialVersionUID</code>. */
    private static final long serialVersionUID = 4738473375975791836L;

    /** Tats�chlich aufgetretene Ausnahme */
    private final Exception exception;

    /** Titel des Fehlerfensters */
    private final String title;

    /** Nachricht an den Benutzer */
    private final String msg;

    /**
     * Konstruktor mit �bergebener, aufgetretener Ausnahme
     * 
     * @param e Tats�chlich aufgetretene Ausnahme
     * @param title Titel des Fehlerfensters
     * @param msg Nachricht im Fehlerfenster
     */
    public BlTipException(Exception e, String title, String msg) {
        this.exception = e;
        this.title = title;
        this.msg = msg;
    }

    /**
     * Konstruktor ohne vorher aufgetretene Ausnahme, #getException liefert dann
     * <code>null</code>
     * 
     * @param title Titel des Fehlerfensters
     * @param msg Nachricht im Fehlerfenster
     */
    public BlTipException(String title, String msg) {
        this(null, title, msg);
    }

    /**
     * @return Die tats�chlich aufgetretene Ausnahme, kann <code>null</code> sein
     */
    public Exception getException() {
        return this.exception;
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
