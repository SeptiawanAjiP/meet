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
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import com.applozic.mobicomkit.uiwidgets.R;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Locale;
//
///**
// * Created by kaddafi on 08/03/2017.
// */
//
//public class InputVoteDialog extends Dialog implements View.OnClickListener {
//
//    private Activity activity;
//    private String id;
//    private TextView tvJudul, isiTanggal, isiWaktu, simpanBtn, batalBtn;
//    private EditText isiTempat;
//
//    private DatePickerDialog getTanggal;
//    private TimePickerDialog getPukul;
//    private SimpleDateFormat dateFormat;
//    private ArrayList<Pilihan> arrayList;
//
//
//    public InputVoteDialog(Activity activity, String id) {
//        super(activity);
//        this.activity = activity;
//        this.id = id;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.modal_isian_vote);
//        tvJudul = (TextView) findViewById(R.id.tv_judul_piljwb);
//        tvJudul.setText("Pilihan "+id);
//
//        isiTanggal = (TextView) findViewById(R.id.et_isiantanggal);
//        isiTanggal.setOnClickListener(this);
//        isiTanggal.requestFocus();
//
//        isiWaktu = (TextView)findViewById(R.id.et_isianwaktu);
//        isiWaktu.setOnClickListener(this);
//
//        isiTempat = (EditText)findViewById(R.id.et_isiantempat);
//
//        simpanBtn = (TextView)findViewById(R.id.btn_simpan_piljwb);
//        simpanBtn .setOnClickListener(this);
//
//        batalBtn = (TextView)findViewById(R.id.btn_batal_piljwb);
//        batalBtn.setOnClickListener(this);
//
//        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
//
//        setDateTimeField();
//        setClock();
//    }
//    private void setDateTimeField(){
//        isiTanggal.setOnClickListener(this);
//
//        final Calendar newCalendar = Calendar.getInstance();
//        getTanggal = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(i,i1,i2+1);
//
//                if(newDate.getTime().before(newCalendar.getTime())){
//                    Toast.makeText(activity, "Tanggal sudah berlalu", Toast.LENGTH_SHORT).show();
//                    isiTanggal.setHint("Tanggal Sudah Berlalu");
//                }else{
//                    isiTanggal.setText(dateFormat.format(newDate.getTime()));
//                }
//            }
//        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));
//
//        getTanggal.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//
//    }
//
//    private void setClock(){
//        isiWaktu.setOnClickListener(this);
//
//        Calendar calendar = Calendar.getInstance();
//
//        getPukul = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                if(i1<10){
//                    isiWaktu.setText(i+":0"+i1);
//                }if(i<10){
//                    isiWaktu.setText("0"+i+":"+i1);
//                }else{
//                    isiWaktu.setText(i+":"+i1);
//                }
//
//            }
//        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        if(view==isiTanggal){
//            getTanggal.show();
//        }else if(view==isiWaktu){
//            getPukul.show();
//        }else if(view==simpanBtn){
//            Pilihan pilihan = new Pilihan();
//            pilihan.setId(id);
//            pilihan.setWaktu(isiWaktu.getText().toString());
//            pilihan.setTanggal(isiTanggal.getText().toString());
//            pilihan.setTempat(isiTempat.getText().toString());
//
//        }else if(view==batalBtn){
//            dismiss();
//        }
//    }
//}
