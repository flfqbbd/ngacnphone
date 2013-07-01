package gov.pianzong.util;

/**
 * Created by Administrator on 13-6-30.
 */
public class StringUtil {
    public static boolean isEmpty(String str) {
        if (str != null && !"".equals(str)) {
            return false;
        } else {
            return true;
        }
    }
}
