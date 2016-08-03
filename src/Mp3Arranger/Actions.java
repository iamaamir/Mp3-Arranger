package Mp3Arranger;

import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import static Mp3Arranger.Util.spit;

/**
 *
 * @author Aamir Khan
 */
public class Actions {

    public int count = 0;

    public static File[] findMp3Files(String folderPath) {
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] mp3files = folder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".mp3");
                }
            });
            return mp3files;
        }
        return null;
    }


    public int CopyData(String song, String destination) throws IOException {

        File afile = new File(song);
        File bfile = new File(destination + File.separator + afile.getName());
        FileOutputStream ostream;
        try (FileInputStream istream = new FileInputStream(afile)) {
            ostream = new FileOutputStream(bfile);
            byte[] buffer = new byte[1024];
            while ((istream.read(buffer)) > 0) {
                ostream.write(buffer);
                ostream.flush();
            }
        }
        ostream.close();
        afile.delete();//Delete the file after copying
        spit(afile.getName() + " Copied into: " + bfile.getParent()
        +"\n------------------------------------------------------");
        return ++count;

    }

}
