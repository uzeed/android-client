package com.neverwin.uzeed.uzeed.Model;

import org.json.JSONObject;

import java.io.Serializable;

public class Ciudad implements Serializable {
    private int id;
    private String descripcion;
    private String pais;
    private int eliminado;

    public Ciudad(){}

    public Ciudad(JSONObject ciudad)
    {
        if(ciudad == null)
        {
            return;
        }

        id = ciudad.optInt("id");
        descripcion = ciudad.optString("descripcion");
        pais = ciudad.optString("pais");
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEliminado(int eliminado) {
        this.eliminado = eliminado;
    }

    public int getId() {
        return id;
    }

    public int getEliminado() {
        return eliminado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String GetFullCity()
    {
        return this.descripcion.toUpperCase() + ", " + this.pais.toUpperCase();
    }
}
