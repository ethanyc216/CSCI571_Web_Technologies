package com.ethanchen.weatherapp;

import android.content.Context;
import android.support.annotation.AnimatorRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

public class DailyListAdapter extends ArrayAdapter<DailyItem> {

    private static final String TAG = "DailyListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView list_date;
        ImageView list_icon;
        TextView list_low;
        TextView list_high;
    }

    public DailyListAdapter(Context context, int resource, ArrayList<DailyItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //setuoImageLoader();

        String date = getItem(position).getDate();
        int icon = getItem(position).getIcon();
        String temperatureLow = getItem(position).getTemperatureLow();
        String temperatureHigh = getItem(position).getTemperatureHigh();

        //DailyItem dailyItem = new DailyItem(date, icon, temperatureLow, temperatureHigh);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView list_date = (TextView) convertView.findViewById(R.id.list_date);
        ImageView list_icon= (ImageView) convertView.findViewById(R.id.list_icon);
        TextView list_low= (TextView) convertView.findViewById(R.id.list_low);
        TextView list_high = (TextView) convertView.findViewById(R.id.list_high);

        list_date.setText(date);
        list_icon.setImageResource(icon);
        list_low.setText(temperatureLow);
        list_high.setText(temperatureHigh);

        return convertView;


        /*
        final View result;

        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.list_date = (TextView) convertView.findViewById(R.id.list_date);
            holder.list_icon = (ImageView) convertView.findViewById(R.id.list_icon);
            holder.list_low = (TextView) convertView.findViewById(R.id.list_low);
            holder.list_high = (TextView) convertView.findViewById(R.id.list_high);

            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext,
        //        (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        //result.startAnimation(animation);
        lastPosition = position;

        ImageLoader imageLoader = ImageLoader.getInstance();
        int defaultImage = mContext.getResources().getIdentifier("@drawable/clear_day", null, mContext.getPackageName());
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        imageLoader.displayImage(icon, holder.icon, options);

        holder.list_date.setText(date);
        holder.list_icon.setImageResource(icon);
        holder.list_low.setText(temperatureLow);
        holder.list_high.setText(temperatureHigh);

        return convertView;

         */
    }

    /*
    private  void setuoImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }
     */
}
