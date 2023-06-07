package com.neverwin.uzeed.uzeed.Activities;

import android.app.DownloadManager;
import android.app.MediaRouteButton;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neverwin.uzeed.uzeed.Adapters.MensajesAdapter;
import com.neverwin.uzeed.uzeed.Model.Chat;
import com.neverwin.uzeed.uzeed.Model.Data;
import com.neverwin.uzeed.uzeed.Model.FirebaseToken;
import com.neverwin.uzeed.uzeed.Model.Mensaje;
import com.neverwin.uzeed.uzeed.Model.Notification;
import com.neverwin.uzeed.uzeed.Model.Profesional;
import com.neverwin.uzeed.uzeed.Model.Sender;
import com.neverwin.uzeed.uzeed.Model.Ubicacion;
import com.neverwin.uzeed.uzeed.Model.Usuario;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.neverwin.uzeed.uzeed.Utils.Utils.showMessage;

public class MensajesActivity extends AppCompatActivity {

    private RecyclerView reciclerView;
    private List<Mensaje> mensajes;
    private MensajesAdapter adapter;
    private ImageButton btnEnviarMensaje;
    private Button btnSolicitarServicio;
    private EditText etEscribirMensaje;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private Profesional profesional;
    private ProgressBar progressBar;
    private static final String USUARIO_RECEIVER = "USUARIO_RECEIVER";
    private static final String PROFESIONAL ="profesional";
    private Usuario usuarioProfesional;
    private ImageButton ibtnArrowBack;
    private TextView tvNombreProfesional;
    private TextView tvEstadoProfesional;
    private ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context mContent = this;


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressBar = (ProgressBar)findViewById(R.id.progressbar_Mensaje);
        reciclerView=(RecyclerView)findViewById(R.id.rv_Mensajes);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        btnEnviarMensaje = (ImageButton)findViewById(R.id.btn_Send);
        btnSolicitarServicio=(Button)findViewById(R.id.btn_SolicitarServicio);
        etEscribirMensaje = (EditText)findViewById(R.id.et_ingreseMensaje);
        ibtnArrowBack = (ImageButton) findViewById(R.id.ibtn_arrow_back);
        tvNombreProfesional = (TextView) findViewById(R.id.tv_nombre_profesional);
        tvEstadoProfesional = (TextView) findViewById(R.id.tv_estado_profesional);
        ivProfile  = (ImageView) findViewById(R.id.ivProfile);
        reciclerView.setLayoutManager(lm);
        mensajes=new ArrayList<>();

        profesional=(Profesional)getIntent().getSerializableExtra(PROFESIONAL);
        if(profesional==null)
        {
            usuarioProfesional = (Usuario) getIntent().getSerializableExtra(USUARIO_RECEIVER);
            getProfesional();
        }
        else
        {
            usuarioProfesional=profesional.getUsuario();
            String titulo = "Chat con " + profesional.getAlias();
            setTitle(titulo);
            tvNombreProfesional.setText(profesional.getAlias());
            byte[] decodedString = Base64.decode(profesional.getImgPerfil(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivProfile.setImageBitmap(decodedByte);
            getEstadoProfesional(profesional.getUsuario());
            adapter= new MensajesAdapter(mensajes, this,profesional);
            reciclerView.setAdapter(adapter);
            LeerMensajesFB();
        }


        btnEnviarMensaje.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (etEscribirMensaje.getText().length() > 0) {
                        CreateMessage(etEscribirMensaje.getText().toString());
                    }
                }
            });
        //LeerMensajesFB();

        //setScrollBarChat();

        btnSolicitarServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String urlSolicitar = Utils.getUrl() + "solicitud?idUsuario="  + Utils.getUsuario().getId() +
                        "&idProfesional=" + profesional.getId();

                progressBar.setVisibility(View.VISIBLE);
                JsonObjectRequest jsonArrayRequestProfesional = new JsonObjectRequest
                        (Request.Method.POST, urlSolicitar, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressBar.setVisibility(View.GONE);
                                showMessage(findViewById(android.R.id.content), getResources().getString(R.string.solicitud_exitosa));
                                //finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                                showMessage(findViewById(android.R.id.content), getResources().getString(R.string.solicitud_error));
                            }
                        }) {
                    @Override
                    public Map getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("Authorization", "Bearer " + Utils.getUsuario().getToken());
                        return headers;
                    }
                };
                SingletonRequestQueue.getInstance(mContent).addToRequestQueue(jsonArrayRequestProfesional);
            }
        });

        ibtnArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void CreateMessage(String mensaje){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",firebaseUser.getUid());
        hashMap.put("receiver",usuarioProfesional.getIdfirebase());
        hashMap.put("message",mensaje);
        hashMap.put("datetime",Utils.getDiaHoraActual());
        hashMap.put("chatId", usuarioProfesional.getIdfirebase() + firebaseUser.getUid());
        reference.child("Chats").push().setValue(hashMap);

        etEscribirMensaje.setText("");

        sendNotification(usuarioProfesional,firebaseUser,mensaje);
    }

    private void sendNotification(final Usuario usuarioProfesional, final FirebaseUser firebaseUser, final String mensaje) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(usuarioProfesional.getIdfirebase());
        final Context mContent = this;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    FirebaseToken firebaseToken = snapshot.getValue(FirebaseToken.class);
                    Data data = new Data(firebaseUser.getUid(),firebaseUser.getDisplayName(),R.mipmap.ic_launcher,Utils.getUsuario().getNombre() + ": "   + mensaje,"Uzzed - Nuevo Mensaje!", usuarioProfesional.getIdfirebase());

                    Notification notification = new Notification("Uzzed - Nuevo Mensaje!", Utils.getUsuario().getNombre() + ": " + mensaje);

                    Sender sender = new Sender(firebaseToken.getToken(),notification,data);
                    //Sender sender = new Sender(data,firebaseToken.getToken(),"Uzzed - Nuevo Mensaje!", firebaseUser.getDisplayName() + ": " + mensaje);



                    final String  url = "https://fcm.googleapis.com/fcm/send";
                    Gson gson = new GsonBuilder().create();
                    String jsonBody = gson.toJson(sender);

                    // Hace el POST

                    final String mRequestBody = jsonBody;

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
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
                            headers.put("Authorization", "key=AAAAK-fkC2o:APA91bG-NtQhOf33jzbE-v04qVuNEcQ9FMA9geal7WY_Ewixt37KUVlYbd_YmtLnLVN2vZXUB99sNz_EP6Yb6mI7US2mwaE21BwwKdQA6QcFahEOxRM4vvkisOSudcrydC5c87eTXITP");
                            return headers;
                        }

                    };

                    SingletonRequestQueue.getInstance(mContent).addToRequestQueue(jsonObjectRequest);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        setScrollBarChat();
    }

    public void setScrollBarChat(){
        reciclerView.scrollToPosition(adapter.getItemCount()-1);
    }

    public void LeerMensajesFB(){
        final ArrayList<Chat> mChats = new ArrayList<Chat>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        String chatId = usuarioProfesional.getIdfirebase() + firebaseUser.getUid();
        Query chatsSender = reference.orderByChild("chatId").equalTo(chatId);

        chatsSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChats.clear();
                mensajes.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Chat chat = snapshot.getValue(Chat.class);

                    mChats.add(chat);
                    if(chat.getReceiver().equalsIgnoreCase(firebaseUser.getUid()))
                    {
                        mensajes.add(new Mensaje(1, chat.getMessage(), chat.getDatetime(), 2 ));
                    }
                    else{
                        mensajes.add(new Mensaje(1, chat.getMessage(), chat.getDatetime(), 1));
                    }

                }
                adapter.notifyDataSetChanged();
                setScrollBarChat();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void getProfesional() {

        final String  url = Utils.getUrl() + "profesional/byuser?username=" + usuarioProfesional.getUsername();
        progressBar.setVisibility(View.VISIBLE);
        final Context context = MensajesActivity.this;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.i(this.getClass().getSimpleName(), "Response: " + response.toString());
                        try {
                            profesional=new Profesional(response);
                            String titulo = "Chat con " + profesional.getAlias();
                            setTitle(titulo);
                            // Setea el encabezado del Chat
                            tvNombreProfesional.setText(profesional.getAlias());
                            byte[] decodedString = Base64.decode(profesional.getImgPerfil(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            ivProfile.setImageBitmap(decodedByte);
                            getEstadoProfesional(profesional.getUsuario());
                            adapter= new MensajesAdapter(mensajes, context,profesional);
                            reciclerView.setAdapter(adapter);
                            LeerMensajesFB();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);

                        //El retorno del servicio es null por lo tanto no tiene profesional asociado

                        if (error.networkResponse.statusCode == 404) {
                        }
                        else
                        {
                            Log.i(this.getClass().getSimpleName(), String.valueOf(error.networkResponse.statusCode));
                           // showMessage(view,getResources().getString(R.string.authentication_error));
                        }

                    }
                }) {

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " +  Utils.getUsuario().getToken());
                return headers;
            }
        };

        SingletonRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);


    }

    private void getEstadoProfesional(Usuario usuario) {

        reference = FirebaseDatabase.getInstance().getReference("Locations");
        DatabaseReference location = reference.child(usuario.getIdfirebase());
        location.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Ubicacion ubicacion = dataSnapshot.getValue(Ubicacion.class);
                if (ubicacion.getStatus() != null) {
                    tvEstadoProfesional.setText(ubicacion.getStatus());
                }
                else
                {
                    tvEstadoProfesional.setText("offline");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

