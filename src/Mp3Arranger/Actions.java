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

    private int count = 0;

    public  File[] findMp3Files(String folderPath) {
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


    public int moveTo(File song, String destinationFolder) throws IOException {

        File moveToPath = new File(destinationFolder + File.separator + song.getName());
        FileOutputStream ostream;
        try (FileInputStream istream = new FileInputStream(song)) {
            ostream = new FileOutputStream(moveToPath);
            byte[] buffer = new byte[1024];
            while ((istream.read(buffer)) > 0) {
                ostream.write(buffer);
                ostream.flush();
            }
        }
        ostream.close();
        song.delete();//Delete the file after copying
        spit(song.getName() + " <-- Moved to --> " + moveToPath.getParent()
        +"\n------------------------------------------------------");
        return ++count;

    }

}
