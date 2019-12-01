package com.ethanchen.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class FavoriteCity  extends Fragment {

    private static final String TAG = "FavoriteCity";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private String cityName;
    private JSONObject weatherJson;
    private String cityTemperature;

    private CardView search_first_card;
    private ImageView search_first_card_icon;
    private TextView search_first_card_temperature;
    private TextView search_first_card_summary;
    private TextView search_first_card_city;

    private TextView search_second_card_humidity;
    private TextView search_second_card_wind;
    private TextView search_second_card_visibility;
    private TextView search_second_card_pressure;

    private ListView search_third_card_list;

    private FloatingActionButton fab;

    private DateFormat dateFormat;
    private Context context;

    ViewPager viewPager;

    private double lat = 0.0;
    private double lng = 0.0;
    private RequestQueue requestQueue;

    public static FavoriteCity newInstance(String text) {
        FavoriteCity f = new FavoriteCity(text);
        return f;
    }

    public FavoriteCity() {
    }

    public FavoriteCity(String text) {
        cityName = text;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.favorite_city, container, false);

        //cityName = getArguments().getString("cityName");

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mPreferences.edit();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        context = getActivity().getApplicationContext();

        search_first_card = view.findViewById(R.id.search_first_card);
        search_first_card_icon = view.findViewById(R.id.search_first_card_icon);
        search_first_card_temperature = view.findViewById(R.id.search_first_card_temperature);
        search_first_card_summary = view.findViewById(R.id.search_first_card_summary);
        search_first_card_city = view.findViewById(R.id.search_first_card_city);

        search_second_card_humidity = view.findViewById(R.id.search_second_card_humidity);
        search_second_card_wind = view.findViewById(R.id.search_second_card_wind);
        search_second_card_visibility = view.findViewById(R.id.search_second_card_visibility);
        search_second_card_pressure = view.findViewById(R.id.search_second_card_pressure);

        search_third_card_list = view.findViewById(R.id.search_third_card_list);

        fab = view.findViewById(R.id.fab);

        String myList = mPreferences.getString("list", "");
        fab.setImageResource(R.drawable.favorite_add);
        fab.setTag("add");
        if (myList.equals("")) {
        } else {
            try {
                JSONArray jsonArray = new JSONArray(myList);
                for (int i = 0; i < jsonArray.length(); i ++) {
                    String curCity = jsonArray.getString(i);
                    if (curCity.equals(cityName)) {
                        fab.setImageResource(R.drawable.favorite_remove);
                        fab.setTag("remove");
                        break;
                    } else {
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        viewPager = (ViewPager) getActivity().findViewById(R.id.mainpage);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fab.getTag().equals("add")) {
                    Toast.makeText(context, cityName + " was added to favorites", Toast.LENGTH_LONG).show();
                    fab.setImageResource(R.drawable.favorite_remove);
                    fab.setTag("remove");

                    String myList = mPreferences.getString("list", "");
                    if (myList.equals("")) {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(cityName);
                        mEditor.putString("list", jsonArray.toString());
                        mEditor.commit();
                    } else {
                        try {
                            JSONArray jsonArray = new JSONArray(myList);
                            jsonArray.put(cityName);
                            mEditor.putString("list", jsonArray.toString());
                            mEditor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    mEditor.putString(cityName+"_lat", String.valueOf(lat));
                    mEditor.commit();
                    mEditor.putString(cityName+"_lng", String.valueOf(lng));
                    mEditor.commit();

                } else {
                    Toast.makeText(context, cityName + " was removed from favorites", Toast.LENGTH_LONG).show();
                    ((MainActivity) getActivity()).removeCity();
                    fab.setImageResource(R.drawable.favorite_add);
                    fab.setTag("add");

                    String myList = mPreferences.getString("list", "");
                    if (myList.equals("")) {
                        mEditor.putString("list", "");
                        mEditor.commit();
                    } else {
                        try {
                            JSONArray newJsonArray = new JSONArray();
                            JSONArray jsonArray = new JSONArray(myList);
                            for (int i = 0; i < jsonArray.length(); i ++) {
                                String curCity = jsonArray.getString(i);
                                if (curCity.equals(cityName)) {
                                } else {
                                    newJsonArray.put(curCity);
                                }
                            }
                            mEditor.putString("list", newJsonArray.toString());
                            mEditor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    mEditor.remove(cityName+"_lat");
                    mEditor.commit();
                    mEditor.remove(cityName+"_lng");
                    mEditor.commit();
                }
            }
        });

        search_first_card_city.setText(cityName);

        requestQueue = Volley.newRequestQueue(getActivity());
        jsonParseCity(cityName);

        search_first_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTabs();
            }
        });

        return view;

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

                    String currentlyIcon = "N/A";
                    if (currentlyObj.has("icon")) {
                        currentlyIcon = currentlyObj.getString("icon");
                    }
                    String currentlyTemperatureString = "N/A";
                    if (currentlyObj.has("temperature")) {
                        int currentlyTemperature = (int) Math.round(currentlyObj.getDouble("temperature"));
                        currentlyTemperatureString = currentlyTemperature + "°F";
                    }
                    cityTemperature = currentlyTemperatureString;
                    String currentlySummary = "N/A";
                    if (currentlyObj.has("summary")) {
                        currentlySummary = currentlyObj.getString("summary");
                    }

                    String currentlyHumidityString = "N/A";
                    if (currentlyObj.has("humidity")) {
                        int currentlyHumidity = (int) Math.round(currentlyObj.getDouble("humidity") * 100);
                        currentlyHumidityString = currentlyHumidity + "%";
                    }
                    String currentlyWindSpeedString = "N/A";
                    if (currentlyObj.has("windSpeed")) {
                        double currentlyWindSpeed = currentlyObj.getDouble("windSpeed");
                        currentlyWindSpeedString = String.format("%.2f", currentlyWindSpeed) + " mph";
                    }
                    String currentlyVisibilityString = "N/A";
                    if (currentlyObj.has("visibility")) {
                        double currentlyVisibility = currentlyObj.getDouble("visibility");
                        currentlyVisibilityString = String.format("%.2f", currentlyVisibility) + " km";
                    }
                    String currentlyPressureString = "N/A";
                    if (currentlyObj.has("pressure")) {
                        double currentlyPressure = currentlyObj.getDouble("pressure");
                        currentlyPressureString = String.format("%.2f", currentlyPressure) + " mb";
                    }

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

                    DailyListAdapter adapter = new DailyListAdapter(getActivity(), R.layout.third_card_list_item_layout, dailyItems);
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

    public void goToTabs() {
        Intent myIntent = new Intent(getActivity(), TabsActivity.class);
        myIntent.putExtra("weatherJson", weatherJson.toString());
        myIntent.putExtra("cityName", cityName);
        myIntent.putExtra("cityTemperature", cityTemperature);
        startActivity(myIntent);
    }
}
