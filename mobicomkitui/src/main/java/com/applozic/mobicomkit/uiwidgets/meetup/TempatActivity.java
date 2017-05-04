package com.applozic.mobicomkit.uiwidgets.meetup;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.applozic.mobicomkit.uiwidgets.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TempatActivity extends FragmentActivity implements OnInfoWindowClickListener,
        OnMapReadyCallback  {

    private GoogleMap mMap;
    private GPSTracker gps;
    private double latitude, longitude;
    private LatLng lokasi;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempat);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.setInfoWindowAdapter(new TagTempatInfoWindow(TempatActivity.this));
        mMap.setOnInfoWindowClickListener(this);
        viewLokasi();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        LatLng pointKirim = marker.getPosition();
        double l1 = pointKirim.latitude;
        double l2 = pointKirim.longitude;
        String message = Double.toString(l1)+","+Double.toString(l2);
        DialogInputTempat dialogInputTempat = new DialogInputTempat(TempatActivity.this, message);
        dialogInputTempat.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogInputTempat.show();
    }

    private void viewLokasi() {
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }

        LatLng point = new LatLng(latitude, longitude);
        final MarkerOptions markerStand = new MarkerOptions();
        markerStand.position(point);
        markerStand.title("Tetapkan lokasi");
        markerStand.icon(BitmapDescriptorFactory.fromResource(R.drawable.mu_kuning48));
        markerStand.draggable(true);
        mMap.addMarker(markerStand);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                markerStand.position(marker.getPosition());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),15));
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }
        });
    }

    @Override
    public void onBackPressed() {
        String message = "kosong";
        Intent intent = new Intent();
        intent.putExtra("MESSAGE",message);
        setResult(7,intent);
        finish();
    }
}
