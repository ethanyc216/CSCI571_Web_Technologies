package com.ethanchen.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity implements CityProvider {

    private int resNum = 0;

    private ProgressBar spinner;
    private LinearLayout l_main;
    private LinearLayout progressBar_lay1;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private MainPageAdapter adapter;

    private Map<String, Integer> currentFav;

    private Map<String, String> states;
    private AutoCompleteAdapter autoCompleteAdapter;
    private Handler handler;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;

    String cityName;
    private double lat = 0.0;
    private double lng = 0.0;

    private Bundle bundle;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    String myList;

    private ArrayList<String> mEntries = new ArrayList<String>();

    Location gpsLoc = null, networkLoc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET}, 1);

        //spinner = (ProgressBar)findViewById(R.id.progressBar1);
        //l_main = findViewById(R.id.l_main);
        progressBar_lay1 = findViewById(R.id.progressBar_lay1);
        progressBar_lay1.setVisibility(View.VISIBLE);
        mEntries.add("cur");

        currentFav = new HashMap<>();
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

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        adapter = new MainPageAdapter(getSupportFragmentManager(), this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        networkLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        lat = gpsLoc.getLatitude();
        lng = gpsLoc.getLongitude();
        cityName = getCityFromGeo(lat, lng);


        viewPager = (ViewPager) findViewById(R.id.mainpage);
        tabLayout = (TabLayout) findViewById(R.id.tabDots);
        viewPager.setOffscreenPageLimit(10);
        viewPager.setAdapter(adapter);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager, true);

    }

    private void refreshViewPager(ViewPager viewPager) {
        /*

        String myList = mPreferences.getString("list", "");
        Map<String, Integer> newCurrentFav = new HashMap<>();
        if (myList.equals("")) {
        } else {
            try {
                JSONArray jsonArray = new JSONArray(myList);
                for (int i = 0; i < jsonArray.length(); i ++) {
                    String curCity = jsonArray.getString(i);
                    if (currentFav.containsKey(curCity)) {
                        newCurrentFav.put(curCity, i);
                    } else{
                        newCurrentFav.put(curCity, i);
                        //Toast.makeText(this, curCity, Toast.LENGTH_LONG).show();
                        String curLat = mPreferences.getString(curCity + "_lat", "");
                        String curLng = mPreferences.getString(curCity + "_lng", "");
                        FavoriteCity favoriteCity = new FavoriteCity();
                        bundle = new Bundle();
                        bundle.putString("cityName", curCity);
                        bundle.putString("lat", curLat);
                        bundle.putString("lng", curLng);
                        favoriteCity.setArguments(bundle);
                        adapter.addFragment(favoriteCity, curCity);
                        adapter.notifyDataSetChanged();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (Map.Entry<String,Integer> entry : currentFav.entrySet()) {
            String favName = entry.getKey();
            int favPos = entry.getValue();
            if (newCurrentFav.containsKey(favName)) {
            } else {
                adapter.notifyChangeInPosition(1);
                adapter.removeFragment(favPos+1);
                adapter.notifyDataSetChanged();
            }

        }
        //adapter.notifyDataSetChanged();
        currentFav = newCurrentFav;

         */
        String myList = mPreferences.getString("list", "");
        boolean change = false;
        if (myList.equals("")) {
        } else {
            try {
                JSONArray jsonArray = new JSONArray(myList);
                if (jsonArray.length()+1 == mEntries.size()) {
                    change = false;
                } else {
                    change = true;
                    mEntries = new ArrayList<>();
                    mEntries.add("cur");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String curCity = jsonArray.getString(i);
                        mEntries.add(curCity);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (change) {
            progressBar_lay1.setVisibility(View.VISIBLE);
            adapter.notifyChangeInPosition(1);
            adapter.notifyDataSetChanged();
        } else if (resNum > 2) {
            progressBar_lay1.setVisibility(View.GONE);
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        /*
        CurrentCity currentCity = new CurrentCity();
        bundle = new Bundle();
        currentCity.setArguments(bundle);
        adapter.addFragment(currentCity, cityName);

        String myList = mPreferences.getString("list", "");
        if (myList.equals("")) {
        } else {
            try {
                JSONArray jsonArray = new JSONArray(myList);
                for (int i = 0; i < jsonArray.length(); i ++) {
                    String curCity = jsonArray.getString(i);
                    currentFav.put(curCity, i);
                    //Toast.makeText(this, curCity, Toast.LENGTH_LONG).show();
                    String curLat = mPreferences.getString(curCity + "_lat", "");
                    String curLng = mPreferences.getString(curCity + "_lng", "");
                    FavoriteCity favoriteCity = new FavoriteCity();
                    bundle = new Bundle();
                    bundle.putString("cityName", curCity);
                    bundle.putString("lat", curLat);
                    bundle.putString("lng", curLng);
                    favoriteCity.setArguments(bundle);
                    adapter.addFragment(favoriteCity, curCity);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
        //viewPager.setAdapter(adapter);

         */
        myList = mPreferences.getString("list", "");
        if (myList.equals("")) {
        } else {
            try {
                JSONArray jsonArray = new JSONArray(myList);
                for (int i = 0; i < jsonArray.length(); i ++) {
                    String curCity = jsonArray.getString(i);
                    currentFav.put(curCity, i+1);
                    mEntries.add(curCity);
                    adapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
                //progressBar_lay1.setVisibility(View.VISIBLE);
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

    @Override
    public void onResume() {
        super.onResume();
        resNum += 1;
        progressBar_lay1.setVisibility(View.VISIBLE);
        String newmyList = mPreferences.getString("list", "");
        refreshViewPager(viewPager);
        /*
        if (myList.equals(newmyList)) {
            //progressBar_lay1.setVisibility(View.GONE);
        }
        else {
            refreshViewPager(viewPager);
            //finish();
            //startActivity(getIntent());
        }

         */
    }

    @Override
    public String getCityForPosition(int position) {
        return mEntries.get(position);
    }
    @Override
    public int getCount() {
        return mEntries.size();
    }

    public void removeCity() {
        progressBar_lay1.setVisibility(View.VISIBLE);
        int position = viewPager.getCurrentItem();
        mEntries.remove(position);
        adapter.notifyChangeInPosition(1);
        adapter.notifyDataSetChanged();
    }

}
