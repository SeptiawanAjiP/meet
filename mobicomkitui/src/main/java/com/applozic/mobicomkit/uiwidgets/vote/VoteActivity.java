package com.applozic.mobicomkit.uiwidgets.vote;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.attachment.FileMeta;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.MessageIntentService;
import com.applozic.mobicomkit.api.conversation.MobiComConversationService;
import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.conncetion.AlamatServer;
import com.applozic.mobicomkit.uiwidgets.meetup.DialogMeetUp;
import com.applozic.mobicomkit.uiwidgets.meetup.DialogMeetUpKosong;
import com.applozic.mobicomkit.uiwidgets.schedule.ScheduledTimeHolder;
import com.applozic.mobicommons.people.channel.ChannelUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteActivity extends AppCompatActivity {

    boolean statAdmin = true;
    boolean voteKosong = true;
    LinearLayout llHasilVote, llKosong;
    RelativeLayout buatVote;
    ActionBar actionBar;
    private TextView isiVote, finishVote;
    RecyclerView recyclerView, rvPilJwbVote;
    PilihanAdapter pilihanAdapter;
    PilihanJawabanAdapter pilihanJawabanAdapter;
    ArrayList<Pilihan> arrayPilihan;
    Vote vote;
    HashMap<String,Pilihan> pilihanPilihan;
    ProgressDialog progressDialog;

    private String idUser, idGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Set up the action bar.
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Vote");

        llKosong = (LinearLayout)findViewById(R.id.ll_kosong);
        llHasilVote = (LinearLayout)findViewById(R.id.ll_hasil_vote);
        buatVote = (RelativeLayout)findViewById(R.id.rv_buat_vote_baru);
        isiVote = (TextView)findViewById(R.id.tv_isi_vote);
        finishVote = (TextView)findViewById(R.id.tv_finish_vote);
        recyclerView = (RecyclerView)findViewById(R.id.rv_hasil_vote);
        rvPilJwbVote = (RecyclerView)findViewById(R.id.rv_list_jwb);
        arrayPilihan = new ArrayList<>();
        vote = new Vote();
        pilihanPilihan = new HashMap<>();
        pilihanAdapter = new PilihanAdapter(arrayPilihan,this);
        pilihanAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(pilihanAdapter);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        rvPilJwbVote.setLayoutManager(mLayoutManager2);

        idUser = getIntent().getStringExtra("id_user");
        idGroup = getIntent().getStringExtra("id_group");
        cekVoting(idUser, idGroup);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void cekVoting(final String id_user, final String id_group){
        showProgress();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url = AlamatServer.getAlamatServer()+AlamatServer.getCheckVoting();

        StringRequest login = new StringRequest(Request.Method.POST, url , new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Log.d("sayang",jsonObject.toString());
                    if(jsonObject.get("status").equals("ada")){

                        JSONObject jsonObject1 = jsonObject.getJSONObject("vote");
                        JSONArray jsonArray = jsonObject1.getJSONArray("pilihan");

                        vote.setJudul(jsonObject1.getString("judul"));
                        vote.setId(jsonObject1.getString("id"));
                        for(int i=0;i<jsonArray.length();i++){
                            Pilihan pilihan = new Pilihan();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            pilihan.setTanggal(jsonObject2.getString("tanggal"));
                            pilihan.setWaktu(jsonObject2.getString("jam"));
                            pilihan.setTempat(jsonObject2.getString("tempat"));
                            pilihan.setId(jsonObject2.getString("id"));
                            pilihan.setJumlah(jsonObject2.getString("jumlah_voting"));
                            arrayPilihan.add(pilihan);
                        }
                        Log.d("sayang",arrayPilihan.toString());
                        vote.setPilihan(arrayPilihan);
                        setTampilan(vote);
                        progressDialog.dismiss();
                    }else{
                        setTampilan(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("error", e.toString());
                }
                pilihanAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("id_user",id_user);
                param.put("id_group",id_group);
                return param;
            }
        };
        requestQueue.add(login);
    }

    private void showProgress() {
        progressDialog=null;// Initialize to null
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void setTampilan(final Vote vote){
        if(vote != null){
            Log.d("sayang",vote.getPilihan().toString());
            Log.d("sayang",vote.getJudul());
            if(getIntent().getStringExtra("is_admin").equals("true")){
                finishVote.setVisibility(View.VISIBLE);
                finishVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finisHVote(vote.getId());
                    }
                });
            }
            llKosong.setVisibility(View.GONE);
            llHasilVote.setVisibility(View.VISIBLE);
            actionBar.setTitle(vote.getJudul());
            pilihanJawabanAdapter = new PilihanJawabanAdapter(vote.getPilihan(), this);
            rvPilJwbVote.setAdapter(pilihanJawabanAdapter);
            isiVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    VoteDialog voteDialog = new VoteDialog(VoteActivity.this,getIntent().getStringExtra("id_user"),vote.getId());
//                    voteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    voteDialog.show();
                    addUserVote(idUser,vote.getId(),pilihanJawabanAdapter.getJwbVoteTerpilih());
                }
            });
        } else {
            Log.d("nyasar","kene");
            llKosong.setVisibility(View.VISIBLE);
            llHasilVote.setVisibility(View.GONE);
            if(getIntent().getStringExtra("is_admin").equals("true")){
                buatVote.setVisibility(View.VISIBLE);
                buatVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),CreateVoteActivity.class);
                        intent.putExtra("id_group",getIntent().getStringExtra("id_group"));
                        startActivity(intent);
                        finish();
                    }
                });
            }else{
                buatVote.setVisibility(View.GONE);
            }
        }
    }

    public void finisHVote(final String id_vote){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url = AlamatServer.getAlamatServer()+AlamatServer.getFinishVote();

        StringRequest finishVote = new StringRequest(Request.Method.POST, url , new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if(jsonObject.getString("status").equals("success")){
                        sendMessage("Gaes..Vote nya udah selesai", null,null, null, Message.ContentType.DEFAULT.getValue());
                        Toast.makeText(VoteActivity.this, "Vote telah diselesaikan", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(VoteActivity.this, jsonObject.getString("status").toString(), Toast.LENGTH_SHORT).show();
                    }
                    Log.d("sayang",jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("error", e.toString());
                }
                pilihanAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("id_vote",id_vote);
                return param;
            }
        };
        requestQueue.add(finishVote);
    }


//    class VoteDialog extends Dialog implements View.OnClickListener {
//
//        private Activity activity;
//        private String idUser,voteId;
//        private TextView tvJudul, isiTanggal, isiWaktu, isiTempat, simpanBtn, batalBtn;
//        private Spinner spinPilJwb;
//        private List<Integer> intPil;
//
//        public VoteDialog(Activity activity,String idUser,String voteId) {
//            super(activity);
//            this.activity = activity;
//            this.idUser = idUser;
//            this.voteId = voteId;
//        }
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            setContentView(R.layout.modal_vote);
//            isiTanggal = (TextView) findViewById(R.id.et_isiantanggal);
//            isiWaktu = (TextView)findViewById(R.id.et_isianwaktu);
//            isiTempat = (TextView) findViewById(R.id.et_isiantempat);
//            simpanBtn = (TextView)findViewById(R.id.btn_simpan_piljwb);
//            spinPilJwb = (Spinner)findViewById(R.id.spinner_jmlpil);
//
//            intPil = new ArrayList<>();
//            for (int i = 1; i <= arrayPilihan.size(); i++) {
//                intPil.add(i);
//            }
//            ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(activity, android.R.layout.simple_spinner_item,intPil);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinPilJwb.setAdapter(dataAdapter);
//
//            spinPilJwb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    final int berapa = i;
//                    Log.d("nengkene_vote_id",voteId);
//                    Log.d("nengkene_id_user",idUser);
//                    isiTanggal.setText(arrayPilihan.get(i).getTanggal());
//                    isiWaktu.setText(arrayPilihan.get(i).getWaktu());
//                    isiTempat.setText(arrayPilihan.get(i).getTempat());
//                    simpanBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            addUserVote(idUser,voteId,arrayPilihan.get(berapa).getId());
//                            dismiss();
//                        }
//                    });
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//            batalBtn = (TextView)findViewById(R.id.btn_batal_piljwb);
//            batalBtn.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View view) {
//
//        }
//
//        public void addUserVote(final String id_user, final String voteId, final String idPilihan){
//            Log.d("nengkene_vote_id",voteId);
//            Log.d("nengkene_id_user",id_user);
//            Log.d("nengkene_id_pilihan",idPilihan);
//            showProgress();
//            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//            final String url = AlamatServer.getAlamatServer()+AlamatServer.getAddUserVote();
//
//            StringRequest login = new StringRequest(Request.Method.POST, url , new Response.Listener<String>(){
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.toString());
//                        Log.d("negkene",jsonObject.toString());
//                        if(jsonObject.get("status").equals("success")){
//                            Toast.makeText(VoteActivity.this, "Anda berhasil melakukan Vote", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(VoteActivity.this, "Anda gagal melakukan Vote", Toast.LENGTH_SHORT).show();
//                        }
//                        progressDialog.dismiss();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Log.d("error", e.toString());
//                    }
//                    pilihanAdapter.notifyDataSetChanged();
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> param = new HashMap<String, String>();
//                    param.put("id_user",id_user);
//                    param.put("vote_id",voteId);
//                    param.put("pilihan",idPilihan);
//                    return param;
//                }
//            };
//            requestQueue.add(login);
//        }
//    }

    public void addUserVote(final String id_user, final String voteId, final String idPilihan){
        Log.d("nengkene_vote_id",voteId);
        Log.d("nengkene_id_user",id_user);
        Log.d("nengkene_id_pilihan",idPilihan);
        showProgress();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url = AlamatServer.getAlamatServer()+AlamatServer.getAddUserVote();

        StringRequest login = new StringRequest(Request.Method.POST, url , new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Log.d("negkene",jsonObject.toString());
                    if(jsonObject.get("status").equals("success")){
                        Toast.makeText(VoteActivity.this, "Anda berhasil melakukan Vote", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(VoteActivity.this, "Anda gagal melakukan Vote", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("error", e.toString());
                }
                pilihanAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("id_user",id_user);
                param.put("vote_id",voteId);
                param.put("pilihan",idPilihan);
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
        messageToSend.setGroupId(Integer.parseInt(getIntent().getStringExtra("id_group")));
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

