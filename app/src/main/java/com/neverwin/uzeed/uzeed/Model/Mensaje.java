package com.neverwin.uzeed.uzeed.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Mensaje {
    private int id;
    private String mensaje;
    private String alias;
    private String hora;
    private int tipoMensaje;

    public Mensaje(int id, String mensaje, String hora, int tipoMensaje) {
        this.id = id;
        this.mensaje = mensaje;
        this.hora = hora;
        this.tipoMensaje=tipoMensaje;
    }

    public Mensaje() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(int tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }
}
