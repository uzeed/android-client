package com.neverwin.uzeed.uzeed.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.neverwin.uzeed.uzeed.Adapters.ProfetionalCardAdapter;
import com.neverwin.uzeed.uzeed.Model.Ciudad;
import com.neverwin.uzeed.uzeed.Model.Genero;
import com.neverwin.uzeed.uzeed.Model.Profesional;
import com.neverwin.uzeed.uzeed.Model.UserCategoria;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.LoadImage;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class CityActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    private Spinner spinnerGeneros;
    private Spinner spinnerCiudades;
    private Genero mGenero;
    private Ciudad mCiudad;
    private Button btnSearchProfesional;
    private ProgressBar progressbarCity;
    private Switch swPremium, swGold, swSilver;
    private Toolbar myToolbar;
    private ProfetionalCardAdapter profetionalCardAdapter;
    private UserCategoria userCategoria;
    private int idUsuario,idGenero,idCiudad,idCategoria,premium,silver,gold;
    private ArrayList<String> descripcionGenero;
    private ArrayList<String> descripcionCiudad;
    private ArrayList<String> descripcionProfesional;
    private ArrayList<Genero> generos;
    private ArrayList<Ciudad> ciudades;
    private ArrayList<Profesional> profesionales;
    private ImageButton ibtnArrowBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context mContent = this;
        final String urlGenero = Utils.getUrl() + "genero/list";
        final String urlCiudad = Utils.getUrl() + "ciudad/list";
        premium=1;
        setContentView(R.layout.activity_city);

        progressbarCity = (ProgressBar) findViewById(R.id.progressbar_City);
        swPremium = (Switch)findViewById(R.id.schPremium) ;
        swGold = (Switch)findViewById(R.id.schGold) ;
        swSilver=(Switch)findViewById(R.id.schSilver) ;

        swPremium.setOnCheckedChangeListener(this);
        swGold.setOnCheckedChangeListener(this);
        swSilver.setOnCheckedChangeListener(this);

        Bundle objetoEnviado = getIntent().getExtras();
        if(objetoEnviado!= null) {
            userCategoria = (UserCategoria) objetoEnviado.getSerializable("userCategoria");
        }

        ibtnArrowBack = (ImageButton) findViewById(R.id.ibtn_arrow_back);
        ibtnArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressbarCity.setVisibility(View.VISIBLE);

        JsonArrayRequest jsonArrayRequestCiudad = new JsonArrayRequest
                (Request.Method.GET, urlCiudad, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                        try {
                            loadCiudad(response);
                            spinnerCiudades = (Spinner) findViewById(R.id.spnCity);
                            ArrayAdapter<String> spinnerCiudadesAdapter = new ArrayAdapter<String>(mContent,R.layout.spinner_item, descripcionCiudad);
                            //spinnerCiudadesAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
                            spinnerCiudades.setAdapter(spinnerCiudadesAdapter);
                            spinnerCiudades.setPadding(0,0,0,0);
                            spinnerCiudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    mCiudad = (Ciudad) ciudades.get(i);
                                    Log.i(getClass().getSimpleName(),"Ciudad seleccionada:" + mCiudad.getId());
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbarCity.setVisibility(View.GONE);
                        Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));

                        Utils.showMessage( findViewById(android.R.id.content),getResources().getString(R.string.ciudad_error));
                    }
                }) {
            /**
             * Passing some request headers
             */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " + Utils.getUsuario().getToken());
                return headers;
            }
        };

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, urlGenero, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                        try {
                            loadGenero(response);
                            spinnerGeneros = (Spinner) findViewById(R.id.spnGender);
                            ArrayAdapter<String> spinnerGenerosAdapter = new ArrayAdapter<String>(mContent,R.layout.spinner_item, descripcionGenero);
                            //spinnerGenerosAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
                            spinnerGeneros.setAdapter(spinnerGenerosAdapter);
                            spinnerGeneros.setPadding(0,0,0,0);
                            spinnerGeneros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    mGenero = (Genero) generos.get(i);
                                    Log.i(getClass().getSimpleName(),"Genero seleccionado:" + mGenero.getId());
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                            //getNacionalidades(view);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressBarProfile.setVisibility(View.GONE);
                        Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));

                        //Utils.showMessage(this,getResources().getString(R.string.genero_error),175);
                    }
                }) {
            /**
             * Passing some request headers
             */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " + Utils.getUsuario().getToken());
                return headers;
            }
        };

        progressbarCity.setVisibility(View.GONE);
        SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonArrayRequest);
        SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonArrayRequestCiudad);

        btnSearchProfesional = (Button) findViewById(R.id.btnSearchProfesional);
        btnSearchProfesional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idUsuario = userCategoria.getIdUsuario();
                idCategoria=userCategoria.getIdCategoria();
                idGenero=mGenero.getId();
                idCiudad=mCiudad.getId();

                //final String urlProfesional="Profesional??";
                final String urlProfesional = Utils.getUrl() + "Profesional/byFilter?idCiudad="+ idCiudad + "&idGenero="+ idGenero +
                                                                "&premium=" + premium + "&silver=" +silver + "&gold=" + gold +
                                                                "&idCategoria=" + idCategoria + "&idUsuario=" + idUsuario;

                clear();
                progressbarCity.setVisibility(View.VISIBLE);
                JsonArrayRequest jsonArrayRequestProfesional = new JsonArrayRequest
                        (Request.Method.GET, urlProfesional, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                                try {
                                    loadProfesional(response);
                                    progressbarCity.setVisibility(View.GONE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressbarCity.setVisibility(View.GONE);
                                Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));
                                //Utils.showMessage(findViewById(android.R.id.content),getResources().getString(R.string.genero_error));
                            }
                        }) {
                    /**
                     * Passing some request headers
                     */
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

    private void loadGenero(JSONArray jsonGeneros) throws JSONException {
        descripcionGenero = new ArrayList<String>();
        generos = new ArrayList<Genero>();
        for (int i=0;i < jsonGeneros.length();i++) {
            Genero genero = new Genero(jsonGeneros.optJSONObject(i));
            generos.add(genero);
            descripcionGenero.add(genero.getDescripcion());
        }
    }

    private void loadCiudad(JSONArray jsonCiudades) throws JSONException {
        descripcionCiudad = new ArrayList<String>();
        ciudades = new ArrayList<Ciudad>();
        for (int i=0;i < jsonCiudades.length();i++) {
            Ciudad ciudad = new Ciudad(jsonCiudades.optJSONObject(i));
            ciudades.add(ciudad);
            descripcionCiudad.add(ciudad.getDescripcion());
        }
    }

    private void loadProfesional(JSONArray jsonProfesionales) throws JSONException {
        descripcionProfesional = new ArrayList<String>();
        profesionales = new ArrayList<Profesional>();
        for (int i=0;i < jsonProfesionales.length();i++) {
            Profesional profesional = new Profesional(jsonProfesionales.optJSONObject(i));

            profesionales.add(profesional);
            descripcionProfesional.add(profesional.getAlias());
        }
        RecyclerView rv = findViewById(R.id.rv_List_item);
        profetionalCardAdapter = new ProfetionalCardAdapter(this, profesionales);
        rv.setAdapter(profetionalCardAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    public void clear() {
        try {
            final int size = profesionales.size();
            profesionales.clear();
            profetionalCardAdapter.notifyDataSetChanged();
        }catch (Exception e) {
            return;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (swPremium.isChecked()) {
            premium=1;
        } else {
            premium=0;
        }

        if (swGold.isChecked()) {
            gold=1;
        } else {
            gold=0;
        }

        if (swSilver.isChecked()) {
            silver=1;
        } else {
            silver=0;
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
