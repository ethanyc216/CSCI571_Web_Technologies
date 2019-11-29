package com.ethanchen.weatherapp;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AutoCompleteApiCall {
    private static AutoCompleteApiCall mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public AutoCompleteApiCall(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized AutoCompleteApiCall getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AutoCompleteApiCall(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void make(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url = "http://csci571hw7nodejs-env.huttzh528b.us-east-2.elasticbeanstalk.com/autocomplete?city=" + query;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        AutoCompleteApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }

}
