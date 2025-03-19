package tests;

import common.CommonChromeDriver;
import org.openqa.selenium.By;

import static common.CommonChromeDriver.instance;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2025-03-19
 * Time: 11:24
 */
public class LoginPageTest {

    {
        CommonChromeDriver.to("https://manage.bitterfree.cn/login.html");
    }

    public void login(String username, String password) {
        instance.findElement(By.cssSelector("#username")).click();
        instance.findElement(By.cssSelector("#username")).sendKeys(username);
        instance.findElement(By.cssSelector("#password")).click();
        instance.findElement(By.cssSelector("#password")).sendKeys(password);
        instance.findElement(By.cssSelector("#login-form > button")).click();
    }

}
