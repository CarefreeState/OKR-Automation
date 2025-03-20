package common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
    public static WebDriverWait webDriverWait;

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
        webDriverWait = new WebDriverWait(instance, Duration.ofMillis(CommonConstants.explicitlyWait));
    }

    public static void quit() {
        instance.quit();
        instance = null;
        shotInstance = null;
        webDriverWait = null;
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
        // 堆栈信息的第一个元素是 getStackTrace 方法本身，第二个元素是 shot 方法，第三个元素是调用 shot 的方法
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // 工作目录为相对路径，获得一个唯一的文件路径
        String fileName = String.format(
                "./testdoc/images/automation/chrome/%s/%s/%s-%s-%c.png",
                CommonUtils.nowDate(),
                tag.isBlank() ? "tmp" : tag,
                CommonUtils.nowTime(),
                stackTrace.length >= 3 ? stackTrace[2].getMethodName() : "none",
                CommonUtils.randomChar()
        );
        // 按下快门
        // todo 无法截取 alert 的情况，无头模式的时候是否记录，模拟键盘的截图怎么样？无头模式下模拟键盘的截图是否可以记录
        // 但对于大部分情况下，不会直接用 alert，而是用模态框
        // todo 实战一下 Actions、Robot，比如用 Actions 模拟鼠标双击选中文本并删除再填入文本，Robot 模拟键盘进行截图
        File srcFile = shotInstance.getScreenshotAs(OutputType.FILE);
        // 下载到指定位置
        try {
            FileUtils.copyFile(srcFile, new File(fileName));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // 没法解决请求延迟的问题，请求延迟的等待，必须要根据相应的现象啊！
    public static void waitRead() {
        CommonChromeDriver.webDriverWait.until(driver -> {
            return (Boolean) ((JavascriptExecutor) driver).executeScript("""
                return window.performance.getEntriesByType('resource').every(resource => resource.responseEnd > 0);
            """);
        });
        CommonChromeDriver.webDriverWait.until(driver -> {
            return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        });
    }

}
