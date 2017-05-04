package com.applozic.mobicomkit.uiwidgets.resumemeetup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.meetup.Meetup;

import java.util.ArrayList;

/**
 * Created by Septiawan Aji Pradan on 4/21/2017.
 */

public class HistoryMeetUpAdapter extends RecyclerView.Adapter<HistoryMeetUpAdapter.MyViewHolder>{
    private ArrayList<Meetup> arrayMeetup;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView status,judulMeetup,tanggalMeetup,tempatMeetup;
        public MyViewHolder(View view){
            super(view);
            judulMeetup = (TextView) view.findViewById(R.id.judul_meetup);
            tanggalMeetup = (TextView) view.findViewById(R.id.tanggal_meetup);
            status = (TextView) view.findViewById(R.id.status_meetup);
            tempatMeetup = (TextView) view.findViewById(R.id.alamat_meetup);
        }
    }

    public HistoryMeetUpAdapter(ArrayList<Meetup> arrayMeetup){
        this.arrayMeetup= arrayMeetup;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_meetup_resume ,null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Meetup meetup = arrayMeetup.get(position);
        holder.judulMeetup.setText(meetup.getJudul());
        holder.tempatMeetup.setText(meetup.getAlamat());
        holder.tanggalMeetup.setText(meetup.getTanggal());
        holder.status.setText(meetup.getStatus());
        if (meetup.getStatus().equals("Absen")){
            holder.status.setBackgroundResource(R.drawable.rounded_tidak_hadir);
        }else if(meetup.getStatus().equals("Telat")){
            holder.status.setBackgroundResource(R.drawable.rounded_telat);
        }
    }

    @Override
    public int getItemCount() {
        return arrayMeetup.size();
    }
}
