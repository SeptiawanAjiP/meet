package com.applozic.mobicomkit.uiwidgets.meetup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.applozic.mobicomkit.uiwidgets.R;

/**
 * Created by kaddafi on 08/03/2017.
 */

public class DialogInputTempat extends Dialog implements View.OnClickListener {

    private Activity activity;
    private EditText namaTempat;
    private TextView tutupBtn, simpanBtn;
    private String messagepoint;
    private String message;

    public DialogInputTempat(Activity activity, String messagepoint) {
        super(activity);
        this.activity = activity;
        this.messagepoint = messagepoint;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modal_dialog_input_tempat);

        namaTempat = (EditText) findViewById(R.id.et_nama_tempat);
        tutupBtn = (TextView) findViewById(R.id.btn_tutup);
        tutupBtn.setOnClickListener(this);
        simpanBtn = (TextView)findViewById(R.id.btn_simpan);
        simpanBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==simpanBtn){
            if(namaTempat.getText().length()!=0){
                message = messagepoint + "," + namaTempat.getText();
                Intent intent = new Intent();
                intent.putExtra("MESSAGE",message);
                activity.setResult(7,intent);
                Log.d("DEBUGMU"," messagekirim " +message);
                activity.finish();
                dismiss();
            } else {
                Toast.makeText(activity.getApplicationContext(),"Tempat tidak boleh kosong",Toast.LENGTH_SHORT).show();
            }
        }else if(view==tutupBtn){
            dismiss();
        }
    }
}
