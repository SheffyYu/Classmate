package constant;

/**
 * Created by Sheffy on 2018/3/27.
 */

public class ServerUrl {
    //服务器IP地址
    public static String IP = "192.168.1.103";
//    public static String IP = "192.168.1.105";
    public static String SERVER_PATH = "http://" + IP + ":8080";

    //获取用户名和密码
    public static String GET_USER_BY_USERID = SERVER_PATH + "/userController/getUserByUserId.action?userId=";

    //注册用户，添加用户
    public static String PUT_USER = SERVER_PATH + "/userController/createUser.action";

    //获取同学录列表
    public static String GET_BOOK_BY_USERID=SERVER_PATH+"/bookController/getAllBookByUserId.action?userId=";

    //添加同学录
    public static String PUT_BOOK=SERVER_PATH+"/bookController/createBook.action";

    //获取目录
    public static String GET_CATALOG_BY_BOOKID=SERVER_PATH+"/classmateController/getItemListByBookId.action?bookId=";

    //添加同学
    public static String PUT_CLASSMATE=SERVER_PATH+"/classmateController/createClassmate.action";

    //更新同学
    public static String UPDATE_CLASSMATE=SERVER_PATH+"/classmateController/updateClassmate.action";

    //删除同学
    public static String PUT_DELETE_CLASSMATE=SERVER_PATH+"/classmateController/deleteClassmate.action";

    //删除同学录
    public static String PUT_DELETE_BOOK=SERVER_PATH+"/bookController/deleteBook.action";

    //获取所有同学
    public static String GET_ALL_CLASSMATE_BY_USERID=SERVER_PATH+"/classmateController/getAllClassmateByUserId.action?userId=";
}
