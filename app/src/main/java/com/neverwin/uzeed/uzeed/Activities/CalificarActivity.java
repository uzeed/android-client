package com.neverwin.uzeed.uzeed.Activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neverwin.uzeed.uzeed.Model.Profesional;
import com.neverwin.uzeed.uzeed.Model.Servicio;
import com.neverwin.uzeed.uzeed.Model.Ubicacion;
import com.neverwin.uzeed.uzeed.Model.Usuario;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CalificarActivity extends AppCompatActivity {
    private ProgressBar progressbar;
    Profesional profesional = null;
    Servicio servicio = null;
    TextView nombre, categoria, califica, tvOnline;
    EditText comentario;
    ImageView ivPortada;
    private Button btnCalificar;
    private int idSolicitud;
    private RatingBar ratingBar;
    int calificacion;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar);
        progressbar = (ProgressBar)findViewById(R.id.progressbar_Calificar);
        getSupportActionBar().hide();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Context mContent = this;
        //setTitle("CALIFICACIÓN");

        nombre = (TextView) findViewById(R.id.txt_Calificar_Nombre);
        categoria = (TextView)findViewById(R.id.txt_Calificar_Categoria);
        ivPortada = (ImageView)findViewById(R.id.calificar_profile_photo);
        comentario=(EditText)findViewById(R.id.edt_comentario_calificacion);
        ratingBar=(RatingBar) findViewById(R.id.ratb_Calificar_Calificacion);
        califica= findViewById(R.id.txt_texto_calificacion);
        tvOnline=findViewById(R.id.txt_Calificar_online);

        Bundle objEnviado = getIntent().getExtras();

        if(objEnviado!= null){
            profesional = (Profesional)objEnviado.getSerializable("profesional");
            idSolicitud = (int) objEnviado.getSerializable("idSolicitud");
            nombre.setText(profesional.getAlias());
            categoria.setText(profesional.ObtenerCategorias());
            ivPortada.setImageBitmap(profesional.ObtenerImagenPortada());
            califica.setText("CALIFICA TU EXPERIENCIA CON "+ profesional.getAlias().toUpperCase());
            getEstadoProfesional(profesional.getUsuario());

        }
        btnCalificar=(Button)findViewById(R.id.btn_Calificar_Profesional);
        btnCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String urlCalificar = Utils.getUrl() +  "solicitud/CalificarSolicitud?id=" + idSolicitud +
                                                              "&calificacion="+ (int)ratingBar.getRating()+
                                                              "&comentario=" + String.valueOf(comentario.getText());

                progressbar.setVisibility(View.VISIBLE);
                JsonObjectRequest jsonArrayRequestProfesional = new JsonObjectRequest
                        (Request.Method.POST, urlCalificar, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                progressbar.setVisibility(View.GONE);
                                Utils.showMessage(findViewById(android.R.id.content), getResources().getString(R.string.calificacion_exitosa));
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

    private void getEstadoProfesional(Usuario usuario) {

        reference = FirebaseDatabase.getInstance().getReference("Locations");
        DatabaseReference location = reference.child(usuario.getIdfirebase());
        location.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Ubicacion ubicacion = dataSnapshot.getValue(Ubicacion.class);
                if (ubicacion.getStatus() != null)
                {
                    tvOnline.setText(ubicacion.getStatus());
                    if (ubicacion.getStatus().equals("online")) {
                        tvOnline.setTextColor(getResources().getColor(R.color.online));
                    }
                    else
                    {
                        tvOnline.setTextColor(getResources().getColor(R.color.colorGrisChat));
                    }
                }
                else
                {
                    tvOnline.setText("offline");
                    tvOnline.setTextColor(getResources().getColor(R.color.colorGrisChat));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
