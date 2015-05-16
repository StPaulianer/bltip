package bltip.valueobject;

import bltip.common.Constants;

/**
 * Ein Objekt dieser Klasse repr�sentiert eine Bundesligamannschaft
 * 
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 30.07.2005
 */
public class Team {

    /*
     * **************************************************************************
     * *
     * 
     * Attribute
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * **************************************************************************
     */

    /** Name der Mannschaft (dieser muss eindeutig sein) */
    private final String name;
    /** Anzahl der Heimsiege */
    private int homewins;
    /** Anzahl der Heimunentschieden */
    private int homeremis;
    /** Anzahl der Heimniederlagen */
    private int homeloses;
    /** Anzahl der Ausw�rtssiege */
    private int guestwins;
    /** Anzahl der Ausw�rtsunentschieden */
    private int guestremis;
    /** Anzahl der Ausw�rtsniederlagen */
    private int guestloses;
    /** Geschossen Heimtore */
    private int homegoals;
    /** Geschossene Ausw�rtstore */
    private int guestgoals;
    /** Heimgegentore */
    private int homegoalsAgainst;
    /** Ausw�rtsgegentore */
    private int guestgoalsAgainst;

    /*
     * **************************************************************************
     * *
     * 
     * Konstruktoren
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * **************************************************************************
     */

    /**
     * Konstruktor mit allen �bergebenen Attributen
     * 
     * @param name Name der Mannschaft
     * @param hwins Anzahl der Heimsiege
     * @param gwins Anzahl der Ausw�rtssiege
     * @param hremis Anzahl der Heimunentschieden
     * @param gremis Anzahl der Ausw�rtsunentschieden
     * @param hloses Anzahl der Heimniederlagen
     * @param gloses Anzahl der Ausw�rtsniederlagen
     * @param hgoals Anzahl der Heimtore
     * @param ggoals Anzahl der Ausw�rtstore
     * @param hgoalsAgainst Anzahl der Heimgegentore
     * @param ggoalsAgainst Anzahl der Ausw�rtsgegentore
     */
    public Team(String name, int hwins, int gwins, int hremis, int gremis, int hloses, int gloses, int hgoals, int ggoals,
            int hgoalsAgainst, int ggoalsAgainst) {

        this.name = name;

        this.setWinsRemisLoses(hwins, hremis, hloses, gwins, gremis, gloses);
        this.setGoals(hgoals, ggoals);
        this.setGoalsAgainst(hgoalsAgainst, ggoalsAgainst);
    }

    /**
     * Copy-Konstruktor
     * 
     * @param team Mannschaft, die kopiert wird
     */
    private Team(Team team) {
        this.name = team.getName();

        this.setWinsRemisLoses(team.getHomewins(), team.getHomeremis(), team.getHomeloses(), team.getGuestwins(), team
                .getGuestremis(), team.getGuestloses());
        this.setGoals(team.getHomegoals(), team.getGuestgoals());
        this.setGoalsAgainst(team.getHomegoalsAgainst(), team.getGoalsAgainst());
    }

    /*
     * **************************************************************************
     * *
     * 
     * Einfache Getter
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * **************************************************************************
     */

    /**
     * Liefert den Namen des Teams
     * 
     * @return Name des Teams
     */
    public String getName() {
        return this.name;
    }

    /**
     * Liefert die Anzahl der Heimsiege
     * 
     * @return Anzahl der Heimsiege
     */
    public int getHomewins() {
        return this.homewins;
    }

    /**
     * Liefert die Anzahl der Heimunentschieden
     * 
     * @return Anzahl der Heimunentschiedena
     */
    public int getHomeremis() {
        return this.homeremis;
    }

    /**
     * Liefert die Anzahl der Heimniederlagen
     * 
     * @return Anzahl der Heimniederlagen
     */
    public int getHomeloses() {
        return this.homeloses;
    }

    /**
     * Liefert die Anzahl der Ausw�rtssiege
     * 
     * @return Anzahl der Ausw�rtssiege
     */
    public int getGuestwins() {
        return this.guestwins;
    }

    /**
     * Liefert die Anzahl der Ausw�rtsunentschieden
     * 
     * @return Anzahl der Ausw�rtsunentschieden
     */
    public int getGuestremis() {
        return this.guestremis;
    }

    /**
     * Liefert die Anzahl der Ausw�rtsniederlagen
     * 
     * @return Anzahl der Ausw�rtsniederlagen
     */
    public int getGuestloses() {
        return this.guestloses;
    }

    /**
     * Liefert die geschossenen Heimtore
     * 
     * @return Anzahl geschossener Heimtore
     */
    public int getHomegoals() {
        return this.homegoals;
    }

    /**
     * Liefert die geschossenen Ausw�rtstore
     * 
     * @return Anzahl geschossener Ausw�rtstore
     */
    public int getGuestgoals() {
        return this.guestgoals;
    }

    /**
     * Liefert die Heimgegentore
     * 
     * @return Anzahl der Heimgegentore
     */
    public int getHomegoalsAgainst() {
        return this.homegoalsAgainst;
    }

    /**
     * Liefert die Ausw�rtsgegentore
     * 
     * @return Anzahl der Ausw�rtsgegentore
     */
    public int getGuestgoalsAgainst() {
        return this.guestgoalsAgainst;
    }

    /*
     * **************************************************************************
     * *
     * 
     * "Zusammengesetzte" Getter
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * **************************************************************************
     */

    /**
     * Liefert die Anzahl der Siege
     * 
     * @return Anzahl der Siege
     */
    public int getWins() {
        return this.homewins + this.guestwins;
    }

    /**
     * Liefert die Anzahl der Unentschieden
     * 
     * @return Anzahl der Unentschieden
     */
    public int getRemis() {
        return this.homeremis + this.guestremis;
    }

    /**
     * Liefert die Anzahl der Niederlagen
     * 
     * @return Anzahl der Niederlagen
     */
    public int getLoses() {
        return this.homeloses + this.guestloses;
    }

    /**
     * Liefert die Anzahl der Heimspiele, die die Mannschaft bereits bestritten hat
     * 
     * @return Anzahl der Heimspiele
     */
    int getCountOfHomegames() {
        return (this.homewins + this.homeremis + this.homeloses);
    }

    /**
     * Liefert die Anzahl der Ausw�rtsspiele, die die Mannschaft bereits bestritten hat
     * 
     * @return Anzahl der Ausw�rtsspiele
     */
    int getCountOfGuestgames() {
        return (this.guestwins + this.guestremis + this.guestloses);
    }

    /**
     * Liefert die Anzahl der Spiele, die die Mannschaft bereits bestritten hat
     * 
     * @return Anzahl der Spiele
     */
    public int getCountOfGames() {
        return (this.getCountOfHomegames() + this.getCountOfGuestgames());
    }

    /**
     * Liefert die Punkte
     * 
     * @return Anzahl der Punkte
     */
    public int getScore() {
        return (this.getHomescore() + this.getGuestscore());
    }

    /**
     * Liefert die Heimpunkte
     * 
     * @return Anzahl der Heimpunkte
     */
    int getHomescore() {
        return (this.homewins * Constants.SCORE_FOR_VICTORY + this.homeremis * Constants.SCORE_FOR_DRAW);
    }

    /**
     * Liefert die Ausw�rtspunkte
     * 
     * @return Anzahl der Ausw�rtspunkte
     */
    int getGuestscore() {
        return (this.guestwins * Constants.SCORE_FOR_VICTORY + this.guestremis * Constants.SCORE_FOR_DRAW);
    }

    /**
     * Liefert die Tordifferenz
     * 
     * @return Tordifferenz
     */
    public int getDifference() {
        return this.getGoals() - this.getGoalsAgainst();
    }

    /**
     * Liefert die geschossenen Tore
     * 
     * @return Anzahl geschossener Tore
     */
    public int getGoals() {
        return this.homegoals + this.guestgoals;
    }

    /**
     * Liefert die Gesamtanzahl der Gegentore
     * 
     * @return Anzahl der Gegentore
     */
    public int getGoalsAgainst() {
        return this.homegoalsAgainst + this.guestgoalsAgainst;
    }

    /*
     * **************************************************************************
     * *
     * 
     * Setter
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * **************************************************************************
     */

    /**
     * Erm�glicht es die Anzahl der Heim- und Ausw�rtssiege, sowie -unentschieden und
     * -niederlagen zu setzen
     * 
     * @param homewins Neue Anzahl der Heimsiege
     * @param homeremis Neue Anzahl der Heimunentschieden
     * @param homeloses Neue Anzahl der Heimniederlagen
     * @param guestwins Neue Anzahl der Ausw�rtssiege
     * @param guestremis Neue Anzahl der Ausw�rtsunentschieden
     * @param guestloses Neue Anzahl der Ausw�rtsniederlagen
     */
    public void setWinsRemisLoses(int homewins, int homeremis, int homeloses, int guestwins, int guestremis, int guestloses) {

        this.homewins = homewins;
        this.homeremis = homeremis;
        this.homeloses = homeloses;
        this.guestwins = guestwins;
        this.guestremis = guestremis;
        this.guestloses = guestloses;
    }

    /** Inkrementiert die Anzahl der Heimsiege */
    public void addHomewin() {
        this.homewins++;
    }

    /** Dekrementiert die Anzahl der Heimsiege */
    public void removeHomewin() {
        this.homewins--;
    }

    /** Inkrementiert die Anzahl der Ausw�rtssiege */
    public void addGuestwin() {
        this.guestwins++;
    }

    /** Dekrementiert die Anzahl der Ausw�rtssiege */
    public void removeGuestwin() {
        this.guestwins--;
    }

    /** Inkrementiert die Anzahl der Heimunentschieden */
    public void addHomeremis() {
        this.homeremis++;
    }

    /** Dekrementiert die Anzahl der Heimunentschieden */
    public void removeHomeremis() {
        this.homeremis--;
    }

    /** Inkrementiert die Anzahl der Ausw�rtsunentschieden */
    public void addGuestremis() {
        this.guestremis++;
    }

    /** Dekrementiert die Anzahl der Ausw�rtsunentschieden */
    public void removeGuestremis() {
        this.guestremis--;
    }

    /** Inkrementiert die Anzahl der Heimniederlagen */
    public void addHomelose() {
        this.homeloses++;
    }

    /** Dekrementiert die Anzahl der Heimniederlagen */
    public void removeHomelose() {
        this.homeloses--;
    }

    /** Inkrementiert die Anzahl der Ausw�rtsniederlagen */
    public void addGuestlose() {
        this.guestloses++;
    }

    /** Dekrementiert die Anzahl der Ausw�rtsniederlagen */
    public void removeGuestlose() {
        this.guestloses--;
    }

    /**
     * Setzt die Heim- sowie Ausw�rtstore
     * 
     * @param hgoals Neue Anzahl geschossener Heimtore
     * @param ggoals Neue Anzahl geschossener Ausw�rtstore
     */
    public void setGoals(int hgoals, int ggoals) {
        this.homegoals = hgoals;
        this.guestgoals = ggoals;
    }

    /**
     * Setzt die Heim- sowie Ausw�rtsgegentore
     * 
     * @param hgoalsAgainst Neue Anzahl Heimgegentore
     * @param ggoalsAgainst Neue Anzahl Ausw�rtsgegentore
     */
    public void setGoalsAgainst(int hgoalsAgainst, int ggoalsAgainst) {
        this.homegoalsAgainst = hgoalsAgainst;
        this.guestgoalsAgainst = ggoalsAgainst;
    }

    /*
     * **************************************************************************
     * *
     * 
     * Allgemeine Methoden
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * **************************************************************************
     */

    /**
     * Liefert true, wenn das �bergebene Objekt ein Team ist und die gleiche ID hat
     * 
     * @param obj Auf Gleichheit zu �berpr�fendes Objekt
     * @return true, wenn die ID des �bergebenen Teams gleich der internen ist, wird
     *         <code>null</code> �bergeben oder ein Objekt mit anderer ID false
     */
    public boolean equals(Object obj) {
        return obj != null && Team.class.isAssignableFrom(obj.getClass()) && ((Team) obj).getName().equals(getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.name;
    }
}