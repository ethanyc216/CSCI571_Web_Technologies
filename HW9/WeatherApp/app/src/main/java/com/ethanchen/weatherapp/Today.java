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

import java.util.HashMap;
import java.util.Map;

public class Today extends Fragment {

    private static final String TAG = "Today";
    private Map<String, String> icons;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today, container, false);

        icons = new HashMap<>();
        icons.put("clear-day","clear day");
        icons.put("clear-night","clear night");
        icons.put("rain","rain");
        icons.put("snow","snow");
        icons.put("sleet","sleet");
        icons.put("wind","wind");
        icons.put("fog","fog");
        icons.put("cloudy","cloudy");
        icons.put("partly-cloudy-day","cloudy day");
        icons.put("partly-cloudy-night","cloudy night");

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

            String currentlyWindSpeedString = "N/A";
            if (currentlyObj.has("windSpeed")) {
                double currentlyWindSpeed = currentlyObj.getDouble("windSpeed");
                currentlyWindSpeedString = String.format("%.2f", currentlyWindSpeed) + " mph";
            }

            String currentlyPressureString = "N/A";
            if (currentlyObj.has("pressure")) {
                double currentlyPressure = currentlyObj.getDouble("pressure");
                currentlyPressureString = String.format("%.2f", currentlyPressure) + " mb";
            }
            String currentlyRainString = "N/A";
            if (currentlyObj.has("precipIntensity")) {
                double currentlyRain = currentlyObj.getDouble("precipIntensity");
                currentlyRainString = String.format("%.2f", currentlyRain) + " mmph";
            }

            String currentlyTemperatureString = "N/A";
            if (currentlyObj.has("temperature")) {
                int currentlyTemperature = (int) Math.round(currentlyObj.getDouble("temperature"));
                currentlyTemperatureString = currentlyTemperature + "°F";
            }
            String currentlyIcon = "N/A";
            if (currentlyObj.has("icon")) {
                currentlyIcon = currentlyObj.getString("icon");
            }
            String currentlyHumidityString = "N/A";
            if (currentlyObj.has("humidity")) {
                int currentlyHumidity = (int) Math.round(currentlyObj.getDouble("humidity") * 100);
                currentlyHumidityString = currentlyHumidity + "%";
            }

            String currentlyVisibilityString = "N/A";
            if (currentlyObj.has("visibility")) {
                double currentlyVisibility = currentlyObj.getDouble("visibility");
                currentlyVisibilityString = String.format("%.2f", currentlyVisibility) + " km";
            }
            String currentlyCloudCoverString = "N/A";
            if (currentlyObj.has("cloudCover")) {
                int currentlyCloudCover = (int) Math.round(currentlyObj.getDouble("cloudCover") * 100);
                currentlyCloudCoverString = currentlyCloudCover + "%";
            }
            String currentlyOzoneString = "N/A";
            if (currentlyObj.has("ozone")) {
                double currentlyOzone = currentlyObj.getDouble("ozone");
                currentlyOzoneString = String.format("%.2f", currentlyOzone) + " DU";
            }

            card_wind_val.setText(currentlyWindSpeedString);
            card_pressure_val.setText(currentlyPressureString);
            card_rain_val.setText(currentlyRainString);

            card_temperature_val.setText(currentlyTemperatureString);
            if (icons.containsKey(currentlyIcon)) {
                card_icon_val.setText(icons.get(currentlyIcon));
            } else {
                card_icon_val.setText("clear day");
            }
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
            //getActivity().findViewById(R.id.progressBar_lay2).setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;

    }
}
