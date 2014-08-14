package Mp3Arranger;

import java.awt.BorderLayout;
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
    JButton browse = new JButton("Browse..");
    JButton go = new JButton("GO");
    
    JFileChooser folder;
    JLabel creadit;
    static JProgressBar wait = new JProgressBar();
    final String items[] = {
        "Select Sort", "By Artist", "By Album", "By Genere"
    };
    JComboBox choice = new JComboBox(items);
    String sortby;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public GUI() {
        
        super(new BorderLayout());
       go.setEnabled(true);
       browse.setEnabled(true);
       choice.setEnabled(true);
        try {
            UIManager.setLookAndFeel(new javax.swing.plaf.nimbus.NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        path = new JTextField("Path", 20);
        JPanel pan1 = new JPanel();
        pan1.add(path);
        add(pan1, BorderLayout.PAGE_START);
        browse.setMnemonic('b');
        browse.addActionListener(this);
        choice.setEditable(false);
        choice.addActionListener(this);
        go.setMnemonic('g');
        JPanel pan2 = new JPanel();
        pan2.add(browse);pan2.add(choice);pan2.add(go);
        add(pan2,BorderLayout.CENTER);
        go.addActionListener(this);
        folder = new JFileChooser();
        folder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        wait.setVisible(false);
        JPanel pan3 = new JPanel();
        pan3.add(wait);
        creadit = new JLabel("Copyright to Aamir khan 2014");
        pan3.add(creadit);
        add(pan3,BorderLayout.SOUTH);
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
                    
                    
                    JOptionPane.showMessageDialog(null, "Sorry We did not Find Mp3\n Select a folder Conatining Mp3 Files", "Oops!!", 0);
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
            
           
            if (Info.getMp3() == null) {
                JOptionPane.showMessageDialog(path, "Reselect the Path", "Error!", JOptionPane.ERROR_MESSAGE);
            } else if (Info.getSortBy() == null) {
                JOptionPane.showMessageDialog(null, "Select a Sort First", "", JOptionPane.INFORMATION_MESSAGE);
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
//                Actions.SortFiles();

//                System.exit(0);
            }
        }
    }

    protected static void showGUI() {
        JFrame gui = new JFrame("Mp3 Arranger");
        gui.setSize(300, 130);
        gui.setDefaultCloseOperation(3);
        JComponent newContentPane = new GUI();
        newContentPane.setOpaque(true);
        gui.setContentPane(newContentPane);
        gui.setResizable(false);
        gui.setVisible(true);
//        gui.pack();
        SwingUtilities.updateComponentTreeUI(gui);
        URL  favicon = GUI.class.getResource("Img/music.png");
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
					
       
                                        @SuppressWarnings({"SleepWhileInLoop", "null"})
                                        @Override
					public void run() {
						
						
						wait.setIndeterminate(true);
						try {
							Thread.sleep(1000);
						}
						catch (InterruptedException ex) {}
						wait.setIndeterminate(false);

						
						while(true){
							Runnable runme = new Runnable() {
                                                                @Override
								public void run() {
									wait.setValue(Info.getStatis());
								}
							};
							SwingUtilities.invokeLater(runme);
							try {
								Thread.sleep(1000);
							}
							catch (InterruptedException ex) {}

                                                        
                                                        
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
    
    public void reEnableButtons(){
        go.setEnabled(true);
       browse.setEnabled(true);
       choice.setEnabled(true);
    }
}
