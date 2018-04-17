package com.example.sheffy.classmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import application.MyApplication;
import bean.UserBean;
import http.HttpCallback;
import http.UserHttpUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edt_user_name;
    private EditText edt_password;
    private Button btn_login;
    private Button btn_regist_login;

    private String userIdEdt;
    private String passwordEdt;

    private UserHttpCallback userCallback;
    private UserBean userBean;
    private MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_login);

        initView();

        //加载网络
        userCallback=new UserHttpCallback();

    }

    //初始化事件绑定
    public void initView(){
        edt_user_name=(EditText)findViewById(R.id.edt_user_name);
        edt_password=(EditText)findViewById(R.id.edt_password);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_regist_login=(Button)findViewById(R.id.btn_regist_login);

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
