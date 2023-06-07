package com.neverwin.uzeed.uzeed.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.sip.SipSession;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.neverwin.uzeed.uzeed.Model.Mensaje;
import com.neverwin.uzeed.uzeed.Model.Profesional;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.LoadImage;
import com.neverwin.uzeed.uzeed.Utils.Utils;
import com.neverwin.uzeed.uzeed.ViewHolder.MensajesViewHolder;

import java.util.List;

import static android.view.View.GONE;

public class
MensajesAdapter extends RecyclerView.Adapter<MensajesViewHolder> {
    private List<Mensaje> mensajes;
    private Context context;
    private Profesional profesional;
    private Bitmap imageProfesional;
    public MensajesAdapter(List<Mensaje> mensajes, Context context, Profesional profesional){
        this.mensajes=mensajes;
        this.context=context;
        this.profesional = profesional;
        /*byte[] decodedString = Base64.decode(profesional.getImgPerfil(), Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length,options);
        */
        //Bitmap decodedByte = profesional.getImagenProfesional();
        //this.imageProfesional = ThumbnailUtils.extractThumbnail(decodedByte,50,50);


        //decodedByte.recycle();

    }
    @Override
    public MensajesViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_mensajes,parent, false);
        return new MensajesViewHolder(v);
    }
    @Override
    public void onBindViewHolder(MensajesViewHolder holder, int posicion) {
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) holder.cvMensajes.getLayoutParams();
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) holder.llItemMensaje.getLayoutParams();
        LinearLayout.LayoutParams llHora = (LinearLayout.LayoutParams) holder.tvHora.getLayoutParams();
        LinearLayout.LayoutParams llMensaje = (LinearLayout.LayoutParams) holder.tvMensaje.getLayoutParams();

        if (mensajes.get(posicion).getTipoMensaje() == 1) { // Tipo de Mensaje 1 del Cliente
            holder.linearLayout.setBackgroundResource(R.drawable.in_mensaje);
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            fl.gravity = Gravity.RIGHT;
            llHora.gravity = Gravity.RIGHT;
            llMensaje.gravity = Gravity.RIGHT;
            holder.tvHora.setTextColor(Color.WHITE);
            holder.tvMensaje.setGravity(Gravity.RIGHT);
            holder.tvMensaje.setTextColor(Color.WHITE);
            holder.ivProfile.setVisibility(GONE);
            holder.tvInitialLetter.setVisibility(View.VISIBLE);
            holder.tvInitialLetter.setText(Utils.getUsuario().getNombre().substring(0,1).toUpperCase());
        } else {

            holder.linearLayout.setBackgroundResource(R.drawable.out_mensaje);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            fl.gravity = Gravity.LEFT;
            llHora.gravity = Gravity.LEFT;
            llMensaje.gravity = Gravity.LEFT;
            holder.tvMensaje.setGravity(Gravity.LEFT);
            holder.tvMensaje.setTextColor(Color.BLACK);
            holder.tvHora.setTextColor(Color.BLACK);
            holder.tvInitialLetter.setVisibility(GONE);
            /*
            byte[] decodedString = Base64.decode(profesional.getImgPerfil(), Base64.DEFAULT);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length,options);

            holder.ivProfile.setImageBitmap(ThumbnailUtils.extractThumbnail(decodedByte,50,50));
            holder.ivProfile.setVisibility(View.VISIBLE);
            decodedByte.recycle();
            */

            LoadImage loadImage = new LoadImage(holder.ivProfile,true);
            loadImage.execute(this.profesional.getImgPerfilPath());
            //holder.ivProfile.setImageBitmap(this.imageProfesional);
            holder.ivProfile.setVisibility(View.VISIBLE);

        }

        holder.cvMensajes.setLayoutParams(rl);
        holder.llItemMensaje.setLayoutParams(fl);
        holder.tvHora.setLayoutParams(llHora);
        holder.tvMensaje.setLayoutParams(llMensaje);
        holder.tvMensaje.setText(mensajes.get(posicion).getMensaje());
        holder.tvHora.setText(mensajes.get(posicion).getHora());
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            holder.cvMensajes.getBackground().setAlpha(0);
        else
            holder.cvMensajes.setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent));
        }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }


}
