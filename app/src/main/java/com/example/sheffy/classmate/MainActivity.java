package com.example.sheffy.classmate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import application.MyApplication;
import bean.BookBean;
import bean.NotesBean;
import http.BookHttpUtils;
import http.HttpCallback;
import http.NotesHttpUtils;

public class MainActivity extends AppCompatActivity implements OnClickListener
        ,HomeFragment.OnFragmentInteractionListener
        ,MyFragment.OnFragmentInteractionListener
        ,NotesFragment.OnFragmentInteractionListener
        ,BookFragment.OnFragmentInteractionListener{
    private TextView btn_home,btn_add,btn_my,btn_search,btn_notes,txv_add_book,txv_add_notes;
    public static MainActivity ma=null;

    //这里没有搜索的碎片
    // 搜索的页面不需要显示底部导航栏
    private FrameLayout ly_content;
    private HomeFragment homeFg;
    private MyFragment myFg;
    private NotesFragment notesFg;

    private PopupWindow mPopupWindow;

    private MyApplication myApp;
    private int bookListSize;
    private String userName;
    private List<BookBean> bookList = new ArrayList<BookBean>();
    private List<NotesBean> notesList=new ArrayList<NotesBean>();
    private BookHttpCallback bookCallback;
    private NotesHttpBackcall notesHttpBackcall;

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
        txv_add_book=(TextView) findViewById(R.id.txv_add_book);
        txv_add_notes=(TextView) findViewById(R.id.txv_add_notes);
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

    //弹出框
    private void showPopWindow(View parentView, int convertViewResource) {
        //创建一个popUpWindow
        View popLayout = LayoutInflater.from(MainActivity.this).inflate(convertViewResource,null);
        //给popUpWindow内的空间设置点击事件his
        //添加同学录
        popLayout.findViewById(R.id.txv_add_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                Intent intent=new Intent(MainActivity.this,AddBookActivity.class);
                startActivity(intent);
            }
        });
        //添加记录
        popLayout.findViewById(R.id.txv_add_notes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                Intent intent=new Intent(MainActivity.this,AddBookActivity.class);
                startActivity(intent);
            }
        });
        if (mPopupWindow == null) {
            //实例化一个popupWindow
            mPopupWindow =
                    new PopupWindow(popLayout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //产生背景变暗效果
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.4f;
            getWindow().setAttributes(lp);
            //点击外面popupWindow消失
            mPopupWindow.setOutsideTouchable(true);
            //popupWindow获取焦点
            mPopupWindow.setFocusable(true);
            //刷新popupWindow
            //popupWindow.update();

            //设置popupWindow消失时的监听
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                //在dismiss中恢复透明度
                public void onDismiss() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1f;
                    getWindow().setAttributes(lp);
                }
            });
            mPopupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        } else {
            //如果popupWindow正在显示，接下来隐藏
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            } else {
                //产生背景变暗效果
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.4f;
                getWindow().setAttributes(lp);
                mPopupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
            }
        }
    }


    public void onClick(View v){

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fgTransaction=fragmentManager.beginTransaction();
//        hideAllFragment(fgTransaction);

        switch (v.getId()){
            //首页
            case R.id.txv_home:
                hideAllFragment(fgTransaction);
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
                hideAllFragment(fgTransaction);
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
                showPopWindow(v,R.layout.layout_popup_add);
                break;
            //点滴记录
            case R.id.txv_notes:
                hideAllFragment(fgTransaction);
                selectedAll();
                btn_notes.setSelected(true);

                //联网
                notesHttpBackcall=new NotesHttpBackcall();
                new NotesHttpUtils().getAllNotes(userName,notesHttpBackcall);
                break;
            //我的
            case R.id.txv_my:
                hideAllFragment(fgTransaction);
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

                    //这里为了避免从文件管理返回时，出现首页与我的以及点滴同时选中的情况，做了一些处理
                    if(btn_my.isSelected()){
                        selectedAll();
                        btn_my.setSelected(true);
                    }
                    if(btn_notes.isSelected()){
                        selectedAll();
                        btn_notes.setSelected(true);
                    }
                }
            });
        }

        @Override
        public void onFailure(String message){
            Log.i("bookBean", "网络加载错误");
        }

    }

    class NotesHttpBackcall implements HttpCallback{
        @Override
        public void onSuccess(Object data){
            //获取同学录列表
            notesList=(List<NotesBean>)data;
            Log.i("notesList", notesList.toString());
            int notesListSize=notesList.size();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    FragmentTransaction fgTransaction=fragmentManager.beginTransaction();
                    // 保存数据
                    myApp=(MyApplication)getApplication();
                    myApp.setNotesBeanList(notesList);

                    //显示碎片
                    if(notesFg == null){
                        notesFg = new NotesFragment();        //如果点滴记录碎片不存在则新建一个
                        fgTransaction.add(R.id.flContainer,notesFg);
                    }
                    else{
                        fgTransaction.show(notesFg);
                    }
                    fgTransaction.commit();
                }
            });
        }

        @Override
        public void onFailure(String message){
            Log.i("notesBean", "网络加载错误");
        }
    }

    //刷新首页内容
    @Override
    protected void onResumeFragments() {
        //加载网络
        bookCallback=new BookHttpCallback();
        //加载同学录列表
        new BookHttpUtils().getAllBookByUserId(userName,bookCallback);
        super.onResumeFragments();
    }

    //点击返回键返回桌面而不是退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

