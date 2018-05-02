package adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sheffy.classmate.R;
import com.example.sheffy.classmate.ShowNotesItemActivity;

import java.util.List;

import application.MyApplication;
import bean.NotesBean;

/**
 * Created by Sheffy on 2018/5/2.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{
    private List<NotesBean> mNotesList;
    private MyApplication myApplication;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View notesView;
        TextView dateText;
        TextView titleText;

        public ViewHolder(View view){
            super(view);
            notesView=view;
            dateText=(TextView)view.findViewById(R.id.txv_item_date);
            titleText=(TextView)view.findViewById(R.id.txv_item_title);
        }
    }

    public NotesAdapter(List<NotesBean> NotesList){
        mNotesList=NotesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_item,parent,false);

        final ViewHolder holder=new ViewHolder(view);
        holder.notesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =holder.getAdapterPosition();
                NotesBean nb=mNotesList.get(position);
                //此处跳转到记录单页****************************************************************
                myApplication=(MyApplication)v.getContext().getApplicationContext();
                myApplication.setNotesBean(nb);
                Log.i("myApplication:", nb.toString());

                Intent intent=new Intent(v.getContext(), ShowNotesItemActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotesBean notesBean=mNotesList.get(position);
        holder.dateText.setText(notesBean.getDate());
        holder.titleText.setText(notesBean.getTitle());
    }

    @Override
    public int getItemCount() {
        return mNotesList.size();
    }


}
