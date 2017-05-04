package com.applozic.mobicomkit.uiwidgets.meetup;

/**
 * Created by Septiawan Aji Pradan on 3/11/2017.
 */

public class Posisi {
    private String idUser;
    private String namaUser;
    private Double latitude;
    private Double longitude;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
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
}
