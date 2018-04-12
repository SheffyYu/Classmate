package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sheffy.classmate.R;

import java.util.List;

import bean.ClassmateBean;

/**
 * Created by Sheffy on 2018/4/12.
 */

public class ClassmateAdapter extends ArrayAdapter<ClassmateBean> {
    private int resourceId;
    public ClassmateAdapter(Context context,int textViewResourceId, List<ClassmateBean> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassmateBean classmateBean=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.imageView=(ImageView)view.findViewById(R.id.iv_sex);
            viewHolder.textView=(TextView)view.findViewById(R.id.txv_item_name);
            view.setTag(viewHolder);
        }
        else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }

        if(classmateBean.getSex().equals("女")){
            viewHolder.imageView.setImageResource(R.drawable.girl);
        }
        else {
            viewHolder.imageView.setImageResource(R.drawable.boy);
        }

        viewHolder.textView.setText(classmateBean.getClassmateName());
        return view;
    }

    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
