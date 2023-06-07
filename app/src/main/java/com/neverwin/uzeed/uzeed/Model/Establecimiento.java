package com.neverwin.uzeed.uzeed.Model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Establecimiento implements Serializable {

    private int id;
    private String nombre;
    private String alias;
    private String direccion;
    private String telefono;
    private String imagenPerfil;
    private boolean isVerificado;
    private Double latitud;
    private Double longitud;
    private List<Categoria> categorias;
    private Ciudad ciudad;
    private String descripcion;
    private int calificacion;
    private String imagenPerfilPath;

    public Establecimiento(){}

    public Establecimiento( JSONObject jsonEstablecimiento) throws JSONException {
        if (jsonEstablecimiento == null) {
            return;
        }
        this.id = jsonEstablecimiento.optInt("id");
        this.nombre = jsonEstablecimiento.optString("nombre");
        this.alias = jsonEstablecimiento.optString("alias");
        this.direccion = jsonEstablecimiento.optString("direccion");
        this.telefono=jsonEstablecimiento.optString("telefono");
        this.imagenPerfil=jsonEstablecimiento.optString("imgPerfil");
        this.isVerificado=jsonEstablecimiento.optBoolean("isVerificado");
        this.latitud=jsonEstablecimiento.optDouble("latitud");
        this.longitud=jsonEstablecimiento.getDouble("longitud");
        JSONObject JsonCiudad = jsonEstablecimiento.optJSONObject("ciudad");
        this.ciudad = new Ciudad(JsonCiudad);
        this.descripcion=jsonEstablecimiento.optString("descripcion");
        this.calificacion=jsonEstablecimiento.optInt("calificacion");
        this.imagenPerfilPath = jsonEstablecimiento.optString("imgPerfilPath");
        this.categorias=new ArrayList<Categoria>();
        JSONArray jArray = jsonEstablecimiento.optJSONArray("categorias");
        for (int i=0;i < jArray.length();i++) {
            Categoria item = new Categoria(jArray.optJSONObject(i));
            this.categorias.add(item);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public boolean isVerificado() {
        return isVerificado;
    }

    public void setVerificado(boolean verificado) {
        isVerificado = verificado;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public String ObtenerCategorias() {
        String stringCategorias = "";
        for(int i =0 ; i< this.categorias.size();i++)
        {
            if( i!=0) {
                stringCategorias = stringCategorias.concat("-");
            }
            stringCategorias = stringCategorias.concat(categorias.get(i).getDescripcion());
        }
        return stringCategorias;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getImagenPerfilPath() {
        return imagenPerfilPath;
    }

    public void setImagenPerfilPath(String imagenPerfilPath) {
        this.imagenPerfilPath = imagenPerfilPath;
    }
}
