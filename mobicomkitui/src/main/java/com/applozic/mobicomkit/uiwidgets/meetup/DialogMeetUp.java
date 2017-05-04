package com.applozic.mobicomkit.uiwidgets.meetup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.conncetion.AlamatServer;
import com.applozic.mobicomkit.uiwidgets.instruction.ApplozicPermissions;
import com.applozic.mobicomkit.uiwidgets.main.MainActivityK;
import com.applozic.mobicomkit.uiwidgets.vote.VoteActivity;
import com.applozic.mobicommons.commons.core.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaddafi on 08/03/2017.
 */

public class DialogMeetUp extends Dialog implements View.OnClickListener {

    private Activity activity;
    private String id;
    private TextView tvJudul, isiTanggal, isiWaktu, isiTempat, trackingBtn, tutupBtn, selesaiMeetUp;
    private Spinner spinPilJwb;
    private Meetup meetup;
    private String org1;
    private String status;

    public DialogMeetUp(Activity activity,Meetup meetup,String org1,String status) {
        super(activity);
        this.activity = activity;
        this.meetup = meetup;
        this.org1 = org1;
        this.status = status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modal_dialog_meetup);
        tvJudul = (TextView) findViewById(R.id.tv_judul_piljwb);
        isiTanggal = (TextView) findViewById(R.id.et_isiantanggal);
        isiWaktu = (TextView)findViewById(R.id.et_isianwaktu);
        isiTempat = (TextView) findViewById(R.id.et_isiantempat);

        tvJudul.setText(meetup.getJudul().toString());
        isiTanggal.setText(meetup.getTanggal().toString());
        isiWaktu.setText(meetup.getPukul().toString());
        isiTempat.setText(meetup.getAlamat().toString());

        tutupBtn = (TextView) findViewById(R.id.btn_tutup);
        tutupBtn.setOnClickListener(this);

        trackingBtn = (TextView)findViewById(R.id.btn_tracking);
        trackingBtn.setOnClickListener(this);

        selesaiMeetUp = (TextView)findViewById(R.id.tv_selesai_meetup);

        if(status.equals("true")){
            selesaiMeetUp.setVisibility(View.VISIBLE);
            selesaiMeetUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finisMeetup(meetup.getId());
                    dismiss();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if(view==trackingBtn){
//            String tanggal = meetup.getTanggal().substring(0,2);
//            String bulan = meetup.getTanggal().substring(3,5);
//            String jam = meetup.getPukul().substring(0,2);
//            String menit = meetup.getPukul().substring(3,5);
//            Calendar cal = Calendar.getInstance();
//            if(tanggal.equals(Integer.toString(cal.get(Calendar.DAY_OF_MONTH))) &&
//                    bulan.equals(Integer.toString(cal.get(Calendar.MONTH)))){
//                int set = Integer.parseInt(jam)*60+Integer.parseInt(menit);
//                int get = cal.get(Calendar.HOUR_OF_DAY)*60+cal.get(Calendar.MINUTE);
//                Log.d("iqoh_set",Integer.toString(set));
//                Log.d("iqoh_get",Integer.toString(get));
//                if(set-get>=240){
//                    Toast.makeText(activity, "Fitur tracking belum aktif (aktif pada 2 jam sebelum meetup)", Toast.LENGTH_SHORT).show();
//                }else {
////                    Toast.makeText(CreateMeetupActivity.this, "MeetUp dibuat minimal 3 jam sebelumnya", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(activity, TrackingActivity.class);
//                    intent.putExtra("id_meetup",meetup.getId());
//                    intent.putExtra("id_user",org1);
//                    intent.putExtra("latitude",meetup.getLatitude());
//                    intent.putExtra("longitude",meetup.getLongitude());
//                    activity.startActivity(intent);
//                }
//            }else{
//                Toast.makeText(activity, "Fitur tracking belum aktif (aktif pada 2 jam sebelum meetup)", Toast.LENGTH_SHORT).show();
//            }
            Intent intent = new Intent(activity, TrackingActivity.class);
            intent.putExtra("id_meetup",meetup.getId());
            intent.putExtra("id_user",org1);
            intent.putExtra("latitude",meetup.getLatitude());
            intent.putExtra("longitude",meetup.getLongitude());
//            Toast.makeText(activity, "Pada aplikasi yang sebenarnya, fitur tracking baru aktif 2 jam sebelummeetup", Toast.LENGTH_LONG).show();
            activity.startActivity(intent);

            dismiss();
        }else if(view==tutupBtn){
            dismiss();
        }
    }
    public void finisMeetup(final String id_meetup){
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        final String url = AlamatServer.getAlamatServer()+AlamatServer.getFinishMeetup();

        StringRequest finishVote = new StringRequest(Request.Method.POST, url , new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Log.d("sayang",jsonObject.toString());
                    Toast.makeText(activity, "Berhapus hapus meetup", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("error", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("id_meetup",id_meetup);
                return param;
            }
        };
        requestQueue.add(finishVote);
    }
}
