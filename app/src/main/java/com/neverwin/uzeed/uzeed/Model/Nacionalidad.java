package com.neverwin.uzeed.uzeed.Model;

import org.json.JSONObject;

import java.io.Serializable;

public class Nacionalidad implements Serializable {
    private int id;
    private String descripcion;

    public Nacionalidad(){}

    public Nacionalidad(JSONObject genero)
    {
        if(genero == null)
        {
            return;
        }

        this.id = genero.optInt("id");
        this.descripcion = genero.optString("descripcion");
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
