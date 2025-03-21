package tests.domain.permit;

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
 * Description: 垂直越权测试
 * User: 马拉圈
 * Date: 2025-03-20
 * Time: 22:49
 */
public class VerticalOversteppingTest {

    private final static LoginPageTest LOGIN_PAGE_TEST = new LoginPageTest();

    public void assertAdminUpdateFail() {
        CommonChromeDriver.shot(() -> {
            // "body > div.jq-toast-wrap.bottom-right > div" getText 是直接忽略标签扫描文本，所以很难准确判断其文本（因为里面包着个 h1 或者其他标签的最终显示的文本）
            WebElement webElement = explicitlyWait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("body > div.jq-toast-wrap.bottom-right > div > h2")
            ));
            Assertions.assertNotEquals("成功", CommonChromeDriver.getText(webElement));
        });
    }

    public void adminUpdateSelfTypeFail() {
        // 管理员登录
        String username = "2040484356777@qq.com";
        LOGIN_PAGE_TEST.login(username, CommonConstants.PASSWORD);
        LOGIN_PAGE_TEST.assertLoginSuc();

        // 查询当前管理员
        CommonChromeDriver.to(CommonConstants.USER_MANAGEMENT);
        instance.findElement(By.cssSelector("#toggle-search")).click();

        // 由于原本是折起来的状态，跟 none 一样是少数不可点击的状态
        explicitlyWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#input-username")));

        instance.findElement(By.cssSelector("#input-username")).click();
        instance.findElement(By.cssSelector("#input-username")).sendKeys(username);
        instance.findElement(By.cssSelector("#toggle-search")).click(); // 失焦触发查询

        // 显示等待请求加载
        explicitlyWait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("#user-list > div > div.user-info > span:nth-child(1)"),
                username));

        // 点击更新用户类型按钮
        instance.findElement(By.cssSelector("#user-list > div > div.actions > button.update-type")).click();
        instance.findElement(By.cssSelector("#new-user-type")).click();

        // 选择下拉框
        Select select = new Select(instance.findElement(By.cssSelector("#new-user-type")));
        select.selectByValue("0");

        // 确认
        instance.findElement(By.cssSelector("#confirm-update")).click();

        // 断言
        assertAdminUpdateFail();
    }

}
