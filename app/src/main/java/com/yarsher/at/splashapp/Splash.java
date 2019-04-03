package com.yarsher.at.splashapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
            publishProgress("Connecting", "0");
            String fromServer = "";
            int BUFFER_SIZE = 2000;
            float fsize = 890000;
            InputStream in = null;

            //check reachability
            try {
                URL url = new URL(ip204);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
                conn.setRequestMethod("HEAD");
                in = conn.getInputStream();
                int status = conn.getResponseCode();
                in.close();
                conn.disconnect();
                if (status == HttpURLConnection.HTTP_NO_CONTENT){
                    //server is reachable => initiate the download
                    publishProgress("Reachable", 0);
                    in = OppenHttpConnection(getURL);
                    InputStream isr = new InputStream(in);
                    int charRead;
                    char[] inputBuffer = new char[BUFFER_SIZE];
                    while ((charRead = isr.read(inputBuffer))>0) {
                        //---convert the chars to a String---
                        String readString = String.copyValueOf(inputBuffer, 0, charRead);
                        fromServer += readString;
                        inputBuffer = new char[BUFFER_SIZE];
                    //---update the progress
                        float ratio = (fromServer.length() / fsize) * 100;
                        int num = (int) ratio;
                        publishProgress("Connecting: " + String.valueOf(num) + "%",
                                String.valueOf(num));
                    }
                    in.close();
                } else {
                    publishProgress("Not Reachable", "0");
                    failedReach();
                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                failedDownload();
            }

            publishProgress("Completed","100");


            return null;
        }

        @Override
        protected void onProgressUpdate(String... item) {
            TextView txt = (TextView) findViewById(R.id.text);
            txt.setText(item[0]);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progBar);
            int num = Integer.parseInt(item[1]);
            progressBar.setProgress(num);
        }
    }



    public static InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        if (!(conn instanceof HttpURLConnection)) throw new IOException("Not an HTTP connection");
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            throw new IOException("Error connecting");
        }
        return in;
    }





}
