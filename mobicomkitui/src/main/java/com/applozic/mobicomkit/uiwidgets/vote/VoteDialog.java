//package com.applozic.mobicomkit.uiwidgets.vote;
//
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.app.TimePickerDialog;
//import android.icu.text.SimpleDateFormat;
//import android.os.Bundle;
//import android.text.InputType;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import com.applozic.mobicomkit.uiwidgets.R;
//
//import java.util.Calendar;
//import java.util.Locale;
//
///**
// * Created by kaddafi on 08/03/2017.
// */
//
//public class VoteDialog extends Dialog implements View.OnClickListener {
//
//    private Activity activity;
//    private String id;
//    private TextView tvJudul, isiTanggal, isiWaktu, isiTempat, simpanBtn, batalBtn;
//    private Spinner spinPilJwb;
//
//    public VoteDialog(Activity activity) {
//        super(activity);
//        this.activity = activity;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.modal_vote);
//        tvJudul = (TextView) findViewById(R.id.tv_judul_piljwb);
//        isiTanggal = (TextView) findViewById(R.id.et_isiantanggal);
//        isiWaktu = (TextView)findViewById(R.id.et_isianwaktu);
//        isiTempat = (TextView) findViewById(R.id.et_isiantempat);
//
//        simpanBtn = (TextView)findViewById(R.id.btn_simpan_piljwb);
//        simpanBtn .setOnClickListener(this);
//
//        batalBtn = (TextView)findViewById(R.id.btn_batal_piljwb);
//        batalBtn.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View view) {
//        if(view==simpanBtn){
//            Toast.makeText(activity.getApplicationContext(),"Jos",Toast.LENGTH_SHORT).show();
//            dismiss();
//        }else if(view==batalBtn){
//            dismiss();
//        }
//    }
//}
