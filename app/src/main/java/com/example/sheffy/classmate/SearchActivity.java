package com.example.sheffy.classmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapter.ClassmateAdapter;
import application.MyApplication;
import bean.ClassmateBean;

public class SearchActivity extends AppCompatActivity {

    private SearchView sv_search;
    private ListView lv_search_result;
    private MyApplication myApplication;
    private List<ClassmateBean> classmateBeanList=new ArrayList<ClassmateBean>();
    private ClassmateAdapter classmateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_search);

        myApplication=(MyApplication)getApplication();
        classmateBeanList=myApplication.getAllClassmate();

        //初始化组件
        initView();

        lv_search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassmateBean classmate=classmateBeanList.get(position);
                myApplication=(MyApplication)getApplication();
                myApplication.setClassmateBean(classmate);
                Log.i("myApplication:", classmate.toString());

                Intent intent=new Intent(SearchActivity.this, ShowClassmateActivity.class);
                startActivity(intent);
                // 转化为activity，然后finish就行了
//                finish();
            }
        });

    }

    //初始化组件
    public void initView(){
        sv_search=(SearchView)findViewById(R.id.sv_search_name);
        lv_search_result=(ListView)findViewById(R.id.lv_search_result);

        classmateAdapter=new ClassmateAdapter(SearchActivity.this,R.layout.catalog_item,classmateBeanList);
        lv_search_result.setAdapter(classmateAdapter);
    }

}
