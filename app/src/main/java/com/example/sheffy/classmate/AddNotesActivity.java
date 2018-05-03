package com.example.sheffy.classmate;

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

import java.text.SimpleDateFormat;

import application.MyApplication;
import bean.NotesBean;
import constant.ServerUrl;
import http.HttpCallback;
import http.HttpUtils;

public class AddNotesActivity extends AppCompatActivity {
    private TextView txv_add_date,txv_commit;
    private EditText et_title,et_content;
    private Toolbar tb_title;

    private String content,date,title,userName;
    private NotesBean nb;
    private MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_add_notes);

        myApp=(MyApplication)getApplication();
        userName=myApp.getUserName();

        initView();
        Log.d("title", userName);
        //使用自定义标题
        setSupportActionBar(tb_title);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
        }
        //设置时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        date=sdf.format(new java.util.Date());
        Log.d("Date", date);
        txv_add_date.setText(date);

        txv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_title.getText().toString().length()==0){
                    Toast.makeText(AddNotesActivity.this,"标题不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(et_content.getText().toString().length()==0){
                    Toast.makeText(AddNotesActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    nb=new NotesBean();
                    nb.setDate(date);
                    nb.setTitle(et_title.getText().toString());
                    nb.setUserId(userName);
                    nb.setContent(et_content.getText().toString());
                    //提交到服务器
                    putNotes();
                }
            }
        });
    }

    //初始化控件
    protected void initView(){
        txv_add_date=(TextView)findViewById(R.id.txv_add_notes_date);
        txv_commit=(TextView)findViewById(R.id.txv_commit_notes);
        tb_title=(Toolbar)findViewById(R.id.tb_title_notes);
        et_content=(EditText)findViewById(R.id.et_note_content);
        et_title=(EditText)findViewById(R.id.et_notes_title);
    }

    //提交到服务器
    protected void putNotes(){
        //转换成 Json 文本
        Gson gson = new Gson();
        String json =  gson.toJson(nb);
        Log.i("json", "putNotes: "+json);

        // 提交 json 文本到服务器
        new HttpUtils().postData(ServerUrl.PUT_NOTES,json,new HttpCallback(){
            @Override
            public void onSuccess(Object data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddNotesActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        finish();   //结束添加页
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddNotesActivity.this,"提交失败,网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
