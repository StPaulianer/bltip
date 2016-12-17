package bltip.gui;

import bltip.common.BlTipException;
import bltip.common.Constants;
import bltip.common.PropertiesConstants;
import bltip.storage.StorageSystem;
import bltip.storage.StorageSystemThread;
import bltip.storage.db.Database;
import bltip.util.BlTipUtility;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Nico
 * @version 30.07.2005
 */
class MainFrame extends JFrame implements ActionListener {

    /**
     * long <code>serialVersionUID</code>.
     */
    private static final long serialVersionUID = -2996843418884692443L;

    private static final boolean DEBUG = false;

    /* ***************************************************************************
     * 
     * Attribute
     * 
     * **************************************************************************
     */

    private final Properties profile;
    private StorageSystem storesys;
    private File cfg_file;
    private File root;
    private boolean init_files_changed = false;

    private JMenuItem mi_close, mi_export_tt, mi_export_ut, mi_results, mi_bltable, mi_usertable, mi_dbcreate,
            mi_dbfill,
            mi_metal, mi_motif, mi_win;

    /**
     * Konstruktor
     */
    private MainFrame() {
        super(Messages.TITLE_MAINFRAME);

        wln("loading profile");
        Properties cfg = new Properties();

        profile = new Properties();
        try {
            profile.load(new FileInputStream(PropertiesConstants.PROFILE));
            String look_and_feel = profile.getProperty(PropertiesConstants.PROFILEKEY_LOOK_AND_FEEL);
            if (look_and_feel != null) {
                switch (look_and_feel) {
                    case GUIConstants.LAFID_METAL:
                        changeLookAndFeel(GUIConstants.PLAF_METAL);
                        break;
                    case GUIConstants.LAFID_WINDOWS:
                        changeLookAndFeel(GUIConstants.PLAF_WINDOWS);
                        break;
                    default:
                        changeLookAndFeel(GUIConstants.PLAF_MOTIF);
                        break;
                }
            }

            String path_cfg_file = profile.getProperty(PropertiesConstants.PROFILEKEY_PROP_FILE);
            if (path_cfg_file != null) {
                this.cfg_file = new File(path_cfg_file);
            }

        } catch (Exception e) {
            // der Benutzer muss eine Konf.-Datei angeben, die die ben�tigten Informationen
            // enth�lt, tut er dies nicht, wird im PropertiesDialog komplett abgebrochen.
            changeLookAndFeel(GUIConstants.PLAF_MOTIF);

            PropertiesDialog pd = new PropertiesDialog(this);
            pd.setLocation(GUIConstants.LOCATION_X, GUIConstants.LOCATION_Y);
            pd.setVisible(true);
        }

        try {
            try {
                cfg.load(new FileInputStream(this.cfg_file));
                this.root = new File(cfg.getProperty(PropertiesConstants.KEY_ROOT_PATH));
            } catch (Exception e) {
                handle(e, e.getMessage());
            }

            wln("connecting to db");
            storesys = new Database(cfg.getProperty(PropertiesConstants.KEY_DBUSER), cfg
                    .getProperty(PropertiesConstants.KEY_DBPWD), cfg.getProperty(PropertiesConstants.KEY_DBNAME), cfg
                    .getProperty(PropertiesConstants.KEY_DBURL), cfg.getProperty(PropertiesConstants
                    .KEY_DBURL_SUFFIX), cfg
                    .getProperty(PropertiesConstants.KEY_DB_MYSQL_DRIVER));

            storesys.connect();
        } catch (BlTipException exc) {
            handle(exc);
        }

        // W�ndowListener hinzufuegen
        addWindowListener(new MyWindowAdapter());

        // Men� erstellen
        JMenuBar menubar = new JMenuBar();
        menubar.add(createFileMenu());
        menubar.add(createWorkMenu());
        menubar.add(createDBMenu());
        menubar.add(createLookAndFeelMenu());
        setJMenuBar(menubar);

        pack();
    }

    /* ***************************************************************************
     * 
     * Methoden
     * 
     * **************************************************************************
     */

    /**
     * Main-Methode
     *
     * @param args unbenutzt
     */
    public static void main(String[] args) {
        MainFrame mfr = new MainFrame();
        mfr.setLocation(GUIConstants.LOCATION_X, GUIConstants.LOCATION_Y);
        mfr.setVisible(true);
    }

    private JMenu createFileMenu() {
        JMenu ret = new JMenu(GUIConstants.FILEMENU_TITLE);
        ret.setMnemonic(GUIConstants.FILEMENU_SHORTCUT);

        // Export
        ret.add(createExportSubMenu());
        // Separator
        ret.addSeparator();
        // Beenden
        mi_close = new JMenuItem(GUIConstants.MENU_CLOSE, GUIConstants.SHORTCUT_CLOSE);
        mi_close.addActionListener(this);
        ret.add(mi_close);

        return ret;
    }

    private JMenu createExportSubMenu() {
        JMenu ret = new JMenu(GUIConstants.MENU_EXPORT);
        ret.setMnemonic(GUIConstants.SHORTCUT_EXPORT);
        // HTML-Tipptabelle
        mi_export_ut = new JMenuItem(GUIConstants.MENU_EXPORT_HTML, GUIConstants.SHORTCUT_EXPORT_HTML);
        mi_export_ut.setToolTipText(GUIConstants.TOOLTIP_EXPORT_HTML);
        mi_export_ut.addActionListener(this);
        ret.add(mi_export_ut);
        // Die getippten Tabellen der Tipper
        mi_export_tt = new JMenuItem(GUIConstants.MENU_EXPORT_HTML_TIPTABLES,
                GUIConstants.SHORTCUT_EXPORT_HTML_TIPTABLES);
        mi_export_tt.setToolTipText(GUIConstants.TOOLTIP_EXPORT_HTML_TIPTABLES);
        mi_export_tt.addActionListener(this);
        ret.add(mi_export_tt);

        return ret;
    }

    /**
     * Erstellt das "Bearbeiten"-Men�
     *
     * @return Das "Bearbeiten"-Men�, niemals <code>null</code>
     */
    private JMenu createWorkMenu() {
        JMenu ret = new JMenu(GUIConstants.WORKMENU_TITLE);
        ret.setMnemonic(GUIConstants.WORKMENU_SHORTCUT);
        // Ergebnisse eingeben
        mi_results = new JMenuItem(GUIConstants.MENU_RESULTS, GUIConstants.SHORTCUT_RESULTS);
        setCtrlAccelerator(mi_results, GUIConstants.SHORTCUT_RESULTS);
        mi_results.setToolTipText(GUIConstants.TOOLTIP_RESULTS);
        mi_results.addActionListener(this);
        ret.add(mi_results);
        // Separator
        ret.addSeparator();
        // BL-Tabelle angucken
        mi_bltable = new JMenuItem(GUIConstants.MENU_BLTABLE, GUIConstants.SHORTCUT_BLTABLE);
        setCtrlAccelerator(mi_bltable, GUIConstants.SHORTCUT_BLTABLE);
        mi_bltable.setToolTipText(GUIConstants.TOOLTIP_BLTABLE);
        mi_bltable.addActionListener(this);
        ret.add(mi_bltable);
        // Tipptabelle angucken
        mi_usertable = new JMenuItem(GUIConstants.MENU_USERTABLE, GUIConstants.SHORTCUT_USERTABLE);
        setCtrlAccelerator(mi_usertable, GUIConstants.SHORTCUT_USERTABLE);
        mi_usertable.setToolTipText(GUIConstants.TOOLTIP_USERTABLE);
        mi_usertable.addActionListener(this);
        ret.add(mi_usertable);

        return ret;
    }

    /**
     * Erstellt das Datenbankmen�
     *
     * @return Das Datenbankmen�, niemals <code>null</code>
     */
    private JMenu createDBMenu() {
        JMenu ret = new JMenu(GUIConstants.DATEBASEMENU_TITLE);
        ret.setMnemonic(GUIConstants.DATABASEMENU_SHORTCUT);
        // Datenbank neu aufsetzen
        mi_dbcreate = new JMenuItem(GUIConstants.MENU_DB_CREATE, GUIConstants.SHORTCUT_DB_CREATE);
        mi_dbcreate.setToolTipText(GUIConstants.TOOLTIP_DB_CREATE);
        mi_dbcreate.addActionListener(this);
        ret.add(mi_dbcreate);
        // Datenbank mit Daten f�llen
        mi_dbfill = new JMenuItem(GUIConstants.MENU_DB_FILL, GUIConstants.SHORTCUT_DB_FILL);
        mi_dbfill.setToolTipText(GUIConstants.TOOLTIP_DB_FILL);
        mi_dbfill.addActionListener(this);
        ret.add(mi_dbfill);

        return ret;
    }

    /**
     * Erstellt das Look and Feel-Men�
     *
     * @return Das Look and Feel-Men�, niemals <code>null</code>
     */
    private JMenu createLookAndFeelMenu() {
        JMenu ret = new JMenu(GUIConstants.LOOK_AND_FEELMENU_TITLE);
        ret.setMnemonic(GUIConstants.LOOK_AND_FEELMENU_SHORTCUT);
        // Metal
        mi_metal = new JMenuItem(GUIConstants.MENU_METAL, GUIConstants.SHORTCUT_METAL);
        mi_metal.addActionListener(this);
        ret.add(mi_metal);
        // Motif
        mi_motif = new JMenuItem(GUIConstants.MENU_MOTIF, GUIConstants.SHORTCUT_MOTIF);
        mi_motif.addActionListener(this);
        ret.add(mi_motif);
        // Windows
        mi_win = new JMenuItem(GUIConstants.MENU_WINDOWS, GUIConstants.SHORTCUT_WINDOWS);
        mi_win.addActionListener(this);
        ret.add(mi_win);

        return ret;
    }

    /**
     * Setzt CTRL-Shortcuts
     */
    private void setCtrlAccelerator(JMenuItem mi, char acc) {
        KeyStroke ks = KeyStroke.getKeyStroke(acc, InputEvent.CTRL_MASK);
        mi.setAccelerator(ks);
    }

    /**
     * Spezifizierung der Aktionen
     *
     * @param e Event
     */
    public void actionPerformed(ActionEvent e) {
        try {
            Object src = e.getSource();
            if (src.equals(mi_close)) {
                // Schlie�en
                closeAndExit();

            } else if (src.equals(mi_export_ut)) {
                // Exportiere die Tipptabelle
                exportUsertable();

            } else if (src.equals(mi_export_tt)) {
                // Exportiere alle getippten Tabellen
                exportTiptables();

            } else if (src.equals(mi_results)) {
                // Ergebnisse eingeben
                fillResults();

            } else if (src.equals(mi_bltable)) {
                // Bundesligatabelle anzeigen
                // StorageSystemThread thread = new StorageSystemThread(
                // storesys,
                // StorageSystemThread.BLTABLE,
                // new Object[]{}
                // );
                // thread.start();
                BLTableDialog bltd = new BLTableDialog(this, Messages.TITLE_BLTABLE_PART1 + (storesys.getActualRound())
                        + Messages.ROUND_EXTENSION, storesys.getBLTable());
                bltd.setLocation(GUIConstants.LOCATION_X, GUIConstants.LOCATION_Y);
                bltd.setVisible(true);

            } else if (src.equals(mi_usertable)) {
                // Tipptabelle anzeigen
                UserTableDialog utd = new UserTableDialog(this, Messages.TITLE_USERTABLE_PART1 + (storesys
                        .getActualRound())
                        + Messages.ROUND_EXTENSION, storesys.getUsertable());
                utd.setLocation(GUIConstants.LOCATION_X, GUIConstants.LOCATION_Y);
                utd.setVisible(true);

            } else if (src.equals(mi_dbcreate)) {
                // Datenbank neu aufsetzen
                int choice = JOptionPane.showConfirmDialog(this, Messages.ENQUIRYMSG_DB_CREATE,
                        Messages.ENQUIRYTITLE_DB_CREATE,
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (choice == JOptionPane.YES_OPTION)
                    storesys.create();

            } else if (src.equals(mi_dbfill)) {
                // Datenbank f�llen
                fillDB();

            } else if (src.equals(mi_metal)) {
                changeLookAndFeel(GUIConstants.PLAF_METAL);
            } else if (src.equals(mi_motif)) {
                changeLookAndFeel(GUIConstants.PLAF_MOTIF);
            } else if (src.equals(mi_win)) {
                changeLookAndFeel(GUIConstants.PLAF_WINDOWS);
            }
        } catch (BlTipException exc) {
            showErrorMessage(exc.getTitle(), exc.getMsg());
        }
    }

    private void exportUsertable() {
        JFileChooser fc = new JFileChooser(root);
        int choice = fc.showSaveDialog(this);
        if (choice != JFileChooser.CANCEL_OPTION) {
            File file = fc.getSelectedFile();
            // die Datei existiert, soll sie ueberschrieben werden?
            if (file.exists()) {
                choice = JOptionPane.showConfirmDialog(this, Messages.ENQUIRYMSG1_OVERWRITE + file.getName()
                        + Messages.ENQUIRYMSG2_OVERWRITE, Messages.ENQUIRYTITLE_OVERWRITE, JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    StorageSystemThread thread = new StorageSystemThread(storesys, StorageSystemThread.EXPORT_USERTABLE,
                            new Object[]{file});
                    thread.start();
                }
            } else {
                StorageSystemThread thread = new StorageSystemThread(storesys, StorageSystemThread.EXPORT_USERTABLE,
                        new Object[]{file});
                thread.start();
            }
        }
    }

    private void exportTiptables() {
        JFileChooser fc = new JFileChooser(root);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int choice = fc.showSaveDialog(this);
        if (choice != JFileChooser.CANCEL_OPTION) {
            File file = fc.getSelectedFile();
            StorageSystemThread thread = new StorageSystemThread(storesys, StorageSystemThread.EXPORT_TIPTABLES,
                    new Object[]{file});
            thread.start();
        }
    }

    private void fillResults() throws BlTipException {
        int r = storesys.getFirstRoundWithoutResults();

        if (r < 1) {
            // wohl noch nix in der db
            showErrorMessage(Messages.ERRORTITLE_NOTHING_IN_DB, Messages.ERRORMSG_NOTHING_IN_DB);
            return;
        }

        // Spieltagsabfrage
        String[] str = new String[Constants.COUNT_OF_ROUNDS];
        for (int i = 0; i < str.length; i++) {
            str[i] = (i + 1) + Messages.ROUND_EXTENSION;
        }
        String chosen_round = (String) JOptionPane.showInputDialog(this, Messages.MSG_ROUNDCHOICE,
                Messages.TITLE_ROUNDCHOICE,
                JOptionPane.QUESTION_MESSAGE, null, str, str[r - 1]);
        if (chosen_round != null) {
            int round = 1;
            for (int i = 0; i < str.length; i++) {
                if (str[i].equals(chosen_round))
                    round = i + 1;
            }
            // Ergebniseingabe
            GamesDialog gd = new GamesDialog(this, Messages.TITLE_RESULTINPUT_PART1 + str[round - 1], storesys, round);
            gd.setLocation(GUIConstants.LOCATION_X, GUIConstants.LOCATION_Y);
            gd.setVisible(true);
        }
    }

    private void fillDB() {
        int choice = JOptionPane.showConfirmDialog(this, Messages.ENQUIRYMSG_DB_FILL, Messages.ENQUIRYTITLE_DB_FILL,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            this.init_files_changed = false;
            final File games = new File(root, "paarungen.txt");
            final File user = new File(root, "tipper.txt");
            final File tips = new File(root, "Tipps");
            if (games.exists() && user.exists() && tips.exists()) {
                setInitFiles();
            } else {
                InitializingDialog initd = new InitializingDialog(this, root);
                initd.setLocation(GUIConstants.LOCATION_X, GUIConstants.LOCATION_Y);
                initd.setVisible(true);
            }

            if (init_files_changed) {
                StorageSystemThread thread = new StorageSystemThread(storesys, StorageSystemThread.DB_FILL,
                        new Object[]{games, user, tips});
                thread.start();
                init_files_changed = false;
            }
        }
    }

    private void changeLookAndFeel(String plaf) {
        try {
            UIManager.setLookAndFeel(plaf);
        } catch (Exception exc) {
            this.showErrorMessage(Messages.ERRORTITLE_CHANGE_LOOKANDFEEL, Messages.ERRORMSG_CHANGE_LOOKANDFEEL);
        }
        SwingUtilities.updateComponentTreeUI(this);
        pack();
    }

    /**
     * Zum Ausgeben von Fehlerfenstern
     *
     * @param title Titel des Errorfensters
     * @param msg   Nachricht, die dem Benutzer angezeigt wird
     */
    public void showErrorMessage(String title, String msg) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Gibt dem <code>InitializingDialog</code> die M�glichkeit, die Dateien zu setzen, die mit
     * diesem Dialog einzugeben sind.
     */
    void setInitFiles() {
        this.init_files_changed = true;
    }

    /**
     * Gibt dem <code>PropertiesDialog</code> die M�glichkeit, dem Hauptfenster die
     * Konfigurationsdatei mitzuteilen.
     *
     * @param file Konfigurationsdatei
     */
    void setConfigFile(File file) {
        this.cfg_file = file;
    }

    private void closeAndExit() {
        try {
            String look_and_feel = GUIConstants.LAFID_MOTIF;
            LookAndFeel laf = UIManager.getLookAndFeel();

            if (laf.getID().equals(GUIConstants.LAFID_METAL))
                look_and_feel = GUIConstants.LAFID_METAL;
            else if (laf.getID().equals(GUIConstants.LAFID_WINDOWS))
                look_and_feel = GUIConstants.LAFID_WINDOWS;

            storesys.disconnect();
            try {
                profile.clear();
                profile.setProperty(PropertiesConstants.PROFILEKEY_PROP_FILE, this.cfg_file.toString());
                profile.setProperty(PropertiesConstants.PROFILEKEY_LOOK_AND_FEEL, look_and_feel);
                profile.store(new FileOutputStream(PropertiesConstants.PROFILE), "Profile");
            } catch (IOException exc) {
                handle(exc, exc.getMessage());
            }
        } catch (BlTipException e) {
            handle(e);
        }
        dispose();
        System.exit(0);
    }

    /**
     * Regelt das Exception-Handling
     *
     * @param e   Tats�chlich aufgetretene Ausnahme
     * @param msg Nachricht im Fehlerfenster
     * @throws BlTipException Bei Datenbankfehlern
     */
    private void handle(Exception e, String msg) throws BlTipException {
        if (e != null) {
            e.printStackTrace();
            throw new BlTipException(Messages.ERRORTITLE_IO_GENERAL, msg);
        } else
            throw new BlTipException(Messages.ERRORTITLE_IO_GENERAL, msg);
    }

    /**
     * Regelt das Exception-Handling
     *
     * @param e Eine Ausnahme
     */
    private void handle(BlTipException e) {
        if (e != null) {
            e.printStackTrace();
            showErrorMessage(e.getTitle(), e.getMsg());
        }
    }

    /**
     * Methode zur Ausgabe von Uhrzeit + Statusmeldung �ber den <code>System.out.println</code>
     * -Stream, wenn <code>DEBUG = true</code> ist.
     *
     * @param str Statusmeldung
     */
    private void wln(String str) {
        if (DEBUG) {
            System.out.println(BlTipUtility.currentTime() + "\tMainFrame\t\t" + str);
        }
    }

    /**
     * um noch Zugriff zu haben, wenn nicht �ber "Beenden" beendet wird
     */
    private class MyWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent event) {
            closeAndExit();
        }
    }
}
