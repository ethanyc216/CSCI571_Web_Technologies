package com.ethanchen.weatherapp;

public interface CityProvider {
    public String getCityForPosition(int position);
    public int getCount();
}