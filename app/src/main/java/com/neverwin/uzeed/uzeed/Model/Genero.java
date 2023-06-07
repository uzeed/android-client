package com.neverwin.uzeed.uzeed.Model;

import org.json.JSONObject;

import java.io.Serializable;

public class Genero implements Serializable {
    private int id;
    private String descripcion;
    private int eliminado;

    public Genero(){}

    public Genero(JSONObject genero)
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

}
