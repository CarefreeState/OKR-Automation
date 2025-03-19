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
    public void test0() {
        // 正常情况需要添加 VM Options（-ea -Dfile.encoding=UTF-8）启动 assert 这个 java 关键字的作用
        // 下在 JUnit 单元测试的时候，assert 默认是启动的（断言为 false 就会抛出 AssertionError）
        assert 1 + 1 != 2;
    }

    @Test
    public void test() {
        CommonChromeDriver.test(() -> {
            LoginPageTest loginPageTest = new LoginPageTest();
            loginPageTest.loginSuc();

            // 要注意不同测试用例之间的依赖！

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
