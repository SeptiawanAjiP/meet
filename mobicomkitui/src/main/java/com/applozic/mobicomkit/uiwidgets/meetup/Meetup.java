package com.applozic.mobicomkit.uiwidgets.meetup;;

/**
 * Created by Septiawan Aji Pradan on 3/7/2017.
 */

public class Meetup {
    private String id;
    private String judul;
    private String tanggal;
    private String pukul;
    private String alamat;
    private Double latitude;
    private Double longitude;
    private int jumlahPeserta;
    private String orang1;
    private String orang2;
    private String idGroup;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getPukul() {
        return pukul;
    }

    public void setPukul(String pukul) {
        this.pukul = pukul;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getJumlahPeserta() {
        return jumlahPeserta;
    }

    public void setJumlahPeserta(int jumlahPeserta) {
        this.jumlahPeserta = jumlahPeserta;
    }

    public String getOrang1() {
        return orang1;
    }

    public void setOrang1(String orang1) {
        this.orang1 = orang1;
    }

    public String getOrang2() {
        return orang2;
    }

    public void setOrang2(String orang2) {
        this.orang2 = orang2;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public String getStatus() {
        return status;
    }

    public Meetup(String judul,String tanggal,String tempat,String status){
        this.judul = judul;
        this.tanggal = tanggal;
        this.alamat = tempat;
        this.status = status;
    }

    public Meetup(){

    }

    public void setStatus(String status) {
        this.status = status;
    }
}
