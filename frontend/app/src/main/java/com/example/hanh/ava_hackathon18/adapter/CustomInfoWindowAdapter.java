package com.example.hanh.ava_hackathon18.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.hanh.ava_hackathon18.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private Context mContext;
    private  View mView;

    public  CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    public void setWindowText(Marker marker, View view) {

        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvSnipet = (TextView) view.findViewById(R.id.tvSnippet);

        String title = marker.getTitle();
        String snippet = marker.getSnippet();

        if(!title.equals("")) {
            tvTitle.setText(title);
        }

        if(!snippet.equals("")) {
            tvSnipet.setText(snippet);
        }

    }
    @Override
    public View getInfoWindow(Marker marker) {
        setWindowText(marker,mView);
        return mView;

    }

    @Override
    public View getInfoContents(Marker marker) {
        setWindowText(marker, mView);
        return mView;
    }
}
