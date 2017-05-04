package com.applozic.mobicomkit.uiwidgets.meetup;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.LinearLayout;
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
import com.applozic.mobicomkit.uiwidgets.vote.VoteActivity;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Septiawan Aji Pradan on 3/11/2017.
 */

public class TrackingActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker gps;
    private double latitude, longitude;
    private LatLng lokasi;
    private ArrayList<Posisi> arrayPosisi;
    String message;
    private Posisi tempat;

    private ApplozicPermissions applozicPermission;

    public LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempat);
        arrayPosisi = new ArrayList<>();
        tempat = new Posisi();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        monitoring();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new TagTempatInfoWindow(TrackingActivity.this));
        mMap.setOnInfoWindowClickListener(this);

        viewLokasi();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    public void monitoring(){
        final Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                if (Utils.hasMarshmallow()) {
//                    new ApplozicPermissions(this, layout).checkRuntimePermissionForLocation();
//                } else {
//                    updateLocation();
//                }

                // do your stuff here, called every second

                updateLocation(getIntent().getStringExtra("id_user"),getIntent().getStringExtra("id_meetup"));

                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.post(runnable);
    }

    private void viewLokasi() {

        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }


        final Handler handler = new Handler();
        final MarkerOptions markerStand = new MarkerOptions();
        final MarkerOptions tempat = new MarkerOptions();
        LatLng point2 = new LatLng(getIntent().getDoubleExtra("latitude",0),getIntent().getDoubleExtra("longitude",0));
        tempat.position(point2);
        tempat.icon(BitmapDescriptorFactory.fromResource(R.drawable.mu_kuning48));
        tempat.title("Tempat Meetup");
        mMap.addMarker(tempat);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point2, 15));
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

//                markerStand.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_contact));



        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mMap.clear();

                LatLng point2 = new LatLng(getIntent().getDoubleExtra("latitude",0),getIntent().getDoubleExtra("longitude",0));
                tempat.position(point2);
                tempat.title("Tempat Meetup");
                tempat.icon(BitmapDescriptorFactory.fromResource(R.drawable.mu_kuning48));

                mMap.addMarker(tempat);

                for(int i=0;i<arrayPosisi.size();i++){

                    LatLng point1 = new LatLng(arrayPosisi.get(i).getLatitude(), arrayPosisi.get(i).getLongitude());
                    markerStand.position(point1);
                    markerStand.title(arrayPosisi.get(i).getIdUser());
                    markerStand.icon(BitmapDescriptorFactory.fromResource(R.drawable.person_loc));

                    mMap.addMarker(markerStand);
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onBackPressed() {
        String message = "kosong";
        Intent intent = new Intent();
        intent.putExtra("MESSAGE",message);
        setResult(7,intent);
        finish();
    }


    public void updateLocation(final String id_user, final String id_meetup){
//        showProgress();
        final GPSTracker gps = new GPSTracker(this);
        gps.getLatitude();
        gps.getLongitude();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = AlamatServer.getAlamatServer()+AlamatServer.getUpdateLocation();
        Log.d("ainun_url",url);
        Log.d("ainun_lat",Double.toString(gps.getLatitude()));
        Log.d("ainun_long",Double.toString(gps.getLongitude()));
        Log.d("ainun_id_user",id_user);
        Log.d("ainun_id_meet_up",id_meetup);

        StringRequest pp = new StringRequest(Request.Method.POST, url , new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("ainun_kabeh",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    ArrayList<Posisi> cs = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("location");
                    tempat.setLatitude(jsonObject.getDouble("meetup_latitude"));
                    tempat.setLongitude(jsonObject.getDouble("meetup_longitude"));
                    for(int i =0;i<jsonArray.length();i++){
                        Posisi posisi = new Posisi();
                        JSONObject js = jsonArray.getJSONObject(i);
                        posisi.setIdUser(js.getString("user_id"));
                        posisi.setLatitude(js.getDouble("latitude"));
                        posisi.setLongitude(js.getDouble("longitude"));
                        cs.add(posisi);
                    }
                    arrayPosisi = cs;
//                    progressDialog.dismiss();
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
                param.put("id_user",id_user);
                param.put("id_meetup",id_meetup);
                param.put("latitude",Double.toString(gps.getLatitude()));
                param.put("longitude",Double.toString(gps.getLongitude()));
                Log.d("ainun_parameter",param.toString());
                return param;
            }
        };
        requestQueue.add(pp);
    }
}

