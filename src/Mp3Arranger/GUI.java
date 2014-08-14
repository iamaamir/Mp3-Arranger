package Mp3Arranger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

// Referenced classes of package Mp3Arranger:
//            Info, Actions
public class GUI extends JPanel
        implements ActionListener {

    JTextField path;
    JButton browse = new JButton(java.util.ResourceBundle.getBundle("Mp3Arranger/config/Bundle").getString("BROWSE.."));
    JButton go = new JButton(java.util.ResourceBundle.getBundle("Mp3Arranger/config/Bundle").getString("GO"));
    JFileChooser folder;
    JLabel creadit;
    JProgressBar wait = new JProgressBar();
    JComboBox choice;
    String sortby;
    String[] items = {"Select Sort","By Artist","By Album","By Genre"}; 
    @SuppressWarnings({"LeakingThisInConstructor", "UseOfObsoleteCollectionType"})
    public GUI() {

        super(new BorderLayout());
        choice = new JComboBox(items);
        go.setEnabled(true);
        browse.setEnabled(true);
        choice.setEnabled(true);
        try {
            UIManager.setLookAndFeel(new javax.swing.plaf.nimbus.NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        path = new JTextField(java.util.ResourceBundle.getBundle("Mp3Arranger/config/Bundle").getString("PATH"), 20);
        JPanel pan1 = new JPanel();
        pan1.add(path);
        add(pan1, BorderLayout.PAGE_START);
        browse.setMnemonic('b');
        browse.addActionListener(this);
        choice.setEditable(false);
        choice.addActionListener(this);
        go.setMnemonic('g');
        JPanel pan2 = new JPanel();
        pan2.add(browse);
        pan2.add(choice);
        pan2.add(go);
        add(pan2, BorderLayout.CENTER);
        go.addActionListener(this);
        folder = new JFileChooser();
        folder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        wait.setVisible(false);
        JPanel pan3 = new JPanel();
        pan3.add(wait);
        creadit = new JLabel("Copyright to Aamir khan 2014");
        pan3.add(creadit);
        add(pan3, BorderLayout.SOUTH);
        path.setEditable(false);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == browse) {
            int val = folder.showOpenDialog(this);
            if (val == 0) {
                File source = folder.getSelectedFile();

                Info.setTruePath(source.getPath());
                path.setText(source.getPath());
                File mp3Files[] = Actions.FilterFiles(path.getText());
                if (mp3Files.length != 0) {
                    Info.setMp3(mp3Files);
                } else {

                    JOptionPane.showMessageDialog(null, "Sorry I did not find any Mp3 File \n Select a folder containing .Mp3 Files", "Oops!!", 0);
                }
            }

        }
        if (e.getSource() == choice) {
            String uchoice = (String) choice.getSelectedItem();
            if (uchoice.equals("Select Sort")) {
                JOptionPane.showMessageDialog(null, "Choose a Valid Sort to Arrange Your Files", "Opps!", 0);
            } else {
                sortby = (String) choice.getSelectedItem();
                Info.setSortBy(sortby);
            }
        }
        if (e.getSource() == go) {
            URL attachimg = GUI.class.getResource("Img/error_go.png");
            ImageIcon icon = new ImageIcon(attachimg);
            JLabel msg = new JLabel("<html><body><b>Please Reselect the Path. </b></body></html>");
            msg.setIcon(icon);
            msg.setForeground(Color.darkGray);

            if (Info.getMp3() == null) {

                JOptionPane.showMessageDialog(path, msg, "Oo!", JOptionPane.DEFAULT_OPTION);
            } else if (Info.getSortBy() == null) {
                JOptionPane.showMessageDialog(null, "Select a Sort First!", "", JOptionPane.INFORMATION_MESSAGE);
            } else {

                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        initWait();
                        Actions.SortFiles();
                        wait.setVisible(false);
                        Info.setMp3(null);
                        reEnableButtons();
                    }
                });
                t.start();
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
//        gui.pack();
        SwingUtilities.updateComponentTreeUI(gui);
        URL favicon = GUI.class.getResource(java.util.ResourceBundle.getBundle("Mp3Arranger/config/Bundle").getString("IMG/CONTROL_EQUALIZER_BLUE.PNG"));
        ImageIcon icon = new ImageIcon(favicon);
        gui.setIconImage(icon.getImage());
    }

    void initWait() {
        wait.setMaximumSize(null);
        go.setEnabled(false);
        browse.setEnabled(false);
        choice.setEnabled(false);
        wait.setVisible(true);
        wait.setMinimum(0);
        wait.setMaximum(Info.getMp3().length);
        wait.setStringPainted(true);

        Thread runner = new Thread() {

            @SuppressWarnings({"SleepWhileInLoop", "null"})
            @Override
            public void run() {

                wait.setIndeterminate(true);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
                wait.setIndeterminate(false);

                while (Info.getMp3().length!=0) {
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
