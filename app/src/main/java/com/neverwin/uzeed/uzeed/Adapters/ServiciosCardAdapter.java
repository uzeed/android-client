package com.neverwin.uzeed.uzeed.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.neverwin.uzeed.uzeed.Model.Imagen;
import com.neverwin.uzeed.uzeed.Model.Profesional;
import com.neverwin.uzeed.uzeed.Model.Servicio;
import com.neverwin.uzeed.uzeed.Network.SingletonRequestQueue;
import com.neverwin.uzeed.uzeed.R;
import com.neverwin.uzeed.uzeed.Utils.LoadImage;
import com.neverwin.uzeed.uzeed.Utils.Utils;
import com.neverwin.uzeed.uzeed.ViewHolder.ProfetionalCardViewHolder;
import com.neverwin.uzeed.uzeed.ViewHolder.ServicioCardViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiciosCardAdapter extends RecyclerView.Adapter<ServicioCardViewHolder> {

    Context mContext;
    List<Servicio> mData;
    ArrayList<Imagen> imagenes;
    View v;
    private RecyclerView mRecyclerView;


    public ServiciosCardAdapter(Context mContext, List<Servicio> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ServicioCardViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.card_item_servicio, parent, false);
        return new ServicioCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ServicioCardViewHolder holder, final int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        final Servicio service = (Servicio) mData.get(position);
        holder.tv_estado.setText(mData.get(position).getEstado());
        holder.tv_fecha.setText(sdf.format(mData.get(position).getFecha()));
        holder.tv_Alias.setText(mData.get(position).getProfesional().getAlias());
        holder.tv_Categoria.setText(mData.get(position).getProfesional().ObtenerCategorias());
        //holder.iv_profile_photo.setImageBitmap(mData.get(position).getProfesional().ObtenerImagenPortada());
        LoadImage loadImage = new LoadImage(holder.iv_profile_photo,false);
        loadImage.execute(mData.get(position).getProfesional().getImgPerfilPath());
        holder.iv_profile_photo.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.servicio = service;
        holder.mContext = mContext;
        holder.viewInflater=v;

        if(mData.get(position).getEstado().equalsIgnoreCase("CALIFICADO")||
                mData.get(position).getEstado().equalsIgnoreCase("CANCELADO")||
                mData.get(position).getEstado().equalsIgnoreCase("RECHAZADO")||
                mData.get(position).getEstado().equalsIgnoreCase("SOLICITADO")) {
            holder.btnCalificar.setVisibility(View.GONE);
        }
        else
        {
            holder.btnCalificar.setVisibility(View.VISIBLE);
        }
        if(mData.get(position).getEstado().equalsIgnoreCase("SOLICITADO")){
            holder.btnCancelar.setVisibility(View.VISIBLE);
        }
        else {
            holder.btnCancelar.setVisibility(View.GONE);
        }

        holder.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String urlCalificar = Utils.getUrl() +  "solicitud/ProcesarSolicitud?id=" + service.getId() +
                        "&estado=CANCELADO"+
                        "&idSolicita=" + Utils.getUsuario().getId()+
                        "&isProfesional=false";

                JsonObjectRequest jsonArrayRequestProfesional = new JsonObjectRequest
                        (Request.Method.POST, urlCalificar, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                holder.btnCancelar.setVisibility(View.GONE);
                                holder.btnCancelar.setEnabled(false);
                                holder.tv_estado.setText("CANCELADO");
                                mData.get(position).setEstado("CANCELADO");
                                notifyItemChanged(position);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast= Toast.makeText(mContext,mContext.getResources().getString(R.string.cancelacion_error),Toast.LENGTH_SHORT);
                                View viewToast = toast.getView();
                                viewToast.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
                                toast.setGravity( Gravity.CENTER_VERTICAL, 0, 0);
                                toast.show();
                            }
                        }) {
                    @Override
                    public Map getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("Authorization", "Bearer " + Utils.getUsuario().getToken());
                        return headers;
                    }
                };
                SingletonRequestQueue.getInstance(mContext).addToRequestQueue(jsonArrayRequestProfesional);
            }
        });
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

