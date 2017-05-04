package com.applozic.mobicomkit.uiwidgets.vote;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applozic.mobicomkit.uiwidgets.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Septiawan Aji Pradan on 3/10/2017.
 */

public class PilihanAdapter extends RecyclerView.Adapter<PilihanAdapter.PilihanViewHolder>{

private ArrayList<Pilihan> arrayPilihan;
private Context context;
public class PilihanViewHolder extends RecyclerView.ViewHolder{
    TextView pilihan,jumlahVote;
    public PilihanViewHolder(View view){
        super(view);
        pilihan = (TextView) view.findViewById(R.id.tv_judul_piljwb);
        jumlahVote = (TextView) view.findViewById(R.id.tv_hasil_piljwb);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

    public PilihanAdapter(ArrayList<Pilihan> arrayPilihan, Context context){
        Log.d("pilihan","sampai");
        Log.d("pilihan",arrayPilihan.toString());
        this.arrayPilihan = arrayPilihan;
        this.context = context;
    }

    @Override
    public PilihanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hasil_vote,parent,false);
        return new PilihanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PilihanViewHolder holder, int position) {
        Pilihan pilihan = arrayPilihan.get(position);
        holder.pilihan.setText("Pilihan "+(position+1));
        holder.jumlahVote.setText(pilihan.getJumlah());

    }

    @Override
    public int getItemCount() {
        return arrayPilihan.size();
    }
}