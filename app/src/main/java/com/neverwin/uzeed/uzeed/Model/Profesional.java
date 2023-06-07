package com.neverwin.uzeed.uzeed.Model;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.neverwin.uzeed.uzeed.Utils.LoadImage;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Profesional implements Serializable {

    /*  Attributes*/
    private int id;
    private String nombre;
    private String alias;
    private Double precio ;
    private int edad;
    private String fechaNacimiento;
    private String username;
    private Double altura;
    private Genero genero;
    private List<Categoria> categorias;
    private int calificacion;
    private String descripcion;
    private boolean favorito;
    private String imgPerfil;
    private String imgPortada;
    private Usuario usuario;
    private Ciudad ciudad;
    private Nacionalidad nacionalidad;
    private String numeroTelefono;
    private boolean tieneDepartamento;
    private String imgPerfilPath;
    private transient Bitmap imagenProfesional;
    /*  Constructors*/
    public Profesional(){}

    public Profesional( JSONObject profesional) throws JSONException {
        if (profesional == null) {
            return;
        }
        this.id = profesional.optInt("id");
        this.nombre = profesional.optString("nombre");
        this.alias = profesional.optString("alias");
        this.precio = profesional.optDouble("precio");
        this.edad = profesional.optInt("edad");
        this.fechaNacimiento = profesional.optString("fechaNacimiento");
        this.username = profesional.optString("username");
        this.altura = profesional.optDouble("altura");
        this.calificacion = profesional.optInt("calificacion");
        this.descripcion = profesional.optString("descripcion");
        this.categorias=new ArrayList<Categoria>();
        this.favorito = profesional.optBoolean("favorito");
        this.imgPortada = profesional.optString("imgPortada");
        this.imgPerfil=profesional.optString("imgPerfil");
        this.usuario=new Usuario(profesional.optJSONObject("usuario"));
        this.ciudad = new Ciudad(profesional.optJSONObject("ciudad"));
        this.numeroTelefono = profesional.optString("numeroTelefono");
        this.tieneDepartamento=profesional.optBoolean("departamento");
        this.nacionalidad= new Nacionalidad(profesional.optJSONObject("nacionalidad"));
        this.imgPerfilPath = profesional.optString("imgPerfilPath");

        JSONArray jArray = profesional.optJSONArray("categorias");
        for (int i=0;i < jArray.length();i++) {
            Categoria item = new Categoria(jArray.optJSONObject(i));
            this.categorias.add(item);
        }

    }

/*  Propertys*/

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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }


    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String ObtenerCategorias() {
        String stringCategorias = "";
        for(int i =0 ; i< this.categorias.size();i++)
        {

            if(i!=0) {
                stringCategorias = stringCategorias.concat("-");
            }
            stringCategorias = stringCategorias.concat(categorias.get(i).getDescripcion());
        }
        return stringCategorias;
    }
    public List<Categoria> getCategorias() {
        return categorias;
    }
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public String getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(String imgPerfil) {
        this.imgPerfil = imgPerfil;
    }

    public String getImgPortada() {
        return imgPortada;
    }

    public void setImgPortada(String imgPortada) {
        this.imgPortada = imgPortada;
    }

    public Bitmap ObtenerImagenPortada(){
        //byte[] decodeString = Base64.decode(this.imgPortada,Base64.DEFAULT);
        //return BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
        return imagenProfesional;
    }

    public Bitmap ObtenerImagenPerfil(){
        //byte[] decodeString = Base64.decode(this.imgPerfil,Base64.DEFAULT);
        //return BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
        return imagenProfesional;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public boolean isTieneDepartamento() {
        return tieneDepartamento;
    }

    public void setTieneDepartamento(boolean tieneDepartamento) {
        this.tieneDepartamento = tieneDepartamento;
    }

    public Nacionalidad getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getImgPerfilPath() {
        return imgPerfilPath;
    }

    public void setImgPerfilPath(String imgPerfilPath) {
        this.imgPerfilPath = imgPerfilPath;
    }

    public Bitmap getImagenProfesional() {
        return imagenProfesional;
    }

    public void setImagenProfesional(Bitmap imagenProfesional) {
        this.imagenProfesional = imagenProfesional;
    }
}
