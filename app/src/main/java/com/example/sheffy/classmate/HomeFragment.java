package com.example.sheffy.classmate;
/**
 * 特别注意：OnFragmentInteractionListener也要在MainActivity中添加
 *          import android.support.v4.app.Fragment;
 *下一步：读取数据库创建同学录碎片，默认碎片在app、数据库中的创建。
 *
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.FragAdapter;
import application.MyApplication;
import bean.BookBean;
import circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HomeFragment extends Fragment implements BookFragment.OnFragmentInteractionListener
        ,View.OnClickListener{

    private OnFragmentInteractionListener mListener;
//    private BookFragment bookFragment;
    private TextView txv_user_name_home;
    private View view;
    private CircleImageView civ_favicon_home;

    private ViewPager viewPager;
    private ArrayList<Fragment> fragmentList;
    private FragAdapter fragAdapter;
    private ImageView iv_red_piont;
    private LinearLayout ll_container;
    private int pointMoveDistance;

    //用户名
    private String userName,faviconPath;
    private int bookListSize;
    private List<BookBean> bookList;
    private MyApplication myApp;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        txv_user_name_home=(TextView) view.findViewById(R.id.txv_user_name_home);
        civ_favicon_home=(CircleImageView)view.findViewById(R.id.user_favicon_home);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        //红点的线性布局  (父)
        ll_container = (LinearLayout) view.findViewById(R.id.ll_container);
        //红点
        iv_red_piont = (ImageView) view.findViewById(R.id.iv_red_piont);
        ////设置用户名
        txv_user_name_home.setText(userName);
        //设置头像
        if(faviconPath!=null){
            Bitmap bitmap = BitmapFactory.decodeFile(faviconPath);
            Log.i("MyFragment","setImageToView:"+bitmap);
            civ_favicon_home.setImageBitmap(bitmap);
        }

        initData();
        initPointData();
        //viewpager 滑动时候圆点的动画
        initViewPager();

        return view;
    }


    //当从其他活动中返回时，刷新首页的内容
    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //从主界面函数中获取数据
            myApp=(MyApplication)getActivity().getApplication();
            userName=myApp.getUserName();
            bookListSize=myApp.getBookListSize();
            bookList=myApp.getBookBeanList();
            faviconPath=myApp.getFaviconPath();
            //设置头像
            if(faviconPath!=null){
                Bitmap bitmap = BitmapFactory.decodeFile(faviconPath);
                Log.i("MyFragment","setImageToView:"+bitmap);
                civ_favicon_home.setImageBitmap(bitmap);
            }
            //清空小圆点，为了避免小圆点重复添加或者多余添加
            ll_container.removeAllViews();
            //刷新碎片和小圆点
            initData();
            initPointData();
            //viewpager 滑动时候圆点的动画
            initViewPager();
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
        //从主界面函数中获取数据
        myApp=(MyApplication)getActivity().getApplication();
        userName=myApp.getUserName();
        bookListSize=myApp.getBookListSize();
        bookList=myApp.getBookBeanList();
        faviconPath=myApp.getFaviconPath();
    }

    //初始化碎片
    private void initData() {
        //存Frangment的list
        fragmentList = new ArrayList<Fragment>();
        for(int i=0;i<bookListSize;i++){
            //创建一个book碎片并向其传值
            BookFragment bookFragment= BookFragment.newInstance(bookList.get(i));
            Log.i("initdata:", bookList.get(i).toString());
            Log.i("initdata:", bookFragment.toString());
            fragmentList.add(bookFragment);
        }

        fragAdapter = new FragAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(fragAdapter);

    }

    //初始化小圆点
    private void initPointData() {
        for (int i = 0; i < fragmentList.size(); i++) {
            //初始化小圆点
            ImageView point = new ImageView(this.getContext());
            point.setBackgroundResource(R.drawable.shape_point_gray);
            //获取父布局参数,在初始化参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            if (i > 0) {
                layoutParams.leftMargin = 10;
            }
            point.setLayoutParams(layoutParams);
            ll_container.addView(point);
        }
    }

    private void initViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 当页面滑动的过程中调用的方法
             * positionOffset,移动偏移的百分比
             * position当前的位置
             * positionOffsetPixels  具体移动了多少像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //更新小红点的距离
                int leftMargin = (int) (pointMoveDistance * positionOffset) + position * pointMoveDistance;
                //计算小红点当前左边的距离
                //先拿到小红点爹的布局参数
                RelativeLayout.LayoutParams parmas = (RelativeLayout.LayoutParams) iv_red_piont.getLayoutParams();
                //再更新他的leftmargin实现移动
                parmas.leftMargin = leftMargin;
                //最后再给子view设置参数
                iv_red_piont.setLayoutParams(parmas);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //xml的监听
        iv_red_piont.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                iv_red_piont.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //获取到两个fragment移动时,两个点需要移动的距离
                if(bookListSize>1){
                    pointMoveDistance = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();
                }
                else{
                    pointMoveDistance=0;
                }

            }
        });
    }


//********************************以下为自动生成函数*************************************************
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    //BookFragment的数据交互
    public void onFragmentInteraction(Uri uri) {

    }

    //点击事件
    public void onClick(View v) {

    }


}


