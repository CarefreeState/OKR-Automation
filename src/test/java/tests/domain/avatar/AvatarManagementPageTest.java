package tests.domain.avatar;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tests.common.CommonConstants;
import tests.common.CommonUtils;
import tests.domain.login.LoginPageTest;
import tests.driver.CommonChromeDriver;

import java.util.List;

import static tests.driver.CommonChromeDriver.explicitlyWait;
import static tests.driver.CommonChromeDriver.instance;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2025-03-19
 * Time: 11:25
 */
public class AvatarManagementPageTest {

    private final static LoginPageTest LOGIN_PAGE_TEST = new LoginPageTest();

    // 对弹框进行断言不是很容易，比较混乱
    public void assertUploadSuc(int oldSize) {
        CommonChromeDriver.shot(() -> {
            String selector = String.format("body > div.container > div.avatar-management > div > div:nth-child(%d)", oldSize + 1);
            explicitlyWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(selector)));
        });
    }

    public void assertDeleteSuc(int oldSize) {
        CommonChromeDriver.shot(() -> {
            // 自定义条件
            explicitlyWait.until(driver -> {
                // 等待当前的数量减一
                return oldSize - 1 == driver.findElements(
                        By.cssSelector("body > div.container > div.avatar-management > div > div")
                ).size();
            });
        });
    }

    public void avatarDeleteSuc(int index) {
        CommonChromeDriver.to(CommonConstants.AVATAR_MANAGEMENT);
        // 查询现在的元素数
        int oldSize = instance.findElements(By.cssSelector("body > div.container > div.avatar-management > div > div")).size();

        // 删除头像
        String selector = String.format("body > div.container > div.avatar-management > div > div:nth-child(%d) > div", index);
        instance.findElement(By.cssSelector(selector)).click();
        instance.findElement(By.cssSelector("#cancel-delete")).click();
        instance.findElement(By.cssSelector(selector)).click();
        instance.findElement(By.cssSelector("#confirm-delete")).click();

        // 断言
        assertDeleteSuc(oldSize);
    }

    public int avatarUploadSuc() {
        CommonChromeDriver.to(CommonConstants.AVATAR_MANAGEMENT);

        // 查询现在的元素数
        int oldSize = instance.findElements(By.cssSelector("body > div.container > div.avatar-management > div > div")).size();

        // 上传文件按钮哪怕是 none 也可以 sendKeys
        String filePath = "D:/notice.png";
        CommonUtils.sleep(1000); // 强制等待 1 秒防止被限流
        instance.findElement(By.cssSelector("#upload-input")).sendKeys(filePath);

        // 断言
        assertUploadSuc(oldSize);

        // 现在这个位置就是新的头像的序号
        return oldSize;
    }

    public void avatarOperateSuc() {
        // 管理员登录
        String username = "2040484356777@qq.com";
        LOGIN_PAGE_TEST.login(username, "123456");
        LOGIN_PAGE_TEST.assertLoginSuc();
        // 上传头像测试
        int index = avatarUploadSuc();
        // 删除头像测试
        avatarDeleteSuc(index);
    }

}
