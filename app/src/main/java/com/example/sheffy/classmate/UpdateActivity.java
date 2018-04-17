package com.example.sheffy.classmate;

import android.app.DatePickerDialog;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import application.MyApplication;
import bean.ClassmateBean;
import constant.ServerUrl;
import http.HttpCallback;
import http.HttpUtils;

public class UpdateActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText et_classmate_name,et_classmate_birthday,
            et_classmate_constellation,et_classmate_phone,et_classmate_qq,
            et_classmate_address,et_classmate_leave,et_classmate_your,
            et_classmate_hobby,et_classmate_blood;
    private Button btn_post_classmate;
    private RadioGroup radgroup;
    private ClassmateBean classmateBean,initClb;
    private String bookName,userName;
    private MyApplication myApp;
    private List<ClassmateBean> cbList=new ArrayList<ClassmateBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_update);

        myApp=(MyApplication)getApplication();
        initClb=new ClassmateBean();
        initClb=myApp.getClassmateBean();
        bookName=initClb.getBookId();
        userName=myApp.getUserName();

        cbList.add(initClb);

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
        toolbar=(Toolbar)findViewById(R.id.tb_title_up);
        et_classmate_name=(EditText)findViewById(R.id.et_up_name);
        radgroup=(RadioGroup)findViewById(R.id.up_radioGroup);
        et_classmate_birthday=(EditText)findViewById(R.id.et_up_birthday);
        et_classmate_constellation=(EditText)findViewById(R.id.et_up_constellation);
        et_classmate_phone=(EditText)findViewById(R.id.et_up_phone);
        et_classmate_qq=(EditText)findViewById(R.id.et_up_qq);
        et_classmate_address=(EditText)findViewById(R.id.et_up_address);
        et_classmate_leave=(EditText)findViewById(R.id.et_up_leave_word);
        et_classmate_your=(EditText)findViewById(R.id.et_up_your_word);
        et_classmate_blood=(EditText)findViewById(R.id.et_up_blood);
        et_classmate_hobby=(EditText)findViewById(R.id.et_up_hobby);
        btn_post_classmate=(Button)findViewById(R.id.btn_post_up_classmate);

        et_classmate_name.setText(initClb.getClassmateName());
        et_classmate_birthday.setText(initClb.getBirthday());
        et_classmate_constellation.setText(initClb.getConstellation());
        et_classmate_phone.setText(initClb.getPhone());
        et_classmate_qq.setText(initClb.getQq());
        et_classmate_address.setText(initClb.getAddress());
        et_classmate_leave.setText(initClb.getLeaveWord());
        et_classmate_your.setText(initClb.getYourWord());
        et_classmate_blood.setText(initClb.getBloodGroup());
        et_classmate_hobby.setText(initClb.getHobby());

        if(initClb.getSex().equals("男")){
            RadioButton rd = (RadioButton) radgroup.getChildAt(0);
            radgroup.check(rd.getId());
        }
        else {
            RadioButton rd = (RadioButton) radgroup.getChildAt(1);
            radgroup.check(rd.getId());
        }

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                UpdateActivity.this.et_classmate_birthday.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    //判断文本框必填项是否为空，数据是否符合规范不为空则提交
    protected void isEmpty(){
        if(et_classmate_name.getText().toString().length()==0){
            Toast.makeText(UpdateActivity.this,"姓名不能为空",Toast.LENGTH_SHORT).show();
        }
        else if(et_classmate_phone.getText().toString().length()==0){
            Toast.makeText(UpdateActivity.this,"联系方式不能为空",Toast.LENGTH_SHORT).show();
        }
        else if(et_classmate_phone.getText().toString().length()>11){
            Toast.makeText(UpdateActivity.this,"联系方式填写格式不正确",Toast.LENGTH_SHORT).show();
        }
        else if(et_classmate_qq.getText().toString().length()==0){
            Toast.makeText(UpdateActivity.this,"QQ不能为空",Toast.LENGTH_SHORT).show();
        }
        else if(et_classmate_qq.getText().toString().length()>20){
            Toast.makeText(UpdateActivity.this,"QQ号长度过长",Toast.LENGTH_SHORT).show();
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
            cbList.add(classmateBean);
            Log.i("cbList:", cbList.toString());
            putClassmate();
        }
    }

    ////提交到服务器并跳转回目录
    protected void putClassmate(){
        //转换成 Json 文本
        Gson gson = new Gson();
        String json =  gson.toJson(cbList);
        Log.i("json", "putClassmateList: "+json);

        // 提交 json 文本到服务器
        new HttpUtils().postData(ServerUrl.UPDATE_CLASSMATE,json,new HttpCallback(){
            @Override
            public void onSuccess(Object data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpdateActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                        finish();   //结束添加页
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //注意在服务器中要有返回，如果为空，则会执行失败
                        Toast.makeText(UpdateActivity.this,"提交失败,网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
