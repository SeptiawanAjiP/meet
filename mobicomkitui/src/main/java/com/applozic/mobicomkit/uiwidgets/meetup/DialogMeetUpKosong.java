package com.applozic.mobicomkit.uiwidgets.meetup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applozic.mobicomkit.uiwidgets.R;

/**
 * Created by kaddafi on 08/03/2017.
 */

public class DialogMeetUpKosong extends Dialog implements View.OnClickListener {

    private Activity activity;
    private String id;
    private TextView tvJudul, isiTanggal, isiWaktu, isiTempat, tutupBtn, buatBtn;
    private Spinner spinPilJwb;
    private boolean admin;
    private String org1,org2,idGroup;

    public DialogMeetUpKosong(Activity activity,String org1,String org2,String idGroup,boolean admin) {
        super(activity);
        this.activity = activity;
        this.org1 = org1;
        this.org2 = org2;
        this.idGroup = idGroup;
        this.admin = admin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modal_dialog_meetup_kosong);

        tutupBtn = (TextView) findViewById(R.id.btn_tutup);
        tutupBtn.setOnClickListener(this);

        buatBtn = (TextView)findViewById(R.id.btn_buat_meetup);
        if (admin){
            buatBtn.setVisibility(View.VISIBLE);
        }else {
            buatBtn.setVisibility(View.GONE);
        }
        buatBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==buatBtn){
            Intent intent = new Intent(activity,CreateMeetupActivity.class);
            intent.putExtra(CreateMeetupActivity.ORANG_1,org1);
            intent.putExtra(CreateMeetupActivity.ORANG_2,org2);
            intent.putExtra(CreateMeetupActivity.ID_GROUP,idGroup);
            activity.startActivity(intent);
            dismiss();
        }else if(view==tutupBtn){
            dismiss();
        }
    }
}
