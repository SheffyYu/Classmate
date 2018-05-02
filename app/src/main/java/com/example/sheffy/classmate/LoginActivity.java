package com.example.sheffy.classmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import application.MyApplication;
import bean.UserBean;
import http.HttpCallback;
import http.UserHttpUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edt_user_name;
    private EditText edt_password;
    private Button btn_login;
    private Button btn_regist_login;
    private CheckBox cb_pass;

    private String userIdEdt;
    private String passwordEdt;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private UserHttpCallback userCallback;
    private UserBean userBean;
    private MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_login);

        initView();

        //是否记住登陆信息
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRember=pref.getBoolean("remember_password",false);
        if(isRember){
            //将账号和密码设置到文本框
            String user=pref.getString("user_name","");
            String password=pref.getString("password","");
            edt_user_name.setText(user);
            edt_password.setText(password);
            cb_pass.setChecked(true);
        }

        //加载网络
        userCallback=new UserHttpCallback();

    }

    //初始化事件绑定
    public void initView(){
        edt_user_name=(EditText)findViewById(R.id.edt_user_name);
        edt_password=(EditText)findViewById(R.id.edt_password);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_regist_login=(Button)findViewById(R.id.btn_regist_login);
        cb_pass=(CheckBox)findViewById(R.id.check_pass);

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
                        myApp=(MyApplication)getApplication();
                        myApp.setUserName(userBean.getUserId());
                        myApp.setFaviconPath(userBean.getFavicon());
                        //在手机中创建一个文件夹，用于保存图片文件
                        //新建一个File，传入文件夹目录
                        String path=Environment.getExternalStorageDirectory().getPath()+"/com.sheffy.classmate"+"/"+edt_user_name.getText().toString();
                        File file = new File(path);
                        myApp.setPath(path);
                        //判断文件夹是否存在，如果不存在就创建，否则不创建
                        if (!file.exists()) {
                            //通过file的mkdirs()方法创建<span style="color:#FF0000;">目录中包含却不存在</span>的文件夹
                            file.mkdirs();
                        }

                        //记住账号和密码
                        editor=pref.edit();
                        if (cb_pass.isChecked()){
                            editor.putBoolean("remember_password",true);
                            editor.putString("user_name",userIdEdt);
                            editor.putString("password",passwordEdt);
                        }
                        else {
                            editor.clear();
                        }
                        editor.apply();

                        //跳转到首页
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();   //结束登录页，首页点返回无法再跳转到登录页，直接退出app
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

}
