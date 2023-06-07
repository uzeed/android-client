package com.neverwin.uzeed.uzeed.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private String username;
    private String password;
    private int eliminado;
    private String nombre;
    private String apellido;
    private String perfil;
    private String email;
    private String passwordTemporal;
    private String token;
    private String code;
    private String idfirebase;
    private String passfirebase;

    public Usuario(JSONObject usuario) throws JSONException {

        nombre = usuario.optString("nombre");
        apellido = usuario.optString("apellido");
        id = usuario.getInt("id");
        email = usuario.getString("email");
        username = usuario.optString("username");
        idfirebase = usuario.optString("idfirebase");
        passfirebase =  usuario.optString("passfirebase");
    }

    public Usuario(JSONObject usuario,String username,String password,String code) {
        this.username = username;
        this.password = password;
        this.code = code;
        if (usuario == null) {
            return;
        }
        id = usuario.optInt("id");
        token = usuario.optString("token");
        nombre = usuario.optString("nombre");
        apellido = usuario.optString("apellido");
        passfirebase =  usuario.optString("passfirebase");
    }

    public Usuario(JSONObject usuario,String username,String password) {
        this.username = username;
        this.password = password;
        if(usuario == null)
        {
            return;
        }
        id                = usuario.optInt("id");
        token             = usuario.optString("token");
        nombre            = usuario.optString("nombre");
        apellido          = usuario.optString("apellido");
        idfirebase = usuario.optString("idfirebase");
        passfirebase =  usuario.optString("passfirebase");
    }

    public Usuario() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEliminado() {
        return eliminado;
    }

    public void setEliminado(int eliminado) {
        this.eliminado = eliminado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordTemporal() {
        return passwordTemporal;
    }

    public void setPasswordTemporal(String passwordTemporal) {
        this.passwordTemporal = passwordTemporal;
    }

    public String getIdfirebase() {
        return idfirebase;
    }

    public void setIdfirebase(String idfirebase) {
        this.idfirebase = idfirebase;
    }

    public String getPassfirebase() {
        return passfirebase;
    }

    public void setPassfirebase(String passfirebase) {
        this.passfirebase = passfirebase;
    }
}
