package Mp3Arranger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author toffe boy Aamir
 */
public class Actions {

    
    
    private  int count = 0;
    static final URL EMO_IMG_URL = GUI.class.getResource("Img/emoticon_smile.png");
    static final ImageIcon EMO_ICON = new ImageIcon(EMO_IMG_URL);

    public static File[] findMp3Files(String folderPath) {
        File fpath = new File(folderPath);
        if (fpath.isDirectory()) {
            File[] mp3files = fpath.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {

                    return name.toLowerCase().endsWith(".mp3");

                }
            });
            return mp3files;
        }
        return null;
    }

    public  int CopyData(String song, String destination) throws IOException {
        System.out.println(destination);
        long start = System.currentTimeMillis();

        InputStream istream = null;
        OutputStream ostream = null;
        try {
            File afile = new File(song);
            File bfile = new File(destination + File.separator + afile.getName());
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
            
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return count++;

    }

}
