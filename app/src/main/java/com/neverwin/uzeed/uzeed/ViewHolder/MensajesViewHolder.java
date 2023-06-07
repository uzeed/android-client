package com.neverwin.uzeed.uzeed.ViewHolder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neverwin.uzeed.uzeed.R;

import org.w3c.dom.Text;

public class MensajesViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivProfile;
    public LinearLayout llItemMensaje;
    public CardView cvMensajes;
    public TextView tvMensaje;
    public TextView tvHora;
    public LinearLayout linearLayout;
    public TextView tvInitialLetter;
    public MensajesViewHolder(View itemView) {
        super(itemView);
        cvMensajes = (CardView) itemView.findViewById(R.id.cv_Mensajes);
        linearLayout = (LinearLayout)itemView.findViewById(R.id.ll_MensajeRecibido) ;
        tvMensaje = (TextView) itemView.findViewById(R.id.tv_Mensaje);
        tvHora=(TextView) itemView.findViewById(R.id.tv_HoraMensaje);
        llItemMensaje = (LinearLayout) itemView.findViewById(R.id.ll_item_mensaje);
        ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
        tvInitialLetter = (TextView) itemView.findViewById(R.id.tv_initial_letter);
    }
}
