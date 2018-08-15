package com.example.android.newsapp;

public class NewsArticle {

    /** Section the article is within the news site */
    public final String mSection;

    /** Title of the news article */
    public final String mWeb;

    /** Date of the news article */
    public long mDate;

    /**URL link*/
    public final String mUrl;

    /**
     * Constructs a new news article.
     */
    public NewsArticle (String eventName, String eventTitle, long eventDate, String url) {
        mSection = eventName;
        mWeb = eventTitle;
        mDate = eventDate;
        mUrl= url;
    }

    /**
     * Returns the magnitude of the earthquake.
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Returns the location of the earthquake.
     */
    public String getWeb() {
        return mWeb;
    }

    /**
     * Returns the time of the earthquake.
     */
    public long getDate() {
        return mDate;
    }
    /**
     * Returns the url of the earthquake.
     */
    public String getUrl() {
        return mUrl;
    }
}
