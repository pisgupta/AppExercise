package com.demo.appparsing;

import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pankaj on 6/26/2015.
 */
public class GetImageURL {

    public static InputStream getImageUrl(String _url) {
        try {
            URL url = new URL(_url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/xml");
            urlConnection.setRequestProperty("Accept", "application/xml ");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream stream = urlConnection.getInputStream();
            return stream;

        } catch (Exception ex) {
            Log.e("GetImageURL", ex.toString());
        }
        return null;
    }
}
