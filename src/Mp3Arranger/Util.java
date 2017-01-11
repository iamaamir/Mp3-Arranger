package Mp3Arranger;

import java.util.Calendar;

/**
 *
 * @author Aamir khan
 */
public class Util {

    static String getSystemYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return (year < 2017) ? "2017" : String.valueOf(year);
    }

    static void spit(String obj) {
        System.out.println(obj);
    }

    static void spitError(String obj) {
        System.err.println(obj);
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
