package com.neverwin.uzeed.uzeed.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.neverwin.uzeed.uzeed.Activities.CalificarActivity;
import com.neverwin.uzeed.uzeed.Activities.MensajesActivity;
import com.neverwin.uzeed.uzeed.Activities.PerfilActivity;
import com.neverwin.uzeed.uzeed.Model.Servicio;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ServicioCardViewHolder extends RecyclerView.ViewHolder  {
    public ImageView iv_profile_photo;
    public TextView tv_Alias, tv_Categoria, tv_fecha, tv_estado;
    public Servicio servicio;
    public final Button btnCalificar, btnCancelar;
    public ProgressBar progressbar;
    public Context mContext;
    public View viewInflater;


    public ServicioCardViewHolder(View itemView){
        super(itemView);

        iv_profile_photo = (ImageView) itemView.findViewById(R.id.servicios_profile_photo);
        tv_Alias=itemView.findViewById(R.id.txt_servicios_Alias);
        tv_Categoria=itemView.findViewById(R.id.txt_servicios_Categorias);
        tv_fecha=itemView.findViewById(R.id.txt_servicios_Fecha);
        tv_estado=itemView.findViewById(R.id.txt_servicios_Estado);
        progressbar= itemView.findViewById(R.id.servicios_progressbar);

        final Button btnPerfil = (Button) itemView.findViewById(R.id.btn_Servicios_VerPerfil);
        final Button btnEnviarMEnsaje = (Button) itemView.findViewById(R.id.btn_Servicios_EnviarMensaje);
        btnCalificar = (Button) itemView.findViewById(R.id.btn_Servicios_Calificar);
        btnCancelar=itemView.findViewById(R.id.btn_Servicios_Cancelar);
        if(servicio != null) {
            if (servicio.getEstado() == "APROBADO" || servicio.getEstado() == "PENDIENTECALIFICACIO") {
                btnCalificar.setEnabled(true);
                btnCalificar.setVisibility(View.GONE);
                btnCancelar.setVisibility(View.GONE);
                this.tv_estado.setText("CANCELADO");
            }
            if(servicio.getEstado()=="SOLICITADO"){
                btnCalificar.setVisibility(View.GONE);
                btnCalificar.setEnabled(false);
                btnCancelar.setVisibility(View.VISIBLE);
                btnCancelar.setEnabled(true);
            }
        }

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(btnPerfil.getContext(), PerfilActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("profesional", servicio.getProfesional());

                intent.putExtras(bundle);

                btnPerfil.getContext().startActivity(intent);
            }
        });

        btnEnviarMEnsaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(btnPerfil.getContext(), MensajesActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("profesional", servicio.getProfesional());
                intent.putExtras(bundle);
                btnEnviarMEnsaje.getContext().startActivity(intent);
            }
        });

        btnCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(btnCalificar.getContext(), CalificarActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("profesional", servicio.getProfesional());
                bundle.putSerializable("idSolicitud",servicio.getId());
                intent.putExtras(bundle);
                btnCalificar.getContext().startActivity(intent);
            }
        });

    }
}