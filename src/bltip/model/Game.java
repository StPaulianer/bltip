package bltip.model;

/**
 * Ein Objekt dieser Klasse repr�sentiert ein Bundesligaspiel.
 *
 * @author Nico
 * @version 28.08.2006
 */
public class Game {

    /**
     * Eindeutiger Schluessel
     */
    private final int id;
    /**
     * Name (ID) der Heimmannschaft
     */
    private final String home;
    /**
     * Name (ID) der Ausw�rtsmannschaft
     */
    private final String guest;
    /**
     * Tore der Heimmannschaft
     */
    private Integer homeresult;
    /**
     * Tore der Ausw�rtsmannschaft
     */
    private Integer guestresult;

    /**
     * Konstruktor mit �bergebener Heim- und Ausw�rtsmannschaft und Ergebnis
     * @param id      Eindeutiger Schl�ssel des Spiels
     * @param hm      ID der Heimmannschaft
     * @param gst     ID der Ausw�rtsmannschaft
     * @param resultH Heimresultat
     * @param resultG Ausw�rtsresultat
     */
    public Game(int id, String hm, String gst, int resultH, int resultG) {
        this.id = id;
        this.homeresult = resultH;
        this.guestresult = resultG;
        this.home = hm;
        this.guest = gst;
    }

    /**
     * Liefert den eindeutigen Schl�ssel der Begegnung
     *
     * @return Eindeutiger Schl�ssel
     */
    public int getId() {
        return this.id;
    }

    /**
     * Liefert die Heimtore
     *
     * @return homeresult Heimtore
     */
    public Integer getHomeresult() {
        return this.homeresult;
    }

    /**
     * Liefert die Ausw�rtstore
     *
     * @return guestresult Ausw�rtstore
     */
    public Integer getGuestresult() {
        return this.guestresult;
    }

    /**
     * Liefert die ID der Heimmannschaft
     *
     * @return home ID der Heimmannschaft
     */
    public String getHome() {
        return this.home;
    }

    /**
     * Liefert die ID der Ausw�rtsmannschaft
     *
     * @return guest ID der Ausw�rtsmannschaft
     */
    public String getGuest() {
        return this.guest;
    }

    /**
     * Setzt das Ergebnis
     *
     * @param hRes Heimergebnis
     * @param gRes Ausw�rtsergebnis
     */
    public void setResult(int hRes, int gRes) {
        this.homeresult = hRes;
        this.guestresult = gRes;
    }

    /**
     * Liefert true, wenn das �bergebene Objekt ein Game ist und die gleiche ID hat
     *
     * @param obj Auf Gleichheit zu �berpr�fendes Objekt
     * @return true, wenn die ID des �bergebenen Games gleich der internen ist, wird null
     * �bergeben oder ein Objekt mit anderer ID false
     */
    public boolean equals(Object obj) {
        return obj != null && Game.class.isAssignableFrom(obj.getClass()) && ((Game) obj).getId() == getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}