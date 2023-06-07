package com.neverwin.uzeed.uzeed.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.neverwin.uzeed.uzeed.R;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);

        getSupportActionBar().hide();

        //getSupportActionBar().hide();

        new DisplaySplash().execute();

    }


    private class DisplaySplash extends AsyncTask<Void, Integer, Integer> {
        protected Integer doInBackground(Void... params) {
            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 0;
        }

        protected void onPostExecute(Integer a) {
            Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
            //Intent intent = new Intent(LaunchActivity.this,SeekActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
