package com.neverwin.uzeed.uzeed.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.neverwin.uzeed.uzeed.Model.Imagen;
import com.neverwin.uzeed.uzeed.R;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    private Context mContext;
    private String[] mImage = new String[]{};
    ArrayList<Imagen> imagenes = new ArrayList<Imagen>();

    public ImageAdapter(Context context, String[] data) {
        mContext = context;
        this.mImage = data;
    }

    @Override
    public int getCount() {
        if (mImage.length > 9)
            return 9;
        return mImage.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setData(String[] data){
        this.mImage=data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String imagen = this.mImage[position];
        byte[] decodeString = Base64.decode(imagen,Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
        imageView.setImageBitmap(decodedByte);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}