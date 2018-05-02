package com.example.sheffy.classmate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import application.MyApplication;
import bean.BookBean;
import constant.ServerUrl;
import http.HttpCallback;
import http.HttpUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BookFragment extends Fragment{

    private OnFragmentInteractionListener mListener;
    private TextView txv_book_name,txv_introduce,txv_setup_book;
    private LinearLayout ll_book;
    private View view;
    private String bookName,introduce,userName;
    private MyApplication myApp;
    private PopupWindow mPopupWindow;
    private List<BookBean> bookBeanList=new ArrayList<BookBean>();
    private BookBean bookBean;
    private int face,pager,faceChange,pagerChange;
    private final String[] itemsFace=new String[]{"For rest","The song of sea","Bird and castle"};
    private final String[] itemsPager=new String[]{"Pandora's box","The plant","The imagination of space"};
    public BookFragment() {
        // Required empty public constructor
    }
//数据操作
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApp=(MyApplication)getActivity().getApplication();

        bookName=getArguments().getString("bookName","我的同学录");
        introduce=getArguments().getString("introduce","默认");
        userName=myApp.getUserName();
        face=getArguments().getInt("bookface");
        pager=getArguments().getInt("bookpager");
    }

//为了传数据，保存数据，获取主函数中联网的数据
    public static BookFragment newInstance(BookBean bookBean){
        BookFragment bookFragment = new BookFragment();

        Bundle args = new Bundle();
        args.putString("bookName",bookBean.getBookId());
        args.putString("introduce", bookBean.getIntroduce());
        args.putInt("bookface",bookBean.getFace());
        args.putInt("bookpager", bookBean.getPager());
        bookFragment.setArguments(args);

        return bookFragment;
    }

    //UI操作
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_book, container, false);

        //初始化组件
        initView();

        txv_introduce.setText(introduce);
        txv_book_name.setText(bookName);
        setSkinFace();

        return view;
    }

    //碎片中的一些操作，如点击事件
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myApp=(MyApplication)getActivity().getApplication();

        //进入目录
        ll_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),CatalogActivity.class);
                myApp.setIntroduce(introduce);
                myApp.setBooknName(bookName);
                myApp.setPager(pager);
                startActivity(intent);
                Log.i("onClick:", "跳转成功");
            }
        });

        //设置
        txv_setup_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow(v,R.layout.layout_popup_setup);
            }
        });
    }

    //初始化组件
    public void initView(){
        txv_book_name=(TextView)view.findViewById(R.id.txv_book_name);
        txv_introduce=(TextView)view.findViewById(R.id.txv_introduce);
        txv_setup_book=(TextView)view.findViewById(R.id.txv_setup_book);
        ll_book=(LinearLayout)view.findViewById(R.id.ll_book);
    }

    //设置封面
    protected void setSkinFace(){
        switch (face){
            case 0://for rest
                ll_book.setBackgroundResource(R.drawable.book_bg);
                break;
            case 1://the song of sea
                ll_book.setBackgroundResource(R.drawable.book_bg_cover1);
                break;
            case 2://bird and castle
                ll_book.setBackgroundResource(R.drawable.book_bg_cover2);
                break;
        }
    }

    //弹出框
    private void showPopWindow(View parentView, int convertViewResource) {
        //创建一个popUpWindow
        View popLayout = LayoutInflater.from(getContext()).inflate(convertViewResource, null);
        //给popUpWindow内的空间设置点击事件
        //修改名称
        popLayout.findViewById(R.id.txv_pop_change_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                //如果为“我的同学录”，则不可修改，避免首页为空
                if(bookName.equals("我的同学录")){
                    Toast.makeText(getActivity(),"默认同学录不可修改",Toast.LENGTH_SHORT).show();
                }
                //弹出编辑框
                else{
                    showDialogChangeName();
                }
            }
        });
        //修改简介
        popLayout.findViewById(R.id.txv_pop_change_introduce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                //弹出编辑框
                showDialogChangeIntroduce();
            }
        });
        //修改封面
        popLayout.findViewById(R.id.txv_pop_change_face).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                //弹出单选框
                showDialogChangeFace();
            }
        });
        //修改背景
        popLayout.findViewById(R.id.txv_pop_change_pager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                //弹出单选框
                showDialogChangePager();
            }
        });
        if (mPopupWindow == null) {
            //实例化一个popupWindow
            mPopupWindow =
                    new PopupWindow(popLayout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //产生背景变暗效果
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.4f;
            getActivity().getWindow().setAttributes(lp);
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
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                }
            });
            mPopupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        } else {
            //如果popupWindow正在显示，接下来隐藏
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            } else {
                //产生背景变暗效果
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 0.4f;
                getActivity().getWindow().setAttributes(lp);
                mPopupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
            }
        }
    }

    //修改同学录名称
    protected void showDialogChangeName(){
        final EditText et = new EditText(getContext());

        new AlertDialog.Builder(getContext()).setTitle("请输入新的同学录名字")
                .setView(et)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getActivity(), "同学录名称不能为空！", Toast.LENGTH_LONG).show();
                        }
                        else if(isExit(input)){
                            Toast.makeText(getActivity(), "同学录名称已存在！", Toast.LENGTH_LONG).show();
                        }
                        else {
                            BookBean bb=new BookBean();
                            bb.setBookId(bookName);
                            bb.setUserId(userName);
                            bookBeanList.add(bb);
                            BookBean bc=new BookBean();
                            bc.setUserId(userName);
                            bc.setBookId(input);
                            bc.setIntroduce(introduce);
                            bookBeanList.add(bc);
                            //提交到服务器
                            putChangeName();
                            dialog.dismiss();
                        }
                    }
                }).show();
    }

    //同学录名称是否存在
    protected boolean isExit(String input){
        List<BookBean> bl=new ArrayList<BookBean>();
        bl=myApp.getBookBeanList();
        for(int i=0;i<bl.size();i++){
            if (bl.get(i).getBookId().equals(input)){
                return true;
            }
        }
        return false;
    }


    //修改同学录名称，提交到服务器
    protected void putChangeName(){
        //转换成 Json 文本
        Gson gson = new Gson();
        String json =  gson.toJson(bookBeanList);
        Log.i("json", "putChangeBookName: "+json);

        // 提交 json 文本到服务器
        new HttpUtils().postData(ServerUrl.PUT_CHANGE_BOOK_NAME,json,new HttpCallback(){
            @Override
            public void onSuccess(Object data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();

                        MainActivity.ma.finish();
                        Intent intent=new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"提交失败,网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //修改同学录介绍
    protected void showDialogChangeIntroduce(){
        final EditText et = new EditText(getContext());

        new AlertDialog.Builder(getContext()).setTitle("请输入新的介绍")
                .setView(et)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        bookBean=new BookBean();
                        bookBean.setBookId(bookName);
                        bookBean.setUserId(userName);
                        bookBean.setIntroduce(input);

                        //提交到服务器
                        putChangeBookBean(ServerUrl.PUT_CHANGE_INTRODUCE);
                        dialog.dismiss();

                    }
                }).show();
    }

    //修改bookBean，提交到服务器
    protected void putChangeBookBean(String url){
        //转换成 Json 文本
        Gson gson = new Gson();
        String json =  gson.toJson(bookBean);
        Log.i("json", "putChangeIntroduce: "+json);

        // 提交 json 文本到服务器
        new HttpUtils().postData(url,json,new HttpCallback(){
            @Override
            public void onSuccess(Object data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();

                        MainActivity.ma.finish();
                        Intent intent=new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"提交失败,网络错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //修改封面
    protected void showDialogChangeFace(){
        new AlertDialog.Builder(getContext()).setTitle("选择封面")
                .setSingleChoiceItems(itemsFace, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        faceChange=which;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        bookBean=new BookBean();
                        bookBean.setBookId(bookName);
                        bookBean.setUserId(userName);
                        bookBean.setFace(faceChange);

                        //提交到服务器
                        putChangeBookBean(ServerUrl.PUT_CHANGE_FACE);
                        dialog.dismiss();

                    }
                }).show();
    }

    //修改内部背景
    protected void showDialogChangePager(){
        new AlertDialog.Builder(getContext()).setTitle("选择内部背景")
                .setSingleChoiceItems(itemsPager, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pagerChange=which;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        bookBean=new BookBean();
                        bookBean.setBookId(bookName);
                        bookBean.setUserId(userName);
                        bookBean.setPager(pagerChange);

                        //提交到服务器
                        putChangeBookBean(ServerUrl.PUT_CHANGE_PAGER);
                        dialog.dismiss();

                    }
                }).show();
    }


//********************************以下为自动生成函数*************************************************
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
