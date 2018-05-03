package com.example.sheffy.classmate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import application.MyApplication;
import bean.NotesBean;
import constant.ServerUrl;
import http.HttpCallback;
import http.HttpUtils;

public class ShowNotesItemActivity extends AppCompatActivity {
    private TextView txv_date,txv_content;
    private FloatingActionButton fab_delete_item;
    private MyApplication myApplication;
    private String date,content;
    private NotesBean notesBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_show_notes_item);

        txv_date=(TextView)findViewById(R.id.txv_notes_date);
        txv_content=(TextView)findViewById(R.id.txv_notes_content);
        fab_delete_item=(FloatingActionButton)findViewById(R.id.fab_delete_item);

        myApplication=(MyApplication)getApplication();
        date=myApplication.getNotesBean().getDate();
        content=myApplication.getNotesBean().getContent();

        txv_date.setText(date);
        txv_content.setText(content);

        fab_delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框
                AlertDialog dialog = new AlertDialog.Builder(ShowNotesItemActivity.this)
                        .setTitle("提示")//设置对话框的标题
                        .setMessage("你确定要删除该记录吗？")//设置对话框的内容
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
                                notesBean=new NotesBean();
                                notesBean=myApplication.getNotesBean();
                                deleteNotes();
                            }
                        }).create();
                dialog.show();
            }
        });
    }

    //删除记录
    protected void deleteNotes(){
        //转换成 Json 文本
        Gson gson = new Gson();
        String json =  gson.toJson(notesBean);
        Log.i("json", "putDeleteNotes: "+json);

        // 提交 json 文本到服务器
        new HttpUtils().postData(ServerUrl.PUT_DELETE_NOTES,json,new HttpCallback(){
            @Override
            public void onSuccess(Object data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShowNotesItemActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShowNotesItemActivity.this,"删除失败,网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
