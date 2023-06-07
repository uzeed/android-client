package com.neverwin.uzeed.uzeed.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.neverwin.uzeed.uzeed.Model.Establecimiento;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.LoadImage;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CalificarEstablecimientoActivity extends AppCompatActivity {
    private TextView nombre, categoria, ciudad, verificado;
    private EditText comentario;
    private RatingBar calificacion;
    private Establecimiento establecimiento;
    private Button btnCalificar;
    private ProgressBar progressbar;
    private ImageView perfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar_establecimiento);
        final Context mContent = this;

        nombre = (TextView)findViewById(R.id.txtNombreEstablecimiento);
        categoria=(TextView)findViewById(R.id.txtCategoriaEstablecimiento);
        ciudad =(TextView)findViewById(R.id.txtCiudadEstablecimiento);
        verificado=(TextView)findViewById(R.id.txtVerificadoEstablecimiento);
        calificacion=(RatingBar)findViewById(R.id.ratb_CalificacionEstablecimiento);
        comentario=(EditText)findViewById(R.id.edt_comentario_calificacion);
        progressbar=(ProgressBar)findViewById(R.id.progressbar_CalificarEstablecimiento);
        btnCalificar=(Button)findViewById(R.id.btn_Calificar_Establecimiento);
        perfil=(ImageView)findViewById(R.id.profile_establecimiento);

        Bundle objEnviado = getIntent().getExtras();
        if(objEnviado!= null) {
            progressbar.setVisibility(View.VISIBLE);
            establecimiento = (Establecimiento) objEnviado.getSerializable("establecimiento");
            String titulo = establecimiento.getNombre();
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

            LoadImage loadImage = new LoadImage(perfil,false);
            loadImage.execute(establecimiento.getImagenPerfilPath());
            //byte[] decodeString = Base64.decode(establecimiento.getImagenPerfil(),Base64.DEFAULT);
            //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
            //perfil.setImageBitmap(decodedByte);
            perfil.setScaleType(ImageView.ScaleType.FIT_CENTER);

            progressbar.setVisibility(View.GONE);
        }

        btnCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (String.valueOf(comentario.getText()).equalsIgnoreCase(""))
                {
                    Utils.showMessage(findViewById(android.R.id.content),getResources().getString(R.string.ingrese_comentario));
                    return;
                }
                final String urlCalificar = Utils.getUrl() +  "establecimiento/Calificar?id=" + establecimiento.getId() +
                        "&calificacion="+ (int)calificacion.getRating()+
                        "&comentario=" + String.valueOf(comentario.getText());

                progressbar.setVisibility(View.VISIBLE);
                JsonObjectRequest jsonArrayRequestProfesional = new JsonObjectRequest
                        (Request.Method.POST, urlCalificar, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                progressbar.setVisibility(View.GONE);
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressbar.setVisibility(View.GONE);
                                Utils.showMessage(findViewById(android.R.id.content),getResources().getString(R.string.calificacion_error));
                            }
                        }) {
                    @Override
                    public Map getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("Authorization", "Bearer " + Utils.getUsuario().getToken());
                        return headers;
                    }
                };
                SingletonRequestQueue.getInstance(mContent).addToRequestQueue(jsonArrayRequestProfesional);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
