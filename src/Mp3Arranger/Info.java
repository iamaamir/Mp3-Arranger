package Mp3Arranger;

import java.io.File;

/**
 *
 * @author toffe boy Aamir
 */
public class Info {

    private static String sortBy;
    private static File[] mp3;
    private static String TruePath;
    private static File fileSource;
    private  int statis;

    public static void setFileSource(File fileSource) {
        Info.fileSource = fileSource;
    }

    public static File getFileSource() {
        return fileSource;
    }

    public static void setSortBy(String sortBy) {
        Info.sortBy = sortBy;
    }

    public static String getSortBy() {
        return Info.sortBy;
    }

    public static void setMp3(File[] mp3) {

        Info.mp3 = mp3;

    }

    public static File[] getMp3() {
        return mp3;
    }

    public static String getTruePath() {
        return TruePath;
    }

    public static void setTruePath(String TruePath) {
        Info.TruePath = TruePath;
    }

    public  void setStatis(int statis) {
        this.statis = statis;
    }

    public  int getStatis() {
        return statis;
    }

}
