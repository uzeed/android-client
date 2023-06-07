package com.neverwin.uzeed.uzeed.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class ImagenEstablecimiento {

    private Establecimiento establecimiento;
    private int id;
    private String imagen64;
    private String page;
    private boolean eliminada;
    private boolean actualizada;
    public ImagenEstablecimiento(Establecimiento establecimiento, int id, String imagen64) {

        this.id = id;
        this.imagen64 = imagen64;
        this.eliminada = false;
        this.actualizada = false;
    }

    public ImagenEstablecimiento(JSONObject jsonObject) throws JSONException {
        if (jsonObject == null) {
            return;
        }

        id = jsonObject.getInt("id");
        JSONObject JsonEstablecimiento = jsonObject.optJSONObject("establecimiento");
        this.establecimiento = new Establecimiento(JsonEstablecimiento);
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

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }
}
