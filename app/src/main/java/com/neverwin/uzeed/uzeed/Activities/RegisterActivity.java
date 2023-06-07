package com.neverwin.uzeed.uzeed.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.neverwin.uzeed.uzeed.Model.Usuario;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {

    private Button btRegistrar;
    private Button btYatengocuenta;
    private EditText etNombre;
    private EditText etEmail;
    private EditText etContrasena;
    private EditText etRepiteContrasena;
    private ProgressBar progressbarRegister;
    private String idFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setViews();
        setListeners();
    }

    private void setListeners() {
        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistroFireBase(view, String.valueOf(etEmail.getText()), String.valueOf(etContrasena.getText()));
            }
        });
        btYatengocuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setViews() {
        btRegistrar = (Button) findViewById(R.id.bt_registrar);
        btYatengocuenta = (Button) findViewById(R.id.bt_yatengocuenta);
        etNombre = (EditText) findViewById(R.id.et_register_nombre);
        etEmail = (EditText) findViewById(R.id.et_register_email);
        etContrasena = (EditText) findViewById(R.id.et_register_contrasena);
        etRepiteContrasena = (EditText) findViewById(R.id.et_register_repite_contrasena);
        progressbarRegister = (ProgressBar) findViewById(R.id.progressbar_register);
    }

    private void registrar(final View view) {
            Log.i(this.getLocalClassName(),"Registrar Usuario");

            final String  url = Utils.getUrl() + "login/signup";
            final String email = String.valueOf(etEmail.getText());
            final String password = String.valueOf(etContrasena.getText());
            final String nombre = String.valueOf(etNombre.getText());
            final String code = Utils.getVerificationCode();
            final Context mContent = this;

            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("username",email);
                jsonBody.put("password", password);
                jsonBody.put("email", email);
                jsonBody.put("perfil", "C");
                jsonBody.put("nombre", nombre);
                jsonBody.put("code", code);
                jsonBody.put("idFirebase",idFirebase);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            progressbarRegister.setVisibility(View.GONE);
                            Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                            Utils.setUsuario(new Usuario(response,email,password,code));
                            Intent intent = new Intent(RegisterActivity.this, VerificationCodeActivity.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressbarRegister.setVisibility(View.GONE);
                            Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));

                            if (error.networkResponse.statusCode == 401) {
                                showMessage(view,getResources().getString(R.string.invalid_username_passsoword));
                            }
                            else if (error.networkResponse.statusCode == 412) {
                                showMessage(view,getResources().getString(R.string.user_already_exist));
                            }
                            else
                            {
                                showMessage(view,getResources().getString(R.string.register_error));
                            }


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
            };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(Utils.MY_DEFAULT_TIMEOUT,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            SingletonRequestQueue.getInstance(mContent).addToRequestQueue(jsonObjectRequest);

        }


    private void showMessage(View view,String message) {

        Snackbar snackbar = Snackbar.make(view,message,Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    private boolean validate(View view) {

        if (etEmail.getText().length() == 0) {
            showMessage(view,getResources().getString(R.string.input_email));
            return false;
        }

        if (etNombre.getText().length() == 0) {
            showMessage(view,getResources().getString(R.string.input_nombre));
            return false;
        }

        if (etContrasena.getText().length() == 0) {
            showMessage(view,getResources().getString(R.string.input_contrasena));
            return false;
        }

        /*if (etContrasena.getText().length() <6) {
            showMessage(view,getResources().getString(R.string.input_contrasena_min));
            return false;
        }*/
        if (etRepiteContrasena.getText().length() == 0) {
            showMessage(view,getResources().getString(R.string.input_contrasena));
            return false;
        }

        if (!String.valueOf(etContrasena.getText()).equals(String.valueOf(etRepiteContrasena.getText()))) {
            showMessage(view,getResources().getString(R.string.contrasenas_no_coinciden));
            return false;
        }
        return true;
    }

    public void RegistroFireBase(final View view, String userName, String Password){
        if (validate(view)) {
            progressbarRegister.setVisibility(View.VISIBLE);
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(userName, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        idFirebase = firebaseUser.getUid();
                        registrar(view);
                    }
                    else
                    {
                        progressbarRegister.setVisibility(View.GONE);
                        showMessage(view,task.getException().getLocalizedMessage());
                    }
                }
            });
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
