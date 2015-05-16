package bltip.valueobject;

/**
 * Ein Objekt dieser Klasse repr�sentiert ein Tipp eines Bundesligaspiels
 * 
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 28.08.2006
 */
public class Tip {

    /***************************************************************************
     * 
     * Attribute
     * 
     **************************************************************************/

    /** Eindeutiger Schluessel der zum Tipp geh�renden Paarung in der Datenbank */
    private final int gameId;

    /**
     * Tore der Heimmannschaft (-1, wenn das Ergebnis noch nicht eingegeben wurde)
     */
    private final int homeresult;

    /**
     * Tore der Ausw�rtsmannschaft (-1, wenn das Ergebnis noch nicht eingegeben wurde)
     */
    private final int guestresult;

    /** Wurde der Joker auf die Paarung gesetzt? */
    private final boolean joker;

    /***************************************************************************
     * 
     * Konstruktoren
     * 
     **************************************************************************/

    /**
     * Konstruktor �bergebenem Ergebnis und Joker
     * 
     * @param gameid Eindeutiger Schl�ssel des zum Tipp geh�renden Spiels
     * @param resultH Heimresultat
     * @param resultG Ausw�rtsresultat
     * @param joke <code>true</code>, wenn Joker auf Paarung gesetzt, <code>false</code> sonst
     */
    public Tip(int gameid, int resultH, int resultG, boolean joke) {
        this.gameId = gameid;
        this.homeresult = resultH;
        this.guestresult = resultG;
        this.joker = joke;
    }

    /***************************************************************************
     * 
     * Getter
     * 
     **************************************************************************/

    /**
     * Liefert den eindeutigen Schl�ssel der Begegnung
     * 
     * @return Eindeutiger Schl�ssel
     */
    public int getGameId() {
        return this.gameId;
    }

    /**
     * Liefert die Heimtore
     * 
     * @return homeresult Heimtore (-1, wenn das Ergebnis noch nicht eingegeben wurde)
     */
    public int getHomeresult() {
        return this.homeresult;
    }

    /**
     * Liefert die Ausw�rtstore
     * 
     * @return guestresult Ausw�rtstore (-1, wenn das Ergebnis noch nicht eingegeben wurde)
     */
    public int getGuestresult() {
        return this.guestresult;
    }

    /**
     * Liefert die Information, ob der Joker auf diese Paarung getippt wurde
     * 
     * @return <code>true</code>, wenn Joker auf Paarung gesetzt, <code>false</code> sonst
     */
    public boolean isJoker() {
        return this.joker;
    }

    /***************************************************************************
     * 
     * Setter
     * 
     **************************************************************************/

    /***************************************************************************
     * 
     * Allgemeine Methoden
     * 
     **************************************************************************/

    /**
     * Liefert true, wenn das �bergebene Objekt ein Game ist und die gleiche ID hat
     * 
     * @param obj Auf Gleichheit zu �berpr�fendes Objekt
     * @return <code>true</code>, wenn die ID des �bergebenen Games gleich der internen ist,
     *         wird <code>null</code> �bergeben oder ein Objekt mit anderer ID
     *         <code>false</code>
     */
    public boolean equals(Object obj) {
        return obj != null && Tip.class.isAssignableFrom(obj.getClass()) && ((Tip) obj).getGameId() == getGameId();
    }

    @Override
    public int hashCode() {
        return getGameId();
    }

    /**
     * Liefert einen String, der die ID und das Tippergebnis enth�lt, im Format ID - HH:GG
     * 
     * @return Tipp im Format ID - HH:GG
     */
    public String toString() {
        return this.getGameId() + " - " + this.getHomeresult() + ":" + this.getGuestresult();
    }
}