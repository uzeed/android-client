package com.neverwin.uzeed.uzeed.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.neverwin.uzeed.uzeed.Activities.EstablecimientoActivity;
import com.neverwin.uzeed.uzeed.Model.Establecimiento;
import com.neverwin.uzeed.uzeed.Model.Ubicacion;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.LoadImageMarkerOptions;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@linkFragment} subclass.
 */
public class MapsEstablecimientoSearchFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    GoogleMap map;
    private LatLng clientLatLng;
    private HashMap<Marker, Establecimiento> eventMarkerMap;

    public MapsEstablecimientoSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps_establecimiento_search, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment supportMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapEstablecimientoSearch);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);
    }

    public void SetLocation(LatLng clientLatLng){
        this.clientLatLng = clientLatLng;
        map.addMarker(new MarkerOptions().position(clientLatLng).title(Utils.getUsuario().getUsername()));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(clientLatLng,10.0f));
    }

    public void loadEstablecimientos(ArrayList<Ubicacion> locations, double distance) {
        map.clear();
        eventMarkerMap = new HashMap<Marker, Establecimiento>();
        if(locations!=null) {
            for (int i = 0; i < locations.size(); i++) {
                final Ubicacion location = locations.get(i);
                Log.i("Distancias", "Lat: " + location.getLat() + "Long:" + location.getLng() + "distanciaFiltro" + distance);
                LatLng establecimientoLatLng = new LatLng(location.getLat(), location.getLng());
                double distanciaEstablecimiento = CalculationByDistance(clientLatLng, establecimientoLatLng);

                //Borrar!!!!
                distanciaEstablecimiento =8;

                if (distanciaEstablecimiento <= distance) {
                    //byte[] decodeString = Base64.decode(location.getEstablecimiento().getImagenPerfil(),Base64.DEFAULT);
                    //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                    //BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(Utils.getBitmapFromLayout(getContext(),R.layout.image_circle,decodedByte));
                    //BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(Utils.getBitmapFromLayout(getContext(), R.layout.image_circle, R.drawable.pointest));
                    /*MarkerOptions marcaOptions = new MarkerOptions().position(establecimientoLatLng)
                            .title(location.getEstablecimiento().getNombre())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    Marker marca = map.addMarker(marcaOptions);
                    eventMarkerMap.put(marca, location.getEstablecimiento());*/
                    //map.animateCamera(CameraUpdateFactory.newLatLngZoom(profecionalLatLng, 12.0f));

                    //BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(Utils.getBitmapFromLayout(getContext(),R.layout.image_circle,R.drawable.pointest));
                    Log.i("CARGANDO", "Lat: " + location.getLat() + "Long:" + location.getLng() + "distanciaFiltro" + distance);
                    //MarkerOptions marcaOptions = new MarkerOptions().position(establecimientoLatLng).title(location.getEstablecimiento().getNombre()).icon(icon);
                    //Marker marca = map.addMarker(marcaOptions);
                    //eventMarkerMap.put(marca, location.getEstablecimiento() );

                    final MarkerOptions marcaOptions = new MarkerOptions().position(establecimientoLatLng).title(location.getEstablecimiento().getNombre());
                    LoadImageMarkerOptions markerOptions = new LoadImageMarkerOptions(marcaOptions,getContext());
                    markerOptions.execute(location.getEstablecimiento().getImagenPerfilPath());

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // Actions to do after 10 seconds
                            Marker marca = map.addMarker(marcaOptions);
                            eventMarkerMap.put(marca, location.getEstablecimiento());
                        }
                    }, 7000);
                }
            }
        }
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) *
        Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        //return Radius * c;
        return km;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Establecimiento establecimientoSeleccionado = eventMarkerMap.get(marker);
        if(establecimientoSeleccionado!=null) {
            Intent intent = null;
            intent = new Intent(this.getActivity(), EstablecimientoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("establecimiento", establecimientoSeleccionado);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return false;
    }
}
