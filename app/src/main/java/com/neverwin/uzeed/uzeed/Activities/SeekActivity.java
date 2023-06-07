package com.neverwin.uzeed.uzeed.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neverwin.uzeed.uzeed.Fragments.MapsSearchFragment;
import com.neverwin.uzeed.uzeed.Model.Genero;
import com.neverwin.uzeed.uzeed.Model.Profesional;
import com.neverwin.uzeed.uzeed.Model.Ubicacion;
import com.neverwin.uzeed.uzeed.Model.UserCategoria;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SeekActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener, CompoundButton.OnCheckedChangeListener {
    private Button btnSearchCity;
    SeekBar seekBar;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RES_REQUEST = 7172;
    private static final int UPDATE_INTERVAL = 5000;
    private static final int FASTEST_INTERVAL = 3000;
    private  static final  int DISTANCE = 10;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private MapsSearchFragment mapsSearchFragment;
    private final ArrayList<Ubicacion> mLocations = new ArrayList<Ubicacion>();
    private Spinner spinnerGeneros;
    private Genero mGenero;
    private ArrayList<String> descripcionGenero;
    private ArrayList<Genero> generos;
    private Switch swPremium, swGold, swSilver;
    private ArrayList<String> descripcionProfesional;
    private ArrayList<Profesional> profesionales;
    private int idUsuario,idGenero,idCategoria,premium,silver,gold;
    public Button btnSearchProfesional;
    private DatabaseReference reference;
    private UserCategoria userCategoria;
    private int distance;
    private ProgressBar progressbar;
    private ImageButton ibtnArrowBack;
    private TextView tvDistancia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek);
        final Context mContent = this;
        premium=1;
        distance=10;

        Bundle objetoEnviado = getIntent().getExtras();
        if(objetoEnviado!= null) {
            userCategoria = (UserCategoria) objetoEnviado.getSerializable("userCategoria");
        }
        progressbar=(ProgressBar)findViewById(R.id.progressbar_Seek);
        tvDistancia = (TextView) findViewById(R.id.tv_distancia);
        seekBar = (SeekBar)findViewById(R.id.SeekBarDistance);
        seekBar.setProgress(distance);

        seekBar.setPadding(10, 0, 10, 0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance=progress;
                int posX = seekBar.getThumb().getBounds().centerX();
                tvDistancia.setText(distance + "KM");
                if (distance == 10)
                {
                    tvDistancia.setX(posX-35);
                }
                else if (distance > 1 && distance <10)
                {
                    tvDistancia.setX(posX-15);
                }
                else
                {
                    tvDistancia.setX(posX-5);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final String urlGenero = Utils.getUrl() + "genero/list";
        swPremium = (Switch)findViewById(R.id.schSeekPremium) ;
        swGold = (Switch)findViewById(R.id.schSeekGold) ;
        swSilver=(Switch)findViewById(R.id.schSeekSilver) ;
        swPremium.setOnCheckedChangeListener(this);
        swGold.setOnCheckedChangeListener(this);
        swSilver.setOnCheckedChangeListener(this);

        btnSearchProfesional =  (Button) findViewById(R.id.btnSeekSearchProfesional);

        ibtnArrowBack = (ImageButton) findViewById(R.id.ibtn_arrow_back);

        ibtnArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressbar.setVisibility(View.VISIBLE);
        //Cargar Genero
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, urlGenero, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                        try {
                            loadGenero(response);
                            spinnerGeneros = (Spinner) findViewById(R.id.spnSeekGender);
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
                        progressbar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbar.setVisibility(View.GONE);
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
        progressbar.setVisibility(View.GONE);
        SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonArrayRequest);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },LOCATION_PERMISSION_REQUEST_CODE);
        }
        else {
            if (checkPlayServices())
            {
                //autenticarFireBase(Utils.getUsuario().getUsername(),Utils.getUsuario().getPassword());
                buildGoogleApiClient();
                createLocationRequest();
                Log.i("SeekActivity","Permiso asignado");
            }
        }


        mapsSearchFragment = new MapsSearchFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.map_content,mapsSearchFragment).commit();

        btnSearchCity=(Button)findViewById(R.id.btnSearchCity);
        btnSearchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SeekActivity", "Presiono SearchCity" );
                Intent intent = new Intent(SeekActivity.this, CityActivity.class);
                Bundle bundle = new Bundle();
                userCategoria.setIdUsuario(Utils.getUsuario().getId());
                userCategoria.setIdCategoria(1);
                bundle.putSerializable("userCategoria", userCategoria);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnSearchProfesional.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             idUsuario = userCategoria.getIdUsuario();
                                             idCategoria = userCategoria.getIdCategoria();
                                             idGenero = mGenero.getId();

                                             final String urlProfesional = Utils.getUrl() + "Profesional/byFilter?idCiudad=&idGenero=" + idGenero +
                                                     "&premium=" + premium + "&silver=" + silver + "&gold=" + gold +
                                                     "&idCategoria=" + idCategoria + "&idUsuario=" + idUsuario;

                                             clear();
                                             progressbar.setVisibility(View.VISIBLE);
                                             JsonArrayRequest jsonArrayRequestProfesional = new JsonArrayRequest
                                                     (Request.Method.GET, urlProfesional, null, new Response.Listener<JSONArray>() {
                                                         @Override
                                                         public void onResponse(JSONArray response) {
                                                             Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                                                             try {
                                                                 loadProfesional(response);
                                                                 getLocations();
                                                                 getLocation();
                                                             } catch (JSONException e) {
                                                                 e.printStackTrace();
                                                             }
                                                             progressbar.setVisibility(View.GONE);
                                                         }
                                                     }, new Response.ErrorListener() {
                                                         @Override
                                                         public void onErrorResponse(VolleyError error) {
                                                             progressbar.setVisibility(View.GONE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(getClass().getSimpleName(),"Connection Failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //GoogleApiClientImplementation.checkPlayServices(this);
                    Log.i("SeekActivity","Ingreso en permiso");
                    buildGoogleApiClient();
                    createLocationRequest();
                }
                break;
            }
        }
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.i(this.getClass().getSimpleName(),"Latitude:" + String.valueOf(mLastLocation.getLatitude()));
            Log.i(this.getClass().getSimpleName(),"Logitude:" + String.valueOf(mLastLocation.getLongitude()));
            LatLng clientLatLng = new LatLng( mLastLocation.getLatitude(),mLastLocation.getLongitude());
            mapsSearchFragment.SetLocation(clientLatLng);
            /*locations = FirebaseDatabase.getInstance().getReference("Locations");
            locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new Tracking(Utils.getUsuario().getEmail(),
                    Utils.getUsuario().getUsername(),String.valueOf(mLastLocation.getLatitude()),
                    String.valueOf(mLastLocation.getLongitude())));*/
        }
        else
        {
            Toast.makeText(this,"Couldn't get the location", Toast.LENGTH_LONG).show();
        }
    }
    private  Boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICES_RES_REQUEST).show();
            }
            else
            {
                Log.i("GoogleApiClient", "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (mLocationRequest != null)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        }

    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(DISTANCE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    public void getLocations() {
        reference = FirebaseDatabase.getInstance().getReference("Locations");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLocations.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ubicacion ubicacion = snapshot.getValue(Ubicacion.class);

                    for (Profesional profesionalItem:profesionales
                    ) {
                        if(profesionalItem.getId()==ubicacion.getUid())
                        {
                            ubicacion.setImgPerfil(profesionalItem.getImgPerfilPath());
                            ubicacion.setProfesional(profesionalItem);
                            mLocations.add(ubicacion);
                        }
                    }
                }
                mapsSearchFragment.loadProfesionales(mLocations, distance);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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

    private void loadGenero(JSONArray jsonGeneros) throws JSONException {
        descripcionGenero = new ArrayList<String>();
        generos = new ArrayList<Genero>();
        for (int i=0;i < jsonGeneros.length();i++) {
            Genero genero = new Genero(jsonGeneros.optJSONObject(i));
            generos.add(genero);
            descripcionGenero.add(genero.getDescripcion());
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
    }
    public void clear() {
        try {
            final int size = profesionales.size();
            profesionales.clear();
            mLocations.clear();
        }catch (Exception e) {
            return;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
