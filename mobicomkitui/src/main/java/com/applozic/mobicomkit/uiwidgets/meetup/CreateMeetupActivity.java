package com.applozic.mobicomkit.uiwidgets.meetup;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.attachment.FileMeta;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.MessageClientService;
import com.applozic.mobicomkit.api.conversation.MessageIntentService;
import com.applozic.mobicomkit.api.conversation.MobiComConversationService;
import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.conncetion.AlamatServer;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.applozic.mobicomkit.uiwidgets.conversation.fragment.MobiComConversationFragment;
import com.applozic.mobicomkit.uiwidgets.instruction.ApplozicPermissions;
import com.applozic.mobicomkit.uiwidgets.meetup.Meetup;
import com.applozic.mobicomkit.uiwidgets.schedule.ScheduledTimeHolder;
import com.applozic.mobicommons.commons.core.utils.PermissionsUtils;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.applozic.mobicommons.people.SearchListFragment;
import com.applozic.mobicommons.people.channel.Channel;
import com.applozic.mobicommons.people.contact.Contact;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Septiawan Aji Pradan on 3/7/2017.
 */

public class CreateMeetupActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String ORANG_1 = "orang1";
    public final static String ORANG_2 = "orang";
    public final static String ID_GROUP = "id_group";
    public final static String JUDUL = "judul";
    public final static String TANGGAL = "tanggal";
    public final static String JAM = "jam";
    public final static String TEMPAT = "tempat";
    private Meetup meetup;
    private TextView tanggal,pukul;
    private EditText judul,tempat;
    private RelativeLayout buatMeetup;
    public LinearLayout layout;
    private ApplozicPermissions applozicPermission;
    ActionBar actionBar;
    private DatePickerDialog getTanggal;
    private TimePickerDialog getPukul;
    private SimpleDateFormat dateFormat;
    private ProgressDialog progressDialog;
    private int tanggalHariIni;
    private String namatempat;
    private LatLng lokasi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meet_up);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Buat MeetUp");
        applozicPermission = new ApplozicPermissions(this, layout);
        meetup = new Meetup();
        layout = (LinearLayout) findViewById(R.id.footerAd);
        judul = (EditText)findViewById(R.id.et_ip_judulmu);
        tempat = (EditText)findViewById(R.id.et_ip_tmptmu);
        tempat.setInputType(InputType.TYPE_NULL);
        tempat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Utils.hasMarshmallow()) {
//                    new ApplozicPermissions(CreateMeetupActivity.this, layout).checkRuntimePermissionForLocation();
//                } else {
//                    startActivityForResult(new Intent(CreateMeetupActivity.this, TempatActivity.class),7);
//                }
//                if(Utils.hasMarshmallow() && PermissionsUtils.checkSelfPermissionForLocation(CreateMeetupActivity.this)){
//                    applozicPermission.requestLocationPermissions();
//                } else {
//                    startMap();
//                }
//                if (Utils.hasMarshmallow()) {
//                    new ApplozicPermissions(CreateMeetupActivity.this, layout).checkRuntimePermissionForLocation();
//                } else {
//                    startMap();
//                }
                startActivityForResult(new Intent(CreateMeetupActivity.this, TempatActivity.class),7);
            }
        });

        dateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.US);
        ambilTanggal();
        setDateTimeField();
        setClock();
        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==7)
        {
            String message = data.getStringExtra("MESSAGE");
            Log.d("DEBUGMU"," messageterima " +message);
            if(!message.equals("kosong")){
                String[] latLng = message.split(",");
                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);
                namatempat = latLng[2];
                tempat.setText(namatempat);
                lokasi = new LatLng(latitude,longitude);
                Toast.makeText(getApplicationContext(),"Berhasil menambahkan tempat",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ambilTanggal(){
        tanggal = (TextView)findViewById(R.id.textIpTgl);
        tanggal.setInputType(InputType.TYPE_NULL);
        tanggal.requestFocus();

        pukul = (TextView)findViewById(R.id.textIpPukul);
        pukul.setInputType(InputType.TYPE_NULL);

        buatMeetup = (RelativeLayout)findViewById(R.id.rv_buat_meetup);
    }

    private void setDateTimeField(){
        tanggal.setOnClickListener(this);

        final Calendar newCalendar = Calendar.getInstance();
        getTanggal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(i,i1,i2);

                tanggalHariIni =i2;
                if(newDate.getTime().before(newCalendar.getTime())){
                    Toast.makeText(CreateMeetupActivity.this, "Tanggal sudah berlalu", Toast.LENGTH_SHORT).show();
                    tanggal.setHint("Tanggal Sudah Berlalu");
                }else{
                    tanggal.setText(dateFormat.format(newDate.getTime()));
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));

        getTanggal.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

    }

    private void setClock(){
        pukul.setOnClickListener(this);

        final Calendar calendar = Calendar.getInstance();

        getPukul = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                if(tanggalHariIni==calendar.get(Calendar.DAY_OF_MONTH)){
                    int set = i*60+i1;
                    int get = calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE);
                    Log.d("iqoh_set",Integer.toString(set));
                    Log.d("iqoh_get",Integer.toString(get));
                    if(set-get>=180){
                        if(i<10){
                            if(i1<10){
                                pukul.setText("0"+i+":0"+i1);
                            }else{
                                pukul.setText("0"+i+":"+i1);
                            }
                        }else {
                            if(i1<10){
                                pukul.setText(i+":0"+i1);
                            }else{
                                pukul.setText(i+":"+i1);
                            }
                        }
                    }else {
                        Toast.makeText(CreateMeetupActivity.this, "MeetUp dibuat minimal 3 jam sebelumnya", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(i<10){
                        if(i1<10){
                            pukul.setText("0"+i+":0"+i1);
                        }else{
                            pukul.setText("0"+i+":"+i1);
                        }
                    }else {
                        if(i1<10){
                            pukul.setText(i+":0"+i1);
                        }else{
                            pukul.setText(i+":"+i1);
                        }
                    }
                }

            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);

    }

    private void getData(){
        buatMeetup.setOnClickListener(this);
        if(!judul.getText().toString().isEmpty()){
            if(!tanggal.getText().toString().isEmpty()){
                if(!pukul.getText().toString().isEmpty()){
                    if(!tempat.getText().toString().isEmpty()){
                        meetup.setJudul(judul.getText().toString());
                        meetup.setTanggal(tanggal.getText().toString());
                        meetup.setPukul(pukul.getText().toString());
                        meetup.setAlamat(tempat.getText().toString());
                        meetup.setLatitude(lokasi.latitude);
                        meetup.setLongitude(lokasi.longitude);
                        meetup.setOrang1(getIntent().getStringExtra(ORANG_1));
                        meetup.setOrang2(getIntent().getStringExtra(ORANG_2));
                        meetup.setIdGroup(getIntent().getStringExtra(ID_GROUP));
                        createMeetup(meetup);
                    }else{
                        Toast.makeText(this, "Masukan tempat meetup", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Masukan pukul meetup", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Masukan tanggal meetup", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Masukan judul meetup", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        if(view==tanggal){
            getTanggal.show();
        }else if(view==pukul){
            getPukul.show();
        }else if(view==buatMeetup) {
            getData();
        }
    }

    private void showProgress() {
        progressDialog=null;// Initialize to null
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createMeetup(final Meetup meetup){
//        Log.d("adinda",meetup.getOrang1());
//        Log.d("adinda",meetup.getOrang2());
//        Log.d("adinda",meetup.getIdGroup());
//        Log.d("adinda",meetup.getJudul());
//        Log.d("adinda",meetup.getAlamat());
//        Log.d("adinda",meetup.getTanggal());
//        Log.d("adinda",meetup.getPukul());

        showProgress();
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest login = new StringRequest(Request.Method.POST, AlamatServer.getAlamatServer()+AlamatServer.getCreateMeetup(), new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), jsonObject.get("status").toString(), Toast.LENGTH_SHORT).show();
                    sendMessage("Haii...Aku udah buat meetup..Cek ya..", null,null, null, Message.ContentType.DEFAULT.getValue());
                    finish();
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
                param.put(ORANG_1,meetup.getOrang1());
                param.put(ORANG_2,meetup.getOrang2());
                param.put(ID_GROUP,meetup.getIdGroup());
                param.put(TANGGAL,meetup.getTanggal());
                param.put(JAM,meetup.getPukul());
                param.put(JUDUL,meetup.getJudul());
                param.put(TEMPAT,meetup.getAlamat());
                param.put("latitude",Double.toString(meetup.getLatitude()));
                param.put("longitude",Double.toString(meetup.getLongitude()));
                return param;
            }
        };
        requestQueue.add(login);
    }

    public void sendMessage(String message, Map<String,String> messageMetaData, FileMeta fileMetas, String fileMetaKeyStrings, short messageContentType) {
        MobiComUserPreference userPreferences = MobiComUserPreference.getInstance(getApplicationContext());
        Message messageToSend = new Message();
        ScheduledTimeHolder scheduledTimeHolder = new ScheduledTimeHolder();
        MobiComConversationService mobiComConversationService = new MobiComConversationService(getApplicationContext());
        Class messageIntentClass = new MessageIntentService().getClass();

        if (meetup.getOrang2().equals("-")) {
            messageToSend.setGroupId(Integer.parseInt(meetup.getIdGroup()));
            messageToSend.setClientGroupId(null);

            /*   List<String> contactIds = new ArrayList<String>();
            List<String> toList = new ArrayList<String>();
            for (Contact contact : channel.getContacts()) {
                if (!TextUtils.isEmpty(contact.getContactNumber())) {
                    toList.add(contact.getContactNumber());
                    contactIds.add(contact.getFormattedContactNumber());
                }
            }
            messageToSend.setTo(TextUtils.join(",", toList));
            messageToSend.setContactIds(TextUtils.join(",", contactIds));*/
        } else {
            messageToSend.setTo(meetup.getOrang2());
            messageToSend.setContactIds(meetup.getOrang1());
        }

        messageToSend.setContentType(messageContentType);
        messageToSend.setRead(Boolean.TRUE);
        messageToSend.setStoreOnDevice(Boolean.TRUE);
        if (messageToSend.getCreatedAtTime() == null) {
            messageToSend.setCreatedAtTime(System.currentTimeMillis() + userPreferences.getDeviceTimeOffset());
        }
//        if(currentConversationId != null && currentConversationId != 0){
//            messageToSend.setConversationId(currentConversationId);
//        }
        messageToSend.setSendToDevice(Boolean.FALSE);
        messageToSend.setMessage(message);
        messageToSend.setDeviceKeyString(userPreferences.getDeviceKeyString());
        messageToSend.setScheduledAt(scheduledTimeHolder.getTimestamp());
        messageToSend.setSource(Message.Source.MT_MOBILE_APP.getValue());

        messageToSend.setFileMetaKeyStrings(fileMetaKeyStrings);
        messageToSend.setFileMetas(fileMetas);
        messageToSend.setMetadata(messageMetaData);

        mobiComConversationService.sendMessage(messageToSend, messageIntentClass);

    }
}
