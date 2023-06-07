package com.neverwin.uzeed.uzeed.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neverwin.uzeed.uzeed.Model.Imagen;
import com.neverwin.uzeed.uzeed.Model.Profesional;
import com.neverwin.uzeed.uzeed.Model.Ubicacion;
import com.neverwin.uzeed.uzeed.Model.Usuario;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.LoadImage;
import com.neverwin.uzeed.uzeed.Utils.Utils;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener {
    TextView nombre, estado, informacion, categoria,txtNacionalidad, txtDepartamento;
    ImageView ivFavorito, perfilPhoto;
    private ProgressBar progressbar;
    Profesional profesional = null;
    ArrayList<Imagen> imagenes;
    int favorito = 0;
    int posicion;
    ImageButton imagenFavorito;
    Button verfotos;
    private TextView tvEstado;
    private TextView tvTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        progressbar = (ProgressBar) findViewById(R.id.progressbar_perfil);
        getSupportActionBar().hide();
        perfilPhoto=findViewById(R.id.perfil_photo);
        nombre = (TextView) findViewById(R.id.txt_Nombre);
        estado = (TextView) findViewById(R.id.txt_estado);
        informacion = (TextView) findViewById(R.id.txt_Informacion);
        categoria = (TextView) findViewById(R.id.txt_categoria);
        txtDepartamento=findViewById(R.id.tv_departamento);
        txtNacionalidad=findViewById(R.id.tv_nacionalidad);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratb_Calificacion);
        imagenFavorito = (ImageButton) findViewById(R.id.iv_favorite);
        final Button btnEnviarMEnsaje = (Button) findViewById(R.id.btn_Send_Message);
        verfotos=(Button)findViewById(R.id.btn_VerFotos);
        TextView ubicacion = findViewById(R.id.txtUbicacionperfil);
        tvEstado = (TextView) findViewById(R.id.txt_estado);
        tvTelefono = (TextView) findViewById(R.id.tv_telefono);

        informacion.setMovementMethod(new ScrollingMovementMethod());

        Bundle objetoEnviado = getIntent().getExtras();

        if (objetoEnviado != null) {
            profesional = (Profesional) objetoEnviado.getSerializable("profesional");
            //String titulo = "Información Profesional de " + profesional.getAlias();
            //setTitle(titulo);
            nombre.setText(profesional.getAlias());
            informacion.setText(profesional.getDescripcion());
            categoria.setText(profesional.ObtenerCategorias());
            txtNacionalidad.setText(profesional.getNacionalidad().getDescripcion());
            if(profesional.isTieneDepartamento()) {
                txtDepartamento.setText("Cuenta con departamento propio");
            }else {
                txtDepartamento.setText("No cuenta con departamento propio");
            }
            ratingBar.setRating(profesional.getCalificacion());
            ubicacion.setText(profesional.getCiudad().GetFullCity());
            //byte[] decodeString = Base64.decode(profesional.getImgPerfil(),Base64.DEFAULT);
            //Bitmap decodedByte =BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
            //perfilPhoto.setImageBitmap(decodedByte);

            LoadImage loadImage = new LoadImage(perfilPhoto,false);
            loadImage.execute(profesional.getImgPerfilPath());

            //perfilPhoto.setImageBitmap(profesional.getImagenProfesional());
            tvTelefono.setText(profesional.getNumeroTelefono());
            getEstadoProfesional(profesional.getUsuario());

        }

        final String url = Utils.getUrl() + "favorito/list?idUsuario="  + Utils.getUsuario().getId() +
                "&idProfesional=" + profesional.getId();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                            LoadFavorito(response);

                        Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));

                        //Utils.showMessage(view,"ERROR",175);
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " +  Utils.getUsuario().getToken());
                return headers;
            }
        };

        SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonArrayRequest);

        imagenFavorito.setOnClickListener(this);



        btnEnviarMEnsaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                Log.i("SeekActivity", "Presiono send message");
                intent = new Intent(PerfilActivity.this, MensajesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("profesional", profesional);
                intent.putExtras(bundle);
                btnEnviarMEnsaje.getContext().startActivity(intent);
            }
        });

        verfotos.setOnClickListener(new View.OnClickListener(){
           @Override
           public  void onClick(View view) {
               Intent intent = null;
               intent = new Intent(PerfilActivity.this, ProfileActivity.class);
               Bundle bundle = new Bundle();
               bundle.putSerializable("profesional", profesional);
               intent.putExtras(bundle);
               verfotos.getContext().startActivity(intent);
           }

        });
    }

    private void LoadFavorito(JSONArray response) {

        if (response.length()==0) {
            favorito = 0;
            imagenFavorito.setImageResource(R.drawable.heartnf);
        } else {
            favorito = 1;
            imagenFavorito.setImageResource(R.drawable.heartcf);
        }

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_favorite:
                final ImageView ivFavorite = (ImageView)findViewById(R.id.iv_favorite);
                if(favorito==1) {
                    ivFavorite.setImageResource(R.drawable.heartnf);
                    favorito=0;
                }
                else {
                    ivFavorite.setImageResource(R.drawable.heartcf);
                    favorito=1;
                }
                SendFavorito(favorito);
                break;
        }

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            byte[] decodeString = Base64.decode(imagenes.get(position).getImagen64(),Base64.DEFAULT);
            Bitmap decodedByte =BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
            imageView.setImageBitmap(decodedByte);

            //LoadImage loadImage = new LoadImage(imageView);
            //loadImage.execute(profesional.getImgPerfilPath());

            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void GetFavorito(){
        final String url = Utils.getUrl() + "favorito/list?idUsuario="  + Utils.getUsuario().getId() +
                "&idProfesional=" + profesional.getId();

        progressbar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.isNull(0))
                            favorito = 0;
                        else
                            favorito = 1;

                        Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                        progressbar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbar.setVisibility(View.GONE);
                        Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));

                        //Utils.showMessage(view,"ERROR",175);
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " +  Utils.getUsuario().getToken());
                return headers;
            }
        };

        SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }

    private void getEstadoProfesional(Usuario usuario) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Locations");
        DatabaseReference location = reference.child(usuario.getIdfirebase());
        location.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Ubicacion ubicacion = dataSnapshot.getValue(Ubicacion.class);
                if (ubicacion.getStatus() != null)
                {
                    tvEstado.setText(ubicacion.getStatus());
                    if (!ubicacion.getStatus().equals("online")) {
                        tvEstado.setTextColor(getResources().getColor(R.color.colorGrisChat));
                    }
                    else
                    {
                        tvEstado.setTextColor(getResources().getColor(R.color.online));
                    }

                }
                else
                {
                    tvEstado.setText("offline");
                    tvEstado.setTextColor(getResources().getColor(R.color.colorGrisChat));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void SendFavorito(int favorito) {
        if (favorito == 1) {
            final String urlPostFavorito = Utils.getUrl() + "favorito?idUsuario="  + Utils.getUsuario().getId() +
                    "&idProfesional=" + profesional.getId();

            progressbar.setVisibility(View.VISIBLE);
            JsonObjectRequest jsonArrayRequestProfesional = new JsonObjectRequest
                    (Request.Method.POST, urlPostFavorito, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            progressbar.setVisibility(View.GONE);
                            //Utils.showMessage(findViewById(android.R.id.content), getResources().getString(R.string.calificacion_exitosa));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.setVisibility(View.GONE);

                            //Utils.showMessage(findViewById(android.R.id.content), getResources().getString(R.string.calificacion_error));
                        }
                    }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Authorization", "Bearer " + Utils.getUsuario().getToken());
                    return headers;
                }
            };
            SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonArrayRequestProfesional);
        } else {
            final String urlDeleteFavorito = Utils.getUrl() + "favorito?idUsuario="  + Utils.getUsuario().getId() +
                    "&idProfesional=" + profesional.getId();

            progressbar.setVisibility(View.VISIBLE);
            JsonObjectRequest jsonArrayRequestProfesional = new JsonObjectRequest
                    (Request.Method.DELETE, urlDeleteFavorito, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            progressbar.setVisibility(View.GONE);
                            //Utils.showMessage(findViewById(android.R.id.content), getResources().getString(R.string.calificacion_exitosa));
                            //finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbar.setVisibility(View.GONE);
                            //Utils.showMessage(findViewById(android.R.id.content), getResources().getString(R.string.calificacion_error));
                        }
                    }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Authorization", "Bearer " + Utils.getUsuario().getToken());
                    return headers;
                }
            };
            SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonArrayRequestProfesional);
        }
    }
}

