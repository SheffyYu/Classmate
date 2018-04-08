package com.example.sheffy.classmate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import bean.BookBean;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BookFragment extends Fragment{

    private OnFragmentInteractionListener mListener;
    private TextView txv_book_name,txv_classmate_number,txv_delete_book,txv_setup_book;
    private LinearLayout ll_book;
    private View view;

    private String bookName,strCount;

    public BookFragment() {
        // Required empty public constructor
    }

    public static BookFragment newInstance(BookBean bookBean){
        BookFragment bookFragment = new BookFragment();

        Bundle args = new Bundle();
        args.putString("bookName",bookBean.getBookId());
        args.putInt("classmateCount", bookBean.getClassmateCount());
        bookFragment.setArguments(args);

        return bookFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_book, container, false);

        int classmateCount=getArguments().getInt("classmateCount", 0);
        bookName=getArguments().getString("bookName","我的同学录");
        strCount=classmateCount+"";

        //初始化组件
        initView();

        txv_classmate_number.setText(strCount);
        txv_book_name.setText(bookName);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ll_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到目录
                Intent intent=new Intent(getActivity(),CatalogActivity.class);
                intent.putExtra("data_book_name",bookName);
                startActivity(intent);
                Log.i("onClick:", "跳转成功");
            }
        });

        //删除
        txv_delete_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
            }
        });

        //设置
        txv_setup_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

    //初始化组件
    public void initView(){
        txv_book_name=(TextView)view.findViewById(R.id.txv_book_name);
        txv_classmate_number=(TextView)view.findViewById(R.id.txv_classmate_number);
        txv_delete_book=(TextView)view.findViewById(R.id.txv_delete_book);
        txv_setup_book=(TextView)view.findViewById(R.id.txv_setup_book);
        ll_book=(LinearLayout)view.findViewById(R.id.ll_book);
    }


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
