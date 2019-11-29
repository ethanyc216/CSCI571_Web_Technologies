package com.ethanchen.weatherapp;

public class PhotoItem {
    private int mImageResource;
    private String text;

    public PhotoItem(int imageResource, String text) {
        mImageResource = imageResource;
        this.text = text;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public void setmImageResource(String text) {

        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(int ImageResource) {

        mImageResource = ImageResource;
    }
}
