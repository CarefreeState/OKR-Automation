package tests;

import tests.domain.avatar.AvatarManagementPageTest;
import tests.domain.permit.HorizontalOversteppingTest;
import tests.domain.permit.VerticalOversteppingTest;
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
            LoginPageTest loginPageTest = new LoginPageTest();
            // 1. 登录成功
            loginPageTest.loginSuc();
            // 2. 登录失败
            loginPageTest.loginFail();
        });
    }


    @Test
    public void testHorizontalOverstepping() {
        CommonChromeDriver.test(() -> {
            HorizontalOversteppingTest horizontalOversteppingTest = new HorizontalOversteppingTest();
            // 1. 未登录
            horizontalOversteppingTest.notLoginQueryFail();
            // 2. 普通用户
            horizontalOversteppingTest.normalUserQueryFail();
            // 3. 封禁用户
            horizontalOversteppingTest.blockedUserQueryFail();
        });
    }

    @Test
    public void testVerticalOversteppingT() {
        CommonChromeDriver.test(() -> {
            VerticalOversteppingTest verticalOversteppingTest = new VerticalOversteppingTest();
            // 1. 管理员修改自己的类型
            verticalOversteppingTest.adminUpdateSelfTypeFail();
        });
    }

    @Test
    public void testAvatarManagementPage() {
        CommonChromeDriver.test(() -> {
            AvatarManagementPageTest avatarManagementPageTest = new AvatarManagementPageTest();
            // 1. 正常的上传头像和删除
            avatarManagementPageTest.avatarOperateSuc();
        });
    }

    @Test
    public void testAll() {

        CommonChromeDriver.test(() -> {
            // 测试所有功能

        });

    }

}
