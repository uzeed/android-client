package com.neverwin.uzeed.uzeed.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
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
import com.neverwin.uzeed.uzeed.Fragments.MapsEstablecimientoSearchFragment;
import com.neverwin.uzeed.uzeed.Model.Establecimiento;
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

public class SeekEstablecimientoActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener, CompoundButton.OnCheckedChangeListener {
        private SeekBar seekBar;
    private int distance;
    private int calificacion;
    private Button btnSearchEstablecimiento;
    private ArrayList<Establecimiento> establecimientos;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RES_REQUEST = 7172;
    private static final int UPDATE_INTERVAL = 5000;
    private static final int FASTEST_INTERVAL = 3000;
    private static final int DISTANCE = 10;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private MapsEstablecimientoSearchFragment mapsSearchFragment;
    private final ArrayList<Ubicacion> mLocations = new ArrayList<Ubicacion>();
    private UserCategoria userCategoria;
    private ArrayList<String> descripcionEstablecimientos;
    private RatingBar rbCalificacion;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_establecimiento);
        final Context mContent = this;
        distance = 1000000000;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Búsqueda de Establecimiento");
        Bundle objetoEnviado = getIntent().getExtras();
        if(objetoEnviado!= null) {
            userCategoria = (UserCategoria) objetoEnviado.getSerializable("userCategoria");
        }
        rbCalificacion=(RatingBar)findViewById(R.id.ratb_SeekCalificacionEstablecimiento);
        rbCalificacion.setRating(5);
        calificacion = 5;
        btnSearchEstablecimiento = (Button) findViewById(R.id.btnSeekSearchEstablecimiento);
        progressbar=(ProgressBar)findViewById(R.id.progressbar_SeekEstablecimiento);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                //autenticarFireBase(Utils.getUsuario().getUsername(),Utils.getUsuario().getPassword());
                buildGoogleApiClient();
                createLocationRequest();
                Log.i("SeekActivity", "Permiso asignado");
            }
        }

        mapsSearchFragment = new MapsEstablecimientoSearchFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.map_contentEstablecimiento, mapsSearchFragment).commit();


        seekBar = (SeekBar) findViewById(R.id.SeekBarDistance);
        seekBar.setMax(10);
        seekBar.setProgress(distance);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

            btnSearchEstablecimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idCategoria = userCategoria.getIdCategoria();
                calificacion = (int)rbCalificacion.getRating();

                final String urlEstablecimiento = Utils.getUrl() + "establecimiento/getByCategoria?idCategoria=" + idCategoria + "&calificacion=" + calificacion;
                clear();
                progressbar.setVisibility(View.VISIBLE);
                JsonArrayRequest jsonArrayRequestProfesional = new JsonArrayRequest
                        (Request.Method.GET, urlEstablecimiento, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                                try {
                                    loadEstablecimiento(response);
                                    getLocations();
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

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.i(this.getClass().getSimpleName(), "Latitude:" + String.valueOf(mLastLocation.getLatitude()));
            Log.i(this.getClass().getSimpleName(), "Logitude:" + String.valueOf(mLastLocation.getLongitude()));
            LatLng clientLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mapsSearchFragment.SetLocation(clientLatLng);
        } else {
            Toast.makeText(this, "Couldn't get the location", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(getClass().getSimpleName(), "Connection Failed");
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //GoogleApiClientImplementation.checkPlayServices(this);
                    Log.i("SeekEstActivity", "Ingreso con permiso");
                }
                break;
            }
        }
    }


    public void clear() {
        try {
            final int size = establecimientos.size();
            establecimientos.clear();
        }catch (Exception e) {
            return;
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


    private void loadEstablecimiento(JSONArray jsonEstablecimientos) throws JSONException {
        descripcionEstablecimientos = new ArrayList<String>();
        establecimientos = new ArrayList<Establecimiento>();
        for (int i=0;i < jsonEstablecimientos.length();i++) {
            Establecimiento establecimiento = new Establecimiento(jsonEstablecimientos.optJSONObject(i));
            establecimientos.add(establecimiento);
            descripcionEstablecimientos.add(establecimiento.getAlias());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
    public void getLocations() {
        Ubicacion ubicacion = new Ubicacion();
        for (Establecimiento establecimientoItem:establecimientos
        ) {
            ubicacion.setEstablecimiento(establecimientoItem);
            ubicacion.setLat(establecimientoItem.getLatitud());
            ubicacion.setLng(establecimientoItem.getLongitud());
            mLocations.add(ubicacion);
        }
        mapsSearchFragment.loadEstablecimientos(mLocations, distance);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}