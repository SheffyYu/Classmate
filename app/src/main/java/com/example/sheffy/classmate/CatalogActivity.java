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
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import adapter.CatalogAdapter;
import application.MyApplication;
import bean.BookBean;
import bean.ClassmateBean;
import constant.ServerUrl;
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
    private List<ClassmateBean> catalogList=new ArrayList<ClassmateBean>();
    private MyApplication myapp;
    private BookBean bookBean,initBookBean;
    private String userName,introduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_catalog);

        myapp=(MyApplication)getApplication();
        bookName=myapp.getBooknName();
        userName= myapp.getUserName();
        introduce=myapp.getIntroduce();
        Log.i("onClick:", "获取bookName"+bookName);

        //采用post方法获取数据
        initBookBean=new BookBean();
        initBookBean.setIntroduce(introduce);
        initBookBean.setUserId(userName);
        initBookBean.setBookId(bookName);

        //获取数据
        getCatologList();
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

    public void getCatologList(){
        Gson gson=new Gson();
        String json=gson.toJson(initBookBean);
        Log.i("json", "postBookBean: "+json);

        // 提交 json 文本到服务器
        new HttpUtils().postData(ServerUrl.GET_CATALOG_BY_BOOKID,json,new HttpCallback(){
            @Override
            public void onSuccess(Object data) {
                //获取同学录列表
                Gson g=new Gson();
                catalogList=g.fromJson(data.toString(),new TypeToken<List<ClassmateBean>>(){}.getType());
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

                        //设置点击事件
                        click();

                    }
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CatalogActivity.this,"提交失败,网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //设置监听器
    protected void click(){
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
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //提交到服务器，做删除操作
                                dialog.dismiss();
                                bookBean=new BookBean();
                                bookBean.setIntroduce(introduce);
                                bookBean.setUserId(userName);
                                bookBean.setBookId(bookName);
                                //删除的网络操作
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


    //删除书本的操作
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
                        //***************************************************************//
                        //为了这一步，老子各种方法都试过了，mdzz,再出问题，老子就跟你说再见//
                        MainActivity.ma.finish();
                        //***************************************************************//
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
        getCatologList();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
