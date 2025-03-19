package common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2025-03-19
 * Time: 10:23
 */
@Slf4j
public class CommonChromeDriver {

    static {
        System.setProperty("webdriver.chrome.driver", "C:/Users/马拉圈/.cache/selenium/chromedriver/win64/134.0.6998.88/chromedriver.exe");
//        WebDriverManager.chromedriver().setup(); // 加载对应版本的驱动（可能有时候很慢）
    }

    public static WebDriver instance;
    public static TakesScreenshot shotInstance;

    public static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions(); // 创建选项
        options.addArguments("--remote-allow-origins=*"); // 允许所有远程源访问
//        options.addArguments("-headless"); // 无头模式
        ChromeDriver chromeDriver = new ChromeDriver(options); // 创建驱动
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // 隐式等待
        return chromeDriver; // 发挥驱动
    }

    public static void start() {
        instance = createChromeDriver();
        shotInstance = (TakesScreenshot) instance;
    }

    public static void to(String url) {
        instance.navigate().to(url);
//        instance.get(url);
    }

    public static void shot(String... tags) {
        // 组合一个目录
        String tag = Optional.ofNullable(tags)
                .map(Arrays::stream)
                .stream()
                .flatMap(stream -> stream)
                .filter(str -> Objects.nonNull(str) && !str.isBlank())
                .collect(Collectors.joining("/"));
        // 工作目录为相对路径，获得一个唯一的文件路径
        String fileName = String.format(
                "./testdoc/images/automation/chrome/%s/%s/%s-%c.png",
                Utils.nowDate(),
                tag.isBlank() ? "tmp" : tag,
                Utils.nowTime(),
                Utils.randomChar()
        );
        // 按快门
        File srcFile = shotInstance.getScreenshotAs(OutputType.FILE);
        // 下载到指定位置
        try {
            FileUtils.copyFile(srcFile, new File(fileName));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void quit() {
        instance.quit();
        instance = null;
        shotInstance = null;
    }

    public static void test(Runnable runnable) {
        start();
        try {
            runnable.run();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            quit();
        }
    }

}
