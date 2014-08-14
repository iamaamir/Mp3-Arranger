package Mp3Arranger;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author toffe boy Aamir
 */
public class Actions {
    
    private static String tag;
    private static File dirPath;
    private static int count = 1;
    
    public static File[] FilterFiles(String folderPath) {
        File fpath = new File(folderPath);
        if (fpath.isDirectory()) {
            File[] mp3files = fpath.listFiles(new FilenameFilter() {
                
                @Override
                public boolean accept(File dir, String name) {
                    
                    return name.endsWith(".mp3");
                    
                }
            });
            return mp3files;
        }
        return null;
    }
    
    public static void SortFiles() {
        
        System.out.println("Total Found: " + Info.getMp3().length);
        
        for (File mp3 : Info.getMp3()) {
            try {
                Mp3File song = new Mp3File(mp3.toString());
                
                if (song.hasId3v2Tag()) {
                    ID3v2 idv2 = song.getId3v2Tag();
                    switch (Info.getSortBy()) {
                        case "By Artist":
                            tag = idv2.getArtist();
                            tag = (tag == null) ? "Unknown Artist" : tag;
                            dirPath = new File(Info.getTruePath() + "/" + tag);
                            if (!dirPath.exists()) {
                                dirPath.mkdirs();
                                
                            }
                            
                            CopyData(mp3.getAbsolutePath(), dirPath.toString());
                            break;
                        case "By Album":
                            tag = idv2.getAlbum();
                            tag = (tag == null) ? "Unknown Album" : tag;
                            dirPath = new File(Info.getTruePath() + "/" + tag);
                            if (!dirPath.exists()) {
                                dirPath.mkdirs();
                                
                            }
                            CopyData(mp3.toString(), dirPath.toString());
                            break;
                        case "By Genre":
                            tag = idv2.getGenreDescription();
                            tag = (tag == null) ? "Unknown Genere" : tag;
                            dirPath = new File(Info.getTruePath() + "/" + tag);
                            if (!dirPath.exists()) {
                                dirPath.mkdirs();
                                
                            }
                            CopyData(mp3.toString(), dirPath.toString());
                            break;
                        default:
                            System.err.println("Unknow Sort");
                    }
                    
                } else {
                    dirPath = new File(Info.getTruePath() + "/Un-Defined Tag");
                    if (!dirPath.exists()) {
                        dirPath.mkdir();
                    }
                    CopyData(mp3.getCanonicalPath(), dirPath.getAbsolutePath());
                }
                
            } catch (IOException | UnsupportedTagException | InvalidDataException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
            
        }
        
        java.net.URL imgURL = GUI.class.getResource("Img/emoticon_smile.png");
        ImageIcon icon = new ImageIcon(imgURL);
        JLabel taskdoneMsg = new JLabel();
        taskdoneMsg.setIcon(icon);
        taskdoneMsg.setText("<html><body><h3 style = color:Green;>Task Completed Successfully </h3></body></html>");
        
        JOptionPane.showMessageDialog(null, taskdoneMsg, "Done", JOptionPane.PLAIN_MESSAGE);
        Info.setMp3(null);
        
    }
    
    @SuppressWarnings("null")
    public static void CopyData(String song, String destination) throws IOException {
        long start = System.currentTimeMillis();
        
        InputStream istream = null;
        OutputStream ostream = null;
        try {
            File afile = new File(song);
            File bfile = new File(destination + "/" + afile.getName());
            istream = new FileInputStream(afile);
            ostream = new FileOutputStream(bfile);
            byte[] cfile = new byte[1024];
            int len = 0;
            while ((len = istream.read(cfile)) > 0) {
                ostream.write(cfile);
                ostream.flush();
            }
            
            istream.close();
            ostream.close();
            
            System.out.println("Name : " + bfile.getName());
            System.out.println(afile.getName() + " Copied into: " + bfile.getParent());
            
            float fsize = bfile.length() / (1024 * 1024f);
            System.out.println("Total size : " + fsize + " MB");
//            System.out.println(Math.round(fsize * 100.0) / 100.0);

            boolean delete = afile.delete();
            System.out.println(delete);
            
            long end = System.currentTimeMillis();
            long milsec;
            milsec = end - start;
            System.out.println("Copied Successs full");
            System.out.println("Copied in " + (milsec / 1000) + " seconds");
            //System.out.println("Copied in "+ (milsec*0.001)+ " seconds");
            count++;
            Info.setStatis(count);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
}
