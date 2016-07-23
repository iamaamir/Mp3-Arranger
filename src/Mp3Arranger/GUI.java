package Mp3Arranger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
        path = new JTextField(ResourceBundle.getBundle("Mp3Arranger/config/Bundle").getString("PATH"), 20);
        JPanel pane = new JPanel();
        pane.add(path);
        add(pane, BorderLayout.PAGE_START);
        
        browse.setMnemonic('b');
        browse.addActionListener(this);
        
        choice.setEditable(false);
        choice.addActionListener(this);
        
        go.setMnemonic('g');
        
        JPanel pane2 = new JPanel();
        pane2.add(browse);
        pane2.add(choice);
        pane2.add(go);
        
        add(pane2, BorderLayout.CENTER);
        
        go.addActionListener(this);
        
        folder = new JFileChooser();
        folder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folder.setMultiSelectionEnabled(false);
        
        wait.setVisible(false);
        
        JPanel pane3 = new JPanel();
        
        pane3.add(wait);
        creadit = new JLabel("Copyright to Aamir khan 2014");
        pane3.add(creadit);
        add(pane3, BorderLayout.SOUTH);
        
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
                File mp3Files[] = Actions.getMp3Files(path.getText());
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

                Thread th = new Thread(new Runnable() {

                    @Override
                    @SuppressWarnings("WaitWhileNotSynced")
                    public void run() {

                        initWait();
                        Actions.SortFiles();
                        wait.setVisible(false);
                        reEnableButtons();
                    }
                });

                th.start();

                System.out.println(th.getName());

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
        URL favicon = GUI.class.getResource(java.util.ResourceBundle.getBundle("Mp3Arranger/config/Bundle").getString("IMG/CONTROL_EQUALIZER_BLUE.PNG"));
        ImageIcon icon = new ImageIcon(favicon);
        gui.setIconImage(icon.getImage());
    }

    void initWait() {

        go.setEnabled(false);
        browse.setEnabled(false);
        choice.setEnabled(false);
        wait.setVisible(true);
        wait.setMinimum(0);
        wait.setMaximum(Info.getMp3().length);
        wait.setStringPainted(true);

        Thread runner = new Thread() {

            @Override
            public void run() {
                wait.setIndeterminate(true);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getCause());
                }
                wait.setIndeterminate(false);

                while (true) {
                    Runnable runme = new Runnable() {
                        @Override
                        public void run() {
                            wait.setValue(Info.getStatis());
                        }
                    };
                    SwingUtilities.invokeLater(runme);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }

                }

            }
        };
        runner.start();
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
}
