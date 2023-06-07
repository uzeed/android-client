package com.neverwin.uzeed.uzeed.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class VerificationCodeActivity extends AppCompatActivity {

    private Button btVerificationCode;
    private ProgressBar progressbarVerificationCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        btVerificationCode = (Button) findViewById(R.id.bt_verification_code);
        progressbarVerificationCode = (ProgressBar) findViewById((R.id.progressbar_verification_code));
        final Context mContent = this;
        final EditText  etVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        btVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                progressbarVerificationCode.setVisibility(View.VISIBLE);
                String username = Utils.getUsuario().getUsername();
                String code = String.valueOf(etVerificationCode.getText());
                final String  url = Utils.getUrl() + "login/validate?username="+username + "&code=" +code;



                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                progressbarVerificationCode.setVisibility(View.GONE);
                                Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                                Utils.getUsuario().setToken(response.optString("token"));
                                Intent intent = new Intent(VerificationCodeActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressbarVerificationCode.setVisibility(View.GONE);
                                Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));

                                if (error.networkResponse.statusCode == 401) {
                                    showMessage(view,getResources().getString(R.string.invalid_verification_code));
                                }
                                else
                                {
                                    showMessage(view,getResources().getString(R.string.register_error));
                                }


                            }
                        });

                SingletonRequestQueue.getInstance(mContent).addToRequestQueue(jsonObjectRequest);
            }
        });
    }

        private void showMessage(View view,String message) {

            Snackbar snackbar = Snackbar.make(view,message,Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
            snackbar.show();
        }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
