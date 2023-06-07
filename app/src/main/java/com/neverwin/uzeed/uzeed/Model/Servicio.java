package com.neverwin.uzeed.uzeed.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Servicio implements Serializable {

    private JSONObject Jsonprofesional;
    /*  Attributes*/
    private int id;
    private Profesional profesional;
    private int calificacion;
    private String comentario;
    private Date fecha;
    private String estado;



    /*  Getter and Setter*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Profesional getProfesional() {
        return profesional;
    }

    public void setProfesional(Profesional profesional) {
        this.profesional = profesional;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /*  Constructors*/
    public Servicio(){}

    public Servicio(JSONObject servicio) throws JSONException, ParseException {
        if (servicio == null) {
            return;
        }
        this.id = servicio.optInt("Id");
        Jsonprofesional = servicio.optJSONObject("Profesional");
        this.profesional = new Profesional(Jsonprofesional);
        this.calificacion = servicio.optInt("Calificacion");
        this.comentario = servicio.optString("Comentario");
        this.estado = servicio.optString("Estado");
        this.fecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(servicio.optString("Fecha"));

    }

}
