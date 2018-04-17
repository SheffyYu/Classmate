package com.example.sheffy.classmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import application.MyApplication;
import bean.BookBean;
import constant.ServerUrl;
import http.HttpCallback;
import http.HttpUtils;

public class AddBookActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txv_sure,txv_bg;
    private EditText et_book_name;
    private String bookName;
    private MyApplication myApplication;
    private List<BookBean> bookBeanList=new ArrayList<BookBean>();
    private int bookListSize;
    private BookBean bookBean;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_add_book);

        //获取数据
        myApplication=(MyApplication)getApplication();
        bookBeanList=myApplication.getBookBeanList();
        bookListSize=myApplication.getBookListSize();
        userName=myApplication.getUserName();

        //初始化数据
        initView();

        //设置标题
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
        }

        txv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookName=et_book_name.getText().toString();
                Log.i("ABA:bookName", bookName);
                if(bookName.length()==0){
                    Toast.makeText(AddBookActivity.this,"名称不能为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    //判断当前同学录是否已经存在
                    if(isExist()){
                        Toast.makeText(AddBookActivity.this,"同学录已存在",Toast.LENGTH_SHORT).show();
                        et_book_name.setText("");
                    }
                    else {
                        //提交到服务器
                        Log.i("ABA:bookName", bookName);
                        bookBean=new BookBean();
                        bookBean.setBookId(bookName);
                        bookBean.setUserId(userName);
                        bookBean.setClassmateCount(0);
                        putBook();
                    }
                }
            }
        });

    }

    //初始化组件
    public void initView(){
        toolbar=(Toolbar)findViewById(R.id.add_book_title_bar);
        txv_sure=(TextView)findViewById(R.id.txv_sure_ab);
        txv_bg=(TextView)findViewById(R.id.txv_bg_ab);
        et_book_name=(EditText)findViewById(R.id.et_book_name);
    }

    //判断同学录是否已经存在
    protected  boolean isExist(){
        for(int i=0;i<bookListSize;i++){
            if(bookBeanList.get(i).getBookId().equals(bookName)){
                return true;
            }
        }
        return false;
    }

    ////提交到服务器并跳转回目录
    protected void putBook(){
        //转换成 Json 文本
        Gson gson = new Gson();
        String json =  gson.toJson(bookBean);
        Log.i("json", "putBook: "+json);

        // 提交 json 文本到服务器
        new HttpUtils().postData(ServerUrl.PUT_BOOK,json,new HttpCallback(){
            @Override
            public void onSuccess(Object data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddBookActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        //跳转到目录
                        MyApplication myApp=(MyApplication)getApplication();
                        myApp.setBooknName(bookName);
                        Intent intent=new Intent(AddBookActivity.this,CatalogActivity.class);
                        startActivity(intent);
                        finish();   //结束添加页
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddBookActivity.this,"提交失败,网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AddBookActivity.this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
