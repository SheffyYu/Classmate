package com.example.sheffy.classmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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
    private List<ClassmateBean> findList=new ArrayList<ClassmateBean>();
    private ClassmateAdapter classmateAdapter;
    private ClassmateAdapter findAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);         //去标题
        setContentView(R.layout.activity_search);

        myApplication=(MyApplication)getApplication();
        classmateBeanList=myApplication.getAllClassmate();

        //初始化组件
        initView();

        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query))
                {
                    Toast.makeText(SearchActivity.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                    lv_search_result.setAdapter(classmateAdapter);
                    lv_search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ClassmateBean classmate=classmateBeanList.get(position);
                            myApplication=(MyApplication)getApplication();
                            myApplication.setClassmateBean(classmate);
                            Log.i("myApplication:", classmate.toString());

                            Intent intent=new Intent(SearchActivity.this, ShowClassmateActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    findList.clear();
                    for(int i = 0; i < classmateBeanList.size(); i++)
                    {
                        ClassmateBean information = classmateBeanList.get(i);
                        if(information.getClassmateName().equals(query))
                        {
                            findList.add(information);
                            break;
                        }
                    }
                    if(findList.size() == 0)
                    {
                        Toast.makeText(SearchActivity.this, "没有该同学", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //查找到结果后显示list结果
                        findAdapter = new ClassmateAdapter(SearchActivity.this,R.layout.catalog_item,findList);
                        lv_search_result.setAdapter(findAdapter);
                        lv_search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ClassmateBean classmate=findList.get(position);
                                myApplication=(MyApplication)getApplication();
                                myApplication.setClassmateBean(classmate);
                                Log.i("myApplication:", classmate.toString());

                                Intent intent=new Intent(SearchActivity.this, ShowClassmateActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
                return true;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText))
                {
                    //为空时显示全部
                    lv_search_result.setAdapter(classmateAdapter);
                    lv_search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ClassmateBean classmate=classmateBeanList.get(position);
                            myApplication=(MyApplication)getApplication();
                            myApplication.setClassmateBean(classmate);
                            Log.i("myApplication:", classmate.toString());

                            Intent intent=new Intent(SearchActivity.this, ShowClassmateActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    findList.clear();
                    for(int i = 0; i < classmateBeanList.size(); i++)
                    {
                        ClassmateBean information = classmateBeanList.get(i);
                        Log.i("onQueryTextChange: ",information.getClassmateName());
                        if(information.getClassmateName().contains(newText))
                        {
                            findList.add(information);
                        }
                    }
                    Log.i("onQueryTextChange: ",findList.toString() );
                    findAdapter = new ClassmateAdapter(SearchActivity.this,R.layout.catalog_item, findList);
                    findAdapter.notifyDataSetChanged();
                    //显示查找到的结果
                    lv_search_result.setAdapter(findAdapter);
                    lv_search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ClassmateBean classmate=findList.get(position);
                            myApplication=(MyApplication)getApplication();
                            myApplication.setClassmateBean(classmate);
                            Log.i("myApplication:", classmate.toString());

                            Intent intent=new Intent(SearchActivity.this, ShowClassmateActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                return true;
            }
        });

    }

    //初始化组件
    public void initView(){
        sv_search=(SearchView)findViewById(R.id.sv_search_name);
        lv_search_result=(ListView)findViewById(R.id.lv_search_result);

        classmateAdapter=new ClassmateAdapter(SearchActivity.this,R.layout.catalog_item,classmateBeanList);
        //最开始时显示全部
        lv_search_result.setAdapter(classmateAdapter);
        lv_search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassmateBean classmate=classmateBeanList.get(position);
                myApplication=(MyApplication)getApplication();
                myApplication.setClassmateBean(classmate);
                Log.i("myApplication:", classmate.toString());

                Intent intent=new Intent(SearchActivity.this, ShowClassmateActivity.class);
                startActivity(intent);
            }
        });
        lv_search_result.setTextFilterEnabled(true);
    }

}
