package com.example.sheffy.classmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

public class SplashMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //去标题
        setContentView(R.layout.activity_splash_main);

        //引导页定时跳转时间设置为2s
        final Intent it = new Intent(this, MainActivity.class); //转向首页
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it); //执行
                finish();   //结束引导页，首页点返回无法再跳转到引导页，直接退出app
            }
        };
        timer.schedule(task, 1000 * 2); //2秒后


    }
}
