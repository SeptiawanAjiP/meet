package com.applozic.mobicomkit.uiwidgets.vote;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.applozic.mobicomkit.uiwidgets.R;

import java.util.ArrayList;

/**
 * Created by kaddafi on 22/04/2017.
 */

public class PilihanJawabanAdapter extends RecyclerView.Adapter<PilihanJawabanAdapter.PilihanViewHolder>{

    private ArrayList<Pilihan> arrayPilihan;
    private Context context;
    private int lastCheckedPosition = -1;

    public class PilihanViewHolder extends RecyclerView.ViewHolder{
        TextView tanggal,waktu, tempat, judul;
        CheckBox boxVote;

        public PilihanViewHolder(View view){
            super(view);
            judul = (TextView) view.findViewById(R.id.tv_judul_piljwb);
            tanggal = (TextView) view.findViewById(R.id.et_isiantanggal);
            waktu = (TextView) view.findViewById(R.id.et_isianwaktu);
            tempat = (TextView) view.findViewById(R.id.et_isiantempat);
            boxVote = (CheckBox) view.findViewById(R.id.box_vote);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = getAdapterPosition();
                    notifyItemRangeChanged(0, getItemCount());
                }
            });
        }
    }

    public PilihanJawabanAdapter(ArrayList<Pilihan> arrayPilihan, Context context){
        Log.d("pilihan","sampai");
        Log.d("pilihan",arrayPilihan.toString());
        this.arrayPilihan = arrayPilihan;
        this.context = context;
    }

    @Override
    public PilihanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_jwb_vote,parent,false);
        PilihanViewHolder holder = new PilihanViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(PilihanViewHolder holder, int position) {
        Pilihan pilihan = arrayPilihan.get(position);
        int posisi = position + 1;
        holder.judul.setText("Pilihan "+posisi);
        holder.tanggal.setText(pilihan.getTanggal());
        holder.waktu.setText(pilihan.getWaktu());
        holder.tempat.setText(pilihan.getTempat());
        holder.boxVote.setChecked(position == lastCheckedPosition);
    }

    @Override
    public int getItemCount() {
        return arrayPilihan.size();
    }

    public String getJwbVoteTerpilih(){
        return arrayPilihan.get(lastCheckedPosition).getId();
    }
}