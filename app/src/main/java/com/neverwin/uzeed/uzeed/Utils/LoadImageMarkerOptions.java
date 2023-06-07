package com.neverwin.uzeed.uzeed.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.neverwin.uzeed.uzeed.R;

import java.io.InputStream;
import java.net.URL;

public class LoadImageMarkerOptions  extends AsyncTask<String, Integer, BitmapDescriptor> {


        MarkerOptions markerOptions;
        Context context;

        public LoadImageMarkerOptions(MarkerOptions markerOptions, Context context) {
            this.markerOptions = markerOptions;
            this.context = context;
        }

        @Override
        protected BitmapDescriptor doInBackground(String... url) {

            try {
                //InputStream is = (InputStream) new URL("http://f1inversiones-001-site20.btempurl.com/img/prof/8_perfil.jpg").getContent();
                //String urlSeleccionada = "http://" + url[0];
                String urlSeleccionada = url[0];
                InputStream is = (InputStream) new URL(urlSeleccionada).getContent();
                Drawable d = Drawable.createFromStream(is, "image");
                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(Utils.getBitmapFromLayout(context, R.layout.image_circle,bitmap));
                return icon;

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
        protected void onPostExecute(BitmapDescriptor imageForm) {

            markerOptions.icon(imageForm);
        }

        @Override
        protected void onCancelled() {
            Log.v("LoadImage","Task Cancelled");
        }
    }

