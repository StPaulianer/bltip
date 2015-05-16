/*
 * Created on 25.09.2004
 */
package bltip.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Ein Dialog, mit dem die Dateien, in denen die Paarungen, die Namen der Tipper und das
 * Verzeichnis, in dem die Tipps liegen, angegeben werden k�nnen.
 * 
 * @author <a href="mailto:nico.mischok@informatik.uni-oldenburg.de">Nico Mischok</a>
 * @version 09.10.2004 todo icons etc.
 */
public class InitializingDialog extends JDialog implements ActionListener {

    /** long <code>serialVersionUID</code>. */
    private static final long serialVersionUID = -687546557721916026L;
    private File games, user, tips;
    private final File root;

    private final MainFrame mfr;
    private JTextField path_games, path_user, path_tips;
    private JButton button_games, button_user, button_tips, ok, cancel;

    /**
     * Konstruktor initialisiert das Layout
     * 
     * @param mfr Hauptfenster, die �bergabe von <code>null</code> f�hrt zu einer
     *            <code>NullPointerException</code>.
     * @param title Titel des Fensters, die �bergabe von <code>null</code> ist nicht erlaubt.
     * @param root Das BL-Tipp-Hauptverzeichnis, um es dem Benutzer etwas komfortabler zu
     *            gestalten.
     */
    public InitializingDialog(MainFrame mfr, String title, File root) {
        super(mfr, title, true);
        this.mfr = mfr;
        this.root = root;

        initLayout();

        pack();
    }

    private void initLayout() {
        Container cp = getContentPane();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

        JLabel label_games = new JLabel("Paarungen:");
        label_games.setPreferredSize(new Dimension(65, 20));
        JLabel label_user = new JLabel("Tipper:");
        label_user.setPreferredSize(new Dimension(65, 20));
        JLabel label_tips = new JLabel("Tipps:");
        label_tips.setPreferredSize(new Dimension(65, 20));

        path_games = new JTextField(GUIConstants.LENGTH_PATH_TEXTFIELD);
        path_games.setBackground(Color.white);
        path_games.setToolTipText(GUIConstants.TOOLTIP_PATH_GAMES);
        path_user = new JTextField(GUIConstants.LENGTH_PATH_TEXTFIELD);
        path_user.setBackground(Color.white);
        path_user.setToolTipText(GUIConstants.TOOLTIP_PATH_USER);
        path_tips = new JTextField(GUIConstants.LENGTH_PATH_TEXTFIELD);
        path_tips.setBackground(Color.white);
        path_tips.setToolTipText(GUIConstants.TOOLTIP_PATH_TIPS);

        button_games = new JButton("Browse");
        button_games.addActionListener(this);
        button_user = new JButton("Browse");
        button_user.addActionListener(this);
        button_tips = new JButton("Browse");
        button_tips.addActionListener(this);
        ok = new JButton(GUIConstants.BUTTON_OK);
        ok.setMnemonic(GUIConstants.SHORTCUT_BUTTON_OK);
        ok.setPreferredSize(new Dimension(GUIConstants.WIDTH_BUTTONS, GUIConstants.HEIGHT_BUTTONS));
        ok.addActionListener(this);
        cancel = new JButton(GUIConstants.BUTTON_CANCEL);
        cancel.setMnemonic(GUIConstants.SHORTCUT_BUTTON_CANCEL);
        cancel.setPreferredSize(new Dimension(GUIConstants.WIDTH_BUTTONS, GUIConstants.HEIGHT_BUTTONS));
        cancel.addActionListener(this);

        JPanel panel_games = new JPanel(new FlowLayout(FlowLayout.CENTER, GUIConstants.FLOWLAYOUT_BUTTONS_HGAP,
                GUIConstants.FLOWLAYOUT_BUTTONS_VGAP));
        JPanel panel_user = new JPanel(new FlowLayout(FlowLayout.CENTER, GUIConstants.FLOWLAYOUT_BUTTONS_HGAP,
                GUIConstants.FLOWLAYOUT_BUTTONS_VGAP));
        JPanel panel_tips = new JPanel(new FlowLayout(FlowLayout.CENTER, GUIConstants.FLOWLAYOUT_BUTTONS_HGAP,
                GUIConstants.FLOWLAYOUT_BUTTONS_VGAP));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, GUIConstants.FLOWLAYOUT_BUTTONS_HGAP,
                GUIConstants.FLOWLAYOUT_BUTTONS_VGAP));

        panel_games.add(label_games);
        panel_games.add(path_games);
        panel_games.add(button_games);
        panel_user.add(label_user);
        panel_user.add(path_user);
        panel_user.add(button_user);
        panel_tips.add(label_tips);
        panel_tips.add(path_tips);
        panel_tips.add(button_tips);
        buttonPanel.add(ok);
        buttonPanel.add(cancel);

        cp.add(panel_games);
        cp.add(panel_user);
        cp.add(panel_tips);
        cp.add(buttonPanel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src.equals(button_games)) {
            JFileChooser fc = new JFileChooser(root);
            int choice = fc.showOpenDialog(this);
            if (choice != JFileChooser.CANCEL_OPTION) {
                games = fc.getSelectedFile();
                path_games.setText(games.toString());
            }

        } else if (src.equals(button_user)) {
            JFileChooser fc = new JFileChooser(root);
            int choice = fc.showOpenDialog(this);
            if (choice != JFileChooser.CANCEL_OPTION) {
                user = fc.getSelectedFile();
                path_user.setText(user.toString());
            }

        } else if (src.equals(button_tips)) {
            JFileChooser fc = new JFileChooser(root);
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int choice = fc.showOpenDialog(this);
            if (choice != JFileChooser.CANCEL_OPTION) {
                tips = fc.getSelectedFile();
                path_tips.setText(tips.toString());
            }

        } else if (src.equals(ok)) {
            mfr.setInitFiles(games, user, tips);
            dispose();

        } else if (src.equals(cancel)) {
            dispose();

        } else {
            dispose();
        }
    }
}
