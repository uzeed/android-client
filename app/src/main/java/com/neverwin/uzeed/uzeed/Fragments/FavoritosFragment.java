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
import com.neverwin.uzeed.uzeed.Adapters.FavoritoCardAdapter;
import com.neverwin.uzeed.uzeed.Model.Favorito;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FavoritosFragment extends Fragment {
    private RecyclerView rv;
    private FavoritoCardAdapter favoritoCardAdapter;
    private ProgressBar progressBar;
    private ArrayList<String> descripcionFavorito;
    private ArrayList<Favorito> favoritos;
    private View view;

    public FavoritosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_favoritos, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_favorite);
        rv = view.findViewById(R.id.rv_Favorite_List_item);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));


        progressBar.setVisibility(View.VISIBLE);

        final String urlFavorito = Utils.getUrl() + "favorito/list?idUsuario="  + Utils.getUsuario().getId(); //+ idUsuario;

        JsonArrayRequest jsonArrayRequestSolicitud = new JsonArrayRequest
                (Request.Method.GET, urlFavorito, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                        try {
                            loadFavoritos(response);
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));
                        //Utils.showMessage(view.findViewById(android.R.id.content),getResources().getString(R.string.genero_error));
                    }
                }) {
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

    private void loadFavoritos(JSONArray jsonFavorito) throws JSONException {
        descripcionFavorito = new ArrayList<String>();
        favoritos = new ArrayList<Favorito>();
        for (int i=0;i < jsonFavorito.length();i++) {
           Favorito favorito = new Favorito(jsonFavorito.optJSONObject(i));
            favoritos.add(favorito);
            descripcionFavorito.add(favorito.getProfesional().getAlias());
        }
        favoritoCardAdapter = new FavoritoCardAdapter(getContext(), favoritos);
        rv.setAdapter(favoritoCardAdapter);

    }
}
