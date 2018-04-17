package com.example.sheffy.classmate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;

import application.MyApplication;
import bean.ClassmateBean;
import constant.ServerUrl;
import http.HttpCallback;
import http.HttpUtils;

public class AddItemActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText et_classmate_name,et_classmate_birthday,
                     et_classmate_constellation,et_classmate_phone,et_classmate_qq,
                     et_classmate_address,et_classmate_leave,et_classmate_your,
                     et_classmate_hobby,et_classmate_blood;
    private Button btn_post_classmate;
    private RadioGroup radgroup;
    private ClassmateBean classmateBean;
    private String bookName,userName;
    private MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_add_item);

        myApp=(MyApplication)getApplication();
        bookName=myApp.getBooknName();
        userName=myApp.getUserName();


        //初始化组件
        initView();

        //使用自定义标题
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
        }

        //设置点击生日的输入框弹出控件
        popupDateDlg();

        //提交
        btn_post_classmate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断必填项是否为空，不为空则跳转
                isEmpty();
            }
        });

    }

    //初始化组件
    public void initView(){
        toolbar=(Toolbar)findViewById(R.id.tb_title_add);
        et_classmate_name=(EditText)findViewById(R.id.et_classmate_name);
        radgroup=(RadioGroup)findViewById(R.id.radioGroup);
        et_classmate_birthday=(EditText)findViewById(R.id.et_classmate_birthday);
        et_classmate_constellation=(EditText)findViewById(R.id.et_classmate_constellation);
        et_classmate_phone=(EditText)findViewById(R.id.et_classmate_phone);
        et_classmate_qq=(EditText)findViewById(R.id.et_classmate_qq);
        et_classmate_address=(EditText)findViewById(R.id.et_classmate_address);
        et_classmate_leave=(EditText)findViewById(R.id.et_classmate_leave_word);
        et_classmate_your=(EditText)findViewById(R.id.et_your_word);
        et_classmate_blood=(EditText)findViewById(R.id.et_classmate_blood);
        et_classmate_hobby=(EditText)findViewById(R.id.et_classmate_hobby);
        btn_post_classmate=(Button)findViewById(R.id.btn_post_add_classmate);
    }

    //设置点击生日的输入框弹出控件
    protected void popupDateDlg(){
        et_classmate_birthday.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        et_classmate_birthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddItemActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                AddItemActivity.this.et_classmate_birthday.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    //判断文本框必填项是否为空，数据是否符合规范不为空则提交
    protected void isEmpty(){
        if(et_classmate_name.getText().toString().length()==0){
            Toast.makeText(AddItemActivity.this,"姓名不能为空",Toast.LENGTH_SHORT).show();
        }
        else if(et_classmate_phone.getText().toString().length()==0){
            Toast.makeText(AddItemActivity.this,"联系方式不能为空",Toast.LENGTH_SHORT).show();
        }
        else if(et_classmate_phone.getText().toString().length()>11){
            Toast.makeText(AddItemActivity.this,"联系方式填写格式不正确",Toast.LENGTH_SHORT).show();
        }
        else if(et_classmate_qq.getText().toString().length()==0){
            Toast.makeText(AddItemActivity.this,"QQ不能为空",Toast.LENGTH_SHORT).show();
        }
        else if(et_classmate_qq.getText().toString().length()>20){
            Toast.makeText(AddItemActivity.this,"QQ号长度过长",Toast.LENGTH_SHORT).show();
        }
        else{
            //提交到服务器并跳转回目录
            classmateBean=new ClassmateBean();
            classmateBean.setBookId(bookName);
            classmateBean.setUserId(userName);
            classmateBean.setClassmateName(et_classmate_name.getText().toString());

            //获取性别
            for (int i = 0; i < radgroup.getChildCount(); i++) {
                RadioButton rd = (RadioButton) radgroup.getChildAt(i);
                if (rd.isChecked()) {
                    classmateBean.setSex(rd.getText().toString());
                }
            }

            classmateBean.setBirthday(et_classmate_birthday.getText().toString());
            classmateBean.setConstellation(et_classmate_constellation.getText().toString());
            classmateBean.setAddress(et_classmate_address.getText().toString());
            classmateBean.setPhone(et_classmate_phone.getText().toString());
            classmateBean.setQq(et_classmate_qq.getText().toString());
            classmateBean.setLeaveWord(et_classmate_leave.getText().toString());
            classmateBean.setYourWord(et_classmate_your.getText().toString());
            classmateBean.setHobby(et_classmate_hobby.getText().toString());
            classmateBean.setBloodGroup(et_classmate_blood.getText().toString());

            //提交
            putClassmate();
        }
    }

    ////提交到服务器并跳转回目录
    protected void putClassmate(){
        //转换成 Json 文本
        Gson gson = new Gson();
        String json =  gson.toJson(classmateBean);
        Log.i("json", "putClassmate: "+json);

        // 提交 json 文本到服务器
        new HttpUtils().postData(ServerUrl.PUT_CLASSMATE,json,new HttpCallback(){
            @Override
            public void onSuccess(Object data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddItemActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        //跳转到目录
                        Intent intent=new Intent(AddItemActivity.this,CatalogActivity.class);
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
                        Toast.makeText(AddItemActivity.this,"提交失败,网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
