package tests.domain.avatar;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tests.common.CommonConstants;
import tests.common.CommonUtils;
import tests.domain.login.LoginPageTest;
import tests.driver.CommonChromeDriver;

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

    public void assertUploadFail() {
        CommonChromeDriver.shot(() -> {
            WebElement webElement = explicitlyWait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("body > div.jq-toast-wrap.bottom-right > div > h2")
            ));
            Assertions.assertNotEquals("成功", CommonChromeDriver.getText(webElement));
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

    public int avatarUploadSuc(String... filePath) {
        CommonChromeDriver.to(CommonConstants.AVATAR_MANAGEMENT);

        // 查询现在的元素数
        int oldSize = instance.findElements(By.cssSelector("body > div.container > div.avatar-management > div > div")).size();

        // 上传文件按钮哪怕是 none 也可以 sendKeys
        CommonUtils.sleep(1000); // 强制等待 1 秒防止被限流
        instance.findElement(By.cssSelector("#upload-input")).sendKeys(filePath);

        // 断言
        assertUploadSuc(oldSize);

        // 现在这个位置就是新的头像的序号
        return oldSize;
    }

    public void avatarUploadFail(String filePath) {
        CommonChromeDriver.to(CommonConstants.AVATAR_MANAGEMENT);

        // 上传文件按钮哪怕是 none 也可以 sendKeys
        CommonUtils.sleep(1000); // 强制等待 1 秒防止被限流
        instance.findElement(By.cssSelector("#upload-input")).sendKeys(filePath);

        // 断言
        assertUploadFail();
    }

    public void avatarOperateSuc() {
        // 管理员登录
        LOGIN_PAGE_TEST.login("2040484356777@qq.com", "123456");
        LOGIN_PAGE_TEST.assertLoginSuc();
        // 上传头像测试（必须是绝对路径）
        int index = avatarUploadSuc(CommonConstants.getPath("notice.png"));
        // 删除头像测试
        avatarDeleteSuc(index);
        // 重复上传相同的文件（防止其因为文件名未修改，而没有触发事件）
        index = avatarUploadSuc(CommonConstants.getPath("notice.png"));
        avatarDeleteSuc(index);
        // 上传 jpg 文件
        index = avatarUploadSuc(CommonConstants.getPath("beian.jpg"));
        avatarDeleteSuc(index);
        // 上传多个文件（只生效 notice.png）
        index = avatarUploadSuc(CommonConstants.getPath("notice.png"), "\n", CommonConstants.getPath("beian.jpg"));
        avatarDeleteSuc(index);
    }

    public void avatarOperateFail() {
        // 管理员登录
        LOGIN_PAGE_TEST.login("2040484356777@qq.com", "123456");
        LOGIN_PAGE_TEST.assertLoginSuc();
        // 上传 txt 文件
        avatarUploadFail(CommonConstants.getPath("test.txt"));
        // 上传 txt 改后缀成的 png 文件
        avatarUploadFail(CommonConstants.getPath("bad.png"));
        // 上传 png 文件（大于 3 M）
        avatarUploadFail(CommonConstants.getPath("bigimg.png"));
        // 上传 png 文件（空文件）
        avatarUploadFail(CommonConstants.getPath("empty.png"));
    }

}
