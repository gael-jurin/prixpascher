package org.nuvola.mobile.prixpascher.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetCheckTask extends AsyncTask<Void,Void,Void> {

    public boolean connection;
    Context ctx;

    public InternetCheckTask(Context context) {
        this.ctx = context;
    }

    public InternetCheckTask() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        if (isNetworkAvailable(this.ctx)) {

            Log.d("NetworkAvailable", "TRUE");
            if (connectGoogle()) {

                Log.d("GooglePing", "TRUE");
                connection = true;
            } else {

                Log.d("GooglePing", "FALSE");
                connection = false;
            }
        } else {

            connection = false;
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean connectGoogle() {
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(10000);
            urlc.connect();
            return (urlc.getResponseCode() == 200);

        } catch (IOException e) {

            Log.d("GooglePing", "IOEXCEPTION");
            e.printStackTrace();
            return false;
        }
    }
}
