package Mp3Arranger;

import static Mp3Arranger.Util.spit;
import static Mp3Arranger.Util.spitError;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author toffe boy Aamir
 */
public class Actions {

    private int count = 0;
    

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

    @SuppressWarnings("null")
    public int CopyData(String song, String destination) throws IOException {

        final long START_TIME = System.currentTimeMillis();

        InputStream istream = null;
        OutputStream ostream = null;
        try {
            File afile = new File(song);
            File bfile = new File(destination + File.separator + afile.getName());
            istream = new FileInputStream(afile);
            ostream = new FileOutputStream(bfile);
            byte[] cfile = new byte[1024];

            while ((istream.read(cfile)) > 0) {
                ostream.write(cfile);
                ostream.flush();
            }
            istream.close();
            ostream.close();

            spit("Name : " + bfile.getName());
            spit(afile.getName() + " Copied into: " + bfile.getParent());

            float fsize = bfile.length() / (1024 * 1024f);
            spit("Total size : " + fsize + " MB");

//Delete the file after copying
            afile.delete();

            final long END_TIME = System.currentTimeMillis();

            spit("Copied Successs full");
            spit("Copied in " + ((END_TIME - START_TIME) / 1000) + " seconds");

        } catch (IOException ex) {
            spitError(ex.getMessage());
        }
        return count++;

    }
//
//    public static String getCurrentYear() {
//        InputStream connection;
//        try {
//            spit("Opening Connection");
//            connection = new URL("http://www.timeapi.org/utc/now").openStream();
//            Scanner year = new Scanner(connection);
//            return year.useDelimiter("\\Z").next().substring(0, 4);
//        } catch (IOException ex) {
//            spitError(ex.getLocalizedMessage());
////            spitError("Connection failed getting system date");
//            return getSystemYear();
//        }
//
//    }


}
