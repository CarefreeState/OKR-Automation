package tests.domain.permit;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tests.common.CommonConstants;
import tests.domain.login.LoginPageTest;
import tests.driver.CommonChromeDriver;

import static tests.driver.CommonChromeDriver.explicitlyWait;
import static tests.driver.CommonChromeDriver.instance;

/**
 * Created With Intellij IDEA
 * Description: 水平越权测试
 * User: 马拉圈
 * Date: 2025-03-20
 * Time: 22:04
 */
@Slf4j
public class HorizontalOversteppingTest {

    private final static LoginPageTest LOGIN_PAGE_TEST = new LoginPageTest();

    public void assertQueryFail() {
        CommonChromeDriver.shot(() -> {
            explicitlyWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body > div.jq-toast-wrap.bottom-right")));
        });
    }

    public void notLoginQueryFail() {
        instance.manage().deleteAllCookies(); // 删除登录凭据
        CommonChromeDriver.to(CommonConstants.AVATAR_MANAGEMENT);
        assertQueryFail();
        CommonChromeDriver.to(CommonConstants.USER_MANAGEMENT);
        assertQueryFail();
    }


    public void normalUserQueryFail() {
        LOGIN_PAGE_TEST.login("mamingsheng103@yeah.net", "123456");
        LOGIN_PAGE_TEST.assertLoginSuc();
        CommonChromeDriver.to(CommonConstants.AVATAR_MANAGEMENT);
        assertQueryFail();
        CommonChromeDriver.to(CommonConstants.USER_MANAGEMENT);
        assertQueryFail();
    }

    public void blockedUserQueryFail() {
        LOGIN_PAGE_TEST.login("mamingsheng001", "123456");
        LOGIN_PAGE_TEST.assertLoginSuc();
        CommonChromeDriver.to(CommonConstants.AVATAR_MANAGEMENT);
        assertQueryFail();
        CommonChromeDriver.to(CommonConstants.USER_MANAGEMENT);
        assertQueryFail();
    }

}
