package com.neverwin.uzeed.uzeed.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.neverwin.uzeed.uzeed.Model.Imagen;
import com.neverwin.uzeed.uzeed.Model.Profesional;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.LoadImage;
import com.neverwin.uzeed.uzeed.Utils.Utils;
import com.neverwin.uzeed.uzeed.ViewHolder.ProfetionalCardViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfetionalCardAdapter extends RecyclerView.Adapter<ProfetionalCardViewHolder> {

    Context mContext;
    List<Profesional> mData;
    ArrayList<Imagen> imagenes;

    public ProfetionalCardAdapter(Context mContext, List<Profesional> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ProfetionalCardViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_item, parent, false);
        return new ProfetionalCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ProfetionalCardViewHolder holder, int position) {
        holder.tv_Alias.setText(mData.get(position).getAlias());
        holder.tv_Categoria.setText(mData.get(position).ObtenerCategorias());
        holder.profesional = mData.get(position);
        //holder.iv_profile_photo.setImageBitmap(mData.get(position).ObtenerImagenPortada());

        LoadImage loadImage = new LoadImage(holder.iv_profile_photo,false);
        loadImage.execute(mData.get(position).getImgPerfilPath());


        holder.iv_profile_photo.setScaleType(ImageView.ScaleType.FIT_XY);
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

