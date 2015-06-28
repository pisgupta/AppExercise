package com.demo.apputil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Pankaj on 6/25/2015.
 */
public class FileUtils {


    public static void close(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(OutputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isNetWorkAvailable(Context mContext) {
        boolean flag = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if(info!=null){
                flag =  info.isConnectedOrConnecting();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return flag;
    }

}
