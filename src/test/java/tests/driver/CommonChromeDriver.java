package tests.driver;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.common.CommonConstants;
import tests.common.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

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
    public static WebDriverWait explicitlyWait;

    // 每次都是创建新的 window，甚至 cookie 都没有，不必担心有浏览器缓存
    public static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions(); // 创建选项
        options.addArguments("--remote-allow-origins=*"); // 允许所有远程源访问
        options.addArguments("-headless"); // 无头模式
        ChromeDriver chromeDriver = new ChromeDriver(options); // 创建驱动
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(CommonConstants.IMPLICITLY_WAIT)); // 隐式等待
        return chromeDriver; // 发挥驱动
    }

    public static void start() {
        instance = createChromeDriver();
        shotInstance = (TakesScreenshot) instance;
        explicitlyWait = new WebDriverWait(instance, Duration.ofMillis(CommonConstants.EXPLICITLY_WAIT));
    }

    public static void quit() {
        Optional.ofNullable(instance).ifPresent(WebDriver::quit);
        instance = null;
        shotInstance = null;
        explicitlyWait = null;
    }

    /**
     * 线程不安全，不应该多线程调用此方法
     */
    public static void test(Runnable runnable) {
        long now = System.currentTimeMillis();
        try {
            log.info("{} 开始执行测试用例", CommonUtils.nowDateTime());
            start();
            runnable.run();
        } finally {
            quit();
            log.info("{} 测试用例执行结束，共耗时 {} ms",
                    CommonUtils.nowDateTime(),
                    System.currentTimeMillis() - now
            );
        }
    }

    public static void to(String url) {
        instance.navigate().to(url);
//        instance.get(url);
    }
    public static void shot() {
        // 堆栈信息的第一个元素是 getStackTrace 方法本身，第二个元素是 shot 方法，第三个元素是调用 shot 的方法
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String origin = stackTrace.length >= 3 ? stackTrace[2].getMethodName() : "none"; // 先获取 origin，否则调用了其他方法就乱了
        shot(origin);
    }

    public static void shot(Runnable runnable) {
        // 堆栈信息的第一个元素是 getStackTrace 方法本身，第二个元素是 shot 方法，第三个元素是调用 shot 的方法
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String origin = stackTrace.length >= 3 ? stackTrace[2].getMethodName() : "none"; // 先获取 origin，否则调用了其他方法就乱了
        try {
            runnable.run();
        } finally {
            shot(origin);
        }
    }

    public static void shot(String origin) {
        // 工作目录为相对路径，获得一个唯一的文件路径
        String fileName = String.format(
                "./testdoc/images/automation/OKR-Management/chrome/%s/%s/%s-%s-%c.png",
                CommonUtils.nowDate(),
                CommonConstants.NUM_TAG,
                CommonUtils.nowTime(),
                origin,
                CommonUtils.randomChar()
        );
        // 按下快门
        // todo 无法截取 alert 的情况，无头模式的时候是否记录，模拟键盘的截图怎么样？无头模式下模拟键盘的截图是否可以记录，但对于大部分情况下，不会直接用 alert，而是用模态框
        // todo 实战一下 Actions、Robot，比如用 Actions 模拟鼠标双击选中文本并删除再填入文本，Robot 模拟键盘进行截图
        CommonChromeDriver.waitReady();
        File srcFile = shotInstance.getScreenshotAs(OutputType.FILE);
        // 下载到指定位置
        try {
            FileUtils.copyFile(srcFile, new File(fileName));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    // 没法解决请求延迟的问题，请求延迟的等待，必须要根据相应的现象，再书写对应的显示等待
    public static void waitReady() {
        explicitlyWait.until(driver -> {
            return (Boolean) ((JavascriptExecutor) driver).executeScript("""
                return window.performance.getEntriesByType('resource').every(resource => resource.responseEnd > 0);
            """);
        });
        explicitlyWait.until(driver -> {
            return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        });
    }

    public static String getText(WebElement element) {
        return element.getAttribute("innerText");
    }

}
