package tests.domain.users;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import tests.common.CommonConstants;
import tests.common.CommonUtils;
import tests.domain.login.LoginPageTest;
import tests.driver.CommonChromeDriver;

import java.util.Objects;

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

    public void assertResetAvatarSuc() {
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

    public void resetAvatar(String username) {
        CommonChromeDriver.to(CommonConstants.USER_MANAGEMENT);
        // 查询 username
        instance.findElement(By.cssSelector("#toggle-search")).click();
        instance.findElement(By.cssSelector("#input-username")).click();
        instance.findElement(By.cssSelector("#input-username")).sendKeys(username);
        instance.findElement(By.cssSelector("#toggle-search")).click(); // 失焦触发查询

        // 显示等待请求加载
        explicitlyWait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("#user-list > div > div.user-info > span:nth-child(1)"),
                username));

        // 放大图片
        instance.findElement(By.cssSelector("#user-list > div > img")).click();
        // 缩小图片
        instance.findElement(By.cssSelector("#avatar-modal > span")).click();
        // 点击重置头像
        instance.findElement(By.cssSelector("#user-list > div > div.actions > button.reset-avatar")).click();
        // 取消
        instance.findElement(By.cssSelector("#cancel-reset")).click();
        // 确认
        instance.findElement(By.cssSelector("#user-list > div > div.actions > button.reset-avatar")).click();
        instance.findElement(By.cssSelector("#confirm-reset")).click();
    }

    public void resetAvatarSuc() {
        // 管理员登录
        LOGIN_PAGE_TEST.login("2040484356777@qq.com", "123456");
        LOGIN_PAGE_TEST.assertLoginSuc();

        // 重置头像
        resetAvatar("mamingsheng103@yeah.net");
        assertResetAvatarSuc();
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
                username));

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

    // 正常情况下很难显示准确等待到响应结束，必须根据系统特性（制造巧合）
    public void assertQueryUsersSuc() {
        CommonChromeDriver.shot(() -> {
            explicitlyWait.until(driver -> {
                String value = driver.findElement(By.cssSelector("#input-current")).getAttribute("value");
                return CommonUtils.hasText(value);
            });
        });
    }

    public void queryUsersNormalCondition(String username, String nickname, String userType) {
        CommonChromeDriver.to(CommonConstants.USER_MANAGEMENT);

        // 查询打开搜索框
        instance.findElement(By.cssSelector("#toggle-search")).click();
        // 查询 username
        instance.findElement(By.cssSelector("#input-username")).clear();
        instance.findElement(By.cssSelector("#input-username")).sendKeys(username);

        // 查询 nickname
        instance.findElement(By.cssSelector("#input-nickname")).clear();
        instance.findElement(By.cssSelector("#input-nickname")).sendKeys(nickname);

        // 人为删除这个页码，制造巧合，请求结束后这里会被渲染（间接的方式）
        instance.findElement(By.cssSelector("#input-current")).clear();

        // 查询 userType
        instance.findElement(By.cssSelector("#select-userType")).click();
        Select select = new Select(instance.findElement(By.cssSelector("#select-userType")));
        select.selectByValue(userType);
        instance.findElement(By.cssSelector("body > div.container > div.navbar > h1")).click();

        // 断言其请求成功并拍照
        assertQueryUsersSuc();
    }

    public void queryUsersNormalConditionSuc() {
        // 管理员登录
        LOGIN_PAGE_TEST.login("2040484356777@qq.com", "123456");
        LOGIN_PAGE_TEST.assertLoginSuc();

        // 执行测试用例
        queryUsersNormalCondition("@", "马", "2");
        queryUsersNormalCondition("@", "马", "1");
        queryUsersNormalCondition("@", "马", "0");
        queryUsersNormalCondition("o-Tr", "微信", "2");
        queryUsersNormalCondition("o-Tr", "微信", "1");
        queryUsersNormalCondition("o-Tr", "微信", "0");

        queryUsersNormalCondition("@", "马", "");
        queryUsersNormalCondition("@", "微信", "");
        queryUsersNormalCondition("o-Tr", "马", "");
        queryUsersNormalCondition("o-Tr", "微信", "");
        queryUsersNormalCondition("@", "", "2");
        queryUsersNormalCondition("@", "", "1");
        queryUsersNormalCondition("@", "", "0");
        queryUsersNormalCondition("", "微信", "2");
        queryUsersNormalCondition("", "微信", "1");
        queryUsersNormalCondition("", "微信", "0");

        queryUsersNormalCondition("@", "", "");
        queryUsersNormalCondition("o-Tr", "", "");
        queryUsersNormalCondition("", "马", "");
        queryUsersNormalCondition("", "微信", "");
        queryUsersNormalCondition("", "", "0");
        queryUsersNormalCondition("", "", "1");
        queryUsersNormalCondition("", "", "2");

        queryUsersNormalCondition("", "", "");
    }

    public void assertQueryUsersByPageParamSuc() {
        CommonChromeDriver.shot(() -> {
            explicitlyWait.until(driver -> {
                String currentInputValue = driver.findElement(By.cssSelector("#input-current")).getAttribute("value");
                String current = CommonChromeDriver.getText(driver.findElement(By.cssSelector("#res-current")));
                String pageSizeInputValue = driver.findElement(By.cssSelector("#input-pageSize")).getAttribute("value");
                String pageSize = CommonChromeDriver.getText(driver.findElement(By.cssSelector("#res-pageSize")));
                // 这两个元素一一对应则为成功查询
                return Objects.equals(current, currentInputValue) && Objects.equals(pageSize, pageSizeInputValue);
            });
        });
    }

    public void queryUsersNormalPageParam(String current, String pageSize) {

        instance.findElement(By.cssSelector("#input-current")).click();
        // 查询 current
        instance.findElement(By.cssSelector("#input-current")).clear();
        instance.findElement(By.cssSelector("#input-current")).sendKeys(current);

        // 查询 nickname
        instance.findElement(By.cssSelector("#input-pageSize")).clear();
        instance.findElement(By.cssSelector("#input-pageSize")).sendKeys(pageSize);

        // 失焦触发查询
        instance.findElement(By.cssSelector("body > div.container > div.navbar > h1")).click();

        // 断言其请求成功并拍照
        assertQueryUsersByPageParamSuc();
    }

    public void queryUsersNormalPageParam(String selector) {
        // 点击 首页/上一页/下一页/尾页
        instance.findElement(By.cssSelector(selector)).click();
        // 断言其请求成功并拍照
        assertQueryUsersByPageParamSuc();
    }

    public void queryUsersNormalPageParamSuc() {

        // 管理员登录
        LOGIN_PAGE_TEST.login("2040484356777@qq.com", "123456");
        LOGIN_PAGE_TEST.assertLoginSuc();

        // 查询打开搜索框
        CommonChromeDriver.to(CommonConstants.USER_MANAGEMENT);
        instance.findElement(By.cssSelector("#toggle-search")).click();

        // 查询 username
        instance.findElement(By.cssSelector("#input-username")).clear();
        instance.findElement(By.cssSelector("#input-username")).sendKeys("o-Tr");

        // 查询 nickname
        instance.findElement(By.cssSelector("#input-nickname")).clear();
        instance.findElement(By.cssSelector("#input-nickname")).sendKeys("微信");

        // 查询 userType
        instance.findElement(By.cssSelector("#select-userType")).click();
        Select select = new Select(instance.findElement(By.cssSelector("#select-userType")));
        select.selectByValue("1");

        // 查询页码、页数
        queryUsersNormalPageParam("", "");
        queryUsersNormalPageParam("", "2");
        queryUsersNormalPageParam("5", "");
        queryUsersNormalPageParam("0", "2");
        queryUsersNormalPageParam("1", "2");
        queryUsersNormalPageParam("5", "0");
        queryUsersNormalPageParam("5", "1");
        queryUsersNormalPageParam("5", "2");

        // 点击 首页/上一页/下一页/尾页
        instance.findElement(By.cssSelector("#input-current")).clear();
        instance.findElement(By.cssSelector("#input-pageSize")).clear();

        // 首页、上一页、首页、下一页
        queryUsersNormalPageParam("body > div.container > div.search-container > div > div.pagination > button:nth-child(2)");
        queryUsersNormalPageParam("body > div.container > div.search-container > div > div.pagination > button:nth-child(3)");
        queryUsersNormalPageParam("body > div.container > div.search-container > div > div.pagination > button:nth-child(2)");
        queryUsersNormalPageParam("body > div.container > div.search-container > div > div.pagination > button:nth-child(4)");

        // 尾页、上一页、尾页、下一页
        queryUsersNormalPageParam("body > div.container > div.search-container > div > div.pagination > button:nth-child(5)");
        queryUsersNormalPageParam("body > div.container > div.search-container > div > div.pagination > button:nth-child(3)");
        queryUsersNormalPageParam("body > div.container > div.search-container > div > div.pagination > button:nth-child(5)");
        queryUsersNormalPageParam("body > div.container > div.search-container > div > div.pagination > button:nth-child(4)");
    }
}
