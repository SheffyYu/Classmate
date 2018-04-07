package constant;

/**
 * Created by Sheffy on 2018/3/27.
 */

public class ServerUrl {
    //服务器IP地址
    public static String IP = "192.168.1.104";
//    public static String IP = "58.19.2.210";

//    public static String IP = "192.168.43.73";
    public static String SERVER_PATH = "http://" + IP + ":8080";

    //获取用户名和密码
    public static String GET_USER_BY_USERID = SERVER_PATH + "/userController/getUserByUserId.action?userId=";

    //注册用户，添加用户
    public static String PUT_USER = SERVER_PATH + "/userController/createUser.action";
}
