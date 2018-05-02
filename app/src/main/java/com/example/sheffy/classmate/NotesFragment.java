package com.example.sheffy.classmate;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.NotesAdapter;
import application.MyApplication;
import bean.NotesBean;
import circleimageview.SpaceItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NotesFragment extends Fragment {
    private View view;
    private TextView txv_blank;
    private RecyclerView rv_list_notes;
    private LinearLayoutManager linearLayoutManager;
    private NotesAdapter notesAdapter;

    private MyApplication myApp;
    private String userName;
    private List<NotesBean> notesBeanList=new ArrayList<NotesBean>();

    public NotesFragment() {}

    //数据操作
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取用户名
        myApp=(MyApplication)getActivity().getApplication();
        userName=myApp.getUserName();
        notesBeanList=myApp.getNotesBeanList();
    }

    //UI操作
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notes, container, false);

        //初始化控件
        initView();

        //当记录为空时
        if (notesBeanList.size()==0){
            txv_blank.setText("还没有任何回忆哦~");
        }

            return view;
    }

    //初始化控件
    protected void initView(){
        txv_blank=(TextView)view.findViewById(R.id.txv_blank_notes);
        rv_list_notes=(RecyclerView)view.findViewById(R.id.rv_list_note);

        linearLayoutManager=new LinearLayoutManager(getContext());
        notesAdapter=new NotesAdapter(notesBeanList);
        rv_list_notes.setLayoutManager(linearLayoutManager);
        //设置item间距，30dp
        rv_list_notes.addItemDecoration(new SpaceItemDecoration(0,20));
        rv_list_notes.setAdapter(notesAdapter);
    }

    //碎片中的操作，如点击事件
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
