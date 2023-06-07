package com.neverwin.uzeed.uzeed.Model;

import org.json.JSONObject;

import java.io.Serializable;

public class Categoria  implements Serializable {

    private int id;
    private String descripcion;
    private int tipo;

    /*  Constructors*/
    public Categoria(){}

    public Categoria(JSONObject categoria)
    {
        if(categoria == null)
        {
            return;
        }

        id = categoria.optInt("id");
        descripcion = categoria.optString("descripcion");
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String nombre) {
        this.descripcion = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
