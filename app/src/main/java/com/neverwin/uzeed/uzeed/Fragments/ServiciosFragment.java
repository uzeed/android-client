package com.neverwin.uzeed.uzeed.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.neverwin.uzeed.uzeed.Adapters.ServiciosCardAdapter;
import com.neverwin.uzeed.uzeed.Model.Servicio;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiciosFragment extends Fragment {

    private RecyclerView rv;
    private ServiciosCardAdapter servicioCardAdapter;
    private ArrayList<String> descripcionServicios;
    private ArrayList<Servicio> servicios;
    private ProgressBar progressbarSolicitud;
    private View view;


    public ServiciosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_servicios, container, false);
        progressbarSolicitud = (ProgressBar) view.findViewById(R.id.progressbar_solicitud);
        rv = view.findViewById(R.id.rv_Servicios_List_item);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        progressbarSolicitud.setVisibility(View.VISIBLE);

        final String urlsolicitud = Utils.getUrl() + "solicitud?idUsuario="  + Utils.getUsuario().getId();

        JsonArrayRequest jsonArrayRequestSolicitud = new JsonArrayRequest
                (Request.Method.GET, urlsolicitud, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                        try {
                            loadSolicitudes(response);
                            progressbarSolicitud.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbarSolicitud.setVisibility(View.GONE);
                        Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));
                        //Utils.showMessage(view.findViewById(android.R.id.content),getResources().getString(R.string.genero_error));
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
        SingletonRequestQueue.getInstance(getContext()).addToRequestQueue(jsonArrayRequestSolicitud);

        return view;
    }

    private void loadSolicitudes(JSONArray jsonServicio) throws JSONException {
        descripcionServicios = new ArrayList<String>();
        servicios = new ArrayList<Servicio>();
        for (int i=0;i < jsonServicio.length();i++) {
            Servicio servicio = null;
            try {
                servicio = new Servicio(jsonServicio.optJSONObject(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            servicios.add(servicio);
            descripcionServicios.add(servicio.getProfesional().getAlias());

        }


        servicioCardAdapter = new ServiciosCardAdapter(getContext(), servicios);
        rv.setAdapter(servicioCardAdapter);

    }
}
