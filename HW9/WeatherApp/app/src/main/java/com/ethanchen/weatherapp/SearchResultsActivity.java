package com.ethanchen.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchResultsActivity extends AppCompatActivity {

    private Intent intent;
    private String cityName;
    private JSONObject weatherJson;
    private String cityTemperature;

    private ImageView search_first_card_icon;
    private TextView search_first_card_temperature;
    private TextView search_first_card_summary;
    private TextView search_first_card_city;

    private TextView search_second_card_humidity;
    private TextView search_second_card_wind;
    private TextView search_second_card_visibility;
    private TextView search_second_card_pressure;

    private ListView search_third_card_list;

    private DateFormat dateFormat;

    private double lat = 0.0;
    private double lng = 0.0;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        cityName = intent.getStringExtra("cityName");

        setTitle(cityName);

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        search_first_card_icon = findViewById(R.id.search_first_card_icon);
        search_first_card_temperature = findViewById(R.id.search_first_card_temperature);
        search_first_card_summary = findViewById(R.id.search_first_card_summary);
        search_first_card_city = findViewById(R.id.search_first_card_city);

        search_second_card_humidity = findViewById(R.id.search_second_card_humidity);
        search_second_card_wind = findViewById(R.id.search_second_card_wind);
        search_second_card_visibility = findViewById(R.id.search_second_card_visibility);
        search_second_card_pressure = findViewById(R.id.search_second_card_pressure);

        search_third_card_list = findViewById(R.id.search_third_card_list);

        search_first_card_city.setText(cityName);

        requestQueue = Volley.newRequestQueue(this);
        jsonParseCity(cityName);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void jsonParseCity(String cityName) {
        String url = "http://csci571hw7nodejs-env.huttzh528b.us-east-2.elasticbeanstalk.com/geoCity?city="+cityName;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray resultsObj = response.getJSONArray("results");
                    JSONObject itemInfo = resultsObj.getJSONObject(0);
                    JSONObject geometryObj = itemInfo.getJSONObject("geometry");
                    JSONObject locationObj = geometryObj.getJSONObject("location");

                    lat = locationObj.getDouble("lat");
                    lng = locationObj.getDouble("lng");

                    jsonParse(lat, lng);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

    }

    private void jsonParse(double lat, double lng) {
        String url = "http://csci571hw7nodejs-env.huttzh528b.us-east-2.elasticbeanstalk.com/weather?latitude="+String.valueOf(lat)+"&longitude="+String.valueOf(lng);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    weatherJson = response;
                    JSONObject currentlyObj = response.getJSONObject("currently");
                    JSONObject dailyObj = response.getJSONObject("daily");

                    String currentlyIcon = currentlyObj.getString("icon");
                    int currentlyTemperature = (int) Math.round(currentlyObj.getDouble("temperature"));
                    String currentlyTemperatureString = currentlyTemperature + "°F";
                    cityTemperature = currentlyTemperatureString;
                    String currentlySummary = currentlyObj.getString("summary");

                    int currentlyHumidity =  (int) Math.round(currentlyObj.getDouble("humidity") * 100);
                    String currentlyHumidityString = currentlyHumidity + "%";
                    double currentlyWindSpeed = currentlyObj.getDouble("windSpeed");
                    String currentlyWindSpeedString =  String.format("%.2f", currentlyWindSpeed) + " mph";
                    double currentlyVisibility = currentlyObj.getDouble("visibility");
                    String currentlyVisibilityString =  String.format("%.2f", currentlyVisibility) + " km";
                    double currentlyPressure = currentlyObj.getDouble("pressure");
                    String currentlyPressureString =  String.format("%.2f", currentlyPressure) + " mb";

                    JSONArray dailyDataArray = dailyObj.getJSONArray("data");
                    //ArrayList<String> dailyDate = new ArrayList<>();
                    //ArrayList<String> dailyIcon = new ArrayList<>();
                    //ArrayList<String> dailyTemperatureLow = new ArrayList<>();
                    //ArrayList<String> dailyTemperatureHigh = new ArrayList<>();
                    ArrayList<DailyItem> dailyItems = new ArrayList<>();

                    for (int i = 0; i < dailyDataArray.length(); i++) {
                        JSONObject dayInfo = dailyDataArray.getJSONObject(i);
                        long dayTime = dayInfo.getInt("time");
                        Date date = new Date(dayTime * 1000);
                        String dateString = dateFormat.format(date);
                        String dayIcon = dayInfo.getString("icon");
                        int dayTemperatureLow = (int) Math.round(dayInfo.getDouble("temperatureLow"));
                        String dayTemperatureLowString =  String.valueOf(dayTemperatureLow);
                        int daytemperatureHigh = (int) Math.round(dayInfo.getDouble("temperatureHigh"));
                        String daytemperatureHighString =  String.valueOf(daytemperatureHigh);
                        //dailyDate.add(dateString);
                        //dailyIcon.add(dayIcon);
                        //dailyTemperatureLow.add(dayTemperatureLowString);
                        //dailyTemperatureHigh.add(daytemperatureHighString);

                        if (dayIcon.equals("clear-night")) {
                            DailyItem dailyItem = new DailyItem(dateString, R.drawable.clear_night, dayTemperatureLowString, daytemperatureHighString);
                            dailyItems.add(dailyItem);
                        } else if (dayIcon.equals("rain")) {
                            DailyItem dailyItem = new DailyItem(dateString, R.drawable.rain, dayTemperatureLowString, daytemperatureHighString);
                            dailyItems.add(dailyItem);
                        } else if (dayIcon.equals("sleet")) {
                            DailyItem dailyItem = new DailyItem(dateString, R.drawable.sleet, dayTemperatureLowString, daytemperatureHighString);
                            dailyItems.add(dailyItem);
                        } else if (dayIcon.equals("snow")) {
                            DailyItem dailyItem = new DailyItem(dateString, R.drawable.snow, dayTemperatureLowString, daytemperatureHighString);
                            dailyItems.add(dailyItem);
                        } else if (dayIcon.equals("wind")) {
                            DailyItem dailyItem = new DailyItem(dateString, R.drawable.wind, dayTemperatureLowString, daytemperatureHighString);
                            dailyItems.add(dailyItem);
                        } else if (dayIcon.equals("fog")) {
                            DailyItem dailyItem = new DailyItem(dateString, R.drawable.fog, dayTemperatureLowString, daytemperatureHighString);
                            dailyItems.add(dailyItem);
                        } else if (dayIcon.equals("cloudy")) {
                            DailyItem dailyItem = new DailyItem(dateString, R.drawable.cloudy, dayTemperatureLowString, daytemperatureHighString);
                            dailyItems.add(dailyItem);
                        } else if (dayIcon.equals("partly-cloudy-night")) {
                            DailyItem dailyItem = new DailyItem(dateString, R.drawable.partly_cloudy_night, dayTemperatureLowString, daytemperatureHighString);
                            dailyItems.add(dailyItem);
                        } else if (dayIcon.equals("partly-cloudy-day")) {
                            DailyItem dailyItem = new DailyItem(dateString, R.drawable.partly_cloudy_day, dayTemperatureLowString, daytemperatureHighString);
                            dailyItems.add(dailyItem);
                        } else {
                            DailyItem dailyItem = new DailyItem(dateString, R.drawable.clear_day, dayTemperatureLowString, daytemperatureHighString);
                            dailyItems.add(dailyItem);
                        }
                    }


                    if (currentlyIcon.equals("clear-night")) {
                        search_first_card_icon.setImageResource(R.drawable.clear_night);
                    } else if (currentlyIcon.equals("rain")) {
                        search_first_card_icon.setImageResource(R.drawable.rain);
                    } else if (currentlyIcon.equals("sleet")) {
                        search_first_card_icon.setImageResource(R.drawable.sleet);
                    } else if (currentlyIcon.equals("snow")) {
                        search_first_card_icon.setImageResource(R.drawable.snow);
                    } else if (currentlyIcon.equals("wind")) {
                        search_first_card_icon.setImageResource(R.drawable.wind);
                    } else if (currentlyIcon.equals("fog")) {
                        search_first_card_icon.setImageResource(R.drawable.fog);
                    } else if (currentlyIcon.equals("cloudy")) {
                        search_first_card_icon.setImageResource(R.drawable.cloudy);
                    } else if (currentlyIcon.equals("partly-cloudy-night")) {
                        search_first_card_icon.setImageResource(R.drawable.partly_cloudy_night);
                    } else if (currentlyIcon.equals("partly-cloudy-day")) {
                        search_first_card_icon.setImageResource(R.drawable.partly_cloudy_day);
                    } else {
                        search_first_card_icon.setImageResource(R.drawable.clear_day);
                    }

                    search_first_card_temperature.setText(currentlyTemperatureString);
                    search_first_card_summary.setText(currentlySummary);

                    search_second_card_humidity.setText(currentlyHumidityString);
                    search_second_card_wind.setText(currentlyWindSpeedString);
                    search_second_card_visibility.setText(currentlyVisibilityString);
                    search_second_card_pressure.setText(currentlyPressureString);

                    DailyListAdapter adapter = new DailyListAdapter(SearchResultsActivity.this, R.layout.third_card_list_item_layout, dailyItems);
                    search_third_card_list.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

    }

    public void goToTabs(View view) {
        Intent myIntent = new Intent(this, TabsActivity.class);
        //TODO
        myIntent.putExtra("weatherJson", weatherJson.toString());
        myIntent.putExtra("cityName", cityName);
        myIntent.putExtra("cityTemperature", cityTemperature);
        startActivity(myIntent);
    }

}
