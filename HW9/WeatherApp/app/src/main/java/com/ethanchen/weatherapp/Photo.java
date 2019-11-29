package com.ethanchen.weatherapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Photo extends Fragment {

    private static final String TAG = "Photo";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        String weatherJsonString = getArguments().getString("weatherJson");
        String cityName = getArguments().getString("cityName");
        requestQueue = Volley.newRequestQueue(getActivity());
        jsonParse(cityName);

        /*
        ArrayList<PhotoItem> itemList = new ArrayList<>();
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));
        itemList.add(new PhotoItem(R.drawable.card_visibility, "11"));

        mRecyclerView = view.findViewById(R.id.photo_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new PhotoItemAdapter(itemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

         */

        mRecyclerView = view.findViewById(R.id.photo_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;

    }

    private void jsonParse(String cityName) {
        String url = "http://csci571hw7nodejs-env.huttzh528b.us-east-2.elasticbeanstalk.com/photo?city=" + cityName + " city image";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray itemsObj = response.getJSONArray("items");
                    ArrayList<String> urlItems = new ArrayList<>();

                    ArrayList<PhotoItem> itemList = new ArrayList<>();

                    for (int i = 0; i < itemsObj.length(); i++) {
                        JSONObject itemInfo = itemsObj.getJSONObject(i);
                        String url = itemInfo.getString("link");
                        urlItems.add(url);
                        itemList.add(new PhotoItem(R.drawable.card_visibility, url));
                    }


                    mAdapter = new PhotoItemAdapter(getActivity(), itemList);
                    mRecyclerView.setAdapter(mAdapter);


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
}
