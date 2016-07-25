package Mp3Arranger;


import static Mp3Arranger.Actions.EMO_ICON;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.*;

// Referenced classes of package Mp3Arranger:
//            Info, Actions
public class GUI extends JPanel implements ActionListener {

    JTextField path;
    JButton browse = new JButton(ResourceBundle.getBundle("Mp3Arranger/config/Bundle").getString("BROWSE.."));
    JButton go = new JButton(ResourceBundle.getBundle("Mp3Arranger/config/Bundle").getString("GO"));
    JFileChooser folder;
    JLabel creadit;
    JProgressBar wait = new JProgressBar();
    JComboBox choice;
    String sortby;
    String[] items = {"Sort By", "By Artist", "By Album", "By Genre"};

    public GUI() {

        super(new BorderLayout());
        choice = new JComboBox(items);

        try {
            UIManager.setLookAndFeel(new javax.swing.plaf.nimbus.NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        path = new JTextField(System.getProperty("user.home") + File.separatorChar + "Music", 20);

        JPanel pane = new JPanel();
        pane.add(path);
        super.add(pane, BorderLayout.PAGE_START);

        browse.setMnemonic('b');
        browse.addActionListener(this);

        choice.setEditable(false);
        choice.addActionListener(this);

        go.setMnemonic('g');

        JPanel pane2 = new JPanel();
        pane2.add(browse);
        pane2.add(choice);
        pane2.add(go);

        super.add(pane2, BorderLayout.CENTER);

        go.addActionListener(this);

        folder = new JFileChooser();
        folder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folder.setMultiSelectionEnabled(false);

        wait.setVisible(false);

        JPanel pane3 = new JPanel();

        pane3.add(wait);
        creadit = new JLabel("Copyright to Aamir khan 2014");
        pane3.add(creadit);
        super.add(pane3, BorderLayout.SOUTH);

        path.setEditable(false);
        Dimension pathPreferredSize = wait.getPreferredSize();
        pathPreferredSize.width = 250;
        wait.setPreferredSize(pathPreferredSize);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == browse) {

            int val = folder.showDialog(GUI.this, "Select");
            if (val == 0) {
                File source = folder.getSelectedFile();

                Info.setTruePath(source.getPath());
                path.setText(source.getPath());
                File mp3Files[] = Actions.findMp3Files(path.getText());
                if (mp3Files.length != 0) {
                    Info.setMp3(mp3Files);
                } else {
                    JOptionPane.showMessageDialog(null, "No Mp3 Found\n Select a folder containing .Mp3 Files", "Oops!!", 0);
                }
            }

        }
        if (e.getSource() == choice) {
            String uchoice = (String) choice.getSelectedItem();
            if (uchoice.equals("Sort By")) {
                JOptionPane.showMessageDialog(null, "Choose a Valid Sort to Arrange Your Files", "Opps!", 0);
            } else {
                sortby = (String) choice.getSelectedItem();
                Info.setSortBy(sortby);
            }
        }
        if (e.getSource() == go) {
            final URL ERROR_IMG = GUI.class.getResource("Img/error_go.png");
            final ImageIcon icon = new ImageIcon(ERROR_IMG);
            JLabel errorMsgLabel = new JLabel("<html><body><b>Please Reselect the Path. </b></body></html>");
            errorMsgLabel.setIcon(icon);
            errorMsgLabel.setForeground(Color.darkGray);

            if (Info.getMp3() == null) {
                JOptionPane.showMessageDialog(path, errorMsgLabel, "Oo!", JOptionPane.ERROR_MESSAGE);
            } else if (Info.getSortBy() == null) {
                JOptionPane.showMessageDialog(null, "Please Select a Sort to Proceed", "Error!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                disableButtons();
                runTask();
                reEnableButtons();
            }
        }
    }

    protected static void showGUI() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screen = tk.getScreenSize();
        int sheight = screen.height / 4;
        int swidth = screen.width / 4;
        JFrame gui = new JFrame("Mp3 Arranger");
        gui.setSize(300, 130);
        gui.setLocation(swidth + gui.getWidth() / 2, sheight + gui.getHeight() / 2);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent newContentPane = new GUI();
        newContentPane.setOpaque(true);
        gui.setContentPane(newContentPane);
        gui.setResizable(false);
        gui.setVisible(true);
        SwingUtilities.updateComponentTreeUI(gui);
        URL favicon = GUI.class.getResource(ResourceBundle.getBundle("Mp3Arranger/config/Bundle").getString("IMG/CONTROL_EQUALIZER_BLUE.PNG"));
        ImageIcon icon = new ImageIcon(favicon);
        gui.setIconImage(icon.getImage());
    }

    void disableButtons() {
        go.setEnabled(false);
        browse.setEnabled(false);
        choice.setEnabled(false);
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                GUI.showGUI();
            }
        });

    }

    public void reEnableButtons() {
        go.setEnabled(true);
        browse.setEnabled(true);
        choice.setEnabled(true);
    }

    private void runTask() {
        SwingWorker<Void, Void> task = new SwingWorker<Void, Void>() {
            private String tag;
            private File dirPath;
            Actions dataHandler = new Actions();
            int tracker;

            @Override
            protected Void doInBackground() throws Exception {
                wait.setVisible(true);
                wait.setMinimum(0);
                wait.setMaximum(Info.getMp3().length);
                wait.setStringPainted(true);

                String SLASH = File.separator;

                for (File mp3 : Info.getMp3()) {
                    try {
                        Mp3File song = new Mp3File(mp3.getAbsolutePath());

                        if (song.hasId3v2Tag()) {
                            ID3v2 idv2 = song.getId3v2Tag();
                            switch (Info.getSortBy()) {
                                case "By Artist":
                                    tag = idv2.getArtist();
                                    tag = (tag == null) ? "Unknown Artist" : tag;
                                    dirPath = new File(Info.getTruePath() + SLASH + tag);
                                    if (!dirPath.exists()) {
                                        dirPath.mkdirs();

                                    }
                                    tracker = dataHandler.CopyData(mp3.getAbsolutePath(), dirPath.toString());
                                    wait.setValue(tracker);
                                    break;
                                case "By Album":
                                    tag = idv2.getAlbum();
                                    tag = (tag == null) ? "Unknown Album" : tag;
                                    dirPath = new File(Info.getTruePath() + SLASH + tag);
                                    if (!dirPath.exists()) {
                                        dirPath.mkdirs();

                                    }
                                    tracker = dataHandler.CopyData(mp3.getAbsolutePath(), dirPath.toString());
                                    wait.setValue(tracker);
                                    break;
                                case "By Genre":
                                    tag = idv2.getGenreDescription();
                                    tag = (tag == null) ? "Unknown Genere" : tag;
                                    dirPath = new File(Info.getTruePath() + SLASH + tag);
                                    if (!dirPath.exists()) {
                                        dirPath.mkdirs();

                                    }
                                    tracker = dataHandler.CopyData(mp3.getAbsolutePath(), dirPath.toString());
                                    wait.setValue(tracker);
                                    break;
                                default:
                                    System.err.println("Unknow Sort");
                            }

                        } else {
                            dirPath = new File(Info.getTruePath() + SLASH + "Un-Defined Tag");
                            if (!dirPath.exists()) {
                                dirPath.mkdir();
                            }
                            tracker = dataHandler.CopyData(mp3.getAbsolutePath(), dirPath.toString());
                            wait.setValue(tracker);
                        }

                    } catch (IOException | UnsupportedTagException | InvalidDataException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }

                }

                JLabel taskdoneMsg = new JLabel();
                taskdoneMsg.setIcon(EMO_ICON);
                taskdoneMsg.setText("<html><body><h3 style = color:Green;>Task Completed Successfully </h3></body></html>");

                JOptionPane.showMessageDialog(null, taskdoneMsg, "Done", JOptionPane.PLAIN_MESSAGE);

                return null;
            }

            @Override
            protected void done() {
                Info.setMp3(null);
                wait.setVisible(false);
            }

        };

        task.execute();
    }
}
