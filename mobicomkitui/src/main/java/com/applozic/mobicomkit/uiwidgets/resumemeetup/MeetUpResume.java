package com.applozic.mobicomkit.uiwidgets.resumemeetup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applozic.mobicomkit.sample.LoginActivity;
import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.conncetion.AlamatServer;
import com.applozic.mobicomkit.uiwidgets.meetup.Meetup;
import com.applozic.mobicomkit.uiwidgets.meetup.MeetupDatabase;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Septiawan Aji Pradan on 4/20/2017.
 */

public class MeetUpResume extends AppCompatActivity {
    private PieChart pieChart ;
    private ArrayList<Entry> entries ;
    private ArrayList<String> PieEntryLabels ;
    private PieDataSet pieDataSet ;
    private PieData pieData ;

    private ArrayList<Meetup> arrayMeetup;

    private TextView totalMeetup,hadir,terlambat,tidakHadir,status,namaGroupTv,ketPersenTotal;
    private ImageView iconStatus;
    private RecyclerView historyMeet;
    private HistoryMeetUpAdapter historyMeetUpAdapter;
    final String url = AlamatServer.getAlamatServer()+ AlamatServer.getRESUME();
    public final static String ID_USER = "id_user";
    public final static String ID_GROUP = "id_group";
    public final static String NAMA_GROUP = "nama_group";
    public final static String OTHER_USER = "other_user";
    private final long SATU_MENIT = 60000;
    String idUser,idGroup,namaGroup,otherUser;
    private MeetupDatabase db;
    int [] warna = {R.color.colorPrimary, R.color.alphabet_e, R.color.primary_dark_color};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meetup_resume_2);
        db = new MeetupDatabase(getApplicationContext());
        totalMeetup = (TextView) findViewById(R.id.total_meet_up);
        hadir = (TextView) findViewById(R.id.tepat_meet_up);
        iconStatus = (ImageView) findViewById(R.id.icon_penghargaan);
        status = (TextView)findViewById(R.id.status);
        namaGroupTv = (TextView)findViewById(R.id.nama_group);
        historyMeet = (RecyclerView)findViewById(R.id.rcv_meetup);
        terlambat = (TextView)findViewById(R.id.tv_telat);
        tidakHadir = (TextView)findViewById(R.id.tv_absen);
        ketPersenTotal = (TextView)findViewById(R.id.ket_persen_total);

        idUser = getIntent().getStringExtra(ID_USER);
        idGroup = getIntent().getStringExtra(ID_GROUP);
        namaGroup = getIntent().getStringExtra(NAMA_GROUP);
        otherUser = getIntent().getStringExtra(OTHER_USER);

        entries = new ArrayList<>();
        PieEntryLabels = new ArrayList<>();
        arrayMeetup = new ArrayList<>();

        cekDatabaseMeetup();
        getData();
    }

    public void getData(){
        StringRequest getResumeMeetmup = new StringRequest(Request.Method.GET, "http://iyasayang.esy.es/kudi/index.php/kegiatankudi/meetup", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final int tot,tepat,telat,tdkhadir;
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    tot = jsonObject.getInt("total_meetup");
                    tepat = jsonObject.getInt("tepat_waktu");
                    telat = jsonObject.getInt("terlambat");
                    tdkhadir = jsonObject.getInt("tidak_hadir");
                    Meetup meetup = new Meetup("Rapat Osis","Bangsal SMA 1 Purwokerto","22 Maret 2012","Hadir");
                    arrayMeetup.add(meetup);
                    meetup = new Meetup("Rapat Paskib 19","Bangsal SMA 1 Purwokerto","19 Januari 2012","Absen");
                    arrayMeetup.add(meetup);
                    meetup = new Meetup("Ekskul Olimpiade","Kelas XI IPA 4","19 Januari 2010","Telat");
                    arrayMeetup.add(meetup);
                    meetup = new Meetup("Kajian Rohis","Masjid SMA 1 Purwokerto","22 Maret 2010","Telat");
                    arrayMeetup.add(meetup);

                    setData(tot,tepat,telat,tdkhadir);
                    historyMeetUp(arrayMeetup);
                }catch (Exception e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> param = new HashMap<String, String>();
//                param.put("orang1",ID_USER);
//                param.put("orang2",OTHER_USER);
//                param.put("id_group",ID_GROUP);
//                return super.getParams();
//            }
//        }
        ;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(getResumeMeetmup);
    }

    public void setData(int banyak,int tepat,int telat,int tidak){
        namaGroupTv.setText(namaGroup);
        totalMeetup.setText(Integer.toString(banyak));
        float presentase =(float) tepat/banyak*100;
        ketPersenTotal.setText(String.format("%.1f",presentase));
        hadir.setText(Integer.toString(tepat));
        terlambat.setText(Integer.toString(telat));
        tidakHadir.setText(Integer.toString(tidak));
        setIconStatus(presentase);
    }

    public void setIconStatus(float persentase){
        if(persentase<=60){
            iconStatus.setImageResource(R.drawable.ic_sad);
            status.setText("Pemalas");
            status.setBackgroundResource(R.drawable.rounded_tidak_hadir);
        }else if(persentase>60 && persentase <=80){
            iconStatus.setImageResource(R.drawable.ic_sedang);
            status.setText("Biasa");
            status.setBackgroundResource(R.drawable.rounded_telat);
        }else{
            iconStatus.setImageResource(R.drawable.ic_happy);
            status.setText("Anak Rajin");
            status.setBackgroundResource(R.drawable.rounded_profil);
        }
    }


    public void historyMeetUp(ArrayList<Meetup> arrayMeetup){
        historyMeetUpAdapter = new HistoryMeetUpAdapter(arrayMeetup);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        historyMeet.setLayoutManager(layoutManager);
        historyMeet.setItemAnimator(new DefaultItemAnimator());
        historyMeet.setAdapter(historyMeetUpAdapter);
    }

    public void cekDatabaseMeetup(){
        setData();
        final Handler mHandler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ArrayList<Meetup> arrayList;
                arrayList = db.getMeetup();

                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String strDate = sdf.format(c.getTime());
                Log.d("menikah_sistem",strDate);

                for(Meetup meetup : arrayList){
                    String perbandingan = meetup.getTanggal()+" "+meetup.getPukul();
                    Calendar jamMeetup = Calendar.getInstance();
                    try {
                        jamMeetup.setTime(sdf.parse(perbandingan));
                        c.setTime(sdf.parse(strDate));
                    }catch (Exception e){
                        Log.d("salah_dijam","salah");
                    }
                    Log.d("salah_now",Long.toString(jamMeetup.getTimeInMillis()));
                    Log.d("salah_now_1",Long.toString(c.getTimeInMillis()));
                    if(c.getTimeInMillis()==jamMeetup.getTimeInMillis()-30*SATU_MENIT){
                        setNotif();
                    }
                }
                mHandler.postDelayed(this, 60000);
            }
        };
        mHandler.post(runnable);
    }

    public void setData(){
        ArrayList<Meetup> arrayList = new ArrayList<>();
        Meetup meetup = new Meetup();
        meetup.setJudul("Muter-muter sore");
        meetup.setAlamat("Alun-alun Purwokerto");
        meetup.setTanggal("04-05-2017");
        meetup.setPukul("15:09");
        db.insertMeetUp(meetup);
        meetup = new Meetup();
        meetup.setJudul("Muter-muter sore");
        meetup.setAlamat("Alun-alun Purwokerto");
        meetup.setTanggal("04-05-2017");
        meetup.setPukul("15:10");
        db.insertMeetUp(meetup);
        meetup = new Meetup();
        meetup.setJudul("Muter-muter sore");
        meetup.setAlamat("Alun-alun Purwokerto");
        meetup.setTanggal("04-05-2017");
        meetup.setPukul("15:11");
        db.insertMeetUp(meetup);
        meetup = new Meetup();
        meetup.setJudul("Muter-muter sore");
        meetup.setAlamat("Alun-alun Purwokerto");
        meetup.setTanggal("04-05-2017");
        meetup.setPukul("15:12");
        db.insertMeetUp(meetup);
    }

    public void setNotif(){
        Intent notificationIntent = new Intent(getApplicationContext(), LoginActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MeetUpResume.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        Notification notification = builder.setContentTitle("Ada Meetup")
                .setContentText("Ayo Datang Meetup")
                .setTicker("Cuss")
                .setSmallIcon(R.drawable.ic_meetup)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }


}
