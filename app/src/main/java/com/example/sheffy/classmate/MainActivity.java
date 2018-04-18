package com.example.sheffy.classmate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import application.MyApplication;
import bean.BookBean;
import http.BookHttpUtils;
import http.HttpCallback;

public class MainActivity extends AppCompatActivity implements OnClickListener
        ,HomeFragment.OnFragmentInteractionListener
        ,MyFragment.OnFragmentInteractionListener
        ,NotesFragment.OnFragmentInteractionListener
        ,BookFragment.OnFragmentInteractionListener{
    private TextView btn_home,btn_add,btn_my,btn_search,btn_notes;
    public static MainActivity ma=null;

    //这里没有搜索的碎片
    // 搜索的页面不需要显示底部导航栏
    private FrameLayout ly_content;
    private HomeFragment homeFg;
    private MyFragment myFg;
    private NotesFragment notesFg;

    private MyApplication myApp;
    private int bookListSize;
    private String userName;
    private List<BookBean> bookList = new ArrayList<BookBean>();
    private BookHttpCallback bookCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_main);
        ma=this;

        myApp=(MyApplication)getApplication();
        userName=myApp.getUserName();
        Log.i("MA.userName", "onCreate: "+userName);

        //加载网络
        bookCallback=new BookHttpCallback();
        //加载同学录列表
        new BookHttpUtils().getAllBookByUserId(userName,bookCallback);

    }


//    初始化事件绑定
    private void initView(){
        btn_home=(TextView) findViewById(R.id.txv_home);
        btn_notes=(TextView) findViewById(R.id.txv_notes);
        btn_search=(TextView) findViewById(R.id.txv_search);
        btn_my=(TextView) findViewById(R.id.txv_my);
        btn_add=(TextView) findViewById(R.id.txv_add);
        ly_content = (FrameLayout) findViewById(R.id.flContainer);

        //绑定点击事件
        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_my.setOnClickListener(this);
        btn_notes.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }


    //初始化数据，默认显示首页
    public void initData(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fgTransaction=fragmentManager.beginTransaction();
        //选中首页
        btn_home.setSelected(true);
        if(homeFg == null){
            homeFg = new HomeFragment();
            fgTransaction.add(R.id.flContainer,homeFg);
        }
        else{
            fgTransaction.show(homeFg);
        }
        fgTransaction.commit();

    }


    //重置所有文本的选中状态
    public void selectedAll(){
        btn_home.setSelected(false);
        btn_search.setSelected(false);
        btn_notes.setSelected(false);
        btn_my.setSelected(false);
    }

    //隐藏所有的碎片
    public void hideAllFragment(FragmentTransaction transaction){
        if(homeFg != null){
            transaction.hide(homeFg);
        }
        if(notesFg != null){
            transaction.hide(notesFg);
        }
        if(myFg != null){
            transaction.hide(myFg);
        }
    }


    public void onClick(View v){

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fgTransaction=fragmentManager.beginTransaction();
        hideAllFragment(fgTransaction);

        switch (v.getId()){
            //首页
            case R.id.txv_home:
                selectedAll();
                btn_home.setSelected(true);
                if(homeFg == null){
                    homeFg = new HomeFragment();        //如果首页碎片不存在则新建一个
                    fgTransaction.add(R.id.flContainer,homeFg);

                }
                else{
                    fgTransaction.show(homeFg);
                }
                break;
            //搜索
            case R.id.txv_search:
                selectedAll();
                btn_search.setSelected(true);
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);

                //返回时显示首页
                selectedAll();
                btn_home.setSelected(true);
                if(homeFg == null){
                    homeFg = new HomeFragment();        //如果首页碎片不存在则新建一个
                    fgTransaction.add(R.id.flContainer,homeFg);

                }
                else{
                    fgTransaction.show(homeFg);
                }
                break;
            //添加同学录
            case R.id.txv_add:
                //跳转到添加同学录页面
                Intent intent1=new Intent(MainActivity.this,AddBookActivity.class);
                startActivity(intent1);
                break;
            //点滴记录
            case R.id.txv_notes:
                selectedAll();
                btn_notes.setSelected(true);
                if(notesFg == null){
                    notesFg = new NotesFragment();        //如果点滴记录碎片不存在则新建一个
                    fgTransaction.add(R.id.flContainer,notesFg);
                }
                else{
                    fgTransaction.show(notesFg);
                }
                break;
            //我的
            case R.id.txv_my:
                selectedAll();
                btn_my.setSelected(true);
                if(myFg == null){
                    myFg = new MyFragment();        //如果我的碎片不存在则新建一个
                    fgTransaction.add(R.id.flContainer,myFg);
                }
                else{
                    fgTransaction.show(myFg);
                }
                break;
            default:
                break;
        }
        fgTransaction.commit();

    }

    //碎片之间的数据交换
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class BookHttpCallback implements HttpCallback {
        @Override
        public void onSuccess(Object data){
            //获取同学录列表
            bookList=(List<BookBean>)data;
            Log.i("bookList", bookList.toString());
            bookListSize=bookList.size();
            Log.i("bookListSize", bookListSize+"");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // 保存数据
                    myApp=(MyApplication)getApplication();
                    myApp.setBookBeanList(bookList);
                    myApp.setBookListSize(bookListSize);

                    initView();
                    //默认启动时现实首页
                    initData();
                }
            });
        }

        @Override
        public void onFailure(String message){
            Log.i("bookBean", "网络加载错误");
        }

    }

    @Override
    protected void onResumeFragments() {
        //加载网络
        bookCallback=new BookHttpCallback();
        //加载同学录列表
        new BookHttpUtils().getAllBookByUserId(userName,bookCallback);
        super.onResumeFragments();
    }
}

