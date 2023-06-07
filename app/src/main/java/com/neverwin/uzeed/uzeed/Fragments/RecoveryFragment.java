package com.neverwin.uzeed.uzeed.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.neverwin.uzeed.uzeed.Activities.MainActivity;
import com.neverwin.uzeed.uzeed.Activities.VerificationCodeActivity;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecoveryFragment extends Fragment {


    private EditText etCodigoVerificacion;
    private EditText etRegisterContrasena;
    private Button btRecuperarContrasena;
    private EditText etRepiteRegisterContrasena;
    private ProgressBar progressBarRecoveryPassword;

    public RecoveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery, container, false);
        setViews(view);
        setListeners();
        return view;
    }

    private void setListeners() {

        btRecuperarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (valid(view)) {
                    progressBarRecoveryPassword.setVisibility(View.VISIBLE);
                    String username = Utils.getUsuario().getUsername();
                    String code = String.valueOf(etCodigoVerificacion.getText());
                    final String  url = Utils.getUrl() + "login/validate?username="+username + "&code=" +code;



                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    progressBarRecoveryPassword.setVisibility(View.GONE);
                                    Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                                    Utils.getUsuario().setToken(response.optString("token"));
                                    Utils.getUsuario().setNombre(response.optString("nombre"));
                                    Utils.getUsuario().setUsername(response.optString("username"));
                                    updatePassword(view);
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressBarRecoveryPassword.setVisibility(View.GONE);
                                    Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));

                                    if (error.networkResponse.statusCode == 401) {
                                        Utils.showMessage(view,getResources().getString(R.string.invalid_verification_code));
                                    }
                                    else
                                    {
                                        Utils.showMessage(view,getResources().getString(R.string.register_error));
                                    }


                                }
                            });

                    SingletonRequestQueue.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

                }
            }
        });
    }

    private void updatePassword(final View view) {

        progressBarRecoveryPassword.setVisibility(View.VISIBLE);
        String username = Utils.getUsuario().getUsername();
        String password = String.valueOf(etRegisterContrasena.getText());
        final String  url = Utils.getUrl() + "Usuario?username=" + username + "&password="+ password;



        StringRequest stringRequest = new StringRequest
                (Request.Method.PUT, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progressBarRecoveryPassword.setVisibility(View.GONE);
                        Log.i(this.getClass().getSimpleName(), "Response: " + response);
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        getActivity().startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBarRecoveryPassword.setVisibility(View.GONE);
                        Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));

                        if (error.networkResponse.statusCode == 401) {
                            Utils.showMessage(view,getResources().getString(R.string.invalid_verification_code));
                        }
                        else
                        {
                            Utils.showMessage(view,getResources().getString(R.string.register_error));
                        }


                    }
                }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + Utils.getUsuario().getToken());
                    return headers;
                }

            };

        SingletonRequestQueue.getInstance(getContext()).addToRequestQueue(stringRequest);

    }



    private boolean valid(View view) {
        if (etCodigoVerificacion.getText().length() == 0) {
            Utils.showMessage(view,getResources().getString(R.string.introduce_codigo));
            return false;
        }
        if (etRegisterContrasena.getText().length() == 0) {
            Utils.showMessage(view,getResources().getString(R.string.input_contrasena));
            return false;
        }
        if (etRepiteRegisterContrasena.getText().length() == 0) {
            Utils.showMessage(view,getResources().getString(R.string.input_contrasena));
            return false;
        }
        if (!String.valueOf(etRegisterContrasena.getText()).equals(String.valueOf(etRepiteRegisterContrasena.getText()))) {
            Utils.showMessage(view,getResources().getString(R.string.contrasenas_no_coinciden));
            return false;
        }
        return true;
    }


    private void setViews(View view) {

        etCodigoVerificacion = (EditText) view.findViewById(R.id.et_codigo_verificacion);
        etRegisterContrasena = (EditText) view.findViewById(R.id.et_register_contrasena);
        etRepiteRegisterContrasena = (EditText) view.findViewById(R.id.et_register_repite_contrasena);
        btRecuperarContrasena = (Button) view.findViewById(R.id.bt_recuperar_contrasena);
        progressBarRecoveryPassword = (ProgressBar) view.findViewById(R.id.progressbar_recovery_password);
        
    }


}
