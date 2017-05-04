package com.applozic.mobicomkit.uiwidgets.meetup;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.conncetion.AlamatServer;
import com.applozic.mobicomkit.uiwidgets.session.SessionManager;
import com.applozic.mobicomkit.uiwidgets.vote.Pilihan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by kaddafi on 28/02/2017.
 */

public class MeetUpFragment extends Fragment {

    private Menu menu;
    private ListMeetUpAdapter listMeetUpAdapter;
    private RecyclerView rvListMeetUp;
    private ProgressBar spinner;
    private SessionManager sm;
    private MeetupDatabase meetupDatabase;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_meetup, container, false);
        spinner = (ProgressBar) view.findViewById(R.id.pbar_circle);
        rvListMeetUp = (RecyclerView) view.findViewById(R.id.rv_list_meetUp);
        sm = new SessionManager(getContext());
        meetupDatabase = new MeetupDatabase(getActivity());
        setHasOptionsMenu(true);
        cekListMeetUp(sm.getUserId());
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        menu.removeItem(R.id.menu_tambah);
        menu.removeItem(R.id.menu_search);
        menu.removeItem(R.id.menu_logout);
        menu.removeItem(R.id.menu_profile);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void cekListMeetUp(final String idUser){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String url = AlamatServer.getAlamatServer()+AlamatServer.getListMeetUp();

        StringRequest request = new StringRequest(Request.Method.POST, url , new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if(jsonObject.get("status").equals("ada")){
                        ArrayList<Meetup> listMeetUp = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("meetup");
                        for(int i=0; i<jsonArray.length(); i++){
                            Meetup meetup = new Meetup();
                            JSONObject jo = jsonArray.getJSONObject(i);
                            meetup.setId(jo.getString("id"));
                            meetup.setJudul(jo.getString("judul"));
                            meetup.setAlamat(jo.getString("tempat"));
                            meetup.setTanggal(jo.getString("tanggal"));
                            meetup.setPukul(jo.getString("waktu"));
                            meetup.setIdGroup(jo.getString("group_id"));
                            meetup.setJumlahPeserta(jo.getInt("jumlah"));
                            meetupDatabase.insertMeetUp(meetup);
                            listMeetUp.add(meetup);
                        }
                        setLayout(listMeetUp);
                    }else{
                        setLayout(null);
                    }
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
                param.put("id_user",idUser);
                return param;
            }
        };
        requestQueue.add(request);
    }

    private void setLayout(ArrayList<Meetup> listMeetUp) {
        listMeetUpAdapter = new ListMeetUpAdapter(getActivity(), listMeetUp, sm.getUserId());
        Log.d("DEBUG","size "+listMeetUp.size());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvListMeetUp.setLayoutManager(layoutManager);
        rvListMeetUp.setAdapter(listMeetUpAdapter);
        rvListMeetUp.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
    }
}