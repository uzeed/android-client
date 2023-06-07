package com.neverwin.uzeed.uzeed.Utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.neverwin.uzeed.uzeed.R;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by mac on 13/5/18.
 */

public class LoadImage extends AsyncTask<String, Integer, Bitmap> {

    private final Boolean thumbnail;
    ImageView imageView;

    public LoadImage(ImageView imageView,Boolean thumbnail) {
        this.imageView = imageView;
        this.thumbnail = thumbnail;
    }

    @Override
    protected Bitmap doInBackground(String... url) {

        try {

            //String urlSeleccionada = "http://" + url[0];
            String urlSeleccionada = url[0];
            InputStream is = (InputStream) new URL(urlSeleccionada).getContent();
            //InputStream is = (InputStream) new URL("http://f1inversiones-001-site20.btempurl.com/img/prof/8_perfil.jpg").getContent();
            Drawable d = Drawable.createFromStream(is, "image");
            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();

            if (thumbnail)
            {
                bitmap = ThumbnailUtils.extractThumbnail(bitmap,50,50);
            }
            return bitmap;

        } catch (Exception e) {
            Log.v("LoadImage","Exception: " + e);
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Bitmap imageForm) {

        imageView.setImageBitmap(imageForm);
    }

    @Override
    protected void onCancelled() {
        Log.v("LoadImage","Task Cancelled");
    }
}


