package tests.domain.login;

import tests.common.CommonConstants;
import tests.driver.CommonChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static tests.driver.CommonChromeDriver.explicitlyWait;
import static tests.driver.CommonChromeDriver.instance;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2025-03-19
 * Time: 11:24
 */
public class LoginPageTest {

    public void login(String username, String password) {
        CommonChromeDriver.to(CommonConstants.LOGIN_PAGE);
        instance.findElement(By.cssSelector("#username")).clear();
        instance.findElement(By.cssSelector("#username")).click();
        instance.findElement(By.cssSelector("#username")).sendKeys(username);
        instance.findElement(By.cssSelector("#password")).clear();
        instance.findElement(By.cssSelector("#password")).click();
        instance.findElement(By.cssSelector("#password")).sendKeys(password);
        instance.findElement(By.cssSelector("#login-form > button")).click();
    }

    public void assertLoginSuc() {
        // 判断是否登录成功
        // 因为请求延迟，这里的 title 无论结果如何都是原来的
//        String title = instance.findElement(By.cssSelector("head > title")).getAttribute("innerText");
//        Assertions.assertNotEquals("管理员登录", title);
//        Assertions.assertNotEquals(loginPage, instance.getCurrentUrl());
        // 需要通过等待判断用例是否通过（等不到则说明用例失败，显示等待超时抛超时异常）
        CommonChromeDriver.shot(() -> {
            explicitlyWait.until(ExpectedConditions.not(ExpectedConditions.urlContains(CommonConstants.LOGIN_PAGE)));
        });

    }

    public void assertLoginFail() {
        // 判断是否登录失败
        // 判断是否有弹框，超时代表不通过（前提是错误现象真的一定是这个）
        // 根据实际情况：前端的警告弹框都会放在 div.jq-toast-wrap.bottom-right
        CommonChromeDriver.shot(() -> {
            explicitlyWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body > div.jq-toast-wrap.bottom-right")));
        });
    }

    // 命名风格（xxxSuc 代表预期成功的案例）
    public void loginSuc() {
        login("2040484356777@qq.com", "123456");
        assertLoginSuc();
    }

    // 命名风格（xxxFail 代表预期失败的案例）
    public void loginFail() {
        login("mms", "123456");
        assertLoginFail();
        login("2040484356777@qq.com", "666666");
        assertLoginFail();
        login("2040484356777@qq.com", "");
        assertLoginFail();
        login("", "123456");
        assertLoginFail();
        login("", "");
        assertLoginFail();
    }

}
