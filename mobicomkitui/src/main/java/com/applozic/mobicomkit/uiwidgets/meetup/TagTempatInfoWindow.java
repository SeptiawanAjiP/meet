package com.applozic.mobicomkit.uiwidgets.meetup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.applozic.mobicomkit.uiwidgets.R;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by kaddafi on 09/03/2017.
 */

public class TagTempatInfoWindow implements InfoWindowAdapter {

    private final View myContentsView;

    public TagTempatInfoWindow(Activity activity) {
        myContentsView = activity.getLayoutInflater().inflate(R.layout.custom_info_contents, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title_info));
        tvTitle.setText(marker.getTitle());
        return myContentsView;
    }
}