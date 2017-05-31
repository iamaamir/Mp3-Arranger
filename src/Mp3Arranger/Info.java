package Mp3Arranger;

import java.io.File;

/**
 *
 * @author Aamir Khan
 */
public class Info {

    private static File[] mp3;
    private static String path;


    public static void setMp3(File[] mp3) {Info.mp3 = mp3;}

    public static File[] getMp3() {return mp3;}

    public static String getPath() {return path;}

    public static void setPath(String path) {Info.path = path;}
}