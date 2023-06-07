package com.neverwin.uzeed.uzeed.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

public class Imagen {
    private int id_profesional;
    private int id;
    private String imagen64;
    private String page;
    private boolean eliminada;
    private boolean actualizada;
    public Imagen(int id_profesional, int id, String imagen64) {
        this.id_profesional = id_profesional;
        this.id = id;
        this.imagen64 = imagen64;
        this.eliminada = false;
        this.actualizada = false;
    }

    public Imagen(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) {
            return;
        }

        id = jsonObject.getInt("id");
        id_profesional = jsonObject.getInt("id_profesional");
        imagen64 = jsonObject.getString("imagen64");
        eliminada = false;
        actualizada = false;
    }

    public boolean isActualizada() {
        return actualizada;
    }

    public void setActualizada(boolean actualizada) {
        this.actualizada = actualizada;
    }

    public boolean isEliminada() {
        return eliminada;
    }

    public void setEliminada(boolean eliminada) {
        this.eliminada = eliminada;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getId_profesional() {
        return id_profesional;
    }

    public void setId_profesional(int id_profesional) {
        this.id_profesional = id_profesional;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagen64() {
        return imagen64;
    }

    public void setImagen64(String imagen64) {
        this.imagen64 = imagen64;
    }
}