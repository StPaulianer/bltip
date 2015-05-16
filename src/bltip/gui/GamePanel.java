/*
 * Created on 20.07.2003
 */
package bltip.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bltip.common.Constants;
import bltip.valueobject.Game;

/**
 * Panel f�r ein Bundesligaspiel
 * 
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 30.07.2005
 */
public class GamePanel extends JPanel {

    /** long <code>serialVersionUID</code>. */
    private static final long serialVersionUID = 8253579237405615433L;

    private final Game game;

    private JTextField homeresult, guestresult;

    /**
     * Konstruktor
     * 
     * @param gm Bundesligaspiel
     */
    public GamePanel(Game gm) {
        game = gm;
        initLayout();
    }

    private void initLayout() {
        // Heim- und Ausw�rtsmannschaft
        JLabel home = new JLabel(game.getHome());
        JLabel guest = new JLabel(game.getGuest());
        home.setPreferredSize(new Dimension(GUIConstants.WIDTH_TEAMLABEL, GUIConstants.HEIGHT_TEAMLABEL));
        guest.setPreferredSize(new Dimension(GUIConstants.WIDTH_TEAMLABEL, GUIConstants.HEIGHT_TEAMLABEL));

        // Heim- und Ausw�rtsergebnis
        homeresult = new JTextField(GUIConstants.LENGTH_RESULT_TEXTFIELD);
        guestresult = new JTextField(GUIConstants.LENGTH_RESULT_TEXTFIELD);
        homeresult.setBackground(Color.white);
        guestresult.setBackground(Color.white);
        homeresult.setEditable(true);
        guestresult.setEditable(true);
        paintResult();

        // und adden...
        setLayout(new FlowLayout(FlowLayout.CENTER, GUIConstants.FLOWLAYOUT_DEFAULT_GAP, GUIConstants.FLOWLAYOUT_DEFAULT_GAP));
        add(home);
        add(new JLabel(GUIConstants.TEAM_SEPARATOR));
        add(guest);
        add(homeresult);
        add(new JLabel(GUIConstants.RESULT_SEPARATOR));
        add(guestresult);
    }

    /**
     * Liefert die Information, ob ein Ergebnis eingegeben wurde, das noch nicht gespeichert
     * wurde
     * 
     * @param <code>true</code>, falls ein Ergebnis eingegeben wurde, das noch nicht
     *        gespeichert wurde, <code>false</code> sonst
     */
    public boolean resultChanged() {
        try {
            int hresult = Integer.parseInt(homeresult.getText().trim());
            int gresult = Integer.parseInt(guestresult.getText().trim());
            return hresult != game.getHomeresult() || gresult != game.getGuestresult();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Liefert das eingegebene Ergebnis in einem zweielementigen Array, in dem unter Index 0
     * das Heimergebnis und unter Index 1 das Ausw�rtsergebnis gespeichert ist.<br>
     * Ist das Heim- oder das Auw�rtsergebnis nicht bzw. falsch eingegeben, wird ein Feld mit
     * zwei Eintr�gen der Form <code>bltipp.common.Constants#NO_RESULT</code> geliefert, also
     * niemals <code>null</code>.
     * 
     * @return Heim- und Ausw�rtsergebnis in einem zweielementigen Array. Unter Index 0 ist das
     *         Heimergebnis und unter Index 1 das Ausw�rtsergebnis gespeichert. Ist das Heim-
     *         oder das Auw�rtsergebnis nicht bzw. falsch eingegeben, wird ein Feld mit zwei
     *         Eintr�gen der Form <code>bltipp.common.Constants#NO_RESULT</code> geliefert,
     *         also niemals <code>null</code>.
     */
    public int[] getEntries() {
        try {
            int hresult = Integer.parseInt(homeresult.getText().trim());
            int gresult = Integer.parseInt(guestresult.getText().trim());
            if (hresult < 0 || gresult < 0) {
                hresult = Constants.NO_RESULT;
                gresult = Constants.NO_RESULT;
            }
            return new int[]{hresult, gresult};
        } catch (NumberFormatException e) {
            return new int[]{Constants.NO_RESULT, Constants.NO_RESULT};
        }
    }

    /** Setzt die Ergebnisse in den Textfeldern neu */
    void paintResult() {
        if (game.getHomeresult() != Constants.NO_RESULT && game.getGuestresult() != Constants.NO_RESULT) {
            homeresult.setText("" + game.getHomeresult());
            guestresult.setText("" + game.getGuestresult());
        } else {
            homeresult.setText("");
            guestresult.setText("");
        }
    }
}
