package com.applozic.mobicomkit.uiwidgets.vote;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.applozic.mobicomkit.uiwidgets.schedule.ScheduledTimeHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateVoteActivity extends AppCompatActivity {

    private EditText etJudulVote;
    private Spinner spinJmlPil;
    private List<Integer> intPil;
    private RecyclerView rvPilJwb;
    private RelativeLayout rlBuatVote;
    private JudulVoteAdapter adapterJudul;
    private List<Vote> listVote;
    public Vote newVote;
    public ArrayList<Pilihan> arrayPilihan;
    ActionBar actionBar;
    ProgressDialog progressDialog;
    HashMap<String,Pilihan> pilihanPilihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_vote);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Set up the action bar.


        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Buat Vote");
        etJudulVote = (EditText) findViewById(R.id.et_ip_judulvote);
        spinJmlPil = (Spinner) findViewById(R.id.spinner_jmlpil);
        rvPilJwb = (RecyclerView) findViewById(R.id.rv_piljwb);

        intPil = new ArrayList<>();
        newVote = new Vote();
        pilihanPilihan = new HashMap<>();

        for (int i = 2; i <= 5; i++) {
            intPil.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, intPil);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinJmlPil.setAdapter(dataAdapter);
        spinJmlPil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rvPilJwb.setVisibility(View.VISIBLE);
                listVote = new ArrayList<>();
                for (int i = 0; i <= position + 1; i++) {
                    Vote vote = new Vote();
                    vote.setId(Integer.toString(i + 1));
                    vote.setJudul("Pilihan " + Integer.toString(position + 1));
                    listVote.add(vote);
                }
                Log.d("DEBUGMU", "size vote a " + listVote.size());
                adapterJudul = new JudulVoteAdapter(listVote, CreateVoteActivity.this);
                adapterJudul.notifyDataSetChanged();
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rvPilJwb.setLayoutManager(mLayoutManager);
                rvPilJwb.setItemAnimator(new DefaultItemAnimator());
                rvPilJwb.setAdapter(adapterJudul);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rlBuatVote = (RelativeLayout) findViewById(R.id.rv_buat_vote);
        rlBuatVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("manusia_asal",Integer.toString(listVote.size()));
                if (pilihanPilihan.size() != listVote.size()) {
                    Toast.makeText(CreateVoteActivity.this, "Ada pilihan yang belum terisi", Toast.LENGTH_SHORT).show();
                } else {
                    arrayPilihan = new ArrayList<>();
                    newVote.setIdGroup(getIntent().getStringExtra("id_group"));
                    newVote.setJudul(etJudulVote.getText().toString());
                    Log.d("jalan","jalan"+pilihanPilihan.size());
                    for(int o=1;o<pilihanPilihan.size()+1;o++){
                        arrayPilihan.add(pilihanPilihan.get(Integer.toString(o)));
                        Log.d("manusia2",pilihanPilihan.get(Integer.toString(o)).getTempat());
                    }
                    newVote.setPilihan(arrayPilihan);
                    kirimIdDevice(newVote);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void kirimIdDevice(Vote vote) {
        showProgress();
        String pilihan="";

        ArrayList<ArrayList> dua = new ArrayList<>();
        RequestQueue request = Volley.newRequestQueue(getApplicationContext());
        final String requestBody;
        try {
            Log.d("SIZEV",""+vote.getPilihan().size());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_group", vote.getIdGroup());
            jsonObject.put("judul", vote.getJudul());
            for(int i=0;i<vote.getPilihan().size();i++){
                ArrayList<String> satu = new ArrayList<>();
                satu.add(vote.getPilihan().get(i).getTanggal());
                satu.add(vote.getPilihan().get(i).getWaktu());
                satu.add(vote.getPilihan().get(i).getTempat());
                dua.add(satu);
            }

            jsonObject.put("pilihan",dua.toString());
            requestBody = jsonObject.toString();
            String url = AlamatServer.getAlamatServer() + AlamatServer.getAddVoting();
            Log.d("jadi", requestBody);
            StringRequest login = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if(jsonObject.get("status").equals("success")){
                            sendMessage("Haii...Aku udah buat vote..Diisi Ya", null,null, null, Message.ContentType.DEFAULT.getValue());
                            Toast.makeText(getApplicationContext(), jsonObject.get("status").toString(), Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.get("status").toString(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
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
                    param.put("vote", requestBody);
                    return param;
                }
            };
            request.add(login);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class InputVoteDialog extends Dialog implements View.OnClickListener {

        private Activity activity;
        private String idIn;
        private TextView tvJudul, isiTanggal, isiWaktu, simpanBtn, batalBtn;
        private EditText isiTempat;

        private DatePickerDialog getTanggal;
        private TimePickerDialog getPukul;
        private SimpleDateFormat dateFormat;
        private ArrayList<Pilihan> arrayList;

        HashMap<String,Pilihan> pilihanYe;


        public InputVoteDialog(Activity activity, String idIn,HashMap<String,Pilihan> pilihanYe) {
            super(activity);
            this.activity = activity;
            this.idIn = idIn;
            this.pilihanYe = pilihanYe;
        }



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.modal_isian_vote);
            tvJudul = (TextView) findViewById(R.id.tv_judul_piljwb);
            tvJudul.setText("Pilihan " + idIn);

            isiTanggal = (TextView) findViewById(R.id.et_isiantanggal);
            isiTanggal.setOnClickListener(this);
            isiTanggal.requestFocus();

            isiWaktu = (TextView) findViewById(R.id.et_isianwaktu);
            isiWaktu.setOnClickListener(this);

            isiTempat = (EditText) findViewById(R.id.et_isiantempat);

            simpanBtn = (TextView) findViewById(R.id.btn_simpan_piljwb);
            simpanBtn.setOnClickListener(this);

            batalBtn = (TextView) findViewById(R.id.btn_batal_piljwb);
            batalBtn.setOnClickListener(this);

            dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

            if(pilihanYe.get(idIn)!=null){
                isiTanggal.setText(pilihanYe.get(idIn).getTanggal());
                isiWaktu.setText(pilihanYe.get(idIn).getWaktu());
                isiTempat.setText(pilihanYe.get(idIn).getTempat());
            }

            setDateTimeField();
            setClock();
        }

        private void setDateTimeField() {
            isiTanggal.setOnClickListener(this);

            final Calendar newCalendar = Calendar.getInstance();
            getTanggal = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(i, i1, i2);

                    if (newDate.getTime().before(newCalendar.getTime())) {
                        Toast.makeText(activity, "Tanggal sudah berlalu", Toast.LENGTH_SHORT).show();
                        isiTanggal.setHint("Tanggal Sudah Berlalu");
                    } else {
                        isiTanggal.setText(dateFormat.format(newDate.getTime()));
                    }
                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

            getTanggal.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        }

        private void setClock() {
            isiWaktu.setOnClickListener(this);

            Calendar calendar = Calendar.getInstance();

            getPukul = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    if(i<10){
                        if(i1<10){
                            isiWaktu.setText("0"+i+":0"+i1);
                        }else{
                           isiWaktu.setText("0"+i+":"+i1);
                        }
                    }else {
                        if(i1<10){
                            isiWaktu.setText(i+":0"+i1);
                        }else{
                            isiWaktu.setText(i+":"+i1);
                        }
                    }

                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        }

        @Override
        public void onClick(View view) {
            if (view == isiTanggal) {
                getTanggal.show();
            } else if (view == isiWaktu) {
                getPukul.show();
            } else if (view == simpanBtn) {
                if(!isiTanggal.getText().toString().isEmpty()){
                    if(!isiWaktu.getText().toString().isEmpty()){
                        if(!isiTempat.getText().toString().isEmpty()){
                            Pilihan pilihan = new Pilihan();
                            pilihan.setId(idIn);
                            pilihan.setWaktu(isiWaktu.getText().toString());
                            pilihan.setTanggal(isiTanggal.getText().toString());
                            pilihan.setTempat(isiTempat.getText().toString());
                            pilihanPilihan.put(pilihan.getId(),pilihan);
//                            arrayPilihan.add(pilihan);
                            Toast.makeText(activity, "Berhasil menyimpan pilihan "+idIn, Toast.LENGTH_SHORT).show();
                            dismiss();
                            Log.d("manusia",pilihanPilihan.get(idIn).getTanggal());
//                            Log.d("manusia",Integer.toString(arrayPilihan.size()));
//                            for(int i=0;i<arrayPilihan.size();i++){
//                                Log.d("manusia",arrayPilihan.get(i).getTanggal());
//                            }

                        }else{
                            Toast.makeText(activity, "Masukan Tempat", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(activity, "Masukan Waktu", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity, "Masukan Tanggal", Toast.LENGTH_SHORT).show();
                }


            } else if (view == batalBtn) {
                dismiss();
            }
        }
    }

    class JudulVoteAdapter extends RecyclerView.Adapter<JudulVoteAdapter.MyViewHolder> {

        private List<Vote> voteList;
        private Activity activity;
        private String id;

        public class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView tvJudul;

            public MyViewHolder(View view){
                super(view);
                tvJudul = (TextView)view.findViewById(R.id.tv_judul_piljwb);

            }
        }

        public JudulVoteAdapter(List<Vote> voteList, Activity activity) {
            this.voteList = voteList;
            this.activity = activity;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_title_piljwb, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            Vote vote = voteList.get(position);
            id = vote.getId();
            holder.tvJudul.setText("Pilihan "+id);
            holder.tvJudul.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateVoteActivity.InputVoteDialog inputVoteDialog = new CreateVoteActivity.InputVoteDialog(activity,voteList.get(position).getId(),pilihanPilihan);
                    inputVoteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Log.d("ID",""+voteList.get(position).getId());
                    inputVoteDialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            Log.d("DEBUGMU","size vote "+voteList.size());
            return voteList.size();
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
