package com.example.sheffy.classmate;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnClickListener
        ,HomeFragment.OnFragmentInteractionListener
        ,MyFragment.OnFragmentInteractionListener
        ,NotesFragment.OnFragmentInteractionListener{
    private TextView btn_home;
    private TextView btn_notes;
    private TextView btn_search;
    private TextView btn_my;
    private TextView btn_add;

    //这里没有搜索的碎片
    // 搜索的页面不需要显示底部导航栏
    private FrameLayout ly_content;
    private HomeFragment homeFg;
    private MyFragment myFg;
    private NotesFragment notesFg;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_main);

        initView();
//        initData();
    }

//    初始化事件绑定
    private void initView(){
        btn_home=(TextView) findViewById(R.id.txv_home);
        btn_notes=(TextView) findViewById(R.id.txv_notes);
        btn_search=(TextView) findViewById(R.id.txv_search);
        btn_my=(TextView) findViewById(R.id.txv_my);
        btn_add=(TextView) findViewById(R.id.txv_add);
        ly_content = (FrameLayout) findViewById(R.id.flContainer);

        btn_home.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_my.setOnClickListener(this);
        btn_notes.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }


    //初始化数据
    public void initData(){
        homeFg = new HomeFragment();
        notesFg = new NotesFragment();
        myFg = new MyFragment();
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
        FragmentTransaction fgTransaction=getSupportFragmentManager().beginTransaction();
        hideAllFragment(fgTransaction);

        switch (v.getId()){
            //首页
            case R.id.txv_home:
                selectedAll();
                btn_home.setSelected(true);
                if(homeFg == null){
                    homeFg = new HomeFragment();        //如果首页碎片不存在则新建一个
                    fgTransaction.add(R.id.flContainer,homeFg);
                    fgTransaction.show(homeFg);
                }
                else{
                    fgTransaction.show(homeFg);
                }
                break;
            //搜索
            case R.id.txv_search:
                selectedAll();
                btn_search.setSelected(true);
                break;
            //添加手账
            case R.id.txv_add:
                selectedAll();
                break;
            //点滴记录
            case R.id.txv_notes:
                selectedAll();
                btn_notes.setSelected(true);
                if(notesFg == null){
                    notesFg = new NotesFragment();        //如果点滴记录碎片不存在则新建一个
                    fgTransaction.add(R.id.flContainer,notesFg);
                    fgTransaction.show(notesFg);
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
                    fgTransaction.show(myFg);
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



}

