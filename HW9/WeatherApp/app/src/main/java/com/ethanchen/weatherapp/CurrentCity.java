package com.ethanchen.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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

import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CurrentCity extends Fragment {

    private static final String TAG = "CurrentCity";

    private Map<String, String> states;

    private CardView first_card;
    private ImageView first_card_icon;
    private TextView first_card_temperature;
    private TextView first_card_summary;
    private TextView first_card_city;

    private TextView second_card_humidity;
    private TextView second_card_wind;
    private TextView second_card_visibility;
    private TextView second_card_pressure;

    private ListView third_card_list;

    private DateFormat dateFormat;

    private double lat = 0.0;
    private double lng = 0.0;
    private RequestQueue requestQueue;

    private String cityName;
    private String weatherJson;
    private String cityTemperature;


    Location gpsLoc = null, networkLoc = null;

    public static CurrentCity newInstance(String text) {
        CurrentCity f = new CurrentCity(text);
        return f;
    }

    public CurrentCity() {
    }

    public CurrentCity(String text) {
        //cityName = text;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.current_city, container, false);

        states = new HashMap<>();
        states.put("Alabama","AL");
        states.put("Alaska","AK");
        states.put("Alberta","AB");
        states.put("American Samoa","AS");
        states.put("Arizona","AZ");
        states.put("Arkansas","AR");
        states.put("Armed Forces (AE)","AE");
        states.put("Armed Forces Americas","AA");
        states.put("Armed Forces Pacific","AP");
        states.put("British Columbia","BC");
        states.put("California","CA");
        states.put("Colorado","CO");
        states.put("Connecticut","CT");
        states.put("Delaware","DE");
        states.put("District Of Columbia","DC");
        states.put("Florida","FL");
        states.put("Georgia","GA");
        states.put("Guam","GU");
        states.put("Hawaii","HI");
        states.put("Idaho","ID");
        states.put("Illinois","IL");
        states.put("Indiana","IN");
        states.put("Iowa","IA");
        states.put("Kansas","KS");
        states.put("Kentucky","KY");
        states.put("Louisiana","LA");
        states.put("Maine","ME");
        states.put("Manitoba","MB");
        states.put("Maryland","MD");
        states.put("Massachusetts","MA");
        states.put("Michigan","MI");
        states.put("Minnesota","MN");
        states.put("Mississippi","MS");
        states.put("Missouri","MO");
        states.put("Montana","MT");
        states.put("Nebraska","NE");
        states.put("Nevada","NV");
        states.put("New Brunswick","NB");
        states.put("New Hampshire","NH");
        states.put("New Jersey","NJ");
        states.put("New Mexico","NM");
        states.put("New York","NY");
        states.put("Newfoundland","NF");
        states.put("North Carolina","NC");
        states.put("North Dakota","ND");
        states.put("Northwest Territories","NT");
        states.put("Nova Scotia","NS");
        states.put("Nunavut","NU");
        states.put("Ohio","OH");
        states.put("Oklahoma","OK");
        states.put("Ontario","ON");
        states.put("Oregon","OR");
        states.put("Pennsylvania","PA");
        states.put("Prince Edward Island","PE");
        states.put("Puerto Rico","PR");
        states.put("Quebec","PQ");
        states.put("Rhode Island","RI");
        states.put("Saskatchewan","SK");
        states.put("South Carolina","SC");
        states.put("South Dakota","SD");
        states.put("Tennessee","TN");
        states.put("Texas","TX");
        states.put("Utah","UT");
        states.put("Vermont","VT");
        states.put("Virgin Islands","VI");
        states.put("Virginia","VA");
        states.put("Washington","WA");
        states.put("West Virginia","WV");
        states.put("Wisconsin","WI");
        states.put("Wyoming","WY");
        states.put("Yukon Territory","YT");

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        first_card = view.findViewById(R.id.first_card);
        first_card_icon = view.findViewById(R.id.first_card_icon);
        first_card_temperature = view.findViewById(R.id.first_card_temperature);
        first_card_summary = view.findViewById(R.id.first_card_summary);
        first_card_city = view.findViewById(R.id.first_card_city);

        second_card_humidity = view.findViewById(R.id.second_card_humidity);
        second_card_wind = view.findViewById(R.id.second_card_wind);
        second_card_visibility = view.findViewById(R.id.second_card_visibility);
        second_card_pressure = view.findViewById(R.id.second_card_pressure);

        third_card_list = view.findViewById(R.id.third_card_list);

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        networkLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        lat = gpsLoc.getLatitude();
        lng = gpsLoc.getLongitude();
        cityName = getCityFromGeo(lat, lng);
        first_card_city.setText(cityName);

        requestQueue = Volley.newRequestQueue(getActivity());
        jsonParse(lat, lng);

        first_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTabs();
            }
        });
        return view;

    }

    private String getCityFromGeo(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && addresses.size() > 0) {
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryCode();
                if (country.equals("US")) {
                    country = "USA";
                }
                if (states.containsKey(state)) {
                    return city + ", " + states.get(state) + ", " + country;
                } else {
                    return city + ", " + country;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void jsonParse(double lat, double lng) {
        String url = "http://csci571hw7nodejs-env.huttzh528b.us-east-2.elasticbeanstalk.com/weather?latitude="+String.valueOf(lat)+"&longitude="+String.valueOf(lng);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    weatherJson = response.toString();
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
                        first_card_icon.setImageResource(R.drawable.clear_night);
                    } else if (currentlyIcon.equals("rain")) {
                        first_card_icon.setImageResource(R.drawable.rain);
                    } else if (currentlyIcon.equals("sleet")) {
                        first_card_icon.setImageResource(R.drawable.sleet);
                    } else if (currentlyIcon.equals("snow")) {
                        first_card_icon.setImageResource(R.drawable.snow);
                    } else if (currentlyIcon.equals("wind")) {
                        first_card_icon.setImageResource(R.drawable.wind);
                    } else if (currentlyIcon.equals("fog")) {
                        first_card_icon.setImageResource(R.drawable.fog);
                    } else if (currentlyIcon.equals("cloudy")) {
                        first_card_icon.setImageResource(R.drawable.cloudy);
                    } else if (currentlyIcon.equals("partly-cloudy-night")) {
                        first_card_icon.setImageResource(R.drawable.partly_cloudy_night);
                    } else if (currentlyIcon.equals("partly-cloudy-day")) {
                        first_card_icon.setImageResource(R.drawable.partly_cloudy_day);
                    } else {
                        first_card_icon.setImageResource(R.drawable.clear_day);
                    }

                    first_card_temperature.setText(currentlyTemperatureString);
                    first_card_summary.setText(currentlySummary);

                    second_card_humidity.setText(currentlyHumidityString);
                    second_card_wind.setText(currentlyWindSpeedString);
                    second_card_visibility.setText(currentlyVisibilityString);
                    second_card_pressure.setText(currentlyPressureString);

                    DailyListAdapter adapter = new DailyListAdapter(getActivity(), R.layout.third_card_list_item_layout, dailyItems);
                    third_card_list.setAdapter(adapter);

                    getActivity().findViewById(R.id.progressBar_lay1).setVisibility(View.GONE);


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
        myIntent.putExtra("weatherJson", weatherJson);
        myIntent.putExtra("cityName", cityName);
        myIntent.putExtra("cityTemperature", cityTemperature);
        startActivity(myIntent);
    }
}
