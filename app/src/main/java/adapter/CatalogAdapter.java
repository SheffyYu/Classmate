package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sheffy.classmate.R;

import java.util.List;

import application.MyApplication;
import bean.ClassmateBean;

/**
 * Created by Sheffy on 2018/4/9.
 */

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {
    private List<ClassmateBean> mCatalogList;
    private MyApplication myApplication;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View catalogView;
        ImageView sexImage;
        TextView nameText;

        public ViewHolder(View view){
            super(view);
            catalogView=view;
            sexImage=(ImageView)view.findViewById(R.id.iv_sex);
            nameText=(TextView)view.findViewById(R.id.txv_item_name);
        }
    }

    public CatalogAdapter(List<ClassmateBean> classmateBeen){
        mCatalogList=classmateBeen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.catalog_item,parent,false);

        final ViewHolder holder=new ViewHolder(view);
        holder.catalogView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position =holder.getAdapterPosition();
                ClassmateBean clb=mCatalogList.get(position);
                //此处跳转到个人单页****************************************************************
                Toast.makeText(v.getContext(),clb.getClassmateName(),Toast.LENGTH_SHORT).show();
                myApplication=(MyApplication)v.getContext().getApplicationContext();
                myApplication.setClassmateBean(clb);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClassmateBean classmateBean=mCatalogList.get(position);
        if(classmateBean.getSex().equals("女")){
            holder.sexImage.setImageResource(R.drawable.girl);
        }
        else{
            holder.sexImage.setImageResource(R.drawable.boy);
        }
        holder.nameText.setText(classmateBean.getClassmateName());
    }

    @Override
    public int getItemCount() {
        return mCatalogList.size();
    }
}
