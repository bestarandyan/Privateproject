package com.chinaLife.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class sc_SpinnerAdapter extends ArrayAdapter<String> {
    Context context;
    String[] items = new String[] {};
    public sc_SpinnerAdapter(final Context context,
            final int textViewResourceId, final String[] objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
    }
    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    android.R.layout.simple_spinner_item, parent, false);
        }
        TextView tv = (TextView) convertView
                .findViewById(android.R.id.text1);
        tv.setText(items[position]);
        tv.setTextColor(Color.BLUE);
        tv.setTextSize(14);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setPadding(10, 10, 4, 10);
        return convertView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    android.R.layout.simple_spinner_item, parent, false);
        }
        TextView tv = (TextView) convertView
                .findViewById(android.R.id.text1);
        tv.setText(items[position]);
        tv.setPadding(10, 0, 0, 0);
        tv.setTextColor(Color.BLUE);
        tv.setGravity(Gravity.LEFT);
        tv.setTextSize(13);
        return convertView;
    }
}

