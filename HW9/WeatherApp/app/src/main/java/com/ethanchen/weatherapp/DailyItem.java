package com.ethanchen.weatherapp;

public class DailyItem {
    private String date;
    private int icon;
    private String temperatureLow;
    private String temperatureHigh;

    public DailyItem(String date, int icon, String temperatureLow, String temperatureHigh) {
        this.date = date;
        this.icon = icon;
        this.temperatureLow = temperatureLow;
        this.temperatureHigh = temperatureHigh;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTemperatureLow() {
        return temperatureLow;
    }

    public void setTemperatureLow(String temperatureLow) {
        this.temperatureLow = temperatureLow;
    }

    public String getTemperatureHigh() {
        return temperatureHigh;
    }

    public void setTemperatureHigh(String temperatureHigh) {
        this.temperatureHigh = temperatureHigh;
    }
}
