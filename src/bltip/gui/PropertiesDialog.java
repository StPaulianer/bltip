/*
 * Created on 09.10.2004
 */
package bltip.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bltip.common.PropertiesConstants;

/**
 * Der Dialog, mit dem Benutzer eine Konfigurationsdatei angeben und diese auch �ndern kann.
 * 
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 10.10.2004
 */
public class PropertiesDialog extends JDialog implements ActionListener {

    /** long <code>serialVersionUID</code>. */
    private static final long serialVersionUID = -4967285299208899711L;

    private final MainFrame mfr;

    private JTextField path_cfg;
    private JLabel[] key;
    private JTextField[] value;
    private JButton browse, ok;

    private File cfg;
    private final Properties properties;

    /**
     * Konstruktor initialisiert das Layout
     * 
     * @param mfr Hauptfenster
     * @param title Titel des Fensters
     */
    public PropertiesDialog(MainFrame mfr, String title) {
        this(mfr, title, null);
    }

    /**
     * Konstruktor initialisiert das Layout
     * 
     * @param mfr Hauptfenster
     * @param title Titel des Fensters
     * @param cfg Konfigurationsdatei
     */
    private PropertiesDialog(MainFrame mfr, String title, File cfg) {
        super(mfr, title, true);
        this.mfr = mfr;
        this.cfg = cfg;
        this.properties = new Properties();

        if (this.cfg != null) {
            try {
                this.properties.load(new FileInputStream(this.cfg));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        initLayout();

        pack();
    }

    private void initLayout() {
        Container cp = getContentPane();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

        cp.add(createCFGFilePanel());
        cp.add(createPropertiesPanel());
        cp.add(createButtonPanel());

        pack();
    }

    private void updateLayout() {
        if (this.cfg != null)
            path_cfg.setText(cfg.toString());
        else
            path_cfg.setText("");

        for (int i = 0; i < PropertiesConstants.ALL_KEYS.length; i++) {
            String str_value = this.properties.getProperty(PropertiesConstants.ALL_KEYS[i]);
            key[i].setText(PropertiesConstants.ALL_KEYS[i]);
            value[i].setText(str_value == null ? "" : str_value);
        }
    }

    private JPanel createCFGFilePanel() {
        JLabel label = new JLabel("Konfigurationsdatei:");
        label.setPreferredSize(new Dimension(120, 20));

        path_cfg = new JTextField(GUIConstants.LENGTH_PATH_TEXTFIELD);
        path_cfg.setEditable(false);

        browse = new JButton("Browse");
        browse.addActionListener(this);
        browse.setMnemonic('B');
        browse.setPreferredSize(new Dimension(GUIConstants.WIDTH_BUTTONS, GUIConstants.HEIGHT_BUTTONS));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, GUIConstants.FLOWLAYOUT_DEFAULT_GAP,
                GUIConstants.FLOWLAYOUT_DEFAULT_GAP));
        panel.add(label);
        panel.add(path_cfg);
        panel.add(browse);

        return panel;
    }

    private JPanel createPropertiesPanel() {
        JPanel prop_panel = new JPanel();
        prop_panel.setLayout(new BoxLayout(prop_panel, BoxLayout.Y_AXIS));

        key = new JLabel[PropertiesConstants.ALL_KEYS.length];
        value = new JTextField[PropertiesConstants.ALL_KEYS.length];

        for (int i = 0; i < PropertiesConstants.ALL_KEYS.length; i++) {
            String str_value = this.properties.getProperty(PropertiesConstants.ALL_KEYS[i]);

            JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, GUIConstants.FLOWLAYOUT_DEFAULT_GAP,
                    GUIConstants.FLOWLAYOUT_DEFAULT_GAP));
            key[i] = new JLabel(PropertiesConstants.ALL_KEYS[i]);
            key[i].setPreferredSize(new Dimension(200, 20));

            value[i] = new JTextField(20);
            value[i].setBackground(GUIConstants.COLOR_EDITABLE_TEXTFIELDS);
            value[i].setText(str_value == null ? "" : str_value);

            row.add(key[i]);
            row.add(new JLabel("="));
            row.add(value[i]);

            prop_panel.add(row);
        }

        return prop_panel;
    }

    private JPanel createButtonPanel() {
        ok = new JButton(GUIConstants.BUTTON_OK);
        ok.setMnemonic(GUIConstants.SHORTCUT_BUTTON_OK);
        ok.setPreferredSize(new Dimension(GUIConstants.WIDTH_BUTTONS, GUIConstants.HEIGHT_BUTTONS));
        ok.addActionListener(this);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, GUIConstants.FLOWLAYOUT_BUTTONS_HGAP,
                GUIConstants.FLOWLAYOUT_BUTTONS_VGAP));

        panel.add(ok);
        return panel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src.equals(browse)) {
            JFileChooser fc = new JFileChooser();
            int choice = fc.showDialog(this, "W�hlen Sie eine Konf.-Datei");
            if (choice != JFileChooser.CANCEL_OPTION) {
                cfg = fc.getSelectedFile();
                try {
                    this.properties.clear();
                    this.properties.load(new FileInputStream(this.cfg));
                } catch (FileNotFoundException exc) {
                    exc.printStackTrace();
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
            updateLayout();

        } else if (src.equals(ok)) {
            if (this.cfg != null) {
                try {
                    this.properties.store(new FileOutputStream(this.cfg), "Konf.-Datei");
                    this.mfr.setConfigFile(this.cfg);
                } catch (FileNotFoundException exc) {
                    exc.printStackTrace();
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            } else {
                // Der MainFrame kriegt keine Konf.-Datei, es wird abgebrochen
                // todo k�nnt man wohl noch was besseres machen
                dispose();
                System.exit(0);
            }
            dispose();

        } else {
            dispose();
        }
    }
}
