package com.example.sheffy.classmate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import application.MyApplication;
import bean.ClassmateBean;
import constant.ServerUrl;
import http.HttpCallback;
import http.HttpUtils;

public class ShowClassmateActivity extends AppCompatActivity {

    private TextView txv_delete_c,txv_change_c,txv_title,
                     txv_name,txv_sex,txv_date,txv_cons,txv_blood,
                     txv_phone,txv_qq,txv_address,txv_hobby,txv_leave,txv_your;
    private Toolbar tb_title_show_c;
    private ClassmateBean classmateBean;
    private MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_show_classmate);

        //获取本条信息
        myApp=(MyApplication)getApplication();
        classmateBean=myApp.getClassmateBean();

        //初始化组件
        initView();

        //使用自定义标题
        txv_title.setText(classmateBean.getBookId());
        setSupportActionBar(tb_title_show_c);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
        }

        //设置信息
        initData();

        click();
    }

    //初始化组件
    public void initView(){
        tb_title_show_c=(Toolbar)findViewById(R.id.tb_title_show_classmate);
        txv_delete_c=(TextView)findViewById(R.id.txv_delete_classmate);
        txv_change_c=(TextView)findViewById(R.id.txv_change_classmate);
        txv_title=(TextView)findViewById(R.id.txv_title_show_classmate);
        txv_name=(TextView)findViewById(R.id.txv_show_classmate_name);
        txv_sex=(TextView)findViewById(R.id.txv_show_classmate_sex);
        txv_date=(TextView)findViewById(R.id.txv_show_classmate_birthday);
        txv_cons=(TextView)findViewById(R.id.txv_show_classmate_constellation);
        txv_blood=(TextView)findViewById(R.id.txv_show_classmate_blood);
        txv_phone=(TextView)findViewById(R.id.txv_show_classmate_phone);
        txv_qq=(TextView)findViewById(R.id.txv_show_classmate_qq);
        txv_address=(TextView)findViewById(R.id.txv_show_classmate_address);
        txv_hobby=(TextView)findViewById(R.id.txv_show_classmate_hobby);
        txv_leave=(TextView)findViewById(R.id.txv_show_classmate_leave);
        txv_your=(TextView)findViewById(R.id.txv_show_classmate_your);

    }

    //初始化数据
    public void initData(){
        txv_name.setText(classmateBean.getClassmateName());
        txv_sex.setText(classmateBean.getSex());
        txv_date.setText(classmateBean.getBirthday());
        txv_cons.setText(classmateBean.getConstellation());
        txv_blood.setText(classmateBean.getBloodGroup());
        txv_phone.setText(classmateBean.getPhone());
        txv_qq.setText(classmateBean.getQq());
        txv_address.setText(classmateBean.getAddress());
        txv_hobby.setText(classmateBean.getHobby());
        txv_leave.setText(classmateBean.getLeaveWord());
        txv_your.setText(classmateBean.getYourWord());
    }

    //点击事件
    public void click(){
        //绑定事件监听器
        //删除
        txv_delete_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框
                AlertDialog dialog = new AlertDialog.Builder(ShowClassmateActivity.this)
                        .setTitle("提示")//设置对话框的标题
                        .setMessage("你确定要删除该同学吗？")//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ShowClassmateActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ShowClassmateActivity.this, "删除", Toast.LENGTH_SHORT).show();
                                //提交到服务器，做删除操作*****************************************************************
                                dialog.dismiss();
                                deleteClassmate();
                            }
                        }).create();
                dialog.show();
            }
        });

        //修改
        txv_change_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShowClassmateActivity.this,UpdateActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    protected void deleteClassmate(){
        //转换成 Json 文本
        Gson gson = new Gson();
        String json =  gson.toJson(classmateBean);
        Log.i("json", "putDeleteClassmate: "+json);

        // 提交 json 文本到服务器
        new HttpUtils().postData(ServerUrl.PUT_DELETE_CLASSMATE,json,new HttpCallback(){
            @Override
            public void onSuccess(Object data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShowClassmateActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                        //跳转到目录
                        Intent intent=new Intent(ShowClassmateActivity.this,CatalogActivity.class);
                        startActivity(intent);
                        finish();   //结束该页
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShowClassmateActivity.this,"删除失败,网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
