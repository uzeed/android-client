package com.neverwin.uzeed.uzeed.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.neverwin.uzeed.uzeed.Adapters.ImageAdapter;
import com.neverwin.uzeed.uzeed.Model.Imagen;
import com.neverwin.uzeed.uzeed.R;

import java.util.ArrayList;

public class ImageSliderActivity extends AppCompatActivity {
    String[] imagenes;
    int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);
        ViewPager viewPager = findViewById(R.id.viewPager);


        //getSupportActionBar().hide();
        Bundle objEnviado = getIntent().getExtras();
        if (objEnviado != null) {
            imagenes = (String[]) objEnviado.getSerializable("Imagenes");
            posicion = (int) objEnviado.getSerializable("imagenSeleccionada");
        }


        ImageAdapter adapter = new ImageAdapter(this, imagenes);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(posicion - 1);

    }
}


