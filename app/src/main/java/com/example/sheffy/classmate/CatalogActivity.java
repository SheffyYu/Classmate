package com.example.sheffy.classmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.CatalogAdapter;
import application.MyApplication;
import bean.ClassmateBean;
import http.ClassmateHttpUtills;
import http.HttpCallback;

public class CatalogActivity extends AppCompatActivity {

    private String bookName;

    private RecyclerView rv_list_name;
    private FloatingActionButton fab_add_item;
    private CatalogAdapter catalogAdapter;
    private LinearLayoutManager linearLayoutManager;
    private TextView txv_title;
    private Toolbar toolbar;
    private ClassmateListHttpCallback catalogCallBack;
    private List<ClassmateBean> catalogList=new ArrayList<ClassmateBean>();
    private MyApplication myapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_catalog);

        myapp=(MyApplication)getApplication();
        bookName=myapp.getBooknName();
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


                    fab_add_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //此处跳转到添加页面
                            Intent intent=new Intent(CatalogActivity.this,AddItemActivity.class);
                            intent.putExtra("bookName",bookName);
                            startActivity(intent);
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
}
