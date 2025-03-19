package tests;

import common.CommonChromeDriver;
import common.Utils;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2025-03-19
 * Time: 11:34
 */
public class RunTests {

    @Test
    public void test() {
        CommonChromeDriver.test(() -> {
            LoginPageTest loginPageTest = new LoginPageTest();
            loginPageTest.login("2040484356777@qq.com", "123456");

            Utils.sleep(5000);
            CommonChromeDriver.instance.findElement(By.cssSelector("#toggle-search")).click();
            Utils.sleep(5000);

//            UserManagementPageTest userManagementPageTest = new UserManagementPageTest();
//            Utils.sleep(2000);

//            AvatarManagementPageTest avatarManagementPageTest = new AvatarManagementPageTest();
//            Utils.sleep(2000);
        });

    }

}
