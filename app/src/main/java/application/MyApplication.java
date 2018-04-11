package application;

import android.app.Application;

import java.util.List;

import bean.BookBean;
import bean.ClassmateBean;

/**
 * Created by Sheffy on 2018/4/11.
 */

public class MyApplication extends Application {
    private String userName;
    private String booknName;
    private List<BookBean> bookBeanList;
    private int bookListSize;
    private ClassmateBean classmateBean;

    public int getBookListSize() {
        return bookListSize;
    }

    public void setBookListSize(int bookListSize) {
        this.bookListSize = bookListSize;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBooknName() {
        return booknName;
    }

    public void setBooknName(String booknName) {
        this.booknName = booknName;
    }

    public List<BookBean> getBookBeanList() {
        return bookBeanList;
    }

    public void setBookBeanList(List<BookBean> bookBeanList) {
        this.bookBeanList = bookBeanList;
    }

    public ClassmateBean getClassmateBean() {
        return classmateBean;
    }

    public void setClassmateBean(ClassmateBean classmateBean) {
        this.classmateBean = classmateBean;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserName("");
    }
}
