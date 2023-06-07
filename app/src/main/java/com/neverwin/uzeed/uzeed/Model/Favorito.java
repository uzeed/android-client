package com.neverwin.uzeed.uzeed.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Favorito implements Serializable {

    private JSONObject Jsonprofesional;
    /*  Attributes*/
    private Profesional profesional;

    /*  Getter and Setter*/

    public Profesional getProfesional() {
        return profesional;
    }

    public void setProfesional(Profesional profesional) {
        this.profesional = profesional;
    }

    /*  Constructors*/
    public Favorito() {
    }

    public Favorito(JSONObject favorito) throws JSONException {
        if (favorito == null) {
            return;
        }
        this.profesional = new Profesional(favorito);

    }
}
