package bltip.model;

/**
 * Ein Objekt dieser Klasse repr�sentiert einen Mitspieler.
 *
 * @author Nico
 * @version 30.07.2005
 */
public class User {

    /**
     * Eindeutiger Schluessel
     */
    private final int id;
    /**
     * Name des Tippers
     */
    private final String name;
    /**
     * Bonuspunkte
     */
    private final int extrascore;
    /**
     * Tipppunkte
     */
    private int tipscore;
    /**
     * Tabellenpunkte
     */
    private int tablescore;

    /**
     * Konstruktor
     *
     * @param id   Eindeutiger Schl�ssel des Tippers (wird auch in der DB gespeichert)
     * @param name Name des Tippers
     * @param tipS Tipppunkte
     * @param tabS Tabellenpunkte
     */
    public User(int id, String name, int tipS, int tabS, int extraS) {
        this.id = id;
        this.name = name;
        this.extrascore = extraS;
        this.setScore(tipS, tabS);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.tipscore + this.tablescore + this.extrascore;
    }

    public int getTipscore() {
        return this.tipscore;
    }

    public int getTablescore() {
        return this.tablescore;
    }

    public int getExtrascore() {
        return extrascore;
    }

    public void setScore(int tipScore, int tabScore) {
        this.tipscore = tipScore;
        this.tablescore = tabScore;
    }

    /**
     * Liefert true, wenn das �bergebene Objekt ein User ist und die gleiche ID hat
     *
     * @param obj Auf Gleichheit zu �berpr�fendes Objekt
     * @return true, wenn die ID des �bergebenen Users gleich der internen ist, wird null
     * �bergeben oder ein Objekt mit anderer ID false
     */
    public boolean equals(Object obj) {
        return obj != null && User.class.isAssignableFrom(obj.getClass()) && ((User) obj).getId() == getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    /**
     * Ueberschriebene Methode toString liefert den Namen des Tippers.
     *
     * @return Name des Tippers
     */
    public String toString() {
        return this.name;
    }
}