package tests.common;

import cn.hutool.core.util.RandomUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2025-03-19
 * Time: 10:21
 */
public class CommonUtils {

    public final static String BASE_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss:SSS";
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    public final static String TIME_PATTERN = "HHmmssSSS"; // 保证毫秒一定是 3 位
//    public final static String TIME_PATTERN = "HHmmssSS"; // 毫秒如果是 210 就打印 21，如果是 211 还是 211
    public final static DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME_PATTERN);
    public final static DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
    public final static DateFormat TIME_FORMAT = new SimpleDateFormat(TIME_PATTERN);

    public static String nowDateTime() {
        return DATE_TIME_FORMAT.format(new Date());
    }

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

    public static String joining(String delimiter, String... tags) {
        return Optional.ofNullable(tags)
                .map(Arrays::stream)
                .stream()
                .flatMap(stream -> stream)
                .filter(str -> Objects.nonNull(str) && !str.isBlank())
                .collect(Collectors.joining(delimiter));
    }

}
