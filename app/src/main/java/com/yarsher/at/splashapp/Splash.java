package com.yarsher.at.splashapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import java.io.InputStream;

public class Splash extends AppCompatActivity {

    int i;
    String getURL = "http://127.0.0.1:8980/myserver/bigfile.txt";
    String ip204 = "http://127.0.0.1:8980/myserver/return204.php";
    public static int connectTimeout = 10000;
    public static int readTimeout = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        if (haveNetworkConnection()){
            new HttpDownLoad().execute();
        }else {
            noConnection();
        }
    }

    public class HttpDownload extends AsyncTask<Void, String, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            publicProgress("Connecting", "0");
            String fromServer = "";
            int BUFFER_SIZE = 2000;
            float fsize = 890000;
            InputStream in = null;

            try{

            }


            return null;
        }
    }





}
