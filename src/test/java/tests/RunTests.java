package tests;

import tests.driver.CommonChromeDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tests.domain.login.LoginPageTest;

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
        // 适用 junit assert 工具包
        Assertions.assertEquals(2, 1 + 1);
    }

    @Test
    public void testLogin() {
        CommonChromeDriver.test(() -> {
            // 登录测试
            LoginPageTest loginPageTest = new LoginPageTest();
            loginPageTest.loginSuc();
            loginPageTest.loginFail();
        });
    }


}
