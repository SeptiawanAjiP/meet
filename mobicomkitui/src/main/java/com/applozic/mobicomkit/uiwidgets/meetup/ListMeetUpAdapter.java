package com.applozic.mobicomkit.uiwidgets.meetup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.applozic.mobicomkit.uiwidgets.R;

import java.util.ArrayList;

/**
 * Created by kaddafi on 22/04/2017.
 */

public class ListMeetUpAdapter extends RecyclerView.Adapter<ListMeetUpAdapter.MyViewHolder>{
    private ArrayList<Meetup> arrayMeetup;
    private Activity activity;
    private String idUser;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tgl, blnthn, jam, judul, tempat, jumlahpst;
        public MyViewHolder(View view){
            super(view);
            tgl = (TextView) view.findViewById(R.id.tgl_mu);
            jam = (TextView) view.findViewById(R.id.jam_mu);
            judul = (TextView) view.findViewById(R.id.judul_mu);
            tempat = (TextView) view.findViewById(R.id.tmpt_mu);
            jumlahpst = (TextView) view.findViewById(R.id.jml_pst_mu);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, TrackingActivity.class);
                    intent.putExtra("id_meetup",arrayMeetup.get(getAdapterPosition()).getId());
                    intent.putExtra("id_user",idUser);
                    activity.startActivity(intent);
                }
            });
        }
    }

    public ListMeetUpAdapter(Activity activity, ArrayList<Meetup> arrayMeetup, String idUser){
        this.arrayMeetup = arrayMeetup;
        this.activity= activity;
        this.idUser = idUser;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_meetup,null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Meetup meetup = arrayMeetup.get(position);
        holder.tgl.setText(meetup.getTanggal());
        holder.jam.setText(meetup.getPukul());
        holder.judul.setText(meetup.getJudul());
        holder.jumlahpst.setText(""+meetup.getJumlahPeserta()+" peserta");
        holder.tempat.setText(meetup.getAlamat());
    }

    @Override
    public int getItemCount() {
        return arrayMeetup.size();
    }
}