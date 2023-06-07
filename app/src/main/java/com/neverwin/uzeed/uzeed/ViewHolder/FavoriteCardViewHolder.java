package com.neverwin.uzeed.uzeed.ViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.neverwin.uzeed.uzeed.Activities.CalificarActivity;
import com.neverwin.uzeed.uzeed.Activities.MensajesActivity;
import com.neverwin.uzeed.uzeed.Activities.PerfilActivity;
import com.neverwin.uzeed.uzeed.Model.Favorito;
import com.neverwin.uzeed.uzeed.Model.Servicio;
import com.neverwin.uzeed.uzeed.R;


public class FavoriteCardViewHolder extends RecyclerView.ViewHolder  {
    public ImageView iv_profile_photo_favorito;
    public TextView tv_AliasFavorito, tv_CategoriaFavorito;
    public RatingBar ratingBarFavorito;
    public Favorito favorito;

    public FavoriteCardViewHolder(View itemView){
        super(itemView);

        iv_profile_photo_favorito = (ImageView) itemView.findViewById(R.id.favorite_profile_photo);
        tv_AliasFavorito= itemView.findViewById(R.id.txt_favorite_Alias);
        tv_CategoriaFavorito= itemView.findViewById(R.id.txt_favorite_Categorias);
        ratingBarFavorito= (RatingBar)itemView.findViewById(R.id.ratb_Calificacion_Favorite);

        final Button btnPerfil = (Button) itemView.findViewById(R.id.btn_favorite_VerPerfil);
        final Button btnEnviarMEnsaje = (Button) itemView.findViewById(R.id.btn_favorite_EnviarMensaje);

        iv_profile_photo_favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(iv_profile_photo_favorito.getContext(), PerfilActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("profesional", favorito.getProfesional());
                intent.putExtras(bundle);
                iv_profile_photo_favorito.getContext().startActivity(intent);
            }
        });
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(btnPerfil.getContext(), PerfilActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("profesional", favorito.getProfesional());

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
                bundle.putSerializable("profesional", favorito.getProfesional());
                intent.putExtras(bundle);
                btnEnviarMEnsaje.getContext().startActivity(intent);
            }
        });
    }
}