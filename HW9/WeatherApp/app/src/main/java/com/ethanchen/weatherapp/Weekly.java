package com.ethanchen.weatherapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Weekly extends Fragment {

    private static final String TAG = "Weekly";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weekly, container, false);
        TextView weekly_summary = view.findViewById(R.id.weekly_summary);
        ImageView weekly_icon = view.findViewById(R.id.weekly_icon);
        LineChart weekly_chart = view.findViewById(R.id.weekly_chart);

        String weatherJsonString = getArguments().getString("weatherJson");
        String cityName = getArguments().getString("cityName");
        JSONObject weatherJson;

        try {
            weatherJson = new JSONObject(weatherJsonString);
            JSONObject dailyObj = weatherJson.getJSONObject("daily");

            String weeklySummary = dailyObj.getString("summary");
            weekly_summary.setText(weeklySummary);

            String weeklyIcon = dailyObj.getString("icon");
            if (weeklyIcon.equals("clear-night")) {
                weekly_icon.setImageResource(R.drawable.clear_night);
            } else if (weeklyIcon.equals("rain")) {
                weekly_icon.setImageResource(R.drawable.rain);
            } else if (weeklyIcon.equals("sleet")) {
                weekly_icon.setImageResource(R.drawable.sleet);
            } else if (weeklyIcon.equals("snow")) {
                weekly_icon.setImageResource(R.drawable.snow);
            } else if (weeklyIcon.equals("wind")) {
                weekly_icon.setImageResource(R.drawable.wind);
            } else if (weeklyIcon.equals("fog")) {
                weekly_icon.setImageResource(R.drawable.fog);
            } else if (weeklyIcon.equals("cloudy")) {
                weekly_icon.setImageResource(R.drawable.cloudy);
            } else if (weeklyIcon.equals("partly-cloudy-night")) {
                weekly_icon.setImageResource(R.drawable.partly_cloudy_night);
            } else if (weeklyIcon.equals("partly-cloudy-day")) {
                weekly_icon.setImageResource(R.drawable.partly_cloudy_day);
            } else {
                weekly_icon.setImageResource(R.drawable.clear_day);
            }

            JSONArray dailyDataArray = dailyObj.getJSONArray("data");
            ArrayList<Entry> dailyTemperatureLow = new ArrayList<>();
            ArrayList<Entry> dailyTemperatureHigh = new ArrayList<>();
            for (int i = 0; i < dailyDataArray.length(); i++) {
                JSONObject dayInfo = dailyDataArray.getJSONObject(i);
                int dayTemperatureLow = (int) Math.round(dayInfo.getDouble("temperatureLow"));
                int daytemperatureHigh = (int) Math.round(dayInfo.getDouble("temperatureHigh"));
                dailyTemperatureLow.add(new Entry(i, dayTemperatureLow));
                dailyTemperatureHigh.add(new Entry(i, daytemperatureHigh));
            }
            LineDataSet lineDataSetLow = new LineDataSet(dailyTemperatureLow, "Minimum Temperature");
            LineDataSet lineDataSetHigh = new LineDataSet(dailyTemperatureHigh, "Maximum Temperature");
            lineDataSetLow.setColor(Color.parseColor("#BB86FC"));
            lineDataSetHigh.setColor(Color.parseColor("#F5A719"));

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(lineDataSetLow);
            dataSets.add(lineDataSetHigh);

            LineData data = new LineData(dataSets);
            weekly_chart.setTouchEnabled(false);
            weekly_chart.setData(data);
            weekly_chart.invalidate();
            weekly_chart.getAxisLeft().setTextColor(Color.WHITE);
            weekly_chart.getAxisRight().setTextColor(Color.WHITE);
            weekly_chart.getXAxis().setTextColor(Color.WHITE);
            weekly_chart.getLegend().setTextColor(Color.WHITE);
            weekly_chart.getLegend().setFormSize(15f);
            weekly_chart.getLegend().setTextSize(15f);
            weekly_chart.getXAxis().setGridColor(Color.parseColor("#0a0a0a"));
            weekly_chart.getAxisLeft().setGranularity(10f);
            weekly_chart.getAxisRight().setGranularity(10f);
            weekly_chart.getXAxis().setDrawAxisLine(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;

    }
}
