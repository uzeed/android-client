package com.neverwin.uzeed.uzeed.Model;

import android.graphics.Bitmap;

public class Ubicacion {
    String email;
    long uid;
    Double lat,lng;
    private String imgPerfil;
    private Profesional profesional;
    private Establecimiento establecimiento;
    private String status;
    public Ubicacion() {
    }

    public Ubicacion(String email, long uid, Double lat, Double lng,String status) {
        this.email = email;
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(String imgPerfil) {
        this.imgPerfil = imgPerfil;
    }

    public Profesional getProfesional() {
        return profesional;
    }

    public void setProfesional(Profesional profesional) {
        this.profesional = profesional;
    }

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
