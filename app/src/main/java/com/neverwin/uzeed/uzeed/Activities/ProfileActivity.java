package com.neverwin.uzeed.uzeed.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.neverwin.uzeed.uzeed.Adapters.ImageAdapter;
import com.neverwin.uzeed.uzeed.Model.Imagen;
import com.neverwin.uzeed.uzeed.Model.Profesional;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity  {

    CircleImageView ivProfile;
    Button btnEnviarMensaje;
    TextView txtCategoria, txtUsername;
    ImageView imgProfile1,imgProfile2,imgProfile3,imgProfile4,imgProfile5,imgProfile6,imgProfile7,imgProfile8,imgProfile9;
    ProgressBar progressbar;
    private Profesional profesional;
    private String[] stringImagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().hide();

        ivProfile=(CircleImageView)findViewById(R.id.ivProfile);
        btnEnviarMensaje=(Button)findViewById(R.id.btnProfileEnviar);
        txtCategoria=(TextView)findViewById(R.id.txtCategoriaProfile);
        txtUsername=(TextView)findViewById(R.id.txtUserProfile);
        imgProfile1=(ImageView)findViewById(R.id.imgProfile1);
        imgProfile2=(ImageView)findViewById(R.id.imgProfile2);
        imgProfile3=(ImageView)findViewById(R.id.imgProfile3);
        imgProfile4=(ImageView)findViewById(R.id.imgProfile4);
        imgProfile5=(ImageView)findViewById(R.id.imgProfile5);
        imgProfile6=(ImageView)findViewById(R.id.imgProfile6);
        imgProfile7=(ImageView)findViewById(R.id.imgProfile7);
        imgProfile8=(ImageView)findViewById(R.id.imgProfile8);
        imgProfile9=(ImageView)findViewById(R.id.imgProfile9);
        progressbar=(ProgressBar)findViewById(R.id.progressbar_profile);

        Bundle objetoEnviado = getIntent().getExtras();
        if (objetoEnviado != null) {
            profesional = (Profesional) objetoEnviado.getSerializable("profesional");
            String titulo = "Información de " + profesional.getAlias();
            setTitle(titulo);
            txtUsername.setText(profesional.getAlias());
            txtCategoria.setText(profesional.ObtenerCategorias());
            byte[] decodeString = Base64.decode(profesional.getImgPerfil(),Base64.DEFAULT);
            Bitmap decodedByte =BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
            ivProfile.setImageBitmap(decodedByte);
        }

        getImagenes();

        btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                Log.i("ProfilekActivity", "Presiono send message");
                intent = new Intent(ProfileActivity.this, MensajesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("profesional", profesional);
                intent.putExtras(bundle);
                btnEnviarMensaje.getContext().startActivity(intent);
            }
        });

        imgProfile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarDialogImagenes(0);
            }
        });
        imgProfile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarDialogImagenes(1);
            }
        });
        imgProfile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarDialogImagenes(2);
            }
        });
        imgProfile4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarDialogImagenes(3);
            }
        });
        imgProfile5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarDialogImagenes(4);
            }
        });
        imgProfile6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarDialogImagenes(5);
            }
        });
        imgProfile7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarDialogImagenes(6);
            }
        });
        imgProfile8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarDialogImagenes(7);
            }
        });
        imgProfile9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarDialogImagenes(8);
            }
        });
    }

    private void MostrarDialogImagenes(int i) {
        Dialog d = new Dialog(this);
        Window window = d.getWindow();
        d.closeOptionsMenu();
        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        d.setTitle("Imágenes");
        d.setContentView(R.layout.custom_dialog_pager);


        final ImageAdapter adapter = new ImageAdapter(this, stringImagenes);
        final ViewPager viewPager = (ViewPager)d. findViewById(R.id.myCustomPagerView);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(i);
        final CircleIndicator indicator = (CircleIndicator)d. findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        d.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void getImagenes() {
        progressbar.setVisibility(View.VISIBLE);
        final String  url = Utils.getUrl() + "Imagen/byIdProfesional?id_profesional=" + profesional.getId() ;
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

    private void LoadImagenes(JSONArray jsonImagenes) throws JSONException {
        ArrayList<Imagen> imagenes = new ArrayList<Imagen>();
        stringImagenes = new String[jsonImagenes.length()];
        imgProfile1.setVisibility(View.GONE);
        imgProfile2.setVisibility(View.GONE);
        imgProfile3.setVisibility(View.GONE);
        imgProfile4.setVisibility(View.GONE);
        imgProfile5.setVisibility(View.GONE);
        imgProfile6.setVisibility(View.GONE);
        imgProfile7.setVisibility(View.GONE);
        imgProfile8.setVisibility(View.GONE);
        imgProfile9.setVisibility(View.GONE);
        int cantidadImagenes;
        if (jsonImagenes.length() > 9) {
            cantidadImagenes = 9;
        } else {
            cantidadImagenes = jsonImagenes.length();
        }
        for (int i = 0; i < cantidadImagenes; i++) {
            Imagen imagen = new Imagen(jsonImagenes.optJSONObject(i));
            imagenes.add(imagen);
            stringImagenes[i] = (imagen.getImagen64());
            byte[] decodeString = Base64.decode(imagen.getImagen64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
            if (i == 0) {
                imgProfile1.setImageBitmap(decodedByte);
                imgProfile1.setVisibility(View.VISIBLE);
            }
            if (i == 1) {
                imgProfile2.setImageBitmap(decodedByte);
                imgProfile2.setVisibility(View.VISIBLE);
            }
            if (i == 2) {
                imgProfile3.setImageBitmap(decodedByte);
                imgProfile3.setVisibility(View.VISIBLE);
            }
            if (i == 3) {
                imgProfile4.setImageBitmap(decodedByte);
                imgProfile4.setVisibility(View.VISIBLE);
            }
            if (i == 4) {
                imgProfile5.setImageBitmap(decodedByte);
                imgProfile5.setVisibility(View.VISIBLE);
            }
            if (i == 5) {
                imgProfile6.setImageBitmap(decodedByte);
                imgProfile6.setVisibility(View.VISIBLE);
            }
            if (i == 6) {
                imgProfile7.setImageBitmap(decodedByte);
                imgProfile7.setVisibility(View.VISIBLE);
            }
            if (i == 7) {
                imgProfile8.setImageBitmap(decodedByte);
                imgProfile8.setVisibility(View.VISIBLE);
            }
            if (i == 8) {
                imgProfile9.setImageBitmap(decodedByte);
                imgProfile9.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
