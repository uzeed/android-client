package com.neverwin.uzeed.uzeed.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.neverwin.uzeed.uzeed.Model.Favorito;
import com.neverwin.uzeed.uzeed.Model.Imagen;
import com.neverwin.uzeed.uzeed.Model.Servicio;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.LoadImage;
import com.neverwin.uzeed.uzeed.ViewHolder.FavoriteCardViewHolder;
import com.neverwin.uzeed.uzeed.ViewHolder.ServicioCardViewHolder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FavoritoCardAdapter extends RecyclerView.Adapter<FavoriteCardViewHolder> {

    Context mContext;
    List<Favorito> mData;

    public FavoritoCardAdapter(Context mContext, List<Favorito> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public FavoriteCardViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_item_favorite, parent, false);
        return new FavoriteCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FavoriteCardViewHolder holder, int position) {
        holder.tv_AliasFavorito.setText(mData.get(position).getProfesional().getAlias());
        holder.tv_CategoriaFavorito.setText(mData.get(position).getProfesional().ObtenerCategorias());
        LoadImage loadImage = new LoadImage(holder.iv_profile_photo_favorito,false);
        loadImage.execute(mData.get(position).getProfesional().getImgPerfilPath());
        //holder.iv_profile_photo_favorito.setImageBitmap(mData.get(position).getProfesional().ObtenerImagenPortada());
        holder.iv_profile_photo_favorito.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.ratingBarFavorito.setRating(mData.get(position).getProfesional().getCalificacion());
        holder.favorito = mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private Bitmap loadImagen(JSONArray json) throws JSONException {
        Imagen imagen = new Imagen(json.getJSONObject(0));
        byte[] decodeString = Base64.decode(imagen.getImagen64(),Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
        }
    }

