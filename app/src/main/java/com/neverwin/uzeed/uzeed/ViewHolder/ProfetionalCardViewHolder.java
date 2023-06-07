package com.neverwin.uzeed.uzeed.ViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.neverwin.uzeed.uzeed.Activities.MensajesActivity;
import com.neverwin.uzeed.uzeed.Activities.PerfilActivity;
import com.neverwin.uzeed.uzeed.Model.Profesional;
import com.neverwin.uzeed.uzeed.R;


public class ProfetionalCardViewHolder extends RecyclerView.ViewHolder  {
    public ImageView iv_profile_photo;
    public TextView tv_Alias, tv_Categoria;
    public Profesional profesional;

    public ProfetionalCardViewHolder(View itemView){
        super(itemView);

        iv_profile_photo = (ImageView) itemView.findViewById(R.id.profile_photo);
        tv_Alias=itemView.findViewById(R.id.txt_Alias);
        tv_Categoria=itemView.findViewById(R.id.txt_Categorias);

        final Button btnPerfil = (Button) itemView.findViewById(R.id.btn_VerPerfil);
        final Button btnEnviarMEnsaje = (Button) itemView.findViewById(R.id.btn_EnviarMensaje);


        iv_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(btnPerfil.getContext(), PerfilActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("profesional", profesional);
                intent.putExtras(bundle);
                btnPerfil.getContext().startActivity(intent);
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(btnPerfil.getContext(), PerfilActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("profesional", profesional);
                intent.putExtras(bundle);
                btnPerfil.getContext().startActivity(intent);
            }
        });

        btnEnviarMEnsaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(btnPerfil.getContext(), MensajesActivity.class);
                //Bundle bundle = new Bundle();
                intent.putExtra("profesional", profesional);
                btnEnviarMEnsaje.getContext().startActivity(intent);
            }
        });
    }
}