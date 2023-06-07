package com.neverwin.uzeed.uzeed.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.neverwin.uzeed.uzeed.Model.Establecimiento;
import com.neverwin.uzeed.uzeed.Model.Imagen;
import com.neverwin.uzeed.uzeed.Model.ImagenEstablecimiento;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class EstablecimientoActivity extends AppCompatActivity {

    private Button btnCalificarEstablecimiento;
    private CarouselView carouselView;
    private TextView nombre,categoria,ciudad,verificado,direccion,telefono, descripcion;
    private RatingBar rbCalificacion;
    private ProgressBar progressbar;

    int[] sampleImages = { R.drawable.portada, R.drawable.estab1, R.drawable.estab2};
    private Establecimiento establecimiento;
    private ArrayList<ImagenEstablecimiento> imagenes;
    private ImageButton ibtnArrowBAck;
    private TextView tvNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establecimiento);
        nombre=(TextView)findViewById(R.id.txtNombreEstablecimiento);
        categoria =(TextView)findViewById(R.id.txtCategoriaEstablecimiento);
        ciudad=(TextView)findViewById(R.id.txtCiudadEstablecimiento);
        verificado=(TextView)findViewById(R.id.txtVerificadoEstablecimiento);
        direccion=(TextView)findViewById(R.id.txt_DireccionEstablecimiento);
        telefono=(TextView)findViewById(R.id.txt_TelefonoEstablecimiento);
        descripcion=(TextView)findViewById(R.id.txtDetalleEstablecimiento);
        rbCalificacion=(RatingBar) findViewById(R.id.ratb_CalificacionEstablecimiento);

        descripcion.setMovementMethod(new ScrollingMovementMethod());

        progressbar = (ProgressBar)findViewById(R.id.progressbar_PerfilEstablecimiento);
        Bundle objEnviado = getIntent().getExtras();
        if(objEnviado!= null) {
            establecimiento = (Establecimiento) objEnviado.getSerializable("establecimiento");
            String titulo = establecimiento.getNombre();

            tvNombre = (TextView) findViewById(R.id.tv_nombre);
            tvNombre.setText(titulo);
            setTitle(titulo);
            nombre.setText(establecimiento.getNombre());
            categoria.setText(establecimiento.ObtenerCategorias());
            ciudad.setText(establecimiento.getCiudad().getDescripcion());
            if(establecimiento.isVerificado()) {
                verificado.setVisibility(View.VISIBLE);
                verificado.setTextColor(getResources().getColor(R.color.online));
            }
            else {
                verificado.setVisibility(View.GONE);
                verificado.setTextColor(Color.RED);
            }

            direccion.setText(establecimiento.getDireccion());
            telefono.setText(establecimiento.getTelefono());
            descripcion.setText(establecimiento.getDescripcion());
            rbCalificacion.setRating(establecimiento.getCalificacion());

            ibtnArrowBAck = (ImageButton) findViewById(R.id.ibtn_arrow_back);

            ibtnArrowBAck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            CargarImagenes(establecimiento.getId());
        }


        carouselView = findViewById((R.id.cv_PhotosEstablecimiento));
        carouselView.setImageListener(imageListener);
     //   ImageListener listener=new ImageListener() {
     //       @Override
     //       public void setImageForPosition(int position, ImageView imageView) {
     //           imageView.setImageResource(sampleImages[position]);
     //       }
     //   };
     //   carouselView.setImageListener(listener);
     //   carouselView.setPageCount(sampleImages.length);

        btnCalificarEstablecimiento = (Button)findViewById(R.id.btn_CalificarEstablecimiento);
        btnCalificarEstablecimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(EstablecimientoActivity.this, CalificarEstablecimientoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("establecimiento", establecimiento);
                intent.putExtras(bundle);
                btnCalificarEstablecimiento.getContext().startActivity(intent);
            }
        });
    }

    private void CargarImagenes(int id) {
        progressbar.setVisibility(View.VISIBLE);
        final String  url = Utils.getUrl() + "imagenestablecimiento/byIdEstablecimiento?id_establecimiento=" + id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            LoadImagenes(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                        progressbar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbar.setVisibility(View.GONE);
                        Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            byte[] decodeString = Base64.decode(imagenes.get(position).getImagen64(),Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
            imageView.setImageBitmap(decodedByte);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    };

    private void LoadImagenes(JSONArray jsonImagenes) throws JSONException {
        imagenes = new ArrayList<ImagenEstablecimiento>();
        for (int i=0;i < jsonImagenes.length();i++) {
            ImagenEstablecimiento imagen = new ImagenEstablecimiento(jsonImagenes.optJSONObject(i));
            imagenes.add(imagen);
        }
        carouselView.setPageCount(imagenes.size());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
