package com.neverwin.uzeed.uzeed.Activities;

import android.app.Application;

import com.neverwin.uzeed.uzeed.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MiAplicacion extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fuentes/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
