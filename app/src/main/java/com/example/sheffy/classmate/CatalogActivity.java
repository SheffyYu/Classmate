package com.example.sheffy.classmate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import adapter.CatalogAdapter;
import application.MyApplication;
import bean.BookBean;
import bean.ClassmateBean;
import constant.ServerUrl;
import http.ClassmateHttpUtills;
import http.HttpCallback;
import http.HttpUtils;

public class CatalogActivity extends AppCompatActivity {

    private String bookName;

    private RecyclerView rv_list_name;
    private FloatingActionButton fab_add_item;
    private CatalogAdapter catalogAdapter;
    private LinearLayoutManager linearLayoutManager;
    private TextView txv_title,txv_default,txv_delete;
    private Toolbar toolbar;
    private ClassmateListHttpCallback catalogCallBack;
    private List<ClassmateBean> catalogList=new ArrayList<ClassmateBean>();
    private MyApplication myapp;
    private BookBean bookBean;
    private String userName;
    private int classmateCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_catalog);

        myapp=(MyApplication)getApplication();
        bookName=myapp.getBooknName();
        userName= myapp.getUserName();
        classmateCount=myapp.getClassmateCount();
        Log.i("onClick:", "获取bookName"+bookName);

        //加载网络
        catalogCallBack=new ClassmateListHttpCallback();
        Log.i("onClick", "成功创建callback对象");
        //获取目录列表
        new ClassmateHttpUtills().getClassmateListByBookId(bookName,catalogCallBack);
        Log.i("onClick", "接收不到数据");

    }

    //初始化/绑定事件
    public void initView(){
        rv_list_name=(RecyclerView)findViewById(R.id.rv_list_name);
        fab_add_item=(FloatingActionButton)findViewById(R.id.fab_add_item);
        txv_title=(TextView)findViewById(R.id.txv_title);
        toolbar = (Toolbar) findViewById(R.id.title_bar);
        txv_default=(TextView)findViewById(R.id.txv_default);
        txv_delete=(TextView)findViewById(R.id.txv_delete_book);

        linearLayoutManager=new LinearLayoutManager(this);
        catalogAdapter=new CatalogAdapter(catalogList);
        rv_list_name.setLayoutManager(linearLayoutManager);
        //添加分割线
        rv_list_name.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rv_list_name.setAdapter(catalogAdapter);
    }

    class ClassmateListHttpCallback implements HttpCallback {
        @Override
        public void onSuccess(Object data){
            //获取同学录列表
            catalogList=(List<ClassmateBean>)data;
            Log.i("catalogList",catalogList.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //初始化数据
                    initView();

                    //设置标题
                    txv_title.setText(bookName);
                    Log.i("bookName:", bookName);
                    setSupportActionBar(toolbar);
                    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null){
                        actionBar.setDisplayShowTitleEnabled(false);
                    }

                    //如果没有同学，则提示添加
                    if(catalogList.size()==0){
                        txv_default.setText("没有同学唉~快去添加吧~~");
                    }
                    else{
                        txv_default.setText("");
                    }

                    txv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //弹出对话框
                            AlertDialog dialog = new AlertDialog.Builder(CatalogActivity.this)
                                    .setTitle("提示")//设置对话框的标题
                                    .setMessage("你确定要删除该同学录吗？")//设置对话框的内容
                                    //设置对话框的按钮
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            Toast.makeText(CatalogActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            Toast.makeText(CatalogActivity.this, "删除", Toast.LENGTH_SHORT).show();
                                            //提交到服务器，做删除操作*****************************************************************
                                            dialog.dismiss();
                                            bookBean=new BookBean();
                                            bookBean.setClassmateCount(classmateCount);
                                            bookBean.setUserId(userName);
                                            bookBean.setBookId(bookName);

                                            deleteBook();
                                        }
                                    }).create();
                            dialog.show();
                        }
                    });


                    fab_add_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //此处跳转到添加页面
                            Intent intent=new Intent(CatalogActivity.this,AddItemActivity.class);
                            intent.putExtra("bookName",bookName);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
            });
        }

        @Override
        public void onFailure(String message){
            Log.i("catalogList", "网络加载错误");
        }

    }

    protected void deleteBook(){

        //转换成 Json 文本
        Gson gson = new Gson();
        String json =  gson.toJson(bookBean);
        Log.i("json", "putDeleteClassmate: "+json);

        // 提交 json 文本到服务器
        new HttpUtils().postData(ServerUrl.PUT_DELETE_BOOK,json,new HttpCallback(){
            @Override
            public void onSuccess(Object data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CatalogActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(CatalogActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CatalogActivity.this,"删除失败,网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        //加载网络
        catalogCallBack=new ClassmateListHttpCallback();
        Log.i("onClick", "成功创建callback对象");
        //获取目录列表
        new ClassmateHttpUtills().getClassmateListByBookId(bookName,catalogCallBack);
        Log.i("onClick", "接收不到数据");
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(CatalogActivity.this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
