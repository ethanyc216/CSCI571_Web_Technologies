package com.ethanchen.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.chabbal.slidingdotsplash.ViewPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class TabsActivity extends AppCompatActivity {

    private Intent intent;
    private String jsonString;
    private String cityName;
    private String cityTemperature;
    private Bundle bundle;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String title[] = {"TODAY", "WEEKLY", "PHOTOS"};
    private int icons[] = {R.drawable.tab_today_active, R.drawable.tab_weekly_active, R.drawable.tab_photo_active};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        intent = getIntent();
        jsonString = intent.getStringExtra("weatherJson");
        cityName = intent.getStringExtra("cityName");
        cityTemperature = intent.getStringExtra("cityTemperature");

        bundle = new Bundle();
        bundle.putString("weatherJson", jsonString);
        bundle.putString("cityName", cityName);

        setTitle(cityName);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.tabspage);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabLayout();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.twitter:
                //TODO
                shareTwitter(cityName, cityTemperature);
                return true;
            case android.R.id.home:
                this.finish();
                return super.onOptionsItemSelected(item);
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tab_menu, menu);
        return true;
    }

    public void shareTwitter(String city, String temp) {
        String url = "https://twitter.com/intent/tweet?text=Check Out ";
        url += city;
        url += "'s Weather! It is ";
        url += temp;
        url += "! &hashtags=CSCO571WeatherSearch";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }


    private void setupTabLayout() {
        tabLayout.getTabAt(0).setIcon(icons[0]);
        tabLayout.getTabAt(1).setIcon(icons[1]);
        tabLayout.getTabAt(2).setIcon(icons[2]);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
    }

    private void setupViewPager(ViewPager viewPager) {
        Today fragToday = new Today();
        Weekly fragWeekly = new Weekly();
        Photo fragPhoto = new Photo();
        fragToday.setArguments(bundle);
        fragWeekly.setArguments(bundle);
        fragPhoto.setArguments(bundle);

        TabPageAdapter adapter = new TabPageAdapter(getSupportFragmentManager());
        adapter.addFragment(fragToday, title[0]);
        adapter.addFragment(fragWeekly, title[1]);
        adapter.addFragment(fragPhoto, title[2]);

        viewPager.setAdapter(adapter);
    }

}
