package Mp3Arranger;

import java.util.Calendar;

/**
 *
 * @author Aamir khan
 */
public class Util {

    static String getSystemYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return (year < 2016) ? "2016" : String.valueOf(year);
    }

    static void spit(String obj) {
        System.out.println(obj);
    }

    static void spitError(String obj) {
        System.err.println(obj);
    }

}
