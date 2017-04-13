package Mp3Arranger;

import java.io.File;
import java.net.URL;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.io.IOException;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import com.mpatric.mp3agic.ID3v2;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import com.mpatric.mp3agic.Mp3File;
import java.awt.event.ActionListener;
import static Mp3Arranger.Util.spitError;
import static Mp3Arranger.Util.getSystemYear;
import com.mpatric.mp3agic.InvalidDataException;
import static java.util.ResourceBundle.getBundle;
import com.mpatric.mp3agic.UnsupportedTagException;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Aamir Khan
 */
// Referenced classes of package Mp3Arranger:
//            Info, Actions
public class GUI extends JPanel implements ActionListener {

    private JPanel pane, buttonsPane, bottomPane;
    private JTextField path;
    private JButton browse, go;
    private JFileChooser folder;
    private JLabel credit;
    private JProgressBar wait;
    private JComboBox<String> choice;
    private final String[] items = {"Sort By", "By Artist", "By Album", "By Genre"};
    private JLabel taskdoneMsg;

    public GUI() {

        super(new BorderLayout());
        initComponents();

        pane.add(path);
        super.add(pane, BorderLayout.PAGE_START);

        browse.setMnemonic('b');
        browse.addActionListener(this);

        choice.setEditable(false);
        choice.addActionListener(this);

        go.setMnemonic('g');

        buttonsPane.add(browse);
        buttonsPane.add(choice);
        buttonsPane.add(go);

        super.add(buttonsPane, BorderLayout.CENTER);

        go.addActionListener(this);

        folder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folder.setMultiSelectionEnabled(false);

        wait.setVisible(false);

        bottomPane.add(wait);

        credit = new JLabel("Copyright to Aamir khan " + getSystemYear());
        bottomPane.add(credit);
        super.add(bottomPane, BorderLayout.SOUTH);

        Dimension pathPreferredSize = wait.getPreferredSize();
        pathPreferredSize.width = 250;
        wait.setPreferredSize(pathPreferredSize);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == browse) {

            int val = folder.showDialog(GUI.this, "Select");
            if (val == 0) {
                String source = folder.getSelectedFile().getPath();
                Info.setPath(source);
                path.setText(source);
            }

        }
        if (e.getSource() == choice) {
            String sortBy = choice.getSelectedItem().toString();
            if (sortBy.equals("Sort By")) {
                JOptionPane.showMessageDialog(null, "Choose a Valid Sort to Arrange Your Files", "Opps!", 0);
            } else {
                Info.setSortBy(sortBy);
            }
        }
        if (e.getSource() == go) {

            final URL ERROR_IMG_URL = GUI.class.getResource("Img/error_go.png");
            final ImageIcon ERROR_ICON = new ImageIcon(ERROR_IMG_URL);

            JLabel errorMsgLabel = new JLabel("<html><body><b>"
                    + "No Mp3 Found "
                    + "Please Reselect the folder containing .Mp3 Files"
                    + "</b></body></html>");
            errorMsgLabel.setIcon(ERROR_ICON);
            errorMsgLabel.setForeground(Color.darkGray);

            File mp3Files[] = Actions.findMp3Files(path.getText());
            if (mp3Files.length == 0) {
                JOptionPane.showMessageDialog(path, errorMsgLabel, "Oo!",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (Info.getSortBy() == null) {
                JOptionPane.showMessageDialog(null,
                        "Please Select a Sort type to Proceed",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Info.setMp3(mp3Files);
                disableButtons(true);
                Info.setPath(path.getText());
                runTask();
            }
        }
    }

    private void runTask() {
        SwingWorker<Void, Void> task;
        task = new SwingWorker<Void, Void>() {
            Actions actions = new Actions();

            @Override
            protected Void doInBackground() throws Exception {
                wait.setVisible(true);
                wait.setMinimum(0);
                wait.setMaximum(Info.getMp3().length);

                for (File mp3 : Info.getMp3()) {
                    try {
                        Mp3File song = new Mp3File(mp3.getAbsolutePath());

                        if (song.hasId3v2Tag()) {
                            ID3v2 idv2 = song.getId3v2Tag();

                            switch (Info.getSortBy()) {

                                case "By Artist":
                                    processMp3(mp3, idv2.getArtist(), "Unknown Artist");
                                    break;

                                case "By Album":
                                    processMp3(mp3, idv2.getAlbum(), "Unknown Album");
                                    break;

                                case "By Genre":
                                    processMp3(mp3, idv2.getGenreDescription(), "Unknown Genre");
                                    break;

                                default:
                                    spitError("Unknow Sort");
                            }

                        } else {
                            processMp3(mp3, "Un-Defined-Tag", null);
                        }

                    } catch (IOException | UnsupportedTagException | InvalidDataException ex) {
                        spitError(ex.toString());
                    }

                }

                return null;
            }

            @Override
            protected void done() {
                Info.setMp3(null);//empty the list
                wait.setValue(0);//reset the bar
                wait.setVisible(false);//hide the bar
                disableButtons(false);//enable buttons
                JOptionPane.showMessageDialog(null, taskdoneMsg, "Done", JOptionPane.PLAIN_MESSAGE);
            }

            private void processMp3(File mp3, String tag, String defaultTag) throws IOException {

                tag = (tag == null) ? defaultTag : tag;
                final File destinationFolder = new File(Info.getPath() + File.separator + tag);
                if (!destinationFolder.exists()) {
                    destinationFolder.mkdirs();
                }
                int tracker = actions.CopyData(
                        mp3.getCanonicalPath(),
                        destinationFolder.getCanonicalPath()
                );
                wait.setValue(tracker);
            }

        };

        task.execute();
    }
    //runTask end here

    private static void showGUI() {
        JFrame gui = new JFrame("Mp3 Arranger");
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screen = tk.getScreenSize();

        final int SCREEN_HEIGHT = screen.height / 4;
        final int SCREEN_WIDTH  = screen.width / 4;
        final int GUI_WIDTH     = gui.getWidth() / 2;
        final int GUI_HEIGHT    = gui.getHeight() / 2;
        //Show the window in center
        gui.setLocation(SCREEN_WIDTH + GUI_WIDTH, SCREEN_HEIGHT + GUI_HEIGHT);
        gui.setSize(300, 130);
        gui.setResizable(false);
        gui.setVisible(true);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent newContentPane = new GUI();
        newContentPane.setOpaque(true);
        gui.setContentPane(newContentPane);

        URL favicon = GUI.class.getResource(
                getBundle("Mp3Arranger/config/Bundle").
                getString("FAVICON"));
        ImageIcon icon = new ImageIcon(favicon);
        gui.setIconImage(icon.getImage());
        //update L&F after Startup
        SwingUtilities.updateComponentTreeUI(gui);

    }

    private void initComponents() {
        this.pane = new JPanel();
        this.bottomPane = new JPanel();
        this.wait = new JProgressBar();
        this.buttonsPane = new JPanel();
        this.folder = new JFileChooser();
        this.wait.setStringPainted(true);
        this.choice = new JComboBox<>(items);
        this.go = new JButton(getBundle("Mp3Arranger/config/Bundle").getString("GO"));
        this.browse = new JButton(getBundle("Mp3Arranger/config/Bundle").getString("BROWSE.."));
        this.path = new JTextField(System.getProperty("user.home") + File.separatorChar + "Music", 20);
        this.taskdoneMsg = new JLabel();
        final ImageIcon EMO_ICON = new ImageIcon(GUI.class.getResource("Img/emoticon_smile.png"));
        taskdoneMsg.setIcon(EMO_ICON);
        taskdoneMsg.setText("<html><body><h3 style = color:Green;>Task Completed Successfully </h3></body></html>");
    }

    private void disableButtons(boolean b) {
        //No need to check for buttons since buttonsPane contains only Buttons
        for (Component btn : buttonsPane.getComponents()) {
            btn.setEnabled(!b);
        }
    }

    public static void main(String args[]) {
        //        Change the look and feel to Nimbus
        try {
            UIManager.setLookAndFeel(new javax.swing.plaf.nimbus.NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            spitError(ex.toString());
        }
        //Show the UI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showGUI();
            }
        });

    }

}
