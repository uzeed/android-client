package com.neverwin.uzeed.uzeed.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.neverwin.uzeed.uzeed.Fragments.ChatsFragment;
import com.neverwin.uzeed.uzeed.Fragments.FavoritosFragment;
import com.neverwin.uzeed.uzeed.Fragments.HomeFragment;
import com.neverwin.uzeed.uzeed.Fragments.ServiciosFragment;
import com.neverwin.uzeed.uzeed.Model.UserCategoria;
import com.neverwin.uzeed.uzeed.R;

import org.w3c.dom.Text;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        selectItem(0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id)
        {
            case R.id.homeMenu:
                selectItem(0);
                break;
            case R.id.favoritosMenu:
                selectItem(1);
                break;
            case R.id.pendientesMenu:
                selectItem(2);
                break;
            case R.id.messageMenu:
                selectItem(3);
                break;
        }
        return true;
    }

    private void selectItem(int position) {
        UserCategoria userCategoria = new UserCategoria();
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:

                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                setTitle(R.string.uzeed);
                break;
            case 1:
                FavoritosFragment favoritosFragment = new FavoritosFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, favoritosFragment).commit();
                break;
            case 2:
                ServiciosFragment srvFragment = new ServiciosFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, srvFragment).commit();
                break;
            case 3:
                ChatsFragment chatsFragment= new ChatsFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, chatsFragment).commit();
                break;
        }
    }


}