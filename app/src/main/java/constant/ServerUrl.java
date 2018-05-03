package constant;

/**
 * Created by Sheffy on 2018/3/27.
 */

public class ServerUrl {
    //服务器IP地址
    public static String IP = "192.168.1.103";
//    public static String IP = "192.168.1.104";
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
    public static String GET_CATALOG_BY_BOOKID=SERVER_PATH+"/classmateController/getItemListByBookId.action";

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

    //修改密码
    public static String UPDATE_PASSWORD=SERVER_PATH+"/userController/updatePassword.action";

    //修改头像
    public static String UPDATE_FAVICON=SERVER_PATH+"/userController/updateFavicon.action";

    //修改同学录名称
    public static String PUT_CHANGE_BOOK_NAME=SERVER_PATH+"/bookController/updateBookName.action";

    //修改同学录简介
    public static String PUT_CHANGE_INTRODUCE=SERVER_PATH+"/bookController/updateIntroduce.action";

    //修改封面
    public static String PUT_CHANGE_FACE=SERVER_PATH+"/bookController/updateFace.action";

    //修改背景
    public static String PUT_CHANGE_PAGER=SERVER_PATH+"/bookController/updatePager.action";

    //获取所有记录
    public static String GET_ALL_NOTES=SERVER_PATH+"/notesController/getAllNotes.action?userId=";

    //添加记录
    public static String PUT_NOTES=SERVER_PATH+"/notesController/addNotes.action";

    //删除记录
    public static String PUT_DELETE_NOTES=SERVER_PATH+"/notesController/deleteNotes.action";
}
