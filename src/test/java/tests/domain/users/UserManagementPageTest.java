package tests.domain.users;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import tests.common.CommonConstants;
import tests.domain.login.LoginPageTest;
import tests.driver.CommonChromeDriver;

import static tests.driver.CommonChromeDriver.explicitlyWait;
import static tests.driver.CommonChromeDriver.instance;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2025-03-19
 * Time: 11:24
 */
public class UserManagementPageTest {

    private final static LoginPageTest LOGIN_PAGE_TEST = new LoginPageTest();

    public void assertUpdateTypeSuc() {
        CommonChromeDriver.shot(() -> {
            WebElement webElement = explicitlyWait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("body > div.jq-toast-wrap.bottom-right > div > h2")
            ));
            Assertions.assertEquals("成功", CommonChromeDriver.getText(webElement));
        });
    }

    public void assertUpdateTypeFail() {
        CommonChromeDriver.shot(() -> {
            WebElement webElement = explicitlyWait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("body > div.jq-toast-wrap.bottom-right > div > h2")
            ));
            Assertions.assertNotEquals("成功", CommonChromeDriver.getText(webElement));
        });
    }

    public void updateTypeByUsername(String username, String option) {
        CommonChromeDriver.to(CommonConstants.USER_MANAGEMENT);
        // 查询 username
        instance.findElement(By.cssSelector("#toggle-search")).click();
        instance.findElement(By.cssSelector("#input-username")).click();
        instance.findElement(By.cssSelector("#input-username")).sendKeys(username);
        instance.findElement(By.cssSelector("#toggle-search")).click(); // 失焦触发查询

        // 显示等待请求加载
        explicitlyWait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("#user-list > div > div.user-info > span:nth-child(1)"),
                username)
        );

        // 点击更新用户类型按钮
        instance.findElement(By.cssSelector("#user-list > div > div.actions > button.update-type")).click();
        // 取消
        instance.findElement(By.cssSelector("#cancel-update")).click();
        // 点击更新用户类型按钮
        instance.findElement(By.cssSelector("#user-list > div > div.actions > button.update-type")).click();
        // 选择用户类型
        instance.findElement(By.cssSelector("#new-user-type")).click();
        Select select = new Select(instance.findElement(By.cssSelector("#new-user-type")));
        select.selectByValue(option);
        // 确认
        instance.findElement(By.cssSelector("#confirm-update")).click();
    }

    public void updateTypeByUsernameSuc() {
        // 管理员登录
        LOGIN_PAGE_TEST.login("2040484356777@qq.com", "123456");
        LOGIN_PAGE_TEST.assertLoginSuc();

        // 成功用例
        updateTypeByUsername("mamingsheng103@yeah.net", "2");
        assertUpdateTypeSuc();
        updateTypeByUsername("mamingsheng103@yeah.net", "0");
        assertUpdateTypeSuc();
        updateTypeByUsername("mamingsheng103@yeah.net", "1");
        assertUpdateTypeSuc();

    }

    public void updateTypeByUsernameFail() {
        // 管理员登录
        LOGIN_PAGE_TEST.login("2040484356777@qq.com", "123456");
        LOGIN_PAGE_TEST.assertLoginSuc();

        // 失败用例
        updateTypeByUsername("mamingsheng103@yeah.net", "");
        assertUpdateTypeFail();

    }

}
