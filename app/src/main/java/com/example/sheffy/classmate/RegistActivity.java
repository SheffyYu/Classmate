package com.example.sheffy.classmate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import bean.UserBean;
import constant.ServerUrl;
import http.HttpCallback;
import http.HttpUtils;
import http.UserHttpUtils;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edt_user_name_regist,edt_password_regist,edt_password_again_regist;
    private Button btn_regist;
    private UserHttpCallback userCallback;

    private String userName,password,passwordAgain;

    private UserBean userBean;
    private UserBean putUserBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_regist);

        initView();
        //加载网络
        userCallback=new UserHttpCallback();
    }

    //初始化事件绑定
    public void initView(){
        edt_user_name_regist=(EditText)findViewById(R.id.edt_user_name_regist);
        edt_password_regist=(EditText)findViewById(R.id.edt_password_regist);
        edt_password_again_regist=(EditText)findViewById(R.id.edt_password_again_regist);
        btn_regist=(Button)findViewById(R.id.btn_regist);

        btn_regist.setOnClickListener(this);
    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_regist:
                userName=edt_user_name_regist.getText().toString();
                password=edt_password_regist.getText().toString();
                passwordAgain=edt_password_again_regist.getText().toString();

                new UserHttpUtils().getUserByUserId(userName,userCallback);
                break;
        }
    }

    class UserHttpCallback implements HttpCallback {
        @Override
        public void onSuccess(Object data){
            //获取用户名和密码
            userBean=new UserBean();
            userBean = (UserBean)data;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //用户名不存在
                    if(userBean == null){
                        //用户名长度不超过10
                        if(userName.length()>10){
                            Toast.makeText(RegistActivity.this,"用户名不超过10位",Toast.LENGTH_SHORT).show();
                            edt_user_name_regist.setText("");
                            edt_password_regist.setText("");
                            edt_password_again_regist.setText("");
                        }//密码长度不超过16不少于6
                        else if(password.length()>16){
                            Toast.makeText(RegistActivity.this,"密码不超过16位",Toast.LENGTH_SHORT).show();
                            edt_password_regist.setText("");
                            edt_password_again_regist.setText("");
                        }
                        else if(password.length()<6){
                            Toast.makeText(RegistActivity.this,"密码不少于6位",Toast.LENGTH_SHORT).show();
                            edt_password_regist.setText("");
                            edt_password_again_regist.setText("");
                        }//密码不相同不可提交
                        else if(!password.equals(passwordAgain)){
                            Toast.makeText(RegistActivity.this,"密码输入不相同",Toast.LENGTH_SHORT).show();
                            edt_password_regist.setText("");
                            edt_password_again_regist.setText("");
                        }
                        //提交//跳转到登录页
                        else{
                            putUserBean = new UserBean();
                            putUserBean.setUserId(userName);
                            putUserBean.setPassword(password);
                            Log.i("userBean", "run: "+putUserBean.toString());
                            //提交
                            putUser();
                        }
                    }
                    //用户名已存在
                    else{
                        Toast.makeText(RegistActivity.this,"用户已存在",Toast.LENGTH_SHORT).show();
                        edt_user_name_regist.setText("");
                    }
                }
            });
        }

        @Override
        public void onFailure(String message){
            Log.i("userBean", "网络加载错误");
        }

        //提交用户
        public void putUser(){
            //转换成 Json 文本
            Gson gson = new Gson();
            String json =  gson.toJson(putUserBean);
            Log.i("json", "putUser: "+json);

            // 提交 json 文本到服务器
            new HttpUtils().postData(ServerUrl.PUT_USER,json,new HttpCallback(){
                @Override
                public void onSuccess(Object data) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            //跳转到登录页
//                            Intent intent=new Intent(RegistActivity.this,LoginActivity.class);
//                            startActivity(intent);
                            finish();   //结束注册页
                        }
                    });
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistActivity.this,"注册失败,网络错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
