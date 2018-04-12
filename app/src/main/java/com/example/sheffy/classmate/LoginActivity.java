package com.example.sheffy.classmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import application.MyApplication;
import bean.BookBean;
import bean.ClassmateBean;
import bean.UserBean;
import http.BookHttpUtils;
import http.ClassmateHttpUtills;
import http.HttpCallback;
import http.UserHttpUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edt_user_name;
    private EditText edt_password;
    private Button btn_login;
    private Button btn_regist_login;

    private String userIdEdt;
    private String passwordEdt;

    private UserHttpCallback userCallback;
    private BookHttpCallback bookCallback;
    private ClassmateCallback classmateCallBack;
    private List<BookBean> bookList = new ArrayList<BookBean>();
    private List<ClassmateBean> classmateBeanList=new ArrayList<ClassmateBean>();
    private UserBean userBean;
    private int bookListSize;
    private MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_login);

        initView();

        //加载网络
        userCallback=new UserHttpCallback();

    }

    //初始化事件绑定
    public void initView(){
        edt_user_name=(EditText)findViewById(R.id.edt_user_name);
        edt_password=(EditText)findViewById(R.id.edt_password);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_regist_login=(Button)findViewById(R.id.btn_regist_login);

        btn_login.setOnClickListener(this);
        btn_regist_login.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_login:
                //验证用户名密码是否正确
                //获取editView
                userIdEdt = edt_user_name.getText().toString();
                passwordEdt = edt_password.getText().toString();

                new UserHttpUtils().getUserByUserId(userIdEdt,userCallback);
                break;
            case R.id.btn_regist_login:
                //跳转到注册页面
                Intent intent=new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
                break;
        }
    }

    class UserHttpCallback implements HttpCallback{
        @Override
        public void onSuccess(Object data){
            //获取用户名和密码
            userBean=new UserBean();
            userBean = (UserBean)data;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(userBean == null){
                        Toast.makeText(LoginActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
                        edt_user_name.setText("");
                        edt_password.setText("");
                    }
                    //String 类型不能用 ==
                    else if(passwordEdt.equals(userBean.getPassword())){
                        Log.i("userBean", userBean.toString());
                        //加载网络
                        bookCallback=new BookHttpCallback();
                        //加载同学录列表
                        new BookHttpUtils().getAllBookByUserId(userBean.getUserId(),bookCallback);
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                        edt_password.setText("");
                    }
                }
            });
        }

        @Override
        public void onFailure(String message){
            Log.i("userBean", "网络加载错误");
        }
    }


    class BookHttpCallback implements HttpCallback {
        @Override
        public void onSuccess(Object data){
            //获取同学录列表
            bookList=(List<BookBean>)data;
            Log.i("bookList", bookList.toString());
            bookListSize=bookList.size();
            Log.i("bookListSize", bookListSize+"");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    //跳转到首页
////                    ArrayList<BookBean> arrayList=new ArrayList<BookBean>();
////                    arrayList=(ArrayList<BookBean>) bookList;
////                    String userName=userBean.getUserId();
//                    myApp=(MyApplication)getApplication();
//                    myApp.setUserName(userBean.getUserId());
//                    myApp.setBookBeanList(bookList);
//                    myApp.setBookListSize(bookListSize);
//
//                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
////                    intent.putExtra("data_user_name",userName);
////                    intent.putExtra("data_bookListSize",bookListSize);
////                    intent.putExtra("data_bookList",arrayList);
//                    startActivity(intent);
//                    finish();   //结束登录页，首页点返回无法再跳转到登录页，直接退出app
                    //加载网络
                    classmateCallBack = new ClassmateCallback();
                    //加载同学录列表
                    new ClassmateHttpUtills().getAllClassmateByUserId(userBean.getUserId(), bookCallback);
                }
            });
        }

        @Override
        public void onFailure(String message){
            Log.i("bookBean", "网络加载错误");
        }

    }


    class ClassmateCallback implements HttpCallback{
        @Override
        public void onSuccess(Object data) {
            //获取全部同学的列表
            classmateBeanList=(List<ClassmateBean>)data;
            Log.i("classmateBeanList", "获取所有同学："+classmateBeanList.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //跳转到首页
//                    ArrayList<BookBean> arrayList=new ArrayList<BookBean>();
//                    arrayList=(ArrayList<BookBean>) bookList;
//                    String userName=userBean.getUserId();
                    myApp=(MyApplication)getApplication();
                    myApp.setUserName(userBean.getUserId());
                    myApp.setBookBeanList(bookList);
                    myApp.setBookListSize(bookListSize);
                    myApp.setAllClassmate(classmateBeanList);

                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                    intent.putExtra("data_user_name",userName);
//                    intent.putExtra("data_bookListSize",bookListSize);
//                    intent.putExtra("data_bookList",arrayList);
                    startActivity(intent);
                    finish();   //结束登录页，首页点返回无法再跳转到登录页，直接退出app
                }
            });
        }

        @Override
        public void onFailure(String message) {
            Log.i("bookBean", "网络加载错误");
        }
    }

}
