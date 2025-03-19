package common;

import cn.hutool.core.util.RandomUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2025-03-19
 * Time: 10:21
 */
public class Utils {

    public final static String BASE_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    public final static String TIME_PATTERN = "HH-mm-ss-SS";
    public final static DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
    public final static DateFormat TIME_FORMAT = new SimpleDateFormat(TIME_PATTERN);

    public static String nowDate() {
        return DATE_FORMAT.format(new Date());
    }

    public static String nowTime() {
        return TIME_FORMAT.format(new Date());
    }

    public static char randomChar() {
        return RandomUtil.randomChar(BASE_STRING);
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
