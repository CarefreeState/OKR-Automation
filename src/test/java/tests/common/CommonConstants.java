package tests.common;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2025-03-20
 * Time: 16:49
 */
public interface CommonConstants {

    String PASSWORD = "xxxxxx";
    String NUM_TAG = "03"; // [01, 99]
    long EXPLICITLY_WAIT = 5000; // 允许网速很慢（超时等待就报错，反正就那一个）
    long IMPLICITLY_WAIT = 5000; // 允许网速很慢（超时等待就报错，反正就那一个）

    String RESOURCES_PATH = "D:/javawork/OKR-Automation/src/test/resources/"; // 测试资源的根目录
    String TEST_DOC_PATH = "D:/javawork/OKR-Test-Doc/images/automation/"; // 测试资源的根目录
    static String getPath(String fileName) {
        return RESOURCES_PATH + fileName;
    }


    String LOGIN_PAGE = "https://manage.bitterfree.cn/login.html";
    String USER_MANAGEMENT = "https://manage.bitterfree.cn/user-management.html";
    String AVATAR_MANAGEMENT = "https://manage.bitterfree.cn/avatar-management.html";

}
