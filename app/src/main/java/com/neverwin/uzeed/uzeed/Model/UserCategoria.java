package com.neverwin.uzeed.uzeed.Model;

import org.json.JSONObject;

import java.io.Serializable;

public class UserCategoria  implements Serializable {
    /*  Attributes*/
    private int idUsuario;
    private int idCategoria;
    private String descripcionCategoria;

    /*  Constructors*/
    public UserCategoria(){}

    public UserCategoria(JSONObject categoria)
    {
        if(categoria == null)
        {
            return;
        }

        idUsuario = categoria.optInt("idUsuario");
        idCategoria = categoria.optInt("idCategoria");
    }

    /*  Propertys*/

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }
}
