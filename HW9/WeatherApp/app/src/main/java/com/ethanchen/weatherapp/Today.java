package com.ethanchen.weatherapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Today extends Fragment {

    private static final String TAG = "Today";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today, container, false);

        TextView card_wind_val = view.findViewById(R.id.card_wind_val);
        TextView card_pressure_val = view.findViewById(R.id.card_pressure_val);
        TextView card_rain_val = view.findViewById(R.id.card_rain_val);

        TextView card_temperature_val = view.findViewById(R.id.card_temperature_val);
        TextView card_icon_val = view.findViewById(R.id.card_icon_val);
        TextView card_humidity_val = view.findViewById(R.id.card_humidity_val);

        TextView card_visibility_val = view.findViewById(R.id.card_visibility_val);
        TextView card_cloud_cover_val = view.findViewById(R.id.card_cloud_cover_val);
        TextView card_ozone_val = view.findViewById(R.id.card_ozone_val);

        ImageView card_icon_icon = view.findViewById(R.id.card_icon_icon);

        String weatherJsonString = getArguments().getString("weatherJson");
        String cityName = getArguments().getString("cityName");
        JSONObject weatherJson;
        try {
            weatherJson = new JSONObject(weatherJsonString);
            JSONObject currentlyObj = weatherJson.getJSONObject("currently");

            double currentlyWindSpeed = currentlyObj.getDouble("windSpeed");
            String currentlyWindSpeedString =  String.format("%.2f", currentlyWindSpeed) + " mph";
            double currentlyPressure = currentlyObj.getDouble("pressure");
            String currentlyPressureString =  String.format("%.2f", currentlyPressure) + " mb";
            double currentlyRain = currentlyObj.getDouble("precipIntensity");
            String currentlyRainString =  String.format("%.2f", currentlyRain) + " mmph";

            int currentlyTemperature = (int) Math.round(currentlyObj.getDouble("temperature"));
            String currentlyTemperatureString = currentlyTemperature + "°F";
            String currentlyIcon = currentlyObj.getString("icon");
            int currentlyHumidity =  (int) Math.round(currentlyObj.getDouble("humidity") * 100);
            String currentlyHumidityString = currentlyHumidity + "%";

            double currentlyVisibility = currentlyObj.getDouble("visibility");
            String currentlyVisibilityString =  String.format("%.2f", currentlyVisibility) + " km";
            int currentlyCloudCover =  (int) Math.round(currentlyObj.getDouble("cloudCover") * 100);
            String currentlyCloudCoverString = currentlyCloudCover + "%";
            double currentlyOzone = currentlyObj.getDouble("ozone");
            String currentlyOzoneString =  String.format("%.2f", currentlyOzone) + " DU";

            card_wind_val.setText(currentlyWindSpeedString);
            card_pressure_val.setText(currentlyPressureString);
            card_rain_val.setText(currentlyRainString);

            card_temperature_val.setText(currentlyTemperatureString);
            card_icon_val.setText(currentlyIcon);
            card_humidity_val.setText(currentlyHumidityString);

            card_visibility_val.setText(currentlyVisibilityString);
            card_cloud_cover_val.setText(currentlyCloudCoverString);
            card_ozone_val.setText(currentlyOzoneString);

            if (currentlyIcon.equals("clear-night")) {
                card_icon_icon.setImageResource(R.drawable.clear_night);
            } else if (currentlyIcon.equals("rain")) {
                card_icon_icon.setImageResource(R.drawable.rain);
            } else if (currentlyIcon.equals("sleet")) {
                card_icon_icon.setImageResource(R.drawable.sleet);
            } else if (currentlyIcon.equals("snow")) {
                card_icon_icon.setImageResource(R.drawable.snow);
            } else if (currentlyIcon.equals("wind")) {
                card_icon_icon.setImageResource(R.drawable.wind);
            } else if (currentlyIcon.equals("fog")) {
                card_icon_icon.setImageResource(R.drawable.fog);
            } else if (currentlyIcon.equals("cloudy")) {
                card_icon_icon.setImageResource(R.drawable.cloudy);
            } else if (currentlyIcon.equals("partly-cloudy-night")) {
                card_icon_icon.setImageResource(R.drawable.partly_cloudy_night);
            } else if (currentlyIcon.equals("partly-cloudy-day")) {
                card_icon_icon.setImageResource(R.drawable.partly_cloudy_day);
            } else {
                card_icon_icon.setImageResource(R.drawable.clear_day);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;

    }
}
