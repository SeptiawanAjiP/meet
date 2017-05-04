package com.applozic.mobicomkit.uiwidgets.vote;

import java.util.ArrayList;

/**
 * Created by kaddafi on 07/03/2017.
 */

public class Vote {
    private String id;
    private String judul;
    private String tanggal;
    private String tempat;
    private String waktu;
    private String idGroup;

    private ArrayList<Pilihan> pilihan;

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Pilihan> getPilihan() {
        return pilihan;
    }

    public void setPilihan(ArrayList<Pilihan> pilihan) {
        this.pilihan = pilihan;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }
}
