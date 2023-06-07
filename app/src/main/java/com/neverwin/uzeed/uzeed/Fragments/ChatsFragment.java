package com.neverwin.uzeed.uzeed.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.neverwin.uzeed.uzeed.Adapters.ChatsAdapter;
import com.neverwin.uzeed.uzeed.Model.Chat;
import com.neverwin.uzeed.uzeed.Model.Profesional;
import com.neverwin.uzeed.uzeed.Model.Usuario;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatsFragment extends Fragment {


    private RecyclerView rvChats;

    FirebaseUser fUser;
    DatabaseReference reference;
    ArrayList<String> usersList;
    private ProgressBar progressBar;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Usuario> usuariosChat;
    private ChatsAdapter chatsAdapter;
    private ArrayList<Profesional> profesionales;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        rvChats = (RecyclerView) view.findViewById(R.id.rv_chats);
        rvChats.setLayoutManager(new LinearLayoutManager(this.getContext()));
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<String>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        progressBar.setVisibility(View.VISIBLE);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getSender().equals(fUser.getUid()))
                    {
                        usersList.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(fUser.getUid())) {
                        usersList.add(chat.getSender());
                    }

                }

                //getUsuarios();
                usersList = cleanUserList(usersList);
                getProfesionales();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }


    private ArrayList<String> cleanUserList(ArrayList<String> mUserList) {

        ArrayList<String> mCleanUserList = new ArrayList<String>();

        for (String id:mUserList)
        {
            if (!mCleanUserList.contains(id))
            {
                mCleanUserList.add(id);
            }
        }

        return mCleanUserList;
    }
    private void getProfesionales() {

        final String  url = Utils.getUrl() + "profesional/getByListId";

         String mBody = "[";
         int count = 0;
        for (String id: usersList) {
            mBody = count == 0 ? mBody + "\"" + id + "\"" : mBody + "," + "\"" + id + "\"";
            count ++;
        }
        mBody = mBody + "]";

        final String mRequestBody = mBody;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONArray>()  {

                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                        try {
                            loadProfesionales(response);
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));


                    }
                }) {
            @Override
            public byte[] getBody() {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " +  Utils.getUsuario().getToken());
                return headers;
            }

        };

        SingletonRequestQueue.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);




    }

    private void loadProfesionales(JSONArray jsonProfesionales) throws JSONException {
        profesionales = new ArrayList<Profesional>();
        for (int i=0;i < jsonProfesionales.length();i++) {
            Profesional profesional = new Profesional(jsonProfesionales.optJSONObject(i));
            profesionales.add(profesional);
        }

        chatsAdapter = new ChatsAdapter(profesionales);
        rvChats.setAdapter(chatsAdapter);

    }
    /*
    private void getUsuarios() {

        final String  url = Utils.getUrl() + "Usuario";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                        try {
                            loadUsuarios(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadUsuariosChat();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));

                        //Utils.showMessage(view,getResources().getString(R.string.cities_error),175);
                    }
                }) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " +  Utils.getUsuario().getToken());
                return headers;
            }
        };

        SingletonRequestQueue.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);



    }

    private void loadUsuariosChat() {

        usuariosChat = new ArrayList<Usuario>();

        for(Usuario usuario : usuarios)
        {
            for (String id: usersList)
            {
                if (usuario.getIdfirebase().equals(id)) // existe en el chat
                {

                    Boolean noExiste = true;
                    for (Usuario usuarioChat: usuariosChat) {
                        if (usuarioChat.getId() == usuario.getId())
                        {
                            noExiste = false;
                        }
                    }
                    if (noExiste) // lo agrego
                    {
                        usuariosChat.add(usuario);
                    }
                }
            }
        }
        chatsAdapter = new ChatsAdapter(usuariosChat);
        rvChats.setAdapter(chatsAdapter);
    }
*/

    private void loadUsuarios(JSONArray arrayUsuarios) throws JSONException {
        usuarios = new ArrayList<Usuario>();
        for (int i = 0; i<arrayUsuarios.length();i++)
        {
            usuarios.add(new Usuario(arrayUsuarios.getJSONObject(i)));
        }
    }
}