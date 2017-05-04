package com.applozic.mobicomkit.uiwidgets.vote;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applozic.mobicomkit.uiwidgets.R;

import java.util.List;

/**
 * Created by kaddafi on 07/03/2017.
 */

//public class JudulVoteAdapter extends RecyclerView.Adapter<JudulVoteAdapter.MyViewHolder> {
//
//    private List<Vote> voteList;
//    private Activity activity;
//    private String id;
//
//    public class MyViewHolder extends RecyclerView.ViewHolder{
//        public TextView tvJudul;
//
//        public MyViewHolder(View view){
//            super(view);
//            tvJudul = (TextView)view.findViewById(R.id.tv_judul_piljwb);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    CreateVoteActivity.InputVoteDialog inputVoteDialog = new CreateVoteActivity.InputVoteDialog(activity,id);
//                    inputVoteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    inputVoteDialog.show();
//                }
//            });
//        }
//    }
//
//    public JudulVoteAdapter(List<Vote> voteList, Activity activity) {
//        this.voteList = voteList;
//        this.activity = activity;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_title_piljwb, parent, false);
//
//        return new MyViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(JudulVoteAdapter.MyViewHolder holder, int position) {
//        Vote vote = voteList.get(position);
//        id = vote.getId();
//        holder.tvJudul.setText("Pilihan "+id);
//    }
//
//    @Override
//    public int getItemCount() {
//        Log.d("DEBUGMU","size vote "+voteList.size());
//        return voteList.size();
//    }
//}
