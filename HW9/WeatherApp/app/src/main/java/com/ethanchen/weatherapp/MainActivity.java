package com.ethanchen.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ArrayList<JSONObject> FavDataJson;
    private ArrayList<String> FavDataCity;
    private ArrayList<String> FavDataTemperature;

    private Map<String, String> states;
    private AutoCompleteAdapter autoCompleteAdapter;
    private Handler handler;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;

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


    Location gpsLoc = null, networkLoc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FavDataJson = new ArrayList<>();
        FavDataCity = new ArrayList<>();
        FavDataTemperature = new ArrayList<>();

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

        first_card_icon = findViewById(R.id.first_card_icon);
        first_card_temperature = findViewById(R.id.first_card_temperature);
        first_card_summary = findViewById(R.id.first_card_summary);
        first_card_city = findViewById(R.id.first_card_city);

        second_card_humidity = findViewById(R.id.second_card_humidity);
        second_card_wind = findViewById(R.id.second_card_wind);
        second_card_visibility = findViewById(R.id.second_card_visibility);
        second_card_pressure = findViewById(R.id.second_card_pressure);

        third_card_list = findViewById(R.id.third_card_list);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET}, 1);

        gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        networkLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        lat = gpsLoc.getLatitude();
        lng = gpsLoc.getLongitude();
        String city = getCityFromGeo(lat, lng);
        first_card_city.setText(city);
        FavDataCity.add(city);

        requestQueue = Volley.newRequestQueue(this);
        jsonParse(lat, lng);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    return;
            }
        }
    }

    private String getCityFromGeo(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && addresses.size() > 0) {
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryCode();
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
                FavDataJson.add(response);
                JSONObject currentlyObj = response.getJSONObject("currently");
                JSONObject dailyObj = response.getJSONObject("daily");

                String currentlyIcon = currentlyObj.getString("icon");
                int currentlyTemperature = (int) Math.round(currentlyObj.getDouble("temperature"));
                String currentlyTemperatureString = currentlyTemperature + "°F";
                FavDataTemperature.add(currentlyTemperatureString);
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

                DailyListAdapter adapter = new DailyListAdapter(MainActivity.this, R.layout.third_card_list_item_layout, dailyItems);
                third_card_list.setAdapter(adapter);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setBackgroundColor(Color.parseColor("#161616"));
        //searchView.setQueryHint("Search...");

        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //searchAutoComplete.setBackgroundColor(Color.BLUE);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);

        // Create a new ArrayAdapter and add data to search auto complete object.
        //String dataArr[] = {"Apple" , "Amazon" , "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
        //ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        autoCompleteAdapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setAdapter(autoCompleteAdapter);


        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //String queryString = (String) adapterView.getItemAtPosition(position);
                //searchAutoComplete.setText("" + queryString);
                //Toast.makeText(MainActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
                searchAutoComplete.setText(autoCompleteAdapter.getObject(position));
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(searchView.getQuery())) {
                        makeApiCall(searchView.getQuery().toString());
                    }
                }
                return false;
            }
        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent myIntent = new Intent(MainActivity.this, SearchResultsActivity.class);
                myIntent.putExtra("cityName", query);
                startActivity(myIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //text change filteruing
                //adapter.getFilter().filter(newText);
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,  AUTO_COMPLETE_DELAY);
                return false;
            }
        });

        return true;

    }

    private void makeApiCall(String text) {
        AutoCompleteApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray array = responseObject.getJSONArray("predictions");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("description"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoCompleteAdapter.setData(stringList);
                autoCompleteAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    public void goToTabs(View view) {
        Intent myIntent = new Intent(this, TabsActivity.class);
        //TODO
        myIntent.putExtra("weatherJson", FavDataJson.get(0).toString());
        myIntent.putExtra("cityName", FavDataCity.get(0));
        myIntent.putExtra("cityTemperature", FavDataTemperature.get(0));
        startActivity(myIntent);
    }

}
