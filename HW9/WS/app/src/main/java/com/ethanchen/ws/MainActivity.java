package com.ethanchen.ws;

import android.Manifest;
import android.app.SearchManager;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
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

import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private TextView locationtxt;
    private double lat = 0.0;
    private double lng = 0.0;
    private RequestQueue requestQueue;

    Location gpsLoc = null, networkLoc = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        locationtxt = findViewById(R.id.locationtxt);

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
        locationtxt.setText(city);

        requestQueue = Volley.newRequestQueue(this);

        jsonParse();
        **/


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
                String country = addresses.get(0).getCountryName();
                return country;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void jsonParse() {
        String url = "http://csci571hw7nodejs-env.huttzh528b.us-east-2.elasticbeanstalk.com/weather?latitude=40.714224&longitude=-73.961452";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject currentlyObj = response.getJSONObject("currently");

                    String currentlyIcon = currentlyObj.getString("icon");
                    int currentlyTemperature = Math.round(currentlyObj.getInt("temperature"));
                    String currentlyTemperatureString = String.valueOf(currentlyTemperature) + "°F";
                    String currentlySummary = currentlyObj.getString("summary");

                    int currentlyHumidity =  (int) Math.round(currentlyObj.getDouble("humidity") * 100);
                    String currentlyHumidityString = currentlyHumidity + "%";
                    double currentlyWindSpeed = currentlyObj.getDouble("windSpeed");
                    String currentlyWindSpeedString =  String.format("%.2f", currentlyWindSpeed) + " mph";
                    double currentlyVisibility = currentlyObj.getDouble("visibility");
                    String currentlyVisibilityString =  String.format("%.2f", currentlyVisibility) + " km";
                    double currentlyPressure = currentlyObj.getDouble("pressure");
                    String currentlyPressureString =  String.format("%.2f", currentlyPressure) + " mb";



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
    protected Boolean onCreatOptionsMenu(Menu menu)  {
        getMenuInflater().inflate(R.menu.mian_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }


}
