package com.neverwin.uzeed.uzeed.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.neverwin.uzeed.uzeed.Activities.ForgotPasswordActivity;
import com.neverwin.uzeed.uzeed.Activities.MainActivity;
import com.neverwin.uzeed.uzeed.Activities.VerificationCodeActivity;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment {


    private Button btRecuperarContrasena;
    private ProgressBar progressbarForgotPassword;
    private EditText etCtaEmail;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        etCtaEmail = (EditText) view.findViewById(R.id.et_cta_email);
        btRecuperarContrasena = (Button) view.findViewById(R.id.bt_recuperar_contrasena);
        progressbarForgotPassword = (ProgressBar) view.findViewById(R.id.progressbar_forgot_password);


        btRecuperarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (valid(view)) {


                    String email = String.valueOf(etCtaEmail.getText());
                    String code = Utils.getVerificationCode();
                    Utils.getUsuario().setEmail(email);
                    Utils.getUsuario().setUsername(email);

                    final String url = Utils.getUrl() + "login/forgotpassword?email="+email + "&code=" +code;

                    progressbarForgotPassword.setVisibility(View.VISIBLE);
                    StringRequest stringRequest = new StringRequest
                            (Request.Method.PUT, url,new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    progressbarForgotPassword.setVisibility(View.GONE);
                                    Log.i(this.getClass().getSimpleName(), "Response: " + response);

                                    Fragment fragment = new RecoveryFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.content_frame, fragment)
                                            .commit();


                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressbarForgotPassword.setVisibility(View.GONE);
                                    Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));

                                    if (error.networkResponse.statusCode == 401) {
                                        Utils.showMessage(view, getResources().getString(R.string.invalid_verification_code));
                                    } else {
                                        Utils.showMessage(view, getResources().getString(R.string.password_recovery_failed));
                                    }


                                }
                            });

                    SingletonRequestQueue.getInstance(getContext()).addToRequestQueue(stringRequest);
                }
            }

        });

        return view;
    }

    private boolean valid(View view) {

        if (etCtaEmail.getText().length() == 0) {
            Utils.showMessage(view,getResources().getString(R.string.input_email));
            return false;
        }

        return true;

    }

}


