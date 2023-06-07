package com.neverwin.uzeed.uzeed.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
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
import com.neverwin.uzeed.uzeed.Activities.PerfilActivity;
import com.neverwin.uzeed.uzeed.Model.Profesional;
import com.neverwin.uzeed.uzeed.Model.Ubicacion;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.LoadImage;
import com.neverwin.uzeed.uzeed.Utils.LoadImageMarkerOptions;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@linkFragment} subclass.
 */
public class MapsSearchFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    GoogleMap map;
    private LatLng clientLatLng;
    private HashMap<Marker, Profesional> eventMarkerMap;

    public MapsSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps_search, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment supportMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapSearch);
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
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(clientLatLng,12.0f));
    }

    public void loadProfesionales(ArrayList<Ubicacion> locations, double distance) {
        eventMarkerMap = new HashMap<Marker, Profesional>();
        map.clear();
        this.SetLocation(this.clientLatLng); //vuelve a asignar la ubicación del cliente
        for (int i=0;i<locations.size();i++) {
            final Ubicacion location = locations.get(i);
            Log.i("Distancias","Lat: " + location.getLat() + "Long:" + location.getLng() + "distanciaFiltro" + distance );
            LatLng profecionalLatLng = new LatLng( location.getLat(),location.getLng());
            double distanciaProfesional = CalculationByDistance(clientLatLng, profecionalLatLng);
            Log.i("Distancia Profesional",String.valueOf(distanciaProfesional));
            if(distanciaProfesional <= distance) {

                //byte[] decodeString = Base64.decode(location.getProfesional().getImgPerfil(),Base64.DEFAULT);
                //Bitmap decodedByte =BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                //BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(Utils.getBitmapFromLayout(getContext(),R.layout.image_circle,decodedByte));
                //BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(Utils.getBitmapFromLayout(getContext(),R.layout.image_circle,R.drawable.girl_face));
                final MarkerOptions marcaOptions = new MarkerOptions().position(profecionalLatLng).title(location.getProfesional().getAlias());
                LoadImageMarkerOptions markerOptions = new LoadImageMarkerOptions(marcaOptions,getContext());
                markerOptions.execute(location.getProfesional().getImgPerfilPath());

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                        Marker marca = map.addMarker(marcaOptions);
                        eventMarkerMap.put(marca, location.getProfesional() );
                    }
                }, 7000);

                //map.animateCamera(CameraUpdateFactory.newLatLngZoom(profecionalLatLng, 12.0f));
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
        Profesional profesionalSeleccionado = eventMarkerMap.get(marker);
        if(profesionalSeleccionado!=null) {
            Intent intent = null;
            intent = new Intent(this.getActivity(), PerfilActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("profesional", profesionalSeleccionado);
            intent.putExtras(bundle);
            this.startActivity(intent);
        }
        return false;
    }
}
