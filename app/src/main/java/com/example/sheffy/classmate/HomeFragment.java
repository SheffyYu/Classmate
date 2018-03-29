package com.example.sheffy.classmate;
/**
 * 特别注意：OnFragmentInteractionListener也要在MainActivity中添加
 *          import android.support.v4.app.Fragment;
 *下一步：读取数据库创建同学录碎片，默认碎片在app、数据库中的创建。
 *
 */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HomeFragment extends Fragment implements BookFragment.OnFragmentInteractionListener
                                                     ,View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private BookFragment bookFragment;
    private TextView txv_user_name_home;
    private View view;

    //用户名
    private String userName;

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

        //设置用户名
        txv_user_name_home=(TextView) view.findViewById(R.id.txv_user_name_home);
        txv_user_name_home.setText(userName);

        //添加一个同学录碎片
        FragmentManager fragmentManager=getChildFragmentManager();
        FragmentTransaction fgTransaction=fragmentManager.beginTransaction();
        if(bookFragment == null){
            bookFragment = new BookFragment();
            fgTransaction.replace(R.id.bookFlContainer,bookFragment);
        }
        else{
            fgTransaction.show(bookFragment);
        }
        fgTransaction.commit();
        return view;
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
        userName=((MainActivity)getActivity()).getUserName();
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
