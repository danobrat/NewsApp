package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsArticle> {

    public NewsAdapter(Context context, List<NewsArticle> news) {

        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_main, parent, false);
        }
        NewsArticle currentNews = getItem(position);
        if (currentNews != null) {


            // Create a new Date object from the time in milliseconds of the earthquake
            // Date dateObject = new Date(currentNews.getDate());

            // Find the TextView with view ID magnitude
            TextView sectionView = (TextView) listItemView.findViewById(R.id.section_name);

            sectionView.setText(currentNews.getSection());

            // Find the TextView with view ID location
            TextView webView = (TextView) listItemView.findViewById(R.id.web_title);
            // Display the location of the current earthquake in that TextView
            webView.setText(currentNews.getWeb());

            // Find the TextView with view ID date
            // TextView dateView = (TextView) listItemView.findViewById(R.id.date);
            // Display the date of the current earthquake in that TextView
            // dateView.setText(currentNews.getDate());


            // Find the TextView with view ID location
            TextView urlView = (TextView) listItemView.findViewById(R.id.url);
            // Display the location of the current earthquake in that TextView
            webView.setText(currentNews.getUrl());
        }
        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}

