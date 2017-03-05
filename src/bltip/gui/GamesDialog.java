package bltip.gui;

import bltip.common.BlTipException;
import bltip.model.Game;
import bltip.storage.StorageSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Dieser Dialog zeigt die Spiele eines Spieltages an und erm�glicht es dem User, die
 * Ergebnisse einzugeben.
 *
 * @author Nico
 * @version 05.10.2004
 */
class GamesDialog extends JDialog implements ActionListener {

    /**
     * long <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = 1612835266938432323L;
    private final StorageSystem storesys;
    private final Game[] games;

    private final MainFrame mfr;
    private GamePanel[] gamePanel;
    private JButton ok, cancel;

    /**
     * Konstruktor
     *
     * @param mfr      Mainframe, von dem der Dialog aufgerufen wurde
     * @param title    Titel des Dialogs
     * @param storesys Das Speicherungssystem, das die Daten zur Verf�gung stellt
     * @param round    Spieltag, dessen Spiele angezeigt werden sollen
     * @throws BlTipException Bei Fehlern auf unteren Ebenen
     */
    public GamesDialog(MainFrame mfr, String title, StorageSystem storesys, int round) throws BlTipException {
        super(mfr, title, true);
        this.mfr = mfr;
        this.storesys = storesys;
        games = this.storesys.getGames(round);

        // W�ndowListener hinzufuegen
        addWindowListener(new MyWindowAdapter());

        initLayout();

        pack();
    }

    /**
     * Initialisiert das Layout<br>
     * ACHTUNG: <code>buttonPanel</code> muss bereits initialisiert sein
     */
    private void initLayout() {
        Container cp = getContentPane();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

        gamePanel = new GamePanel[games.length];
        // adden der Panels
        for (int i = 0; i < gamePanel.length; i++) {
            gamePanel[i] = new GamePanel(games[i]);
            cp.add(gamePanel[i]);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, GUIConstants.FLOWLAYOUT_BUTTONS_HGAP,
                GUIConstants.FLOWLAYOUT_BUTTONS_VGAP));

        ok = new JButton(GUIConstants.BUTTON_OK);
        ok.setMnemonic(GUIConstants.SHORTCUT_BUTTON_OK);
        ok.setPreferredSize(new Dimension(GUIConstants.WIDTH_BUTTONS, GUIConstants.HEIGHT_BUTTONS));

        cancel = new JButton(GUIConstants.BUTTON_CANCEL);
        cancel.setMnemonic(GUIConstants.SHORTCUT_BUTTON_CANCEL);
        cancel.setPreferredSize(new Dimension(GUIConstants.WIDTH_BUTTONS, GUIConstants.HEIGHT_BUTTONS));

        ok.addActionListener(this);
        cancel.addActionListener(this);

        buttonPanel.add(ok, BorderLayout.WEST);
        buttonPanel.add(cancel, BorderLayout.EAST);
        cp.add(buttonPanel);
    }

    /**
     * Spezifizierung der Aktionen
     *
     * @param e Event
     */
    public void actionPerformed(ActionEvent e) {
        try {
            Object source = e.getSource();
            if (source.equals(ok)) {
                // Speichern der Ergebnisse und Ende
                saveGames();
                dispose();
            } else if (source.equals(cancel)) {
                // Abbruch ohne Speichern?
                boolean changed = false;
                for (GamePanel aGamePanel : this.gamePanel) {
                    if (aGamePanel.resultChanged()) {
                        changed = true;
                        break;
                    }
                }
                if (changed) {
                    int choice = JOptionPane.showConfirmDialog(this, Messages.ENQUIRYMSG_SAVE_CHANGES,
                            Messages.ENQUIRYTITLE_SAVE_CHANGES, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (choice == JOptionPane.YES_OPTION) {
                        saveGames();
                        dispose();
                    } else if (choice == JOptionPane.NO_OPTION) {
                        dispose();
                    }
                } else
                    dispose();

            } else {
                // Fehlerbehandlung f�r unidentifizierte Quelle.
                dispose();
            }
        } catch (BlTipException exc) {
            handle(exc);
        }
    }

    /**
     * Speichert die eingegebenen Ergebnisse
     *
     * @throws BlTipException Bei Fehlern auf unteren Ebenen
     */
    private void saveGames() throws BlTipException {
        int[] hresults = new int[games.length];
        int[] gresults = new int[games.length];

        for (int i = 0; i < games.length; i++) {
            int[] result = gamePanel[i].getEntries();
            hresults[i] = result[0];
            gresults[i] = result[1];
        }

        storesys.setResults(games, hresults, gresults);
    }

    /**
     * Regelt das Exception-Handling
     *
     * @param e Eine Ausnahme
     */
    private void handle(BlTipException e) {
        if (e != null) {
            e.printStackTrace();
            mfr.showErrorMessage(e.getTitle(), e.getMsg());
        }
    }

    /**
     * Liefert den Dialog selbst
     *
     * @return Der Dialog selbst
     */
    private GamesDialog getGamesDialog() {
        return this;
    }

    /**
     * um noch Zugriff zu haben, wenn nicht �ber "Beenden" beendet wird
     */
    private class MyWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent event) {
            try {
                // Abbruch ohne Speichern?
                boolean changed = false;
                for (GamePanel aGamePanel : gamePanel) {
                    if (aGamePanel.resultChanged()) {
                        changed = true;
                        break;
                    }
                }
                if (changed) {
                    int choice = JOptionPane.showConfirmDialog(getGamesDialog(), Messages.ENQUIRYMSG_SAVE_CHANGES,
                            Messages.ENQUIRYTITLE_SAVE_CHANGES, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (choice == JOptionPane.YES_OPTION) {
                        saveGames();
                        dispose();
                    } else if (choice == JOptionPane.NO_OPTION) {
                        dispose();
                    }
                } else
                    dispose();
            } catch (BlTipException e) {
                handle(e);
            }
        }
    }
}
