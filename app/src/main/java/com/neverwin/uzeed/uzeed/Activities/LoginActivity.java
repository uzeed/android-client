package com.neverwin.uzeed.uzeed.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neverwin.uzeed.uzeed.Model.Usuario;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    private Button btIngresa;
    private Button btRegistrate;
    private EditText etEmail;
    private EditText etContrasena;
    private ProgressBar progressbarLogin;
    private Button btOlvidatesContrasena;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        btIngresa = (Button) findViewById(R.id.bt_ingresa);
        btRegistrate = (Button) findViewById(R.id.bt_registrate);
        btOlvidatesContrasena = (Button) findViewById(R.id.bt_olvidaste_contrasena);
        etEmail = (EditText) findViewById(R.id.et_email);
        etContrasena = (EditText) findViewById(R.id.et_contrasena);
        progressbarLogin = (ProgressBar) findViewById(R.id.progressbar_login);

        final Context mContent = this;
        btIngresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (validate(view)) {
                    //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //startActivity(intent);

                    progressbarLogin.setVisibility(View.VISIBLE);

                    final String  url = Utils.getUrl() + "login/authenticate";
                    final String username = String.valueOf(etEmail.getText());
                    final String password = String.valueOf(etContrasena.getText());
                    JSONObject jsonBody = new JSONObject();
                    try {
                        jsonBody.put("UserName",username);
                        jsonBody.put("Password", password);
                        jsonBody.put("Perfil","C");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final String mRequestBody = jsonBody.toString();

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    progressbarLogin.setVisibility(View.GONE);
                                    Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                                    Utils.setUsuario(new Usuario(response,username,password));
                                    //RegistroFireBase();
                                    //AutenticarFireBase(username, password);
                                    AutenticarFireBase(Utils.getUsuario().getUsername(),Utils.getUsuario().getPassfirebase());
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressbarLogin.setVisibility(View.GONE);

                                    if (error.networkResponse == null) {
                                        if (error.getClass().equals(TimeoutError.class)) {
                                            // Show timeout error message
                                            showMessage(view,getResources().getString(R.string.authentication_error));
                                        }
                                    }
                                    else {
                                        if (error.networkResponse.statusCode == 401) {
                                            showMessage(view, getResources().getString(R.string.invalid_username_passsoword));
                                        } else {
                                            showMessage(view, getResources().getString(R.string.authentication_error));
                                        }
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
                        /*
                            {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                //headers.put("Content-Type", "application/json");
                                headers.put("Authorization", "Basic Y2tfYmZiOTAzNDE3ZWVhMmY1ZjJiMDU2YTBjZjc2Y2IwNWU4ODEwMDgwMTpjc19kNjA0MjQ5NDk4NzMxMTBmYWE3NGE0NWIyNWVlYjQ1MmFmNzEzYzcx");
                                return headers;
                            }
                            */
                    SingletonRequestQueue.getInstance(mContent).addToRequestQueue(jsonObjectRequest);
                }
            }
        });

        btRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                //Intent intent = new Intent(LoginActivity.this, VerificationCodeActivity.class);
                startActivity(intent);
            }
        });
        
        btOlvidatesContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
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

            if (etContrasena.getText().length() == 0) {
                showMessage(view,getResources().getString(R.string.input_contrasena));
                return false;
            }

            return true;
    }

    public void RegistroFireBase(){
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword("conductoresutn@gmail.com","123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    HashMap<String,String> hashMap = new HashMap<>();

                    hashMap.put("id",userid);
                    hashMap.put("username","Pablo Gmail");
                    hashMap.put("imageURL","default");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this,"Hasta aca llego",Toast.LENGTH_SHORT);
                            }
                        }
                    });

                }
            }
        });

    }

    public void AutenticarFireBase(String mail, String contraseña){
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(mail,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //if (task.isSuccessful()){
                    //Toast.makeText(LoginActivity.this,"Authentication OK!",Toast.LENGTH_SHORT).show();
                //}
                //else
                if (!task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
