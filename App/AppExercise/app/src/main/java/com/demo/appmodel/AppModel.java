package com.demo.appmodel;

import android.graphics.Bitmap;

/**
 * Created by gupta on 6/27/2015.
 */
public class AppModel {
    private Bitmap bitmapImage;
    private String iconpath;
    private String imagepath;

    public AppModel() {
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public String getIconpath() {
        return iconpath;
    }

    public void setIconpath(String iconpath) {
        this.iconpath = iconpath;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
